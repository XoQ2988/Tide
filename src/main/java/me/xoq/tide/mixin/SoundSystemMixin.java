package me.xoq.tide.mixin;

import me.xoq.tide.TideClient;
import me.xoq.tide.events.misc.SoundEvent;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundSystem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SoundSystem.class)
public abstract class SoundSystemMixin {
    @Inject(method = "play(Lnet/minecraft/client/sound/SoundInstance;)V", at = @At("HEAD"), cancellable = true)
    private void onPlay(SoundInstance soundInstance, CallbackInfo callbackInfo) {
        SoundEvent soundEvent = new SoundEvent(soundInstance);
        TideClient.EVENT_BUS.dispatch(soundEvent);
        if (soundEvent.isCancelled()) {
            callbackInfo.cancel();
        }
    }
}