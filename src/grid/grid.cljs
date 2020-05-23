(ns grid.grid
  (:require ["react-grid-layout" :refer [Responsive WidthProvider]]
            [reagent.core :as r]
            [re-frame.core :as rf]
            [grid.dummy-contents :as dc]))



(defn onLayoutChange [old-data new-data]
  (let [chg (dc/reduce-layouts (js->clj new-data :keywordize-keys true))
        fst (first chg)]

    (prn "onLayoutChange" old-data "////" chg)

    (if (and
          (not (empty? chg))
          (<= 1 (count chg))
          (not= (:i fst) "null"))
      (rf/dispatch [:update-layout chg]))))
;(on-change prev (js->clj new :keywordize-keys true))))




(defn grid [args]
  (r/create-class
    {:reagent-render
     (fn [{:keys [id data layouts width row-height cols breakpoints item-props on-change empty-text] :as props}]
       (prn ">>>> re-rendering grid <<<<" data "////" layouts)
       ;
       (into [:> (WidthProvider Responsive)
              {:id             "test-grid"
               :className      "layout"
               :layouts        layouts
               :cols           {:lg 12, :md 10, :sm 6, :xs 4, :xxs 2}
               :breakpoints    {:lg 1200, :md 996, :sm 768, :xs 480, :xxs 0}
               :onLayoutChange #(prn ":onLayoutChange" %1 "////" %2
                                  (onLayoutChange data %1))}]
         data))}))



(comment
  (grid.dummy-contents/make-content 5)


  (grid (grid.dummy-contents/make-content 5))

  (def l [{:y 0, :maxH nil, :moved false, :minW nil, :w 1, :static false, :isDraggable nil, :isResizable nil, :h 1, :minH nil, :x 0, :maxW nil, :i "0"}
          {:y 1, :maxH nil, :moved false, :minW nil, :w 1, :static false, :isDraggable nil, :isResizable nil, :h 1, :minH nil, :x 0, :maxW nil, :i "1"}
          {:y 2, :maxH nil, :moved false, :minW nil, :w 1, :static false, :isDraggable nil, :isResizable nil, :h 1, :minH nil, :x 0, :maxW nil, :i "2"}
          {:y 3, :maxH nil, :moved false, :minW nil, :w 1, :static false, :isDraggable nil, :isResizable nil, :h 1, :minH nil, :x 0, :maxW nil, :i "3"}
          {:y           4, :maxH nil, :moved false, :minW nil, :w 1, :static false, :isDraggable nil,
           :isResizable nil, :h 1, :minH nil, :x 0, :maxW nil, :i "4"}])

  (some true? (map #(:moved %) l))

  ())