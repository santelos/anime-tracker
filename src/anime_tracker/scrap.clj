(ns anime-tracker.scrap
    (:use pl.danieljanus.tagsoup)
    (:require [clj-http.client :as httpClient]))

(defn scrap-mal [url]
    (-> url client/get parse-mal-html))

(defn parse-mal-html [html]
    ())
