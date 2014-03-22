(ns webpage.routes.home
  (:require [compojure.core :refer :all]
            [webpage.views.layout :as layout]))

(defn home []
  (layout/common
    [:div#header
     [:div#home-button [:a#button {:href "/"} "Home"]]
     [:div#about-button [:a#button {:href "http://github.com"} "About"]]]))

(defroutes home-routes
  (GET "/" [] (home)))
