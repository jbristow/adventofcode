(ns aoc2016.day02
  (:import (java.io BufferedReader FileReader)))

(defmulti keypad-move (fn [direction number] direction))

(defmethod keypad-move \U [direction number]
  (let [n (- number 3)]
    (if (< n 1) number n)))

(defmethod keypad-move \D [direction number]
  (let [n (+ 3 number)]
    (if (< 9 n) number n)))

(defmethod keypad-move \L [direction number]
  (if (zero? (mod (dec number) 3))
    number
    (dec number)))

(defmethod keypad-move \R [direction number]
  (if (zero? (mod number 3))
    number
    (inc number)))

(defn process-a [input]
  (reduce (fn [sum value] (keypad-move value sum)) 5 input))

(defn answer-a [filename]
  (clojure.string/join
   (map process-a (line-seq (BufferedReader. (FileReader. filename))))))

(def down-from {1 3, 2 6, 3 7, 4 8, 6 \A, 7 \B, 8 \C, \B \D})
(def up-from {3 1, 6 2, 7 3, 8 4, \A 6, \B 7, \C 8, \D \B})
(def left-from {3 2, 4 3, 6 5, 7 6, 8 7, 9 8, \B \A, \C \B})
(def right-from {2 3, 3 4, 5 6, 6 7, 7 8, 8 9, \A \B, \B \C})

(defmulti diamond-move (fn [direction number] direction))

(defmethod diamond-move \U [direction number]
  (if (contains? up-from number) (get up-from number) number))
(defmethod diamond-move \D [direction number]
  (if (contains? down-from number) (get down-from number) number))
(defmethod diamond-move \R [direction number]
  (if (contains? right-from number) (get right-from number) number))
(defmethod diamond-move \L [direction number]
  (if (contains? left-from number) (get left-from number) number))

(defn process-b [input]
  (reduce (fn [sum value] (diamond-move value sum)) 5 input))

(defn answer-b [filename]
  (clojure.string/join 
   (map process-b (line-seq (BufferedReader. (FileReader. filename))))))
