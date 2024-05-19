#include "../raylib/src/raylib.h"
#include "../raylib/src/rlgl.h"
#include "../raylib/src/raymath.h"

#define VY_GL_ACTIVE_UNIFORMS GL_ACTIVE_UNIFORMS
#define VY_GL_ACTIVE_ATTRIBUTES GL_ACTIVE_ATTRIBUTES

typedef struct VyModelMeta {
    unsigned char* name;
    char drawingDisabled;
} VyModelMeta;

typedef struct VyMesh {
    Mesh mesh;
    Vector3 translation;
    Vector4 rotation;
    Vector3 scale;
} VyMesh;

typedef struct VyModel {
    Model model;
    int metaCount;
    VyModelMeta *meta;
    VyMesh *meshes;
} VyModel;

// For supporting rotations from GLTF files.
typedef struct VyCamera {
    Camera camera;
    Quaternion rotation;
} VyCamera;

typedef struct VyShaderParameter {
    char* name;
    int type;
    int size;
    int loc;
} VyShaderParameter;

typedef struct VyShaderParameters {
    int uniformsCount;
    VyShaderParameter uniforms[256];

    int attributesCount;
    VyShaderParameter attributes[256];

    int shaderId;
} VyShaderParameters;

void VyDrawModel(VyModel vyModel, Vector3 position, float scale, Color tint);
void VyDrawModelEx(VyModel vyModel, Vector3 position, Vector3 rotationAxis, float rotationAngle, Vector3 scale, Color tint);
void VyDrawModelExQuat(VyModel vyModel, Vector3 position, Quaternion quaternion, Vector3 scale, Color tint);
void VyBeginMode3D(VyCamera camera);
float VyQuaternionToAxisAngle(Quaternion q);
Vector3 VyQuaternionToAxisVector(Quaternion q);

VyShaderParameters VyGlGetActiveParameters(int id);
int VyGlGetActiveUniformsCount(int id);
int VyGlGetActiveAttributesCount(int id);

void *malloc(unsigned long size);
unsigned long strlen(const char *str);
char* strcpy(char* destination, const char* source);

void glGetProgramiv(	int program,
                        int pname,
                        int *params);
void glGetActiveUniform(	int program,
                                int index,
                                int bufSize,
                                int *length,
                                int *size,
                                int *type,
                                char  *name);

void glGetActiveAttrib(	int program,
                        int index,
                        int bufSize,
                        int *length,
                        int *size,
                        int *type,
                        char *name);
int glGetUniformLocation(	int program,
                                const char *name);
int glGetAttribLocation(	int program,
                                const char *name);


VyShaderParameters VyGlGetActiveParameters(int id)
{
    int namelen = -1;
    int size = -1;
    char name[256] = { 0 };     // Assume no variable names longer than 256
    int type = 0;
    VyShaderParameters vyparameters = { 0 };
    vyparameters.shaderId = id;

    int count = VyGlGetActiveUniformsCount(id);
    vyparameters.uniformsCount = count;

    for (int i = 0; i < count; i++)
    {
        glGetActiveUniform(id, i, sizeof(name) - 1, &namelen, &size, &type, name);
        name[namelen] = 0;

        vyparameters.uniforms[i].name = (char *)malloc(strlen(name)+1);
        strcpy(vyparameters.uniforms[i].name,name);
        vyparameters.uniforms[i].type = type;
        vyparameters.uniforms[i].size = size;
        vyparameters.uniforms[i].loc = glGetUniformLocation(id, name);
    }

    count = VyGlGetActiveAttributesCount(id);
    vyparameters.attributesCount = count;

    for (int i = 0; i < count; i++)
    {
        glGetActiveAttrib(id, i, sizeof(name) - 1, &namelen, &size, &type, name);
        name[namelen] = 0;

        vyparameters.attributes[i].name = (char *)malloc(strlen(name)+1);
        strcpy(vyparameters.attributes[i].name,name);
        vyparameters.attributes[i].type = type;
        vyparameters.uniforms[i].size = size;
        vyparameters.uniforms[i].loc = glGetAttribLocation(id, name);
    }

    return vyparameters;
}

int VyGlGetActiveUniformsCount(int id)
{
    int count;
    glGetProgramiv(id, 0x8B86, &count);

    return count;
}

int VyGlGetActiveAttributesCount(int id)
{
    int count;
    glGetProgramiv(id, 0x8B89, &count);

    return count;
}

void VyDrawModel(VyModel vyModel, Vector3 position, float scale, Color tint)
{
    Vector3 vScale = { scale, scale, scale };
    Vector3 rotationAxis = { 0.0f, 1.0f, 0.0f };

    VyDrawModelEx(vyModel, position, rotationAxis, 0.0f, vScale, tint);
}

void VyDrawModelEx(VyModel vyModel, Vector3 position, Vector3 rotationAxis, float rotationAngle, Vector3 scale, Color tint)
{
    VyDrawModelExQuat(vyModel, position, QuaternionFromAxisAngle(rotationAxis, rotationAngle), scale, tint);
}

void VyDrawModelExQuat(VyModel vyModel, Vector3 position, Quaternion quaternion, Vector3 scale, Color tint)
{
    Model model = vyModel.model;
    // Calculate transformation matrix from function parameters
    // Get transform matrix (rotation -> scale -> translation)
    Matrix matScale = MatrixScale(scale.x, scale.y, scale.z);
    Matrix matRotation = QuaternionToMatrix(quaternion);
    Matrix matTranslation = MatrixTranslate(position.x, position.y, position.z);

    Matrix matTransform = MatrixMultiply(MatrixMultiply(matScale, matRotation), matTranslation);

    // Combine model transformation matrix (model.transform) with matrix generated by function parameters (matTransform)
    model.transform = MatrixMultiply(model.transform, matTransform);

    for (int i = 0; i < model.meshCount; i++)
    {
        if (i < vyModel.metaCount) {
            VyModelMeta meta = vyModel.meta[i];
            if (meta.drawingDisabled == 1) {
                continue;
            }
        }

        Color color = model.materials[model.meshMaterial[i]].maps[MATERIAL_MAP_DIFFUSE].color;

        Color colorTint = WHITE;
        colorTint.r = (unsigned char)((((float)color.r/255.0f)*((float)tint.r/255.0f))*255.0f);
        colorTint.g = (unsigned char)((((float)color.g/255.0f)*((float)tint.g/255.0f))*255.0f);
        colorTint.b = (unsigned char)((((float)color.b/255.0f)*((float)tint.b/255.0f))*255.0f);
        colorTint.a = (unsigned char)((((float)color.a/255.0f)*((float)tint.a/255.0f))*255.0f);

        model.materials[model.meshMaterial[i]].maps[MATERIAL_MAP_DIFFUSE].color = colorTint;
        DrawMesh(model.meshes[i], model.materials[model.meshMaterial[i]], model.transform);
        model.materials[model.meshMaterial[i]].maps[MATERIAL_MAP_DIFFUSE].color = color;
    }
}

void VyBeginMode3D(VyCamera vyCamera)
{
    Camera camera = vyCamera.camera;
    rlDrawRenderBatchActive();      // Update and draw internal render batch

    rlMatrixMode(RL_PROJECTION);    // Switch to projection matrix
    rlPushMatrix();                 // Save previous matrix, which contains the settings for the 2d ortho projection
    rlLoadIdentity();               // Reset current matrix (projection)

    float aspect = GetCurrentAspect();

    // NOTE: zNear and zFar values are important when computing depth buffer values
    if (camera.projection == CAMERA_PERSPECTIVE)
    {
        // Setup perspective projection
        double top = rlGetCullDistanceNear()*tan(camera.fovy*0.5*DEG2RAD);
        double right = top*aspect;

        rlFrustum(-right, right, -top, top, rlGetCullDistanceNear(), rlGetCullDistanceFar());
    }
    else if (camera.projection == CAMERA_ORTHOGRAPHIC)
    {
        // Setup orthographic projection
        double top = camera.fovy/2.0;
        double right = top*aspect;

        rlOrtho(-right, right, -top,top, rlGetCullDistanceNear(), rlGetCullDistanceFar());
    }

    rlMatrixMode(RL_MODELVIEW);     // Switch back to modelview matrix
    rlLoadIdentity();               // Reset current matrix (modelview)

    // Setup Camera view
    Quaternion q = QuaternionInvert(vyCamera.rotation);
    Matrix matView = MatrixMultiply(MatrixTranslate(-camera.position.x, -camera.position.y, -camera.position.z),
                                    MatrixRotate(VyQuaternionToAxisVector(q), VyQuaternionToAxisAngle(q)));
    rlMultMatrixf(MatrixToFloat(matView));      // Multiply modelview matrix by view matrix (camera)

    rlEnableDepthTest();            // Enable DEPTH_TEST for 3D
}

// Get the rotation angle and axis for a given quaternion
float VyQuaternionToAxisAngle(Quaternion q)
{
    if (fabsf(q.w) > 1.0f)
    {
        // QuaternionNormalize(q);
        float length = sqrtf(q.x*q.x + q.y*q.y + q.z*q.z + q.w*q.w);
        if (length == 0.0f) length = 1.0f;
        float ilength = 1.0f/length;

        q.x = q.x*ilength;
        q.y = q.y*ilength;
        q.z = q.z*ilength;
        q.w = q.w*ilength;
    }

    float resAngle = 2.0f*acosf(q.w);

    return resAngle;
}

Vector3 VyQuaternionToAxisVector(Quaternion q)
{
    if (fabsf(q.w) > 1.0f)
    {
        // QuaternionNormalize(q);
        float length = sqrtf(q.x*q.x + q.y*q.y + q.z*q.z + q.w*q.w);
        if (length == 0.0f) length = 1.0f;
        float ilength = 1.0f/length;

        q.x = q.x*ilength;
        q.y = q.y*ilength;
        q.z = q.z*ilength;
        q.w = q.w*ilength;
    }

    Vector3 resAxis = { 0.0f, 0.0f, 0.0f };
    float den = sqrtf(1.0f - q.w*q.w);

    if (den > EPSILON)
    {
        resAxis.x = q.x/den;
        resAxis.y = q.y/den;
        resAxis.z = q.z/den;
    }
    else
    {
        // This occurs when the angle is zero.
        // Not a problem: just set an arbitrary normalized axis.
        resAxis.x = 1.0f;
    }

    return resAxis;
}
