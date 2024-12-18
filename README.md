[![Clojars Project](https://img.shields.io/clojars/v/io.github.pfeodrippe/vybe.svg)](https://clojars.org/io.github.pfeodrippe/vybe)

## Vybe

A Clojure framework for game dev (very WIP, I am still working on a small game
using it).

## Packages

We have a Github Actions pipeline that builds, tests and publishes to Clojars
for each OS, each has a suffix for its version, check below:

- OSX universal
  - `io.github.pfeodrippe/vybe {:mvn/version "0.7.444-macos-universal"}`
- Linux (x64)
  - `io.github.pfeodrippe/vybe {:mvn/version "0.7.444-linux-x64"}`
- Windows (x64)g
  - `io.github.pfeodrippe/vybe {:mvn/version "0.7.444-win-x64"}`

## Getting started

Go to <https://github.com/pfeodrippe/vybe-games> and start the REPL for this project using
the following instructions:

``` shell
# This will put the dynamic libs in the right place and start raylib in the main thread,
# open the REPL and call call the `init` function inside `leo.clj`.

# Linux (x64)
clj -M:linux -m vybe.native.loader && clj -M:linux -m vybe.raylib

# Mac (Universal)
clj -M:osx -m vybe.native.loader && clj -M:osx -m vybe.raylib

# Windows (x64)
clj -M:win -m vybe.native.loader && clj -M:win -m vybe.raylib
```

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

### `vybe.jolt`

Physics! See <https://jrouwe.github.io/JoltPhysics/>.

### `vybe.game`

Here is where we put everything together, we have some default systems (e.g. for
transforms supporting hierarchy using Flecs), drawing functions (Flecs + Raylib),
even a GLTF (GLB really) loader with hot reload support... just like we are used
in Clojure.

### `vybe.network`

Includes helpers so you can connect to another computer (P2P), it tries UDP hole
punching using a server I've setup in Digital Ocean (it's only for starting the
connection, not for persistent communication) so the peers can know each other's
IP.

We use https://github.com/pfeodrippe/cute_headers/blob/master/cute_net.h to have
encrypted UDP packets and the options to send some packet reliably (similar to TCP,
but without its overhead). Read the referred link.

## Requirements

- Java 22 (at least)
