(ns anime-tracker.persistence
    (:require [clojure.java.jdbc :as jdbc]
              [anime-tracker.mapping :as mapping]))

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

(defn list-of-users []
    (jdbc/query pg
              ["SELECT * FROM users"]))

(defn insert-user [user]
  (jdbc/insert! pg :users {:name (user :name)}))

(defn delete-user [user]
  (jdbc/delete! pg :users ["id = ?" (read-string (user :id))])
  )

(defn insert-title-with-users [title]
  (let [new-title (jdbc/insert! pg :titles  (mapping/extract-title-from-form title) {:return-keys ["id"]})]
    (doseq [user (title "users")] (jdbc/insert! pg :titles_2_users (hash-map :title_id ((first new-title) :id) :user_id (read-string user))))))
