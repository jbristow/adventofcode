(ns aoc2015.day03)

(def start-house '(0, 0))

(defn get-dir [c]
  (case c
    \> '(1 0)
    \< '(-1 0)
    \^ '(0 1)
    \v '(0 -1)))

(defn add-points [a b]
  (map + a b))

(defn delivery [s]
  (count
   (distinct
    (reductions (fn [acc c] (add-points (get-dir c) acc)) start-house s))))

(defn robo-delivery [s]
  (->> s
       (partition 2)
       (reductions (fn [acc c] (map add-points (map get-dir c) acc))
                   (repeat 2 start-house))

       (apply concat)
       distinct
       count))
