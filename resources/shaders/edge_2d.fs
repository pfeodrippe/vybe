#version 330

in vec2 fragTexCoord;

// Input uniform values
uniform sampler2D texture0;
//uniform vec2        u_time;

// Output fragment color
out vec4 finalColor;

// #define NOISEBLUR_SECS u_time
// #define NOISEBLUR_GAUSSIAN_K 2.0
// #define BLUENOISE_TEXTURE u_noise
// #define BLUENOISE_TEXTURE_RESOLUTION u_noiseResolution

#include "lygia/sample/clamp2edge.glsl"
#define EDGE_SAMPLER_FNC(TEX, UV) sampleClamp2edge(TEX, UV).r
#include "lygia/filter/edge.glsl"

void main (void) {
    vec3 color = vec3(0.0);
    vec2 pixel = 1.0/vec2(1200., 1200.);
    vec2 st = fragTexCoord;
    float radius = 2.0;

    color += edgePrewitt(texture0, st, pixel * radius);

    finalColor = vec4(color,1.0);
}
