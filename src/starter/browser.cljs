(ns starter.browser
  (:require [reagent.core :as r]
            [grid.grid :as grid]
            [grid.dummy-contents :refer [make-content]]))


(defn home-page []

  [:p "Reagent is alive!"]
  [grid/grid (make-content 5)])






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
  (start))

;; this is called before any code is reloaded
(defn ^:dev/before-load stop []
  (js/console.log "stop"))
