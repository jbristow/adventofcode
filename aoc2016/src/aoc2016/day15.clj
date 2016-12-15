(ns aoc2016.day15)

(defn create-disc [max-position position]
  {:max-position max-position :position position})

;;
(def disc-1 (create-disc 17 1))
(def disc-2 (create-disc 7 0))
(def disc-3 (create-disc 19 2))
(def disc-4 (create-disc 5 0))
(def disc-5 (create-disc 3 0))
(def disc-6 (create-disc 13 5))

(def discs [disc-1
            disc-2
            disc-3
            disc-4
            disc-5
            disc-6])

(def discs-b (concat discs [(create-disc 11 0)]))

(def small [(create-disc 5 4)
            (create-disc 2 1)])

(def ideal [(create-disc 17 0)])

(defn step [state n]
  (map (fn [{:keys [max-position position]}]
         (create-disc max-position (mod (+ position n) max-position)))
       state))

(defn clear? [state]
  (let [size (count state)]
    (empty?
     (remove (fn [[i {:keys [max-position position]}]]
               (= position (+ max-position (- 0 (inc i))))) (map-indexed (fn [i v] [i v]) state)))))

(defn correct-position? [i {:keys [max-position position]}]
  (= (- max-position (mod i max-position))
     position))

(defn answer [d]
  (loop [state d
         n 0]
    (cond
      (clear? state)
      n

      (every? identity (map-indexed #(correct-position? (inc %1) %2)
                                    (take 6 state)))
      n

      (every? identity (map-indexed #(correct-position? (inc %1) %2)
                                    (take 5 state)))
      (let [j (apply * (map :max-position (take 5 state)))]
        (recur (step state j) (+ n j)))

      (every? identity (map-indexed #(correct-position? (inc %1) %2)
                                    (take 4 state)))
      (let [j (apply * (map :max-position (take 4 state)))]
        (recur (step state j) (+ n j)))

      (every? identity (map-indexed #(correct-position? (inc %1) %2)
                                    (take 3 state)))
      (let [j (apply * (map :max-position (take 3 state)))]
        (recur (step state j) (+ n j)))

      (every? identity (map-indexed #(correct-position? (inc %1) %2)
                                    (take 2 state)))
      (let [j (apply * (map :max-position (take 2 state)))]
        (recur (step state j) (+ n j)))

      (every? identity (map-indexed #(correct-position? (inc %1) %2)
                                    (take 1 state)))
      (let [j (apply * (map :max-position (take 1 state)))]
        (recur (step state j) (+ n j)))

      :else
      (recur (step state 1) (inc n)))))

(defn answer-b [d]
  (loop [state d
         n 0]
    (cond
      (clear? state)
      n

      (every? identity (map-indexed #(correct-position? (inc %1) %2)
                                    (take 7 state)))
      n

      (every? identity (map-indexed #(correct-position? (inc %1) %2)
                                    (take 6 state)))
      (let [j (apply * (map :max-position (take 6 state)))]
        (recur (step state j) (+ n j)))

      (every? identity (map-indexed #(correct-position? (inc %1) %2)
                                    (take 5 state)))
      (let [j (apply * (map :max-position (take 5 state)))]
        (recur (step state j) (+ n j)))

      (every? identity (map-indexed #(correct-position? (inc %1) %2)
                                    (take 4 state)))
      (let [j (apply * (map :max-position (take 4 state)))]
        (recur (step state j) (+ n j)))

      (every? identity (map-indexed #(correct-position? (inc %1) %2)
                                    (take 3 state)))
      (let [j (apply * (map :max-position (take 3 state)))]
        (recur (step state j) (+ n j)))

      (every? identity (map-indexed #(correct-position? (inc %1) %2)
                                    (take 2 state)))
      (let [j (apply * (map :max-position (take 2 state)))]
        (recur (step state j) (+ n j)))

      (every? identity (map-indexed #(correct-position? (inc %1) %2)
                                    (take 1 state)))
      (let [j (apply * (map :max-position (take 1 state)))]
        (recur (step state j) (+ n j)))

      :else
      (recur (step state 1) (inc n)))))
