(ns aoc2016.day05
  (:require [aoc2016.util :as util]))

(defn iterations [initial]
  (map #(util/md5 (str initial %)) (range)))

(defn valid-hashes [input] (filter #(clojure.string/starts-with? % "00000")
                                   (iterations input)))

(defn answer [input]
  (clojure.string/join
   (take 8
         (map #(nth % 5)
              (valid-hashes input)))))

(defn answer-b [input]
  (let [hashes (valid-hashes input)]
    (clojure.string/join
     (map second
          (sort-by first
                   (into (list)
                         (loop [h (first hashes)
                                r (rest hashes)
                                seen {}]
                           (let [i (Integer/valueOf (str (nth h 5)) 16)
                                 v (nth h 6)]
                             (println h i v (contains? seen i)
                                      (<= 8 i))
                             (cond
                               (= 8 (count seen))
                               seen

                               (or (contains? seen i)
                                   (<= 8 i))
                               (recur (first r) (rest r) seen)

                               :else
                               (recur (first r) (rest r) (merge seen {i v})))))))))))
