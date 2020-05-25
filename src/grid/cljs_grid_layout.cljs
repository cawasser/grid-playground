(ns grid.cljs-grid-layout
  (:require [reagent.core :as r]
            [re-frame.core :as rf]))



(defn- set-drag [context id evt]
  (let [ret (reset! context id)]
    ret))



(defn wrap-widget [context id content]
  ^{:key id}
  [:div {:key           id
         :style         {:border-style "solid"
                         :width        100 :height 100}
         :draggable     true
         :on-drag-start #(set-drag context id %)
         :on-drop       #(set-drag context nil %)}
   [:p (str content)]])



(defn make-content [context content]
  (into []
    (for [[id _] @content]
      (wrap-widget context id @(rf/subscribe [:subscribe id])))))



(defn- on-drag-over [evt]
  (.preventDefault evt))



(defn- on-drop [evt]
  (prn "dropping " evt))



(defn grid [{:keys [content layout] :as params}]
  (let [context (r/atom nil)
        widgets (make-content context content)]

    (fn []
      [:div
       [:p (str "dragging " @context)]
       (into [:div {:on-drop #(on-drop %)
                    :on-drag-over #(on-drag-over %)}]
         (make-content context content))])))




(comment

  (def context (atom nil))
  (def content (atom {:source-0 [1 0],
                      :source-1 [1 0],
                      :source-2 [2 0],
                      :source-3 [3 0],
                      :source-4 [4 0]}))

  (for [[id _] @content]
    id)



  ())

