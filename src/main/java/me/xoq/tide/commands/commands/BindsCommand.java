package me.xoq.tide.commands.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import me.xoq.tide.commands.Command;
import me.xoq.tide.modules.Module;
import me.xoq.tide.modules.Modules;
import me.xoq.tide.utils.input.Keybind;
import me.xoq.tide.utils.misc.Utils;
import net.minecraft.command.CommandSource;

import java.util.List;

public class BindsCommand extends Command {
    public BindsCommand() {
        super("binds", "List all module keybinds");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(this::execute);
    }

    @SuppressWarnings("SameReturnValue")
    private int execute(CommandContext<CommandSource> context) {
        List<Module> all = Modules.getAll();
        List<Module> bound = all.stream()
                .filter(m -> !m.keybind.equals(Keybind.none()))
                .toList();

        info("§eModule keybinds (" + bound.size() + "):");
        for (Module module : bound) {
            String keyName = Utils.getKeyName(module.keybind.getCode());
            info("§7[ §6" + module.getTitle() + " §7] §e" + keyName + "§7");
        }
        return SINGLE_SUCCESS;
    }
}