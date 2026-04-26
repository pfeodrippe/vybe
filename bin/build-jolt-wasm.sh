#!/usr/bin/env bash
set -euo pipefail

ROOT="$(cd "$(dirname "$0")/.." && pwd)"
BUILD_DIR="$ROOT/target/jolt-wasm"
OUT="$ROOT/resources/vybe/wasm/jolt.wasm"

rm -rf "$BUILD_DIR"
mkdir -p "$BUILD_DIR" "$(dirname "$OUT")"

emcmake cmake -S "$ROOT/JoltPhysicsSharp" -B "$BUILD_DIR" \
  -DCMAKE_BUILD_TYPE=Release \
  -DJPH_SAMPLES=OFF \
  -DJPH_UNIT_TESTS=OFF \
  -DBUILD_SHARED_LIBS=OFF \
  -DINTERPROCEDURAL_OPTIMIZATION=OFF \
  -DGENERATE_DEBUG_SYMBOLS=OFF \
  -DFETCHCONTENT_SOURCE_DIR_JOLTPHYSICS="$ROOT/JoltPhysics" \
  -DUSE_WASM_SIMD=OFF \
  -DUSE_ASSERTS=OFF \
  -DDOUBLE_PRECISION=OFF \
  -DENABLE_OBJECT_STREAM=OFF \
  -DENABLE_ALL_WARNINGS=OFF \
  -DPROFILER_IN_DEBUG_AND_RELEASE=OFF \
  -DPROFILER_IN_DISTRIBUTION=OFF \
  -DDEBUG_RENDERER_IN_DEBUG_AND_RELEASE=OFF \
  -DDEBUG_RENDERER_IN_DISTRIBUTION=OFF \
  -DCMAKE_CXX_FLAGS='-Wno-unused-variable -Wno-overloaded-virtual'

# The vendored joltc target forces -Werror. Emscripten emits harmless wrapper
# warnings for this target, so remove Werror from the generated Wasm build only.
LC_ALL=C sed -i.bak 's/ -Werror//g' "$BUILD_DIR/src/joltc/CMakeFiles/joltc.dir/flags.make"

cmake --build "$BUILD_DIR" --target joltc -j "${JOBS:-$(sysctl -n hw.ncpu 2>/dev/null || echo 4)}"

em++ -c "$ROOT/bin/vybe_jolt_wasm.cpp" \
  -o "$BUILD_DIR/vybe_jolt_wasm.o" \
  -I "$ROOT" \
  -I "$ROOT/JoltPhysics" \
  -I "$ROOT/JoltPhysicsSharp/src/joltc" \
  -DJPH_OBJECT_STREAM=0 \
  -DJPH_DEBUG_RENDERER=0 \
  -DJPH_PROFILE_ENABLED=0 \
  -std=c++17 \
  -O3 \
  -ffp-model=precise \
  -Wno-unused-variable \
  -Wno-overloaded-virtual

em++ -Wl,--whole-archive "$BUILD_DIR/vybe_jolt_wasm.o" "$BUILD_DIR/lib/libjoltc.a" "$BUILD_DIR/lib/libJolt.a" -Wl,--no-whole-archive \
  -o "$OUT" \
  --no-entry \
  -s STANDALONE_WASM=1 \
  -s ERROR_ON_UNDEFINED_SYMBOLS=0 \
  -s ALLOW_MEMORY_GROWTH=1 \
  -s INITIAL_MEMORY=64MB \
  -Wl,--export-all \
  -Wl,--no-gc-sections

ls -lh "$OUT"
