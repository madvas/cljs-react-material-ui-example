(ns cljs-react-material-ui-example.website
  (:require [com.stuartsierra.component :as c]
            [bidi.bidi :as b]
            [clojure.java.io :as io]
            [yada.yada :refer [yada]]
            [yada.resources.classpath-resource :as yr]))

(defrecord Website []
  c/Lifecycle
  (start [component]
    component)
  (stop [component]
    component)

  b/RouteProvider
  (routes [component]
    ["/" [["" (yada (io/resource "public/html/index.html"))]
          ["" (yada (yr/new-classpath-resource "public/"))]]]))

(defn new-website []
  (-> (map->Website {})))