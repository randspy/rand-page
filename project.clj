(defproject webpage "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/clojurescript "0.0-2173"]
                 [compojure "1.1.6"]
                 [hiccup "1.0.5"]
                 [ring-server "0.3.1"]]
  :plugins [[lein-ring "0.8.10"]
            [lein-cljsbuild "1.0.2"]]
  :cljsbuild {
               :builds [{
                          ; The path to the top-level ClojureScript source directory:
                          :source-paths ["src-cljs"]
                          ; The standard ClojureScript compiler options:
                          ; (See the ClojureScript compiler documentation for details.)
                          :compiler {
                                      :output-to "resources/public/js/main.js"
                                      :optimizations :whitespace
                                      :pretty-print true}}]}
  :ring {:handler webpage.handler/app
         :init webpage.handler/init
         :destroy webpage.handler/destroy}
  :aot :all
  :profiles
  {:production
   {:ring
    {:open-browser? false, :stacktraces? false, :auto-reload? false}}
   :dev
   {:dependencies [[ring-mock "0.1.5"] [ring/ring-devel "1.2.1"]]}})
