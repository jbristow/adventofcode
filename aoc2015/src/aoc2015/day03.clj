(ns aoc2015.day03)

(def start-house '(0, 0))

(defn right [x y] 
  (println "going right from" x y [(inc x) y])
  (list (inc x) y))

(defn get-dir [c [x y]]
  (case c
    \> (list (inc x) y)
    \< (list (dec x) y)
    \^ (list x (inc y))
    \v (list x (dec y))))

(defn delivery [s]
  (loop [[h & t] s last-house start-house house-set [last-house]]
    (let [curr-house (get-dir h last-house)]
      (if (nil? t)
        (conj house-set curr-house)
        (recur t curr-house (conj house-set curr-house))))))

(defn robo-delivery [s]
  (loop [[h r & t] s
         last-santa-house start-house
         last-robot-house start-house
         santa-house-set [last-santa-house]
         robot-house-set [last-robot-house]]
    (let [curr-santa-house (get-dir h last-santa-house)
          curr-robot-house (get-dir r last-robot-house)]
      (if (nil? t)
        [(conj santa-house-set curr-santa-house)
         (conj robot-house-set curr-robot-house)]
        (recur
         t
         curr-santa-house
         curr-robot-house
         (conj santa-house-set curr-santa-house)
         (conj robot-house-set curr-robot-house))))))
