(ns aoc2015.day23)

(def input (slurp "resources/day23.input"))

(def matches 
  (->> (clojure.string/split input #"\n")
       (map (partial re-matches #"(?:(hlf|tpl|inc) ([ab]))|(?:(jmp) ([+\-])(\d+))|(?:(jie|jio) ([ab]), ([+\-])(\d+))"))
       (map (partial remove nil?))
       (map rest)))

(defmulti line-of-code (fn [instr & args] 
                         (do
                           (println "instr" instr)
                           (str instr))))
(defmethod line-of-code "inc" [instr register state]
  (assoc state 
         register (inc (get state register))
         :line (inc (:line state))))
(defmethod line-of-code "hlf" [instr register state]
  (assoc state 
         register (/ (get state register) 2)
         :line (inc (:line state))))
(defmethod line-of-code "tpl" [instr register state]
  (assoc state 
         register (* (get state register) 3)
         :line (inc (:line state))))
(defmethod line-of-code "jmp" [instr direction amount state]
  (if (= direction "+")
    (update-in state [:line] + (Integer. amount)) 
    (update-in state [:line] - (Integer. amount))))

(defmethod line-of-code "jie" [instr register direction amount state]
  (if (even? (get state register))
    (line-of-code "jmp" direction amount state)
    (update-in state [:line] inc)))

(defmethod line-of-code "jio" [instr register direction amount state]
  (if (= 1 (get state register))
    (line-of-code "jmp" direction amount state)
    (update-in state [:line] inc)))

(defn run-program [input a-start b-start]
  (let [line-count (count input)]
    (loop [state {"a" a-start, "b" b-start, :line 0 :total-lines line-count}]
      (println "Current State:" state)
      (if (>= (:line state) line-count)
        state
        (let [curr-line (nth input (:line state))
              curr-args (reverse (conj (reverse curr-line) state))]
          (println "Current Line:" curr-line)
          (let [new-state (apply line-of-code curr-args)]
            (println "Next State:" new-state)
            (recur new-state)))))))
