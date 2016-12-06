(ns aoc2016.day06
  (:import (java.io BufferedReader FileReader)))

(def puzzle-input (line-seq (BufferedReader. (FileReader. "resources/day06.txt"))))

(defn find-most-freq [input]
  (loop [[h & r] input
         max-char -1
         max-num -1]

    (let [char-count (inc (count (filter #(= h %) r)))]
      (cond

        (nil? h)
        max-char

        (> char-count max-num)
        (recur (remove #(= h %) r) h char-count)

        :else
        (recur (remove #(= h %) r) max-char max-num)))))

(defn find-least-freq [input]
  (loop [[h & r] input
         min-char -1
         min-num Integer/MAX_VALUE]

    (let [char-count (inc (count (filter #(= h %) r)))]
      (cond

        (nil? h)
        min-char

        (< char-count min-num)
        (recur (remove #(= h %) r) h char-count)

        :else
        (recur (remove #(= h %) r) min-char min-num)))))

(defn answer [input]
  (clojure.string/join
   (map find-most-freq
        (partition (count input)
                   (apply interleave input)))))

(defn answer-b [input]
  (clojure.string/join
   (map find-least-freq
        (partition (count input)
                   (apply interleave input)))))
