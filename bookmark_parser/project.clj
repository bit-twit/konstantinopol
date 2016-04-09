(defproject bookmark_parser "0.1.0-SNAPSHOT"
  :description "Parses bookmark files"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"] [instaparse "1.4.1"]]
  :profiles {:dev {
                   :plugins [[cider/cider-nrepl "0.10.0-SNAPSHOT"]]
                   :dependencies [[org.clojure/tools.nrepl "0.2.7"]]}}
  :main cljparser.core
  :plugins [])
