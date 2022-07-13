package com.lab.business.threadlocal;

public final class BaseUserIdThreadLocal {

    private static final ThreadLocal<Long> LOCAL = new ThreadLocal<>();

    private BaseUserIdThreadLocal() {

    }

    public static void set(Long userId) {
        LOCAL.set(userId);
    }

    public static Long get() {
        return LOCAL.get();
    }

    public static void remove() {
        LOCAL.remove();
    }
}
