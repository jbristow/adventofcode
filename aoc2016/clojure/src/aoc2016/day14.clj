(ns aoc2016.day14
  (:require [aoc2016.util :as util]))

(def salt
  ;"cuanljph"

  "qzyelonm")

(defn first-triplet [i]
  (let [matches (re-find #"(.)\1\1" i)]
    matches))

(def key-candidates (map #(str salt %) (iterate inc 0)))

(defn find-keys
  []
  (map (fn [s]
         (let [m (util/md5 s)]
           (list s
                 m
                 (Integer. (clojure.string/replace s salt ""))
                 (first-triplet m)))) key-candidates))

(defn multi-md5 [n s]
  (nth (iterate util/md5 s) n))

(defn find-keys-b
  []
  (map (fn [s]
         (let [m (multi-md5 2017 s)]
           (list s
                 m
                 (Integer. (clojure.string/replace s salt ""))
                 (first-triplet m)))) key-candidates))

(defn first-pentet [t s]
  (re-find (re-pattern (str t "{5}")) s))

(defn appears-as-pentet-in-n [lst c]
  (some #(first-pentet c (second %)) lst))

(defn answer []
  (loop [[[s m i [_ ch :as t] :as h] & r] (find-keys)
         found '()]
    (cond
      (= 64 (count found))
      found

      (and t
           (appears-as-pentet-in-n (take 1000 r) ch))
      (do (println s m i (inc (count found)))
          (recur r (conj found i)))

      :else
      (recur r found))))

(defn answer-b []
  (loop [[[s m i [_ ch :as t] :as h] & r] (find-keys-b)
         found '()]
    (cond
      (= 64 (count found))
      found

      (and t
           (appears-as-pentet-in-n (take 1000 r) ch))
      (do (println s m i (inc (count found)))
          (recur r (conj found i)))

      :else
      (recur r found))))
