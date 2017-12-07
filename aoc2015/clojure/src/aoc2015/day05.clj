(ns aoc2015.day05)

(def vowels [\a \e \i \o \u])
(def bad-strs ['(\a \b) '(\c \d) '(\p \q) '(\x \y)])
(defn has-gt-three-vowels? [s] 
  (<= 3 (count (filter (fn [c] (some #(= c %) vowels)) s))))

(defn has-doubles? [s]
  (<= 1 (count (filter #(= (first %) (second %)) (partition 2 1 s)))))

(defn has-bad? [s]
  (<= 1 (count (filter (fn [t] (some #(= t %) bad-strs)) (partition 2 1 s)))))

(defn is-nice? [s]
  (and (has-gt-three-vowels? s)
       (has-doubles? s)
       (not (has-bad? s))))

(defn is-nice-2? [s]
  (some? (re-find #"(?=.*(\w).\1)(?=.*(\w\w).*\2)" s)))
