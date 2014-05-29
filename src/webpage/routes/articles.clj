(ns webpage.routes.articles
  (:require [compojure.core :refer :all]
            [clojure.string :as str]
            [clojure.algo.generic.functor :as functor]
            [webpage.views.layout :as layout]
            [webpage.routes.menu :as menu]
            [clojure.java.io :as io]
            [webpage.routes.markdown :as md])
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
  (vec (cons :div
             (map (fn [elem] [:div elem])
                  (map (fn [article]
                         [:a#article-link
                          {:href (str path (first article))}
                          [:div#article-in-list
                           [:div#article-date-frame
                            [:div#article-date (:date (second article))]]
                           [:div#article-name
                            [:div#reverse (:title (second article))]]]]) articles)))))

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
        [:div @articles-list]]]))

(defn generate-article-page [text]
  (layout/common
    [:div#left
     (menu/vertical-menu)]
    [:div#right
     [:div#about-text
       (get @all-articles text)]
     [:div#footer
      [:div#footer-empty-space]
      [:a#reverse {:href "/articles"} "Back"]]]))

(defroutes article-routes
           (GET "/articles" [] (generate-page-with-list-of-articles))
           (GET "/articles/2014-04-20-efficiency" []
                (generate-article-page "2014-04-20-efficiency"))
           (GET "/articles/2014-05-08-function-params" []
                (generate-article-page "2014-05-08-function-params")))

(defn read-all-articles-from-files []
  (let [row-articles (slurp-directory "public/posts/" ["2014-05-08-function-params"
                                                        "2014-04-20-efficiency"] ".md")
        articles-elements (functor/fmap
                            #(separate-article-elements %) row-articles)]
    (reset! all-articles (md/parse-markdown-file-into-html-structure articles-elements))
    (reset! articles-list (generate-all-articles-titles "/articles/" articles-elements))))
