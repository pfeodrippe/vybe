#!/usr/bin/env bash
set -euo pipefail

repo_root="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
out_dir="$repo_root/resources/vybe/wasm"
out_file="$out_dir/flecs.wasm"
tmp_dir="$(mktemp -d)"
trap 'rm -rf "$tmp_dir"' EXIT

mkdir -p "$out_dir"

common_flags=(
  -I"$repo_root/flecs/distr"
  -I"$repo_root/raylib/src"
  -O3
  -flto
  -std=gnu99
  -fno-exceptions
  -DNDEBUG
  -DVYBE_WASM_CORE
  -Dflecs_EXPORTS
  -DFLECS_CUSTOM_BUILD
  -DFLECS_DOC
  -DFLECS_META
  -DFLECS_MODULE
  -DFLECS_APP
  -DFLECS_STATS
  -DFLECS_OS_API_IMPL
  -DFLECS_PARSER
  -DFLECS_PIPELINE
  -DFLECS_QUERY_DSL
  -DFLECS_SYSTEM
  -DFLECS_JSON
  -DFLECS_REST
  -DFLECS_NO_CPP
  -DFLECS_NDEBUG
  -DFLECS_KEEP_ASSERT
  -DFLECS_SOFT_ASSERT
)

emcc -c "$repo_root/flecs/distr/flecs.c" \
  "${common_flags[@]}" \
  -o "$tmp_dir/flecs.o"

emcc -c "$repo_root/bin/vybe_flecs.c" \
  "${common_flags[@]}" \
  -o "$tmp_dir/vybe_flecs.o"

emcc -c "$repo_root/bin/vybe_flecs_wasm_callbacks.c" \
  "${common_flags[@]}" \
  -o "$tmp_dir/vybe_flecs_wasm_callbacks.o"

emcc \
  "$tmp_dir/flecs.o" \
  "$tmp_dir/vybe_flecs.o" \
  "$tmp_dir/vybe_flecs_wasm_callbacks.o" \
  -s STANDALONE_WASM=1 \
  -s ALLOW_MEMORY_GROWTH=0 \
  -s INITIAL_MEMORY=1073741824 \
  -s STACK_SIZE=8388608 \
  -s EMULATE_FUNCTION_POINTER_CASTS=1 \
  -s DISABLE_EXCEPTION_THROWING=1 \
  -s ERROR_ON_UNDEFINED_SYMBOLS=0 \
  -s WARN_ON_UNDEFINED_SYMBOLS=0 \
  -s SUPPORT_LONGJMP=0 \
  -s ASSERTIONS=0 \
  -flto \
  -Wl,--export-all \
  --no-entry \
  -o "$out_file"

(cd "$repo_root" && clj -M "bin/generate-wasm-abi.clj" "bin/flecs-wasm-abi.edn" >/dev/null)

echo "$out_file"
