#!/bin/bash

set -ex

# WINDOWS OPTIONS
VYBE_GCC_RAYLIB="raylib/src/rcore.o raylib/src/rshapes.o raylib/src/rtextures.o raylib/src/rtext.o raylib/src/utils.o raylib/src/rglfw.o raylib/src/rmodels.o raylib/src/raudio.o raylib/src/raygui.o raylib/src/raylib.dll.rc.data -Lraylib/src raylib/src/libraylibdll.a -static-libgcc -lopengl32 -lgdi32 -lwinmm"

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
rm -rf src-java/org/vybe/imgui

# -- ImGUI
echo "Extracting ImGUI "

# As the generated java code is huge by default because of some transitive libs,
# we have to filter it. So we do a jextract dump.
$VYBE_JEXTRACT \
    -DCIMGUI_DEFINE_ENUMS_AND_STRUCTS=1 \
    --dump-includes .vybe-imgui-includes-original.txt \
    cimgui/cimgui.h

grep -e imgui.h .vybe-imgui-includes-original.txt > .vybe-imgui-includes.txt

if [[ $VYBE_EXTENSION == "dll" ]]; then
    # The DLL file is in `build/Debug` instead of `build` for Windows
    mkdir -p cimgui/build && \
        cd cimgui/build && \
        cmake .. && \
        cmake --build . && \
        cd - && \
        cp "cimgui/build/Debug/cimgui.$VYBE_EXTENSION" "native/${VYBE_LIB_PREFIX}cimgui.$VYBE_EXTENSION"

    $VYBE_JEXTRACT @.vybe-imgui-includes.txt \
        -DCIMGUI_DEFINE_ENUMS_AND_STRUCTS=1 \
        --use-system-load-library \
        --library cimgui \
        --output src-java \
        --header-class-name imgui \
        -t org.vybe.imgui cimgui/cimgui.h
else
    mkdir -p cimgui/build && \
        cd cimgui/build && \
        cmake .. && \
        cmake --build . && \
        cd - && \
        cp "cimgui/build/cimgui.$VYBE_EXTENSION" "native/${VYBE_LIB_PREFIX}cimgui.$VYBE_EXTENSION"

    $VYBE_JEXTRACT @.vybe-imgui-includes.txt \
        -l ":${VYBE_TMP_PREFIX}/tmp/pfeodrippe_vybe_native/${VYBE_LIB_PREFIX}cimgui.$VYBE_EXTENSION" \
        -DCIMGUI_DEFINE_ENUMS_AND_STRUCTS=1 \
        --output src-java \
        --header-class-name imgui \
        -t org.vybe.imgui cimgui/cimgui.h
fi

# -- Netcode and Cute net
echo "Extracting Netcode and Cute net (network libraries)"

# ---- Cute net (network library)
echo "Extracting cute net"

$VYBE_GCC \
    $VYBE_GCC_FLECS_OPTS \
    -shared \
    bin/vybe_cutenet.c \
    -I cute_headers \
    -o "native/${VYBE_LIB_PREFIX}vybe_cutenet.$VYBE_EXTENSION" $VYBE_GCC_END

if [ ! -d "libsodium-1.0.20" ]; then
    if [[ $VYBE_EXTENSION != "dll" ]]; then

        curl -o sodium.tar.gz https://download.libsodium.org/libsodium/releases/libsodium-1.0.20.tar.gz
        tar -xf sodium.tar.gz
        cd libsodium-1.0.20
        ./configure
        make && make check
        sudo make install
        cd -

        ls -lh /usr/local/lib

        cp "/usr/local/lib/${VYBE_SODIUM_LIB}" "native/${VYBE_LIB_PREFIX}sodium.$VYBE_EXTENSION"

    else

        curl -o sodium.tar.gz https://download.libsodium.org/libsodium/releases/libsodium-1.0.20-stable-mingw.tar.gz
        tar -xf sodium.tar.gz
        cp "libsodium-win64/bin/libsodium-26.dll" "native/sodium.dll"

    fi
fi

# As the generated java code is huge by default because of some transitive libs,
# we have to filter it. So we do a jextract dump.
$VYBE_JEXTRACT \
    --dump-includes .vybe-netcode-includes-original.txt \
    bin/vybe_cutenet.c

grep -e netcode.h -e cute_net.h .vybe-netcode-includes-original.txt > .vybe-netcode-includes.txt

if [[ $VYBE_EXTENSION == "dll" ]]; then
    $VYBE_GCC \
        $VYBE_GCC_FLECS_OPTS \
        -shared \
        netcode/netcode.c \
        -I netcode \
        -I libsodium-win64/include \
        -o "native/${VYBE_LIB_PREFIX}netcode.$VYBE_EXTENSION" -L libsodium-win64/lib libsodium-win64/lib/libsodium.a -static-libgcc $VYBE_GCC_END

    $VYBE_JEXTRACT @.vybe-netcode-includes.txt \
        --use-system-load-library \
        --library vybe_cutenet \
        --library sodium \
        --library netcode \
        --output src-java \
        --header-class-name netcode \
        -t org.vybe.netcode bin/vybe_cutenet.c
else
    $VYBE_GCC \
        $VYBE_GCC_FLECS_OPTS \
        -shared \
        netcode/netcode.c \
        -I netcode \
        -o "native/${VYBE_LIB_PREFIX}netcode.$VYBE_EXTENSION"

    $VYBE_JEXTRACT @.vybe-netcode-includes.txt \
        -l ":${VYBE_TMP_PREFIX}/tmp/pfeodrippe_vybe_native/${VYBE_LIB_PREFIX}vybe_cutenet.$VYBE_EXTENSION" \
        -l ":${VYBE_TMP_PREFIX}/tmp/pfeodrippe_vybe_native/${VYBE_LIB_PREFIX}sodium.$VYBE_EXTENSION" \
        -l ":${VYBE_TMP_PREFIX}/tmp/pfeodrippe_vybe_native/${VYBE_LIB_PREFIX}netcode.$VYBE_EXTENSION" \
        --output src-java \
        --header-class-name netcode \
        -t org.vybe.netcode bin/vybe_cutenet.c
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
    RAYLIB_LIBTYPE=SHARED RAYMATH_IMPLEMENTATION=TRUE make PLATFORM=PLATFORM_DESKTOP RAYLIB_MODULE_RAYGUI=TRUE && \
    cd - && \
    cp "raylib/src/${VYBE_LIB_PREFIX}raylib.$VYBE_EXTENSION" native

$VYBE_GCC \
    -shared \
    bin/vybe_raylib.c \
    -I raylib/src \
    -I raygui/src \
    -o "native/${VYBE_LIB_PREFIX}vybe_raylib.$VYBE_EXTENSION" $VYBE_GCC_END $VYBE_GCC_RAYLIB

if [[ $VYBE_EXTENSION == "dll" ]]; then
    # As the generated java code is huge by default because of some transitive libs,
    # we have to filter it. So we do a jextract dump.
    $VYBE_JEXTRACT \
        -D_WIN32=TRUE \
        -DRAYMATH_IMPLEMENTATION=TRUE \
        -DBUILD_LIBTYPE_SHARED=TRUE \
        -I raygui/src \
        -I raylib/src \
        --dump-includes .vybe-raylib-includes-original.txt \
        bin/vybe_raylib.c

    grep -e raylib.h -e rlgl.h -e raymath.h -e raygui.h .vybe-raylib-includes-original.txt > .vybe-raylib-includes.txt

    $VYBE_JEXTRACT @.vybe-raylib-includes.txt \
        --use-system-load-library \
        --library raylib \
        --library vybe_raylib \
        -D_WIN32=TRUE \
        -DRAYMATH_IMPLEMENTATION=TRUE \
        -DBUILD_LIBTYPE_SHARED=TRUE \
        -I raygui/src \
        -I raylib/src \
        --output src-java \
        --header-class-name raylib \
        -t org.vybe.raylib bin/vybe_raylib.c
else
    # As the generated java code is huge by default because of some transitive libs,
    # we have to filter it. So we do a jextract dump.
    $VYBE_JEXTRACT \
        -DRAYMATH_IMPLEMENTATION=TRUE \
        -DBUILD_LIBTYPE_SHARED=TRUE \
        -I raygui/src \
        -I raylib/src \
        --dump-includes .vybe-raylib-includes-original.txt \
        bin/vybe_raylib.c

    grep -e raylib.h -e rlgl.h -e raymath.h -e raygui.h .vybe-raylib-includes-original.txt > .vybe-raylib-includes.txt

    $VYBE_JEXTRACT @.vybe-raylib-includes.txt \
        -l ":${VYBE_TMP_PREFIX}/tmp/pfeodrippe_vybe_native/${VYBE_LIB_PREFIX}raylib.$VYBE_EXTENSION" \
        -l ":${VYBE_TMP_PREFIX}/tmp/pfeodrippe_vybe_native/${VYBE_LIB_PREFIX}vybe_raylib.$VYBE_EXTENSION" \
        -DRAYMATH_IMPLEMENTATION=TRUE \
        -DBUILD_LIBTYPE_SHARED=TRUE \
        -I raygui/src \
        -I raylib/src \
        --output src-java \
        --header-class-name raylib \
        -t org.vybe.raylib bin/vybe_raylib.c
fi

ls -lh native
