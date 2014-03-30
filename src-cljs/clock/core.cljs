(ns clock.core)

(defn ^:export main []
  (.write js/document "<h1>This is a sparta!!</h1>" ))