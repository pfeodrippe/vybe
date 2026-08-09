#!/usr/bin/env bash

set -euo pipefail

if [[ -z "${VYBE_FLECS_SOURCE_DIR:-}" ]]; then
    echo "VYBE_FLECS_SOURCE_DIR must point to the exact Flecs checkout" >&2
    exit 2
fi

flecs_checkout_dir="$(cd "${VYBE_FLECS_SOURCE_DIR}" && pwd -P)"
flecs_source_dir="${flecs_checkout_dir}"
temporary_source_dir=""

cleanup() {
    if [[ -n "${temporary_source_dir}" && -d "${temporary_source_dir}" ]]; then
        case "${temporary_source_dir}" in
            /tmp/vybe-flecs-provider.*|${TMPDIR:-/tmp}/vybe-flecs-provider.*)
                rm -rf -- "${temporary_source_dir}"
                ;;
            *)
                echo "Refusing to remove unexpected temporary directory: ${temporary_source_dir}" >&2
                ;;
        esac
    fi
}
trap cleanup EXIT

requested_commit="${VYBE_FLECS_COMMIT:-}"
if [[ -n "${requested_commit}" ]]; then
    resolved_commit="$(git -C "${flecs_checkout_dir}" rev-parse --verify "${requested_commit}^{commit}")"
    checkout_commit="$(git -C "${flecs_checkout_dir}" rev-parse HEAD)"
    if [[ "${resolved_commit}" != "${checkout_commit}" ]]; then
        temporary_source_dir="$(mktemp -d "${TMPDIR:-/tmp}/vybe-flecs-provider.XXXXXX")"
        git -C "${flecs_checkout_dir}" archive "${resolved_commit}" | tar -x -C "${temporary_source_dir}"
        flecs_source_dir="${temporary_source_dir}"
    fi
fi

flecs_header="${VYBE_FLECS_HEADER:-${flecs_source_dir}/distr/flecs.h}"
flecs_source="${VYBE_FLECS_SOURCE:-${flecs_source_dir}/distr/flecs.c}"

if [[ ! -f "${flecs_header}" || ! -f "${flecs_source}" ]]; then
    echo "Flecs header/source not found under ${flecs_source_dir}" >&2
    exit 2
fi

case "$(uname -s)" in
    Darwin)
        extension="dylib"
        shared_flags=(-dynamiclib)
        ;;
    Linux)
        extension="so"
        shared_flags=(-shared -fPIC)
        ;;
    *)
        echo "build-flecs-provider.sh currently supports macOS and Linux" >&2
        exit 2
        ;;
esac

output_path="${VYBE_FLECS_PROVIDER_OUTPUT:-resources/vybe/native/libvybe_flecs_external.${extension}}"
output_parent="$(dirname "${output_path}")"
mkdir -p "${output_parent}"
output_parent="$(cd "${output_parent}" && pwd -P)"
output_path="${output_parent}/$(basename "${output_path}")"

flecs_commit="${resolved_commit:-}"
if [[ -z "${flecs_commit}" ]] && git -C "${flecs_checkout_dir}" rev-parse HEAD >/dev/null 2>&1; then
    flecs_commit="$(git -C "${flecs_checkout_dir}" rev-parse HEAD)"
fi
flecs_commit="${flecs_commit:-unknown}"

compiler="${CC:-clang}"
compatibility_flag=""
if sed -n '/typedef struct ecs_system_desc_t {/,/} ecs_system_desc_t;/p' "${flecs_header}" | grep -q 'ecs_entity_t phase;'; then
    compatibility_flag="-DVYBE_FLECS_SYSTEM_DESC_HAS_PHASE"
fi
if [[ "${VYBE_FLECS_HOSTED:-0}" == "1" ]]; then
    hosted_link_flag=""
    if [[ -n "${VYBE_FLECS_SHARED_LIBRARY:-}" ]]; then
        if [[ ! -f "${VYBE_FLECS_SHARED_LIBRARY}" ]]; then
            echo "VYBE_FLECS_SHARED_LIBRARY is not a file: ${VYBE_FLECS_SHARED_LIBRARY}" >&2
            exit 2
        fi
        hosted_link_flag="${VYBE_FLECS_SHARED_LIBRARY}"
    elif [[ "$(uname -s)" == "Darwin" ]]; then
        hosted_link_flag="-undefined dynamic_lookup"
    fi
    "${compiler}" \
        -std=gnu11 \
        "${shared_flags[@]}" \
        ${hosted_link_flag} \
        ${compatibility_flag:+"${compatibility_flag}"} \
        -DFLECS_SCRIPT_MATH \
        -DVYBE_FLECS_COMMIT=\"${flecs_commit}\" \
        -I"$(dirname "${flecs_header}")" \
        bin/vybe_flecs.c \
        -o "${output_path}"
else
    "${compiler}" \
        -std=gnu11 \
        "${shared_flags[@]}" \
        ${compatibility_flag:+"${compatibility_flag}"} \
        -DFLECS_SCRIPT_MATH \
        -DVYBE_FLECS_COMMIT=\"${flecs_commit}\" \
        -I"$(dirname "${flecs_header}")" \
        bin/vybe_flecs.c \
        "${flecs_source}" \
        -o "${output_path}"
fi

echo "Built ${output_path} from Flecs ${flecs_commit}"

if [[ "${VYBE_FLECS_GENERATE_BINDINGS:-0}" == "1" ]]; then
    jextract_bin="${VYBE_JEXTRACT:-}"
    if [[ -z "${jextract_bin}" || ! -x "${jextract_bin}" ]]; then
        echo "VYBE_JEXTRACT must identify an executable jextract" >&2
        exit 2
    fi

    bindings_output="${VYBE_FLECS_BINDINGS_OUTPUT:-src-java}"
    generated_package="${bindings_output}/org/vybe/flecs"
    if [[ -d "${generated_package}" ]]; then
        generated_package="$(cd "${generated_package}" && pwd -P)"
        bindings_root="$(mkdir -p "${bindings_output}" && cd "${bindings_output}" && pwd -P)"
        case "${generated_package}" in
            "${bindings_root}"/org/vybe/flecs)
                rm -rf -- "${generated_package}"
                ;;
            *)
                echo "Refusing to remove unexpected generated package: ${generated_package}" >&2
                exit 2
                ;;
        esac
    fi
    "${jextract_bin}" \
        --use-system-load-library \
        --library vybe_flecs \
        --output "${bindings_output}" \
        --header-class-name flecs \
        -I "$(dirname "${flecs_header}")" \
        -t org.vybe.flecs \
        bin/vybe_flecs.c

    generated_loader="${bindings_output}/org/vybe/flecs/flecs_2.java"
    LC_ALL=C LANG=C perl -0pi -e \
        's/System\.loadLibrary\("vybe_flecs"\);/VybeFlecsNativeLoader.load();/' \
        "${generated_loader}"
    grep -q 'VybeFlecsNativeLoader.load();' "${generated_loader}"
    echo "Generated exact Flecs bindings in ${bindings_output}"
fi
