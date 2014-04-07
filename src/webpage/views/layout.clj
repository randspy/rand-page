(ns webpage.views.layout
  (:require [hiccup.page :refer [html5 include-css]]))

(defn common [& body]
  (html5
    [:head
     [:title "randspy.com"]
     (include-css "/css/screen.css")
     (include-css "/css/menu.css")
     (include-css "/css/clock.css")]
    [:body body]))
