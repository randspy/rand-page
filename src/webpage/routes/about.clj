(ns webpage.routes.about
  (:require [compojure.core :refer :all]
            [webpage.views.layout :as layout]))

(defn about []
  (layout/common
    [:div#header
     [:div#home-button [:a#button {:href "/"} "Home"]]]))

