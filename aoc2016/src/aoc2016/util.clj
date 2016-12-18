(ns aoc2016.util
  (:require [clojure.zip :as zip])
  (:import (java.security MessageDigest)))

(defn md5 [s]
  (let [algorithm (MessageDigest/getInstance "MD5")
        size (* 2 (.getDigestLength algorithm))
        raw (.digest algorithm (.getBytes s))
        sig (.toString (BigInteger. 1 raw) 16)
        padding (clojure.string/join (repeat (- size (count sig)) "0"))]
    (str padding sig)))

(defn- zip-children [loc]
  (when-let [child (zip/down loc)]
    (take-while (complement nil?) (iterate zip/right child))))

(defn breadth-first
  "Take a zipper location and return a list of nodes in breadth first order."
  [loc]
  ((fn bfs [queue]
     (lazy-seq
      (when (seq queue)
        (let [new-loc  (peek queue)
              children (zip-children new-loc)]
          (cons new-loc (bfs (into (pop queue) children)))))))
   (conj clojure.lang.PersistentQueue/EMPTY loc)))
