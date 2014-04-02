(ns clock.utils)

(defn get-time []
  (let [date (js/Date.)]
    {:hours (.getHours date)
     :minutes (.getMinutes date)
     :seconds (.getSeconds date)}))

(defn parse-date-into-string [{:keys [hours minutes seconds]}]
  (str hours ":" minutes ":" seconds))