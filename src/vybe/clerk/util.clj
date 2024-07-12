(ns vybe.clerk.util
  (:require
   [nextjournal.clerk.view :as clerk.view]
   [nextjournal.clerk :as clerk]
   [nextjournal.clerk.webserver :as webserver]
   [nextjournal.clerk.eval :as eval]
   [hiccup.page :as page]))

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

(def ^:private head-import
  (alter-var-root
   #'clerk.view/include-css+js
   (fn [_]
     (fn [state]
       (concat (-original-include-css+js state)
               (list
                (page/include-css "https://cdn.jsdelivr.net/npm/dat.gui@0.7.9/build/dat.gui.min.css")
                (page/include-css "https://cdn.jsdelivr.net/npm/jsxgraph/distrib/jsxgraph.css")
                [:script {:src "https://cdn.jsdelivr.net/npm/jsxgraph/distrib/jsxgraphcore.js"}]
                [:script {:src "https://cdn.jsdelivr.net/npm/dat.gui@0.7.9/build/dat.gui.min.js"}]
                [:script {:type "module"}
                 "
import * as tremor from 'https://cdn.jsdelivr.net/npm/@tremor/react@3.17.4/+esm';
window.tremor = tremor;
"]
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
//import * as React from 'https://cdn.jsdelivr.net/npm/react@18.3.1/+esm';
//import { useRef } from 'https://cdn.jsdelivr.net/npm/react@18.3.1/+esm';

import * as leva from 'https://cdn.jsdelivr.net/npm/leva@0.9.35/dist/leva.esm.js/+esm';
//window.React = React;
window.leva = leva;"]))))))
