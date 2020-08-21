(ns anime-tracker.persistence
    (:require [clojure.java.jdbc :as jdbc]
              [anime-tracker.mapping :as mapping]
              [anime-tracker.config :as cfg]))

(def pg
  {:dbtype "postgresql" :connection-uri (cfg/database-url) :user (cfg/database-user) :password (cfg/database-password)})


(defn list-of-titles []
  (jdbc/query pg
            ["SELECT * FROM titles AS t
                LEFT JOIN titles_2_users AS t2u ON t.id = t2u.title_id
                LEFT JOIN users AS u ON u.id = t2u.user_id"]))

(defn title-by-id [id]
  (jdbc/query pg
            ["SELECT * FROM titles AS t
                LEFT JOIN titles_2_users AS t2u ON t.id = t2u.title_id
                LEFT JOIN users AS u ON u.id = t2u.user_id
                WHERE t.id = ?" (read-string id)]))

(defn list-of-users []
  (jdbc/query pg
            ["SELECT * FROM users ORDER BY id ASC"]))

(defn insert-user [user]
  (jdbc/insert! pg :users {:name (user :name) :color (user :color)}))

(defn update-user [user]
  (jdbc/update! pg :users {:name (user :name) :color (user :color)} ["id = ?" (read-string (user :id))]))

(defn delete-user [user]
  (jdbc/delete! pg :users ["id = ?" (read-string (user :id))]))

(defn insert-title-with-users [title]
  (jdbc/with-db-transaction [tx pg]
    (let [new-title (jdbc/insert! tx :titles (mapping/bump-title (mapping/extract-title-from-form title)) {:return-keys ["id"]})]
      (cond
        (nil? (title "users")) ()
        (vector? (title "users")) (doseq [user (title "users")] (jdbc/insert! tx :titles_2_users (hash-map :title_id ((first new-title) :id) :user_id (read-string user))))
        :else  (jdbc/insert! tx :titles_2_users (hash-map :title_id ((first new-title) :id) :user_id (read-string (title "users"))))))))


(defn update-title-with-users [title id]
  (jdbc/with-db-transaction [tx pg]
    (jdbc/update! tx :titles (mapping/bump-title (mapping/extract-title-from-form title)) ["id = ?" id])
    (jdbc/delete! tx :titles_2_users ["title_id = ?" id])
    (cond
      (nil? (title "users")) ()
      (vector? (title "users")) (doseq [user (title "users")] (jdbc/insert! tx :titles_2_users (hash-map :title_id id :user_id (read-string user))))
      :else  (jdbc/insert! tx :titles_2_users (hash-map :title_id id :user_id (read-string (title "users")))))))

(defn increase-title-series [id]
  (jdbc/execute! pg
    ["UPDATE titles SET watched_series = watched_series + 1, updated_at = ? WHERE id = ?" (System/currentTimeMillis) id])
  (let [res (first (jdbc/query pg ["SELECT watched_series, total_series FROM titles WHERE id = ?" id]))]
    (cond
     (= (res :watched_series) (res :total_series)) (jdbc/update! pg :titles {:status 3} ["id = ?" id])
     (< 0 (res :watched_series)) (jdbc/update! pg :titles {:status 1} ["id = ?" id]))))

;  (if (let [res (first (jdbc/query pg ["SELECT watched_series, total_series FROM titles WHERE id = ?" id]))]
;        (= (res :watched_series) (res :total_series)))
;    (jdbc/update! pg :titles {:status 3} ["id = ?" id])))

(defn list-of-unwatched-titles []
  (jdbc/query pg
              ["SELECT t.id, t.original_name FROM titles AS t
                WHERE t.status = 0"]))