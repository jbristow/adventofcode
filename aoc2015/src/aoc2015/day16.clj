(ns aoc2015.day16)

(def input (clojure.string/split (slurp "resources/day16.input") #"\n"))

(defn get-info [infostring]
  (let [matches (re-seq #"(\w+): (\d+),?" infostring)]
    (apply merge {}
           (map 
            (fn [match] 
              {(keyword (first (rest match))) (Integer. (second (rest match)))}) 
            matches))))

(defn process-line [line]
  (let [match 
        (re-matches #"Sue (\d+): (.*)" line)]
    {(second match) (get-info (nth match 2))}))

(defn exclusion [k amount aunt]
  (let [item (first (vals aunt))]
    (and (contains? item k)
         (not= amount (k item)))))

(defn exclusion-gt [k amount aunt]
  (let [item (first (vals aunt))]
    (and (contains? item k)
         (>= amount (k item)))))

(defn exclusion-lt [k amount aunt]
  (let [item (first (vals aunt))]
    (and (contains? item k)
         (<= amount (k item)))))

(defn answer-1 [lines]
  (let [aunts (map process-line lines)]
    (->> 
     aunts
     (remove (partial exclusion :children 3))
     (remove (partial exclusion :cats 7))
     (remove (partial exclusion :samoyeds 2))
     (remove (partial exclusion :pomeranians 3))
     (remove (partial exclusion :akitas 0))
     (remove (partial exclusion :vizslas 0))
     (remove (partial exclusion :goldfish 5))
     (remove (partial exclusion :trees 3))
     (remove (partial exclusion :cars 2))
     (remove (partial exclusion :perfumes 1)))))

(defn answer-2 [lines]
  (let [aunts (map process-line lines)]
    (->> 
     aunts
     (remove (partial exclusion :children 3))
     (remove (partial exclusion-gt :cats 7))
     (remove (partial exclusion :samoyeds 2))
     (remove (partial exclusion-lt :pomeranians 3))
     (remove (partial exclusion :akitas 0))
     (remove (partial exclusion :vizslas 0))
     (remove (partial exclusion-lt :goldfish 5))
     (remove (partial exclusion-gt :trees 3))
     (remove (partial exclusion :cars 2))
     (remove (partial exclusion :perfumes 1)))))
