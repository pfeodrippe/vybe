---
title: Vybe
description: A Clojure Game Framework
---

[![Clojars Project](https://img.shields.io/clojars/v/io.github.pfeodrippe/vybe.svg)](https://clojars.org/io.github.pfeodrippe/vybe)

Code at <https://github.com/pfeodrippe/vybe>.

## Vybe

A Clojure framework for game dev (alpha).

## Vybe Packages

We have a Github Actions pipeline that builds, tests and publishes to Clojars
for each OS, each has a suffix for its version, check below:

- OSX universal
    - `io.github.pfeodrippe/vybe {:mvn/version "0.7.570-macos-universal"}`
- Windows (x64)
    - `io.github.pfeodrippe/vybe {:mvn/version "0.7.570-win-x64"}`
- Linux (x64)
    - `io.github.pfeodrippe/vybe {:mvn/version "0.7.570-linux-x64"}`
- Linux (x64) Basic, for example, if you don't have access to AVX2 instructions
    - `io.github.pfeodrippe/vybe {:mvn/version "0.7.570-linux-x64--basic"}`

## Getting started

Check [Getting Started](getting-started.md).
