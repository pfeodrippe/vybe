#!/bin/bash

set -ex

clojure -T:build build-flecs && cp target/classes/META-INF/maven/io.github.pfeodrippe/vybe-flecs/pom.xml .

cat <<EOF > ~/.m2/settings.xml
<settings>
  <servers>
    <server>
      <id>clojars</id>
      <username>\${env.DEPLOY_USERNAME}</username>
      <password>\${env.DEPLOY_TOKEN}</password>
    </server>
  </servers>
</settings>
EOF

cat ~/.m2/settings.xml
