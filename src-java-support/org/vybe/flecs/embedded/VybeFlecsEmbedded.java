package org.vybe.flecs.embedded;

import clojure.java.api.Clojure;
import clojure.lang.IFn;

/** Stable JNI-facing facade for the embedded Clojure runtime. */
public final class VybeFlecsEmbedded {
    private static final Object LOCK = new Object();
    private static IFn attach;
    private static IFn poll;
    private static IFn request;
    private static IFn detach;
    private static volatile Throwable lastError;

    private VybeFlecsEmbedded() {
    }

    public static boolean attach(long worldAddress) {
        try {
            ensureLoaded();
            return Boolean.TRUE.equals(attach.invoke(worldAddress));
        } catch (Throwable throwable) {
            report("attach", throwable);
            return false;
        }
    }

    public static int poll(long worldAddress) {
        try {
            ensureLoaded();
            return ((Number) poll.invoke(worldAddress)).intValue();
        } catch (Throwable throwable) {
            report("poll", throwable);
            return -1;
        }
    }

    public static boolean request(long worldAddress, String prompt) {
        try {
            ensureLoaded();
            return Boolean.TRUE.equals(request.invoke(worldAddress, prompt));
        } catch (Throwable throwable) {
            report("request", throwable);
            return false;
        }
    }

    public static void detach(long worldAddress) {
        try {
            ensureLoaded();
            detach.invoke(worldAddress);
        } catch (Throwable throwable) {
            report("detach", throwable);
        }
    }

    public static String lastError() {
        Throwable throwable = lastError;
        return throwable == null ? null : throwable.toString();
    }

    private static void ensureLoaded() {
        if (attach != null) {
            return;
        }
        synchronized (LOCK) {
            if (attach == null) {
                IFn require = Clojure.var("clojure.core", "require");
                require.invoke(Clojure.read("vybe.flecs.embedded"));
                attach = Clojure.var("vybe.flecs.embedded", "attach!");
                poll = Clojure.var("vybe.flecs.embedded", "poll!");
                request = Clojure.var("vybe.flecs.embedded", "request-system!");
                detach = Clojure.var("vybe.flecs.embedded", "detach!");
            }
        }
    }

    private static void report(String operation, Throwable throwable) {
        lastError = throwable;
        System.err.println("[vybe-jvm] " + operation + " failed: " + throwable);
        throwable.printStackTrace(System.err);
    }
}
