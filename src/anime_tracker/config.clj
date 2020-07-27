(ns anime-tracker.config
  (:require [environ.core :refer [env]]))


(defn port []
  (read-string (or (env :port) "8080")))

(defn database-url []
  (or (env :jdbc-database-url) (env :database-url) "jdbc:postgresql://localhost:5432/postgres"))

(defn database-password []
  (or (env :jdbc-database-password) "postgres"))

(defn database-user []
  (or (env :jdbc-database-username) "postgres"))