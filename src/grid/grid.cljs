(ns grid.grid
  (:require ["react-grid-layout" :refer [Responsive WidthProvider]]))





(defn grid [content]

  (into [:> (WidthProvider Responsive)
         {:id        "test-grid"
          :className "layout"
          :cols      {:lg 12, :md 10, :sm 6, :xs 4, :xxs 2}}]
    content))



(comment
  (grid.dummy-contents/make-content 5)


  (grid (grid.dummy-contents/make-content 5))

  ())