(ns aoc2015.day01)

(defn char->delta [c] (case c \( 1 \) -1 0))

(defn converter [s] (reduce + 0 (map char->delta s)))

(defn whereami [s]
  (count
   (take-while (complement neg?)
               (reductions + 0 (map char->delta s)))))
