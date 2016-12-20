(ns aoc2016.day20
  (:import (java.io BufferedReader FileReader)))

(def puzzle-input (line-seq (BufferedReader. (FileReader. "resources/day20.txt"))))

(defn clean-blacklist [lines]
  (let [ranges (sort-by first (map (fn [a] (map (fn [b] (Long/valueOf b))
                                                (clojure.string/split a #"-"))) lines))
        nums (iterate inc 0)]
    (loop [[x y :as a] (first ranges)
           [x2 y2 :as b] (second ranges)
           r (rest (rest ranges))
           nr '()]
      (cond (nil? b)
            (reverse (conj nr a))

            (< y (dec x2))
            (recur b (first r) (rest r) (conj nr a))

            (>= y y2)
            (recur a (first r) (rest r) nr)

            :else
            (recur (list x y2) (first r) (rest r) nr)))))

(defn count-valid-ips [bl]
  (loop [[x1 y1 :as a] (first bl)
         [x2 y2 :as b] (second bl)
         r (rest (rest bl))
         n x1]
    (cond
      (nil? b)
      (+ n (- 4294967295 y1))

      :else
      (recur b (first r) (rest r) (+ n (- (dec x2) y1))))))

(defn answer-a [] (inc (second (first (clean-blacklist puzzle-input)))))

(defn answer-b [] (count-valid-ips (clean-blacklist puzzle-input)))
