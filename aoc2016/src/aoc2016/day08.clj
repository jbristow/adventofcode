(ns aoc2016.day08
  (:import (java.io BufferedReader FileReader)))

(defn screen [x y]
  (apply hash-map
         (interleave (mapcat (fn [y] (map (fn [x] (keyword (str x "-" y)))
                                          (range x)))
                             (range y))
                     (repeat false))))

(defn rect [m [x y]]
  (let [r (mapcat (fn [y] (map (fn [x] (keyword (str x "-" y)))
                               (range x)))
                  (range y))]
    (println "make rectangle" x y)
    (merge m (mapcat #(hash-map % true) r))))

(defn rotate
  "Treat a list/vector as a circular data structure and rotate it by n
   places:
 
   (rotate 0  [1 2 3 4]) ;=> [1 2 3 4]
   (rotate 2  [1 2 3 4]) ;=> [3 4 1 2]
   (rotate -1 [1 2 3 4]) ;=> [4 1 2 3]
 
   Note, coll should be countable."
  [n coll]
  (let [size   (count coll)
        offset (mod n size)
        s      (cycle coll)
        s      (drop offset s)]
    (vec (take size s))))

(defn rotate-column [m x y n]
  (println "rotate column" x y n)
  (let [r (map #(get m (keyword (str x "-" %))) (range y))]
    (merge m
           (apply hash-map
                  (interleave (map #(keyword (str x "-" %)) (range y))
                              (rotate (* -1 n) r))))))
(defn rotate-row [m x y n]
  (println "rotate row" x y n)
  (let [r (map #(get m (keyword (str % "-" y))) (range x))]
    (merge m
           (apply hash-map
                  (interleave (map #(keyword (str % "-" y)) (range x))
                              (rotate (* -1 n) r))))))

(defn run-line [m line]
  (let [instructions (clojure.string/split line #" ")]
    (cond
      (= "rect" (first instructions))
      (let [xy (map #(Integer. (str %)) (clojure.string/split (last instructions) #"x"))]
        (rect m xy))

      (and (= "rotate" (first instructions))
           (= "column" (second instructions)))
      (let [[_ _ col-instr _ amount] instructions
            col (Integer. (second (clojure.string/split col-instr #"=")))]
        (rotate-column m col 6 (Integer. amount)))

      (and (= "rotate" (first instructions))
           (= "row" (second instructions)))
      (let [[_ _ row-instr _ amount] instructions
            row (Integer. (second (clojure.string/split row-instr #"=")))]
        (rotate-row m 50 row (Integer. amount))))))

(defn solver-1 [lines m]
  (loop [[h & r] lines
         display m]
    (if (nil? h)
      display
      (recur r (run-line display h)))))

(def puzzle-input (line-seq (BufferedReader. (FileReader. "resources/day08.txt"))))

(defn answer [solver]
  (count (filter true? (vals (solver puzzle-input (screen 50 6))))))

(defn printout []
  (let [ans (solver-1 puzzle-input (screen 50 6))]
    (doseq [y (range 6)]
      (println (map #(if (get ans (keyword (str % "-" y))) "#" ".")  (range 50))))))
