package me.xoq.tide.commands.commands;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import me.xoq.tide.commands.Command;
import me.xoq.tide.hud.HudEntry;
import me.xoq.tide.hud.HudManager;
import me.xoq.tide.modules.Module;
import me.xoq.tide.modules.Modules;
import me.xoq.tide.modules.impl.HudDisplay;
import net.minecraft.command.CommandSource;

import java.util.Collection;

public class HudCommand extends Command {
    public HudCommand() {
        super("hud", "View and edit HUD entries");
    }

    // suggest every registered HUD entry by ID
    private static final SuggestionProvider<CommandSource> ENTRY_SUGGESTER = (ctx, sb) -> {
        HudManager.get().getAllEntries().stream()
                .map(HudEntry::getId)
                .forEach(sb::suggest);
        return sb.buildFuture();
    };

    // suggest two boolean states
    private static final SuggestionProvider<CommandSource> STATE_SUGGESTER = (ctx, sb) -> {
        sb.suggest("true").suggest("false");
        return sb.buildFuture();
    };

    // suggest current template for this entry
    private static final SuggestionProvider<CommandSource> TEMPLATE_SUGGESTER = (ctx, sb) -> {
        String id = StringArgumentType.getString(ctx, "entry");
        HudDisplay hud = getHud();
        if (hud != null) {
            String tmpl = hud.templates.get(id);
            if (tmpl != null) sb.suggest(tmpl);
        }
        return sb.buildFuture();
    };


    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder
                // /hud
                .executes(this::listAll)
                // /hud <entry> ...
                .then(argument("entry", StringArgumentType.word())
                        .suggests(ENTRY_SUGGESTER)
                        .executes(this::viewOne)

                        // /hud <entry> toggle <true|false>
                        .then(literal("toggle")
                                .then(argument("state", BoolArgumentType.bool())
                                        .suggests(STATE_SUGGESTER)
                                        .executes(this::toggleOne)
                                )
                        )
                        // /hud <entry> template <value...>
                        .then(literal("template")
                                .then(argument("value", StringArgumentType.greedyString())
                                        .suggests(TEMPLATE_SUGGESTER)
                                        .executes(this::setTemplate)
                                )
                        )
                        // /hud <entry> reset
                        .then(literal("reset")
                                .executes(this::resetOne)
                        )
                );
    }

    @SuppressWarnings("SameReturnValue")
    private int listAll(CommandContext<CommandSource> ctx) {
        Collection<HudEntry> entries = HudManager.get().getAllEntries();
        if (entries.isEmpty()) {
            info("No HUD entries registered.");
        } else {
            info("HUD entries:");
            for (HudEntry e : entries) {
                info(" - " + e.getId());
            }
        }
        return SINGLE_SUCCESS;
    }

    private int viewOne(CommandContext<CommandSource> ctx) {
        String id = StringArgumentType.getString(ctx, "entry");
        HudDisplay hud = getHud();
        if (hud == null) {
            error("HudDisplay not loaded.");
            return 0;
        }

        Boolean tog = hud.toggles.get(id);
        String tmpl = hud.templates.get(id);
        if (tog == null || tmpl == null) {
            warn("Unknown entry: " + id);
        } else {
            info("Entry '" + id + "' is " + (tog ? "ON" : "OFF")
                    + "; template: " + tmpl);
        }
        return SINGLE_SUCCESS;
    }

    private int setTemplate(CommandContext<CommandSource> ctx) {
        String id    = StringArgumentType.getString(ctx, "entry");
        String value = StringArgumentType.getString(ctx, "value");
        HudDisplay hud = getHud();
        if (hud == null) {
            error("HudDisplay not loaded.");
            return 0;
        }

        if (!hud.templates.containsKey(id)) {
            warn("Unknown entry: " + id);
        } else {
            hud.templates.put(id, value);
            info("Template for '" + id + "' set to: " + value);
        }
        return SINGLE_SUCCESS;
    }

    private int toggleOne(CommandContext<CommandSource> ctx) {
        String id = StringArgumentType.getString(ctx, "entry");
        boolean state = BoolArgumentType.getBool(ctx, "state");
        HudDisplay hud = getHud();
        if (hud == null) {
            error("HudDisplay not loaded.");
            return 0;
        }

        if (!hud.toggles.containsKey(id)) {
            warn("Unknown entry: " + id);
        } else {
            hud.toggles.put(id, state);
            info("Entry '" + id + "' toggled " + (state ? "ON" : "OFF") + ".");
        }
        return SINGLE_SUCCESS;
    }


    private int resetOne(CommandContext<CommandSource> ctx) {
        String id = StringArgumentType.getString(ctx, "entry");
        HudDisplay hud = getHud();
        if (hud == null) {
            error("HudDisplay not loaded.");
            return 0;
        }

        HudEntry entry = HudManager.get().getAllEntries().stream()
                .filter(e -> e.getId().equals(id)).findAny().orElse(null);

        if (entry == null) {
            warn("Unknown entry: " + id);
        } else {
            hud.toggles.put(id, true);
            hud.templates.put(id, entry.getTemplate());
            info("Entry '" + id + "' reset to defaults.");
        }
        return SINGLE_SUCCESS;
    }

    private static HudDisplay getHud() {
        Module mod = Modules.getByName("hud-display");
        return (mod instanceof HudDisplay hud) ? hud : null;
    }
}