#version 330

// Input vertex attributes
in vec3 vertexPosition;
in vec2 vertexTexCoord;
in vec3 vertexNormal;
in vec4 vertexColor;

layout(location = 6) in vec4 a_joint;
layout(location = 7) in vec4 a_weight;

// Input uniform values
uniform mat4 mvp;
uniform mat4 matModel;
uniform mat4 matNormal;
uniform mat4 u_jointMat[19];
uniform float u_time;

// Output vertex attributes (to fragment shader)
out vec3 fragPosition;
out vec2 fragTexCoord;
out vec4 fragColor;
out vec3 fragNormal;

// NOTE: Add here your custom variables

float random(vec2 st) {
    return fract(sin(dot(st.xy,
                         vec2(12.9898,78.233)))*
                 43758.5453123);
}

float noise(in vec2 st) {
    vec2 i = floor(st);
    vec2 f = fract(st);

    // Four corners in 2D of a tile
    float a = random(i);
    float b = random(i + vec2(1.0, 0.0));
    float c = random(i + vec2(0.0, 1.0));
    float d = random(i + vec2(1.0, 1.0));

    // Smooth Interpolation

    // Cubic Hermine Curve.  Same as SmoothStep()
    vec2 u = f*f*(3.0-2.0*f);
    // u = smoothstep(0.,1.,f);

    // Mix 4 coorners percentages
    return mix(a, b, u.x) +
        (c - a)* u.y * (1.0 - u.x) +
        (d - b) * u.x * u.y;
}

void main()
{

    mat4 skinMat = mat4(1.0);

    if (a_weight.x > 0. || a_weight.y > 0. || a_weight.z > 0.) {
    //if (false) {
        //if (true) {
        skinMat = a_weight.x * u_jointMat[int(a_joint.x)] +
            a_weight.y * u_jointMat[int(a_joint.y)] +
            a_weight.z * u_jointMat[int(a_joint.z)] +
            a_weight.w * u_jointMat[int(a_joint.w)];
    }

    // Send vertex attributes to fragment shader
    fragPosition = vec3(matModel * skinMat * vec4(vertexPosition, 1.0));
    fragTexCoord = vertexTexCoord;
    fragColor = vertexColor;
    fragNormal = normalize(vec3(matNormal * transpose(inverse(skinMat)) * vec4(vertexNormal, 1.0)));

    // vec3 v = vertexPosition;
    // float factor = 1.;
    // float mul = 10.;
    // v.x += -0.1 * noise(vertexTexCoord) * sin(u_time * factor * 2 + 19) * mul;
    // v.y += -0.02 * noise(vertexTexCoord) * sin(u_time * factor * 3 +2) * mul;
    // v.z += 0.1 * noise(vertexTexCoord) * sin(u_time * factor * 5 + 42) * mul;

    // Calculate final vertex position
    gl_Position = mvp * skinMat * vec4(vertexPosition, 1.0);
}
