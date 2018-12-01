(ns aoc2016.day01-test
  (:require [aoc2016.day01 :refer [turn move north east west]]
            [clojure.test :refer [deftest is testing]]))

(deftest turn-test-right
  (testing "turn-right"
    (is (= '(1 0) (turn "R" '(0 1))))))
(deftest turn-test-left
  (testing "turn-left"
    (is (= '(1 0) (turn "L" '(0 -1))))))

(deftest move-test
  (testing "moving to the right"
    (is (= (list east '(100 0)) (move north "R100"))))
  (testing "moving to the left"
    (is (= (list west '(-100 0)) (move north "L100")))))
