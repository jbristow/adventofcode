(ns aoc2015.day01-test
  (:require [aoc2015.day01 :refer [converter whereami]]
            [clojure.test :refer [are deftest is testing]]))

(deftest samples-day01
  (testing "sample data part01"
    (are [expected input] (= expected (converter input))
      0 "(())"
      3 "((("
      3 "(()(()("
      3 "))((((("
      -1 "())"
      -1 "))("
      -3 ")))"
      -3 ")())())"))
  (testing "sample data part02"
    (is (= 1 (whereami ")")))
    (is (= 5 (whereami "()())")))))

(deftest answers-day01
  (let [data (slurp "../resources/day01.input")]
    (testing "part01"
      (is (= 232 (converter data))))))
