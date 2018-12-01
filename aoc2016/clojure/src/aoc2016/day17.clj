(ns aoc2016.day17
  (:require [aoc2016.util :refer [breadth-first md5]]
            [clojure.zip :as zip]))

(def passcode "vwbaicqe")

(defn doorzip [input]
  (zip/zipper
   (fn [[x y route]]
     (let [[u d l r] (map #(< 10 (Integer/parseInt (str %) 16))
                          (take 4 (md5 (str input route))))]
       (and (not (and (= 3 x) (= 3 y)))

            (or (and u (pos? y))
                (and d (> 3 y))
                (and l (pos? x))
                (and r (> 3 x))))))

   (fn [[x y route]]
     (when-not (and (= x 3) (= y 3))
       (let [dir-code (take 4 (md5 (str input route)))]
         (keep (fn [[_ dir]]
                 (cond (and (= dir "U") (pos? y))
                       [x (dec y) (str route "U")]

                       (and (= dir "D") (> 3 y))
                       [x (inc y) (str route "D")]

                       (and (= dir "L") (pos? x))
                       [(dec x) y (str route "L")]

                       (and (= dir "R") (> 3 x))
                       [(inc x) y (str route "R")]))
               (filter #(< 10 (first %))
                       (map #(vector (Integer/parseInt (str %1) 16) %2)
                            dir-code
                            ["U" "D" "L" "R"]))))))

   (fn [_ c] c)

   [0 0 ""]))

(defn answer [input]
  (last
   (zip/node
    (first
     (filter #(let [[x y route] (zip/node %)]
                (and (= 3 x)
                     (= 3 y)))
             (breadth-first (doorzip input)))))))

(defn answer-b [input]
  (count
   (zip/path
    (last
     (filter #(let [[x y route] (zip/node %)]
                (and (= 3 x)
                     (= 3 y)))
             (breadth-first (doorzip input)))))))
