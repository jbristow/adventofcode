(ns aoc2015.day10)

(defn look [input]
  (loop [[h & r] input
         saw []
         current {:letter h :count 0}]
    (if (nil? h)
      (conj saw current)
      (let [same-letter-state (update-in current [:count] inc)
            different-letter-state {:letter h :count 1}]
        (if (= h (:letter current))
          (recur r saw same-letter-state)
          (recur r (conj saw current) different-letter-state))))))

(defn say [input]
  (mapcat 
   (fn [{:keys [count letter]}] 
     (list count (Integer. (str letter))))
   input))

(defn see->say [start n]
  (loop [i 0
         next-str (say (look start))]
    (if (< i (dec n))
      (recur (inc i) (say (look next-str)))
      next-str)))
