(ns anime-tracker.scrap
    (:use clojure.core)
    (:require [clj-http.client :as httpClient]
              [hickory.core :as parser]
              [hickory.select :as s]
              [clojure.string :as string]))

(defn filter-series-predicate [item]
  (= (-> (get (:content item) 1) :content first) "Episodes:"))

(defn find-title [html]
  (-> (s/select (s/child
                 (s/class "h1-title")
                 s/first-child) html) first :content first string/trim))

(defn find-total-series [html]
   (string/trim
    (get
     (-> (filterv filter-series-predicate (s/select (s/class "spaceit") html)) first :content)
     2)))

(defn find-picture-link [html]
  (-> (s/select (s/child (s/attr :itemprop #(= "image" %))) html)
      first :attrs :data-src))

(defn parse-mal-html [html, url]
  (hash-map :link url
            :original_name (-> html find-title name)
            :watched_series 0
            :total_series (-> html find-total-series)
            :status 0
            :picture_link (-> html find-picture-link)))

(defn to-parsed-html [html]
  (-> html :body parser/parse parser/as-hickory))

(defn scrap-mal [url]
  (parse-mal-html (to-parsed-html (httpClient/get url)) url))
