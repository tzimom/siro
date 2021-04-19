package de.tzimom.siro.listeners;

import de.tzimom.siro.utils.CustomPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class PlayerQuitEventListener implements Listener {

    @EventHandler
    public void handlePlayerQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        CustomPlayer.getPlayer(uuid).onQuit();

        event.setQuitMessage("§8« §c" + player.getName());
    }

}
