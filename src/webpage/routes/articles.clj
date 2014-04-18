(ns webpage.routes.articles
  (:require [compojure.core :refer :all]
            [clojure.string :as str]
            [clojure.algo.generic.functor :as functor]
            [webpage.views.layout :as layout]
            [webpage.routes.menu :as menu]
            [me.raynes.cegdown :as md]
            [stasis.core :as stasis]))


(defn separate-name-from-text [article]
  (let [separated (str/split article #"\n-----\n")
        name (first separated)
        text (or (second separated) "")]
    {:name name
     :text text}))

(defn extract-filename-base [text]
  (clojure.string/replace
    (clojure.string/replace text #"/" "")
    #".md" ""))

(defn file->link [posts func]
  (into {} (for [[key value] posts]
             [(func key) value])))

(defn parse-posts [posts]
  (functor/fmap separate-name-from-text posts))

(def all-articles (atom {}))

(defn read-all-articles-from-files []
  (reset! all-articles (stasis/slurp-directory "posts/" #".*\.(md)$")))

(defn build-article [title content]
  [:div#article-header title
   [:div#article-content content]])

(defn articles [header text]
  (layout/common
    [:div#left
     (menu/vertical-menu)]
    [:div#right
     [:div#about-text
      (build-article (md/to-html header)
                     (md/to-html (get @all-articles
                                      text)))]]))

(defn generate-all-articles-titles [path articles]
  (vec (cons :div
         (map (fn [elem] [:div elem])
              (map (fn [article]
                     [:a#article-link
                      {:href (str path (first article))}
                      (:name (second article))]) articles)))))


(defn list-of-articles []
  (layout/common
    [:div#left
     (menu/vertical-menu)]
    [:div#right
     [:div#about-text
      [:div
       (generate-all-articles-titles
         "/articles/"
         (parse-posts
           (file->link @all-articles
                       extract-filename-base)))]]]))


(defroutes article-routes
           (GET "/articles" [] (list-of-articles))
           (GET "/articles/test" [] (articles "hi\n" "/test.md"))
           (GET "/articles/test2" [] (articles "hi\n" "/test2.md")))
