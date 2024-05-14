#version 330

#ifdef GL_ES
precision mediump float;
#endif

#include "../com/pfeodrippe/vybe/shaders/lygia/distort/grain.glsl"
#include "../com/pfeodrippe/vybe/shaders/lygia/distort/barrel.glsl"
#include "../com/pfeodrippe/vybe/shaders/lygia/filter/sharpen.glsl"
#include "../com/pfeodrippe/vybe/shaders/lygia/draw/rect.glsl"

varying vec4 v_color;
varying vec2 v_texCoords;
varying vec2 v_resolution;
uniform sampler2D u_texture;
uniform float u_time;

out vec4 fragColor;

void main() {
    u_texture;
    u_time;

    //fragColor =  vec4(grain(u_texture, st, v_resolution, 20.5*cos(.4*u_time)), 1.);
    //fragColor = vec4(grain(u_texture, st, v_resolution, 20.5*cos(.4*u_time)), 1.);

    vec2 pixel = 0.1/v_resolution.xy; // For smaller stuff
    //vec2 pixel = 10.0/v_resolution.xy; // For the whole screen
    vec2 st = gl_FragCoord.xy * pixel;

    //st = barrel(st, fract(80.0 - u_time * 0.000));
    st = barrel(st, 40.0);

    st *= 5.0;
    st = fract(st - 2.0*u_time);
    // st = fract(st - 10.0*u_time);

    //color = vec3(rect(vec2(st.x), 0.55));

    fragColor = v_color * texture(u_texture, v_texCoords) - st.g * 0.5 + st.r * 0.5; // smaller stuff
    // fragColor = v_color * texture(u_texture, v_texCoords) - st.g * 0.1 + st.r * 0.1; // whole screen
    // fragColor = (v_color + vec4(-st.x, -st.y, 0.0, 0.0) * 0.5) * texture(u_texture, v_texCoords);
}
