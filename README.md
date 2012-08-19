#Bumpkin
---------
Bumpkin is quite simply a Clojure bump script with [leiningen](http://leiningen.org/) support which implements the [Semantic Versioning](http://semver.org/) specification.

###Features
Bumpkin not only provides the minimum of bump script functionality which one would expect, such as tracking and bumping a version number, but also provides some options by which the user can explicitly bump the major, minor and build version numbers as well as generate a (probably nonsensical) name for your version.

###Usage

     Usage:
        $ [bump|bumpkin] [-x|-y|-z|-f <version>] [<version-file>]

        If invoked with no arguments, bumpkin will print the version number and
        exit 0. Other behavior requires arguments as specified below.
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
          <http://opensource.org/licenses/EPL-1.0>

###On The Horizon (a long way off)

     * The power to bump the version number in a Leinigen project.clj
     * Good codename generation ( actually pretty hard )
     * Codename generation API
