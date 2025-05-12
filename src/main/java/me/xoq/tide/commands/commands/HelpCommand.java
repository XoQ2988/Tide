package me.xoq.tide.commands.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import me.xoq.tide.commands.Command;
import me.xoq.tide.commands.Commands;
import me.xoq.tide.modules.Module;
import me.xoq.tide.modules.Modules;
import net.minecraft.command.CommandSource;

public class HelpCommand extends Command {
    public HelpCommand() {
        super("help", "List all available commands");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(this::execute);
    }

    @SuppressWarnings("SameReturnValue")
    private int execute(CommandContext<CommandSource> context) {
        info("§eAvailable commands:");
        for (Command cmd : Commands.getAll()) {
            info("§7. §6" + cmd.getTitle() + " §7– §f" + cmd.getDescription());
        }
        info("§eAvailable modules:");
        for (Module mod : Modules.getAll()) {
            info("§7. §6" + mod.getTitle() + " §7– §f" + mod.getDescription());
        }
        return SINGLE_SUCCESS;
    }
}