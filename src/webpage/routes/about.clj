(ns webpage.routes.about
  (:require [compojure.core :refer :all]
            [webpage.views.layout :as layout]
            [webpage.routes.menu :as menu]))

(defn about []
  (layout/common
    [:div#header
     (menu/vertical-menu)]
    [:div#main-about
      [:ul [:li [:a#link-text {:href "http://clojure.org/"} "This webpage was developed in clojure."]]]]))
