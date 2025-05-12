package me.xoq.tide.utils.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.xoq.tide.TideClient;
import me.xoq.tide.modules.Module;
import me.xoq.tide.modules.Modules;
import me.xoq.tide.settings.SettingsManager;
import me.xoq.tide.utils.input.Keybind;

import java.io.*;

public final class ConfigManager {
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    public static TideConfig config;

    private ConfigManager() { }

    public static void load() {
        File configFile = TideClient.CONFIG_FILE.toFile();

        if (configFile.exists()) {
            try (Reader fileReader = new FileReader(configFile)) {
                config = GSON.fromJson(fileReader, TideConfig.class);
            } catch (IOException ioException) {
                TideClient.LOG.error("Failed to read config, using defaults", ioException);
                config = new TideConfig();
            }
        } else {
            // first run or deleted file
            config = new TideConfig();
        }

        // apply module entries…
        for (TideConfig.ModuleEntry moduleEntry : config.modules) {
            Module module = Modules.getByName(moduleEntry.name);

            if (module == null) continue;

            module.setEnabled(moduleEntry.enabled);
            module.keybind = moduleEntry.keybind != null
                    ? moduleEntry.keybind
                    : Keybind.none();
        }

        // load all registered settings
        SettingsManager.getInstance().load(config);
    }

    public static void save() {
        TideConfig newCfg = new TideConfig();

        for (Module module : Modules.getAll()) {
            TideConfig.ModuleEntry moduleEntry = new TideConfig.ModuleEntry();
            moduleEntry.name    = module.getName();
            moduleEntry.enabled = module.isEnabled();
            moduleEntry.keybind = module.keybind;
            newCfg.modules.add(moduleEntry);
        }

        // snapshot settings into config
        SettingsManager.getInstance().save(newCfg);

        // write JSON out
        File configFile = TideClient.CONFIG_FILE.toFile();
        try (Writer writer = new FileWriter(configFile)) {
            GSON.toJson(newCfg, writer);
        } catch (IOException ioException) {
            TideClient.LOG.error("Failed to save config", ioException);
        }
    }
}