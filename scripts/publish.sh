#!/usr/bin/env bash

modules=(
    sparkCore sparkRdd sparkDs sparkStreaming sparkStructuredStreaming
)

for md in "${modules[@]}"
do
   sbt ";project $md; ++${TRAVIS_SCALA_VERSION}; fullRelease"
done