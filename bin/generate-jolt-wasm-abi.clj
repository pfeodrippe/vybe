(System/setProperty "vybe.wasm-abi.no-main" "true")
(load-file "bin/generate-wasm-abi.clj")

((requiring-resolve 'generate-wasm-abi/generate!)
 {:repo-root "."
  :generated-by "bin/generate-jolt-wasm-abi.clj"
  :header "JoltPhysicsSharp/src/joltc/joltc.h"
  :include-header "joltc.h"
  :wasm-file "resources/vybe/wasm/jolt.wasm"
  :out-file "resources/vybe/wasm/jolt_abi.edn"
  :layout-source "src/vybe/jolt.clj"
  :function-prefixes ["JPC_" "vybe_"]
  :tmp-prefix "vybe-jolt-abi"
  :cc "em++"
  :source-extension "cpp"
  :cflags ["-I" "JoltPhysicsSharp/src/joltc"
           "-I" "JoltPhysics"
           "-I" "."
           "-DJPH_OBJECT_STREAM=0"
           "-DJPH_DEBUG_RENDERER=0"
           "-DJPH_PROFILE_ENABLED=0"]})

(shutdown-agents)
