#!/usr/bin/env bash

tests=(
    rddTest dsTest streamingTest structuredTest
)

for t in "${tests[@]}"
do
   sbt ++${TRAVIS_SCALA_VERSION} ${t}
done