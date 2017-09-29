(ns cljs-react-material-ui-example.dev-server
  (:require [cljs-react-material-ui-example.core :as cljs-react-material-ui-example]
            [cljs-react-material-ui-example.figwheel :as f]))


(defn -main []
  (def config
    {:web-port 8999})

  (cljs-react-material-ui-example/dev-start config)
  (println (str "Started server on port " (:web-port config)))
  (.addShutdownHook (Runtime/getRuntime)
                    (Thread. #(do (cljs-react-material-ui-example/stop)
                                  (println "Server stopped"))))
  (f/start-fig!))
