(ns aoc2015.day17
  (:require [clojure.math.combinatorics :as combo]))

(def input (map (fn [i] (Integer. i))  (clojure.string/split (slurp "resources/day17.input") #"\n")))

(def test-input (map (fn [i] (Integer. i)) (clojure.string/split "20, 15, 10, 5, 5" #", ")))

(defn find-multiples [l]
  (distinct (filter #(= 2 (second %)) (map #(list % (count (filter (partial = %) l))) l)))) 

(defn find-matches [size sizes]
  (filter (fn [s] (= size (apply + s))) (combo/subsets sizes)))

(defn get-sets-with-one [a b sets]
  (filter
   (fn [s] 
     (and 
      (= 1 (count (filter #(= a %) s)))
      (not= 1 (count (filter #(= b %) s)))))
   sets))

(defn get-sets-with-both [a b sets]
  (filter
   (fn [s] 
     (and 
      (= 1 (count (filter #(= a %) s)))
      (= 1 (count (filter #(= b %) s)))))
   sets))

(defn get-sets-with-neither [a b sets]
  (filter
   (fn [s] 
     (and 
      (not= 1 (count (filter #(= a %) s)))
      (not= 1 (count (filter #(= b %) s)))))
   sets))

(defn answer-1 [sizes]
  (let [matches (find-matches 150 (sort sizes))]
    (list 
     (* 2 (count (get-sets-with-one 18 40 matches)))
     (* 2 (count (get-sets-with-one 40 18 matches)))
     (* 4 (count (get-sets-with-both 18 40 matches)))
     (count (get-sets-with-neither 18 40 matches)))))

(defn answer-2 [sizes]
  (let [matches (find-matches 150 (sort sizes))
        min-containers (first (sort (map count matches)))
        min-matches (filter #(= 4 (count %)) matches)]
    (list 
     (* 2 (count (get-sets-with-one 18 40 min-matches)))
     (* 2 (count (get-sets-with-one 40 18 min-matches)))
     (* 4 (count (get-sets-with-both 18 40 min-matches)))
     (count (get-sets-with-neither 18 40 min-matches)))))
