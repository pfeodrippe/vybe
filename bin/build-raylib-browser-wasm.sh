#!/usr/bin/env bash
set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
BUILD="$ROOT/target/raylib-browser-wasm"
OUT_DIR="$ROOT/resources/vybe/wasm/browser"
OUT_JS="$OUT_DIR/raylib.js"

rm -rf "$BUILD"
mkdir -p "$BUILD" "$OUT_DIR"

common_flags=(
  -I"$ROOT/raylib/src"
  -I"$ROOT/raylib/src/external/glfw/include"
  -D_GNU_SOURCE
  -DPLATFORM_WEB
  -DGRAPHICS_API_OPENGL_ES3
  -DBUILD_LIBTYPE_SHARED
  -DSUPPORT_MODULE_RAUDIO
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
)

sources=(rcore.c rshapes.c rtextures.c rtext.c utils.c rmodels.c raudio.c)

for src in "${sources[@]}"; do
  emcc -c "$ROOT/raylib/src/$src" \
    "${common_flags[@]}" \
    -o "$BUILD/${src%.c}.o"
done

emcc -c "$ROOT/bin/vybe_raylib_extra.c" \
  "${common_flags[@]}" \
  -o "$BUILD/vybe_raylib_extra.o"

(cd "$ROOT" && clj -M "bin/generate-wasm-abi.clj" "bin/raylib-wasm-abi.edn" >/dev/null)
(cd "$ROOT" && clj -M "bin/generate-wasm-wrappers.clj" "bin/raylib-wasm-wrappers.edn" >/dev/null)

emcc -c "$ROOT/target/raylib-wasm/vybe_raylib_wasm_wrappers.c" \
  "${common_flags[@]}" \
  -o "$BUILD/vybe_raylib_wasm_wrappers.o"

exported_functions="$(
  cd "$ROOT"
  clj -M -e '
    (require (quote clojure.string))
    (let [source (slurp "target/raylib-wasm/vybe_raylib_wasm_wrappers.c")
          names (->> (re-seq #"(?m)^[A-Za-z_][A-Za-z0-9_ *]+\s+(vybe_raylib_[A-Za-z0-9_]+)\s*\(" source)
                     (map second)
                     (concat ["malloc" "free"])
                     (map #(str "_" %))
                     distinct
                     sort)]
      (println (str "[" (clojure.string/join "," (map pr-str names)) "]")))'
)"

emcc "$BUILD"/*.o \
  -o "$OUT_JS" \
  -s MODULARIZE=1 \
  -s EXPORT_NAME=createVybeRaylibModule \
  -s ENVIRONMENT=web \
  -s ALLOW_MEMORY_GROWTH=1 \
  -s INITIAL_MEMORY=64MB \
  -s USE_GLFW=3 \
  -s ERROR_ON_UNDEFINED_SYMBOLS=0 \
  -s WARN_ON_UNDEFINED_SYMBOLS=0 \
  -s EXPORTED_RUNTIME_METHODS='["ccall","cwrap","getValue","setValue","UTF8ToString","stringToUTF8","lengthBytesUTF8"]' \
  -s EXPORTED_FUNCTIONS="$exported_functions" \
  -s DISABLE_EXCEPTION_THROWING=1 \
  -s ASSERTIONS=0 \
  -s MIN_WEBGL_VERSION=2 \
  -s MAX_WEBGL_VERSION=2 \
  -s FULL_ES3=1 \
  -s GL_ENABLE_GET_PROC_ADDRESS=1

perl -0pi -e "s/alpha: \\(GLFW\\.hints\\[0x00021004\\] > 0\\)      \\/\\/ GLFW_ALPHA_BITS/alpha: (GLFW.hints[0x00021004] > 0),     \\/\\/ GLFW_ALPHA_BITS\\n              preserveDrawingBuffer: true,\\n              powerPreference: 'high-performance'/" "$OUT_JS"

ls -lh "$OUT_DIR"/raylib.*
