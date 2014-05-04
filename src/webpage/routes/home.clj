(ns webpage.routes.home
  (:require [compojure.core :refer :all]
            [webpage.views.layout :as layout]
            [webpage.routes.menu :as menu]
            [webpage.routes.about :as about-page]
            [webpage.routes.articles :as articles]))

(defn home []
  (layout/common
    [:div#header
     [:div#left
      (menu/vertical-menu)]
     [:div#right
      [:div#clock
       "<!--[if lte IE 8]> Internet Exploler 8 and balow are not supported.<![endif]-->"
       "<![if gt IE 8]-->"
       [:script {:src "js/main.js" :type "text/javascript"}]
       [:script {:src "http://fb.me/react-0.9.0.js"}]
       [:script "clock.core.run(\"clock\")"]
       "<![endif]-->"
       "<!--[if !IE]>"
       [:script {:src "js/main.js" :type "text/javascript"}]
       [:script {:src "http://fb.me/react-0.9.0.js"}]
       [:script "clock.core.run(\"clock\")"]
       "<![endif]-->"]]]))

(defroutes home-routes
  (GET "/" [] (home))
  (GET "/about" [] (about-page/about)))
