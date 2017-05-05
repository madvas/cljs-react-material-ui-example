(defproject cljs-react-material-ui-example "0.1.0-SNAPSHOT"
  :description "Example app for library cljs-react-material-ui"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.8.40"]
                 [org.omcljs/om "1.0.0-alpha37" :exclusions [cljsjs/react cljsjs/react-dom]]
                 [com.stuartsierra/component "0.3.1"]
                 [com.cemerick/piggieback "0.2.1"]
                 [figwheel-sidecar "0.5.0-2" :scope "test"]
                 [com.cemerick/piggieback "0.2.1"]
                 [org.clojure/tools.nrepl "0.2.10"]
                 [aleph "0.4.1-beta5"]
                 [bidi "2.0.4"]
                 [yada "1.1.5"]
                 [juxt.modular/bidi "0.9.5"]
                 [juxt.modular/maker "0.5.0"]
                 [juxt.modular/wire-up "0.5.0"]
                 [juxt.modular/aleph "0.1.4"]
                 [com.andrewmcveigh/cljs-time "0.4.0"]
                 [cljs-react-material-ui "0.2.43"]
                 [cljsjs/material-ui-chip-input "0.13.5-0"]
                 [prismatic/schema "1.1.1"]
                 [print-foo-cljs "2.0.0"]]

  :plugins [[lein-cljsbuild "1.1.3"]]
  :min-lein-version "2.0.0"
  :uberjar-name "cljs-react-material-ui-example.jar"
  :clean-targets ^{:protect false} ["resources/public/js"]
  :source-paths ["src/clj" "src/cljs"]

  :cljsbuild {:builds {:app {:source-paths ["src/cljs"]
                             :figwheel     true
                             :compiler     {:main            cljs-react-material-ui-example.core
                                            :output-to       "resources/public/js/app.js"
                                            :output-dir      "resources/public/js"
                                            :asset-path      "/js"
                                            :optimizations   :none
                                            :pretty-print    true
                                            :externs         ["src/js/externs.js"]
                                            :closure-defines {goog.DEBUG false}
                                            :parallel-build  true
                                            :verbose         true}}}}

  :profiles {:dev     {:source-paths ["env/dev/clj"]
                       :main         cljs-react-material-ui-example.dev-server
                       :repl-options {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}
                       :plugins      [[lein-figwheel "0.5.1"]]}
             :uberjar {:source-paths ["env/prod/clj"]
                       :main         cljs-react-material-ui-example.prod-server
                       :hooks        [leiningen.cljsbuild]
                       :aot          :all
                       :omit-source  true
                       :cljsbuild    {:builds {:app
                                               {:compiler {:optimizations   :advanced
                                                           :closure-defines {:goog.DEBUG false}
                                                           :pretty-print    false}}}}}})