(ns anime-tracker.config
  (:require [environ.core :refer [env]]))


(defn port []
  (read-string (or (env :port) "8080")))

(defn database-url []
  (or (env :db-url) "jdbc:postgresql://localhost:5432/postgres"))

(defn database-password []
  (or (env :db-password) "postgres"))

(defn database-user []
  (or (env :db-user) "postgres"))