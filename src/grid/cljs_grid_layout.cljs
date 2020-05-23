(ns grid.cljs-grid-layout)




(defn grid [{:keys [content layout] :as params}]
  (into [:div {:style {:width 100 :height 100}}]
    content))