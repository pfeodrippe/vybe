#include <string.h>
#include "raylib.h"
#include "rlgl.h"
#if defined(PLATFORM_WEB)
#include <GLES3/gl3.h>
#endif

static const unsigned char *vybe_file_data = NULL;
static int vybe_file_data_size = 0;

static unsigned char *VyLoadFileDataCallback(const char *fileName, int *dataSize)
{
    (void)fileName;
    if ((vybe_file_data == NULL) || (vybe_file_data_size <= 0)) {
        *dataSize = 0;
        return NULL;
    }

    unsigned char *copy = (unsigned char *)MemAlloc((unsigned int)vybe_file_data_size);
    if (copy == NULL) {
        *dataSize = 0;
        return NULL;
    }

    memcpy(copy, vybe_file_data, (size_t)vybe_file_data_size);
    *dataSize = vybe_file_data_size;
    return copy;
}

Model VyLoadModelFromMemory(const unsigned char *data, int dataSize)
{
    vybe_file_data = data;
    vybe_file_data_size = dataSize;
    SetLoadFileDataCallback(VyLoadFileDataCallback);

    Model model = LoadModel("vybe_model.glb");

    SetLoadFileDataCallback(NULL);
    vybe_file_data = NULL;
    vybe_file_data_size = 0;
    return model;
}

static unsigned int VyLoadDepthTextureWebGL2(int width, int height)
{
#if defined(GRAPHICS_API_OPENGL_ES3)
    unsigned int id = 0;

    glGenTextures(1, &id);
    glBindTexture(GL_TEXTURE_2D, id);
    glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT24, width, height, 0,
                 GL_DEPTH_COMPONENT, GL_UNSIGNED_INT, NULL);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
    glBindTexture(GL_TEXTURE_2D, 0);

    return id;
#else
    return rlLoadTextureDepth(width, height, false);
#endif
}

static RenderTexture2D VyLoadRenderTextureWithDepthTexture(int width, int height)
{
    RenderTexture2D target = {0};

    target.id = rlLoadFramebuffer();
    target.texture.width = width;
    target.texture.height = height;

    if (target.id > 0) {
        rlEnableFramebuffer(target.id);

        target.texture.id = rlLoadTexture(NULL, width, height,
                                          PIXELFORMAT_UNCOMPRESSED_R8G8B8A8, 1);
        target.texture.width = width;
        target.texture.height = height;
        target.texture.format = PIXELFORMAT_UNCOMPRESSED_R8G8B8A8;
        target.texture.mipmaps = 1;

        target.depth.id = VyLoadDepthTextureWebGL2(width, height);
        target.depth.width = width;
        target.depth.height = height;
        target.depth.format = 19;
        target.depth.mipmaps = 1;

        rlFramebufferAttach(target.id, target.texture.id,
                            RL_ATTACHMENT_COLOR_CHANNEL0,
                            RL_ATTACHMENT_TEXTURE2D, 0);
        rlFramebufferAttach(target.id, target.depth.id,
                            RL_ATTACHMENT_DEPTH,
                            RL_ATTACHMENT_TEXTURE2D, 0);

        if (!rlFramebufferComplete(target.id)) target.id = 0;
        rlDisableFramebuffer();
    }

    return target;
}

RenderTexture2D VyLoadRenderTexture(int width, int height)
{
    return VyLoadRenderTextureWithDepthTexture(width, height);
}

RenderTexture2D VyLoadShadowmapRenderTexture(int width, int height)
{
    return VyLoadRenderTextureWithDepthTexture(width, height);
}

void VySetShaderValueMatrixV(Shader shader, int locIndex, const Matrix *matrices, int count)
{
#if defined(GRAPHICS_API_OPENGL_ES3)
    if ((locIndex > -1) && (matrices != NULL) && (count > 0)) {
        float values[count * 16];

        for (int i = 0; i < count; i++) {
            const Matrix mat = matrices[i];
            float *dst = values + (i * 16);

            dst[0] = mat.m0;
            dst[1] = mat.m1;
            dst[2] = mat.m2;
            dst[3] = mat.m3;
            dst[4] = mat.m4;
            dst[5] = mat.m5;
            dst[6] = mat.m6;
            dst[7] = mat.m7;
            dst[8] = mat.m8;
            dst[9] = mat.m9;
            dst[10] = mat.m10;
            dst[11] = mat.m11;
            dst[12] = mat.m12;
            dst[13] = mat.m13;
            dst[14] = mat.m14;
            dst[15] = mat.m15;
        }

        rlEnableShader(shader.id);
        glUniformMatrix4fv(locIndex, count, false, values);
    }
#else
    (void)shader;
    (void)locIndex;
    (void)matrices;
    (void)count;
#endif
}
