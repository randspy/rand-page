(ns webpage.test.routes.articles
  (:use expectations
        ring.mock.request
        webpage.routes.articles)
  (:require [clojure.string :as str]))

;;separate a name from a text
(expect {:header "name" :text "text"}
        (separate-header-from-text "name\n-----\ntext" #"\n-----\n"))

;;when there is no text an empty one should be returden
(expect {:header "name" :text ""}
        (separate-header-from-text "name\n-----\n" #"\n-----\n"))
(expect {:header "name" :text ""}
        (separate-header-from-text "name" #"\n-----\n"))

;;when input is empty should return empty
(expect {:header "" :text ""}
        (separate-header-from-text "" #"\n-----\n"))

(expect '(:date "10/10/1000" :title "name")
        (separate-header-elements "date 10/10/1000\ntitle name"))

(expect {:title "name" :date "10/10/1000" :text "text"}
        (separate-article-elements "date 10/10/1000\ntitle name\n-----\ntext"))