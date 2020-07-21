(ns anime-tracker.persistence
    (:require [clojure.java.jdbc :as jdbc]))

;(def loadDriver
;  ((println "1231342543456")
;    (Class/forName "org.postgresql.Driver")))

;(def pg
;  {:connection-uri (str "postgresql://postgres:postgres@localhost:5432/postgres")})

(def pg
  {:dbtype "postgresql"
   :dbname "postgres"
   :host "localhost"
   :user "postgres"
   :password "postgres"})


(defn list-of-titles []
    (jdbc/query pg
              ["SELECT * FROM titles AS t
                  LEFT JOIN titles_2_users AS t2u ON t.id = t2u.title_id
                  LEFT JOIN users AS u ON u.id = t2u.user_id"]))

(defn insert-user [user]
  (jdbc/insert-multi! pg :users
                      [{:name user}]))


