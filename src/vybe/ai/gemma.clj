(ns vybe.ai.gemma
  "Local Gemma client for producing ordinary Vybe `defsystem-c` forms."
  (:require
   [clojure.string :as str]
   [jsonista.core :as json])
  (:import
   (java.net URI)
   (java.net.http HttpClient HttpRequest HttpRequest$BodyPublishers
                  HttpResponse$BodyHandlers)
   (java.time Duration)))

(def default-model
  "gemma4:26b-a4b-it-qat")

(defn- -component-context
  [registry request]
  (->> (:components registry)
       (filter #(= :reflected (:status %)))
       (sort-by (fn [{:keys [path]}]
                  [(if (str/includes? (str/lower-case request)
                                      (str/lower-case path))
                     0
                     1)
                   path]))
       (take 64)
       (map (fn [{:keys [path fields]}]
              (str path " "
                   (pr-str (mapv (fn [{:keys [field schema]}]
                                   [field (if (map? schema) :struct schema)])
                                 fields)))))
       (str/join "\n")))

(defn- -strip-fence
  [source]
  (-> source
      str/trim
      (str/replace #"(?s)^```(?:clojure)?\s*" "")
      (str/replace #"(?s)\s*```$" "")
      str/trim))

(defn generate-system
  [{:keys [request registry]}]
  (let [base-url (or (System/getenv "VYBE_MODEL_URL") "http://127.0.0.1:11434")
        model (or (System/getenv "VYBE_MODEL_NAME") default-model)
        system-instruction
        (str "You write runtime systems for Vybe and Flecs. Return exactly one "
                "Clojure (vf/defsystem-c name _w [bindings ...] body...) form, no markdown. "
                "Every component binding is a local-symbol/component-name pair. For example, "
                "(vf/defsystem-c move _w [position :Position velocity \"Velocity\"] "
                "(merge @position {:x (+ (:x @position) (:x @velocity))})). "
                "Use component keyword or string names exactly as listed. Never use a "
                "component name as an unbound symbol and never omit a needed binding. "
                "The body uses VybeC, so component pointers are read with @ and updated "
                "with merge. Do not add require, ns, explanation, pseudocode, C++, a data "
                "description, or a second form.")
        prompt (str
                "Available reflected components:\n" (-component-context registry request)
                "\n\nRequest:\n" request)
        context-size (or (some-> (System/getenv "VYBE_MODEL_CONTEXT") parse-long)
                         8192)
        max-output-tokens (or (some-> (System/getenv "VYBE_MODEL_MAX_TOKENS") parse-long)
                              512)
        keep-alive (or (System/getenv "VYBE_MODEL_KEEP_ALIVE") "1m")
        payload (json/write-value-as-string
                 {:model model
                  :stream false
                  :think false
                  :keep_alive keep-alive
                  :options {:num_ctx context-size
                            :num_predict max-output-tokens
                            :temperature 0.2}
                  :messages [{:role "system" :content system-instruction}
                             {:role "user" :content prompt}]})
        http-client (-> (HttpClient/newBuilder)
                        (.connectTimeout (Duration/ofSeconds 5))
                        .build)
        http-request (-> (HttpRequest/newBuilder)
                         (.uri (URI/create (str (str/replace base-url #"/$" "") "/api/chat")))
                         (.timeout (Duration/ofMinutes 5))
                         (.header "Content-Type" "application/json")
                         (.POST (HttpRequest$BodyPublishers/ofString payload))
                         .build)
        response (.send http-client http-request (HttpResponse$BodyHandlers/ofString))]
    (when-not (<= 200 (.statusCode response) 299)
      (throw (ex-info "Local Gemma endpoint returned an error"
                      {:status (.statusCode response)
                       :body (.body response)})))
    (let [body (json/read-value (.body response)
                                (json/object-mapper {:decode-key-fn true}))
          source (or (get-in body [:message :content]) (:response body))]
      (when-not (seq source)
        (throw (ex-info "Local Gemma response did not contain source" {:body body})))
      (-strip-fence source))))
