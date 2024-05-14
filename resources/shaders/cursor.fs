#version 330

#include "lygia/draw/circle.glsl"
#include "lygia/space/ratio.glsl"

// Input vertex attributes (from vertex shader)
in vec2 fragTexCoord;
in vec4 fragColor;

// Input uniform values
uniform sampler2D texture0;
uniform vec4 colDiffuse;
uniform vec2 u_cursor;

// Output fragment color
out vec4 finalColor;

//uniform float u_time;
//uniform vec2 u_cursor;

void main() {        
    vec2 st = gl_FragCoord.xy/vec2(1200.0, 1200.0);
    st = ratio(st, vec2(1200.0, 1200.0));
    vec2 cursor_pos = st - u_cursor;    

    float cursor;

    cursor = circle(cursor_pos, 0.02);    

    if (cursor > 0.0) {    
        finalColor = vec4(0.0, 0.0, 0.0, 0.6);
    } else {
        finalColor = vec4(0.0, 0.0, 0.0, .0);
    }
}
