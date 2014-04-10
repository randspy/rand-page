(ns clock.core
  (:require
    [reagent.core :as reagent :refer [atom]]))

(defn cell [n bit]
  [:div.clock-cell {:class (if (bit-test n bit)
                             "light"
                             "dark")}])

(defn row [n is-hovering]
  [:div.clock-row
   [cell n 5]
   [cell n 4]
   [cell n 3]
   [cell n 2]
   [cell n 1]
   [cell n 0]
   [:div.clock-cell.number
    {:class (if is-hovering
              "show"
              "not-show")} n]])

(defn clock [date is-hovering toggle-hovering]
  [:div.clock-main {:onMouseEnter toggle-hovering
                    :onMouseLeave toggle-hovering}
   [row (.getHours date) is-hovering]
   [row (.getMinutes date) is-hovering]
   [row (.getSeconds date) is-hovering]])

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