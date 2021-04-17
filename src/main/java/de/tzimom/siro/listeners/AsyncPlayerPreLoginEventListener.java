package de.tzimom.siro.listeners;

import de.tzimom.siro.utils.CustomPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.UUID;

public class AsyncPlayerPreLoginEventListener implements Listener {

    @EventHandler
    public void handleAsyncPlayerPreLoginEvent(AsyncPlayerPreLoginEvent event) {
        UUID uuid = event.getUniqueId();

        CustomPlayer customPlayer = CustomPlayer.getPlayer(uuid);
        customPlayer.onPreLogin();

        if (customPlayer.getRemainingTime() <= 0) {
            event.setKickMessage("§cDeine Zeit ist bereits abgelaufen");
            event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
        }
    }

}
