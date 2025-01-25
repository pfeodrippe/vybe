#include "../raylib/src/raylib.h"
#include "../raylib/src/rlgl.h"
#include "../raylib/src/raymath.h"

#if !defined(_WIN32)
  #define RAYGUI_IMPLEMENTATION TRUE
#endif

// To prevent undefined symbol on Linux when loading raygui functions, we
// provide the text-related functions shown at
// https://github.com/raysan5/raygui/blob/54bff64d7dbaefea877b0b2e32323761fc5692f2/examples/standalone/raygui_custom_backend.h#L139
#if defined(WIN32) || defined(_WIN32) || defined(__WIN32__) || defined(__NT__)

#elif __APPLE__

#else
//-------------------------------------------------------------------------------
// Text required functions
//-------------------------------------------------------------------------------
// USED IN: GuiLoadStyleDefault()
RLAPI Font GetFontDefault(void)
{
    Font font = { 0 };

    // TODO: Return default rendering Font for the UI

    return font;
}

// USED IN: GetTextWidth()
RLAPI Vector2 MeasureTextEx(Font font, const char *text, float fontSize, float spacing)
{
    Vector2 size = { 0 };

    // TODO: Return text size (width, height) on screen depending on the Font, text, fontSize and spacing

    return size;
}

// USED IN: GuiDrawText()
RLAPI void DrawTextEx(Font font, const char *text, Vector2 position, float fontSize, float spacing, Color tint)
{
    // TODO: Draw text on the screen
}

//-------------------------------------------------------------------------------
// GuiLoadStyle() required functions
//-------------------------------------------------------------------------------
RLAPI Font LoadFontEx(const char *fileName, int fontSize, int *fontChars, int glyphCount)
{
    Font font = { 0 };

    // TODO: Load a new font from a file

    return font;
}

RLAPI char *LoadText(const char *fileName)
{
    // TODO: Load text file data, used by GuiLoadStyle() to load characters list required on Font generation,
    // this is a .rgs feature, probably this function is not required in most cases

    return ((void *)0);
}

RLAPI const char *GetDirectoryPath(const char *filePath)
{
    // TODO: Get directory path for .rgs file, required to look for a possible .ttf/.otf font file referenced,
    // this is a .rgs feature, probably this function is not required in most cases

    return ((void *)0);
}
#endif

#include "../raygui/src/raygui.h"
