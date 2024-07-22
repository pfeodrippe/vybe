#!/bin/bash

set -ex

unameOut="$(uname -s)"
case "${unameOut}" in
    Linux*)     VYBE_EXTENSION=so;    __VYBE_DEFAULT_GCC_ARGS="gcc -undefined";                VYBE_GCC_FLECS_OPTS="-fPIC";;
    Darwin*)    VYBE_EXTENSION=dylib; __VYBE_DEFAULT_GCC_ARGS="gcc -undefined dynamic_lookup"; VYBE_GCC_FLECS_OPTS="";;
    CYGWIN*)    VYBE_EXTENSION=dll;   __VYBE_DEFAULT_GCC_ARGS="gcc -undefined dynamic_lookup"; VYBE_GCC_FLECS_OPTS="";;
    MINGW*)     VYBE_EXTENSION=dll;   __VYBE_DEFAULT_GCC_ARGS="gcc -undefined dynamic_lookup"; VYBE_GCC_FLECS_OPTS="";;
    MSYS_NT*)   VYBE_EXTENSION=dll;   __VYBE_DEFAULT_GCC_ARGS="gcc -undefined dynamic_lookup"; VYBE_GCC_FLECS_OPTS="";;
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
    -std=gnu99 "$VYBE_GCC_FLECS_OPTS" -Dflecs_EXPORTS -DFLECS_NDEBUG -DFLECS_KEEP_ASSERT -DFLECS_SOFT_ASSERT \
    -shared \
    bin/vybe_flecs.c \
    bin/flecs.c \
    -o "native/libvybe_flecs.$VYBE_EXTENSION"

$VYBE_JEXTRACT \
    -l ":/tmp/pfeodrippe_vybe_native/libvybe_flecs.$VYBE_EXTENSION" \
    --output src-java \
    --header-class-name flecs \
    -t org.vybe.flecs bin/vybe_flecs.c

# -- Raylib
echo "Extracting Raylib"

cd raylib/src && \
    make clean && \
    RAYLIB_LIBTYPE=SHARED RAYMATH_IMPLEMENTATION=TRUE make PLATFORM=PLATFORM_DESKTOP && \
    cd - && \
    cp "raylib/src/libraylib.$VYBE_EXTENSION" native

$VYBE_GCC \
    -shared \
    bin/vybe_raylib.c \
    -I raylib/src \
    -o "native/libvybe_raylib.$VYBE_EXTENSION"

$VYBE_JEXTRACT \
    -l ":/tmp/pfeodrippe_vybe_native/libraylib.$VYBE_EXTENSION" \
    -l ":/tmp/pfeodrippe_vybe_native/libvybe_raylib.$VYBE_EXTENSION" \
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
    cp "zig-gamedev/libs/zphysics/zig-out/lib/libjoltc.$VYBE_EXTENSION" "native/libjoltc_zig.$VYBE_EXTENSION"

$VYBE_GCC \
    -shared \
    bin/vybe_jolt.c \
    -I zig-gamedev/libs/zphysics/libs/JoltC \
    -o "native/libvybe_jolt.$VYBE_EXTENSION"

$VYBE_JEXTRACT \
    -l ":/tmp/pfeodrippe_vybe_native/libjoltc_zig.$VYBE_EXTENSION" \
    -l ":/tmp/pfeodrippe_vybe_native/libvybe_jolt.$VYBE_EXTENSION" \
    --output src-java \
    --header-class-name jolt \
    -t org.vybe.jolt bin/vybe_jolt.c
