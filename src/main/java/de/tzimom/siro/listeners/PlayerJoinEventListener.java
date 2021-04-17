package de.tzimom.siro.listeners;

import de.tzimom.siro.utils.CustomPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class PlayerJoinEventListener implements Listener {

    @EventHandler
    public void handlePlayerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        CustomPlayer.getPlayer(uuid).onJoin();
    }

}
