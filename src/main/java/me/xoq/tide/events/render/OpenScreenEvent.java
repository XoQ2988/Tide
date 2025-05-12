package me.xoq.tide.events.render;

import me.xoq.tide.events.CancellableEvent;
import net.minecraft.client.gui.screen.Screen;

public class OpenScreenEvent extends CancellableEvent {
    private final Screen screen;

    public OpenScreenEvent(Screen screen) {
        this.screen = screen;
    }

    public Screen getScreen() {
        return this.screen;
    }
}