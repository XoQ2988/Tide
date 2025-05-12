package me.xoq.tide.hud;

import me.xoq.tide.utils.misc.Utils;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HudManager {
    private static final HudManager INSTANCE = new HudManager();
    private final Map<String, HudEntry> entries = new ConcurrentHashMap<>();

    public static HudManager get() { return INSTANCE; }

    public void register(HudEntry entry) {
        entries.put(Utils.titleToName(entry.getId()), entry);
    }

    public Collection<HudEntry> getAllEntries() {
        return Collections.unmodifiableCollection(entries.values());
    }
}