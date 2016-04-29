(ns cljs-react-material-ui-example.util
  (:require [clojure.pprint :refer [pprint]]))

(defn p [& args]
  "Like print, but returns last arg. For debugging purposes"
  (doseq [a args]
    (let [f (if (map? a) pprint print)]
      (f a)))
  (println)
  (flush)
  (last args))

(defn str->int [s]
  (if (number? s)
    s
    (Integer/parseInt (re-find #"\A-?\d+" s))))