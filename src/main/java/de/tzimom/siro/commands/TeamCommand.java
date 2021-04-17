package de.tzimom.siro.commands;

import de.tzimom.siro.Main;
import de.tzimom.siro.utils.Permission;
import de.tzimom.siro.utils.Usage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class TeamCommand implements CommandExecutor {

    private Main plugin = Main.getInstance();

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission(Permission.COMMAND_SIRO.getPermission())) {
            sender.sendMessage(plugin.prefix + plugin.noPermission);
            return true;
        }

        if (args.length >= 2 && args[0].equalsIgnoreCase("create")) {

        } else {
            Usage.TEAM.send(sender);
        }

        return true;
    }

}
