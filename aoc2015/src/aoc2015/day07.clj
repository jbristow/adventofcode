(ns aoc2015.day07)

(defn match-digit [[matches line]]
  (if matches 
    [matches line]
    (if-let [[_ value varname & others] (re-matches #"(\d+) -> (\w+)" line)]
      [{:type "assign" :depends-on nil :val (Integer. value) :var varname} line]
      [nil line])))

(defn match-not [[matches line]]
  (if matches 
    [matches line]
    (if-let [[_ notvar varname & others] (re-matches #"NOT (\w+) -> (\w+)" line)]
      [{:type "not" :depends-on #{notvar} :var varname} line]
      [nil line])))

(defn match-lshift
  [[matches line]]
  (if matches
    [matches line]
    (if-let [[_ a b varname & others] (re-matches #"(\w+) LSHIFT (\d+) -> (\w+)" line)]
      [{:type "lshift", :depends-on #{a}, :amount (Integer. b), :var varname} line] 
      [nil line])))

(defn match-rshift
  [[matches line]]
  (if matches [matches line]
      (if-let [[_ a b varname & others] (re-matches #"(\w+) RSHIFT (\d+) -> (\w+)" line)] 
        [{:type "rshift", :depends-on #{a}, :amount (Integer. b), :var varname} line]
        [nil line])))

(defn match-num-and
  [[matches line]]
  (if matches [matches line]
      (if-let [[_ a b varname & others] (re-matches #"(\d+) AND (\w+) -> (\w+)" line)]
        [{:type "num-and", :depends-on #{b}, :val (Integer. a) :var varname} line]
        [nil line])))

(defn match-and
  [[matches line]]
  (if matches [matches line]
      (if-let
       [[_ a b varname & others] (re-matches #"(\w+) AND (\w+) -> (\w+)" line)]
        [{:type "and",
          :depends-on #{a b},
          :a a
          :b b
          :var varname} line]
        [nil line])))

(defn match-or
  [[matches line]]
  (if matches
    [matches line]
    (if-let
     [[_ a b varname & others]
      (re-matches #"(\w+) OR (\w+) -> (\w+)" line)]
      [{:type "or",
        :depends-on #{a b},
        :a a
        :b b
        :var varname}
       line]
      [nil line])))

(defn match-eq [[matches line]]
  (if matches 
    [matches line]
    (if-let [[_ notvar varname & others] (re-matches #"(\w+) -> (\w+)" line)]
      [{:type "eq" :depends-on #{notvar} :var varname} line]
      [nil line])))

(defn parse-line [line]
  (let [result (->> [nil line] 
                    match-digit
                    match-eq
                    match-not
                    match-lshift
                    match-rshift
                    match-num-and
                    match-and
                    match-or)]
    result))

(defn parse-lines 
  [lines] 
  (map 
   #(first (parse-line %)) 
   lines))

(defn sort-parsed [l]
  (loop [[h & r] (remove #(nil? (:depends-on %)) l)
         output (filter #(nil? (:depends-on %)) l)]
    (if (nil? h)
      output
      (let [deps (set (map :var output))]
        (if (clojure.set/subset? (:depends-on h) deps)
          (recur r (concat output [h]))
          (recur (concat r [h]) output))))))

(defmulti process (fn [m o] (:type m)))

(defmethod process "assign" 
  [{varname :var value :val} o] 
  (assoc o varname value))

(defmethod process "eq" 
  [{varname :var depends :depends-on} o]
  (assoc o varname (get o (first (vec depends)))))

(defmethod process "not" 
  [{varname :var depends :depends-on} o]
  (let [result (bit-and 65535 (bit-not (get o (first (vec depends)))))]
    (assoc o varname result)))

(defmethod process "lshift" 
  [{varname :var depends :depends-on amount :amount} o]
  (let [a-val (get o (first (vec depends))) 
        shift-result (bit-shift-left a-val amount)
        result (bit-and 65535 shift-result)]
    (assoc o varname result)))

(defmethod process "rshift" 
  [{varname :var depends :depends-on amount :amount} o]
  (let [a-val (get o (first (vec depends))) 
        shift-result (bit-shift-right a-val amount)
        result (bit-and 65535 shift-result)]
    (assoc o varname result)))

(defmethod process "and"
  [{varname :var depends :depends-on a :a b :b} o]
  (let [a-val (get o a)
        b-val (get o b)
        and-result (bit-and a-val b-val)
        result (bit-and 65535 and-result)]
    (assoc o varname result)))

(defmethod process "num-and"
  [{varname :var depends :depends-on value :val} o]
  (let [[a-var] (vec depends)
        a-val (get o a-var)
        result (bit-and value a-val)]
    (assoc o varname result)))

(defmethod process "or"
  [{varname :var depends :depends-on a :a b :b} o]
  (let [a-val (get o a)
        b-val (get o b)
        result (bit-and 65535 (bit-or a-val b-val))]
    (assoc o varname result)))

(defn process-all [l]
  (loop [[h & t] l
         output {}]
    (if (nil? h)
      output
      (recur t (process h output)))))
