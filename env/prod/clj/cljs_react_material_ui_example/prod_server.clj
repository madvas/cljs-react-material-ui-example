(ns cljs-react-material-ui-example.prod-server
  (:require [cljs-react-material-ui-example.core :as app])
  (:gen-class))

(def config
  {:web-port (or (System/getenv "PORT") 8082)})

(defn -main [& args]
  (println "Starting on port " (:web-port config))
  (app/dev-start config)
  (println (str "Started server on port " (:web-port config)))
  (.addShutdownHook (Runtime/getRuntime)
                    (Thread. #(do (app/stop)
                                  (println "Server stopped")))))