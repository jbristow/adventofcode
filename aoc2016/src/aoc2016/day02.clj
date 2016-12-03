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

(defn process-a [n input]
  (reduce (fn [sum value] (keypad-move value sum)) n input))

(defn answer [solver input]
  (loop [lines input
         last-num 5
         output '()]
    (if (nil? (first lines))
      (clojure.string/join (reverse output))
      (let [digit (solver last-num (first lines))]
        (recur (rest lines) digit (conj output digit))))))

(defn solver-a [filename]
  (answer process-a (line-seq (BufferedReader. (FileReader. filename)))))

(defn solver-b [filename]
  (answer process-b (line-seq (BufferedReader. (FileReader. filename)))))

(def down-from {1 3, 2 6, 3 7, 4 8, 6 \A, 7 \B, 8 \C, \B \D})
(def up-from {3 1, 6 2, 7 3, 8 4, \A 6, \B 7, \C 8, \D \B})
(def left-of {3 2, 4 3, 6 5, 7 6, 8 7, 9 8, \B \A, \C \B})
(def right-of {2 3, 3 4, 5 6, 6 7, 7 8, 8 9, \A \B, \B \C})

(defn get-next-key [keymap number]
  (if (contains? keymap number) (get keymap number) number))

(defmulti diamond-move (fn [direction number] direction))

(defmethod diamond-move \U [direction number]
  (get-next-key up-from number))
(defmethod diamond-move \D [direction number]
  (get-next-key down-from number))
(defmethod diamond-move \R [direction number]
  (get-next-key right-of number))
(defmethod diamond-move \L [direction number]
  (get-next-key left-of number))

(defn process-b [n input]
  (reduce (fn [sum value] (diamond-move value sum)) n input))
