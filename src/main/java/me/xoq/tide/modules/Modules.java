package me.xoq.tide.modules;

import me.xoq.tide.TideClient;
import me.xoq.tide.events.EventHandler;
import me.xoq.tide.events.misc.KeyEvent;
import me.xoq.tide.modules.impl.*;
import me.xoq.tide.utils.input.Input;
import me.xoq.tide.utils.input.KeyAction;
import me.xoq.tide.utils.input.Keybind;
import me.xoq.tide.utils.misc.Utils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static me.xoq.tide.TideClient.mc;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F3;

public class Modules {
    private static final Modules INSTANCE = new Modules();

    private final Map<String, Module> modules = new LinkedHashMap<>();
    private Module moduleToBind;

    private Modules() { }

    public static Modules get() {
        return INSTANCE;
    }

    public static void init() {
        // register each module
        register(new AntiBreak());
        register(new AutoTool());
        register(new HudDisplay());
        register(new RandomHotbar());

        // listen for key events to handle toggles & rebinding
        TideClient.EVENT_BUS.register(INSTANCE);
    }

    public static <T extends Module> void register(T module) {
        get().modules.put(module.getName().toLowerCase(), module);
    }

    public static List<Module> getAll() {
        return get().modules.values().stream().toList();
    }

    public static Module getByName(String name) {
        return get().modules.get(name.toLowerCase());
    }

    // Binding
    public void setModuleToBind(Module module) {
        this.moduleToBind = module;
    }

    @EventHandler
    private void onKeyEvent(KeyEvent event) {
        // ignore when in any GUI or F3 is held
        if (mc.currentScreen != null || Input.isKeyPressed(GLFW_KEY_F3)) {
            return;
        }

        // “capture next key” mode logic
        if (moduleToBind != null) {
            if (event.getAction() == KeyAction.Release) return;

            if (event.getAction() == KeyAction.Press) {
                if (event.getKey() == GLFW_KEY_ESCAPE) {
                    moduleToBind.keybind.set(Keybind.none());
                    moduleToBind.info("Bind cleared.");
                } else {
                    moduleToBind.keybind.set(true, event.getKey(), event.getModifiers());
                    moduleToBind.info("Bound to " +
                            Utils.getKeyName(moduleToBind.keybind.getCode()) + ".");
                }
            }
            event.cancel();
            moduleToBind = null;
            return;
        }

        // Normal module-toggle behavior
        if (event.getAction() != KeyAction.Press) {
            return;
        }

        for (Module module : modules.values()) {
            Keybind kb = module.keybind;
            if (kb.matches(true, event.getKey(), event.getModifiers())) {
                module.toggle();
                event.cancel();
                break;
            }
        }
    }
}