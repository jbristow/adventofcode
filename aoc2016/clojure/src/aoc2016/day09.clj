(ns aoc2016.day09
  (:require [clojure.string :as str])
  (:import (java.io BufferedReader FileReader)))

(defn process [input]
  (loop [[h & r] input
         output '()]
    (cond
      (nil? h)
      (str/join (reverse output))

      (= h \()
      (let [data (map #(Integer. %)
                      (str/split
                       (str/join (take-while (partial not= \)) r))
                       #"x"))
            after-paren (rest (drop-while (partial not= \)) r))
            expanded (flatten (repeat (second data)
                                      (take (first data) after-paren)))
            remaining (drop (first data) after-paren)]
        (recur remaining (concat (reverse expanded) output)))

      :else
      (recur r (conj output h)))))

(defn process-b [input]
  (loop [[h & r] input
         output 0]
    (cond
      (nil? h)
      output

      (= h \()
      (let [data (map #(Integer. %)
                      (str/split
                       (str/join (take-while (partial not= \)) r))
                       #"x"))
            after-paren (rest (drop-while (partial not= \)) r))
            expanded (apply + (repeat (second data)
                                      (process-b (take (first data)
                                                       after-paren))))
            remaining (drop (first data) after-paren)]
        (recur remaining (+ output expanded)))

      :else
      (recur r (inc output)))))

(defn answer []
  (count
   (str/replace
    (first (map process
                (line-seq (BufferedReader.
                           (FileReader. "resources/day09.txt")))))
    #"\s" "")))
(defn answer-b []
  (first (map process-b
              (line-seq (BufferedReader.
                         (FileReader. "resources/day09.txt"))))))
