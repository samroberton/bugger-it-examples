# /usr/bin/env bash

JAVA_OPTS="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n -Dclojure.compiler.disable-locals-clearing=true" lein repl
