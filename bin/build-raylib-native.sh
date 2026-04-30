#!/usr/bin/env bash
set -euo pipefail
set -x

repo_root="$(cd "$(dirname "$0")/.." && pwd)"
cd "$repo_root"

VYBE_GCC_RAYLIB="raylib/src/rcore.o raylib/src/rshapes.o raylib/src/rtextures.o raylib/src/rtext.o raylib/src/utils.o raylib/src/rglfw.o raylib/src/rmodels.o raylib/src/raudio.o raylib/src/raylib.dll.rc.data -Lraylib/src raylib/src/libraylibdll.a -static-libgcc -lopengl32 -lgdi32 -lwinmm"
VYBE_TMP_PREFIX=""

unameOut="$(uname -s)"
case "${unameOut}" in
    Linux*)
        VYBE_EXTENSION=so
        __VYBE_DEFAULT_GCC_ARGS="gcc -undefined"
        VYBE_GCC_END=""
        VYBE_GCC_RAYLIB="-fPIC"
        VYBE_LIB_PREFIX="lib";;
    Darwin*)
        VYBE_EXTENSION=dylib
        __VYBE_DEFAULT_GCC_ARGS="gcc -undefined dynamic_lookup"
        VYBE_GCC_END=""
        VYBE_GCC_RAYLIB=""
        VYBE_LIB_PREFIX="lib";;
    CYGWIN*|MINGW*|MSYS_NT*)
        VYBE_EXTENSION=dll
        __VYBE_DEFAULT_GCC_ARGS="gcc -undefined"
        VYBE_GCC_END="-lws2_32"
        VYBE_LIB_PREFIX="";;
    *)
        echo "Unsupported OS for Raylib native build: ${unameOut}" >&2
        exit 1;;
esac

__VYBE_JEXTRACT_DEFAULT="$HOME/Downloads/jextract-osx/bin/jextract"
VYBE_JEXTRACT="${VYBE_JEXTRACT:-$__VYBE_JEXTRACT_DEFAULT}"
VYBE_GCC="${VYBE_GCC:-$__VYBE_DEFAULT_GCC_ARGS}"

if [[ ! -x "$VYBE_JEXTRACT" ]]; then
  echo "jextract not found or not executable: $VYBE_JEXTRACT" >&2
  exit 1
fi

mkdir -p resources/vybe/native src-java/org/vybe
rm -rf src-java/org/vybe/raylib

echo "Building Raylib native exception"
(
  cd raylib/src
  make clean
  RAYLIB_BUILD_MODE=DEBUG \
    RAYLIB_LIBTYPE=SHARED \
    RAYMATH_IMPLEMENTATION=TRUE \
    RAYGUI_IMPLEMENTATION=TRUE \
    BUILD_LIBTYPE_SHARED=TRUE \
    make PLATFORM=PLATFORM_DESKTOP
)

cp "raylib/src/${VYBE_LIB_PREFIX}raylib.$VYBE_EXTENSION" resources/vybe/native

$VYBE_GCC \
  -shared \
  -DBUILD_LIBTYPE_SHARED=TRUE \
  bin/vybe_raylib.c \
  -I raylib/src \
  -o "resources/vybe/native/${VYBE_LIB_PREFIX}vybe_raylib.$VYBE_EXTENSION" \
  $VYBE_GCC_END $VYBE_GCC_RAYLIB

if [[ $VYBE_EXTENSION == "dll" ]]; then
  "$VYBE_JEXTRACT" \
    -D_WIN32=TRUE \
    -DRAYMATH_IMPLEMENTATION=TRUE \
    -DRAYGUI_IMPLEMENTATION=TRUE \
    -DBUILD_LIBTYPE_SHARED=TRUE \
    -D_GNU_SOURCE=TRUE \
    -DPLATFORM_DESKTOP=TRUE \
    -DGRAPHICS_API_OPENGL_33=TRUE \
    -I raylib/src \
    --dump-includes .vybe-raylib-includes-original.txt \
    bin/vybe_raylib.c

  grep -e raylib.h -e rlgl.h -e raymath.h -e raygui.h .vybe-raylib-includes-original.txt > .vybe-raylib-includes.txt

  "$VYBE_JEXTRACT" @.vybe-raylib-includes.txt \
    --use-system-load-library \
    --library raylib \
    --library vybe_raylib \
    -D_WIN32=TRUE \
    -DRAYMATH_IMPLEMENTATION=TRUE \
    -DRAYGUI_IMPLEMENTATION=TRUE \
    -DBUILD_LIBTYPE_SHARED=TRUE \
    -D_GNU_SOURCE=TRUE \
    -DPLATFORM_DESKTOP=TRUE \
    -DGRAPHICS_API_OPENGL_33=TRUE \
    -I raylib/src \
    --output src-java \
    --header-class-name raylib \
    -t org.vybe.raylib bin/vybe_raylib.c
else
  "$VYBE_JEXTRACT" \
    -DRAYMATH_IMPLEMENTATION=TRUE \
    -DRAYGUI_IMPLEMENTATION=TRUE \
    -DBUILD_LIBTYPE_SHARED=TRUE \
    -D_GNU_SOURCE=TRUE \
    -DPLATFORM_DESKTOP=TRUE \
    -DGRAPHICS_API_OPENGL_33=TRUE \
    -I raylib/src \
    --dump-includes .vybe-raylib-includes-original.txt \
    bin/vybe_raylib.c

  grep -e raylib.h -e rlgl.h -e raymath.h -e raygui.h .vybe-raylib-includes-original.txt > .vybe-raylib-includes.txt

  "$VYBE_JEXTRACT" @.vybe-raylib-includes.txt \
    --use-system-load-library \
    --library raylib \
    --library vybe_raylib \
    -DRAYMATH_IMPLEMENTATION=TRUE \
    -DRAYGUI_IMPLEMENTATION=TRUE \
    -DBUILD_LIBTYPE_SHARED=TRUE \
    -D_GNU_SOURCE=TRUE \
    -DPLATFORM_DESKTOP=TRUE \
    -DGRAPHICS_API_OPENGL_33=TRUE \
    -I raylib/src \
    --output src-java \
    --header-class-name raylib \
    -t org.vybe.raylib bin/vybe_raylib.c
fi

ls -lh resources/vybe/native | grep -E 'raylib|vybe_raylib|keep' || true
