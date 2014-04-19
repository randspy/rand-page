(defproject webpage "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/clojurescript "0.0-2173"]
                 [org.clojure/algo.generic "0.1.2"]
                 [compojure "1.1.6"]
                 [hiccup "1.0.5"]
                 [ring-server "0.3.1"]
                 [lib-noir "0.7.9"]
                 [reagent "0.4.2"]
                 [me.raynes/cegdown "0.1.1"]
                 [clygments "0.1.1"]
                 [markdown-clj "0.9.43"]
                 [enlive "1.1.5"]
                 [stasis "1.0.0"]
                 [expectations "2.0.6"]]

  :plugins [[lein-ring "0.8.10"]
            [lein-cljsbuild "1.0.2"]
            [lein-autoexpect "1.2.2"]]

  :hooks [leiningen.cljsbuild]
  :main webpage.handler
  :min-lein-version "2.0.0"
  :uberjar-name "webpage.jar"

  :cljsbuild {
               :builds  {
                          :prod {
                             :source-paths ["src-cljs"]

                             :compiler {
                                         :output-to "resources/public/js/main.js"
                                         :optimizations :advanced
                                         :preamble ["reagent/react.min.js"]}}
                          :development {
                             :source-paths ["src-cljs"]
                             :compiler {
                                         :output-to "resources/public/js/main.js"
                                         :output-dir "resources/public/js"
                                         :source-map "resources/public/js/main.js.map"
                                         :optimizations :whitespace
                                         :preamble ["reagent/react.js"]
                                         :pretty-print true}}}
                         }
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
