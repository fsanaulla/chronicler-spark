#!/usr/bin/env bash

- openssl aes-256-cbc -K ${encrypted_56e8fc46d7cd_key} -iv ${encrypted_56e8fc46d7cd_iv} -in secring.asc.enc -out secring.asc -d
- openssl aes-256-cbc -K ${encrypted_56e8fc46d7cd_key} -iv ${encrypted_56e8fc46d7cd_iv} -in pubring.asc.enc -out pubring.asc -d
