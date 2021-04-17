package de.tzimom.siro.utils;

import de.tzimom.siro.Main;
import org.bukkit.command.CommandSender;

public enum Usage {

    SIRO,
    TEAM,
    BAN;

    private Main plugin = Main.getInstance();

    public void send(CommandSender sender) {
        sendHead(sender);

        switch (this) {
            case SIRO:
                sendLine(sender, "siro start", "Starts the game");
                sendLine(sender, "siro stop", "Ends the game or cancels the countdown");
                break;
            case TEAM:
                sendLine(sender, "team create <TeamName> [<SpielerName> ...]", "Erstellt ein Team");
                break;
            case BAN:
                sendLine(sender, "ban <SpielerName>", "Schließt einen Spieler aus dem Projekt aus");
                break;
            default:
                sender.sendMessage(plugin.prefix + "§cKeine Hilfe verfügbar");
        }
    }

    private void sendLine(CommandSender sender, String usage, String description) {
        sender.sendMessage(plugin.prefix + "§8- §6/" + usage + " §8- §7" + description);
    }

    private void sendHead(CommandSender sender) {
        sender.sendMessage(plugin.prefix + "§6§lHilfe:");
    }

}
