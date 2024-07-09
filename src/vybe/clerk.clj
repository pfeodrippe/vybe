(ns vybe.clerk
  (:require
   #_[jsxgraph.core :as jsx]
   [nextjournal.clerk.view :as clerk.view]
   [nextjournal.clerk :as clerk]
   [hiccup.page :as page]
   #_[mentat.clerk-utils.css :as css]
   #_[mentat.clerk-utils.show :refer [show-sci]]))

#_(show-sci
  (require '[jsxgraph.sci]))

(clerk/eval-cljs
 '(do (require '[reagent.core :as reagent])
      (def gui (new (.. js/dat -GUI)))
      (def jxg js/JXG)
      #_(require '["leva" :as l])))

(def jgx-viewer
  {:transform-fn clerk/mark-presented
   :render-fn '(fn [value]
                 (when value
                   [:div {:style {:width "500px" :height "500px"}
                          :ref (fn [el]
                                 (when el
                                   (.. jxg -JSXGraph
                                       (initBoard el (clj->js value)))))}]))})

(def gui-viewer
  {:transform-fn clerk/mark-presented
   :render-fn '(fn [value]
                 (when value
                   (defonce folder (.. gui (addFolder "Flow Field")))
                   (defonce person (clj->js {:age 45}))
                   (defonce aaa
                     (.. folder (add person "age" 0 120)))
                   (js/console.log person)
                   #_[:div {:style {:width "500px" :height "500px"}
                            :ref (fn [el]
                                   (when el
                                     (.. js/JXG -JSXGraph
                                         (initBoard el (clj->js value)))))}]))})

(clerk/with-viewer jgx-viewer
  {:boundingBox [-10 10 10 -10]
   :axis true})

(clerk/with-viewer gui-viewer
  {:boundingBox [-10 10 10 -10]
   :axis true})

(clerk/with-viewer
  '(fn [_]
     (reagent.core/with-let [counter (reagent.core/atom 0)]
       (let [f (fn []
                 (.. js/leva (useControls (clj->js {:name "World" :aNumber 0})))
                 [:span "sss"])]

         [:div
          #_[:f> f]]
         #_[:h3.cursor-pointer {:on-click #(swap! counter inc)}
            "I was clicked " @counter " times."])))
  nil)

(comment

  (clerk/serve! {:browse false})

  (clerk/serve! {:watch-paths ["notebooks" "src" "../vybe/src"]})

  ())

(defonce -original-fn
  clerk.view/include-css+js)

(def head-import
  (alter-var-root
   #'clerk.view/include-css+js
   (fn [_]
     (fn [state]
       (concat (-original-fn state)

               (list
                [:script {:src "https://cdn.jsdelivr.net/npm/jsxgraph/distrib/jsxgraphcore.js"}]
                [:script {:src "https://cdn.jsdelivr.net/npm/dat.gui@0.7.9/build/dat.gui.min.js"}]
                (page/include-css "https://cdn.jsdelivr.net/npm/dat.gui@0.7.9/build/dat.gui.min.css")
                (page/include-css "https://cdn.jsdelivr.net/npm/jsxgraph/distrib/jsxgraph.css")
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

#_(show-sci
  (let [text "Include any Reagent vector!"]
    [:pre text]))

#_(show-sci
 [jsx/JSXGraph {:boundingbox [-2 2 2 -2] :axis true}
  [jsx/Arrow {:name "A" :size 4
              :parents [[0 0] [1 1]]}]])

;; AdssAasasdasddsdsss

;; assasssds

(clerk/show! *ns*)
