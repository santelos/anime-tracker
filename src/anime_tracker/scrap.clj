(ns anime-tracker.scrap
    (:use clojure.core)
    (:require [clj-http.client :as httpClient]
              [hickory.core :as parser]
              [hickory.select :as s]
              [clojure.string :as string]))

(defn parse-mal-html [html]
  (-> (s/select (s/class "h1-title") html) :content string/trim))

(defn to-parsed-html [html]
  (-> html :body parser/parse parser/as-hickory))

(defn scrap-mal [url]
  (-> (httpClient/get url) to-parsed-html))
