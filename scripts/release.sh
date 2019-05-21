#!/usr/bin/env bash

# get tag from version.sbt
version="$(sed -n 's/.*version in ThisBuild := \"\([0-9].[0-9].[0-9]*\)\"/\1/p' version.sbt)" # extracted version
tag="v$version" # git tag

# create tag
git tag ${tag}

# push git tag to remote repo
git push https://${GITHUB_USERNAME}:${GITHUB_TOKEN}@github.com/fsanaulla/chronicler-spark.git ${tag}

# push release
sbt githubRelease