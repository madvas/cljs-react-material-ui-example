(ns cljs-react-material-ui.core
  (:refer-clojure :exclude [list])
  (:require-macros [cljs-react-material-ui.elements :as me])
  (:require [clojure.string :as str]
            [clojure.walk :as walk]
            [cljsjs.material-ui]))

(defn ^:private kebab->camel [kw]
  (keyword (str/replace (name kw) #"-(\w)" (comp str/upper-case second))))

(defn ^:private camel->kebab [s]
  (cond-> (str/join "-" (map str/lower-case (re-seq #"\w[a-z]*" (name s))))
          (keyword? s) keyword))

(defn ^:private map-entry? [x]
  (and (vector? x) (= (count x) 2)))

(defn ^:private walk-map-keys [f props]
  (walk/prewalk (fn [x]
                  (if (map-entry? x)
                    [(f (key x)) (val x)]
                    x)) props))

(def props-kebab->camel->js (comp clj->js (partial walk-map-keys kebab->camel)))

(def props-camel->kebab (partial walk-map-keys camel->kebab))

(defn ^:private create-mui-cmp [root-obj type args]
  (let [first-arg (first args)
        args (if (or (map? first-arg) (nil? first-arg)) args (cons {} args))]
    (apply js/React.createElement (aget root-obj type)
           (props-kebab->camel->js (first args)) (rest args))))

(def ThemeManager js/MaterialUI.Styles.ThemeManager)

(defn ^:private get-mui-theme*
  ([] (get-mui-theme* nil))
  ([raw-theme] (.getMuiTheme ThemeManager raw-theme)))

(defn get-mui-theme
  ([] (get-mui-theme))
  ([raw-theme] (-> raw-theme
                   props-kebab->camel->js
                   get-mui-theme*
                   props-camel->kebab)))

(defn color [color-key]
  (aget js/MaterialUI "Styles" "Colors" (name (kebab->camel color-key))))

(defn ^:private mui-root* [{:keys [mui-theme] :as opts} & children]
  (println (keys opts))
  (let [ctor (js/React.createFactory
               (js/React.createClass
                 #js {:getDisplayName    (fn [] "mui-root")
                      :childContextTypes #js {:muiTheme js/React.PropTypes.object}
                      :getChildContext   (fn [] #js {:muiTheme mui-theme})
                      :render            (fn []
                                           (apply js/React.DOM.div nil children))}))]
    (ctor nil)))

(defn mui-root [opts & children]
  (cond
    (map? opts) (apply mui-root* (update opts :mui-theme props-kebab->camel->js) children)
    :else (apply mui-root* {:mui-theme (get-mui-theme*)} children)))


(defn with-theme [f & children]
  (let [ctor (js/React.createFactory
               (js/React.createClass
                 #js {:getDisplayName     (fn [] "mui-with-theme")
                      :contextTypes       #js {:muiTheme js/React.PropTypes.object}
                      :getInitialState    (fn []
                                            (this-as this
                                              #js {:muiTheme (aget this "context" "muiTheme")}))
                      :childContextTypes  #js {:muiTheme js/React.PropTypes.object}
                      :getChildContext    (fn []
                                            (this-as this
                                              #js {:muiTheme (aget this "state" "muiTheme")}))
                      :componentWillMount (fn []
                                            (this-as this
                                              (let [muiTheme ((comp props-kebab->camel->js
                                                                    f
                                                                    props-camel->kebab
                                                                    walk/keywordize-keys
                                                                    js->clj)
                                                               (aget this "state" "muiTheme"))]
                                                (.setState this #js {:muiTheme muiTheme}))))
                      :render             (fn []
                                            (apply js/React.DOM.div nil children))}))]
    (ctor nil)))

(def ^:private create-mui-el (partial create-mui-cmp js/MaterialUI))


(defn app-bar [& args] (create-mui-el "AppBar" args))
(defn app-canvas [& args] (create-mui-el "AppCanvas" args))
(defn auto-complete [& args] (create-mui-el "AutoComplete" args))
(defn avatar [& args] (create-mui-el "Avatar" args))
(defn badge [& args] (create-mui-el "Badge" args))
(defn before-after-wrapper [& args] (create-mui-el "BeforeAfterWrapper" args))
(defn card [& args] (create-mui-el "Card" args))
(defn card-actions [& args] (create-mui-el "CardActions" args))
(defn card-expandable [& args] (create-mui-el "CardExpandable" args))
(defn card-header [& args] (create-mui-el "CardHeader" args))
(defn card-media [& args] (create-mui-el "CardMedia" args))
(defn card-text [& args] (create-mui-el "CardText" args))
(defn card-title [& args] (create-mui-el "CardTitle" args))
(defn checkbox [& args] (create-mui-el "Checkbox" args))
(defn circular-progress [& args] (create-mui-el "CircularProgress" args))
(defn clear-fix [& args] (create-mui-el "ClearFix" args))
(defn date-picker [& args] (create-mui-el "DatePicker" args))
(defn date-picker-dialog [& args] (create-mui-el "DatePickerDialog" args))
(defn dialog [& args] (create-mui-el "Dialog" args))
(defn divider [& args] (create-mui-el "Divider" args))
(defn drop-down-icon [& args] (create-mui-el "DropDownIcon" args))
(defn drop-down-menu [& args] (create-mui-el "DropDownMenu" args))
(defn enhanced-button [& args] (create-mui-el "EnhancedButton" args))
(defn flat-button [& args] (create-mui-el "FlatButton" args))
(defn floating-action-button [& args] (create-mui-el "FloatingActionButton" args))
(defn font-icon [& args] (create-mui-el "FontIcon" args))
(defn grid-list [& args] (create-mui-el "GridList" args))
(defn grid-tile [& args] (create-mui-el "GridTile" args))
(defn icon-button [& args] (create-mui-el "IconButton" args))
(defn icon-menu [& args] (create-mui-el "IconMenu" args))
(defn left-nav [& args] (create-mui-el "LeftNav" args))
(defn linear-progress [& args] (create-mui-el "LinearProgress" args))
(defn list [& args] (create-mui-el "List" args))
(defn list-divider [& args] (create-mui-el "ListDivider" args))
(defn list-item [& args] (create-mui-el "ListItem" args))
(defn menu [& args] (create-mui-el "Menu" args))
(defn menu-item [& args] (create-mui-el "MenuItem" args))
(defn overlay [& args] (create-mui-el "Overlay" args))
(defn paper [& args] (create-mui-el "Paper" args))
(defn popover [& args] (create-mui-el "Popover" args))
(defn radio-button [& args] (create-mui-el "RadioButton" args))
(defn radio-button-group [& args] (create-mui-el "RadioButtonGroup" args))
(defn raised-button [& args] (create-mui-el "RaisedButton" args))
(defn refresh-indicator [& args] (create-mui-el "RefreshIndicator" args))
(defn ripples [& args] (create-mui-el "Ripples" args))
(defn select-field [& args] (create-mui-el "SelectField" args))
(defn selectable-container-enhance [& args] (create-mui-el "SelectableContainerEnhance" args))
(defn slider [& args] (create-mui-el "Slider" args))
(defn svg-icon [& args] (create-mui-el "SvgIcon" args))
(defn styles [& args] (create-mui-el "Styles" args))
(defn snackbar [& args] (create-mui-el "Snackbar" args))
(defn tab [& args] (create-mui-el "Tab" args))
(defn tabs [& args] (create-mui-el "Tabs" args))
(defn table [& args] (create-mui-el "Table" args))
(defn table-body [& args] (create-mui-el "TableBody" args))
(defn table-footer [& args] (create-mui-el "TableFooter" args))
(defn table-header [& args] (create-mui-el "TableHeader" args))
(defn table-header-column [& args] (create-mui-el "TableHeaderColumn" args))
(defn table-row [& args] (create-mui-el "TableRow" args))
(defn table-row-column [& args] (create-mui-el "TableRowColumn" args))
(defn toggle [& args] (create-mui-el "Toggle" args))
(defn theme-wrapper [& args] (create-mui-el "ThemeWrapper" args))
(defn time-picker [& args] (create-mui-el "TimePicker" args))
(defn text-field [& args] (create-mui-el "TextField" args))
(defn toolbar [& args] (create-mui-el "Toolbar" args))
(defn toolbar-group [& args] (create-mui-el "ToolbarGroup" args))
(defn toolbar-separator [& args] (create-mui-el "ToolbarSeparator" args))
(defn toolbar-title [& args] (create-mui-el "ToolbarTitle" args))
(defn tooltip [& args] (create-mui-el "Tooltip" args))



