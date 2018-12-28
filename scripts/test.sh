#!/usr/bin/env bash

tests=(
    rddTest dsTest streamingTest structuredTest
)

for t in "${tests[@]}"
do
   sbt ++$1 ${t}
done