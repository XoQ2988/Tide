package me.xoq.tide.events.render;

import net.minecraft.client.gui.DrawContext;

public record Render2DEvent(DrawContext context, int screenWidth, int screenHeight, float tickDelta) {
}