package me.xoq.tide.modules;

import me.xoq.tide.TideClient;
import me.xoq.tide.settings.SettingsManager;
import me.xoq.tide.utils.config.SettingHelper;
import me.xoq.tide.utils.input.Keybind;
import me.xoq.tide.utils.misc.ChatUtils;
import me.xoq.tide.utils.misc.Utils;

public class Module {
    private final String name;
    private final String title;
    private final String description;
    private final boolean subscribeOnToggle;
    private boolean enabled;

    public final SettingHelper settings;
    public Keybind keybind = Keybind.none();

    protected Module(String name, String description, @SuppressWarnings("SameParameterValue") Boolean subscribeOnToggle) {
        this.name = name;
        this.title = Utils.nameToTitle(name);
        this.description = description;
        this.subscribeOnToggle = subscribeOnToggle;
        this.settings = new SettingHelper(SettingsManager.getInstance(), name);

        if (!subscribeOnToggle) {
            // always listening
            TideClient.EVENT_BUS.register(this);
        }
    }

    // default constructor, listens only when enabled
    protected Module(String name, String description) {
        this(name, description, true);
    }

    public void toggle() {
        ChatUtils.info("Toggled §3" + title + "§r " +(!enabled ? "§2on" : "§4off"));
        setEnabled(!enabled);
    }

    public void setEnabled(boolean on) {
        if (this.enabled == on) return;
        this.enabled = on;

        if (on) onEnabled();
        else    onDisabled();

        if (subscribeOnToggle) {
            if (on) TideClient.EVENT_BUS.register(this);
            else    TideClient.EVENT_BUS.unregister(this);
        }
    }

    protected void onEnabled() { }
    protected void onDisabled() { }

    public void info(String message) { ChatUtils.info(title, message); }
    public void warn(String message) { ChatUtils.info(title, "§e" + message); }
    public void error(String message) { ChatUtils.info(title, "§l§c" + message); }

    // getters
    public String getName()        { return name; }
    public String getTitle()       { return title; }
    public String getDescription() { return description; }
    public boolean isEnabled()     { return enabled; }
}