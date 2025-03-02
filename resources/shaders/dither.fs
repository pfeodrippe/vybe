#version 330

in vec2 fragTexCoord;

// Input uniform values
uniform sampler2D texture0;
uniform vec3 u_offsets;
uniform vec2 u_mouse;
uniform float u_radius;

// Output fragment color
out vec4 finalColor;

// #define PLATFORM_WEBGL
// #define DITHER_TIME u_time
// #define DITHER_CHROMA

#define DITHER_PRECISION 30
//#define SAMPLEDITHER_FNC ditherBayer
#define SAMPLEDITHER_FNC ditherBlueNoise
//#define SAMPLEDITHER_FNC ditherTriangleNoise
//#define SAMPLEDITHER_FNC ditherInterleavedGradientNoise
//#define SAMPLEDITHER_FNC ditherVlachos
//#define SAMPLEDITHER_FNC ditherShift

#include "lygia/math/decimate.glsl"
#include "lygia/color/luma.glsl"
#include "lygia/sample/dither.glsl"

void main() {
    vec4 color = vec4(0.0, 0.0, 0.0, 1.0);
    vec2 st = fragTexCoord;
    vec2 direction = pow(st - u_mouse, vec2(2., 2.));
    vec4 texelColor = texture(texture0, fragTexCoord);
    float radius = 0.5;
    if (u_radius != 0.0) {
        radius = u_radius;
    }
    // Below with #define SAMPLEDITHER_FNC ditherVlachos looks good.
    //float radius = 0.5/5.0;
    // The ones below are interesting.
    // float radius = 0.5/3.0;
    //float radius = 0.08;

    if (true) {
        // Chromatic aberration.
        vec3 offsets = vec3(u_offsets.x, u_offsets.y, u_offsets.z);
        float rr = sampleDither(texture0, max(st - vec2(offsets.r)*direction, vec2(0.0)), vec2(1200., 1200.) * radius).r;
        float gg = sampleDither(texture0, max(st - vec2(offsets.g)*direction, vec2(0.0)), vec2(1200., 1200.) * radius).g;
        float bb = sampleDither(texture0, max(st - vec2(offsets.b)*direction, vec2(0.0)), vec2(1200., 1200.) * radius).b;

        color.r = rr;
        color.g = gg;
        color.b = bb;
    } else {
        color = sampleDither(texture0, st, vec2(1200., 1200.) * radius);
    }

    color.a = texelColor.a;

    //color.rgb = vec3(luma(color.rgb));

    if (st.y < 0.) {
        finalColor = vec4(1.0, 0., 0., 1.0);
    } else {
        finalColor = color;
    }

}
