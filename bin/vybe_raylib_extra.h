#pragma once

#include "raylib.h"

Model VyLoadModelFromMemory(const unsigned char *data, int dataSize);
RenderTexture2D VyLoadShadowmapRenderTexture(int width, int height);
