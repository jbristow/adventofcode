(ns aoc2016.day16
  (:require [clojure.string :as str]))

(defn calculate [a]
  (concat a "0" (map #(if (= % \1) \0 \1)
                     (reverse a))))

(defn order [x]
  (int (Math/ceil (/ (Math/log x) (Math/log 2)))))

(defn fill [length seed]
  (println length (order (/ length (count seed))))
  (str/join
   (take length
         (nth
          (iterate calculate seed)
          (order (/ length (count seed)))))))

(defn checksum [s]
  (println (count s))
  (if (odd? (count s))
    (checksum (map #(if (= (first %) (second %)) 1 0) (partition 2 s)))))

(defn answer [] (checksum (fill 272 "01000100010010111")))

(defn answer-b [] (checksum (fill 35651584 "01000100010010111")))
