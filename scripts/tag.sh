#!/usr/bin/env bash

version="$(sed -n 's/.*version in ThisBuild := \"\([0-9].[0-9].[0-9]*\)\"/\1/p' version.sbt)" # extracted version
tag="v$version" # git tag
git tag ${tag} # create git tag
git push origin ${tag} # push git tag to remote repo