(ns aoc2015.day01)

(defn converter [s] (reduce + 0 (map #(if (= % \() 1 -1) s)))

(defn whereami [s]
  (loop [[h & t] (map #(if (= % \() 1 -1) s) floor 0 instr 0]
    (println h floor instr)
    (if (= -1 (+ floor h))
      (inc instr)
      (recur t (+ floor h) (inc instr)))))

(defn -main [& args] 
  (println (converter "(())"))
  (println (converter "()()"))
  (println (converter "((("))
  (println (converter "(()(()("))
  (println (converter "))((((("))
  (println (converter (slurp "resources/day01.input"))))
