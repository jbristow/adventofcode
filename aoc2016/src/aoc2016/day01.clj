(ns aoc2016.day01)

(def north '(0 1))
(def east '(1 0))
(def south '(0 -1))
(def west '(-1 0))

(defmulti turn (fn [turn-direction direction-vector] turn-direction))

(defmethod turn "R" [turn-direction direction-vector]
  (condp = direction-vector
    north east
    east south
    south west
    west north))

(defmethod turn "L" [turn-direction direction-vector]
  (condp = direction-vector
    north west
    east north
    south east
    west south))

(defn move [current-direction input]
  (let [turn-direction (str (first input))
        movement (Integer. (clojure.string/join (rest input)))
        new-direction (turn turn-direction current-direction)]
    (list new-direction (map #(* movement %) new-direction))))

(defn process [input]
  (loop [current-direction north
         location '(0 0)
         directions (clojure.string/split input #", ")]
    (let [next-instruction (first directions)]
      (if (nil? next-instruction)
        location
        (let [[next-direction location-now] (move current-direction next-instruction)]
          (recur next-direction (map + location location-now) (rest directions)))))))

(defn move-b [current-direction current-location input]
  (let [turn-direction (str (first input))
        movement (Integer. (clojure.string/join (rest input)))
        new-direction (turn turn-direction current-direction)
        steps (map #(map + current-location (map * new-direction (list % %))) (range 1 (inc movement)))]
    (list new-direction steps)))

(defn process-b [input]
  (loop [current-direction north
         location '(0 0)
         directions (clojure.string/split input #", ")
         seen (list (list 0 0))]
    (let [next-instruction (first directions)]
      (if (nil? next-instruction)
        location
        (let [[next-direction steps] (move-b current-direction location next-instruction)
              next-loc (last steps)
              matches (clojure.set/intersection (set steps) (set seen))]
          (if (<= 1 (count matches))
            (first matches)

            (recur next-direction next-loc (rest directions) (concat seen steps))))))))

(def problem "R1, R1, R3, R1, R1, L2, R5, L2, R5, R1, R4, L2, R3, L3, R4, L5, R4, R4, R1, L5, L4, R5, R3, L1, R4, R3, L2, L1, R3, L4, R3, L2, R5, R190, R3, R5, L5, L1, R54, L3, L4, L1, R4, R1, R3, L1, L1, R2, L2, R2, R5, L3, R4, R76, L3, R4, R191, R5, R5, L5, L4, L5, L3, R1, R3, R2, L2, L2, L4, L5, L4, R5, R4, R4, R2, R3, R4, L3, L2, R5, R3, L2, L1, R2, L3, R2, L1, L1, R1, L3, R5, L5, L1, L2, R5, R3, L3, R3, R5, R2, R5, R5, L5, L5, R2, L3, L5, L2, L1, R2, R2, L2, R2, L3, L2, R3, L5, R4, L4, L5, R3, L4, R1, R3, R2, R4, L2, L3, R2, L5, R5, R4, L2, R4, L1, L3, L1, L3, R1, R2, R1, L5, R5, R3, L3, L3, L2, R4, R2, L5, L1, L1, L5, L4, L1, L1, R1")

(defn answer-1 [] (apply + (map #(Math/abs %1) (process problem))))
(defn answer-2 [] (apply + (map #(Math/abs %1) (process-b problem))))
