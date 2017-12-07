(ns aoc2015.day08)

(defn count-chars [input] 
  (do
    (println "Input:" input)
    (let [sanitized (-> input
                        (clojure.string/replace #"\\\\" "S")
                        (clojure.string/replace #"\\\"" "Q")
                        (clojure.string/replace #"\\x[0-9a-fA-F][0-9a-fA-F]" "N"))
          matches (re-matches #"^\"(.*)\"$" sanitized)]
      (println "Sanitized:" sanitized)
      {:chars (count (second matches)) :code (count input)})))

(defn count-encoded [input] 
  (do
    (println "Input:" input)
    (let [sanitized (-> input
                        (clojure.string/replace #"\\" "\\\\\\\\")
                        (clojure.string/replace #"\"" "\\\\\""))]
      (println "Encoded:" sanitized)
      {:encoded (count (apply str "\"" sanitized "\"")) :code (count input)})))
