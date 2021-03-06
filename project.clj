(defproject anime-tracker "0.1.1"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [ring/ring-core "1.8.1"]
                 [ring/ring-jetty-adapter "1.8.1"]
                 [ring/ring-json "0.5.0"]
                 [metosin/reitit-ring "0.5.5"]
                 [org.clojure/java.jdbc "0.7.11"]
                 [selmer "1.12.27"]
                 [org.postgresql/postgresql "42.2.14"]
                 [clj-http "3.10.1"]
                 [hickory "0.7.1"]
                 [environ "1.2.0"]]
  :main ^:skip-aot anime-tracker.core
  :uberjar-name "anime-tracker.jar"
  :target-path "target/%s"
  :min-lein-version "2.0.0"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
