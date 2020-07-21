
(ns anime-tracker.core
  (:gen-class)
  (:use ring.adapter.jetty
        ring.util.response)
  (:require [reitit.ring :as ring]
            [selmer.parser :refer [render-file]]))

(defn handler [request]
  (response (render-file "../resources/index.html" {:test (range 10)})))

  ; (content-type (file-response "index.html" {:root "resources"}) "text/html"))

;(defn ping [request]
;  (content-type (file-response "index.html" {:root "resources"}) "text/html"))
;
;(defn order [request]
;  (content-type (file-response "huindex.html" {:root "resources"}) "text/html"))

(def app
  (ring/ring-handler
   (ring/router
    [["/all" handler]])))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Server is starting up")
  (run-jetty #'app {:port 8080}))