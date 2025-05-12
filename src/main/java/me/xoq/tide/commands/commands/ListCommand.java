package me.xoq.tide.commands.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import me.xoq.tide.commands.Command;
import me.xoq.tide.modules.Module;
import me.xoq.tide.modules.Modules;
import net.minecraft.command.CommandSource;

import java.util.List;

public class ListCommand extends Command {
    public ListCommand() {
        super("list", "List all modules and their states");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(this::execute);
    }

    @SuppressWarnings("SameReturnValue")
    private int execute(CommandContext<CommandSource> context) {
        List<Module> all = Modules.getAll();

        info("§eModules (" + all.size() + "):");
        for (Module module : all) {
            info("§7[ " + (module.isEnabled() ? "§a ON " : "§cOFF") + " §7]" + " §6" + module.getTitle() + "§7");
        }
        return SINGLE_SUCCESS;
    }
}
