package de.tzimom.varo.commands;

import de.tzimom.varo.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TimeCommand implements CommandExecutor {

    private Main plugin = Main.getInstance();

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.prefix + plugin.noPlayer);
            return true;
        }

        Player player = (Player) sender;

        return true;
    }

}
