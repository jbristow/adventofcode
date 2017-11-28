(ns aoc2016.day24
  (:require [clojure.set :as set]
            [clojure.string :as str]))

(def sample-input (str
                   "###########\n"
                   "#0.1.....2#\n"
                   "#.#######.#\n"
                   "#4.......3#\n"
                   "###########\n"))

(defn get-markers-to-coord [input]
  (apply hash-map
         (flatten
          (map-indexed (fn [y row]
                         (keep-indexed (fn [x cell]
                                         (when (re-matches #"[\d]" (str cell))
                                           [(Long/valueOf (str cell)) {:x x :y y}])) row))
                       (str/split input #"\n")))))

(defn get-coord-to-marker [input]
  (apply hash-map
         (flatten
          (map-indexed (fn [y row]
                         (keep-indexed (fn [x cell]
                                         (when (re-matches #"[\d]" (str cell))
                                           [{:x x :y y} (Long/valueOf (str cell))])) row))
                       (str/split input #"\n")))))

(defn get-passages [input]
  (flatten
   (map-indexed (fn [y row]
                  (keep-indexed (fn [x cell]
                                  (when (re-matches #"[\d.]" (str cell))
                                    {:x x :y y})) row))
                (str/split input #"\n"))))

(defn step [wavefront target seen legal n]
  (let [nextwave (set/intersection
                  (set/difference (into #{} (mapcat (fn [{:keys [x y]}]
                                                      #{{:x (inc x) :y y}
                                                        {:x (dec x) :y y}
                                                        {:x x :y (inc y)}
                                                        {:x x :y (dec y)}})
                                                    wavefront))
                                  seen)
                  legal)]

    (if (some (partial = target) wavefront)
      n
      (step nextwave target (set (concat seen nextwave)) legal (inc n)))))

(defn from [a b input]
  (let [flags (get-markers-to-coord input)
        flag-coords (get-coord-to-marker input)
        good-coords (set (get-passages input))
        target-coord (get flags b)]
    (step #{(get flags a)} target-coord #{} good-coords 0)))

(defn sample-dists [] (map #(conj % (inc (rand-int 10))) [[0 1] [0 2] [0 3] [0 4]
                                                          [1 2] [1 3] [1 4]
                                                          [2 3] [2 4]
                                                          [3 4]]))

(defn make-distmap [edgelist]
  (reduce (fn [m [a b dist]]
            (let [am (get m a {})
                  bm (get m b {})]
              (assoc m
                     a (assoc am b dist)
                     b (assoc bm a dist))))
          {}
          edgelist))

(defn djikstra [distmap start]
  (let [nodecount (count distmap)]
    (loop [[[[lastseen & _ :as seen] total-dist :as head] & r :as stack] (sort-by second (map (fn [[end dist]] [[end start] dist]) (get distmap start)))]
      (if (and (= nodecount (count seen)))

        head
        (let [nextstack (sort-by second
                                 (concat r
                                         (keep (fn [[end dist]]
                                                 (when (not-any? (partial = end) seen)
                                                   [(cons end seen) (+ dist total-dist)])) (get distmap lastseen))))]
          (recur nextstack))))))

(defn djikstra-2 [distmap start]
  (let [nodecount (count distmap)]
    (loop [[[[lastseen & _ :as seen] total-dist :as head] & r :as stack] (sort-by second (map (fn [[end dist]] [[end start] dist]) (get distmap start)))]

      (if (or (nil? head)
              (and (= nodecount (count seen))
                   (= lastseen (dec nodecount))))

        head
        (let [nextstack (sort-by second
                                 (concat r
                                         (keep (fn [[end dist]]
                                                 (when (not-any? (partial = end) seen) [(cons end seen) (+ dist total-dist)])) (get distmap lastseen))))]
          (recur nextstack))))))

;; Ugh, I coudn't map this in code, but the algorithm works...
;; At least it's not brute force.
(def
  actual-dists
  (reduce concat [] (map-indexed (fn [start row]
                                   (keep-indexed (fn [end dist]
                                                   (when-not (or (= start end) (zero? dist)) [start end dist]))
                                                 row))
                                 [[0	28	310	304	88	314	70	292]
                                  [28	0	310	304	104	314	78	292]
                                  [310	310	0	34	258	68	272	38]
                                  [304	304	34	0	252	54	266	20]
                                  [88	104	258	252	0	262	38	240]
                                  [314	314	68	54	262	0	276	42]
                                  [70	78	272	266	38	276	0	254]
                                  [292	292	38	20	240	42	254	0]])))

(def
  actual-dists-2
  (reduce concat [] (map-indexed (fn [start row]
                                   (keep-indexed (fn [end dist]
                                                   (when-not (or (= start end) (zero? dist)) [start end dist]))
                                                 row))
                                 [[0	28	310	304	88	314	70	292 0]
                                  [28	0	310	304	104	314	78	292 28]
                                  [310	310	0	34	258	68	272	38 310]
                                  [304	304	34	0	252	54	266	20 304]
                                  [88	104	258	252	0	262	38	240 88]
                                  [314	314	68	54	262	0	276	42 314]
                                  [70	78	272	266	38	276	0	254 70]
                                  [292	292	38	20	240	42	254	0 292]
                                  [0	28	310	304	88	314	70	292 0]])))
