(ns aoc2016.day25
  (:require [aoc2016.day12 :as d12]
            [clojure.string :as str])
  (:import (java.io BufferedReader FileReader)))

(defmethod d12/instruction "out" [[_ value register] {output :output :as registers} n]
  [(assoc registers :output (cons
                             (if (re-matches #"[a-z]" value)
                               (get registers value)
                               (Integer/valueOf value))
                             output)) (inc n)])
(def sample-data ["cpy 41 a"
                  "inc a"
                  "inc a"
                  "dec a"
                  "jnz a 2"
                  "dec a"
                  "out a"
                  "out 123"])

(defn puzzle-data []
  (line-seq (BufferedReader. (FileReader. "resources/day25.txt"))))

(defn answer [data a-init]
  (let [instructions (map #(clojure.string/split % #" ") data)
        max-line (count instructions)]

    (loop [registers {"a" a-init "b" 0 "c" 0 "d" 0 :output '()}
           n 0
           stepcount 0]
      (if (or (<= max-line n)
              (<= 50 (count (:output registers)))
              (and (< 1 (count (:output registers)))
                   (some (complement #(= % '(0 1))) (partition 2 (reverse (:output registers))))))
        (str/join (reverse (:output registers)))
        (let [[registers line-num] (d12/instruction (nth instructions n) registers n)]
          (recur registers line-num (inc stepcount)))))))
