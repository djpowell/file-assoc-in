(ns net.djpowell.file-assoc-in.core
  (:gen-class )
  (:require [clojure.java.io :as io])
  (:require [net.djpowell.file-assoc-in.xml :as xml]
            [net.djpowell.file-assoc-in.simple :as simple])
  (:use [net.djpowell.file-assoc-in.util]))

(defn -main
  "Permanently update a file containing a nested Clojure map, by setting
  a parameter as-with Clojure's assoc-in; but without
  damaging the lexical structure of comments and layout.

  Example:

  \"~/.lein/profiles.clj\" \"[:user :java-cmd]\" \"c:\\jdk\\bin\\java.exe\"
  "
  [& args]
  (when-not (= (count args) 3)
    (fail 1 "Params: <source-path> <assoc-in-vector> <value>"))
  (try
    (let [[src-path assoc-in-str value] args
          assoc-in-vector (read-str assoc-in-str "assoc-in-vector")]
      (xml/file-assoc-in (io/file src-path) assoc-in-vector value))
    (catch Exception e
      (fail 2 (str "Error: " (.getMessage e)) (ex-data e)))))

