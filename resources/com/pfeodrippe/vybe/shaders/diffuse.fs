#version 330

// Draw diffuse color to output. Useful for debugging and for
// using RenderTexture textures in other shaders.

// Input vertex attributes (from vertex shader)
in vec2 fragTexCoord;
in vec4 fragColor;

// Input uniform values
uniform sampler2D texture0;
uniform vec4 colDiffuse;

// Output fragment color
out vec4 finalColor;

void main()
{
    vec4 texelColor = texture(texture0, fragTexCoord);
    finalColor = colDiffuse;
    finalColor.a = texelColor.a;
}
