(ns aoc2015.day03-test
  (:require [aoc2015.day03 :refer [delivery robo-delivery]]
            [clojure.string :as str]
            [clojure.test :refer [are deftest is testing]]))

(deftest sample-day03
  (testing "part1"
    (are [expected input] (= expected (delivery input))
      2 ">"
      4 "^>v<"
      2 "^v^v^v^v^v"))
  (testing "part2"
    (are [expected input] (= expected (robo-delivery input))
      3 "^v"
      3 "^>v<"
      11 "^v^v^v^v^v")))

(deftest answer-day03
  (let [data (str/trim (slurp "../resources/day03.input"))]
    (testing "part1"
      (is (= 2572 (delivery data))))
    (testing "part2"
      (is (= 2631 (robo-delivery data))))))
