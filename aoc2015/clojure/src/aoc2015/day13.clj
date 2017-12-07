(ns aoc2015.day13
  (:require [clojure.math.combinatorics :as combo]))

(defn conjugate [f g & [g-inv]] 
  (comp (or g-inv g) f g)) 

(defn composite [f f-inv n x] 
  (nth (iterate (if (pos? n) f f-inv) x) (Math/abs n))) 

(defn rotate-left [xs] 
  (when (seq xs) (concat (rest xs) [(first xs)]))) 

(def rotate-right (conjugate rotate-left reverse)) 

(def data 
  (map 
   (partial re-matches #"^(\w+) would (lose|gain) (\d+).* (\w+).") 
   (clojure.string/split 
    (slurp "resources/day13.input") 
    #"\n")))

(defn parse-amount [amount posneg]
  (if (= posneg "gain")
    (Integer. amount)
    (Integer. (str "-" amount))))

(defn clean-data [rows]
  {:names (distinct (map second rows))
   :scores (into {} (map (fn [r] {(str (second r) "-" (last r)) (parse-amount (nth r 3) (nth r 2))}) rows))})

(def
  p-data
  (let [{names :names
         scores :scores} (clean-data data)]
    (map
     (fn [r]
       (apply +
              (map #(get scores %) 
                   (mapcat
                    (fn [a b c] (list (str a "-" b) (str a "-" c)))
                    r
                    (rotate-left r)
                    (rotate-right r)))))
     (distinct (combo/permutations names)))))
