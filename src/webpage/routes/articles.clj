(ns webpage.routes.articles
  (:require [compojure.core :refer :all]
            [webpage.views.layout :as layout]
            [webpage.routes.menu :as menu]
            [me.raynes.cegdown :as md]
            [stasis.core :as stasis]))


(def all-articles (atom {}))

(defn read-all-articles-from-files []
  (reset! all-articles (stasis/slurp-directory "posts/" #".*\.(md)$")))

(defn build-article [title content]
  [:div#article-header title
   [:div#article-content content]])



(defn articles []
  (layout/common
    [:div#left
     (menu/vertical-menu)]
    [:div#right
     [:div#about-text
      (build-article (md/to-html "# hi\n")
                     (md/to-html (get @all-articles
                                      "/test.md")))]]))
