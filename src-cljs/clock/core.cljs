(ns clock.core
  (:require [clock.utils :as ut]
            [reagent.core :as reagent :refer [atom]]))

(def timer (atom (js/Date.)))

(defn update-time [time]
  ;; Update the time every 1/10 second to be accurate...
  (js/setTimeout #(reset! time (js/Date.)) 100))

(defn clock []
  (update-time timer)
  (let [time-str (-> @timer .toTimeString (clojure.string/split " ") first)]
    [:div.example-clock
     time-str]))

(defn clock-display []
  [:div [clock]])

(defn ^:export run [parent-node]
  (reagent/render-component [clock-display]
                            (.getElementById js/document "clock")))