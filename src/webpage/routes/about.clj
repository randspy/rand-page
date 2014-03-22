(ns webpage.routes.about
  (:require [compojure.core :refer :all]
            [webpage.views.layout :as layout]))

(defn about []
  (layout/common
    [:div#header
     [:div#home-button [:a#button {:href "/"} "Home"]]
     [:div#header-text [:b "The truth is out there."]]
     [:div#clear]]
    [:div#main-about
      [:ul [:li [:a#link-text {:href "http://clojure.org/"} "This webpage was developed in clojure."]]
           [:li [:a#link-text {:href "https://github.com/randspy/webpage"} "A github repository."]]
           [:li [:a#link-text {:href "https://fr.linkedin.com/pub/przemyslaw-koziel/35/0/947/"} "About me."]]]]))
