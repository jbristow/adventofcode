(ns aoc2016.day22
  (:import (java.io BufferedReader FileReader)))

(def puzzle-input
  (drop 2
        (line-seq
         (BufferedReader. (FileReader. "resources/day22.txt")))))

(defn process [line]
  (let [spline (clojure.string/split line #"\s+")
        [_ x y] (re-find #".*-x(\d+)-y(\d+)" (first spline))
        size (Integer/valueOf (clojure.string/join (butlast (nth spline 1))))
        used (Integer/valueOf (clojure.string/join (butlast (nth spline 2))))
        avail (Integer/valueOf (clojure.string/join (butlast (nth spline 3))))
        used-pct (Integer/valueOf (clojure.string/join (butlast (nth spline 4))))]

    {:name (first spline)
     :x (Integer/valueOf x)
     :y (Integer/valueOf y)
     :size size :used used :avail avail :used-pct used-pct}))

(defn answer []
  (let [all-inputs (map process puzzle-input)
        nonzeros (remove #(zero? (:used %)) all-inputs)]
    (map (fn [{a-name :name a-used :used}]
           (remove (fn [{b-name :name b-avail :avail}]
                     (or (= a-name b-name)
                         (> a-used b-avail))) all-inputs)) nonzeros)))

(defn input-at [m xi yi]
  (first (filter (fn [{:keys [x y]}] (and (= x xi) (= y yi))) m)))

(defn pretty-print []
  (let [all-inputs (map process puzzle-input)
        min-size (:size (first (sort-by :size all-inputs)))
        max-y (last (sort-by :y all-inputs))
        max-x (last (sort-by :x all-inputs))
        x-vals (range 0 (inc (:x max-x)))
        y-vals (range 0 (inc (:y max-y)))
        points (map (fn [y]
                      (map (fn [x]
                             (let [{avail :avail used :used size :size}
                                   (input-at all-inputs x y)]
                               (cond (< min-size used) "_______"
                                     (zero? used) (format "*%2d*%2d*" x y)
                                     :else (format " %2d,%2d " x y))))
                           x-vals)) y-vals)]
    (doseq [y points]
      (println (clojure.string/join y)))))

(defn answer-b
  "After viewing the output of pretty-print: 
    record the start point, 
    move up until you hit the wall (record point), 
    move left until you just clear the wall (record point)
    move up until you hit the top (record point)
    move right until you hit the edge (record point)
    Compute distance traveled, then multiply by 5 * max-x -1"
  []
  (+ 2 16 10 22 (* 31 5)))
