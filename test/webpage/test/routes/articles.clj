(ns webpage.test.routes.articles
  (:use expectations
        ring.mock.request
        webpage.routes.articles))

;;separate a name from a text
(expect {:name "name" :text "text"}
        (separate-name-from-text "name\n-----\ntext" #"\n-----\n"))

;;when there is no text an empty one should be returden
(expect {:name "name" :text ""}
        (separate-name-from-text "name\n-----\n" #"\n-----\n"))
(expect {:name "name" :text ""}
        (separate-name-from-text "name" #"\n-----\n"))

;;when input is empty should return empty
(expect {:name "" :text ""}
        (separate-name-from-text "" #"\n-----\n"))

;;generate a list of html tags for a post structure
(expect [:ul
         [:li [:a#article-link {:href "/articles/text2"} "name2"]]
         [:li [:a#article-link {:href "/articles/text"} "name"]]]
        (generate-all-articles-titles
          "/articles/"
          {"text" {:name "name" :text "text"}
           "text2" {:name "name2" :text "text2"}}))

;;extract file name without slash and a filetype
(expect "test"
        (extract-filename-base "/test.md"))


;;extract link from a file name
(expect {"text" "name"}
        (post-filename-to-article-page-link {"/text.md" "name"}
                    extract-filename-base))