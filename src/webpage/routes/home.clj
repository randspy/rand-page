(ns webpage.routes.home
  (:require [compojure.core :refer :all]
            [webpage.views.layout :as layout]))

(defn home []
  (layout/common
    [:a#button {:href "http://github.com"} "About"]))

(defroutes home-routes
  (GET "/" [] (home)))
