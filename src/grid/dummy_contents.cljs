(ns grid.dummy-contents
  (:require [re-frame.core :as rf]
            [day8.re-frame.tracing :refer-macros [fn-traced defn-traced]]))




(defn- apply-updates [new-layout old-layout]
  (-> old-layout
    (assoc :x (:x new-layout))
    (assoc :y (:y new-layout))
    (assoc :w (:w new-layout))
    (assoc :h (:h new-layout))))


(defn reduce-layouts [layout]
  (map (fn [n]
         {:y (:y n)
          :x (:x n)
          :w (:w n)
          :h (:h n)
          :i (:i n)})
    layout))


(defn update-layout [old-layout layout]
  (let [ret (->> (for [old old-layout
                       l   layout]
                   (if (= (str (:i old)) (:i l))
                     (apply-updates l old)))
              (remove nil?)
              (into []))]
    ;(prn "update-layout" ret)
    ret))







(rf/reg-event-db
  :init-db
  (fn-traced [db _]
    ;(prn "init app-db")
    (assoc db
      :widgets {:source-0 [1 1]
                :source-1 [1 1]
                :source-2 [2 1]
                :source-3 [3 1]
                :source-4 [4 1]}
      :layout [{:i 0 :x 0 :y 0 :w 1 :h 1}
               {:i 1 :x 0 :y 0 :w 1 :h 1}
               {:i 2 :x 0 :y 0 :w 1 :h 1}
               {:i 3 :x 0 :y 0 :w 1 :h 1}
               {:i 4 :x 0 :y 0 :w 1 :h 1}]
      :last-tick 0)))



(rf/reg-event-db
  :update-layout
  (fn-traced [db [_ layout]]
    (let [new-layout (update-layout (:layout db) layout)
          ret (assoc db :layout new-layout)]
      (prn ":update-layout" ret)
      ret)))




(rf/reg-event-db
  :add-source
  (fn-traced [db [_ source-id source-tick source-value]]
    (let [ret (assoc-in db [:widgets source-id] [source-tick source-value])]
      ;(prn ":add-source" ret)
      ret)))




(defn- tick-update [t v i]
  (if (= 0 (mod i t))
    (inc v)
    v))

(rf/reg-event-db
  :tick
  (fn-traced [db _]
    (prn "tick!!!!!")
    (let [next-tick (inc (:last-tick db))
          ret       (reduce-kv (fn [m k [t v]]
                                 (assoc m k [t (tick-update t v next-tick)]))
                      {}
                      (:widgets db))]
      (prn ":tick" ret)
      (assoc db
        :last-tick next-tick
        :widgets ret))))



(rf/reg-sub
  :subscribe
  (fn [db [_ source-id]]
    (-> db
      :widgets
      source-id)))


(rf/reg-sub
  :layout
  (fn [db _]
    (-> db
      :layout)))




(defn tick [current-tick [trigger current-val]]
  (if (= 0 (mod current-tick trigger))
    (inc current-val)
    current-val))



(defn one-dummy-content [id content]
  ^{:key id}
  [:div {:key   id
         :style {:border-style "solid"}}
   [:p (str @(rf/subscribe [:subscribe content]))]])



(defn make-content [n]
  (into []
    (for [idx (range n)]
      (one-dummy-content idx (keyword (str "source-" idx))))))






(comment
  (def w (widgets @re-frame.db/app-db))

  (rf/dispatch [:init-db])

  (apply-updates {} {})
  (def layout
    [{:y 0, :maxH nil, :moved false, :minW nil, :w 1, :static false, :isDraggable nil, :isResizable nil, :h 1, :minH nil, :x 1, :maxW nil, :i "0"}
     {:y 1, :maxH nil, :moved false, :minW nil, :w 1, :static false, :isDraggable nil, :isResizable nil, :h 1, :minH nil, :x 2, :maxW nil, :i "1"}
     {:y 2, :maxH nil, :moved false, :minW nil, :w 1, :static false, :isDraggable nil, :isResizable nil, :h 1, :minH nil, :x 3, :maxW nil, :i "2"}
     {:y 3, :maxH nil, :moved false, :minW nil, :w 1, :static false, :isDraggable nil, :isResizable nil, :h 1, :minH nil, :x 4, :maxW nil, :i "3"}
     {:y           4, :maxH nil, :moved false, :minW nil, :w 1, :static false,
      :isDraggable nil, :isResizable nil, :h 1, :minH nil, :x 5, :maxW nil, :i "4"}])

  (def old-layout [{:i 0 :x 0 :y 0 :w 1 :h 1}
                   {:i 1 :x 0 :y 0 :w 1 :h 1}
                   {:i 2 :x 0 :y 0 :w 1 :h 1}
                   {:i 3 :x 0 :y 0 :w 1 :h 1}
                   {:i 4 :x 0 :y 0 :w 1 :h 1}])

  (reduce-layouts layout)
  (apply-updates (nth layout 3) (nth old-layout 3))
  (update-layout old-layout (reduce-layouts layout))

  ())


(comment
  (range 5)

  (one-dummy-content 1 :source-1)

  (make-content 5)


  @(rf/subscribe [:subscribe :source-1])

  (keyword (str "source-" 2))

  [:p (str @(rf/subscribe [:subscribe (keyword (str "source-" 3))]))]

  (rf/dispatch [:empty])

  (for [i (range 10)]
    (if (= 0 (mod i 2))
      (prn "tick " i)))

  (def adb {:source-1 [1 1]
            :source-2 [2 1]
            :source-3 [3 1]})

  (for [x {:source-1 [1 1]
           :source-2 [2 1]
           :source-3 [3 1]}])

  (doseq [[k [t v]] adb]
    (mash-map k [t (if (mod 10 t) (inc v) v)]))

  (map (fn [[k [t v]]]
         {k [t
             (if (= 0 (mod 10 t))
               (inc v)
               v)]}) adb)

  (defn u [t v i]
    (if (= 0 (mod i t))
      (inc v)
      v))

  (reduce-kv (fn [m k [t v]]
               (assoc m k [t (u t v 10)]))
    {}
    adb)

  (def current-tick 4)
  (reduce-kv (fn [m k [t v]]
               (assoc m k [t (tick-update t v current-tick)]))
    {}
    db)

  (rf/dispatch [:tick])

  (rf/dispatch-sync [:add-source :source-1 1 1])
  (rf/dispatch-sync [:add-source :source-2 2 1])
  (rf/dispatch-sync [:add-source :source-3 3 1])

  (rf/subscribe [:subscribe :source-3])

  (def source-1 @(rf/subscribe [:source :source-1]))

  (def db @re-frame.db/app-db)
  (def next-tick (inc (:last-tick db)))
  (def t 3)
  (def v 2)
  (tick-update t v 9)

  (reduce-kv (fn [m k [t v]])
    (assoc m k [t (tick-update t v current-tick)])
    {}
    db)


  ())


