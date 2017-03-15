(ns clojure-club.x106-number-maze.mbastian
  (:import (clojure.lang PersistentQueue)))

;Produce only the number of steps.
(defn number-maze[s g]
  (letfn[(nbrs[x] (cond-> [(* 2 x) (+ x 2)] (even? x) (conj (/ x 2))))]
    (inc (count (take-while #(not (% g)) (iterate #(reduce into #{} (map nbrs %)) #{s}))))))

(number-maze 9 2)
(number-maze 2 9)

;Return a single path
(defn number-path [s g]
  (letfn [(nbrs [x] (cond-> [(+ 2 x) (* 2 x)] (even? x) (conj (/ x 2))))]
    (loop [v {} [[n :as edge] :as q] (conj PersistentQueue/EMPTY [s :done])]
      (cond
        (v g) (reverse (take-while (complement #{:done}) (iterate v g)))
        (v n) (recur v (pop q))
        :default (recur (conj v edge) (into (pop q) (map (fn [x] [x n]) (nbrs n))))))))

(number-path 7 43)
(number-path 9 2)
(number-path 2 9)

;Return them all
(defn number-paths[s g]
  (letfn[(nbrs[x] (cond-> [(* 2 x) (+ x 2)] (even? x) (conj (/ x 2))))
         (expand [paths] (for [path paths n (nbrs (peek path))
                               :when (not-any? #{n} path)] (conj path n)))]
    (->> (iterate expand [[s]])
         (some #(when (some (comp #{g} peek) %) %))
         (filter (comp #{g} peek)))))

;(time (number-paths 1 4137))
(time (number-paths 7 43))
(time (number-paths 9 2))
(time (number-paths 2 9))
(time (number-paths 2 4))

;Return them all
(defn number-paths1[s g]
  (letfn[(nbrs[x] (cond-> [(* 2 x) (+ x 2)] (even? x) (conj (/ x 2))))
         (expand [paths] (for [{:keys [path visited]} paths n (nbrs (peek path))
                               :when ((complement visited) n)]
                           {:path (conj path n) :visited (conj visited n)}))]
    (let [pred (comp #{g} peek :path)]
      (->> (iterate expand [{:path [s] :visited #{s}}])
           (some #(when (some pred %) %))
           (filter pred)
           (mapv :path)))))

;(time (number-paths1 1 4137))
(time (number-paths1 7 43))
(time (number-paths1 9 2))
(time (number-paths1 2 9))
(time (number-paths1 2 4))