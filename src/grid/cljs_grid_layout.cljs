(ns grid.cljs-grid-layout
  (:require [reagent.core :as r]
            [re-frame.core :as rf]))



(defn- compute-columns [width columns gap]
  (let [delta-w (int (Math/floor (/ width columns)))]
    (for [left (range 0 width delta-w)]
      (+ left gap))))

(defn- compute-rows [height rows gap]
  (let [delta-w (int (Math/floor (/ height rows)))]
    (for [top (range 0 height delta-w)]
      (+ top gap))))

(defn- compute-grid [width height columns rows gap]
  (partition (inc rows)
    (for [left (compute-columns width columns gap)
          top  (compute-rows height rows gap)]
      [left top])))

(comment
  (def element ())
  (def width 1024)
  (def height 2048)
  (def rows 18)
  (def columns 10)
  (def gap 4)

  (def delta-w (int (Math/floor (/ width columns))))

  (for [x (range 0 width delta-w)]
    (+ gap x))

  (compute-columns width columns gap)
  (compute-rows height rows gap)

  (compute-grid width height columns rows gap)

  ())



(defn- compute-occupation-map [column-map row-map layout]
  [[[:source-0]]
   [[:source-1]]
   [[:source-2]]
   [[:source-3]]
   [[:source-4]]])

(comment


  ())





(defn- set-drag [context id evt]
  (let [ret (reset! context id)]
    (prn "event ")
    ret))


(defn wrap-widget [context idx id content]
  ^{:key id}
  [:div {:key           id
         :style         {:border-style "solid"
                         :width        100 :height 100
                         :position     :absolute
                         :top          0
                         :left         (* idx 110)}
         :draggable     true
         :on-drag-start #(set-drag context id %)
         :on-drop       #(set-drag context nil %)}
   [:p (str content)]])



(defn make-content [context content]
  (into []
    (for [[idx [id _]] (map-indexed vector @content)]
      (wrap-widget context idx id @(rf/subscribe [:subscribe id])))))



(defn- on-drag-over [evt]
  (.preventDefault evt)
  (prn "event " (-> evt .-target .-id)))



(defn- on-drop [evt]
  (prn "dropping " evt))



(defn grid [{:keys [content layout columns gap] :as params}]
  (let [context (r/atom nil)]

    (fn []
      (let [width         1024
            column-layout (compute-columns width columns gap)]
        [:div
         [:p (str "dragging " @context " / " (if @context @(rf/subscribe [:layout-widget @context])))]
         (into [:div {:style        {:position :relative}
                      :on-drop      #(on-drop %)
                      :on-drag-over #(on-drag-over %)}]
           (make-content context content))]))))





(defn abs-dummy []
  [:div.background {:style {:position :relative :width "728px" :height "200px" :border-style "solid"}}
   [:div {:style {:position :absolute :top "70px" :left "255px" :width "240px" :background-color "azure" :border-style "solid"}}
    [:p ":position absolute :top 70px :left 255px :width 240px "]]
   [:div {:style {:position :absolute :top "10px" :left "3px" :width "240px" :height "150px" :background-color "pink" :border-style "solid"}}
    [:p ":position absolute :top 10px :left 3px :width 240px "]]
   [:div {:style {:position :absolute :top "25px" :left "507px" :width "210px" :height "125px" :background-color "yellow" :border-style "solid"}}
    [:p ":position absolute :top 25px :left 507px :width 210px "]]])



(comment

  (def context (atom nil))
  (def content (atom {:source-0 [1 0],
                      :source-1 [1 0],
                      :source-2 [2 0],
                      :source-3 [3 0],
                      :source-4 [4 0]}))
  (def width 1024)
  (def columns 10)
  (def gap 5)

  (for [[id _] @content]
    id)


  (abs-dummy)


  ())

