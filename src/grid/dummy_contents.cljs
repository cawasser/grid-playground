(ns grid.dummy-contents
  (:require [re-frame.core :as rf]
            [day8.re-frame.tracing :refer-macros [fn-traced defn-traced]]))




(rf/reg-event-db
  :init-db
  (fn-traced [db _]
    (prn "init app-db")
    (assoc db
      :widgets {:source-0 [1 0]
                :source-1 [1 0]
                :source-2 [2 0]
                :source-3 [3 0]
                :source-4 [4 0]}
      :layout {:source-0 {:x 0 :y 0 :w 1 :h 1}
               :source-1 {:x 1 :y 0 :w 1 :h 1}
               :source-2 {:x 2 :y 0 :w 1 :h 1}
               :source-3 {:x 3 :y 0 :w 1 :h :1}
               :source-4 {:x 4 :y 0 :w 1 :h :1}}

      :last-tick 0)))




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
    ;(prn "tick!!!!!")
    (let [next-tick (inc (:last-tick db))
          ret       (reduce-kv (fn [m k [t v]]
                                 (assoc m k [t (tick-update t v next-tick)]))
                      {}
                      (:widgets db))]
      ;(prn ":tick" ret)
      (assoc db
        :last-tick next-tick
        :widgets ret))))



(rf/reg-sub
  :sources
  (fn [db _]
    ;(prn ":subscribe" source-id)
    (-> db
      :widgets)))



(rf/reg-sub
  :subscribe
  (fn [db [_ source-id]]
    ;(prn ":subscribe" source-id)
    (-> db
      :widgets
      source-id)))


(rf/reg-sub
  :layout-widget
  (fn [db [_ source-id]]
    ;(prn ":layout-widget" source-id)
    (-> db
      :layout
      source-id)))


(rf/reg-sub
  :layout
  (fn [db _]
    (-> db :layout)))




(defn tick [current-tick [trigger current-val]]
  (if (= 0 (mod current-tick trigger))
    (inc current-val)
    current-val))





(comment
  (def context (atom nil))
  (def id :source-2)
  (def c (rf/subscribe [:subscribe id]))

  (set-drag context id)

  ())

;(defn make-content [n]
;  (into []
;    (for [idx (range n)]
;      (one-widget-content idx (keyword (str "source-" idx))))))









(comment
  (range 5)

  (one-dummy-content 1 :source-1)

  (make-content 5)


  @(rf/subscribe [:subscribe :source-1])

  (keyword (str "source-" 2))

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