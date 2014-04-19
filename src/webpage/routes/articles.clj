(ns webpage.routes.articles
  (:require [compojure.core :refer :all]
            [clojure.string :as str]
            [clojure.algo.generic.functor :as functor]
            [webpage.views.layout :as layout]
            [webpage.routes.menu :as menu]
            [markdown.core :as md]
            [me.raynes.cegdown :as ceg]
            [clygments.core :as cly]
            [net.cgrand.enlive-html :as enlive]
            [stasis.core :as stasis])
  (:import (java.io StringReader)))


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

(defn generate-all-articles-titles [path articles]
  (vec (cons :div
             (map (fn [elem] [:div elem])
                  (map (fn [article]
                         [:a#article-link
                          {:href (str path (first article))}
                          (:name (second article))]) articles)))))

(defn- extract-code
  [highlighted]
  (-> highlighted
      StringReader.
      enlive/html-resource
      (enlive/select [:pre])
      first
      :content))


(defn- highlight [node]
  (let [code (->> node :content (apply str))
        lang (->> node :attrs :class keyword)]
    (extract-code (cly/highlight code lang :html))))

(defn highlight-code-blocks [page]
  (enlive/sniptest page
                   [:pre :code] highlight
                   [:pre] #(assoc-in % [:attrs :class] "codefont")))


(defn highlight-code-for-posts [posts]
  (into {} (for [[key value] posts]
             [key (highlight-code-blocks (ceg/to-html value [:fenced-code-blocks]))])))

(def all-articles (atom {}))
(def articles-list (atom {}))


(defn read-all-articles-from-files []
  (let [row-articles (file->link
                       (stasis/slurp-directory "posts/" #".*\.(md)$")
                       extract-filename-base)]
    (reset! all-articles (highlight-code-for-posts row-articles))
    (reset! articles-list (generate-all-articles-titles
                            "/articles/"
                            (parse-posts row-articles)))))

(defn list-of-articles []
  (layout/common
    [:div#left
     (menu/vertical-menu)]
    [:div#right
     [:div#about-text
      [:div @articles-list]]]))

(defn articles [text]
  (layout/common
    [:div#left
     (menu/vertical-menu)]
    [:div#right
     [:div#about-text
       (get @all-articles text)]]))

(defroutes article-routes
           (GET "/articles" [] (list-of-articles))
           (GET "/articles/efficiency" [] (articles "efficiency")))
