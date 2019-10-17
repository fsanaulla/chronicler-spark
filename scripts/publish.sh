#!/usr/bin/env bash

sbt "; + publishSigned; sonatypeBundleRelease"
