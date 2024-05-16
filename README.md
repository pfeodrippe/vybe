## Vybe

A Clojure framework for game dev (very WIP, I am still working on a small game
using it).

## Getting started

See <https://github.com/pfeodrippe/vybe-example> for a example project.

## Help wanted

If possible, check `bin/jextract-libs.sh` and open a PR to compile to your OS =D

## What does it include?

### `vybe.panama`

Its dependency is only potemkin, it's general purpose, not game related, it
defines (we will be using the `vp` alias for this namespace):

- VybeComponent
  - Components are the equivalent of C structs (or union, but we didn't need it yet),
    you define the schemas by using Malli-like syntax
  - Use `vp/defcomp` to create new components globally or `vp/make-component` to create
    them in a local scope
  - See usage in `vybe.game`
- VybePMap
  - It represents Panama's memory segments using Clojure's hash map abstraction
  - You can use `assoc`, `dissoc` etc to modify memory segments in place, we are in a mutable world
  - Again, it's mutable!
- VybePSeq
  - Similar to `VybePMap`, but its goal is to abstract sequences
  - Use `vp/arr` to create one of these

Besides some of the above types, it has support to generate instances or components
for jextract-generated classes. See more about jextract at <https://github.com/openjdk/jextract/tree/master/doc>.

This namespace could easily be a lib on its own.

### `vybe.raylib`

We use `jextract` to generate java classes in the `org.vybe.raylib` package. In
`vybe.raylib.c` + `vybe.raylib.impl`, we read these generated classes and generate
vars for them, you will be able to see the expected and return types for each of
them, very convenient (convenience is one of the goals of `vybe` BTW).

Raylib is a C game lib that has lots and lots of utility function for a game, see
<https://www.raylib.com>.

### `vybe.flecs`

Similar to `vybe.raylib`, we also use `jextract` and have the equivalent `c` and
`impl` namespaces.

Flecs is a Entity Component System (ECS), there is a lot to it, see
<https://www.flecs.dev/flecs/index.html>. But again, we have tried to reuse
most of the concept we already know in Clojure (maps, maps, maps!!!), so you can
also use them here. It composes very well with the `VybePMap` map type, accessing
a pointer never was so convenient!

There are so many new concepts in Flecs though, somo ECS ones like systems, but others
that were basically introduced by Flecs (e.g. relationships, with `vybe.flecs` you
can represent these with just a vector pair `[A B]`). It's mind blowing, read their
docS!

### `vybe.game`

Here is where we put everything together, we have some default systems (e.g. for
transforms supporting hierarchy using Flecs), drawing functions (Flecs + Raylib),
even a GLTF (GLB really) loader with hot reload support... just like we are used
in Clojure.

## Requirements

- Java 22 (at least)
- I've only tested on a ARM macbook (please open a PR adding it to your OS)
  - Although it shouldn't be too difficult for someone to add support for other OSs
    as both Flecs and Raylib work with Windows/Linux
