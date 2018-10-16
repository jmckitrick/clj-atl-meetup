(ns clj-atl-meetup.core-test
    (:require
     [cljs.test :refer-macros [deftest is testing]]
     [clj-atl-meetup.core :refer [multiply fix-query]]
     [clojure.string :as string]))

(deftest multiply-test
  (is (= (* 1 2) (multiply 1 2))))

(deftest multiply-test-2
  (is (= (* 75 10) (multiply 10 75))))

(deftest fix-query-test
  (is (= "this+is+a+test" (fix-query "this is a test")))
  (is (not (= "this is a test" (fix-query "this is a test"))))
  (is (not (string/includes? (fix-query "this is a test") " "))))
