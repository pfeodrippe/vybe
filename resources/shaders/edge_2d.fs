#version 330

in vec2 fragTexCoord;
in vec4 fragColor;

// Input uniform values
uniform sampler2D texture0;
uniform vec4 colDiffuse;
uniform float edge_fill;

// Output fragment color
out vec4 finalColor;

// #define NOISEBLUR_SECS u_time
// #define NOISEBLUR_GAUSSIAN_K 2.0
// #define BLUENOISE_TEXTURE u_noise
// #define BLUENOISE_TEXTURE_RESOLUTION u_noiseResolution

#include "lygia/sample/clamp2edge.glsl"
#define EDGE_SAMPLER_FNC(TEX, UV) sampleClamp2edge(TEX, UV).g
#include "lygia/filter/edge.glsl"

void main (void) {
    vec3 color = vec3(0.0);
    vec2 pixel = 1.0/vec2(1200., 1200.);
    vec2 st = fragTexCoord;
    // Higher radius is like an out of focus effect.
    float radius = 1.1;

    float c = 1.0;

    bool trigger =
        st.x * st.y < sin(edge_fill * 0.87 * c) * cos(edge_fill * 0.23 * c) + sin(edge_fill * 0.37 * c) * cos(edge_fill * 0.53 * c)
        //st.x > sin(edge_fill * 0.24) * 1.0 && st.x < sin(edge_fill * 0.87 * c)
        //st.y > sin(edge_fill * 0.41 * c) * 0.4 && st.y < sin(edge_fill * 0.46 * c)
        ;

    if (trigger) {
        //color += edgePrewitt(texture0, st, pixel * radius) * vec3(0.9, 0.3, 0.4);
        color += edgePrewitt(texture0, st, pixel * radius) * vec3(0.9, 0.7, 0.5) + 0.2;

        /*
        if (color.x <= 0.0001) {
            vec4 texelColor = texture(texture0, fragTexCoord);
            finalColor = texelColor*colDiffuse*fragColor;
        } else {
            finalColor = vec4(color,1.0);
        }
        */

        finalColor = vec4(color, 1.0);
    } else {
        // Texel color fetching from texture sampler
        vec4 texelColor = texture(texture0, fragTexCoord);

        // NOTE: Implement here your fragment shader code

        finalColor = texelColor*colDiffuse*fragColor;
    }
}
