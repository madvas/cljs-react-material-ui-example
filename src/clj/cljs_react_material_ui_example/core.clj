(ns cljs-react-material-ui-example.core
  (:require [com.stuartsierra.component :as component]
            [cljs-react-material-ui-example.system :as system]))

(def servlet-system (atom nil))

;; =============================================================================
;; Development

(defn dev-start [config]
  (let [sys  (system/dev-system config)
        sys' (component/start sys)]
    (reset! servlet-system sys')
    (println "System started")
    sys'))

(defn stop []
  (swap! servlet-system component/stop)
  (println "System stopped"))

(defn dev-restart [config]
  (stop)
  (dev-start config))



