(ns anime-tracker.mapping)

(defn mapping-by-title [title]
  (hash-map :id (title :id) :original_name  (title :original_name) :name (title :name) :status (title :status) :link (title :link)  :watched_series (title :watched_series) :total_series (title :total_series)))

(defn extract-user [user]
  (hash-map :id (user :id) :name (user :name_2)))

(defn instruct-users-to-title [titles-with-users]
  (let [[title users] titles-with-users]
    (assoc title :users (into [] (map extract-user users)))))

(defn map-titles [titles]
  (map instruct-users-to-title (seq (group-by mapping-by-title titles))))

(defn extract-title-from-form [title]
  (hash-map :name (title "name") :link (title "link") :name (title "name") :original_name (title "original_name") :watched_series (read-string (title "watched_series")) :total_series (read-string (title "total_series")) :status (read-string (title "status"))))