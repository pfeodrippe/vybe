#version 330

in vec2 fragTexCoord;

// Input uniform values
uniform sampler2D texture0;
uniform sampler2D shadowMap;
uniform float u_far_clip;
uniform float u_near_clip;
//uniform vec2        u_time;

// Output fragment color
out vec4 finalColor;

//#define LIGHT_SHADOWMAP shadowMap;
#define RESOLUTION vec2(1200., 1200.)
#include "lygia/sample/clamp2edge.glsl"
//#define SAMPLEDOF_DEBUG
#define SAMPLEDOF_BLUR_SIZE 12.
#define SAMPLEDOF_COLOR_SAMPLE_FNC(TEX, UV) sampleClamp2edge(TEX, UV).rgb
#define SAMPLEDOF_DEPTH_SAMPLE_FNC(TEX, UV) linearizeDepth( sampleClamp2edge(TEX, UV).r, u_near_clip, u_far_clip)
#include "lygia/space/linearizeDepth.glsl"
#include "lygia/sample/dof.glsl"
#include "lygia/lighting/pbr.glsl"
#include "lygia/lighting/material/new.glsl"

// For debugging.
#include "lygia/draw/digits.glsl"

#define POSTPROCESSING

void main (void) {
    vec3 color = vec3(0.0);
    vec2 pixel = 1.0/vec2(1200., 1200.);
    vec2 st = fragTexCoord;

#if defined(POSTPROCESSING)
    color = sampleDoF(texture0, shadowMap, st, 1.9, 2.0).rgb;
    //color = texture(shadowMap, st).rgb;
    //color = vec3(linearizeDepth(texture(shadowMap, st).r, u_near_clip, u_far_clip));
    //color = texture(texture0, st).rgb;
#else
    Material material = materialNew();
    material.roughness = 0.1;

    color = pbr(material).rgb;
#endif

    finalColor = vec4(color,1.0);

    // Debugging.
    /*
    vec2 stt = st - vec2(0.01, 0.6);
    finalColor += digits(stt, u_far_clip);
    stt = st - vec2(0.01, 0.55);
    finalColor += digits(stt, u_near_clip);
    */
}
