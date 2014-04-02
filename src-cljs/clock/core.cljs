(ns clock.core
  (:require [goog.dom :as dom]
            [goog.events :as gevents]
            [goog.Timer :as timer]
            [clock.utils :as ut]))


(defn getElementId [element]
  (.getElementById js/document element))

(defn update-clock []
  (dom/setTextContent (getElementId "bit")
                      (ut/parse-date-into-string (ut/get-time))))

(defn create-button []
  (dom/createDom "a" (clj->js {"id" "bit"})
                 (ut/parse-date-into-string (ut/get-time))))

(defn add-to-html [parent-node]
  (dom/append (getElementId parent-node)
              (create-button)))

(defn ^:export main [parent-node]
  (let [time (goog.Timer. 1000)]
    (gevents/listen time timer/TICK update-clock)
    (. time (start))
    (add-to-html parent-node)))