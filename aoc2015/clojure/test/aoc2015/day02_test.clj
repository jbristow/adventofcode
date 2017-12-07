(ns aoc2015.day02-test
  (:require [aoc2015.day02 :refer [paper parse-data parse-line ribbon total]]
            [clojure.test :refer [deftest is testing]])
  (:import (java.io BufferedReader FileReader)))

(deftest parse-line-test
  (testing "parsing"
    (is (= '(2 3 4) (parse-line "2x3x4")))
    (is (= '(1 1 10) (parse-line "1x1x10")))))

(deftest parse-data-test
  (testing "parsing lines"
    (is (= [[2 3 4] [1 1 10]] (parse-data ["2x3x4" "1x1x10"])))))

(deftest samples-day01
  (testing "part01"
    (is (= 58 (paper [2 3 4])))
    (is (= 43 (paper [1 1 10]))))
  (testing "part02"
    (is (= 34 (ribbon [2 3 4])))
    (is (= 14 (ribbon [1 1 10])))))

(deftest answers-day01
  (let [data (line-seq (BufferedReader. (FileReader. "../resources/day02.input")))]
    (testing "part01"
      (is (= 1606483 (total :paper data))))
    (testing "part02"
      (is (= 3842356 (total :ribbon data))))))
