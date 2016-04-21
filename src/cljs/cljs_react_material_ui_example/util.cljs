(ns cljs-react-material-ui-example.util
  (:require [cognitect.transit :as t]
            [cljs.pprint :refer [pprint]]
            [goog.events.KeyCodes :as kc]))


(defn p [& args]
  "Like print, but returns last arg. For debugging purposes"
  (doseq [a args]
    (let [f (if (map? a) pprint print)]
      (f a)))
  (println)
  (flush)
  (last args))

(defn pcoll [items]
  (doall (map p items)))

(defn prevent-default [e]
  (doto e (.preventDefault) (.stopPropagation)))

(defn target-val [e]
  (.. e -target -value))

(defn on-key-down [key-fns]
  (fn [e]
    (let [f (condp == (aget e "keyCode")
              kc/ESC (:key/esc key-fns)
              kc/ENTER (:key/enter key-fns)
              #(do %))]
      (f e))))

(defn event-data [e]
  (aget (.-event_ e) "data"))

(defn apply-if [pred f x & args]
  (if-not (pred x)
    (apply f x args)
    x))