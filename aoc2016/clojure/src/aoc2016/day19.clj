(ns aoc2016.day19)

(defn answer [n]
  (Integer/valueOf (clojure.string/join (concat (drop 1 (Integer/toBinaryString n)) [1])) 2))

(defn answer-b
  ([n p]
   (let [p1 (first (drop-while #(<= (* 3 %) n) (iterate #(* 3 %) p)))]
     (if (= n p1)
       n
       (- n (+ p1 (max (- n (* 2 p1)) 0))))))
  ([n] (answer-b n 1)))
