(ns aoc2016.day10
  (:import (java.io BufferedReader FileReader)))

(defn create-bot [q v] {:queue q :values v})

(defn process [q bots output] nil)

(defn parse-line [input]
  (let [split-line (clojure.string/split input #" ")]
    (cond (= "bot" (first split-line))
          {:which :default
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

(defmethod give :direct [{bot-n :bot-n value :value :as instruction} bots]
  (let [{q :queue :as curr-bot} (get bots bot-n)
        v (sort (conj (:values curr-bot) value))]
    (cond (nil? curr-bot)
          (merge bots {bot-n (create-bot q v)})

          (= 2 (count v))
          (give {:which :direct :value (first v) :bot-n (:low q)}
                (give {:which :direct :value (second v) :bot-n (:high q)}
                      (merge bots {bot-n (create-bot nil v)})))
          :else
          (merge bots {bot-n (create-bot q v)}))))

(defmethod give :default [{bot-n :bot-n :as instruction} bots]
  (let [{v :values :as curr-bot} (get bots bot-n)
        q instruction]
    (cond
      (nil? bot-n)
      {}

      (nil? curr-bot)
      (merge bots {bot-n (create-bot instruction '())})

      (= 2 (count v))
      (give {:which :direct :value (first v) :bot-n (:low q)}
            (give {:which :direct :value (second v) :bot-n (:high q)}
                  (merge bots {bot-n (create-bot nil v)})))

      :else
      (merge bots {bot-n (create-bot instruction v)}))))

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
