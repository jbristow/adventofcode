(ns aoc2016.day02-test
  (:require [aoc2016.day02 :refer [keypad-move]]
            [clojure.test :refer [deftest is are testing]]))

(deftest test-move
  (testing "move up"
    (are [start-key expected] (= expected (keypad-move \U start-key))
         1 1
         2 2
         3 3
         4 1
         5 2
         6 3
         7 4
         8 5
         9 6))
  (testing "move down"
    (are [start-key expected] (= expected (keypad-move \D start-key))
         1 4
         2 5
         3 6
         4 7
         5 8
         6 9
         7 7
         8 8
         9 9))
  (testing "move left"
    (are [start-key expected] (= expected (keypad-move \L start-key))
         1 1
         2 1
         3 2
         4 4
         5 4
         6 5
         7 7
         8 7
         9 8))
  (testing "move right"
    (are [start-key expected] (= expected (keypad-move \R start-key))
         1 2
         2 3
         3 3
         4 5
         5 6
         6 6
         7 8
         8 9
         9 9)))
