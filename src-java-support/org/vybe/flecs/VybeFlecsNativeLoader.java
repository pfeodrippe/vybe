package org.vybe.flecs;

import java.io.IOException;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.Linker;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SymbolLookup;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

/** Selects and loads the native Flecs provider used by generated bindings. */
public final class VybeFlecsNativeLoader {
    public static final String PATH_ENV = "VYBE_FLECS_LIBRARY_PATH";
    public static final String PATH_PROPERTY = "vybe.flecs.library.path";
    public static final String EXPECTED_COMMIT_ENV = "VYBE_FLECS_EXPECTED_COMMIT";
    public static final String EXPECTED_COMMIT_PROPERTY = "vybe.flecs.expected.commit";
    public static final String ALLOW_UNVERIFIED_ENV = "VYBE_FLECS_ALLOW_UNVERIFIED";

    private static final Object LOCK = new Object();

    private static String configuredPath;
    private static String loadedPath;
    private static boolean loaded;

    private VybeFlecsNativeLoader() {
    }

    /** Configure an absolute provider path before the generated Flecs class loads. */
    public static void configure(String libraryPath) {
        String canonicalPath = canonicalLibraryPath(libraryPath);
        synchronized (LOCK) {
            if (loaded && !canonicalPath.equals(loadedPath)) {
                throw new IllegalStateException(
                    "Flecs provider is already loaded from " + loadedPath);
            }
            if (configuredPath != null && !configuredPath.equals(canonicalPath)) {
                throw new IllegalStateException(
                    "Flecs provider is already configured as " + configuredPath);
            }
            configuredPath = canonicalPath;
        }
    }

    /** Load the configured provider, or the bundled vybe_flecs fallback. */
    public static void load() {
        synchronized (LOCK) {
            if (loaded) {
                return;
            }

            String requestedPath = requestedPath();
            if (requestedPath != null && !requestedPath.isBlank()) {
                String canonicalPath = canonicalLibraryPath(requestedPath);
                System.load(canonicalPath);
                loadedPath = canonicalPath;
            } else {
                System.loadLibrary("vybe_flecs");
                loadedPath = "library:vybe_flecs";
            }
            loaded = true;
            if (!"library:vybe_flecs".equals(loadedPath)) {
                verifyExternalProvider(false);
            }
        }
    }

    public static boolean isLoaded() {
        synchronized (LOCK) {
            return loaded;
        }
    }

    public static String requestedPath() {
        synchronized (LOCK) {
            if (configuredPath != null) {
                return configuredPath;
            }
        }

        String propertyPath = System.getProperty(PATH_PROPERTY);
        if (propertyPath != null && !propertyPath.isBlank()) {
            return propertyPath;
        }

        String environmentPath = System.getenv(PATH_ENV);
        if (environmentPath != null && !environmentPath.isBlank()) {
            return environmentPath;
        }
        return null;
    }

    public static Map<String, Object> info() {
        synchronized (LOCK) {
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("requested-path", requestedPath());
            result.put("loaded-path", loadedPath);
            result.put("loaded?", loaded);
            result.put("external?", loadedPath != null
                && !"library:vybe_flecs".equals(loadedPath));
            if (loaded) {
                result.put("provider", providerInfo());
            }
            return result;
        }
    }

    /** Return ABI metadata exported by a recent Vybe Flecs provider. */
    public static Map<String, Object> providerInfo() {
        synchronized (LOCK) {
            if (!loaded) {
                load();
            }
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("contract-version", callInt("vybe_flecs_provider_contract_version"));
            result.put("version-major", callInt("vybe_flecs_provider_version_major"));
            result.put("version-minor", callInt("vybe_flecs_provider_version_minor"));
            result.put("version-patch", callInt("vybe_flecs_provider_version_patch"));
            result.put("commit", callString("vybe_flecs_provider_commit"));
            result.put("sizeof-iter", callLong("vybe_flecs_provider_sizeof_iter"));
            result.put("sizeof-system-desc", callLong("vybe_flecs_provider_sizeof_system_desc"));
            result.put("sizeof-query-desc", callLong("vybe_flecs_provider_sizeof_query_desc"));
            return result;
        }
    }

    /** Verify the external provider against the now-initialized bindings. */
    public static Map<String, Object> verifyProvider() {
        synchronized (LOCK) {
            load();
            if (!"library:vybe_flecs".equals(loadedPath)) {
                verifyExternalProvider(true);
            }
            return providerInfo();
        }
    }

    private static void verifyExternalProvider(boolean verifyGeneratedSizes) {
        Map<String, Object> provider = providerInfo();
        if (provider.get("contract-version") == null) {
            if (!"1".equals(System.getenv(ALLOW_UNVERIFIED_ENV))) {
                throw new IllegalStateException(
                    "External Flecs library is not a Vybe provider. Rebuild it with "
                    + "bin/build-flecs-provider.sh, or set " + ALLOW_UNVERIFIED_ENV
                    + "=1 only for a deliberate legacy override.");
            }
            return;
        }

        String expectedCommit = System.getProperty(EXPECTED_COMMIT_PROPERTY);
        if (expectedCommit == null || expectedCommit.isBlank()) {
            expectedCommit = System.getenv(EXPECTED_COMMIT_ENV);
        }
        Object providerCommit = provider.get("commit");
        if (expectedCommit != null && !expectedCommit.isBlank()
                && !expectedCommit.equals(providerCommit)) {
            throw new IllegalStateException(
                "Flecs provider commit mismatch: expected " + expectedCommit
                + ", loaded " + providerCommit);
        }

        if (verifyGeneratedSizes) {
            verifySize(provider, "sizeof-iter", "org.vybe.flecs.ecs_iter_t");
            verifySize(provider, "sizeof-system-desc", "org.vybe.flecs.ecs_system_desc_t");
            verifySize(provider, "sizeof-query-desc", "org.vybe.flecs.ecs_query_desc_t");
        }
    }

    private static void verifySize(
            Map<String, Object> provider, String key, String generatedClassName) {
        Long nativeSize = (Long) provider.get(key);
        if (nativeSize == null) {
            return;
        }
        try {
            Class<?> generatedClass = Class.forName(generatedClassName);
            long generatedSize = ((Number) generatedClass.getMethod("sizeof").invoke(null))
                .longValue();
            if (nativeSize.longValue() != generatedSize) {
                throw new IllegalStateException(
                    "Flecs ABI mismatch for " + generatedClassName + ": provider="
                    + nativeSize + ", bindings=" + generatedSize);
            }
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException(
                "Could not validate generated Flecs ABI for " + generatedClassName,
                exception);
        }
    }

    private static MemorySegment symbol(String name) {
        return SymbolLookup.loaderLookup().find(name).orElse(null);
    }

    private static Integer callInt(String name) {
        MemorySegment address = symbol(name);
        if (address == null) {
            return null;
        }
        MethodHandle handle = Linker.nativeLinker().downcallHandle(
            address, FunctionDescriptor.of(ValueLayout.JAVA_INT));
        try {
            return (int) handle.invokeExact();
        } catch (Throwable throwable) {
            throw new IllegalStateException("Could not invoke provider symbol " + name, throwable);
        }
    }

    private static Long callLong(String name) {
        MemorySegment address = symbol(name);
        if (address == null) {
            return null;
        }
        MethodHandle handle = Linker.nativeLinker().downcallHandle(
            address, FunctionDescriptor.of(ValueLayout.JAVA_LONG));
        try {
            return (long) handle.invokeExact();
        } catch (Throwable throwable) {
            throw new IllegalStateException("Could not invoke provider symbol " + name, throwable);
        }
    }

    private static String callString(String name) {
        MemorySegment address = symbol(name);
        if (address == null) {
            return null;
        }
        MethodHandle handle = Linker.nativeLinker().downcallHandle(
            address, FunctionDescriptor.of(ValueLayout.ADDRESS));
        try {
            MemorySegment value = (MemorySegment) handle.invokeExact();
            return value.equals(MemorySegment.NULL)
                ? null
                : value.reinterpret(256).getString(0);
        } catch (Throwable throwable) {
            throw new IllegalStateException("Could not invoke provider symbol " + name, throwable);
        }
    }

    private static String canonicalLibraryPath(String libraryPath) {
        if (libraryPath == null || libraryPath.isBlank()) {
            throw new IllegalArgumentException("Flecs provider path is empty");
        }

        Path path = Path.of(libraryPath);
        if (!path.isAbsolute()) {
            throw new IllegalArgumentException(
                "Flecs provider path must be absolute: " + libraryPath);
        }
        if (!Files.isRegularFile(path)) {
            throw new IllegalArgumentException(
                "Flecs provider does not exist or is not a file: " + libraryPath);
        }

        try {
            return path.toRealPath().toString();
        } catch (IOException exception) {
            throw new IllegalArgumentException(
                "Could not resolve Flecs provider path: " + libraryPath,
                exception);
        }
    }
}
