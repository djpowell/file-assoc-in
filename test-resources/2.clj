{:custom1 {
            :some "property"}
 :user {
         :plugins [
                    [lein-cljsbuild "0.3.0"]
                    ]
         :repositories ^:replace
         [
           ["releases" "http://localhost:8061/nexus/content/repositories/releases/"]
           ["snapshots" "http://localhost:8061/nexus/content/repositories/snapshots/"]
           ["public" "http://localhost:8061/nexus/content/groups/public/"]
           ]
         }
 :auth {:repository-auth {#"releases|snapshots" {:username "nobody" :password "nobody"}}}
 }

