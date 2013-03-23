(ns net.djpowell.file-assoc-in.xml-test
  (:import (java.io File))
  (:require [clojure.java.io :as io])
  (:use clojure.test
        net.djpowell.file-assoc-in.xml))

(def test-paths (map
                  #(str % ".clj")
                  (range 1 9)))

(deftest test-1
  (doseq [test-path test-paths]
    (testing test-path
      (println "Testing:" test-path)
      (with-open [test-resource (io/input-stream (io/resource test-path))]
        (let [temp-path (doto (File/createTempFile (str test-path) ".clj") .deleteOnExit)]
          (io/copy test-resource temp-path)
          (try
            (file-assoc-in temp-path [:user :java-cmd] "c:\\jdk-test\\bin\\java.exe")
            (catch Exception e
              (.printStackTrace e)
              (throw e)))
          (let [new-content (slurp temp-path)]
            (println new-content)
            (println)))))))
