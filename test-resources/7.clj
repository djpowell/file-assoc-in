{:custom1 {    #_ ignore
            :some "property"} ; a comment



 :user {
         :plugins [
                    [lein-cljsbuild "0.3.0"]
                    ]
         :java-cmd "c:\\wrong\\place\\java.exe"
         :repositories ^:replace
         [
           ["releases" "http://localhost:8061/nexus/content/repositories/releases/"]
           ["snapshots" "http://localhost:8061/nexus/content/repositories/snapshots/"]
           ["public" "http://localhost:8061/nexus/content/groups/public/"]
           ]
         }


 :auth {:repository-auth
        {#"releases|snapshots"
         {:username
          "nobody"
          :password
          "nobody"}}}


 }

; the end
