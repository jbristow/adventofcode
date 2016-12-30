(ns aoc2016.day23
  (:require [aoc2016.day12 :as d12])
  (:import (java.io BufferedReader FileReader)))

(defn register? [s]
  (re-matches #"[a-z]" s))

(defn get-value [register registers]
  (if (register? register)
    (get registers register)
    (Long. register)))

(defmulti instruction (fn [line registers n instructions]
                        (first line)))

(defmethod instruction "cpy" [[_ value register _] registers n instructions]
  (cond
    (not (register? register))
    [registers (inc n) instructions]

    (register? value)
    [(merge registers {register (get registers value)}) (inc n) instructions]

    :else
    [(merge registers {register (Long. value)}) (inc n) instructions]))

(defn adder? [[i-0 arg0-0]
              [i-1 arg0-1]
              [i-2 arg0-2 arg1-2]
              registers]
  (and (not (nil? i-1))
       (not (nil? i-2))
       (register? arg0-1)
       (register? arg0-2)
       (= arg0-1 arg0-2)
       (= "-2" arg1-2)
       (= "dec" i-1)
       (pos? (get-value arg0-1 registers))))

(defn multiplier?  [[i-0 arg0-0]
                    [i-1 arg0-1]
                    [i-2 arg0-2 arg1-2]
                    [i-3 arg0-3 arg1-3]
                    [i-4 arg0-4 arg1-4]
                    registers]
  (and (not (nil? i-2))
       (not (nil? i-3))
       (not (nil? i-4))
       (= "jnz" i-2 i-4)
       (= "dec" i-1 i-3)
       (= "-2" arg1-2)
       (= "-5" arg1-4)
       (pos? (get-value arg0-1 registers))
       (pos? (get-value arg0-3 registers))))

(defmethod instruction "inc" [[i-0 arg0-0 :as line-0] registers n instructions]
  (let [[i-1 arg0-1 :as line-1] (get instructions (inc n))
        [i-2 arg0-2 arg1-2 :as line-2] (get instructions (+ 2 n))
        [i-3 arg0-3 arg1-3 :as line-3] (get instructions (+ 3 n))
        [i-4 arg0-4 arg1-4 :as line-4] (get instructions (+ 4 n))]
    (cond
      (multiplier? line-0 line-1 line-2 line-3 line-4 registers)
      [(merge registers {arg0-1 0
                         arg0-0 (+ (get-value arg0-0 registers)
                                   (* (get-value arg0-1 registers)
                                      (get-value arg0-3 registers)))}) (+ 5 n) instructions]

      (adder? line-0 line-1 line-2 registers)
      [(merge registers {arg0-1 0
                         arg0-0 (+ (get-value arg0-0 registers)
                                   (get-value arg0-1 registers))}) (+ 3 n) instructions]

      :else

      [(merge registers {arg0-0 (inc (get registers arg0-0))}) (inc n) instructions])))

(defmethod instruction "dec" [[_ arg0-0] registers n instructions]
  (let [[i-1 arg0-1] (get instructions (inc n))
        [i-2 arg0-2 arg1-2] (get instructions (+ 2 n))]
    (cond
      (and (not (nil? i-1))
           (not (nil? i-2))
           (register? arg0-0)
           (register? arg0-2)
           (= arg0-2 arg0-0)
           (= "-2" arg1-2)
           (= "inc" i-1)
           (pos? (get-value arg0-0 registers)))
      [(merge registers {arg0-0 0
                         arg0-1 (+ (get-value arg0-0 registers)
                                   (get-value arg0-1 registers))}) (+ 3 n) instructions]

      :else
      [(merge registers {arg0-0 (dec (get registers arg0-0))}) (inc n) instructions])))

(defmethod instruction "jnz" [[_ register jump-amount] registers n instructions]
  (let [value (get-value register registers)
        j (get-value jump-amount registers)
        next-n (d12/jump value n j)]

    [registers next-n instructions]))

(defn toggle [value lines]
  (let [[instr a b :as line] (get lines value)
        arguments (dec (count line))]
    (cond
      (nil? line)
      lines

      (and (= 1 arguments)
           (= "inc" instr))
      (merge lines {value ["dec" a]})

      (= 1 arguments)
      (merge lines {value ["inc" a]})

      (and (= 2 arguments)
           (= "jnz" instr))
      (merge lines {value ["cpy" a b]})

      (= 2 arguments)
      (merge lines {value ["jnz" a b]}))))

(defmethod instruction "tgl" [[_ v] registers n instructions]
  [registers (inc n) (toggle (+ n (get-value v registers)) instructions)])

(def puzzle-data (line-seq (BufferedReader. (FileReader. "resources/day23.txt"))))

(defn answer [data]
  (let [instructions (map #(clojure.string/split % #" ") data)
        max-line (count instructions)]
    (loop [registers {"a" 12 "b" 0 "c" 0 "d" 0}
           n 0
           instr-map (apply merge (map-indexed hash-map instructions))]
      (when (neg? (get registers "d")) (throw (Exception. "neg-d")))
      (if (<= max-line n)
        registers
        (let [[next-registers next-n next-instr-map] (instruction (get instr-map n) registers n instr-map)]
          (recur next-registers next-n next-instr-map))))))
