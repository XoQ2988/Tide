package me.xoq.tide.modules.impl;

import me.xoq.tide.events.EventHandler;
import me.xoq.tide.events.render.Render2DEvent;
import me.xoq.tide.hud.HudEntry;
import me.xoq.tide.hud.HudManager;
import me.xoq.tide.hud.PlaceholderRegistry;
import me.xoq.tide.modules.Module;
import me.xoq.tide.settings.ColorSetting;
import me.xoq.tide.settings.EnumSetting;
import me.xoq.tide.settings.IntSetting;
import me.xoq.tide.settings.Setting;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static me.xoq.tide.TideClient.mc;

public class HudDisplay extends Module {
    public HudDisplay() {
        super("hud-display", "Render all registered HUD entries");

        for (HudEntry entry : HudManager.get().getAllEntries()) {
            String id = entry.getId();
            toggles.put(id, false);
            templates.put(id, entry.getTemplate());
        }
    }

    private final Setting<Anchor> anchor = settings.add(
            new EnumSetting.Builder<Anchor>()
                    .name("anchor")
                    .description("Which corner to draw the HUD from")
                    .defaultValue(Anchor.TOP_LEFT)
                    .build()
    );

    private final Setting<Integer> primaryColor = settings.add(
            new ColorSetting.Builder()
                    .name("primary-color")
                    .description("Color for HUD text (named preset or hex)")
                    .defaultValue(Formatting.GRAY.getColorValue())
                    .build()
    );

    private final Setting<Integer> secondaryColor = settings.add(
            new ColorSetting.Builder()
                    .name("secondary-color")
                    .description("Alternate color")
                    .defaultValue(Formatting.DARK_AQUA.getColorValue())
                    .build()
    );

    private final Setting<Integer> padding = settings.add(
            new IntSetting.Builder()
                    .name("padding")
                    .description("Distance in pixels from the screen edge")
                    .defaultValue(2)
                    .range(0, 50)
                    .build()
    );

    public final Map<String, Boolean> toggles = new LinkedHashMap<>();
    public final Map<String, String> templates = new LinkedHashMap<>();

    @EventHandler
    private void onRender2D(Render2DEvent event) {
        if (mc.options.hudHidden || !isEnabled()) return;

        List<HudEntry> visible = HudManager.get().getAllEntries().stream()
                .filter(hudEntry -> toggles.get(hudEntry.getId()))
                .toList();

        if (visible.isEmpty()) return;

        Pattern placeholderPattern = Pattern.compile("\\{(.+?)}");
        Map<String, Supplier<String>> suppliers = PlaceholderRegistry.getAll();

        List<HudEntry> toRender = new ArrayList<>();
        for (HudEntry hudEntry : visible) {
            String template = templates.get(hudEntry.getId());
            Matcher matcher = placeholderPattern.matcher(template);
            boolean ok = true;
            while (matcher.find()) {
                Supplier<String> supplier = suppliers.get(matcher.group(1));
                if (supplier == null || supplier.get() == null) {
                    ok = false;
                    break;
                }
            }
            if (ok) toRender.add(hudEntry);
        }

        if (toRender.isEmpty()) return;

        for (HudEntry hudEntry : toRender) {
            String id = hudEntry.getId();
            hudEntry.setTemplate(templates.get(id));
            hudEntry.clearPlaceholders();
            Matcher matcher = placeholderPattern.matcher(hudEntry.getTemplate());
            while (matcher.find()) {
                hudEntry.setPlaceholder(matcher.group(1), suppliers.get(matcher.group(1)).get());
            }
        }

        DrawContext drawContext = event.context();
        TextRenderer textRenderer = mc.textRenderer;

        int lineHeight = textRenderer.fontHeight + 2;
        int scaledWidth = mc.getWindow().getScaledWidth();
        int scaledHeight = mc.getWindow().getScaledHeight();
        int pad = padding.getValue();
        boolean left = anchor.getValue().isLeft();
        boolean top = anchor.getValue().isTop();
        int baseX = left ? pad : scaledWidth - pad;
        int baseY = top  ? pad : scaledHeight - pad - lineHeight * toRender.size();

        int primaryColorValue = primaryColor.getValue();
        int secondaryColorValue = secondaryColor.getValue();

        for (int i = 0; i < toRender.size(); i++) {
            HudEntry hudEntry = toRender.get(i);
            String template = templates.get(hudEntry.getId());
            List<Segment> segments = buildSegments(template, placeholderPattern);

            int x = baseX;
            int y = baseY + lineHeight * i;

            for (Segment segment : segments) {
                String text = segment.isPlaceholder() ? hudEntry.getPlaceholder(segment.key()) : segment.text();
                int rendererWidth = textRenderer.getWidth(text);
                int color = segment.isPlaceholder() ? secondaryColorValue : primaryColorValue;

                drawContext.drawTextWithShadow(textRenderer, Text.literal(text), left ?
                        x : x - rendererWidth, y, color);

                x += left ? rendererWidth : -rendererWidth;
            }
        }
    }

    private static List<Segment> buildSegments(String template, Pattern pattern) {
        List<Segment> segments = new ArrayList<>();
        Matcher matcher = pattern.matcher(template);

        int last = 0;
        while (matcher.find()) {
            if (matcher.start() > last) {
                segments.add(new Segment(template.substring(last, matcher.start()), false, null));
            }
            segments.add(new Segment(null, true, matcher.group(1)));
            last = matcher.end();
        }

        if (last < template.length()) {
            segments.add(new Segment(template.substring(last), false, null));
        }

        return segments;
    }

    private record Segment(String text, boolean isPlaceholder, String key) {}

    public enum Anchor {
        TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT;
        public boolean isLeft() { return this == TOP_LEFT || this == BOTTOM_LEFT; }
        public boolean isTop() { return this == TOP_LEFT || this == TOP_RIGHT; }
    }
}