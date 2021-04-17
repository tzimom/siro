package de.tzimom.siro.commands;

import de.tzimom.siro.Main;
import de.tzimom.siro.utils.CustomPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class TimeCommand implements CommandExecutor {

    private final Main plugin = Main.getInstance();

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.prefix + plugin.noPlayer);
            return true;
        }

        Player player = (Player) sender;
        UUID uuid = player.getUniqueId();
        CustomPlayer customPlayer = CustomPlayer.getPlayer(uuid);

        if (!plugin.getGameManager().isRunning()) {
            player.sendMessage(plugin.prefix + "§cDas Spiel ist noch nicht gestartet");
            return true;
        }

        customPlayer.notifyPlayer(player);
        return true;
    }

}
