#version 330

// Input vertex attributes
in vec3 vertexPosition;
in vec2 vertexTexCoord;
in vec4 vertexColor;
in vec3 vertexNormal;

layout(location = 6) in vec4 a_joint;
layout(location = 7) in vec4 a_weight;

// Output vertex attributes (to fragment shader)
out vec2 fragTexCoord;
out vec4 fragColor;
out vec3 fragNormal;

// Input uniform values
uniform mat4 mvp;
uniform mat4 matNormal;
uniform mat4 u_jointMat[19];

// NOTE: Add here your custom variables

void main()
{
    mat4 skinMat = mat4(1.0);

    if (a_weight.x > 0. || a_weight.y > 0. || a_weight.z > 0.) {
        //if (true) {
        skinMat = a_weight.x * u_jointMat[int(a_joint.x)] +
            a_weight.y * u_jointMat[int(a_joint.y)] +
            a_weight.z * u_jointMat[int(a_joint.z)] +
            a_weight.w * u_jointMat[int(a_joint.w)];
    }

    // Send vertex attributes to fragment shader
    fragTexCoord = vertexTexCoord;
    fragColor = vertexColor;
    fragNormal = normalize(vec3(matNormal*vec4(vertexNormal, 1.0)));

    // Calculate final vertex position
    gl_Position = mvp * (skinMat * vec4(vertexPosition, 1.0));
    //gl_Position = mvp * vec4(vertexPosition, 1.0);
}
