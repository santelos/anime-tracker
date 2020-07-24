(ns anime-tracker.mapping)

(defn mapping-by-title [title]
  (hash-map :id (title :id) :original_name  (title :original_name) :name (title :name) :status (title :status) :link (title :link)  :watched_series (title :watched_series) :total_series (title :total_series)))

(defn extract-user [user]
  (hash-map :id (user :id_2) :name (user :name_2) :color (user :color)))

(defn instruct-users-to-title [titles-with-users]
  (let [[title users] titles-with-users]
    (assoc title :users (sort-by :id (into [] (map extract-user users))))))

(defn map-titles [titles]
  (sort-by :id (map instruct-users-to-title (seq (group-by mapping-by-title titles)))))

(defn extract-title-from-form [title]
  (hash-map :name (name (title "name")) :link (name (title "link")) :original_name (name (title "original_name")) :watched_series (read-string (title "watched_series")) :total_series (read-string (title "total_series")) :status (read-string (title "status"))))

(defn mapping-user [user-from-params]
  (hash-map :id (user-from-params "id") :name (user-from-params "name") :color (user-from-params "color")))
