(ns webpage.routes.menu
  (:require [webpage.views.layout :as layout]))

(defn vertical-menu []
  (layout/common
    [:div#main
      [:ul#navigationMenu
       [:li [:a {:class "home" :href "/"} [:span "Home"]]]
       [:li [:a {:class "about" :href "/about"} [:span "About"]]]
       [:li [:a {:class "articles" :href "/articles"} [:span "Articles"]]]
       [:li [:a {:class "linkedin" :href "https://fr.linkedin.com/pub/przemyslaw-koziel/35/0/947/"} [:span "LinkedIn"]]]
       [:li [:a {:class "github" :href "https://github.com/randspy"} [:span "Github"]]]]]))
