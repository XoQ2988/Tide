package me.xoq.tide.utils.config;

import com.google.gson.JsonElement;
import me.xoq.tide.utils.input.Keybind;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TideConfig {
    public final List<ModuleEntry> modules = new ArrayList<>();
    public final Map<String, JsonElement> settings = new LinkedHashMap<>();

    public static class ModuleEntry {
        public String name;
        public boolean enabled;
        public Keybind keybind;
    }
}