#!/bin/bash

set -ex

git submodule update --init --recursive

export VYBE_JEXTRACT=jextract-22/bin/jextract
bin/jextract-libs.sh

clojure -T:build compile-app
