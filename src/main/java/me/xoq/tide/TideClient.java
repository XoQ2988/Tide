package me.xoq.tide;

import me.xoq.tide.commands.Commands;
import me.xoq.tide.events.EventBus;
import me.xoq.tide.hud.HudEntries;
import me.xoq.tide.modules.Modules;
import me.xoq.tide.utils.config.ConfigManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.client.MinecraftClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

public class TideClient implements ClientModInitializer {
	public static final String MOD_ID = "tide";
	public static final ModMetadata MOD_META;
	public static final String NAME;
	public static MinecraftClient mc;
	public static final EventBus EVENT_BUS = new EventBus();
	public static final Path CONFIG_FILE;

	public static final Logger LOG;

	static {
        ModContainer container = FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow();
		MOD_META = container.getMetadata();
		NAME = MOD_META.getName();
		CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve(MOD_ID + ".json");

		LOG = LoggerFactory.getLogger(NAME);
	}

	@Override
	public void onInitializeClient() {
		// cache client instance
		mc = MinecraftClient.getInstance();

		// init core systems
		HudEntries.init();
		Modules.init();
		Commands.init();
		ConfigManager.load();

		// ensure config is saved on shutdown
		Runtime.getRuntime().addShutdownHook(new Thread(ConfigManager::save));

		LOG.info("{} initialized", NAME);
	}
}