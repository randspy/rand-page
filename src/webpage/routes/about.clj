(ns webpage.routes.about
  (:require [compojure.core :refer :all]
            [webpage.views.layout :as layout]
            [webpage.routes.menu :as menu]))

(defn about []
  (layout/common
    [:div#left
     (menu/vertical-menu)]
    [:div#right
      [:div#about-text
        [:p "This blog/webpage was developed with using "
         [:a#link-text {:href "http://clojure.org/"} "clojure"]
         " at the server side and "
         [:a#link-text {:href "http://clojure.org/clojurescript"} "clojurescript"]
         " at the client side."]
        [:p "Menu is a variation of "
          [:a#link-text {:href "http://demo.tutorialzine.com/2010/06/css3-minimalistic-navigation-menu/demo.html"} "css3"]
          " demo. Homepage binary clock was inspired by "
          [:a#link-text {:href "http://holmsand.github.io/reagent/news/binary-clock.html"} "holmsand"]
          ". Repository with the source code you can be found on "
          [:a#link-text {:href "https://github.com/randspy/webpage"} "github"]
          "."]]]))
