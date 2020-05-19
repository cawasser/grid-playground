(ns grid.dummy-contents)




(defn one-dummy-content [id content]
  [:div {:key id
         :style {:border-style "solid"}}
   content])



(defn make-content [n]
  (into []
    (for [idx (range n)]
      (one-dummy-content idx (str "testing " idx)))))





(comment
  (range 5)

  (one-dummy-content 1 "Testing")

  (make-content 5)

  ())