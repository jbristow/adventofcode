(ns aoc2016.day20
  (:require [clojure.string :as str])
  (:import (java.io BufferedReader FileReader)))

(def puzzle-input (line-seq (BufferedReader. (FileReader. "resources/day20.txt"))))

(defn clean-blacklist [lines]
  (let [ranges (sort-by first
                        (map #(let [[x y] (str/split % #"-")]
                                (list (Long/valueOf x)
                                      (Long/valueOf y)))
                             lines))]
    (loop [[x1 y1 :as a] (first ranges)
           [x2 y2 :as b] (second ranges)
           r (drop 2 ranges)
           nr '()]
      (cond (nil? b)
            (reverse (conj nr a))

            (< y1 (dec x2))
            (recur b (first r) (rest r) (conj nr a))

            (>= y1 y2)
            (recur a (first r) (rest r) nr)

            :else
            (recur (list x1 y2) (first r) (rest r) nr)))))

(defn count-valid-ips [bl]
  (loop [[x1 y1 :as a] (first bl)
         [x2 y2 :as b] (second bl)
         r (drop 2 bl)
         n x1]
    (if (nil? b)
      (+ n (- 4294967295 y1))
      (recur b (first r) (rest r) (+ n (- (dec x2) y1))))))

(defn answer-a [] (inc (second (first (clean-blacklist puzzle-input)))))

(defn answer-b [] (count-valid-ips (clean-blacklist puzzle-input)))
