#!/bin/bash

set -ex

git submodule update --init --recursive

mv test-resources/AtkUGens.scx sonic-pi/prebuilt/windows/x64/plugins
ls sonic-pi/prebuilt/windows/x64/plugins

export VYBE_JEXTRACT=jextract-22/bin/jextract
bin/jextract-libs.sh
