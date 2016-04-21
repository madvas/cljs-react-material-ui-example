(ns cljs-react-material-ui-example.parser
  (:require [om.next :as om]
            [cljs-react-material-ui-example.util :as u]))

;; =============================================================================
;; Reads

(defmulti read om/dispatch)

(defmethod read :default
  [{:keys [state]} k _]
  (println "Default read " k)
  {:remote false})

(defmulti mutate om/dispatch)

(defmethod mutate :default
  [_ _ _]
  {:remote true})


