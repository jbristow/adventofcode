(ns aoc2016.day21
  (:require [aoc2016.util :as util]
            [clojure.string :as str])
  (:import (java.io BufferedReader FileReader)))

(def puzzle-input
  (line-seq (BufferedReader. (FileReader. "resources/day21.txt"))))

(defn rotate-amount [i len] (mod (if (<= 4 i) (+ 2 i) (inc i)) len))

(defn rotate-me-back [x len]
  (nth '(1 1 6 2 7 3 0 4) x))

(defmulti instruction (fn [data s] (:type data)))

(defmethod instruction :swap-position [{:keys [x y]} s]
  (let [a (min x y)
        b (max x y)
        begin (str/join (take a s))
        mid (str/join (take (dec (- b a)) (drop (inc a) s)))
        end (str/join (drop (inc b) s))
        c-a (nth s a)
        c-b (nth s b)]
    (str/join [begin c-b mid c-a end])))

(defmethod instruction :swap-letter [{:keys [x y]} s]
  (str/replace
   (str/replace
    (str/replace s (re-pattern x) "~")
    (re-pattern y) x) #"~" y))

(defmethod instruction :rotate-n [{:keys [direction x]} s]
  (str/join
   (util/rotate (if (= direction :right) (* -1 x) x) s)))

(defmethod instruction :rotate-position [{:keys [x]} s]
  (let [index-x (str/index-of s x)
        n (rotate-amount index-x (count s))]
    (str/join (util/rotate (* -1 n) s))))

(defmethod instruction :rotate-position-reverse [{:keys [x]} s]
  (let [index-x (str/index-of s x)
        n (rotate-me-back index-x (count s))]
    (str/join (util/rotate n s))))

(defmethod instruction :reverse [{:keys [x y]} s]
  (let [a (min x y)
        b (max x y)
        begin (str/join (take a s))
        mid (str/join (reverse (take (- (inc b) a) (drop a s))))
        end (str/join (drop (inc b) s))]
    (str/join [begin mid end])))

(defmethod instruction :move [{:keys [x y]} s]
  (let [char-at-x (nth s x)
        n-s (concat (take x s) (drop (inc x) s))
        begin (str/join (take y n-s))
        end (str/join (drop y n-s))]
    (str/join [begin char-at-x end])))

(defn process-line [line]
  (let [l (str/split line #" ")]
    (cond (and (= (nth l 0) "swap")
               (= (nth l 1) "position"))
          {:type :swap-position
           :x (Integer/valueOf (nth l 2))
           :y (Integer/valueOf (nth l 5))}

          (and (= (nth l 0) "swap")
               (= (nth l 1) "letter"))
          {:type :swap-letter :x (nth l 2) :y (nth l 5)}

          (and (= (nth l 0) "rotate")
               (str/starts-with? (nth l 3) "step"))
          {:type :rotate-n
           :direction (keyword (nth l 1))
           :x (Integer/valueOf (nth l 2))}

          (and (= (nth l 0) "rotate")
               (= (nth l 1) "based"))
          {:type :rotate-position :x (nth l 6)}

          (= (nth l 0) "reverse")
          {:type :reverse
           :x (Integer/valueOf (nth l 2))
           :y (Integer/valueOf (nth l 4))}

          (= (nth l 0) "move")
          {:type :move
           :x (Integer/valueOf (nth l 2))
           :y (Integer/valueOf (nth l 5))})))

(defn process-reverse [line]
  (let [l (str/split line #" ")]
    (cond (and (= (nth l 0) "swap")
               (= (nth l 1) "position"))
          {:type :swap-position
           :y (Integer/valueOf (nth l 2))
           :x (Integer/valueOf (nth l 5))}

          (and (= (nth l 0) "swap")
               (= (nth l 1) "letter"))
          {:type :swap-letter :y (nth l 2) :x (nth l 5)}

          (and (= (nth l 0) "rotate")
               (= (nth l 1) "left")
               (str/starts-with? (nth l 3) "step"))
          {:type :rotate-n :direction :right :x (Integer/valueOf (nth l 2))}

          (and (= (nth l 0) "rotate")
               (= (nth l 1) "right")
               (str/starts-with? (nth l 3) "step"))
          {:type :rotate-n :direction :left :x (Integer/valueOf (nth l 2))}

          (and (= (nth l 0) "rotate")
               (= (nth l 1) "based"))
          {:type :rotate-position-reverse :x (nth l 6)}

          (= (nth l 0) "reverse")
          {:type :reverse
           :x (Integer/valueOf (nth l 2))
           :y (Integer/valueOf (nth l 4))}

          (= (nth l 0) "move")
          {:type :move
           :y (Integer/valueOf (nth l 2))
           :x (Integer/valueOf (nth l 5))})))
