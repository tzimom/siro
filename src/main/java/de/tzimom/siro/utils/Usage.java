package de.tzimom.siro.utils;

import de.tzimom.siro.Main;
import org.bukkit.command.CommandSender;

public enum Usage {

    TEAM;

    private Main plugin = Main.getInstance();

    public void send(CommandSender sender) {
        sendHead(sender);

        switch (this) {
            case TEAM:
                sendLine(sender, "team create <TeamName> [<PlayerName> ...]", "Erstellt ein Team");
                break;
            default:
                sender.sendMessage(plugin.prefix + "§cNo help available");
        }
    }

    private void sendLine(CommandSender sender, String usage, String description) {
        sender.sendMessage(plugin.prefix + "§8- §6/" + usage + " §8- §7" + description);
    }

    private void sendHead(CommandSender sender) {
        sender.sendMessage(plugin.prefix + "§6§lHilfe:");
    }

}
