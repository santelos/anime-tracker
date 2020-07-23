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