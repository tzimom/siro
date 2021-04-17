package de.tzimom.siro.commands;

import de.tzimom.siro.Main;
import de.tzimom.siro.utils.Permission;
import de.tzimom.siro.utils.Usage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SiroCommand implements CommandExecutor {

    private final Main plugin = Main.getInstance();

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission(Permission.COMMAND_SIRO)) {
            sender.sendMessage(plugin.prefix + plugin.noPermission);
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("start")) {
            if (!plugin.getGameManager().startCountdown())
                sender.sendMessage(plugin.prefix + "§cDas Spiel läuft bereits");
        } else if (args.length == 1 && args[0].equalsIgnoreCase("stop")) {
            if (!plugin.getGameManager().stopGame())
                sender.sendMessage(plugin.prefix + "§cDas Spiel ist nicht gestartet");
        } else
            Usage.SIRO.send(sender);

        return true;
    }

}
