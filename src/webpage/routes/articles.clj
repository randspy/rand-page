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
            [clojure.java.io :as io])
  (:import (java.io StringReader))
  (:gen-class))


(defn separate-name-from-text [article separator]
  (let [separated (str/split article separator)
        name (first separated)
        text (or (second separated) "")]
    {:name name
     :text text}))

(defn generate-all-articles-titles [path articles]
  (vec (cons :ul
             (map (fn [elem] [:li elem])
                  (map (fn [article]
                         [:a#article-link
                          {:href (str path (first article))}
                          (:name (second article))]) articles)))))

(defn extract-code-tag-from-pre-tag [highlighted]
  (-> highlighted
      StringReader.
      enlive/html-resource
      (enlive/select [:pre])
      first
      :content))

(defn highlight [node]
  "After highlight we are getting <pre><div><pre>...
   combination of tags, what is incorrect. That why
   we are doing an extraction."
  (let [code (->> node :content (apply str))
        lang (->> node :attrs :class keyword)]
    (extract-code-tag-from-pre-tag (cly/highlight code lang :html))))

(defn highlight-code-tag [page]
  (enlive/sniptest page
                   [:pre :code] highlight
                   [:pre] #(assoc-in % [:attrs :class] "code")))

(defn parse-markdown-file-into-html-structure [posts]
  (into {} (for [[key value] posts]
             [key (highlight-code-tag (ceg/to-html value [:fenced-code-blocks]))])))

(def all-articles (atom {}))
(def articles-list (atom {}))

(defn read-file [filename]
  (->
    filename
    (clojure.java.io/resource)
    (slurp)))

(defn slurp-directory [dir post-names type]
  (into {} (map (fn [post-name]
                  (let [full-file-path (str dir post-name type)]
                    [post-name (read-file full-file-path)])) post-names)))

(defn read-all-articles-from-files []
  (let [row-articles (slurp-directory "public/posts/" ["efficiency"] ".md")]
    (reset! all-articles (parse-markdown-file-into-html-structure row-articles))
    (reset! articles-list (generate-all-articles-titles
                            "/articles/"
                            (functor/fmap
                              #(separate-name-from-text % #"\n-----\n") row-articles)))))

(defn generate-page-with-list-of-articles []
  (layout/common
    [:div#left
     (menu/vertical-menu)]
    [:div#right
      [:div#about-text
        [:div#h1 "Articles"]
        [:div @articles-list]]]))

(defn generate-article-page [text]
  (layout/common
    [:div#left
     (menu/vertical-menu)]
    [:div#right
     [:div#about-text
       (get @all-articles text)]
     [:div#footer [:a {:href "/articles"} "Back"]]]))

(defroutes article-routes
           (GET "/articles" [] (generate-page-with-list-of-articles))
           (GET "/articles/efficiency" [] (generate-article-page "efficiency")))
