(ns aoc2020.day01
  (:import (java.nio.file Files Paths)))

(def file-name "../kotlin/src/main/resources/day01.txt")

(defn part1 [input]
  (let [numbers (map #( Integer/parseInt %) input)]
    (->> numbers
         (pmap (fn [n] (map #(list n %) (filter #(== 2020 (+ n %)) numbers))))
         (mapcat identity)
         (filter #(and (apply not= %) (nth % 1)))
         (first)
         (apply *))))

(defn part2 [input]
  (let [numbers (map #( Integer/parseInt %) input)]
    (->> (pmap (fn [n] ( map #(list n %) numbers)) numbers)
         (mapcat identity)
         (filter (fn [[a b]] (and (not= a b)
                                  (> 2020 (+ a b)))))
         (pmap (fn [[a b]] (pmap #(list a b %) numbers)))
         (mapcat identity)
         (filter (fn [[a b c]] (== 2020 (+ a b c))))
         first
         (apply *))))


(defn -main
  [& args]
  (let [line-stream (Files/lines (Paths/get file-name (make-array String 0)))
        lines (-> line-stream .iterator iterator-seq)]
    (do (println "Part1" (part1 lines))
        (println "Part2" (part2 lines))
        (.close line-stream)
        nil)))


