package de.tzimom.siro.listeners;

import de.tzimom.siro.Main;
import de.tzimom.siro.managers.GameManager;
import de.tzimom.siro.utils.CustomPlayer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.UUID;

public class AsyncPlayerPreLoginEventListener implements Listener {

    private final Main plugin = Main.getInstance();

    @EventHandler
    public void handleAsyncPlayerPreLoginEvent(AsyncPlayerPreLoginEvent event) {
        UUID uuid = event.getUniqueId();

        for (OfflinePlayer operator : Bukkit.getOperators()) {
            if (operator.getUniqueId().equals(event.getUniqueId()))
                return;
        }

        if (plugin.getGameManager().isCountDownRunning()) {
            event.setKickMessage("§cDer Countdown hat bereits begonnen");
            event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_FULL);
            return;
        }

        if (plugin.getGameManager().hasClosed()) {
            event.setKickMessage("§cDer Server hat nur zwischen " + GameManager.SERVER_OPENING_TIME + " Uhr und " +
                    GameManager.SERVER_CLOSING_TIME + " Uhr geöffnet");
            event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_FULL);
            return;
        }

        CustomPlayer customPlayer = CustomPlayer.getPlayer(uuid);
        customPlayer.onPreLogin(true);

        if (customPlayer.isBanned()) {
            event.setKickMessage("§cDu bist aus dem Projekt ausgeschieden");
            event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_BANNED);
            return;
        }

        if (!plugin.getGameManager().isRunning())
            return;

        if (customPlayer.getTeam() == null) {
            event.setKickMessage("§cDu bist in keinem Team");
            event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_BANNED);
            return;
        }

        if (customPlayer.getRemainingTime() <= 0) {
            event.setKickMessage("§cDeine Zeit ist bereits abgelaufen");
            event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
        }
    }

}
