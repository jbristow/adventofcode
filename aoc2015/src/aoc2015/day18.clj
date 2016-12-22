(ns aoc2015.day18
  (:import (java.io BufferedReader FileReader)))

(def puzzle-input (line-seq (BufferedReader. (FileReader. "resources/day18.input"))))

(defn input->map [input]
  (let [initial (map  #(clojure.string/split % #"") input)]
    (apply merge
           (map-indexed (fn [y v]
                          (apply merge
                                 (map-indexed (fn [x w]
                                                (hash-map (clojure.string/join [x "-" y])
                                                          (hash-map :x x :y y :on (= "#" w))))
                                              v)))
                        initial))))

(defn input->map-b [input]
  (let [maxnum (dec (count input))]
    (merge (input->map input) {"0-0" {:x 0 :y 0 :on true}
                               (str "0-" maxnum) {:x 0 :y maxnum :on true}
                               (str maxnum "-" maxnum) {:x maxnum :y maxnum :on true}
                               (str maxnum "-0") {:x maxnum :y 0 :on true}})))

(defn on-neighbors [lst]
  (count (remove #(false? (:on %)) lst)))

(defn get-neighbors [{:keys [x y]} m]
  (remove nil? [(get m (clojure.string/join [(dec x) "-" (dec y)]))
                (get m (clojure.string/join [(dec x) "-" y]))
                (get m (clojure.string/join [(dec x) "-" (inc y)]))
                (get m (clojure.string/join [x "-" (dec y)]))
                (get m (clojure.string/join [x "-" (inc y)]))
                (get m (clojure.string/join [(inc x) "-" (dec y)]))
                (get m (clojure.string/join [(inc x) "-" y]))
                (get m (clojure.string/join [(inc x) "-" (inc y)]))]))

(defn step [m]
  (apply merge (map (fn [[label {:keys [x y on] :as node}]]
                      (let [neighbors (get-neighbors node m)
                            on-count (on-neighbors neighbors)]
                        (hash-map label
                                  (merge node {:on (cond
                                                     (and on (and (not= 2 on-count)
                                                                  (not= 3 on-count)))
                                                     false

                                                     (and (not on) (= 3 on-count))
                                                     true

                                                     :else
                                                     on)})))) m)))

(defn step-broken [m maxnum]
  (merge (step m) {"0-0" {:x 0 :y 0 :on true}
                   (str "0-" maxnum) {:x 0 :y maxnum :on true}
                   (str maxnum "-" maxnum) {:x maxnum :y maxnum :on true}
                   (str maxnum "-0") {:x maxnum :y 0 :on true}}))

(defn n-steps [n m]
  (nth (iterate step (input->map m)) n))

(defn n-steps-b [n m]
  (nth (iterate #(step-broken % (dec (count m))) (input->map-b m)) n))

(defn pretty-print [input n]
  (let [m (n-steps n input)
        maxn (count input)]
    (doseq [y (range maxn)]
      (let [xlst (range maxn)
            points (clojure.string/join
                    (map (fn [x]
                           (if (:on (get m (clojure.string/join [x "-" y]))) "#" "."))
                         xlst))]

        (println points)))))

(defn pretty-print-b [input n]
  (let [m (n-steps-b n input)
        maxn (count input)]
    (doseq [y (range maxn)]
      (let [xlst (range maxn)
            points (clojure.string/join
                    (map (fn [x]
                           (if (:on (get m (clojure.string/join [x "-" y]))) "#" "."))
                         xlst))]

        (println points)))))

(defn answer []
  (count (filter (fn [[_ {on :on}]] on) (n-steps 100 puzzle-input))))

(defn answer-b []
  (count (filter (fn [[_ {on :on}]] on) (n-steps-b 100 puzzle-input))))

(def sample (clojure.string/split ".#.#.#
...##.
#....#
..#...
#.#..#
####.." #"\s+"))

