(ns cljs-react-material-ui-example.core
  (:require [cljsjs.material-ui]
            [cljs-react-material-ui.core :as ui]
            [cljs-react-material-ui.icons :as ic]
            [goog.dom :as gdom]
            [om.next :as om :refer-macros [defui]]
            [cljs-react-material-ui-example.parser :as p]
            [cljs-react-material-ui-example.util :as u]
            [om.dom :as dom]
            [cljs-time.format :as tf]
            [cljs-time.coerce :refer [from-date]]
            [cljs-react-material-ui-example.state :refer [init-state]]
            [schema.core :as s :include-macros true]
            [print.foo :as pf :include-macros true]))

(enable-console-print!)

(defn get-step-content [step-index]
  (case step-index
    0 "Select campaign settings..."
    1 "What is an ad group anyways?"
    2 "This is the bit I really care about!"
    "You're a long way from home sonny jim!"))

(defui MyStepper
  Object
  (componentWillMount [this]
    (om/set-state! this {:finished? false :step-index 0}))
  (render [this]
    (let [{:keys [finished? step-index]} (om/get-state this)]
      (dom/div
        #js {:className "row center-xs mar-top-20"}
        (ui/paper
          {:class-name "col-xs-12 col-md-8 col-lg-6 pad-10"}
          (ui/stepper
            {:active-step step-index}
            (ui/step
              (ui/step-label "Select campaign settings"))
            (ui/step
              (ui/step-label "Create an ad group"))
            (ui/step
              (ui/step-label "Create an ad")))
          (if finished?
            (dom/div
              nil
              (ui/floating-action-button
                {:secondary    true
                 :on-touch-tap #(om/set-state! this {:finished? false :step-index 0})}
                (ic/content-clear)))
            (dom/div
              nil
              (dom/p #js {:className "mar-bot-20"} (get-step-content step-index))
              (dom/div
                nil
                (ui/flat-button
                  {:label        "Back"
                   :disabled     (= step-index 0)
                   :on-touch-tap #(om/set-state! this {:step-index (- step-index 1)})})
                (ui/raised-button
                  {:label        (if (= step-index 2) "Finish" "Next")
                   :primary      true
                   :on-touch-tap #(om/set-state! this {:step-index (+ step-index 1)
                                                       :finished?  (>= step-index 2)})})))))))))

(def my-stepper (om/factory MyStepper {}))

(defui Person
  static om/Ident
  (ident [this {:keys [db/id]}]
    [:person/by-id id])

  static om/IQuery
  (query [this]
    [:db/id :person/name :person/date {:person/status [:status/name]} {:person/happiness [:db/id :happiness/name]}])

  Object
  (render [this]
    (let [{:keys [person/name person/date person/status person/happiness]} (om/props this)]
      (ui/table-row
        (ui/table-row-column name)
        (ui/table-row-column (tf/unparse (:date tf/formatters) (from-date date)))
        (ui/table-row-column (:status/name status))
        (ui/table-row-column (:happiness/name happiness))))))

(def person (om/factory Person {}))

(defn my-table [people]
  (ui/table
    {:height "250px"}
    (ui/table-header
      {:display-select-all  false
       :adjust-for-checkbox false}
      (ui/table-row
        (ui/table-header-column "Name")
        (ui/table-header-column "Date")
        (ui/table-header-column "Status")
        (ui/table-header-column "Happiness")))
    (ui/table-body
      (map person people))))


(defn radio-btn-group [c happiness-list val]
  (let [[sad normal superb] happiness-list]
    (ui/radio-button-group
      {:person/name    "happiness"
       :value-selected (str val)
       :on-change      #(om/transact! c `[(person-new/change {:value ~(js/parseInt %2)
                                                              :path  [:person/happiness 1]})
                                          :person/new])
       :class-name     "row between-xs mar-ver-15"}
      (ui/radio-button
        {:value          (str (:db/id sad))
         :label          (:happiness/name sad)
         :class-name     "col-xs-4"
         :checked-icon   (ic/social-sentiment-dissatisfied)
         :unchecked-icon (ic/social-sentiment-dissatisfied)})
      (ui/radio-button
        {:value      (str (:db/id normal))
         :label      (:happiness/name normal)
         :class-name "col-xs-4"})
      (ui/radio-button
        {:value          (str (:db/id superb))
         :label          (:happiness/name superb)
         :class-name     "col-xs-4"
         :checked-icon   (ic/action-favorite)
         :unchecked-icon (ic/action-favorite-border)}))))

(s/defschema ValidPerson
  {:person/name      s/Str
   :person/date      s/Inst
   :person/status    [(s/one s/Keyword "status/by-id") s/Int]
   :person/happiness [(s/one s/Keyword "happiness/by-id") s/Int]})

(defn handle-chip-delete [this idx]
  (om/update-state! this (fn [state]
                           (update state :chip-data (partial remove #(= (key %) idx))))))

(defui MyChips
  Object
  (componentWillMount [this]
    (om/set-state! this {:chip-data {0 "Clojure"
                                     1 "Clojurescript"
                                     2 "Om.Next"
                                     3 "MaterialUI"}}))
  (render [this]
    (let [chip-data (:chip-data (om/get-state this))]
      (ui/paper
        {:class-name "col-xs-12 col-md-8 col-md-offset-2 col-lg-6 col-lg-offset-3 pad-10 mar-top-20 row"}
        (for [chip (into [] chip-data)]
          (ui/chip {:key   (key chip)
                    :style {:margin-right 5}
                    :on-request-delete #(handle-chip-delete this (key chip))}
                   (val chip)))))))

(def my-chips (om/factory MyChips))

(defui AppRoot
  static om/IQuery
  (query [this]
    [{:person/list (om/get-query Person)}
     {:status/list [:db/id :status/name]}
     {:happiness/list [:db/id :happiness/name]}
     {:person/new (om/get-query Person)}])
  Object
  (render [this]
    (let [props (om/props this)
          state (om/get-state this)
          person-list (:person/list props)
          status-list (:status/list props)
          happiness-list (:happiness/list props)
          person-new (:person/new props)
          close-help #(om/update-state! this assoc :open-help? false)
          {:keys [drawer-open?]} (om/get-state this)]
      (ui/mui-theme-provider
        {:mui-theme (ui/get-mui-theme)}
        (dom/div
          #js {:className "h-100"}
          (ui/app-bar
            {:title "Material UI Om.Next App"
             :icon-element-right
                    (ui/flat-button
                      {:label     "Github"
                       :href      "https://github.com/madvas/cljs-react-material-ui-example"
                       :secondary true
                       :target    :_blank})
             :on-left-icon-button-touch-tap
                    #(om/set-state! this {:drawer-open? true})})
          (ui/drawer
            {:docked            false
             :open              drawer-open?
             :on-request-change #(om/set-state! this {:drawer-open? %})}
            (ui/menu-item {:on-touch-tap #(println "Menu Item Clicked")} "Menu Item")
            (ui/menu-item "Menu Item 2"))

          (dom/div
            #js {:className "row around-xs mar-top-20"}
            (ui/paper
              {:class-name "col-xs-11 col-md-6 col-lg-4"}
              (ui/text-field
                {:floating-label-text "Name"
                 :class-name          "w-100"
                 :value               (:person/name person-new)
                 :on-change           #(om/transact! this `[(person-new/change {:value ~(u/target-val %)
                                                                                :path  [:person/name]})
                                                            :person/new])})
              (ui/date-picker
                {:hint-text  "Select Date"
                 :mode       :landscape
                 :class-name "w-100"
                 :value      (:person/date person-new)
                 :on-change  #(om/transact! this `[(person-new/change {:value ~%2
                                                                       :path  [:person/date]})
                                                   :person/new])})
              (ui/auto-complete
                {:data-source    (map :status/name status-list)
                 :hint-text      "Type status"
                 :full-width     true
                 :open-on-focus  true
                 :search-text    (or (:status/name
                                       (u/find-by-key :db/id (second (:person/status person-new)) status-list))
                                     "")

                 :filter         (aget js/MaterialUI "AutoComplete" "caseInsensitiveFilter")
                 :on-new-request (fn [chosen]
                                   (let [status-id (:db/id (u/find-by-key :status/name chosen status-list))]
                                     (om/transact! this `[(person-new/change {:value [:status/by-id ~status-id]
                                                                              :path  [:person/status]})
                                                          :person/new])))})

              (radio-btn-group this happiness-list (get-in person-new [:person/happiness 1]))
              (dom/div
                #js {:className "row pad-10 reverse"}
                (ui/raised-button
                  {:label          "Add"
                   :primary        true
                   :label-position :before
                   :icon           (ic/content-add-circle)
                   :disabled       (boolean (s/check ValidPerson person-new))
                   :on-touch-tap   #(om/transact! this `[(person-new/add)
                                                         :person/new :person/list])})
                (ui/raised-button
                  {:label          "Help"
                   :class-name     "mar-rig-10"
                   :secondary      true
                   :label-position :before
                   :icon           (ic/action-help)
                   :on-touch-tap   #(om/update-state! this assoc :open-help? true)})))

            (ui/paper
              {:class-name "col-xs-11 col-md-11 col-lg-7"}
              (ui/mui-theme-provider
                {:mui-theme (ui/get-mui-theme
                              {:table-header-column
                               {:text-color (ui/color :deep-orange500)}})}
                (my-table person-list))))

          (ui/mui-theme-provider
            {:mui-theme (ui/get-mui-theme
                          {:palette {:primary1-color (ui/color :amber600)
                                     :shadow-color   (ui/color :deep-orange900)
                                     :text-color     (ui/color :indigo900)}
                           :stepper {:inactive-icon-color  (ui/color :deep-orange900)
                                     :connector-line-color (ui/color :light-blue600)
                                     :text-color           (ui/color :teal900)
                                     :disabled-text-color  (ui/color :teal200)}})}
            (my-stepper))
          (my-chips)
          (ui/dialog
            {:title            "Help"
             :key              "dialog"
             :modal            false
             :open             (boolean (:open-help? state))
             :actions          [(ui/raised-button
                                  {:label        "Back"
                                   :key          "back"
                                   :on-touch-tap close-help})
                                (ui/raised-button
                                  {:label        "Thanks"
                                   :key          "thanks"
                                   :on-touch-tap close-help})]
             :on-request-close close-help})
          )))))

(def reconciler
  (om/reconciler
    {:state     (atom init-state)
     :normalize true
     :parser    (om/parser {:read p/read :mutate p/mutate})}))

(om/add-root! reconciler AppRoot (gdom/getElement "app"))

