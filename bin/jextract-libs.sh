#!/bin/bash

set -e

JEXTRACT=~/Downloads/jextract-22/bin/jextract

# -- Raylib
echo "Extracting Raylib"

cd raylib/src && \
    RAYLIB_LIBTYPE=SHARED RAYMATH_IMPLEMENTATION=TRUE make PLATFORM=PLATFORM_DESKTOP && \
    cd - && \
    cp raylib/src/libraylib.dylib bin

cp raylib/src/libraylib.dylib native

gcc -undefined dynamic_lookup \
    -shared \
    bin/vybe_raylib.c \
    -I raylib/src \
    -o native/libvybe_raylib.dylib

"$JEXTRACT" -l :/tmp/pfeodrippe_vybe_native/libraylib.dylib -l :/tmp/pfeodrippe_vybe_native/libvybe_raylib.dylib \
            -DRAYMATH_IMPLEMENTATION=TRUE \
            -DBUILD_LIBTYPE_SHARED=TRUE \
            --output src-java \
            --header-class-name raylib \
            -t org.vybe.raylib bin/vybe_raylib.c

# -- Flecs
echo "Extracting Flecs"

cp flecs/flecs.h bin/
cp flecs/flecs.c bin/

gcc -undefined dynamic_lookup \
    -std=gnu99 -Dflecs_EXPORTS -DFLECS_NDEBUG \
    -shared \
    bin/vybe_flecs.c \
    bin/flecs.c \
    -o native/libvybe_flecs.dylib

"$JEXTRACT" -l :/tmp/pfeodrippe_vybe_native/libvybe_flecs.dylib \
            --output src-java \
            --header-class-name flecs \
            -t org.vybe.flecs bin/vybe_flecs.c
