#!/bin/bash

set -ex

unameOut="$(uname -s)"
case "${unameOut}" in
    Linux*)     VYBE_EXTENSION=so;    __VYBE_DEFAULT_GCC_ARGS="gcc -undefined";                VYBE_GCC_FLECS_OPTS="-std=gnu99 -fPIC"; VYBE_GCC_END="";         VYBE_GCC_RAYLIB="";           VYBE_LIB_PREFIX="lib";;
    Darwin*)    VYBE_EXTENSION=dylib; __VYBE_DEFAULT_GCC_ARGS="gcc -undefined dynamic_lookup"; VYBE_GCC_FLECS_OPTS="-std=gnu99";       VYBE_GCC_END="";         VYBE_GCC_RAYLIB="";           VYBE_LIB_PREFIX="lib";;
    CYGWIN*)    VYBE_EXTENSION=dll;   __VYBE_DEFAULT_GCC_ARGS="gcc -undefined";                VYBE_GCC_FLECS_OPTS="-std=gnu99";       VYBE_GCC_END="-lws2_32"; VYBE_GCC_RAYLIB="-static-libgcc -lopengl32 -lgdi32 -lwinmm"; VYBE_LIB_PREFIX="";;
    MINGW*)     VYBE_EXTENSION=dll;   __VYBE_DEFAULT_GCC_ARGS="gcc -undefined";                VYBE_GCC_FLECS_OPTS="-std=gnu99";       VYBE_GCC_END="-lws2_32"; VYBE_GCC_RAYLIB="-static-libgcc -lopengl32 -lgdi32 -lwinmm"; VYBE_LIB_PREFIX="";;
    MSYS_NT*)   VYBE_EXTENSION=dll;   __VYBE_DEFAULT_GCC_ARGS="gcc -undefined";                VYBE_GCC_FLECS_OPTS="-std=gnu99";       VYBE_GCC_END="-lws2_32"; VYBE_GCC_RAYLIB="-static-libgcc -lopengl32 -lgdi32 -lwinmm"; VYBE_LIB_PREFIX="";;
    *)          VYBE_EXTENSION="UNKNOWN:${unameOut}"
esac

__VYBE_JEXTRACT_DEFAULT=~/Downloads/jextract-osx/bin/jextract

VYBE_JEXTRACT="${VYBE_JEXTRACT:-$__VYBE_JEXTRACT_DEFAULT}"
VYBE_GCC="${VYBE_GCC:-$__VYBE_DEFAULT_GCC_ARGS}"

rm -rf src-java/org/vybe/jolt
rm -rf src-java/org/vybe/flecs
rm -rf src-java/org/vybe/raylib
rm native/*
touch native/keep

# -- Flecs
echo "Extracting Flecs"

cp flecs/flecs.h bin/
cp flecs/flecs.c bin/

$VYBE_GCC \
    $VYBE_GCC_FLECS_OPTS -Dflecs_EXPORTS -DFLECS_NDEBUG -DFLECS_KEEP_ASSERT -DFLECS_SOFT_ASSERT \
    -shared \
    bin/vybe_flecs.c \
    bin/flecs.c \
    -o "native/${VYBE_LIB_PREFIX}vybe_flecs.$VYBE_EXTENSION" $VYBE_GCC_END

$VYBE_JEXTRACT \
    -l ":/tmp/pfeodrippe_vybe_native/${VYBE_LIB_PREFIX}vybe_flecs.$VYBE_EXTENSION" \
    --output src-java \
    --header-class-name flecs \
    -t org.vybe.flecs bin/vybe_flecs.c

# -- Raylib
echo "Extracting Raylib"

cd raylib/src && \
    make clean && \
    RAYLIB_LIBTYPE=SHARED RAYMATH_IMPLEMENTATION=TRUE make PLATFORM=PLATFORM_DESKTOP && \
    cd - && \
    cp "raylib/src/${VYBE_LIB_PREFIX}raylib.$VYBE_EXTENSION" native

$VYBE_GCC \
    -shared \
    bin/vybe_raylib.c \
    -I raylib/src \
    -o "native/${VYBE_LIB_PREFIX}vybe_raylib.$VYBE_EXTENSION" $VYBE_GCC_END $VYBE_GCC_RAYLIB

$VYBE_JEXTRACT \
    -l ":/tmp/pfeodrippe_vybe_native/${VYBE_LIB_PREFIX}raylib.$VYBE_EXTENSION" \
    -l ":/tmp/pfeodrippe_vybe_native/${VYBE_LIB_PREFIX}vybe_raylib.$VYBE_EXTENSION" \
    -DRAYMATH_IMPLEMENTATION=TRUE \
    -DBUILD_LIBTYPE_SHARED=TRUE \
    --output src-java \
    --header-class-name raylib \
    -t org.vybe.raylib bin/vybe_raylib.c

# -- Jolt Physics
echo "Extracting Jolt Physics"

cd zig-gamedev/libs/zphysics && \
    zig build && \
    cd - && \
    cp "zig-gamedev/libs/zphysics/zig-out/lib/${VYBE_LIB_PREFIX}joltc.$VYBE_EXTENSION" "native/${VYBE_LIB_PREFIX}joltc_zig.$VYBE_EXTENSION"

$VYBE_GCC \
    -shared \
    bin/vybe_jolt.c \
    -I zig-gamedev/libs/zphysics/libs/JoltC \
    -o "native/${VYBE_LIB_PREFIX}vybe_jolt.$VYBE_EXTENSION"

$VYBE_JEXTRACT \
    -l ":/tmp/pfeodrippe_vybe_native/${VYBE_LIB_PREFIX}joltc_zig.$VYBE_EXTENSION" \
    -l ":/tmp/pfeodrippe_vybe_native/${VYBE_LIB_PREFIX}vybe_jolt.$VYBE_EXTENSION" \
    --output src-java \
    --header-class-name jolt \
    -t org.vybe.jolt bin/vybe_jolt.c
