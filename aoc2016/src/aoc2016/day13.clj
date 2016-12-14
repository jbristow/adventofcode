(ns aoc2016.day13
  (:require [clojure.set :as s]
            [clojure.zip :as z]))

(def favorite-number 1364)

(defn compute
  "x*x + 3*x + 2*x*y + y + y*y."
  [x y]
  (+ (* x x)
     (* 3 x)
     (* 2 x y)
     y
     (* y y)
     favorite-number))

(defn open? [x y]
  (even?
   (count
    (clojure.string/replace
     (Integer/toBinaryString (compute x y))  #"0" ""))))

(defn open-neighbors [x y]
  (let [candidates [[(inc x) y]
                    [x (inc y)]
                    [x (dec y)]
                    [(dec x) y]]]
    (filter #(and (<= 0 (first %))
                  (<= 0 (second %))
                  (apply open? %))
            candidates)))

(defn matching-node [[i j] seen]
  (empty? (filter (fn [[x y]]
                    (and  (= i x)
                          (= j y))) seen)))

(defn branch? [[[x y :as loc] seen]]
  (seq (s/difference (set (open-neighbors x y)) (set seen))))

(defn children [[[x y :as loc] seen]]
  (map #(vector % (conj seen loc))
       (apply list (s/difference (set (open-neighbors x y))
                                 (set seen)))))

(def z (z/zipper branch? children (fn [_ c] c) [[1 1] '()]))

(defn- zip-children [loc]
  (when-let [child (z/down loc)]
    (take-while (complement nil?) (iterate z/right child))))

(defn breadth-first [loc]
  ((fn bfs [queue]
     (lazy-seq
      (when (seq queue)
        (let [new-loc  (peek queue)
              children (zip-children new-loc)]
          (cons new-loc (bfs (into (pop queue) children)))))))
   (conj clojure.lang.PersistentQueue/EMPTY loc)))

(defn answer
  "Fewest number of steps to get to 31,39"
  [final-node]
  (count (second (z/node (first (filter #(= final-node (first (z/node %)))
                                        (breadth-first z)))))))

(defn answer-b
  "Number of distinct steps that can be reached in 50 steps"
  [max-steps]
  (loop [steps 0
         seen [z]]
    (if (= max-steps steps)
      (count (distinct (map #(first (z/node %)) seen)))
      (recur (inc steps) (distinct (concat seen (mapcat zip-children seen)))))))
