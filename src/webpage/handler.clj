(ns webpage.handler
  (:require [compojure.core :refer [defroutes routes]]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.file-info :refer [wrap-file-info]]
            [hiccup.middleware :refer [wrap-base-url]]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [webpage.routes.home :refer [home-routes]]
            [webpage.routes.articles :refer [read-all-articles-from-files]])
  (:use [compojure.core]
        [ring.adapter.jetty :as ring]))

(defn init []
  (println "webpage is starting")
  (read-all-articles-from-files))

(defn destroy []
  (println "webpage is shutting down"))

(defroutes app-routes
  (route/resources "/")
  (route/not-found "This is not a webpage you are looking for"))

(def app
  (-> (routes home-routes app-routes)
      (handler/site)
      (wrap-base-url)
      ))


(defn -main [port]
  (run-jetty (handler/site (routes home-routes app-routes))
     {:port (read-string port) :join? false}))