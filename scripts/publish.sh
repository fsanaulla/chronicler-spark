#!/usr/bin/env bash

sbt "project sparkRdd" "+ fullRelease"
sbt "project sparkDs" "+ fullRelease"
sbt "project sparkStreaming" "+ fullRelease"
sbt "project sparkStructuredStreaming" "+ fullRelease"