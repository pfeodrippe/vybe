(ns vybe.clerk.util
  (:require
   [nextjournal.clerk.config :as clerk.config]
   [nextjournal.clerk.view :as clerk.view]
   [nextjournal.clerk :as clerk]
   [nextjournal.clerk.webserver :as webserver]
   [nextjournal.clerk.eval :as eval]
   [clojure.set :as set]
   [hiccup.page :as page]
   [nextjournal.clerk.viewer :as v]
   [nextjournal.clerk.analyzer :as analyzer]
   [weavejester.dependency :as dep]))

(defonce ^:private recompute!
  (alter-var-root
   #'clerk/recompute!
   (fn [_]
     (fn []
       (binding [*ns* (:ns @webserver/!doc)]
         (let [{:keys [result time-ms]} (eval/time-ms (eval/eval-analyzed-doc @webserver/!doc))]
           #_(println (str "Clerk recomputed '" @@#'clerk/!last-file "' in " time-ms "ms."))
           (webserver/update-doc! result)))))))

(defonce ^:private -original-include-css+js
  clerk.view/include-css+js)

(alter-var-root
 #'clerk.view/include-css+js
 (fn [_]
   (fn [state]
     (concat (-original-include-css+js state)
             (list
              (page/include-css "https://cdn.jsdelivr.net/npm/dat.gui@0.7.9/build/dat.gui.min.css")
              (page/include-css "https://cdn.jsdelivr.net/npm/jsxgraph/distrib/jsxgraph.css")
              ;; <link href="https://cdn.jsdelivr.net/npm/daisyui@4.12.10/dist/full.min.css" rel="stylesheet" type="text/css" />

              [:script {:src "https://cdn.jsdelivr.net/npm/jsxgraph/distrib/jsxgraphcore.js"}]
              [:script {:src "https://cdn.jsdelivr.net/npm/dat.gui@0.7.9/build/dat.gui.min.js"}]
              [:script {:type "module"}
               #_"
import { useControls } from \"https://cdn.jsdelivr.net/npm/leva@0.9.35/dist/leva.esm.js/+esm\"

function MyComponent() {
  const { name, aNumber } = useControls({ name: 'World', aNumber: 0 });

  return 22;
}

window.MyComponent = MyComponent;
"
               "
import * as react from 'https://cdn.jsdelivr.net/npm/react@18.3.1/+esm';
window.react = react;

import * as leva from 'https://cdn.jsdelivr.net/npm/leva@0.9.35/dist/leva.esm.js/+esm';
window.leva = leva;

import * as tremor from 'https://cdn.jsdelivr.net/npm/@tremor/react@3.17.4/+esm';
window.tremor = tremor;"])))))

(alter-var-root
 #'clerk.view/->html
 (fn [_]
   (fn [{:as state :keys [conn-ws? current-path html exclude-js?]}]
     (page/html5 {:data-theme "cupcake"}
       [:head
        [:script {:src "https://cdn.tailwindcss.com"}]
        [:script
         "tailwind.config = {
      important: true
    }"]

        [:link {:href "https://cdn.jsdelivr.net/npm/daisyui@4.12.10/dist/full.min.css" :rel "stylesheet" :type "text/css"}]

        [:script {:src "https://cdnjs.cloudflare.com/ajax/libs/highlight.js/11.10.0/highlight.min.js"}]
        [:script {:src "https://cdnjs.cloudflare.com/ajax/libs/highlight.js/11.10.0/languages/clojure.min.js"}]
        [:script {:src "https://cdnjs.cloudflare.com/ajax/libs/highlight.js/11.10.0/languages/markdown.min.js"}]

        [:meta {:charset "UTF-8"}]
        [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
        (when conn-ws?
          [:script {:type "text/javascript"}
           "if ('serviceWorker' in navigator) {
          navigator.serviceWorker
            .register('/clerk_service_worker.js')
            //.then(function() { console.log('Service Worker: Registered') })
            .catch(function(error) { console.log('Service Worker: Error', error) })
        }"])
        (when current-path (v/open-graph-metas (-> state :path->doc (get current-path) v/->value :open-graph)))
        (if exclude-js?
          (clerk.view/include-viewer-css state)
          (clerk.view/include-css+js state))]
       [:body.dark:bg-gray-900 {:data-theme "cupcake"}
        [:div#clerk html]
        (when-not exclude-js?
          [:script {:type "module"} "let viewer = nextjournal.clerk.sci_env
let state = " (-> state v/->edn clerk.view/escape-closing-script-tag pr-str) ".replaceAll('nextjournal.clerk.view/escape-closing-script-tag', 'script')
viewer.init(viewer.read_string(state))\n"
           (when conn-ws?
             "viewer.connect(document.location.origin.replace(/^http/, 'ws') + '/_ws')")])]

       [:script "hljs.highlightAll();"]))))

(alter-var-root
 #'analyzer/hash-codeblock
 (fn [_]
   (fn hash-codeblock
     [->hash {:keys [ns graph]} {:as codeblock :keys [hash form id vars]}]
     (let [deps (dep/immediate-dependencies graph id)
           hashed-deps (into #{} (keep ->hash) deps)]
       (when-some [dep-with-missing-hash
                   (some (fn [dep]
                           (when-not (get ->hash dep)
                             (when-not (analyzer/deref? dep) ;; on a first pass deref-nodes do not have a hash yet
                               dep)))
                         deps)]
         #_(throw (ex-info (format "Hash is missing on dependency '%s' of the form '%s' in %s" dep-with-missing-hash form ns)
                         {:dep dep-with-missing-hash :codeblock codeblock :ns ns})))
       (analyzer/sha1-base58 (binding [*print-length* nil]
                               (pr-str (set/union (conj hashed-deps (if form (#'analyzer/remove-type-meta form) hash))
                                                  vars))))))))

(defmacro without-clerk
  [& body]
  (when-not clerk.config/*in-clerk*
    `(do ~@body)))
