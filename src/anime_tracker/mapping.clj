(ns anime-tracker.mapping)

(defn mapping-by-title [title]
  (hash-map :id (title :id)
            :original_name (title :original_name)
            :name (title :name)
            :status (title :status)
            :link (title :link)
            :watched_series (title :watched_series)
            :total_series (title :total_series)
            :comment (title :comment)
            :picture_link (title :picture_link)))

(defn extract-user [user]
  (hash-map :id (user :id_2) :name (user :name_2) :color (user :color)))

(defn instruct-users-to-title [titles-with-users]
  (let [[title users] titles-with-users]
    (assoc title :users (sort-by :id (into [] (map extract-user users))))))

(defn map-titles [titles]
  (map instruct-users-to-title (seq (group-by mapping-by-title titles))))

(defn extract-title-from-form [title]
  (hash-map :name (if (nil? (title "name")) nil (name (title "name")))
            :link (if (nil? (title "link")) nil (name (title "link")))
            :original_name (if (nil? (title "original_name")) nil (name (title "original_name")))
            :watched_series (if (nil? (title "watched_series")) nil (read-string (title "watched_series")))
            :total_series (if (nil? (title "total_series")) nil (read-string (title "total_series")))
            :status (if (nil? (title "status")) nil (read-string (title "status")))
            :comment (if (nil? (title "comment")) nil (name (title "comment")))
            :picture_link (if (nil? (title "picture_link")) nil (name (title "picture_link")))))

(defn mapping-user [user-from-params]
  (hash-map :id (user-from-params "id") :name (user-from-params "name") :color (user-from-params "color")))

(defn mark-user [user, title-users]
  (assoc user :used (contains? title-users (user :id))))

(defn mark-users [title-and-users]
  (assoc title-and-users :users (map (fn[user](mark-user user (group-by :id ((title-and-users :title) :users)))) (title-and-users :users))))