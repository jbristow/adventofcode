(defproject aoc2015 "0.1.0-SNAPSHOT"
  :description "Solutions for adventofcode"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [clj-http "2.0.0"]
                 [org.clojure/data.json "0.2.6"]
                 [org.clojure/math.combinatorics "0.1.1"]]
  :target-path "target/%s"
  :repl-options {:init-ns aoc2015.core}
  :plugins [[lein-cloverage "1.0.6"]]
  :profiles {:uberjar {:aot :all}})
