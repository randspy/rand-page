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


(defn split-token-from-text
  "From 'test name token' we will get {:test 'name token'}"
  [token]
  (let [separated (str/split token #" ")
        key (keyword (first separated))
        content (str/join " " (rest separated))]
    [key content]))

(defn separate-header-elements [elements]
  (let [separated (str/split elements #"\n")]
    (flatten
      (functor/fmap split-token-from-text separated))))

(defn separate-header-from-text [article separator]
  (let [separated (str/split article separator)
        header (first separated)
        text (or (second separated) "")]
    {:header header
     :text text}))

(defn separate-article-elements [article]
  (let [separated (separate-header-from-text article #"\n-----\n")
        header (:header separated)
        text (:text separated)]
    (apply hash-map (flatten [(separate-header-elements header) :text text]))))

(defn generate-all-articles-titles [path articles]
  (vec (cons :ul
             (map (fn [elem] [:li elem])
                  (map (fn [article]
                         [:a#article-link
                          {:href (str path (first article))}
                          (str/join " " [(:date (second article)) " - "
                                         (:title (second article))])]) articles)))))

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
             [key (highlight-code-tag (ceg/to-html (:text value) [:fenced-code-blocks]))])))

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

(defn generate-page-with-list-of-articles []
  (layout/common
    [:div#left
     (menu/vertical-menu)]
    [:div#right
      [:div#about-text
        [:div#h1 "Articles :"]
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
           (GET "/articles/2014-04-20-efficiency" []
                (generate-article-page "2014-04-20-efficiency"))
           (GET "/articles/2014-05-08-function-params" []
                (generate-article-page "2014-05-08-function-params")))

(defn read-all-articles-from-files []
  (let [row-articles (slurp-directory "public/posts/" ["2014-04-20-efficiency"
                                                       "2014-05-08-function-params"] ".md")
        articles-elements (functor/fmap
                            #(separate-article-elements %) row-articles)]
    (reset! all-articles (parse-markdown-file-into-html-structure articles-elements))
    (reset! articles-list (generate-all-articles-titles "/articles/" articles-elements))))
