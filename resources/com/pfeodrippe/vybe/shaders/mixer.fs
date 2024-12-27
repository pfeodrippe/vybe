#version 330

in vec2 fragTexCoord;
in vec4 fragColor;

// Input uniform values
uniform sampler2D texture0;
uniform sampler2D texture1;
uniform vec4 colDiffuse;
uniform float edge_fill;

// Output fragment color
out vec4 finalColor;

void main (void) {
    vec2 st = fragTexCoord;
    // Higher radius is like an out of focus effect.
    float radius = 1.1;

    float c = 1.0;bool trigger =
        st.x * st.y < sin(edge_fill * 0.87 * c) * cos(edge_fill * 0.23 * c) + sin(edge_fill * 0.37 * c) * cos(edge_fill * 0.53 * c)
        //st.x > sin(edge_fill * 0.24) * 1.0 && st.x < sin(edge_fill * 0.87 * c) &&
        //st.y > sin(edge_fill * 0.41 * c) * 0.4 && st.y < sin(edge_fill * 0.46 * c)
        ;

    if (trigger) {
        // Texel color fetching from texture sampler
        vec4 texelColor = texture(texture1, fragTexCoord);

        finalColor = texelColor*colDiffuse*fragColor;
    } else {
        // Texel color fetching from texture sampler
        vec4 texelColor = texture(texture0, fragTexCoord);

        finalColor = texelColor*colDiffuse*fragColor;
    }
}
