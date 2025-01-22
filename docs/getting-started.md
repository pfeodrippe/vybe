---
title: Vybe Starter
---

!!! warning READ THIS 
    Use the template at  <https://github.com/pfeodrippe/vybe-games> as your start point, try to run the commands in that README. If you try to use the `vybe` github project directly, you would have to build everything from scratch, but the deps used in `vybe-games` use the clojars version, which contains all the dynamic libs you need. Have fun \o/

# Getting Started

This is the minimal amount of code to have physics (with Jolt) + ECS (with Flecs) +
rendering (with Raylib) using Vybe. We will talk about each of the parts later,
check the code in full below.

``` clojure
--8<-- "test/minimal.clj"
```

## `draw`

The `draw` function receives a world (more on it later)
and a delta time (time since the last iteration). You won't call
this function directly, the Vybe runtime will be calling it for you.

``` clojure
--8<-- "test/minimal.clj:flecs_physics"
```

We are calling `vf/progress`, which will advance
the Flecs (<https://www.flecs.dev/flecs/>) ECS system (`w` is a Flecs
world), Flecs is a well-written C engine and the core of the Vybe
framework. `vg/physics-update!` handles the physics using Jolt
(<https://jrouwe.github.io/JoltPhysics/>), a C++ physics engine (we
use a C wrapper to interact with it).

``` clojure
--8<-- "test/minimal.clj:rendering"
```

Backed by Raylib (<https://www.raylib.com/>, we are able to draw some
simple lightning (using a custom shader, see the implementation),
apply some effects to the screen (`vg/with-drawing-fx` will render the
result to a render texture and draw it into the screen,
`vg/with-drawing` is the version of it that doesn't apply any
effect).

The namespaces with a 3rd party lib + `.c` (e.g. `vybe.raylib.c`,
`vybe.flecs.c`, `vybe.jolt.c`) are clj wrappers for the respective libs,
e.g. `vr.c/clear-background` will call raylib's `ClearBackground`
(cheatsheet for raylib at
<https://www.raylib.com/cheatsheet/cheatsheet.html>). We use jextract
(<https://github.com/openjdk/jextract>) for these native libs and then
read the generated Java classes, it's a fantastic tool made by the
same people who have worked in the Panama project.

After clearing the background, we have that `vf/with-query` there,
what is it for? We will go more in depth in one of the Flecs sections,
but it's iterating over all of the ECS entities that have a
`:vg/camera-active` tag (an identifier, here managed by Vybe) and that have a
`vt/Camera` component (components are like structs types in C, from CLJ
we have it reified and can inspect it type, construct an instance for
it, generating a native MemorySegment). A query can have as many terms
(a term is a binding) as you need, and we have 2 in the case here, we
use the retrieved camera (which is the active one and can there only
be one in any given time) and call game/raylib related functions to
draw a scece using the camera data.

Finally, we draw the FPS using raylib's `DrawFPS`.

Yeah, I know, there is a lot to unpack here, but you will see that
things integrate with each other very well and with CLJ.

## `init`

This function will setup the world, the windows size, pass the `draw`
var to Vybe's runtime and have a initial function that's called from
the main thread.

!!! note "Main thead"
    It's important that you initiate any raylib's functionality,
    specially drawing-related, inside this initial function as we may
    have (specially in OSX) some issues related to graphics being
    rendered outside the main thread.

The initial function is loading a GLTF model (`.glb`  is the binary
version of it), you can generate a `.glb` from Blender, for example,
it should load things correctly, try it!
