(ns clock.core
  (:require [goog.dom :as dom])
  (:import [goog.ui Zippy]))

(defn getElementId [element]
  (.getElementById js/document element))

(defn ^:export main [parent-node]
  (dom/append (getElementId parent-node)
              (dom/createDom "h1" nil "This is a sparta!!")))