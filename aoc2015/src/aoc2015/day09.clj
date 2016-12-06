(ns aoc2015.day09
  (:require [clojure.math.combinatorics :as combo]))

(def input (clojure.string/split "London to Dublin = 464\nLondon to Belfast = 518\nDublin to Belfast = 141" #"\n"))

(defn parse-line [line]
  (rest (re-matches #"(\w+) to (\w+) = (\d+)" line)))

(defn parse-lines [lines]
  (map parse-line lines))

(defn get-locations [in]
  (let [point-as (map first in)
        point-bs (map second in)]
    (distinct (concat point-as point-bs))))

(defn all-combos [possibilities]
  (combo/permutations possibilities))

(defn all-stops [parsed]
  (map (partial partition 2 1) (all-combos (get-locations parsed))))

(defn apply-distances
  [parsed]
  (map
   (fn [line]
     (reduce
      +
      (map
       (fn [[a b]]
         (Integer.
          (last
           (first
            (filter
             (fn
               [[ai bi _]]
               (or (and (= a ai) (= b bi)) (and (= a bi) (= b ai))))
             parsed)))))
       line)))
   (all-stops parsed)))

(defn answer-1 [lines]
  (apply min (apply-distances (parse-lines lines))))

(defn answer-2 [lines]
  (apply max (apply-distances (parse-lines lines))))
