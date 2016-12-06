(ns aoc2015.day22)

(defmulti cast-spell (fn [spell state] spell))

(defmethod cast-spell :magic-missile
  [spell {my-mana :my-mana, my-hp :my-hp, boss-hp :boss-hp, effects :effects}]
  {:my-mana (- my-mana 53),
   :my-hp my-hp,
   :boss-hp (- boss-hp 4),
   :effects effects})

(defmethod cast-spell :drain 
  [spell {my-mana :my-mana, my-hp :my-hp, boss-hp :boss-hp, effects :effects}]
  {:my-mana (- my-mana 73),
   :my-hp (+ my-hp 2),
   :boss-hp (- boss-hp 2),
   :effects effects})

(defmethod cast-spell :shield
  [spell {my-mana :my-mana, my-hp :my-hp, boss-hp :boss-hp, effects :effects}]
  {:my-mana (- my-mana 113),
   :my-hp my-hp,
   :boss-hp my-hp,
   :effects (conj effects {:name "Shield" :rounds-remaining 6 :armor 7})})

(defmethod cast-spell :poison
  [spell {my-mana :my-mana, my-hp :my-hp, boss-hp :boss-hp, effects :effects}]
  {:my-mana (- my-mana 173),
   :my-hp my-hp,
   :boss-hp my-hp,
   :effects (conj effects {:name "Poison" :rounds-remaining 6 :boss-dmg 3})})

(defmethod cast-spell :recharge
  [spell {my-mana :my-mana, my-hp :my-hp, boss-hp :boss-hp, effects :effects}]
  (println "Recharging...")
  {:my-mana (- my-mana 229),
   :my-hp my-hp,
   :boss-hp my-hp,
   :effects (conj effects {:name "Recharge" :rounds-remaining 5 :my-mana 101})})

(defn recharging? [effects]
  (pos? (count (filter #(= "Recharge" (:name %)) effects))))
(defn shielded? [effects]
  (pos? (count (filter #(= "Shield" (:name %)) effects))))

(defmulti turn (fn [whose input] whose))

(defn apply-effects [input]
  (println "Applying effects..." (:effects input))
  (let [zeroed-armor (assoc input :armor 0)])
  (for [effect (:effects input)]
    (if (= (:name effect) "Shield")
      (println "Shielded..." effect)
      (println effect))))

(defmethod turn :boss
  [whose input]
  (println "Taking boss turn." input)
  (let [effect-input (apply-effects input)])
  (if (>= 0  (:boss-hp input))
    "Boss dead"
    (turn :player (merge input {:my-hp (- (:my-hp input) 8)}))))

(defn cast-recharge? [input]
  (and  
   (>= 402 (:my-mana input))
   (not (recharging? (:effects input)))))

(defn cast-shield? [{mana :my-mana effects :effects}]
  (and (<= 113 mana)
       (not (shielded? effects))))

(defmethod turn :player 
  [whose input]
  (println "Taking player turn..." input)
  (if (>= 0 (:my-hp input))
    "Player dead."
    (turn :boss 
          (cond
            (cast-recharge? input)
            (cast-spell :recharge input)

            (cast-shield? input)
            (cast-spell :shield input)

            (<= 53 (:my-mana input))
            (cast-spell :magic-missile input)

            :else
            input))))
