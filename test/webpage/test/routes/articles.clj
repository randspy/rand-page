(ns webpage.test.routes.articles
  (:use expectations
        ring.mock.request
        webpage.routes.articles))

;;separate a name from a text
(expect {:name "name" :text "text"}
        (separate-name-from-text "name\n-----\ntext"))

;;when there is no text an empty one should be returden
(expect {:name "name" :text ""}
        (separate-name-from-text "name\n-----\n"))
(expect {:name "name" :text ""}
        (separate-name-from-text "name"))

;;when input is empty should return empty
(expect {:name "" :text ""}
        (separate-name-from-text ""))

(expect {"/text.md" {:name "name" :text "text"}
         "/text2.md" {:name "name2" :text "text2"}}
         (parse-posts {"/text.md" "name\n-----\ntext"
                       "/text2.md" "name2\n-----\ntext2"}))

;;generate a list of html tags for a post structure
(expect [:div
         [:div [:a#article-link {:href "/articles/text2"} "name2"]]
         [:div [:a#article-link {:href "/articles/text"} "name"]]]
        (generate-all-articles-titles
          "/articles/"
          {"text" {:name "name" :text "text"}
           "text2" {:name "name2" :text "text2"}}))



;;extract file name without slash and a filetype
(expect "test"
        (extract-filename-base "/test.md"))


;;extract link from a file name
(expect {"text" "name"}
        (file->link {"/text.md" "name"}
                    extract-filename-base))