package me.xoq.tide.events.misc;

import me.xoq.tide.events.CancellableEvent;
import me.xoq.tide.utils.input.KeyAction;

public class KeyEvent extends CancellableEvent {
    private final int key;
    private final int modifiers;
    private final KeyAction action;

    public KeyEvent(int key, int modifiers, KeyAction action) {
        this.key = key;
        this.modifiers = modifiers;
        this.action = action;
    }

    public int getKey() {
        return key;
    }

    public int getModifiers() {
        return modifiers;
    }

    public KeyAction getAction() {
        return action;
    }
}