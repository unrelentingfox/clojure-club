(ns clojure-club.playground.astar)

(def map-32x32x4
  ["################################"
   "#######################     ####"
   "######################       ###"
   "#####################        ###"
   "##################     ###  ####"
   "#################     ##########"
   "#################     ##########"
   "#################     ##########"
   "#####  ##########    ###########"
   "####    ######      ############"
   "###             ################"
   "###      #######################"
   "####    ########################"
   "#####################  #########"
   "####################    ########"
   "###################     ########"
   "##################     #########"
   "##################     #########"
   "#######   #####        #########"
   "#######               ##########"
   "#######           ##############"
   "######        ##################"
   "######    ########!#############"
   "#######   ######################"
   "################################"
   "################################"
   "##########   ###################"
   "#########     ##################"
   "##########    ##################"
   "###########    #################"
   "###########    #################"
   "############  ##################"])

(def goal \!)
(def clear \#)
(def blocked \space)
(def path \@)

(def closed
  [{:x 1 :y 2 :distance 40}])

(defn neighbors [[x y]]
  [[(inc x) y] [(dec x) y] [x (inc y)] [x (dec y)]])





(defn manh-dist [u v]
  (reduce +
          (map (fn [[a b]] (Math/abs (- a b)))
               (zipmap u v))))


(defn manh-path-px [[x1 y1] [x2 y2]]
  (set
    (concat
      (map (fn [x] [x y1]) (apply range (sort [x1 x2])))
      (map (fn [y] [x2 y]) (apply range (sort [y1 y2]))))))

(defn check-valid-state [[x y] terrain]
  (if (= (get (get map-32x32x4 y) x) clear)
    true
    false))

(def open-list
  [{:location [0 0]
    :score    43
    :path     [[1 0] [-1 0] [0 1] [0 -1]]}])

(def closed-list
  (hash-set (first open-list)))

(defn not-in-closed-list [[x y] closed-list]
  (not (contains? closed-list (hash [x y]))))

(defn within-bounds [[x y] terrain]
  (if (= (get-in terrain [x y]) nil)
    false
    true))

(defn valid-neighbor [[x y] closed-list terrain]
  (if (and (within-bounds [x y] terrain) (not-in-closed-list [x y] closed-list))
    true
    false))

; (filter #(valid-neighbor % closed-list map-32x32x4) (neighbors [0 0]))

(defn solve [open-list closed terrain]
  (let [curr (first open-list)]
    (if (= (get-in terrain [(:y curr) (:x curr)]) goal)
      (doall
        (println "yeah")
        (assoc curr :path (conj (:path curr) [(:x curr) (:y curr)])))
      (doall
        (println "nope")
        (solve (neighbors (curr :location)))))))







































