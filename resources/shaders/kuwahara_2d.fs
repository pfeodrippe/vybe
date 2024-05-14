#version 330

// This shader is based on the basic lighting shader
// This only supports one light, which is directional, and it (of course) supports shadows

// Input vertex attributes (from vertex shader)
in vec3 fragPosition;
in vec2 fragTexCoord;
//in vec4 fragColor;
in vec3 fragNormal;

// Input uniform values
uniform sampler2D texture0;
uniform vec4 colDiffuse;

// Output fragment color
out vec4 finalColor;

// Input lighting values
uniform vec3 lightDir;
uniform vec4 lightColor;
uniform vec4 ambient;
uniform vec3 viewPos;

// Input shadowmapping values
uniform mat4 lightVP; // Light source view-projection matrix
uniform sampler2D shadowMap;

uniform int shadowMapResolution;

#include "lygia/sample/clamp2edge.glsl"
#define KUWAHARA_SAMPLER_FNC(TEX, UV) sampleClamp2edge(TEX, UV)
#include "lygia/filter/kuwahara.glsl"
#include "lygia/draw/digits.glsl"

void main() {
    // Painting-like effect.
    vec2 pixel = 1.0/vec2(600., 600.);
    vec2 st = gl_FragCoord.xy * pixel;
    vec3 painting = vec3(0.0);

    painting += kuwahara(texture0, st, pixel, 5).rgb;    

    finalColor = vec4(painting, 1.0);
}
