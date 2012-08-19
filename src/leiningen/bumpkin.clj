(ns leiningen.bumpkin
  (:require [bumpkin.core :as bmp]
            [clojure.repl]))

(defn project-file [project]
  (clojure.java.io/file (str (:root project) "/project.clj")))

(defn wrapper [project incrementer]
  (println 
    (bmp/render-version 
      (bmp/update-version-number 
        (project-file project) incrementer))))


(defn ^{:no-project-needed false} 
  bumpkin 
  "A tool for incrementing the version number
   Arguments:
       ([major]          - increments the version by 1.0.0 resetting minor and patch
        [minor]          - increments the version by 0.1.0 resetting patch
        [patch]          - increments patch
       )"
  ([project]
   (println (clojure.repl/doc bumpkin)))
  
  ([project cmd & args]
   (case cmd
     "help"  (println (clojure.repl/doc bumpkin))
     "major" (wrapper project bmp/major-inc)
     "minor" (wrapper project bmp/minor-inc)
    (wrapper project bmp/patch-inc)
   )))
