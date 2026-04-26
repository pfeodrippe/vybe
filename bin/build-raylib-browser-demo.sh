#!/usr/bin/env bash
set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
BUILD="$ROOT/target/raylib-browser-demo"
OUT_DIR="$ROOT/resources/vybe/wasm/browser-demo"
OUT_HTML="$OUT_DIR/raylib-demo.html"

rm -rf "$BUILD"
mkdir -p "$BUILD" "$OUT_DIR"

common_flags=(
  -I"$ROOT/raylib/src"
  -I"$ROOT/raylib/src/external/glfw/include"
  -D_GNU_SOURCE
  -DPLATFORM_WEB
  -DGRAPHICS_API_OPENGL_ES2
  -DBUILD_LIBTYPE_SHARED
  -DNDEBUG
  -Wall
  -Wno-extern-initializer
  -Wno-implicit-const-int-float-conversion
  -Wno-missing-braces
  -Wno-pointer-sign
  -Wno-unused-function
  -Wno-unused-variable
  -fno-strict-aliasing
  -std=gnu99
  -O3
)

sources=(rcore.c rshapes.c rtextures.c rtext.c utils.c rmodels.c)
for src in "${sources[@]}"; do
  emcc -c "$ROOT/raylib/src/$src" "${common_flags[@]}" -o "$BUILD/${src%.c}.o"
done

emcc -c "$ROOT/bin/vybe_raylib_browser_demo.c" "${common_flags[@]}" -o "$BUILD/vybe_raylib_browser_demo.o"

emcc "$BUILD"/*.o \
  -o "$OUT_HTML" \
  -s USE_GLFW=3 \
  -s ASYNCIFY=1 \
  -s ALLOW_MEMORY_GROWTH=1 \
  -s INITIAL_MEMORY=64MB \
  -s DISABLE_EXCEPTION_THROWING=1 \
  -s ASSERTIONS=0 \
  -s MIN_WEBGL_VERSION=2 \
  -s MAX_WEBGL_VERSION=2 \
  -s GL_ENABLE_GET_PROC_ADDRESS=1 \
  --shell-file "$ROOT/bin/raylib-browser-shell.html" \

ls -lh "$OUT_DIR"/raylib-demo.*
