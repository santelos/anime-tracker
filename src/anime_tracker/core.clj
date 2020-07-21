
(ns anime-tracker.core
  (:gen-class)
  (:use ring.adapter.jetty
        ring.util.response)
  (:require [reitit.ring :as ring]
            [selmer.parser :refer [render-file]]
            [anime-tracker.persistence :as persistence]))

(defn handler [request]
  (response (render-file "../resources/index.html" {:titles (persistence/list-of-titles)})))

(defn user-inserter [request]
  (content-type (response (persistence/insert-user (str "Вася" "2")) ) "application/json"))

(def app
  (ring/ring-handler
   (ring/router
    [["/" handler]["/insert-user" user-inserter]])))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Server is starting up")
  (run-jetty #'app {:port 8080}))