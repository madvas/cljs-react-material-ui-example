(ns cljs-react-material-ui-example.figwheel
  (:require [figwheel-sidecar.repl-api :as ra]
            [cljs.build.api :as b]))


(def opts
  {:main           'cljs-react-material-ui-example.core
   :asset-path     "/js"
   :output-to      "resources/public/js/app.js"
   :output-dir     "resources/public/js"
   :parallel-build true
   :compiler-stats true
   :verbose        true})

(comment
  (b/build "src/cljs" opts))

(defn build []
  (b/build "src/cljs" opts))


(defn start-fig! []
  (ra/start-figwheel!
    {:figwheel-options {:server-port 5309}
     :build-ids        ["dev"]
     :all-builds       [{:id           "dev"
                         :figwheel     true
                         :source-paths ["src/cljs"]
                         :compiler     opts}]}))

(ra/cljs-repl)
