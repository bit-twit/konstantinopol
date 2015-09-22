(:require [clojure.java.io :as io])

(def bookmarks-string (slurp "/home/bit_twit/chromium_bookmarks.html"))

(defn -main [& args]
  (println bookmarks-string))
