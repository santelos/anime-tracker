
(ns anime-tracker.core
  (:gen-class)
  (:use ring.adapter.jetty
        ring.util.response)
  (:require [reitit.ring :as ring]
            [ring.middleware.params :as pp]
            [selmer.parser :refer [render-file]]
            [anime-tracker.persistence :as persistence]
            [anime-tracker.mapping :as mapping]))

(defn handler [request]
  (response (render-file "../resources/index.html" {:titles (mapping/map-titles (persistence/list-of-titles)) :users (persistence/list-of-users)})))

(defn user-inserter [request]
  (content-type (response (persistence/insert-user (str "Вася" "2")) ) "application/json"))

(defn add-title [request]
  (persistence/insert-title-with-users ((pp/assoc-form-params request "UTF-8") :form-params))
  (redirect "/"))

(def app
  (ring/ring-handler
   (ring/router
    [["/" handler]
     ["/add-title" add-title]
     ["/insert-user" user-inserter]])))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Server is starting up")
  (run-jetty #'app {:port 8080}))