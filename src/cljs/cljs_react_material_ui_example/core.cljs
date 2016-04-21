(ns cljs-react-material-ui-example.core
  (:require [goog.dom :as gdom]
            [om.next :as om :refer-macros [defui]]
            [om.dom :as dom]
            [cljs-react-material-ui-example.parser :as p]
            [cljs-react-material-ui-example.util :as u]
            [clojure.string :as str]
            [goog.events :as e]
            [goog.events.EventType :as et]
            [cljs-react-material-ui.core :as mui]
    #_[cljs-react-material-ui-example.icons :as mic]
            )
  (:import [goog History]
           [goog.history EventType]))

(enable-console-print!)

(println "heren")

(def state (atom {}))

(def reconciler
  (om/reconciler
    {:state  state
     :parser (om/parser {:read p/read :mutate p/mutate})}))

(def my-raw-theme
  {:spacing     (aget js/MaterialUI "Styles" "Spacing")
   :z-index     (aget js/MaterialUI "Styles" "ZIndex")
   :font-family "Roboto, sans-serif"
   :palette     {:primary1-color (mui/color :blue-grey500)
                 :primary2-color (mui/color :cyan700)
                 :primary3-color (mui/color :light-black)
                 :accent1-color  (mui/color :amber600)
                 :accent2-color  (mui/color :grey100)
                 :accent3-color  (mui/color :grey500)}})

(def my-raw-theme2
  (assoc-in my-raw-theme [:palette :accent1-color] (mui/color :amber600)))

(defui Table
  Object
  (componentWillMount [this])
  (render [this]
    (mui/table
      (mui/table-header
        (mui/table-row
          (mui/table-header-column "ID")
          (mui/table-header-column "wtf")))
      (mui/table-body
        (mui/table-row
          (mui/table-row-column "Abc")
          (mui/table-row-column "oolla"))))))

(def table (om/factory Table {}))

(defn blue-raised-btn [theme]
  (assoc-in theme [:raised-button :primary-color] (mui/color :cyan700)))

(defui AppRoot
  Object
  (render [this]
    (mui/mui-root {:mui-theme (mui/get-mui-theme my-raw-theme)}
                  (mui/app-bar {:title              "Tittle"
                                ;:icon-element-left  (mui/icon-button (mic/social-notifications-none {}))
                                :icon-element-right (mui/flat-button {:label "Save"})})
                  (table)
                  (mui/raised-button {:label "Theme 1" :primary true})
                  (mui/with-theme blue-raised-btn
                                  (mui/raised-button {:label "Theme 2" :primary true}))

                  (mui/toolbar
                    (mui/toolbar-group
                      {:float "left"}
                      (mui/drop-down-menu
                        {:value 1}
                        (mui/menu-item
                          {:value        1
                           :primary-text "All Broad"})
                        (mui/menu-item
                          {:value        2
                           :primary-text "Lele"})))))))

(om/add-root! reconciler AppRoot (gdom/getElement "app"))

