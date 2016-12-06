(ns aoc2015.day02)

(def data
  (map
   (fn [row] (map #(Integer. %) (clojure.string/split row #"x")))
   (clojure.string/split (slurp "resources/day02.input") #"\n")))

(defn paper [l w h]
  (let [a (* 2 l w)
        b (* 2 w h)
        c (* 2 h l)
        d (/ (min a b c) 2)]
    (+ a b c d)))

(defn ribbon [l w h]
  (let  [a (+ l l w w)
         b (+ w w h h)
         c (+ h h l l)] 
    (+ (* l w h) (min a b c))))

(defn answer-1 [] (reduce + 0 (map (fn [[a b c]] (paper a b c)) data)))

(defn answer-2 [] (reduce + 0 (map (fn [[a b c]] (ribbon a b c)) data)))
