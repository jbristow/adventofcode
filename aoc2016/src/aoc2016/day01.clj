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
        movement (Integer. (apply str (rest input)))
        new-direction (turn turn-direction current-direction)]
    (list new-direction (map #(* movement %) new-direction))))

(defn process [input]
  (loop [current-direction north
         walked '(0 0)
         directions (clojure.string/split input #", ")]
    (let [next-instruction (first directions)]
      (if (nil? next-instruction)
        walked
        (let [[next-direction walked-now] (move current-direction next-instruction)]
          (println current-direction walked directions next-direction walked-now)
          (recur next-direction (map + walked walked-now) (rest directions)))))))
