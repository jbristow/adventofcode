(ns aoc2015.day11)

(defn char->index [c]
  (- (int c) (int \a)))

(defn index->char [c]
  (char (+ (int c) (int \a))))

(defn has-straight? [input]
  (seq
   (filter
    #(and 
      (< (first %) (second %) (nth % 2)) 
      (= (+ 2 (first %)) (nth % 2))) 
    (partition 3 1 input))))

(defn contains-forbidden? [input]
  (seq 
   (filter 
    #(or 
      (= (char->index \i) %)
      (= (char->index \o) %)
      (= (char->index \l) %))
    input)))

(defn has-doubles? [input]
  (<= 2 
      (count 
       (distinct 
        (filter #(apply = %) (partition 2 1 input))))))

(defn passes-security? [input]
  (and
   (has-straight? input)
   (not (contains-forbidden? input))
   (has-doubles? input)))

(defn next-pw [input]
  (loop [[h & t] (reverse (map char->index input))
         overflow true 
         output []]
    (cond
      (and (nil? h) (passes-security? (reverse output)))
      (clojure.string/join (map index->char (reverse output)))

      (nil? h)
      (recur output true [])

      overflow
      (recur t (zero? (mod (inc h) 26)) (conj output (mod (inc h) 26)))

      :else
      (recur t false (conj output h)))))
