(ns aoc2015.day02
  (:require [clojure.string :as str]))

(defn parse-line
  "Converts AAxBBxCC into an integer triplet."
  [line]
  (take 3 (map #(Integer/valueOf %) (str/split line #"x"))))

(defn parse-data [lines]
  (map parse-line lines))

(defn paper [edges]
  (let [faces (->> edges
                   cycle
                   (partition 2 1)
                   (take 3)
                   (map (partial apply * 2)))]
    (apply + (/ (apply min faces) 2) faces)))

(defn ribbon [edges]
  (+ (apply * edges)
     (->> edges
          (mapcat #(repeat 2 %))
          cycle
          (partition 4 2)
          (take 3)
          (map (partial reduce + 0))
          (apply min))))

(defn totaler [f input]
  (reduce + 0 (map f (parse-data input))))

(defmulti total (fn [which input] which))
(defmethod total :paper [_ input] (totaler paper input))
(defmethod total :ribbon [_ input] (totaler ribbon input))
