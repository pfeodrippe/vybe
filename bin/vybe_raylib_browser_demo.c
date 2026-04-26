#include "raylib.h"
#include <emscripten/emscripten.h>
#include <math.h>

static Color bg = { 16, 16, 20, 255 };
static Color fg = { 232, 226, 207, 255 };
static Color accent = { 48, 190, 160, 255 };
static Color dim = { 120, 126, 136, 255 };

static void frame(void) {
    float t = (float)GetTime();
    int x = 300 + (int)(cosf(t * 1.7f) * 120.0f);
    int y = 320 + (int)(sinf(t * 2.3f) * 85.0f);

    BeginDrawing();
    ClearBackground(bg);
    DrawRectangle(40, 40, 260, 120, accent);
    DrawText("Vybe Raylib Wasm desktop window", 46, 44, 24, fg);
    DrawText("WebGL-backed, no project dylib", 46, 78, 18, dim);
    DrawCircle(x, y, 44.0f, accent);
    DrawFPS(500, 560);
    EndDrawing();
}

int main(void) {
    SetConfigFlags(FLAG_MSAA_4X_HINT);
    InitWindow(600, 600, "Vybe Raylib Wasm");
    SetTargetFPS(60);
    emscripten_set_main_loop(frame, 0, 1);
    return 0;
}
