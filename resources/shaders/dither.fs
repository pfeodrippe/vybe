#version 330

in vec2 fragTexCoord;
in vec4 fragColor;

// Input uniform values
uniform sampler2D texture0;
uniform vec4 colDiffuse;
uniform vec3 u_offsets;
uniform vec2 u_mouse;
uniform float u_radius;

uniform sampler2D u_color_ids_tex;
uniform vec4 u_color_ids_bypass[10];
uniform int u_color_ids_bypass_count;

uniform vec2 u_resolution;

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
    // BYPASS
    vec4 color_id = texture(u_color_ids_tex, fragTexCoord);
    bool is_bypassing = false;
    for (int i = 0; i < u_color_ids_bypass_count; i++) {
        if (abs(color_id.r - (u_color_ids_bypass[i]).r) < 0.001 &&
            abs(color_id.g - (u_color_ids_bypass[i]).g) < 0.001 &&
            abs(color_id.b - (u_color_ids_bypass[i]).b) < 0.001 &&
            abs(color_id.a - (u_color_ids_bypass[i]).a) < 0.001) {
            is_bypassing = true;
        }
    }

    if (is_bypassing) {
        // Texel color fetching from texture sampler
        vec4 texelColor = texture(texture0, fragTexCoord);

        finalColor = texelColor*colDiffuse*fragColor;
        finalColor.a = texelColor.a;
        return;
    }
    // END OF BYPASS

    vec4 color = vec4(0.0, 0.0, 0.0, 1.0);
    vec2 st = fragTexCoord;
    vec2 direction = pow(st - u_mouse, vec2(2., 2.));
    vec4 texelColor = texture(texture0, fragTexCoord);
    float radius = 0.5;
    if (u_radius != 0.0) {
        radius = u_radius;
    }
    vec2 factor = u_resolution * radius;
    // Below with #define SAMPLEDITHER_FNC ditherVlachos looks good.
    //float radius = 0.5/5.0;
    // The ones below are interesting.
    // float radius = 0.5/3.0;
    //float radius = 0.08;

    if (true) {
        // Chromatic aberration.
        vec3 offsets = vec3(u_offsets.x, u_offsets.y, u_offsets.z);
        float rr = sampleDither(texture0, max(st - vec2(offsets.r)*direction, vec2(0.0)), factor).r;
        float gg = sampleDither(texture0, max(st - vec2(offsets.g)*direction, vec2(0.0)), factor).g;
        float bb = sampleDither(texture0, max(st - vec2(offsets.b)*direction, vec2(0.0)), factor).b;

        color.r = rr;
        color.g = gg;
        color.b = bb;
    } else {
        color = sampleDither(texture0, st, factor);
    }

    color.a = texelColor.a;

    //color.rgb = vec3(luma(color.rgb));

    if (st.y < 0.) {
        finalColor = vec4(1.0, 0., 0., 1.0);
    } else {
        finalColor = color;
    }

}
