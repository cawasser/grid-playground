(ns grid.dnd-grid
  "a grid based upon 're-dnd' from https://github.com/Kah0ona/re-dnd"
  (:require [re-frame.core :as rf]
            [reagent.core :as r]
            [re-dnd.events :as dnd]
            [re-dnd.views :as dndv]
            [re-dnd.subs]
            [taoensso.timbre :as timbre
             :refer-macros (log  trace  debug  info  warn  error  fatal  report
                             logf tracef debugf infof warnf errorf fatalf reportf
                             spy get-env log-env)]))


(defn- make-widget [data-source]
  (let [d (rf/subscribe [:subscribe data-source])]
    [dndv/draggable data-source
     [:p {:style {:width 200 :height 100 :border-style :solid}}
      (str @d)]]))



; multimethods to customize re-dnd features

(defmethod dndv/dropped-widget
  :my-drop-marker
  [{:keys [type id]}]
  [:div.drop-marker])

(defmethod dndv/drag-handle
  :my-drop-marker
  [{:keys [type id]}]
  [:div {:style {:width 200 :height 100 :border-style :dotted}}])

(defmethod dndv/dropped-widget
  :bluebox
  [{:keys [type id source-element-id]}]
  [:div.box.blue-box
   (if source-element-id
     (str @(rf/subscribe [:subscribe source-element-id]))
     (str type ", " id))])

(defmethod dndv/drag-handle
  :bluebox
  [{:keys [type id]}]
  [:div "bluedraghandle"])

(defmethod dndv/dropped-widget
  :redbox
  [{:keys [type id source-element-id]}]
  [:div.box.red-box
   (if source-element-id
     (str @(rf/subscribe [:subscribe source-element-id]))
     (str type ", " id))])

(defmethod dndv/drag-handle
  :redbox
  [{:keys [type id]}]
  [:div "reddraghandle"])



(def last-id (r/atom 0))

; this handle is the one to customize for how,exactly, this app wants
; to handle dropping/moving widgets
;
(rf/reg-event-fx
  :my-drop-dispatch
  (fn [{db :db}
       [_
        [source-drop-zone-id source-element-id]
        [drop-zone-id dropped-element-id dropped-position]]]
    ;;position = index in the list of dropped elements
    (debug "source:" source-drop-zone-id source-element-id
      " target:" drop-zone-id dropped-element-id dropped-position)
    (swap! last-id inc)
    {:db       db
     :dispatch (if (= source-drop-zone-id drop-zone-id)
                 [:dnd/move-drop-zone-element drop-zone-id source-element-id dropped-position]

                 [:dnd/add-drop-zone-element
                  drop-zone-id
                  {:id (keyword (str (name source-element-id) "-dropped-" @last-id))
                   :source-element-id source-element-id
                   :type (if (odd? @last-id)
                           :bluebox
                           :redbox)}
                  dropped-position])}))





(defn my-drop-zone
  []
  [dndv/drop-zone :drop-zone-1                              ;;:drop-zone-1 is a unique identifier
   [:div {:style {:width        200
                  :border-style :solid}}
    "drop zone"]])



(defn my-draggable
  []
  [dndv/draggable :draggable-1
   [:div {:style {:width        100 :height 100
                  :border-style :solid}}
    "Style me any way you like, i'm draggable."]])



(defn dnd-grid
  []

  (prn "dnd-grid")

  (let [drag-box-state (rf/subscribe [:dnd/drag-box])]

    (rf/dispatch [:dnd/initialize-drop-zone
                  :drop-zone-1
                  {:drop-dispatch [:my-drop-dispatch]
                   :drop-marker   :my-drop-marker}
                  ;;initial elements can be put here
                  []]);{:type      :bluebox
                   ; :id        (keyword (str "drop-zone-element-" 0))
                   ; :source-id :source-0
                   ;{:type      :redbox
                   ; :id        (keyword (str "drop-zone-element-" 1))
                   ; :source-id :source-4]])
    (fn []
      [:div {:style {:width 600}}
       [:p "dnd-grid"]

       (when @drag-box-state
         [dndv/drag-box])

       ; my-draggable is more like a "toolbox" of widgets that can be added to the grid
       ; TOTO: swap my-draggable for another way to add widgets
       [:div {:style {:float :left}}
        [make-widget :source-1]
        [make-widget :source-2]
        [make-widget :source-3]]

       ; my-drop-zone if the "grid" itself
       [:div {:style {:float :right}}
        [my-drop-zone]]])))







