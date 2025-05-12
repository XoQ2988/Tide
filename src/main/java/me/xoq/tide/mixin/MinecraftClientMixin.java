package me.xoq.tide.mixin;

import me.xoq.tide.TideClient;
import me.xoq.tide.events.render.OpenScreenEvent;
import me.xoq.tide.events.misc.TickEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Inject(at = @At("HEAD"), method = "tick")
    private void onTick(CallbackInfo info) {
        TideClient.EVENT_BUS.dispatch(new TickEvent());
    }

    @Inject(method = "setScreen", at = @At("HEAD"), cancellable = true)
    private void onSetScreen(Screen screen, CallbackInfo callbackInfo) {
        OpenScreenEvent openScreenEvent = new OpenScreenEvent(screen);
        TideClient.EVENT_BUS.dispatch(openScreenEvent);

        if (openScreenEvent.isCancelled()) callbackInfo.cancel();
    }
}