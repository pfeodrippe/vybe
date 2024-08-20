#include "../raylib/src/raylib.h"
#include "../raylib/src/rlgl.h"
#include "../raylib/src/raymath.h"

#if !defined(_WIN32)
  #define RAYGUI_IMPLEMENTATION TRUE
#endif

#include "../raygui/src/raygui.h"

#if defined(_WIN32)
RAYGUIAPI void GuiLoadStyleCherry(void);
RAYGUIAPI void GuiLoadStyleTerminal(void);
RAYGUIAPI void GuiLoadStyleSunny(void);
RAYGUIAPI void GuiLoadStyleCandy(void);
RAYGUIAPI void GuiLoadStyleAshes(void);
RAYGUIAPI void GuiLoadStyleEnefet(void);
#endif
