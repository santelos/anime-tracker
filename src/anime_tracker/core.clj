
(ns anime-tracker.core
  (:gen-class)
  (:use ring.adapter.jetty
        ring.util.response)
  (:require [reitit.ring :as ring]
            [selmer.parser :refer [render-file]]
            [anime-tracker.persistence :as persistence]))

(defn handler [request]
  (response (render-file "../resources/index.html" {:test (range 10), :titles (doall (persistence/list-of-titles))})))

(defn ping [request]
  (content-type (response ) "application/json"))

(def app
  (ring/ring-handler
   (ring/router
    [["/" handler]])))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Server is starting up")
  (run-jetty #'app {:port 8080}))