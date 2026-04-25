(System/setProperty "vybe.wasm-abi.no-main" "true")
(load-file "bin/generate-wasm-abi.clj")

((requiring-resolve 'generate-wasm-abi/generate!)
 {:repo-root "."
  :generated-by "bin/generate-flecs-wasm-abi.clj"
  :header "flecs/distr/flecs.h"
  :include-header "flecs.h"
  :wasm-file "resources/vybe/wasm/flecs.wasm"
  :out-file "resources/vybe/wasm/flecs_abi.edn"
  :layout-source "src/vybe/flecs.clj"
  :tmp-prefix "vybe-flecs-abi"
  :cflags ["-I" "flecs/distr"
           "-DFLECS_CUSTOM_BUILD"
           "-DFLECS_DOC"
           "-DFLECS_META"
           "-DFLECS_MODULE"
           "-DFLECS_APP"
           "-DFLECS_STATS"
           "-DFLECS_OS_API_IMPL"
           "-DFLECS_PARSER"
           "-DFLECS_PIPELINE"
           "-DFLECS_QUERY_DSL"
           "-DFLECS_SYSTEM"
           "-DFLECS_JSON"
           "-DFLECS_REST"
           "-DFLECS_NO_CPP"]})

(shutdown-agents)
