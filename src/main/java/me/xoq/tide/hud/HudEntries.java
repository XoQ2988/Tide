package me.xoq.tide.hud;

import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;

import static me.xoq.tide.TideClient.mc;

public class HudEntries {
    public static final HudEntry FPS = new HudEntry("fps", "FPS: {FPS}");
    public static final HudEntry PING = new HudEntry("ping",   "Ping: {PING}ms");
    public static final HudEntry COORDS = new HudEntry("coords", "Position: {X}, {Y}, {Z}");
    public static final HudEntry NETHER = new HudEntry("nether_cords", "Nether: {NX}, {Y}, {NZ}");
    public static final HudEntry SPEED = new HudEntry("speed",       "Speed: {SPEED} m/s");
    public static final HudEntry HELD_DUR = new HudEntry("held_dur",    "Held Dur: {HELD_DUR}%");
    public static final HudEntry ARMOR_DUR = new HudEntry("armor_dur",   "Armor Dur: {ARMOR_DUR}%");

    public static void init() {
        HudManager hudManager = HudManager.get();
        hudManager.register(FPS);
        hudManager.register(PING);
        hudManager.register(COORDS);
        hudManager.register(NETHER);
        hudManager.register(SPEED);
        hudManager.register(HELD_DUR);
        hudManager.register(ARMOR_DUR);

        // register runtime suppliers
        PlaceholderRegistry.register("FPS", () -> String.valueOf(mc.getCurrentFps()));
        PlaceholderRegistry.register("PING", () -> {
            if (mc.getNetworkHandler() == null || mc.player == null) return null;
            PlayerListEntry entry = mc.getNetworkHandler().getPlayerListEntry(mc.player.getUuid());
            return entry == null ? null : String.valueOf(entry.getLatency());
        });

        // coordinates
        PlaceholderRegistry.register("X", () -> mc.player == null ? null
                : String.format("%.1f", mc.player.getX()));
        PlaceholderRegistry.register("Y", () -> mc.player == null ? null
                : String.format("%.1f", mc.player.getY()));
        PlaceholderRegistry.register("Z", () -> mc.player == null ? null
                : String.format("%.1f", mc.player.getZ()));

        // scaled (nether) coords
        PlaceholderRegistry.register("NX", () -> mc.player == null ? null
                : String.format("%.1f", mc.player.getX() / 8.0));
        PlaceholderRegistry.register("NZ", () -> mc.player == null ? null
                : String.format("%.1f", mc.player.getZ() / 8.0));

        // Horizontal speed
        PlaceholderRegistry.register("SPEED", () -> {
            if (mc.player == null) return null;
            Vec3d v = mc.player.getVelocity();
            return String.format("%.2f", Math.hypot(v.x, v.z));
        });

        // In‐game time HH:mm
        PlaceholderRegistry.register("TIME", () -> {
            if (mc.world == null) return null;
            long t = mc.world.getTimeOfDay() % 24000;
            return String.format("%02d:%02d", (int) ((t / 1000 + 6) % 24), (int) ((t % 1000) * 60 / 1000));
        });

        // Held item durability %
        PlaceholderRegistry.register("HELD_DUR", () -> {
            if (mc.player == null) return null;
            ItemStack handStack = mc.player.getMainHandStack();
            if (handStack.isEmpty() || !handStack.isDamageable()) return null;
            float pct = (handStack.getMaxDamage() - handStack.getDamage()) * 100f / handStack.getMaxDamage();
            return String.format("%.0f", pct);
        });

        // Armor average durability %
        PlaceholderRegistry.register("ARMOR_DUR", () -> {
            if (mc.player == null) return null;
            float total = 0;
            int count = 0;
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                if (slot.getType() != EquipmentSlot.Type.HUMANOID_ARMOR) continue;
                ItemStack s = mc.player.getEquippedStack(slot);
                if (s.isEmpty() || !s.isDamageable()) continue;
                float pct = (s.getMaxDamage() - s.getDamage()) * 100f / s.getMaxDamage();
                total += pct;
                count++;
            }
            return count == 0 ? null : String.format("%.0f", total / count);
        });
    }
}