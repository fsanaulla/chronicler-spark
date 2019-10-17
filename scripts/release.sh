#!/usr/bin/env bash

# get tag from version.sbt
version="$(sed -n 's/.*version in ThisBuild := \"\([0-9].[0-9].[0-9]*\)\"/\1/p' version.sbt)" # extracted version
tag="v$version" # git tag

# create tag
git tag $tag

# push git tag to remote repo
git push https://${GITHUB_USERNAME}:${GITHUB_TOKEN}@github.com/fsanaulla/chronicler-spark.git ${tag}

filename="${version}.md"
release_note=`cat changelog/${filename}`
branch=$(git rev-parse --abbrev-ref HEAD)
repo_full_name=$(git config --get remote.origin.url | sed 's/.*:\/\/github.com\///;s/.git$//')
release_name="chronicler-spark $version"
json_data=$( jq -n \
                  --arg tn "$version" \
                  --arg tc "$branch" \
                  --arg nm "$release_name" \
                  --arg bd "$release_note" \
                  '{tag_name: $tn, target_commitish: $tc, name: $nm, body: $bd, draft: false, prerelease: false}' )

echo "Create release $version for repo: $repo_full_name branch: $branch"
curl --data "$json_data" "https://api.github.com/repos/$repo_full_name/releases?access_token=${GITHUB_TOKEN}"