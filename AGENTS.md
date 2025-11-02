Build and test

- Install deps: clj (tools.deps), Zig v0.13.0 required for native bits.
- Build native libs and jar: bin/jextract-libs.sh && clj -T:build compile-app && clj -T:build jar
- Run all Clojure tests (Kaocha): clj -M:test
- Run a single test file (Kaocha): clj -M:test test/vybe/flecs_test.clj
- Run OS-specific dev profile: clj -M:osx:dev -m vybe.raylib (change alias: :linux, :win)

Lint & format

- Lint with clj-kondo: clj-kondo --lint "src,src-java,test" (install clj-kondo separately)
- Format with zprint or cljfmt if available: zprint :reformat src test

Code style (for agents)

- Namespace and imports: require namespaces with aliases and qualify long names. Keep :require lists grouped and aligned.
- Naming: use kebab-case for functions and vars, PascalCase for records/protocol types if needed, UPPER_SNAKE for constants only when interoping with C.
- Types & specs: annotate public functions with simple docstrings; prefer Malli/schema or clojure.spec for complex data shapes when present.
- Small functions: prefer small, pure functions. Keep side-effects isolated (init/start/! functions clearly mark mutation).
- Error handling: catch specific exceptions, log useful context, rethrow when caller must handle. Prefer non-fatal fallbacks and fail-fast for invalid invariants.
- Formatting: follow 2-space indenting for Clojure forms, keep expressions short (<= 80-100 chars when reasonable).
- Imports/interop: when calling native code, validate arguments and handle nils/invalid pointers gracefully; centralize FFI glue in dedicated namespaces (e.g. vybe.panama, vybe.c).
- Tests: write deterministic tests, mock native interactions where possible, and use Kaocha metadata selectors to run focused suites.

Docstrings

- ALWAYS run the linter before the changes and after to compare errors introduced by you
- When adding or improving docstrings, edit only the string literal directly under the target `defn`/`defmacro`; never touch the surrounding code path.
- Focus on public functions unless explicitly instructed; describe purpose, accepted inputs, return values, and edge cases when the behavior is subtle.
- Docstrings for functions that accept option maps MUST document all supported option keys and their expected values or shapes (for example `:rt`, `:shaders`, `:frag`, `:uniforms`, etc.). This helps callers and automated tooling understand supported configurations.
- Be cautious with patches so that embedded documentation maps or commented examples are not modified inadvertently; always re-read the diff around automated replacements.
- Preserve balanced delimiters and existing quoting/escaping—docstring work must not introduce syntax changes.

REPL Usage (for agents)

- To evaluate Clojure code in the running REPL, use the clojure_evaluate_code tool specifying the code, namespace, and session key (usually 'clj').
- To check the output of recent REPL evaluations, use the clojure_repl_output_log tool, starting with since-line=0 and incrementing as needed.
- Example: To evaluate (+ 5 3) in the vybe.flecs namespace, call clojure_evaluate_code with code '(+ 5 3)', namespace 'vybe.flecs', replSessionKey 'clj'.
- Always check the output tool for application logs, errors, or printed results after evaluation.
- For more details, see the workspace attachments or ask for the latest REPL output if unsure about the current state.

When in doubt, mimic existing patterns in the repository (namespaces under `src/vybe`, test layout under `test/vybe`).
