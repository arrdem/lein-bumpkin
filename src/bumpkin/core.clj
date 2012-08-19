(ns bumpkin.core
  (:use clojure.stacktrace)
  (:require [clojure.tools.cli :only [cli]]
            [clojure.java.io]))

;------------------------------------------------------------------------------
; Lay out the version datastructure and related functions

; Explicit Standard for the data representation of versions
; Type: clojure.lang.IMap
; Keys:
;     :major     - the major version number
;     :minor     - the minor version number
;     :patch     - the patch number
;     :status    - string, probably one of "SNAPSHOT" or "RELEASE"

(defn str-convert [v]
  (-> v
    (assoc :major (read-string (:major v)))
    (assoc :minor (read-string (:minor v)))
    (assoc :patch (read-string (:patch v)))))

(defn major-inc [s]
  (-> s
    (assoc :major (inc (:major s)))
    (assoc :minor 0)
    (assoc :patch 0)))

(defn minor-inc [s]
  (-> s
    (assoc :minor (inc (:minor s)))
    (assoc :patch 0)))

(defn patch-inc [s]
  (assoc s :patch (inc (:patch s))))

(defn parse-version [text]
  (let [values (re-find #"(\d+)\.(\d+)\.(\d+)(-\w+)?" text)]
    (if (not (nil? values))
      (str-convert (zipmap [:match :major :minor :patch :status] values))
      (throw (Exception. "Impropper version format, nil numerics")))))

(defn render-version [version]
  (str (:major version)
       "."
       (:minor version)
       "."
       (:patch version)
       (if (nil? (:status version))
         "-"
         (str (:status version)))))

(defn safe-parse-version 
  "In the case that you have no idea what the input string is, this routine
   is your best bet for getting back a version number not an error somewhere.
   In the event that no valid version is parsed, it will return the version
   map representation of 0.1.0-SNAPSHOT, the standard starting point."
  [text]
  (try
    (parse-version text)
    (catch Exception e (do
                         (println (.getMessage e))
                         (.printStackTrace e)
                         {:major 0 :minor 1 :patch 0 :status "SNAPSHOT"}))))

;------------------------------------------------------------------------------
; Do the mucking about to find the VERSION file

(def possible-version-files
  (list
    (clojure.java.io/file "./project.clj")
    (clojure.java.io/file "./version")
    (clojure.java.io/file "./VERSION")
    (clojure.java.io/file "./version.txt")
    (clojure.java.io/file "./VERSION.txt")))

(defn get-version-file 
  "Return the first version file which exists, else a new file"
  [& files]
  (loop [v (concat possible-version-files files)]
    (if (.exists (first v)) (first v)
      (if (empty? v)
        (clojure.java.io/file "./version")
        (recur (rest v))))))

(defn write-version [file version]
  (with-open [wrtr (clojure.java.io/writer file)]
        (.write wrtr (render-version version))))

(defn update-version-number [file updater]
  (let [vs    (slurp file)
        value (safe-parse-version vs)
        vf    (updater value)]
    (with-open [wrtr (clojure.java.io/writer file)]
      (.write wrtr (clojure.string/replace 
                     vs 
                     (re-pattern (get value :match 
                                      "\\d+\\.\\d+\\.\\d+(-\\w+)?"))
                                           (render-version vf))))
    vf))

;------------------------------------------------------------------------------
; Lay out the actual command-line invocation of the core bumpkin program

(def usage
"Usage:
    $ [bump|bumpkin] [-x|-y|-z|-f <version>] [<version-file>]

    If invoked with no arguments, bumpkin will print the version number and
    exit 0. Other behavior requires arguments as specified below.
      -?, --help    forces the printing of this message, *nix standard behavior
      -x, --major   causes bump to up the major version as per SemVer 2.0.0-RC1
      -y, --minor   .... for the minor version
      -z, --build   .... for the build number
      -f, --force   forcibly sets the version number, if the provided value is
                      a legal SemVer version.
    
Notes:
 - If a version-file argument is provided, it MUST be the last argument or else 
   will probably be ignored, or the other argument(s) will be parsed 
   incorrectly.
 - Simulnateous use of multiple version files is not supported.
 - If multiple increment flags are provided, only the one which effects the 
   most significant version digit is honored, the rest are discarded.
    
About:
  Bumpkin is a (repatively simple) bump program, which records, increments and
  prints version numbers which are correct or at least legal under the 
  Semantic Versioning 2.0.0-RC1 specification (referred to above as SemVer).

  When `bump`ing, bumpkin looks for files which may contain version numbers in
  the following order
  ./version, ./VERSION

  Copyright Reid McKenzie <rmckenzie92@gmail.com>
  released under the Eclipse Public License 1.0 
      <http://opensource.org/licenses/EPL-1.0>")

(defn parse-args [args]
  (clojure.tools.cli/cli args 
   ["-x" "--major" "specifies that the major version is to be bumped" 
       :default false :flag true]
   ["-y" "--minor" "specifies that the minor version is to be bumped" 
       :default false :flag true]
   ["-z" "--patch" "specifies that the patch number is to be bumped"  
       :default true :flag true]
   ["-f" "--force" "specifies that the build number is to be forcibly set"      
       :default nil :parse-fn parse-version]
   ["-?" "--help" "standard flag, forces the help message to be printed"
       :default nil :flag true]))

(defn rp [value]
  (println (render-version value)))

(defn -main
  "The entry point function for bumpkin"
  [& args]
  (let [[options junk banner] (parse-args args)
        fallback-file (last args)]
    (cond
      (empty? args) 
          (println 
            (render-version 
              (safe-parse-version 
                (slurp
                  (get-version-file)))))

      (:help options)
          (println usage)

      (not (nil? (:force options)))
         (let [v (safe-parse-version (:force options))]
          (write-version (get-version-file) v)
          (rp v)) 
         
      (:major options)
        (rp (update-version-number (get-version-file) major-inc))

      (:minor options)
        (rp (update-version-number (get-version-file) minor-inc))
    
      (:patch options)
        (rp (update-version-number (get-version-file) patch-inc))

      )
    )
  )
