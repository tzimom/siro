package de.tzimom.siro.commands;

import de.tzimom.siro.Main;
import de.tzimom.siro.utils.CustomPlayer;
import de.tzimom.siro.utils.Permission;
import de.tzimom.siro.utils.Usage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class BanCommand implements CommandExecutor {

    private final Main plugin = Main.getInstance();

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission(Permission.COMMAND_BAN)) {
            sender.sendMessage(plugin.prefix + plugin.noPermission);
            return true;
        }

        if (args.length != 1) {
            Usage.BAN.send(sender);
            return true;
        }

        String playerName = args[0];
        Player player = Bukkit.getPlayer(playerName);

        if (player == null) {
            sender.sendMessage(plugin.prefix + "§cDer Spieler ist nicht online");
            return true;
        }

        UUID uuid = player.getUniqueId();
        CustomPlayer customPlayer = CustomPlayer.getPlayer(uuid);

        if (!plugin.getGameManager().isRunning()) {
            sender.sendMessage(plugin.prefix + "§cDas Spiel ist noch nicht gestartet");
            return true;
        }

        if (customPlayer.isBanned()) {
            sender.sendMessage(plugin.prefix + "§cDer Spieler ist bereits ausgeschieden");
            return true;
        }

        customPlayer.ban();
        sender.sendMessage(plugin.prefix + "§7Der Spieler §6" + player.getName() + " §7wurde aus dem Projekt ausgeschieden.");

        return true;
    }

}
