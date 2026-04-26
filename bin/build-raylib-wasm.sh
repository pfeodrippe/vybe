#!/usr/bin/env bash
set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
BUILD="$ROOT/target/raylib-wasm"
OUT_DIR="$ROOT/resources/vybe/wasm"
OUT="$OUT_DIR/raylib.wasm"

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
  -Werror=pointer-arith
  -Wno-extern-initializer
  -Wno-implicit-const-int-float-conversion
  -Wno-missing-braces
  -Wno-pointer-sign
  -Wno-static-in-inline
  -Wno-unused-but-set-variable
  -Wno-unused-function
  -Wno-unused-variable
  -fno-strict-aliasing
  -std=gnu99
  -O3
  -flto
)

sources=(rcore.c rshapes.c rtextures.c rtext.c utils.c rmodels.c)

for src in "${sources[@]}"; do
  emcc -c "$ROOT/raylib/src/$src" \
    "${common_flags[@]}" \
    -o "$BUILD/${src%.c}.o"
done

emcc -c "$ROOT/bin/vybe_raylib_extra.c" \
  "${common_flags[@]}" \
  -o "$BUILD/vybe_raylib_extra.o"

emcc "$BUILD"/*.o \
  -o "$OUT" \
  --no-entry \
  -s STANDALONE_WASM=1 \
  -s ALLOW_MEMORY_GROWTH=1 \
  -s INITIAL_MEMORY=64MB \
  -s ERROR_ON_UNDEFINED_SYMBOLS=0 \
  -s WARN_ON_UNDEFINED_SYMBOLS=0 \
  -s DISABLE_EXCEPTION_THROWING=1 \
  -s ASSERTIONS=0 \
  -flto \
  -Wl,--export-all \
  -Wl,--no-gc-sections

(cd "$ROOT" && clj -M "bin/generate-wasm-abi.clj" "bin/raylib-wasm-abi.edn" >/dev/null)
(cd "$ROOT" && clj -M "bin/generate-wasm-wrappers.clj" "bin/raylib-wasm-wrappers.edn" >/dev/null)

emcc -c "$BUILD/vybe_raylib_wasm_wrappers.c" \
  "${common_flags[@]}" \
  -o "$BUILD/vybe_raylib_wasm_wrappers.o"

emcc "$BUILD"/*.o \
  -o "$OUT" \
  --no-entry \
  -s STANDALONE_WASM=1 \
  -s ALLOW_MEMORY_GROWTH=1 \
  -s INITIAL_MEMORY=64MB \
  -s ERROR_ON_UNDEFINED_SYMBOLS=0 \
  -s WARN_ON_UNDEFINED_SYMBOLS=0 \
  -s DISABLE_EXCEPTION_THROWING=1 \
  -s ASSERTIONS=0 \
  -flto \
  -Wl,--export-all \
  -Wl,--no-gc-sections

(cd "$ROOT" && clj -M "bin/generate-wasm-abi.clj" "bin/raylib-wasm-abi.edn" >/dev/null)

ls -lh "$OUT"
