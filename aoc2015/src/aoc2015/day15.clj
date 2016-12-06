(ns aoc2015.day15
  (:require [clojure.math.combinatorics :as combo]))

(def input (clojure.string/split (slurp "resources/day15.input") #"\n"))

(defn parse-line
  [line]
  (let
   [[_ ingredient capacity durability flavor texture calories]
    (re-matches
     #"(\w+): capacity (-?\d+), durability (-?\d+), flavor (-?\d+), texture (-?\d+), calories (-?\d+)"
     line)]
    {:ingredient ingredient,
     :capacity (bigint capacity),
     :durability (bigint durability),
     :flavor (bigint flavor),
     :texture (bigint texture),
     :calories (bigint calories)}))

(defn parse-lines [lines]
  (map parse-line lines))

(defn all-combos [parsed]
  (combo/combinations parsed 5))

(defn count-ingredients
  [i]
  (let
   [ingr-list (distinct (map :ingredient i))]
    (sort-by
     first
     (map
      (fn [a] [a (count (filter (fn [b] (= a (:ingredient b))) i))])
      ingr-list))))

(defn calculate
  [keyname ingredients amounts]
  (max
   0
   (reduce + (map (fn [a b] (* (keyname a) b)) ingredients amounts))))

(defn combine
  [ingredients amounts]
  (let
   [total-capacity
    (calculate :capacity ingredients amounts)
    total-durability
    (calculate :durability ingredients amounts)
    total-flavor
    (calculate :flavor ingredients amounts)
    total-texture
    (calculate :texture ingredients amounts)
    total
    (* total-capacity total-durability total-flavor total-texture)]
    (when-not (zero? total) total)))

(defn combine-2
  [ingredients amounts]
  (let
   [total-capacity
    (calculate :capacity ingredients amounts)
    total-durability
    (calculate :durability ingredients amounts)
    total-flavor
    (calculate :flavor ingredients amounts)
    total-texture
    (calculate :texture ingredients amounts)
    total-calories
    (calculate :calories ingredients amounts)
    total
    (* total-capacity total-durability total-flavor total-texture)]
    (when (and (= 500 total-calories) (not (zero? total))) total)))

(defn possibilities []
  (mapcat 
   (fn [a] 
     (mapcat 
      (fn [b] 
        (map 
         (fn [c] 
           (list a b c (- 100 a b c)))
         (range 0 (- 101 (+ a b)))))
      (range 0 (- 101 a)))) 
   (range 0 101)))

(defn answer-1 
  [lines] 
  (reduce 
   max 
   (keep 
    #(combine (parse-lines lines) %) 
    (possibilities))))

(defn answer-2
  [lines]
  (reduce 
   max 
   (keep 
    #(combine-2 (parse-lines lines) %) 
    (possibilities))))
