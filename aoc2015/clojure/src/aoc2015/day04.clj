(ns aoc2015.day04)

(defn md5 [s]
  (let [algorithm (java.security.MessageDigest/getInstance "MD5")
        size (* 2 (.getDigestLength algorithm))
        raw (.digest algorithm (.getBytes s))
        sig (.toString (BigInteger. 1 raw) 16)
        padding (clojure.string/join (repeat (- size (count sig)) "0"))]
    (str padding sig)))

(defn miner [k]
  (loop [n 0]
    (if (re-matches #"^00000.*" (md5 (str k n)))
      n
      (recur (inc n)))))
(defn miner-2 [k]
  (loop [n 0]
    (if (re-matches #"^000000.*" (md5 (str k n)))
      n
      (recur (inc n)))))
