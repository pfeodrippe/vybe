#!/bin/bash

set -ex

# WINDOWS OPTIONS
VYBE_GCC_RAYLIB="raylib/src/rcore.o raylib/src/rshapes.o raylib/src/rtextures.o raylib/src/rtext.o raylib/src/utils.o raylib/src/rglfw.o raylib/src/rmodels.o raylib/src/raudio.o raylib/src/raylib.dll.rc.data -Lraylib/src raylib/src/libraylibdll.a -static-libgcc -lopengl32 -lgdi32 -lwinmm"

VYBE_GCC_JOLT="-Wl,--out-implib,JoltPhysics/Build/VS2022_CL/Distribution/Jolt.lib"
VYBE_JOLT_EXTENSION="lib"

VYBE_ZIG_BUILD="zig build"

VYBE_TMP_PREFIX=""
# END OF WINDOWS OPTIONS

unameOut="$(uname -s)"
case "${unameOut}" in
    Linux*)
        VYBE_EXTENSION=so;
        __VYBE_DEFAULT_GCC_ARGS="gcc -undefined";
        VYBE_GCC_FLECS_OPTS="-std=gnu99 -fPIC";
        VYBE_GCC_END="";
        VYBE_GCC_RAYLIB="";
        VYBE_GCC_JOLT="";
        VYBE_JOLT_EXTENSION="so";
        VYBE_ZIG_BUILD="zig build";
        VYBE_TMP_PREFIX="";
        VYBE_SODIUM_LIB="libsodium.so.26.2.0";
        VYBE_LIB_PREFIX="lib";;
    Darwin*)
        VYBE_EXTENSION=dylib;
        __VYBE_DEFAULT_GCC_ARGS="gcc -undefined dynamic_lookup";
        VYBE_GCC_FLECS_OPTS="-std=gnu99";
        VYBE_GCC_END="";
        VYBE_GCC_RAYLIB="";
        VYBE_GCC_JOLT="";
        VYBE_JOLT_EXTENSION="dylib";
        VYBE_ZIG_BUILD="zig build";
        VYBE_TMP_PREFIX="";
        VYBE_SODIUM_LIB="libsodium.26.dylib";
        VYBE_LIB_PREFIX="lib";;
    CYGWIN*)
        VYBE_EXTENSION=dll;
        __VYBE_DEFAULT_GCC_ARGS="gcc -undefined";
        VYBE_GCC_FLECS_OPTS="-std=gnu99";
        VYBE_GCC_END="-lws2_32";
        VYBE_LIB_PREFIX="";;
    MINGW*)
        VYBE_EXTENSION=dll;
        __VYBE_DEFAULT_GCC_ARGS="gcc -undefined";
        VYBE_GCC_FLECS_OPTS="-std=gnu99";
        VYBE_GCC_END="-lws2_32";
        VYBE_LIB_PREFIX="";;
    MSYS_NT*)
        VYBE_EXTENSION=dll;
        __VYBE_DEFAULT_GCC_ARGS="gcc -undefined";
        VYBE_GCC_FLECS_OPTS="-std=gnu99";
        VYBE_GCC_END="-lws2_32";
        VYBE_LIB_PREFIX="";;
    *)
        VYBE_EXTENSION="UNKNOWN:${unameOut}"
esac

__VYBE_JEXTRACT_DEFAULT=~/Downloads/jextract-osx/bin/jextract

VYBE_JEXTRACT="${VYBE_JEXTRACT:-$__VYBE_JEXTRACT_DEFAULT}"
VYBE_GCC="${VYBE_GCC:-$__VYBE_DEFAULT_GCC_ARGS}"

rm -rf src-java/org/vybe/jolt
rm -rf src-java/org/vybe/flecs
rm -rf src-java/org/vybe/raylib
rm -rf src-java/org/vybe/netcode

# -- Netcode
echo "Extracting Netcode"

if [ ! -d "libsodium-1.0.20" ]; then
    curl -o sodium.tar.gz https://download.libsodium.org/libsodium/releases/libsodium-1.0.20.tar.gz
    tar -xf sodium.tar.gz
    cd libsodium-1.0.20
    ./configure
    make && make check
    sudo make install
    cd -

    ls -lh /usr/local/lib

    cp "/usr/local/lib/${VYBE_SODIUM_LIB}" "native/${VYBE_LIB_PREFIX}netcode.$VYBE_EXTENSION"
fi

if [[ $VYBE_EXTENSION == "dll" ]]; then
    $VYBE_JEXTRACT \
        --use-system-load-library \
        --library netcode \
        --output src-java \
        --header-class-name netcode \
        -t org.vybe.netcode netcode/netcode.h
else
    $VYBE_JEXTRACT \
        -l ":${VYBE_TMP_PREFIX}/tmp/pfeodrippe_vybe_native/${VYBE_LIB_PREFIX}netcode.$VYBE_EXTENSION" \
        --output src-java \
        --header-class-name netcode \
        -t org.vybe.netcode netcode/netcode.h
fi

# -- Jolt Physics
echo "Extracting Jolt Physics"

if [[ $VYBE_EXTENSION == "dll" ]]; then
    $VYBE_JEXTRACT \
        --use-system-load-library \
        --library joltc_zig \
        --output src-java \
        --header-class-name jolt \
        -t org.vybe.jolt bin/vybe_jolt.c
else
    cd zig-gamedev/libs/zphysics && \
        $VYBE_ZIG_BUILD && \
        cd - && \
        ls zig-gamedev/libs/zphysics/zig-out/lib && \
        cp "zig-gamedev/libs/zphysics/zig-out/lib/${VYBE_LIB_PREFIX}joltc.$VYBE_JOLT_EXTENSION" "native/${VYBE_LIB_PREFIX}joltc_zig.$VYBE_JOLT_EXTENSION"

    $VYBE_GCC \
        -shared \
        bin/vybe_jolt.c \
        -I zig-gamedev/libs/zphysics/libs/JoltC \
        -o "native/${VYBE_LIB_PREFIX}vybe_jolt.$VYBE_EXTENSION" $VYBE_GCC_JOLT

    $VYBE_JEXTRACT \
        -l ":/tmp/pfeodrippe_vybe_native/${VYBE_LIB_PREFIX}joltc_zig.$VYBE_EXTENSION" \
        -l ":/tmp/pfeodrippe_vybe_native/${VYBE_LIB_PREFIX}vybe_jolt.$VYBE_EXTENSION" \
        --output src-java \
        --header-class-name jolt \
        -t org.vybe.jolt bin/vybe_jolt.c
fi

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

if [[ $VYBE_EXTENSION == "dll" ]]; then
    $VYBE_JEXTRACT \
        --use-system-load-library \
        --library vybe_flecs \
        --output src-java \
        --header-class-name flecs \
        -t org.vybe.flecs bin/vybe_flecs.c
else
    $VYBE_JEXTRACT \
        -l ":${VYBE_TMP_PREFIX}/tmp/pfeodrippe_vybe_native/${VYBE_LIB_PREFIX}vybe_flecs.$VYBE_EXTENSION" \
        --output src-java \
        --header-class-name flecs \
        -t org.vybe.flecs bin/vybe_flecs.c
fi

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

if [[ $VYBE_EXTENSION == "dll" ]]; then
    $VYBE_JEXTRACT \
        --use-system-load-library \
        --library raylib \
        --library vybe_raylib \
        -D_WIN32=TRUE \
        -DRAYMATH_IMPLEMENTATION=TRUE \
        -DBUILD_LIBTYPE_SHARED=TRUE \
        --output src-java \
        --header-class-name raylib \
        -t org.vybe.raylib bin/vybe_raylib.c
else
    $VYBE_JEXTRACT \
        -l ":${VYBE_TMP_PREFIX}/tmp/pfeodrippe_vybe_native/${VYBE_LIB_PREFIX}raylib.$VYBE_EXTENSION" \
        -l ":${VYBE_TMP_PREFIX}/tmp/pfeodrippe_vybe_native/${VYBE_LIB_PREFIX}vybe_raylib.$VYBE_EXTENSION" \
        -DRAYMATH_IMPLEMENTATION=TRUE \
        -DBUILD_LIBTYPE_SHARED=TRUE \
        --output src-java \
        --header-class-name raylib \
        -t org.vybe.raylib bin/vybe_raylib.c
fi

ls -lh native
