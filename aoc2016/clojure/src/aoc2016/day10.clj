(ns aoc2016.day10
  (:import (java.io BufferedReader FileReader)))

(defn create-bot [q v] {:queue q :values v})

(defn parse-line [input]
  (let [split-line (clojure.string/split input #" ")]
    (cond (= "bot" (first split-line))
          {:which :queue
           :bot-n (str "bot" (get split-line 1))
           :low (str (get split-line 5)
                     (get split-line 6))
           :high (str (get split-line 10)
                      (get split-line 11))}

          (= "value" (first split-line))
          {:which :direct
           :value (Integer. (get split-line 1))
           :bot-n (str "bot" (get split-line 5))})))

(defmulti give (fn [instruction bots] (:which instruction)))

(defn update-bots [bot-n {:keys [low high] :as q} [low-value high-value :as v] bots]
  (if (= 2 (count v))
    (give {:which :direct :value low-value :bot-n low}
          (give {:which :direct :value high-value :bot-n high}
                (merge bots {bot-n (create-bot nil v)})))

    (merge bots {bot-n (create-bot q v)})))

(defmethod give :direct [{:keys [bot-n value] :as instruction} bots]
  (let [{q :queue curr-values :values} (get bots bot-n)
        [low-value high-value :as v] (sort (conj curr-values value))]
    (update-bots bot-n q v bots)))

(defmethod give :queue [{:keys [bot-n] :as q} bots]
  (let [{v :values} (get bots bot-n)]
    (update-bots bot-n q v bots)))

(defmethod give :default [_ _] {})

(def sample ["value 5 goes to bot 2"
             "bot 2 gives low to bot 1 and high to bot 0"
             "value 3 goes to bot 1"
             "bot 1 gives low to output 1 and high to bot 0"
             "bot 0 gives low to output 2 and high to output 0"
             "value 2 goes to bot 2"])

(defn final-bots []
  (reduce (fn [a b] (give b a)) {}
          (map parse-line
               (line-seq (BufferedReader.
                          (FileReader. "resources/day10.txt"))))))

(defn answer []
  (ffirst (filter #(= '(17 61) (:values (val %))) (final-bots))))

(defn answer-b []
  (apply * (mapcat :values (vals (select-keys (final-bots)
                                              ["output0" "output1" "output2"])))))
