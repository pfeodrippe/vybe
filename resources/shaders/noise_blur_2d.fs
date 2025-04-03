#version 330

in vec2 fragTexCoord;

// Input uniform values
uniform sampler2D texture0;
uniform float u_radius;
//uniform vec2        u_time;

// Output fragment color
out vec4 finalColor;

// #define NOISEBLUR_SECS u_time
// #define NOISEBLUR_GAUSSIAN_K 2.0
// #define BLUENOISE_TEXTURE u_noise
// #define BLUENOISE_TEXTURE_RESOLUTION u_noiseResolution

#include "lygia/sample/clamp2edge.glsl"
#define NOISEBLUR_SAMPLER_FNC(TEX, UV) sampleClamp2edge(TEX, UV)
#include "lygia/filter/noiseBlur.glsl"

void main (void) {
    vec3 color = vec3(0.0);
    vec2 pixel = 1.0/vec2(1200., 1200.);
    vec2 st = fragTexCoord;
    vec4 texelColor = texture(texture0, fragTexCoord);
    if (texelColor.a == 0.0) discard;

    color += noiseBlur(texture0, st, pixel, u_radius).rgb;

    finalColor = vec4(color,texelColor.a);
}
