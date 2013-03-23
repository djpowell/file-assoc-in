(ns net.djpowell.file-assoc-in.xml
  "An implementation of file-assoc-in that preserves lexical features
  such as comments and formatting"
  (:import (java.io StringReader StringWriter)
           (javax.xml.transform Transformer TransformerFactory)
           (javax.xml.transform.stream StreamSource StreamResult))
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [net.cgrand.sjacket :as sj]
            [net.cgrand.sjacket.parser :as jp]
            [net.cgrand.enlive-html :as html])
  (:use [net.djpowell.file-assoc-in.util]))

;; yes... this is a fairly ridiculous method of transforming clojure,
;; but doing sibling selection with clojure tools was difficult, and it works

(defn clojure-as-xml
  [clj-str]
  (apply str (html/emit* (jp/parser clj-str))))

(defn xml-as-clojure-str
  [s]
  (sj/str-pt (first (html/xml-resource (java.io.StringReader. s)))))

(defn transform-string
  [xsl-path xml-s & params]
  (let [factory (TransformerFactory/newInstance)
        xsl (StreamSource. (io/input-stream (io/resource xsl-path)))
        transformer (.newTransformer factory xsl)
        param-kvs (apply hash-map params)
        _ (doseq [[k v] param-kvs]
            (.setParameter transformer k v))
        xml (StreamSource. (StringReader. xml-s))
        str-out (StringWriter.)
        out (StreamResult. str-out)]
    (.transform transformer xml out)
    (.toString str-out)))

(defn has-crs
  "Returns true if the input file contained carriage-returns"
  [in-str]
  (some #(= \return %) in-str))

(defn strip-crs
  "Strip carriage-returns before parsing"
  [in-str]
  (str/replace in-str "\r" ""))

(defn add-back-crs
  "Add back carriage-returns that were stripped before parsing"
  [in-str]
  (str/replace in-str "\r" ""))

(defn file-assoc-in
  [f ks v]
  (let [clj-in (slurp f :encoding "utf-8")
        use-crs (has-crs clj-in)
        clj-in (strip-crs clj-in)
        xml-in (clojure-as-xml clj-in)
        xml-out (transform-string "update-profiles.xsl" xml-in "value" (pr-str v))
        clj-out (xml-as-clojure-str xml-out)
        clj-out (if use-crs (add-back-crs clj-out) clj-out)]
    (safely-update-file f clj-out ks v)))
