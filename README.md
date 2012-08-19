#Bumpkin
---------
Bumpkin is quite simply a Clojure bump script with [leiningen](http://leiningen.org/) support which implements the [Semantic Versioning](http://semver.org/) specification.

###Features
Bumpkin not only provides the minimum of bump script functionality which one would expect, such as tracking and bumping a version number, but also provides some options by which the user can explicitly bump the major, minor and build version numbers as well as generate a (probably nonsensical) name for your version.

###Usage

    $ bump [& args]
        About:
            Incriments the  project version number, for lein projects updating project.clj
            and for other projects writing it to <root>/version and appending it to 
            <root>/NEWS
        Arguments:
            [none]     -  Bumps the version number by a single patch (+0.0.1)
            --patch    -  Same as no arguments
            --build    -  Bumps by +0.1.0, zeroing the patch number
            --release  -  Bumps by +1.0.0, zeroing out the buld and patch numbers
            --codename -  Generates and prints a codename for your release after 
                          evaluating the other arguments. Intended usage is 
                          $ git flag $(bump --codename)

###On The Horizon (a long way off)

     * Good codename generation ( actually pretty hard )
     * Codename generation API