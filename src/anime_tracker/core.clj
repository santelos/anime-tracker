
(ns anime-tracker.core
  (:gen-class)
    (:use ring.adapter.jetty
          ring.util.response
          reitit.core))


(defn handler [request]
  (content-type (file-response "index.html" {:root "resources"}) "text/html"))
(defn ping [request]
  (content-type (file-response "index.html" {:root "resources"}) "text/html"))

(defn order [request]
  (content-type (file-response "huindex.html" {:root "resources"}) "text/html"))

(def rr
  (reitit.core/router
    [["/api/ping" ::ping]
     ["/api/orders" ::order]]))
(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Server is starting up")
  (run-jetty rr {:port 8080}))