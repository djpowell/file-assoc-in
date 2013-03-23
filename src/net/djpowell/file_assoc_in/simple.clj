(ns net.djpowell.file-assoc-in.simple
  "A simple implementation of file-assoc-in that doesn't
  preserve lexical features"
  (:use
   [net.djpowell.file-assoc-in.util]))
  
(defn emit-str
  [d]
  (with-out-str
    (binding [*print-meta* true]
      (prn d))))

(defn file-assoc-in
  [f ks v]
  (let [old-text (slurp f :encoding "utf-8")
        old-data (read-str old-text "old")
        new-data (assoc-in old-data ks v)
        new-text (emit-str new-data)]
    (safely-update-file f new-text ks v)))
