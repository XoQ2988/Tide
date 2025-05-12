package me.xoq.tide.events.misc;

import me.xoq.tide.events.CancellableEvent;
import net.minecraft.client.sound.SoundInstance;

public class SoundEvent extends CancellableEvent {
    private final SoundInstance soundInstance;

    public SoundEvent(SoundInstance soundInstance) {
        this.soundInstance = soundInstance;
    }

    public SoundInstance getSoundInstance() {
        return soundInstance;
    }
}