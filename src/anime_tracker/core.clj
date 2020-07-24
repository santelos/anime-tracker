
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

(defn show-users [request]
  (response (render-file "../resources/users.html" {:users (persistence/list-of-users)})))

(defn user-inserter [request]
  (persistence/insert-user (mapping/mapping-user ((pp/assoc-form-params request "UTF-8") :form-params)))
  (redirect "/users"))

(defn user-updater [request]
  (persistence/update-user (mapping/mapping-user ((pp/assoc-form-params request "UTF-8") :form-params)))
  (redirect "/users"))

(defn user-deleter [request]
      (response (persistence/delete-user (mapping/mapping-user ((pp/assoc-form-params request "UTF-8") :form-params))))
  )

(defn add-title [request]
  (persistence/insert-title-with-users ((pp/assoc-form-params request "UTF-8") :form-params))
  (redirect "/"))

(defn favicon [request]
  (file-response "favicon.ico" {:root "resources"}))

(defn get-edit-title [request]
  (response (render-file "../resources/edit-title.html" {:title (first (mapping/map-titles (persistence/title-by-id ((request :path-params) :id)))) :users (persistence/list-of-users)})))

(def app
  (ring/ring-handler
   (ring/router
    [
      ["/" handler]
      ["/add-title" add-title]
      ["/users" show-users]
      ["/insert-user" user-inserter]
      ["/delete-user" user-deleter]
      ["/update-user" user-updater]
      ["/favicon.ico" favicon]
      ["/edit-title/:id" get-edit-title]
     ])))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Server is starting up")
  (run-jetty #'app {:port 8080}))