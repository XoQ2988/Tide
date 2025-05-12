package me.xoq.tide.hud;

import java.util.HashMap;
import java.util.Map;

public class HudEntry {
    private final String id;
    private String template;
    private final Map<String, String> placeholders = new HashMap<>();

    protected HudEntry(String id,
                    String text) {
        if (id == null) throw new IllegalArgumentException("ID cannot be null");
        this.id = id;
        setTemplate(text);
    }

    // getters
    public String getId() {
        return id;
    }

    public String getTemplate() {
        return template;
    }

    public String getPlaceholder(String key) {
        return placeholders.get(key);
    }

    public String getText() {
        String result = template;
        for (Map.Entry<String, String> e : placeholders.entrySet()) {
            result = result.replace("{" + e.getKey() + "}", e.getValue());
        }
        return result;
    }

    // setters
    public void setTemplate(String template) {
        this.template = template != null ? template : "";
    }

    public void setPlaceholder(String key, String value) {
        placeholders.put(key, value != null ? value : "");
    }

    public void removePlaceholder(String key) {
        placeholders.remove(key);
    }

    public void clearPlaceholders() {
        placeholders.clear();
    }
}