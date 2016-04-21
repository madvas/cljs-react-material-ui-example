(ns cljs-react-material-ui-example.prod-server
  (:require [cljs-react-material-ui.core :as cljs-react-material-ui])
  (:gen-class))

(def config
  {:web-port (or (System/getenv "PORT") 8081)})

(defn -main [& args]
  (println "Starting on port " (:web-port config))
  (cljs-react-material-ui/dev-start config)
  (println (str "Started server on port " (:web-port config)))
  (.addShutdownHook (Runtime/getRuntime)
                    (Thread. #(do (cljs-react-material-ui/stop)
                                  (println "Server stopped")))))