(ns aoc2015.day08-test
  (:require [aoc2015.day08 :refer :all]
            [clojure.test :refer [deftest is]]))

(deftest empty-string 
  (is (= {:code 2 :chars 0} (count-chars "\"\""))))

(deftest simple-string
  (is (= {:code 5 :chars 3} (count-chars "\"abc\""))))

(deftest quoted-string
  (is (= {:code 10 :chars 7} (count-chars "\"aaa\\\"aaa\""))))

(deftest symbol-string
  (is (= {:code 6 :chars 1} (count-chars "\"\\x27\""))))

(deftest empty-encode 
  (is (= {:code 2 :encoded 6} (count-encoded "\"\""))))
(deftest simple-encode
  (is (= {:code 5 :encoded 9} (count-encoded "\"abc\""))))
(deftest quoted-encode
  (is (= {:code 10 :encoded 16} (count-encoded "\"aaa\\\"aaa\""))))

(deftest symbol-encode
  (is (= {:code 6 :encoded 11} (count-encoded "\"\\x27\""))))
