#!/usr/bin/env bash
set -euo pipefail

repo_root="$(cd "$(dirname "$0")/.." && pwd)"

"$repo_root/bin/build-flecs-wasm.sh"
"$repo_root/bin/build-jolt-wasm.sh"
