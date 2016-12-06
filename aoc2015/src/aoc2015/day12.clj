(ns aoc2015.day12
  (:require [clojure.data.json :as json]))

(defn process [a]
  (println "processing" a)
  (if (map? a)
    (process (vals a))
    (loop [[h & t] a
           sum 0]
      (cond 
        (nil? h)
        sum

        (vector? h)
        (recur t (+ sum (process h)))

        (map? h)
        (recur t (+ sum (process (vals h))))

        (string? h)
        (recur t sum)

        :else
        (recur t (+ sum h))))))

(defn has-red? [m]
  (seq (filter (partial = "red") m)))

(defn process-no-red [a]
  (println "processing" a)
  (cond 
    (and (map? a) (has-red? (vals a)))
    0

    (map? a)
    (process-no-red (vals a))

    :else
    (loop [[h & t] a
           sum 0]
      (cond 
        (nil? h)
        sum

        (vector? h)
        (recur t (+ sum (process-no-red h)))

        (map? h)
        (recur t (+ sum (process-no-red h)))

        (string? h)
        (recur t sum)

        :else
        (recur t (+ sum h))))))
