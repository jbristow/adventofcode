(ns aoc2015.day14)

(def input (clojure.string/split (slurp "resources/day14.input") #"\n"))

(defn parse-line
  [line]
  (let
   [[_ name speed fly-time rest-time]
    (re-matches
     #"(\w+) can fly (\d+) km/s for (\d+) seconds, but then must rest for (\d+) seconds."
     line)
    cycle-time (+ (Integer. fly-time) (Integer. rest-time))
    cycle-count (quot 2503 cycle-time)
    remainder (rem 2503 cycle-time)
    distance (+ 
              (* cycle-count 
                 (Integer. fly-time) 
                 (Integer. speed)) 
              (* 
               (min 
                (Integer. fly-time) 
                remainder) 
               (Integer. speed)))]
    {:name name, 
     :speed speed, 
     :fly-time fly-time 
     :rest-time rest-time 
     :cycle-time cycle-time
     :cycle-count cycle-count
     :remainder remainder
     :total-dist distance}))

(defn parse-line-2
  [line]
  (let
   [[_ name speed fly-time rest-time]
    (re-matches
     #"(\w+) can fly (\d+) km/s for (\d+) seconds, but then must rest for (\d+) seconds."
     line)
    cycle-time (+ (Integer. fly-time) (Integer. rest-time))
    t (range 1 2504)]
    {:name name
     :speed (Integer. speed) 
     :fly-time (Integer. fly-time) 
     :rest-time (Integer. rest-time)
     :distance-at-time (map (fn [i] (let [cycle-count (quot i cycle-time)
                                          remainder (rem i cycle-time)
                                          distance (+ 
                                                    (* cycle-count 
                                                       (Integer. fly-time) 
                                                       (Integer. speed)) 
                                                    (* 
                                                     (min 
                                                      (Integer. fly-time) 
                                                      remainder) 
                                                     (Integer. speed)))]
                                      distance)) t)

     :points 0}))

(defn results [lines]
  (map
   (fn
     [i]
     (reverse
      (sort-by
       second
       (map
        (fn [{n :name, dist :distance-at-time}] 
          (list n (nth dist i)))
        (map parse-line-2 lines)))))
   (range 0 2503)))

(defn find-winner [l]
  (loop [[h & t] l
         last-distance -1
         winners []]
    (if (nil? h)
      winners
      (if (< (second h) last-distance)
        winners
        (recur t (second h) (concat winners [(first h)]))))))

(defn all-winners [results]
  (loop [[h & t] results
         winners []]
    (if (nil? h)
      winners
      (recur t (concat winners (find-winner h))))))

(defn answer [lines]
  (let [result-list (results lines)]
    (second
     (last
      (sort-by
       second
       (map
        (fn [i] [i (count (filter (partial = i) result-list))])
        (distinct (all-winners result-list))))))))
