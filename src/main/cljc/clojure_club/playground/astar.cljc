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
    :path     []}])

(def closed-list
  (hash-set (:location (first open-list))))

(defn within-bounds [[x y] terrain]
  (if (= (get-in terrain [x y]) nil)
    false
    true))

(defn neighbors [[x y]]
  [{:location [(inc x) y]} {:location [(dec x) y]} {:location [x (inc y)]} {:location [x (dec y)]}])

(defn valid-neighbor-filter [[x y] closed-list terrain]
  (and (not (= (get-in terrain [x y]) nil)) (not (contains? closed-list [x y]))))

; (filter #(valid-neighbor % closed-list map-32x32x4) (neighbors [0 0]))

(defn generate-neighbor-maps [curr closed-list terrain]
  (map
    #(assoc
       %
       :path
       (conj
         (:path curr) (:location curr))
       :score 0)
    (filter
      #(valid-neighbor-filter (:location %) closed-list terrain)
      (neighbors (:location curr)))))


(defn sorted-insert-on-score [coll n]
  (let [[l r] (split-with #(> (get % :score) (get n :score)) coll)]
    (concat l [n] r)))

(defn solve [open-list closed terrain]
  (let [curr (first open-list)
        open-list (rest open-list)]
    (if (= (get-in terrain (:location curr)) goal)
      (doall
        ; return current as the path
        (println "yeah")
        curr)
      (doall
        ; add to closed list
        ; create neighbors with path = (curr :path) + (curr :location)
        (println "nope")
        (reduce
          (fn [new-open-list neighbor] (sorted-insert-on-score new-open-list neighbor))
          open-list
          (generate-neighbor-maps curr closed-list terrain))))))



































