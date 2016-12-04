(ns aoc2016.day04
  (:import (java.io BufferedReader FileReader)))

(defn parse-room [input]
  (let [[_ enc-name _ sector-id checksum]
        (re-matches #"(([a-z]+-)*[a-z]+)-(\d+)\[([a-z]+)\]" input)]
    {:encrypted-name enc-name :sector-id (Integer. sector-id) :checksum checksum}))

(defn letter-count [input]
  (loop [head (first input)
         tail (rest input)
         counts {}]
    (cond
      (nil? head)
      counts

      (= \- head)
      (recur (first tail) (rest tail) counts)

      (contains? counts head)
      (recur (first tail) (rest tail) (merge counts {head (inc (get counts head))}))

      :else
      (recur (first tail) (rest tail) (merge counts {head 1})))))

(defn sort-letters [[a-a a-b] [b-a b-b]]
  (cond
    (< a-b b-b)
    1

    (> a-b b-b)
    -1

    (and (= a-b b-b)
         (pos? (compare a-a b-a)))
    1

    (and (= a-b b-b)
         (neg? (compare a-a b-a)))
    -1

    :else
    0))

(defn generate-checksum [input]
  (clojure.string/join (take 5 (map first (sort-by identity sort-letters
                                                   (into '() (letter-count input)))))))

(defn parse-and-filter [inputs]
  (filter #(= (:checksum %)
              (generate-checksum (:encrypted-name %)))
          (map parse-room inputs)))

(defn solver-a [inputs]
  (apply + (map :sector-id (parse-and-filter inputs))))

(defn rotate-char [c n]
  (if (= \- c)
    \ 
    (char (+ (int \a) (mod (+ (- (int c) (int \a)) (mod n 26)) 26)))))

(defn decrypt [{sector-id :sector-id encrypted :encrypted-name :as a}]
  (clojure.string/join (map #(rotate-char % sector-id) encrypted)))

(defn solver-b [inputs]
  (:sector-id (first (filter #(= "northpole object storage" (:decrypted %))
                             (map (fn [i] (merge i {:decrypted (decrypt i)}))
                                  (parse-and-filter inputs))))))

(defn answer [filename solver]
  (solver (line-seq (BufferedReader. (FileReader. filename)))))
