
(ns anime-tracker.core
  (:gen-class)
  (:use ring.adapter.jetty
        ring.util.response)
  (:require [reitit.ring :as ring]
            [ring.middleware.params :as pp]
            [ring.middleware.keyword-params :as kp]
            [ring.middleware.json :as json]
            [selmer.parser :refer [render-file]]
            [anime-tracker.persistence :as persistence]
            [anime-tracker.mapping :as mapping]
            [anime-tracker.scrap :as scrap]
            [anime-tracker.config :as cfg]
            [ring.middleware.resource :refer [wrap-resource]]))

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

(defn parse-mal [request]
  (response (scrap/scrap-mal (((kp/keyword-params-request (pp/params-request request "UTF-8")) :params) :url))))

(defn favicon [request]
  (file-response "favicon.ico" {:root "resources"}))

(defn get-edit-title [request]
  (response (render-file "../resources/edit-title.html" {:title (first (mapping/map-titles (persistence/title-by-id ((request :path-params) :id)))) :users (persistence/list-of-users)})))

(defn edit-title [request]
  (persistence/update-title-with-users ((pp/assoc-form-params request "UTF-8") :form-params)  (read-string ((request :path-params) :id)))
  (redirect "/"))

(defn increase-series [request]
  (persistence/increase-title-series (read-string ((request :path-params) :id)))
  (redirect "/"))

(def app
  (wrap-resource
   (json/wrap-json-response
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
        ["/edit-title/:id" {:get {:handler get-edit-title} :post {:handler edit-title}}]
        ["/increase-series/:id" {:post {:handler increase-series}}]
        ["/parse-mal" parse-mal]]))) ""))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Server is starting up")
  (run-jetty #'app {:port (cfg/port)}))