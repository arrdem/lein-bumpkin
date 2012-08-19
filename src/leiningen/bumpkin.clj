(ns leiningen.bumpkin
  (:use bumpkin.core))

(declare bumpkin release build patch set)

(defn project-file [project]
  (clojure.java.io/file (str (:root project) "/project.clj")))

(def bump bumpkin)

(defn bumpkin 
  "A tool for incrementing the version number
   Arguments:
       ([major]          - increments the version by 1.0.0 resetting minor and patch
        [minor]          - increments the version by 0.1.0 resetting patch
        [patch]          - increments patch
        [set <VERSION>]  - forcibly sets the version number to some value
       )"
  ^{:subtasks [release build patch force]} 
  [project & args]
  (update-version-number (project-file project) patch-inc))

(defn major [project]
  (update-version-number (project-file project) major-inc))

(defn minor [project] 
  (update-version-number (project-file project) minor-inc))

(defn patch [project]
  (update-version-number (project-file project) patch-inc))

(defn set [project version]
  (let [file (project-file project)]
    (update-version file (slurp file) (parse-version version))))
