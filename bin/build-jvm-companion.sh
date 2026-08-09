#!/usr/bin/env bash

set -euo pipefail

java_home="${JAVA_HOME:-}"
if [[ -z "${java_home}" ]] && [[ "$(uname -s)" == "Darwin" ]]; then
    java_home="$(/usr/libexec/java_home)"
fi
if [[ -z "${java_home}" || ! -f "${java_home}/include/jni.h" ]]; then
    echo "JAVA_HOME must point to a JDK with include/jni.h" >&2
    exit 2
fi

case "$(uname -s)" in
    Darwin)
        extension="dylib"
        platform_include="darwin"
        shared_flags=(-dynamiclib -undefined dynamic_lookup)
        ;;
    Linux)
        extension="so"
        platform_include="linux"
        shared_flags=(-shared -fPIC)
        ;;
    *)
        echo "build-jvm-companion.sh currently supports macOS and Linux" >&2
        exit 2
        ;;
esac

output="${VYBE_JVM_COMPANION_OUTPUT:-target/libvybe_jvm.${extension}}"
mkdir -p "$(dirname "${output}")"

"${CC:-clang}" \
    -std=c11 \
    "${shared_flags[@]}" \
    -I"${java_home}/include" \
    -I"${java_home}/include/${platform_include}" \
    -I native \
    native/vybe_jvm.c \
    -o "${output}"

echo "Built ${output}"
