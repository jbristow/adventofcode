(ns aoc2016.day12
  (:import (java.io BufferedReader FileReader)))

(defn jump [v n j]
  (cond
    (zero? v)
    (inc n)

    (pos? j)
    (+ n j)

    :else
    (+ n j)))

(defmulti instruction (fn [line registers n] (first line)))

(defmethod instruction "cpy" [[_ value register] registers n]
  (if (re-matches #"[a-z]" value)
    [(merge registers {register (get registers value)}) (inc n)]
    [(merge registers {register (Integer. value)}) (inc n)]))

(defmethod instruction "inc" [[_ register] registers n]
  [(merge registers {register (inc (get registers register))}) (inc n)])

(defmethod instruction "dec" [[_ register] registers n]
  [(merge registers {register (dec (get registers register))}) (inc n)])

(defmethod instruction "jnz" [[_ register jump-amount] registers n]
  (let [value (if (re-matches #"[a-z]" register)
                (get registers register)
                (Integer. register))
        j (Integer. jump-amount)
        next-n (jump value n j)]
    (when (and (= "b" register) (neg? value)) (throw (Exception. "jello")))
    [registers next-n]))

(def sample-data ["cpy 41 a"
                  "inc a"
                  "inc a"
                  "dec a"
                  "jnz a 2"
                  "dec a"])

(defn puzzle-data []
  (line-seq (BufferedReader. (FileReader. "resources/day12.txt"))))

(defn answer [data]
  (let [instructions (map #(clojure.string/split % #" ") data)
        max-line (count instructions)]

    (loop [registers {"a" 0 "b" 0 "c" 0 "d" 0}
           n 0]
      (if (<= max-line n)
        registers
        (let [processed (instruction (nth instructions n) registers n)]
          (recur (first processed) (second processed)))))))
(defn answer-b [data]
  (let [instructions (map #(clojure.string/split % #" ") data)
        max-line (count instructions)]

    (loop [registers {"a" 0 "b" 0 "c" 1 "d" 0}
           n 0]
      (if (<= max-line n)
        registers
        (let [processed (instruction (nth instructions n) registers n)]
          (recur (first processed) (second processed)))))))
