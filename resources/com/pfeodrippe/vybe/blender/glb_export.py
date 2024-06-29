import bpy
from bpy.app.handlers import persistent
from bpy.props import (StringProperty,
                       PointerProperty,
                       CollectionProperty,
                       )
from bpy.types import (Panel,
                       PropertyGroup
                       )
import os
from dataclasses import dataclass


# -- Export
@persistent
def VY__export_models(file):
    
    try:
        # https://docs.blender.org/api/current/bpy.ops.export_scene.html#bpy.ops.export_scene.gltf
        bpy.ops.export_scene.gltf(
            filepath=bpy.path.abspath("//models.glb"),
            export_format='GLB',
            use_active_collection=False,
            export_apply=True,
            use_renderable=True,
            use_visible=True,
            export_yup=True,
            export_extras=True,
            export_cameras=True,
            export_lights=True,
        )
    finally:
        pass
    
    
save_post_operators= [
    VY__export_models,
]

save_post_operators_names = [v.__name__ for v in save_post_operators]

# Remove existing (if any)
for f in bpy.app.handlers.save_post:
    if f.__name__ in save_post_operators_names:
        bpy.app.handlers.save_post.remove(f)

# Then add the operators
# https://docs.blender.org/api/current/bpy.app.handlers.html
for f in save_post_operators:
    bpy.app.handlers.save_post.append(f)
    
    
# -- Components panel
@dataclass
class MyString():
    value: any
    
global_obj = MyString(None)

class MyProperties(PropertyGroup):
    def get_name(self):
        #return str(self.idx)
        v = global_obj.value.get("vybe_" + str(self.idx))
        if v is None:
            return ""
        return v
    
    def set_name(self,value):
        if value.strip() is "":
            del global_obj.value["vybe_" + str(self.idx)]
        else:
            global_obj.value["vybe_" + str(self.idx)] = value
        
    vybe: StringProperty(
        name="",
        description="",
        default="",
        maxlen=1024,
        get=get_name,
        set=set_name,
    )
    idx: bpy.props.IntProperty()


class VIEW3D_PT_VY_components_panel(Panel):
    bl_space_type = "VIEW_3D"
    bl_region_type = "UI"
    
    bl_category = "Vybe Components"
    bl_label = "Vybe Components"
    bl_context = "objectmode"
    
    def draw(self, context):
        layout = self.layout
        scene = context.scene
        mytool = scene.my_tool
        
        global_obj.value = context.object
        
        #layout.row().label(text=str(mytool[0].idx))
        #mytool.idx += 1
        
        last_index = 0
        for i in reversed(range(1, 20)):
            v = context.object.get("vybe_" + str(mytool[i-1].idx))
            if v is not None:
                last_index = i
                break
        
        for i in range(0, last_index + 1):
            layout.prop(mytool[i], "vybe")
    
classes = (
    MyProperties,
    VIEW3D_PT_VY_components_panel,
)
    
def register_components_panel():
    for cls in classes:
        bpy.utils.register_class(cls)

    bpy.types.Scene.my_tool = CollectionProperty(type=MyProperties)
    for i in range(20):
        inst = bpy.context.scene.my_tool.add()
        bpy.context.scene.my_tool[i].idx = i
    
def unregister_components_panel():
    for cls in reversed(classes):
        bpy.utils.unregister_class(cls)
    del bpy.types.Scene.my_tool
    
if __name__ == "__main__":
    register_components_panel()