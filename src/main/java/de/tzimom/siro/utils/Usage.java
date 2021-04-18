package de.tzimom.siro.utils;

import de.tzimom.siro.Main;
import org.bukkit.command.CommandSender;

public enum Usage {

    SIRO,
    TEAM;

    private Main plugin = Main.getInstance();

    public void send(CommandSender sender) {
        sendHead(sender);

        switch (this) {
            case SIRO:
                sendLine(sender, "siro start", "Startet das Spiel");
                sendLine(sender, "siro stop", "Beendet das Spiel oder bricht den CountDown ab");
                sendLine(sender, "siro addspawn", "Fügt einen Spawnpunkt hinzu");
                sendLine(sender, "siro removespawn <ID>", "Entfernt einen Spawnpunkt");
                sendLine(sender, "siro spawns", "Zeigt die Spawns an");
                sendLine(sender, "siro ban <PlayerName>", "Lässt einen Spieler ausscheiden");
                break;
            case TEAM:
                sendLine(sender, "team create <TeamName> <SpielerName> [<SpielerName>]", "Erstellt ein Team");
                sendLine(sender, "team delete <TeamName>", "Löscht ein Team");
                sendLine(sender, "team list", "Listet die Teams auf");
                sendLine(sender, "team of [<PlayerName>]|[*]", "Zeigt, in welchem Team ein, oder alle Spieler sich befinden");
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
