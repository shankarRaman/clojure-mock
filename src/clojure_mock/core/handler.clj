(ns clojure-mock.core.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [clojure.edn :as edn]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

(def responses (atom (clojure.edn/read-string (slurp "resources/public/mockdata.edn"))))

;;here we are recording the response
(defn fetch-real-data [request]
  "This is data coming from actual service call")


(defn save-responses []
  (spit "resources/public/mockdata.edn" @responses)
  )


;;here are playing back the mocked response
(defn mockendpoint[request]
  ;; grab for foo
  (println @responses)
  (println "\n")
  (println request)
  (let [k (select-keys request [:server-name :scheme :uri :server-port])]

   ;; (println k)
    (if (contains? @responses k)
      (get @responses k))
      (let [real-data (fetch-real-data request)]
        (swap! responses #(assoc % k real-data))
        (save-responses)
        real-data)))


(defn read-file [filename]
  (clojure.edn/read-string (slurp filename)))

(defroutes app-routes
  (GET "/" [] "Hello World")
  (route/not-found mockendpoint))

(def app
  (wrap-defaults app-routes site-defaults))

