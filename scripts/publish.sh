#!/usr/bin/env bash

modules=(
    sparkRdd sparkDs sparkStreaming sparkStructuredStreaming
)

for md in "${modules[@]}"
do
   sbt "project $md" "++$1 fullRelease"
done