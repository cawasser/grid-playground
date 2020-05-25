(ns starter.browser
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [grid.grid :as grid]
            [grid.cljs-grid-layout :as cljs-grid-layout]))



(defn- fixed-grid [content]
  (prn "fixed-grid re-render")
  (into [:div]
    content))

(defn home-page []
  [:div
   ;[:p "react-grid-layout"]
   ;[grid/grid (make-content 5)]
   [:p "cljs-grid-layout"]
   [:button {:on-click #(rf/dispatch [:tick])} "Tick"]
   [cljs-grid-layout/grid {:content (rf/subscribe [:sources])
                           :layout (rf/subscribe [:layout])}]])






(defn mount [c]
  (r/render-component [c] (.getElementById js/document "app")))


;; start is called by init and after code reloading finishes
(defn ^:dev/after-load start []
  (js/console.log "start")
  (mount home-page))

(defn ^:export init []
  ;; init is called ONCE when the page loads
  ;; this is called in the index.html and must be exported
  ;; so it is available even in :advanced release builds
  (js/console.log "init")
  (rf/dispatch [:init-db])
  (start))

;;; this is called before any code is reloaded
;(defn ^:dev/before-load stop []
;  (js/console.log "stop"))
