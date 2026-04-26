#pragma once

#include "raylib.h"

Model VyLoadModelFromMemory(const unsigned char *data, int dataSize);
RenderTexture2D VyLoadRenderTexture(int width, int height);
RenderTexture2D VyLoadShadowmapRenderTexture(int width, int height);
void VySetShaderValueMatrixV(Shader shader, int locIndex, const Matrix *matrices, int count);
