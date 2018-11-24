#!/usr/bin/env bash

sbt "++${TRAVIS_SCALA_VERSION} rddPublish"
sbt "++${TRAVIS_SCALA_VERSION} dsPublish"
sbt "++${TRAVIS_SCALA_VERSION} streamingPublish"
sbt "++${TRAVIS_SCALA_VERSION} structuredPublish"