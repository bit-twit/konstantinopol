(ns bookmark_parser.core
  (:require [instaparse.core :as insta]
            [clojure.java.io :as io]))

(def bookmarks-file (str (.getAbsolutePath (.getParentFile (io/file  (System/getProperty "user.dir")))) "/bookmarks_chromium.html"))

(println bookmarks-file)

(def bookmark-content (slurp bookmarks-file))

(comment 
(def chromium-parser
  (insta/parser
     "HTML = TAGS*
      TAGS = TAGS | DL_TAG | OTHER_TAG | WS
      DL_TAG = #'<DL><p>' WS (DT_H3_TAG | DT_CHILD_TAG) WS '</DL>'
      DT_H3_TAG = '<DT><H3></H3>'
      DT_CHILD_TAG = ''
      OTHER_TAG = ('<' HTML_TEXT (WS ATTRIBUTE)* WS? '/>') | ('<' HTML_TEXT (WS ATTRIBUTE)* WS? '>') | ('<' HTML_TEXT (WS ATTRIBUTE)* WS? '>' (HTML_TEXT | WS)* '</' HTML_TEXT '>') | ('<!DOCTYPE' (WS ATTRIBUTE)* WS '>') | ('<!--' (WS HTML_TEXT WS?)* WS? '-->')
      HTML_TEXT= #'([0-9a-zA-Z.!\\/;-=]\\s)+'
      ATTRIBUTE= (HTML_TEXT '=' '\"' (HTML_TEXT | ATTRIBUTE | WS) * '\"') | HTML_TEXT 
      WS=#'\\s*'"
     )))

(def chromium-parser
  (insta/parser
     "HTML = ((TAG|WS)*NEWLINE+)
      TAG = '<' ATTR '>'
      ATTR = #'[!.&&^[<>]]*'
      WS=#'\\s*'
      NEWLINE='\r\n'"
     ))

(def parser
  (insta/parser
     "HTML = (TAG|<COMMENT>|TEXT|<WS>)*
      TAG = <'<' '/'?> <WS?> NAME <WS?> (ATTR <WS?>)* <WS?> <'>'>
      COMMENT = <'<!--'> <WS?> (NAME|WS)* <WS?> <'-->'>
      ATTR = NAME (<'='> VAL)?
      NAME = #'[A-Za-z-!0-9._]+'
      VAL = <'\"'>? #'([A-Za-z-!0-9._ %:,\\'*+~;=/\\\\|&#@$?()\\[\\]{}]|\\pL|\u00AB|\u00AE|\u00B7|\u00BB|\u00D7|\u2019|\u2026|\u2022|\u2014|\u2013|\u2033|\u201C|\u201D|\u2190|\u25B6)*' <'\"'>?
      TEXT = VAL
      WS=#'\\s*'
      "
     ))

;; simple version of parser, starts with the outbound tags
(def chromium-parser-simple
  (insta/parser
     "HTML = (TAG|WS)*
      TAG = ('<' HTML_TEXT (WS ATTRIBUTE)* WS? '/>') | ('<' HTML_TEXT (WS ATTRIBUTE)* WS? '>') | ('<' HTML_TEXT (WS ATTRIBUTE)* WS? '>' (HTML_TEXT | WS)* '</' HTML_TEXT '>') | ('<!DOCTYPE' (WS ATTRIBUTE)* WS '>') | ('<!--' (WS HTML_TEXT WS?)* WS? '-->')
      HTML_TEXT= #'([0-9a-zA-Z.!\\/;=]|WS|\\p{Punct}|[^\\x20-\\x7E])+'
      ATTRIBUTE= (HTML_TEXT '=' '\"' (HTML_TEXT | ATTRIBUTE | WS) * '\"') | HTML_TEXT 
      WS=#'\\s'"
     ))

(defn chromium-parse [bookmark-content]
  (println "Number of characters: " (count bookmark-content))
  (insta/parse chromium-parser bookmark-content))

(defn -main
  []
  (let [test-string "<!DOCTYPE NETSCAPE-Bookmark-file-1>
<!-- This is an automatically generated file.
     It will be read and overwritten.
     DO NOT EDIT! -->
"
        test-string-2 "<!DOCTYPE NETSCAPE-Bookmark-file-1>
<!-- This is an automatically generated file.
     It will be read and overwritten.
     DO NOT EDIT! -->
<META HTTP-EQUIV=\"Content-Type\" CONTENT=\"text/html; charset=UTF-8\">
<TITLE>Bookmarks</TITLE>
<H1>Bookmarks</H1>
<DL><p>
    <DT><H3 ADD_DATE=\"1434279368\" LAST_MODIFIED=\"1432846188\" PERSONAL_TOOLBAR_FOLDER=\"true\">Bookmarks bar</H3>
    <DL><p>
        <DT><H3 ADD_DATE=\"1434279436\" LAST_MODIFIED=\"1434279437\">programare</H3>
        <DL><p>
            <DT><H3 ADD_DATE=\"1434279437\" LAST_MODIFIED=\"1434279437\">dicom</H3>
            <DL><p>
                <DT><A HREF=\"http://www.dcm4che.org/confluence/display/d2/dcmrcv\" ADD_DATE=\"1355822573\" ICON=\"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAACWElEQVQ4jaWTXUjTYRjFf///pvn9HWvZZgtpDloxF1rkRaURNKIIu0jrIgqJMqwos7rxKujjQi/MLrNIDRr0oYFSkbKyUkMxTMVQk2S1oUO3Oef07WK2HF6F5+45D+/hPee8r4S5RLAKyKs5HBKQZQmTXkNaUlzY0pJn5MheUxiXmhhLdpYWWZb+CQgBxsx0mqpKOXt0NwAGnZraa8XcqyjCpNcAcOrQLpqqSjEbMhBLxpVBAUFjaxcmvZaaq8cwZqYzOGZHo0oGID/HwAnLDi4WFVD7tI2HzR2IJQVpeYiyLHGnrJBLxQX8/O0iKT4ahULGMeVGo0rmvrWd87cbCCwshiwpWG+u/DsIAW87B1ClJrBnu57ICCVKhYLEuGgevfrIuVv1zAcWVoa4HIGFRc7cfExja2eIe97Wy8nKOub8gRUtKJcPGepU9BkqJEnC7pwO8RMOF/k5WUiSxNDYL0YmnKGdhLlE5G7RcaEoH3VaIp++jhAfE8Xpw3lEKBWhWz14+QGny83OrZtwTLmpqn/N+97vUHb3iXB7faLZ1id0B68L1b7LwtYzLIQQoqHls6hr6hBCCNHVPyrU+68IraVCWN98EV6fX9yoeSZkz+wcXp+fZlsfIxNOCguyydSsxdYzTHm1lfJqK++6h9CoUjh+IJcf9kletPfi9fnx+OaCFrZt3oAlz0jP4DjD4w6mZrzMeHz4/PMArIlUEh8TRUpCLLr0NMwGLS0d/XR/Gwt/B1kb1wEwOe3BNTOLfz6YeoQyWGVyQgwKWWZg1B4e4opu/gOr/o1/ALof6jmpQHImAAAAAElFTkSuQmCC\">dcmrcv - dcm4che-2.x - Confluence</A>"]
    (println (insta/parse parser test-string))
    (println (insta/parse parser test-string-2))
    (println (insta/parse parser bookmark-content))))
