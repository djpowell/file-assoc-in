(defproject net.djpowell/file-assoc-in "0.1.1-SNAPSHOT"
  :description "Utility for updating clojure data files containing maps (such as leiningen profiles), without losing comments and lexical layout"
  :url "https://github.com/djpowell/file-assoc-in"
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
