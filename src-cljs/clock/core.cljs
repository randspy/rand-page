(ns clock.core
  (:require
    [reagent.core :as reagent :refer [atom]]))

(defn cell [n bit]
  [:div.clock-cell {:class (if (bit-test n bit)
                             "light"
                             "dark")}])

(defn column [n is-hovering]
  [:div.clock-col
   [cell n 3]
   [cell n 2]
   [cell n 1]
   [cell n 0]
   [:div.clock-cell.number 
     {:class (if is-hovering
               "show"
               "not-show")} n]])

(defn column-pair [n is-hovering]
  [:div.clock-pair
   [column (quot n 10) is-hovering]
   [column (mod n 10) is-hovering]])

(defn clock [date is-hovering toggle-hovering]
  [:div.clock-main {:onMouseEnter toggle-hovering
                    :onMouseLeave toggle-hovering}
   [column-pair (.getHours date) is-hovering]
   [column-pair (.getMinutes date) is-hovering]
   [column-pair (.getSeconds date) is-hovering]])

(def clock-state (atom {:time (js/Date.)
                        :is-hovering false}))

(defn update-time []
  (swap! clock-state assoc :time (js/Date.)))

(defn main []
  (let [{:keys [time is-hovering]} @clock-state]
    (js/setTimeout update-time 1000)
    [clock time is-hovering
     #(swap! clock-state update-in [:is-hovering] not)]))

(defn ^:export run [parent-node]
  (reagent/render-component [main]
    (.getElementById js/document parent-node)))