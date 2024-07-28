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
uniform vec3 lightDirs[10];
uniform int lightsCount;

uniform vec4 lightColor;
uniform vec4 ambient;
uniform vec3 viewPos;

// Input shadowmapping values
uniform mat4 lightVPs[10]; // Light source view-projection matrix
uniform sampler2D shadowMaps[10];


uniform int shadowMapResolution;


//#define LIGHT_DIRECTION     lightDir
//#define LIGHT_COLOR     lightColor
#define LIGHT_SHADOWMAP shadowMap
#define LIGHT_SHADOWMAP_SIZE 600
#define LIGHT_INTENSITY 0.3

#include "lygia/color/space/linear2gamma.glsl"
#include "lygia/lighting/pbr.glsl"
#include "lygia/lighting/material/new.glsl"
#include "lygia/sample/shadowPCF.glsl"

void main3() {
    vec4 color = colDiffuse;
    vec4 texelColor = texture(texture0, fragTexCoord);

    Material material = materialNew();

    material.albedo.rgb = texelColor.rgb * colDiffuse.rgb;
    material.normal = fragNormal;
    material.position = fragPosition;
    material.roughness = 1.0;
    //material.metallic= 1.0;

    color = pbr(material);
    color = linear2gamma(color);

    finalColor = color;
}

void main10() {
    vec4 color = colDiffuse;
    vec2 pixel = 1.0/vec2(600., 600.);
    vec2 st = gl_FragCoord.xy * pixel;
    vec2 uv = st;

    //color = texture(texture0, fragTexCoord);

    // Diffuse shading
    vec3 n = normalize(fragNormal);
    vec3 l = normalize(lightDirs[0]);

    color.rgb *= (dot(n, l) + 1.0 ) * 0.5;

    // Shadow
    color.rgb *= sampleShadowPCF(shadowMaps[0], vec2(shadowMapResolution), lightDirs[0].xy, lightDirs[0].z - 0.005) * 0.8 + 0.2;

    finalColor = color;
}

vec4 apply_light(vec4 texelColor, vec3 normal, vec3 viewD,
                 vec3 lightDir, mat4 lightVP, sampler2D shadowMap) {
    vec3 lightDot = vec3(0.0);
    vec3 specular = vec3(0.0);

    vec3 l = -lightDir;

    float NdotL = max(dot(normal, l), 0.0);
    lightDot += lightColor.rgb*NdotL;

    float specCo = 0.0;
    if (NdotL > 0.0) specCo = pow(max(0.0, dot(viewD, reflect(-(l), normal))), 16.0); // 16 refers to shine
    specular += specCo;

    vec4 finalColor = (texelColor*((colDiffuse + vec4(specular, 1.0))*vec4(lightDot, 1.0)));

    // Shadow calculations
    vec4 fragPosLightSpace = lightVP * vec4(fragPosition, 1);
    fragPosLightSpace.xyz /= fragPosLightSpace.w; // Perform the perspective division
    fragPosLightSpace.xyz = (fragPosLightSpace.xyz + 1.0f) / 2.0f; // Transform from [-1, 1] range to [0, 1] range
    vec2 sampleCoords = fragPosLightSpace.xy;
    float curDepth = fragPosLightSpace.z;
    // Slope-scale depth bias: depth biasing reduces "shadow acne" artifacts, where dark stripes appear all over the scene.
    // The solution is adding a small bias to the depth
    // In this case, the bias is proportional to the slope of the surface, relative to the light
    float bias = max(0.00001 * (1.0 - dot(normal, l)), 0.00002) + 0.00001;
    int shadowCounter = 0;
    const int numSamples = 12;
    // PCF (percentage-closer filtering) algorithm:
    // Instead of testing if just one point is closer to the current point,
    // we test the surrounding points as well.
    // This blurs shadow edges, hiding aliasing artifacts.
    vec2 texelSize = vec2(1.0f / float(shadowMapResolution));
    for (int x = -1; x <= 1; x++)
    {
        for (int y = -1; y <= 1; y++)
        {
            float sampleDepth = texture(shadowMap, sampleCoords + texelSize * vec2(x, y)).r;
            if (curDepth - bias > sampleDepth)
            {
                shadowCounter++;
            }
        }
    }
    return mix(finalColor, vec4(0, 0, 0, 1), float(shadowCounter) / float(numSamples));
}

void main()
{
    // Texel color fetching from texture sampler
    vec4 texelColor = texture(texture0, fragTexCoord);
    vec3 normal = normalize(fragNormal);
    vec3 viewD = normalize(viewPos - fragPosition);

    finalColor = vec4(0, 0, 0, 1);

    for (int i = 0; i < lightsCount; i++){
        // We use a switch here to avoid the
        // "sampler arrays indexed with non-constant expressions are forbidden in GLSL 1.30 and later" error
        switch (i) {
        case 0:
            finalColor += apply_light(texelColor, normal, viewD, lightDirs[i], lightVPs[i], shadowMaps[0]);
            break;
        case 1:
            finalColor += apply_light(texelColor, normal, viewD, lightDirs[i], lightVPs[i], shadowMaps[1]);
            break;
        case 2:
            finalColor += apply_light(texelColor, normal, viewD, lightDirs[i], lightVPs[i], shadowMaps[2]);
            break;
        case 3:
            finalColor += apply_light(texelColor, normal, viewD, lightDirs[i], lightVPs[i], shadowMaps[3]);
            break;
        case 4:
            finalColor += apply_light(texelColor, normal, viewD, lightDirs[i], lightVPs[i], shadowMaps[4]);
            break;
        case 5:
            finalColor += apply_light(texelColor, normal, viewD, lightDirs[i], lightVPs[i], shadowMaps[5]);
            break;
        case 6:
            finalColor += apply_light(texelColor, normal, viewD, lightDirs[i], lightVPs[i], shadowMaps[6]);
            break;
        case 7:
            finalColor += apply_light(texelColor, normal, viewD, lightDirs[i], lightVPs[i], shadowMaps[7]);
            break;
        case 8:
            finalColor += apply_light(texelColor, normal, viewD, lightDirs[i], lightVPs[i], shadowMaps[8]);
            break;
        case 9:
            finalColor += apply_light(texelColor, normal, viewD, lightDirs[i], lightVPs[i], shadowMaps[9]);
            break;
        }
    }

    // Add ambient lighting whether in shadow or not
    finalColor += texelColor*(ambient/1.0)*colDiffuse;

    // Gamma correction
    finalColor = pow(finalColor, vec4(1.0/2.2));

    //finalColor = vec4(normal, 1.0);
}
