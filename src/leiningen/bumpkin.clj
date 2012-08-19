(ns leinigen.bumpkin
  (:use bumpkin.core))

(declare bumpkin release build patch set)

(defn project-file [project]
  (clojure.java.io/File. (str (:root project) "/project.clj")))

(def bump bumpkin)

(defn bumpkin 
  "A tool for incrementing the version number
   Arguments:
       ([release]  - increments the version by 1.0.0 resetting build and patch
        [build]    - increments the version by 0.1.0 resetting patch
        [patch]    - increments patch
        [set]    - forcibly sets the version number to some value
        [codename]
       )"
  ^{:subtasks [release build patch force]} 
  [project & args]
  (patch project))

(defn release)
(defn build)
(defn patch)
(defn set)
