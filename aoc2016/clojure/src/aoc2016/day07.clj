(ns aoc2016.day07
  (:import (java.io BufferedReader FileReader)))

(defn abba? [[a b c d]]
  (and (= a d)
       (= b c)
       (not= a b)))

(defn aba? [[a b c]]
  (and (= a c)
       (not= a b)))

(defn aba->bab [[a b c]] [b a b])

(defn supports-tls? [input]
  (let [hypernet (map second (re-seq #"\[([a-z]+)\]" input))
        other (clojure.string/split
               (clojure.string/replace input #"\[[a-z]+\]" "#") #"#")]
    (and (empty? (mapcat #(filter abba?  (partition 4 1 %)) hypernet))
         (seq (mapcat #(filter abba? (partition 4 1 %)) other)))))

(defn supports-ssl? [input]
  (let [hypernet (map second (re-seq #"\[([a-z]+)\]" input))
        other (clojure.string/split
               (clojure.string/replace input #"\[[a-z]+\]" "#") #"#")
        other-aba (mapcat #(filter aba? (partition 3 1 %)) other)]
    (when (seq other-aba)
      (seq (clojure.set/intersection (set (map aba->bab other-aba))
                                     (set (mapcat #(partition 3 1 %)
                                                  hypernet)))))))

(def puzzle-input
  (line-seq (BufferedReader. (FileReader. "resources/day07.txt"))))

(defn answer [solver input] (solver input))

(defn solver-1 [input] (count (filter supports-tls? input)))

(defn solver-2 [input] (count (filter supports-ssl? input)))
