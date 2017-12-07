(defproject aoc2015 "0.1.0-SNAPSHOT"
  :description "Solutions for adventofcode"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [clj-http "3.7.0"]
                 [org.clojure/data.json "0.2.6"]
                 [org.clojure/math.combinatorics "0.1.4"]]
  :target-path "target/%s"
  :plugins [[lein-cloverage "1.0.6"]]
  :profiles {:uberjar {:aot :all}})
