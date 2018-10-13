(ns ^:figwheel-hooks clj-atl-meetup.core
  (:require
   [ajax.core :refer [GET POST]]
   [goog.dom :as gdom]
   [reagent.core :as reagent :refer [atom]]))

(println "This text is printed from src/clj_atl_meetup/core.cljs. Go ahead and edit it and see reloading in action.")

(defn multiply [a b] (* a b))


;; define your app data so that it doesn't get over-written on reload
(defonce app-state (atom {:text "Hello world!"}))
(defonce app-state-demo (atom {}))

(defn get-app-element []
  (gdom/getElement "app"))

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
        :type "text"}]]
     [:div
       [:label "\u00A0 State"]]
     [:div
      [:select.custom-select
       {:id "state"
        :type "select"}
       [:option "Select a State"]
       [:option {:value "GA"} "GA"]
       [:option {:value "FL"} "FL"]
       [:option {:value "SC"} "SC"]
       [:option {:value "NC"} "NC"]]]]]])

(defn result-panel []
  [:div
   [form-panel]
   [:div.card
    [:div.card-body
     [:div.card-title "Distance calculation"]
     [:div.card-text
      #_[:p (:text @app-state)]
      [:p
       [:h1 (:time @app-state-demo)]]
      #_[:h3 "Edit this in src/clj_atl_meetup/core.cljs and watch it change!"]]]]])

(defn mount [el]
  (reagent/render-component [result-panel] el))

(defn mount-app-element []
  (when-let [el (get-app-element)]
    (mount el)))

;; conditionally start your application based on the presence of an "app" element
;; this is particularly helpful for testing this ns without launching the app
(mount-app-element)

(defn get-data []
  (GET "http://localhost:3000/demo/distance?start=atlanta&end=nyc"
      {:handler (fn [response] (js/console.log "Here!!") (reset! app-state-demo {:time response}))
       ;:headers (get-ajax-headers)
       ;:error-handler error-handler
       ;;:response-format :json
       ;;:keywords? true
       }))


;; specify reload hook with ^;after-load metadata
(defn ^:after-load on-reload []
  (mount-app-element)
  (get-data)
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
