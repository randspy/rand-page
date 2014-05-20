(ns webpage.routes.markdown
  (:require [me.raynes.cegdown :as ceg]
            [clygments.core :as cly]
            [net.cgrand.enlive-html :as enlive])
  (:import (java.io StringReader)))


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
  (into {} (for [[post-name post-content] posts]
             [post-name (highlight-code-tag
                          (ceg/to-html (:text post-content)
                                       [:fenced-code-blocks]))])))