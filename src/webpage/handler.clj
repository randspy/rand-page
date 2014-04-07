(ns webpage.handler
  (:require [compojure.core :refer [defroutes routes]]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.file-info :refer [wrap-file-info]]
            [hiccup.middleware :refer [wrap-base-url]]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [webpage.routes.home :refer [home-routes]])
  (:use [compojure.core]
        [ring.adapter.jetty :as ring]))

(defn init []
  (println "webpage is starting"))

(defn destroy []
  (println "webpage is shutting down"))

(defroutes app-routes
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (-> (routes home-routes app-routes)
      (handler/site)
      (wrap-base-url)))


(defn -main [port]
  (run-jetty (handler/site app-routes)
     {:port (read-string port) :join? false}))