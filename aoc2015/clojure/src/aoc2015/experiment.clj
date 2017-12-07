(ns aoc2015.experiment)

(defprotocol Shape
  (area [s] "Calculate the area of a shape")
  (perimeter [s] "Calculate the perimeter of a shape"))

(defrecord Rectangle [length width]
  Shape
  (area [this] (* length width))
  (perimeter [this] (* 2 (+ length width))))

(defrecord Triangle [x y z]
  Shape
  (area [this] (let [s (* 0.5 (+ x y z))]
                 (Math/sqrt (* s (- s x) (- s y) (- s z)))))
  (perimeter [this] (+ x y z)))
