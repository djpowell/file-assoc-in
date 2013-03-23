(defproject net.djpowell/file-assoc-in "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :main net.djpowell.file-assoc-in.core
  :dependencies [
                  [org.clojure/clojure "1.5.1"]
                  [enlive "1.1.1"]
                  [org.clojars.djpowell/sjacket "0.1.0.3"]
                  ]
  :profiles {:test {:resource-paths ["test-resources"]}}
  )
