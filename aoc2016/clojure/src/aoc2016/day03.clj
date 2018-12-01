(ns aoc2016.day03
  (:require [clojure.string :as str])
  (:import (java.io BufferedReader FileReader)))

(defn triangle? [[a b c]]
  (and (> (+ a b) c)
       (> (+ b c) a)
       (> (+ c a) b)))

(defn solve [input]
  (count (filter triangle? input)))

(defn answer [filename]
  (solve (map (fn [i] (map #(Integer. %)
                           (str/split
                            (str/trim i) #"\s+")))
              (line-seq (BufferedReader. (FileReader. filename))))))

(defn answer-b [filename]
  (let [lines (map
               (fn [i]
                 (map #(Integer. %)
                      (str/split (str/trim i) #"\s+")))
               (line-seq (BufferedReader. (FileReader. filename))))]
    (solve
     (mapcat (fn [i] [(map first i) (map second i) (map #(nth % 2) i)])
             (partition 3 lines)))))
