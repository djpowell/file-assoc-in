(ns net.djpowell.file-assoc-in.util
  (:import
   (java.io File))
  (:require
   [clojure.java.io :as io]
   [clojure.walk :as walk]
   [clojure.pprint :as pp]
   ))

(defn println-err
  "println to stderr"
  [& args]
  (binding [*out* *err*]
    (apply println args)))

(defn fail
  "println to stderr, and exit with failure status"
  ([exit-code message data]
    (binding [*out* *err*]
      (println-err message)
      (pp/pprint data))
    (System/exit exit-code))
  ([exit-code message]
    (binding [*out* *err*]
      (println-err message))
    (System/exit exit-code)))


    (defn read-str
  [s nm]
  (try
    (binding [*read-eval* nil]
      (read-string s))
    (catch Exception e
      (throw (ex-info "Failed to read data" {:s s :name nm} e)))))

(defn intern-regexps
  "Identical regexps read from two files aren't interned, and aren't
  equal - fix that."
  [d intern-map]
  (let [intern-atom (atom intern-map)]
    [(walk/postwalk #(if (instance? (class #"") %)
                       (let [regexp-str (str %)]
                         (if-let [regexp (get @intern-atom regexp-str)]
                           regexp
                           (do
                             (swap! intern-atom assoc (str %) %)
                             %)))
                       %)
       d)
     @intern-atom]))

(defn validate-change
  [oldtext newtext ks v]
  (when-not (pos? (count newtext))
    (throw (ex-info "New version is empty" {:newtext newtext})))
  (when-not (seq ks)
    (throw (ex-info "Keys are empty" {:ks ks})))
  (let [old-data (read-str oldtext "old")
        [old-data ireg] (intern-regexps old-data {})
        new-data (read-str newtext "new")
        [new-data _] (intern-regexps new-data ireg)
        expected-data (assoc-in old-data ks v)]
    ; TODO walk the data to check meta-data too?
    (when-not (= new-data expected-data)
      (throw (ex-info "New data doesn't match expectations" {:new new-data :expected expected-data :old old-data :ks ks :v v})))))

(defn safely-update-file
  [^File file ^String contents ks v]
  (validate-change (slurp file :encoding "utf-8") contents ks v)
  (let [dir (.getParentFile file)
        base (.getName file)
        tempfile (File/createTempFile base ".tmp" dir)
        bakfile (File/createTempFile base ".bak" dir)]
    (.delete tempfile)
    (.delete bakfile)
    (.deleteOnExit tempfile)
    (try
      (try
        (spit tempfile contents :encoding "utf-8")
        (catch Exception e
          (throw (ex-info "Failed to write new file" {:file tempfile :contents contents} e))))
      (if-not (.renameTo file bakfile)
        (throw (ex-info "Failed to backup original file" {:from file :to bakfile}))
        (if-not (.renameTo tempfile file)
          (let [revert (.renameTo bakfile file)] ; revert rename of original
            (throw (ex-info "Failed to replace original file" {:from tempfile :to file :bakfile bakfile :revert revert})))))
      {:bakfile bakfile}
      (finally
        (when (.exists tempfile)
          (.delete tempfile))))))
