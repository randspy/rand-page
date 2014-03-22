(ns webpage.routes.home
  (:require [compojure.core :refer :all]
            [webpage.views.layout :as layout]
            [webpage.routes.about :as about-page]))

(defn home []
  (layout/common
    [:div#header
     [:div#home-button [:a#button {:href "/"} "Home"]]
     [:div#about-button [:a#button {:href "/about"} "About"]]]))

(defroutes home-routes
  (GET "/" [] (home))
  (GET "/about" [] (about-page/about)))
