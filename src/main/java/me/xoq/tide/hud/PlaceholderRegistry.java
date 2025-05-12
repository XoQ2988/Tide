package me.xoq.tide.hud;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class PlaceholderRegistry {
    private static final Map<String, Supplier<String>> suppliers = new ConcurrentHashMap<>();

    public static void register(String key, Supplier<String> supplier) {
        if (key == null || supplier == null) throw new IllegalArgumentException();
        suppliers.put(key, supplier);
    }

    public static Map<String, Supplier<String>> getAll() {
        return Map.copyOf(suppliers);
    }
}
