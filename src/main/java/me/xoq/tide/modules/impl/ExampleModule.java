package me.xoq.tide.modules.impl;

import me.xoq.tide.events.*;
import me.xoq.tide.events.misc.TickEvent;
import me.xoq.tide.modules.Module;
import me.xoq.tide.settings.BoolSetting;
import me.xoq.tide.settings.Setting;

public class ExampleModule extends Module {
    // 1. super call
    public ExampleModule() {
        super("example", "An example module", true);
    }

    // 2. settings
    private final Setting<Boolean> exampleToggle = settings.add(
            new BoolSetting.Builder()
                    .name("exampleToggle")
                    .description("An on/off toggle")
                    .defaultValue(true)
                    .build()
    );

    // 3. local vars
    private int counter;

    // 4. lifecycle overrides
    @Override
    protected void onEnabled() {
        info("ExampleModule enabled");
        counter = 0;
    }

    @Override
    protected void onDisabled() {
        info("ExampleModule disabled");
    }

    // 5. event handlers
    @EventHandler
    private void onTick(TickEvent event) {
        if (!exampleToggle.getValue()) return;

        counter++;
        if (counter % 20 == 0) {
            info("Ticked " + counter + " times");
        }

        doSomething();
    }

    // 6. local helper methods
    @SuppressWarnings("EmptyMethod")
    private void doSomething() {
        // ...
    }
}