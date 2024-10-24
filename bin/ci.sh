#!/bin/bash

set -ex

git submodule update --init --recursive

mv test-resources/AtkUGens.scx sonic-pi/prebuilt/macos/universal/supercollider/Resources/plugins
ls sonic-pi/prebuilt/macos/universal/supercollider/Resources/plugins

export VYBE_JEXTRACT=jextract-22/bin/jextract
bin/jextract-libs.sh

clojure -T:build compile-app
