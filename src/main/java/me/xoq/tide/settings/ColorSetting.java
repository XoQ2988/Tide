package me.xoq.tide.settings;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class ColorSetting extends BaseSetting<Integer> {
    private static final Map<String, Integer> PRESETS;
    static {
        Map<String,Integer> map = new LinkedHashMap<>();

        // Minecraft formatting colors (ARGB)
        map.put("black",        0xFF000000);
        map.put("dark_blue",    0xFF0000AA);
        map.put("dark_green",   0xFF00AA00);
        map.put("dark_aqua",    0xFF00AAAA);
        map.put("dark_red",     0xFFAA0000);
        map.put("dark_purple",  0xFFAA00AA);
        map.put("gold",         0xFFFFAA00);
        map.put("gray",         0xFFAAAAAA);
        map.put("dark_gray",    0xFF555555);
        map.put("blue",         0xFF5555FF);
        map.put("green",        0xFF55FF55);
        map.put("aqua",         0xFF55FFFF);
        map.put("red",          0xFFFF5555);
        map.put("light_purple", 0xFFFF55FF);
        map.put("yellow",       0xFFFFFF55);
        map.put("white",        0xFFFFFFFF);

        PRESETS = Collections.unmodifiableMap(map);
    }

    protected ColorSetting(Builder builder) {
        super(builder.name, builder.description, builder.defaultValue);
    }

    public static Map<String, Integer> getPresets() {
        return PRESETS;
    }

    public static int parseColor(String raw) throws IllegalArgumentException {
        String string = raw.trim();
        String lower = string.toLowerCase();

        if (PRESETS.containsKey(lower)) {
            return PRESETS.get(lower);
        }

        if (lower.startsWith("#")) {
            string = "0x" + string.substring(1);
        }

        if (lower.startsWith("0x")) {
            // hex parse (up to 8 digits)
            return (int) Long.parseLong(string.substring(2), 16);
        }

        // decimal fallback
        return Integer.parseInt(string);
    }


    public static class Builder {
        private String name;
        private String description = "";
        private Integer defaultValue = 0;

        public ColorSetting.Builder name(String name) {
            this.name = name;
            return this;
        }
        public ColorSetting.Builder description(String desc) {
            this.description = desc;
            return this;
        }
        public ColorSetting.Builder defaultValue(Integer def) {
            this.defaultValue = def;
            return this;
        }

        public ColorSetting build() {
            if (name == null) {
                throw new IllegalStateException("name is required");
            }
            return new ColorSetting(this);
        }
    }
}