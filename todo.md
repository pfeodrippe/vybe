## Refs

- VAO, https://paroj.github.io/gltut/Basics/Tut01%20Following%20the%20Data.html
- Baked light map, https://gazebosim.org/api/rendering/5.0/lightmap.html
- GLTF spec, https://registry.khronos.org/glTF/specs/2.0/glTF-2.0.html
- GLTF SO question, https://stackoverflow.com/questions/55989429/understanding-the-skinning-part-of-a-gltf2-0-file-for-opengl-engine
- GLTF anim article, https://lisyarus.github.io/blog/posts/gltf-animation.html
- GLTF anim example, https://github.com/KhronosGroup/glTF-Tutorials/blob/main/gltfTutorial/gltfTutorial_019_SimpleSkin.md
- GLTF reference guide, https://www.khronos.org/files/gltf20-reference-guide.pdf
- GLTF code, https://github.com/KhronosGroup/glTF-Sample-Viewer/blob/d32ca25dc273c0b0982e29efcea01b45d0c85105/src/skin.js#L32-L36
- Animation blending/skin article, https://animationprogramming.com
- Supercollider PDF, https://cs.wellesley.edu/~cs203/lecture_materials/synthdefs/synthdefs.pdf
- Use rust lib with Panama, https://foojay.io/today/java-panama-polyglot-rust-part-4/
- JOLT
  - https://github.com/jrouwe/JoltPhysics/blob/master/HelloWorld/HelloWorld.cpp
  - https://github.com/zig-gamedev/zig-gamedev/blob/main/samples/physics_test_wgpu/src/physics_test_wgpu.zig#L321
  - https://github.com/aecsocket/jolt-java/blob/main/src/test/java/jolt/HelloJolt.java

## TODO

- [x] fix memory leak
- [x] solve lexical scope for with-each
- [x] fix already closed issue
- [ ] solve lexical scope for with-system

- [ ] deploy to clojars
  - [x] vybe
  - [ ] panama
  - [ ] raylib
  - [ ] flecs
- [ ] compile libs to targets
  - [ ] windows
  - [ ] linux
  - [ ] osx
    - [ ] universal?
- [ ] Use https://github.com/zeux/meshoptimizer
- [ ] debug arena by tracing calls (for memory leak)
- [ ] ability to apply locks when creating an VybeFlecsSetEntity
