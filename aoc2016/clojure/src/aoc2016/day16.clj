(ns aoc2016.day16
  (:require [clojure.string :as str]))

(defn calculate [a]
  (concat a "0" (map #(if (= % \1) \0 \1)
                     (reverse a))))

(defn order [x]
  (int (Math/ceil (/ (Math/log x) (Math/log 2)))))

(defn fill [length seed]
  (take length (some #(when (<= length (count %)) %)
                     (iterate calculate seed))))

(defn checksum [s]
  (if (odd? (count s))
    (count s)
    (checksum (map #(if (= (first %) (second %)) 1 0) (partition 2 s)))))

(defn answer [] (checksum (fill 272 "01000100010010111")))

(defn answer-b [] (checksum (fill 35651584 "01000100010010111")))
