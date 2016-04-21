(ns cljs-react-material-ui-example.system
  (:require [com.stuartsierra.component :as c]
            cljs-react-material-ui-example.website
            [modular.maker :as mm]
            [modular.bidi :refer [new-router new-web-resources]]
            [modular.aleph :refer [new-webserver]]
            [cljs-react-material-ui-example.util :as u]))

(defn http-listener-components [system config]
  (assoc system :http-listener (new-webserver :port (u/s->int (:web-port config)))))

(defn modular-bidi-router-components [system config]
  (assoc system :bidi-request-handler (mm/make new-router config)))

(defn website-components [system config]
  (assoc system :website
                (-> (mm/make cljs-react-material-ui-example.website/new-website config)
                    (c/using []))))

(defn new-dependency-map []
  {:http-listener        {:request-handler :bidi-request-handler}
   :bidi-request-handler {:website :website}})

(defn new-system-map
  [config]
  (apply c/system-map
         (apply concat
                (-> {}
                    (http-listener-components config)
                    (modular-bidi-router-components config)
                    (website-components config)))))

(defn dev-system
  ([] (dev-system {}))
  ([config]
   (-> (new-system-map config)
       (c/system-using (new-dependency-map)))))
