__VYBE_JEXTRACT_DEFAULT=~/Downloads/jextract-osx/bin/jextract

VYBE_JEXTRACT="${VYBE_JEXTRACT:-$__VYBE_JEXTRACT_DEFAULT}"

rm -rf src-java/org/vybe/blender

$VYBE_JEXTRACT \
    --output src-java \
    --header-class-name blender \
    -I ~/dev/blender/source/blender/makesdna \
    -I ~/dev/blender/source/blender/blenlib \
    -t org.vybe.blender basilisp/vybe_blender.c

# NOTE We need, for now, to delete the trace methods calls in blender_2.java.
