(ns aoc2016.day18)

(defn trap? [i]
  (= i \^))

(defn safe? [i]
  (= i \.))

(defn trapped? [s]
  (or
   (= s "^^.")
   (= s ".^^")
   (= s "^..")
   (= s "..^")))

(defn next-row [row]
  (clojure.string/join
   (map #(if (trapped? (clojure.string/join %)) \^ \.)
        (partition 3 1 (str "." row ".")))))

(defn generate [n known]
  (take n (iterate next-row known)))

(defn count-safe [rows]
  (apply + (map (fn [x] (count (filter (partial = \.) x))) rows)))

(def puzzle-input "^^^^......^...^..^....^^^.^^^.^.^^^^^^..^...^^...^^^.^^....^..^^^.^.^^...^.^...^^.^^^.^^^^.^^.^..^.^")

(defn answer []
  (count-safe (generate 40 puzzle-input)))

(defn answer-b []
  (count-safe (generate 400000 puzzle-input)))
