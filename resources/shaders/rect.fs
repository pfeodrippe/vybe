#version 330

#ifdef GL_ES
precision mediump float;
#endif

#include "../com/pfeodrippe/vybe/shaders/lygia/draw/rect.glsl"
#include "../com/pfeodrippe/vybe/shaders/lygia/space/ratio.glsl"

in vec4 v_color;
in vec2 v_texCoords;
in vec2 v_resolution;
in float v_stroke;

in float v_default;

uniform sampler2D u_texture;

out vec4 fragColor;

void main() {
    if (v_default == 1.0) {
        fragColor = v_color * texture(u_texture, v_texCoords);
        return;
    }

    vec2 st = v_texCoords.xy;
    //vec2 st = gl_FragCoord.xy / v_resolution;
    //st = ratio(st, v_resolution);

    //fragColor = vec4(1.0, 1.0, 0.0, 1.0) * rect(st, vec2(0.1, 0.4), 0.15);

    float x = st.x;
    float y = st.y;
    float stroke = v_stroke * 200. / v_resolution.x;
    float y_ratio = stroke * v_resolution.x/v_resolution.y;
    if (x <= stroke || x >= 1. - stroke || y <= y_ratio || y >= 1. - y_ratio) {
        fragColor = v_color;
        //fragColor = vec4(v_texCoords.x, v_texCoords.y, 0.0, 1.0);
    } else {
        fragColor = vec4(0.0);

    }

    //fragColor = vec4(st.x,st.y,0.0,1.0);

    //fragColor = v_color * rect(st, vec2(1.0, 1.0), 0.05);
}
