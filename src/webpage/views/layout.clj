(ns webpage.views.layout
  (:require [hiccup.page :refer [html5 include-css]]))

(defn common [& body]
  (html5
    [:head
     [:title "randspy.com"]
     [:link {:href "//fonts.googleapis.com/css?family=Esteban:400" ,
             :rel "stylesheet"
             :type "text/css"}]
     (include-css "/css/screen.css")
     (include-css "/css/menu.css")
     (include-css "/css/clock.css")
     "<!--[if lt IE 9]>"
     [:script {:src "http://html5shiv.googlecode.com/svn/trunk/html5.js"}]
     "<![endif]-->"]
    [:body body]))
