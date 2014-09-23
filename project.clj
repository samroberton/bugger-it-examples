(defproject bugger-it-examples "0.1.0-SNAPSHOT"
  :description "Various examples to demonstrate and test usage of bugger-it."
  :url "https://github.com/samroberton/bugger-it-examples"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [ring "1.3.1"]]
  :main ^:skip-aot bugger-it-examples.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
