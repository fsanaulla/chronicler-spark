#!/usr/bin/env bash

modules=(
    sparkRdd sparkDs sparkStreaming sparkStructuredStreaming
)

for md in "${modules[@]}"
do
   sbt ";project $md; ++$TRAVIS_SCALA_VERSION; fullRelease"
done