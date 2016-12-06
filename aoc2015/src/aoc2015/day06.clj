(ns aoc2015.day06)

(def x-axis (range 1000))
(def y-axis (range 1000))

(def starting-grid
  (concat 
   (first 
    (for [x x-axis]
      (for [y y-axis]
        [(list x y) false])))))

(defn map-change [change state inmap]
  (map (fn [i] 
         (cond 
           (and (= change (first i)) (= state "turn on"))
           (list change true)

           (and (= change (first i)) (= state "turn off"))
           (list change false)

           (and (= change (first i)) (= state "toggle"))
           (list change (not (second i)))

           :else
           i)) inmap))

(defn make-change [x1 y1 x2 y2 state input]
  (let [change-list (concat 
                     (first 
                      (for [x (range x1 (inc x2))]
                        (for [y (range y1 (inc y2))]
                          (list x y)))))]
    (loop [[change & t] change-list
           grid input]
      (if (nil? t)
        (map-change change state grid)
        (recur t (map-change change state grid))))))

(defn parse [s] 
  (let [result (re-find #"(turn on|turn off|toggle) (\d+),(\d+) through (\d+),(\d+)" s)]
    [(Integer. (nth result 2))
     (Integer. (nth result 3))
     (Integer. (nth result 4))
     (Integer. (nth result 5))
     (second result)]))

(defn process [input s]
  (let [[x1 y1 x2 y2 state] (parse s)]
    (make-change x1 y1 x2 y2 state input)))
