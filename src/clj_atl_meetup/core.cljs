(ns ^:figwheel-hooks clj-atl-meetup.core
  (:require
   [ajax.core :refer [GET POST]]
   [clojure.string :as string]
   [goog.dom :as gdom]
   [reagent.core :as reagent :refer [atom]]))


(defn multiply [a b] (* a b))


(defonce app-state (atom {:text "Hello world!"}))


(defn get-app-element []
  (gdom/getElement "app"))


(defonce app-state-demo (atom {:state ""}))


(defn fix-query [s]
  (string/replace s #" " "+"))


(defn get-data
  ([]
   (GET "http://localhost:3000/demo/distance?start=atlanta&end=nyc"
       {:handler (fn [response]
                   (js/console.log "Here!!")
                   (swap! app-state-demo assoc :time response))}))
  ([starting-address]
   (let [ending-address "101 W. CHAPEL HILL STREET SUITE 300 DURHAM, NC 27701"
         start (string/replace starting-address #" " "+")
         end (string/replace ending-address #" " "+")]
     (GET (str "http://localhost:3000/demo/distance?start=" start
               "&end=" end)
         {:handler (fn [response]
                     (js/console.log "Here!!")
                     (swap! app-state-demo assoc :time response))})))
  ([starting-address ending-address]
   (let [start (string/replace starting-address #" " "+")
         end (string/replace ending-address #" " "+")]
     (GET (str "http://localhost:3000/demo/distance?start=" start
               "&end=" end)
         {:handler (fn [response]
                     (js/console.log "Here!!")
                     (swap! app-state-demo assoc :time response))}))))


(defn get-distance-result []
  (js/console.log "Clicked!")
  (swap! app-state-demo assoc :time "---" )
  (let [street-field (gdom/getElement "street")
        city-field (gdom/getElement "city")
        state-field (gdom/getElement "state")
        zip-field (gdom/getElement "zip")
        street (.-value street-field)
        city (.-value city-field)
        state (.-value state-field)
        zip (.-value zip-field)]
    (get-data (string/join ", " [street city state zip]))))


(defn form-panel []
  [:div.card
   [:div.card-body
    [:div.card-title "Starting address"]
    [:div
     [:div
      [:label "\u00A0 Street"]]
     [:div
      [:input
       {:id "street"
        :type "text"
        :value (@app-state-demo :address)
        :on-change #(swap! app-state-demo assoc :address (-> % .-target .-value))}]]
     [:div
      [:label "\u00A0 City"]]
     [:div
      [:input
       {:id "city"
        :type "text"
        :value (@app-state-demo :city)
        :on-change #(swap! app-state-demo assoc :city (-> % .-target .-value))}]]
     [:div
      [:label "\u00A0 State"]]
     [:div
      [:select.custom-select
       {:id "state"
        :type "select"
        :value (@app-state-demo :state)
        :on-change #(swap! app-state-demo assoc :state (-> % .-target .-value))}
       [:option "Select a State"]
       [:option {:value "GA"} "GA"]
       [:option {:value "FL"} "FL"]
       [:option {:value "SC"} "SC"]
       [:option {:value "NC"} "NC"]
       [:option {:value "NY"} "NY"]]]
     [:div
      [:label "\u00A0 Zip"]]
     [:div
      [:input
       {:id "zip"
        :type "text"
        :value (@app-state-demo :zip)
        :on-change #(swap! app-state-demo assoc :zip (-> % .-target .-value))}]]
     [:div
      [:button.btn.btn-primary
       {:type "button"
        :on-click get-distance-result}
       "Calculate"]]]]])



(defn result-panel []
  [:div
   [:h3 (@app-state :text)]
   #_[:div
    [form-panel]
    [:div.card
     [:div.card-body
      [:div.card-title "Distance calculation"]
      [:div.card-text
       #_[:p (:text @app-state)]
       [:div
        [:h1 (:time @app-state-demo)]]
       #_[:h3 "Edit this in src/clj_atl_meetup/core.cljs and watch it change!"]]]]]])


(defn mount [el]
  (reagent/render-component [result-panel] el))


(defn mount-app-element []
  (when-let [el (get-app-element)]
    (mount el)))


;;(mount-app-element)

;; specify reload hook with ^;after-load metadata
(defn ^:after-load on-reload []
  (mount-app-element))
