#version 330

in vec2 fragTexCoord;
in vec4 fragColor;
in vec3 fragNormal;

// Input uniform values
uniform sampler2D texture0;
uniform vec4 colDiffuse;
uniform vec3 camDir;

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

#include "lygia/draw/digits.glsl"

void main (void) {
    vec3 color = vec3(0.0);
    vec4 texel = texture(texture0, fragTexCoord);
    vec2 pixel = 1.0/vec2(1200., 1200.);
    vec2 st = fragTexCoord;
    float radius = 2.0;
    float alpha = 1.0;

    //if (colDiffuse.rgb == vec3(1.0) && fragNormal.z < 0.45) {
    if (colDiffuse.rgb == vec3(1.0)) {
        //color += edgePrewitt(texture0, st, pixel * radius);
        //if (color.r == .0 && (st.x < 0.48 || st.x < 0.52 || st.x > 0.96)) {
        if (abs(dot(normalize(camDir), fragNormal)) > 0.1) {
            discard;
        }

        finalColor = texel*colDiffuse*fragColor;
        //finalColor = vec4(color, alpha);
    } else {
        finalColor = texel*colDiffuse*fragColor;
    }

    vec2 stt = st - vec2(1/4.0 + 0.01, 0.04);
    stt.y = -stt.y;

    //finalColor += digits(stt, dot(camDir, fragNormal));


}
