(ns konstantinopol.core
  (:require [clojurewerkz.elastisch.rest :as esr])
  (:gen-class))

(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))

(defn -main
  [& args]
  (let [conn (esr/connect "http://127.0.0.1:9200")]
    (println conn)
    ))
