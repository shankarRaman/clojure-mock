(ns clojure-mock.core.handler-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [clojure-mock.core.handler :refer :all]
            [midje.sweet :refer :all]))

(deftest test-app
  (testing "main route"
    (let [response (app (mock/request :get "/"))]
      (is (= (:status response) 200))
      (is (= (:body response) "Hello World"))))
  
  (testing "not-found route"
    (let [response (app (mock/request :get "/invalid"))]
      (is (= (:status response) 404)))))

(fact "response from file is returned"
  (mockendpoint {:server-port 3000, :uri "/mock3/", :scheme :http, :server-name "localhost"})
    => "This is data coming from actual service call")

(fact "response from file is not returned"
  (mockendpoint {:server-port 3000, :uri "/csomethingnotincache/", :scheme :http, :server-name "localhost"})
    => nil)

;;{{}