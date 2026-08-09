(ns vybe.flecs.native
  "Configures the native Flecs provider before loading vybe.flecs."
  (:import
   (org.vybe.flecs VybeFlecsNativeLoader)))

(defn native-library-info
  "Return the requested path, loaded path, and external-provider state."
  []
  (into {} (VybeFlecsNativeLoader/info)))

(defn provider-info
  "Return the loaded provider's contract, Flecs commit, and ABI sizes."
  []
  (into {} (VybeFlecsNativeLoader/providerInfo)))

(defn verify-provider!
  "Validate the external provider contract and generated-struct ABI sizes."
  []
  (into {} (VybeFlecsNativeLoader/verifyProvider)))

(defn configure-native-library!
  "Select a Flecs provider before `vybe.flecs` is loaded.

  Options:
    - `:path` - absolute path to a Vybe-compatible Flecs dynamic library.

  The path must identify an existing regular file. Reconfiguration after a
  different provider has loaded throws. Most command-line applications can set
  `VYBE_FLECS_LIBRARY_PATH` instead."
  [{:keys [path]}]
  (when-not path
    (throw (ex-info "Missing Flecs provider :path" {})))
  (VybeFlecsNativeLoader/configure path)
  (native-library-info))

(defn load-native-library!
  "Load the selected provider immediately and return its loader information."
  []
  (VybeFlecsNativeLoader/load)
  (native-library-info))
