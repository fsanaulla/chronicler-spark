#!/usr/bin/env bash

- sbt ++${TRAVIS_SCALA_VERSION} rddPublish dsPublish streamingPublish structuredPublish
