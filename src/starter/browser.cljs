(ns starter.browser
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [grid.grid :as grid]
            [grid.dummy-contents :refer [make-content]]))



(defn- fixed-grid [content]
  (prn "fixed-grid re-render")
  (into [:div]
    content))

(defn home-page []
  (let [content (make-content 5)
        layout  @(rf/subscribe [:layout])]
    (prn "home-page" content "////" layout)
    [:div
     [:p "react-grid-layout"]
     [grid/grid {:data       content
                 :layouts    {:lg layout}
                 :item-props {:class "widget-component"}}]
     [:p "fixed div"]
     [fixed-grid (make-content 5)]]))






(defn mount [c]
  (r/render-component [c] (.getElementById js/document "app")))


;; start is called by init and after code reloading finishes
(defn start []
  (js/console.log "start")
  (rf/dispatch-sync [:init-db])
  (mount home-page))

(defn ^:export init []
  ;; init is called ONCE when the page loads
  ;; this is called in the index.html and must be exported
  ;; so it is available even in :advanced release builds
  (js/console.log "init")
  (start))

;;; this is called before any code is reloaded
;(defn ^:dev/before-load stop []
;  (js/console.log "stop"))
