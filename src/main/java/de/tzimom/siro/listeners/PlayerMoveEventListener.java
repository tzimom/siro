package de.tzimom.siro.listeners;

import de.tzimom.siro.Main;
import de.tzimom.siro.utils.CustomPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.UUID;

public class PlayerMoveEventListener implements Listener {

    private final Main plugin = Main.getInstance();

    @EventHandler
    public void handlePlayerMoveEvent(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        CustomPlayer customPlayer = CustomPlayer.getPlayer(uuid);

        Location from = event.getFrom();
        Location to = event.getTo();

        if (customPlayer.isProtected()) {
            boolean movedOutOfBlock = from.getBlockX() != to.getBlockX()
                    || from.getBlockY() != to.getBlockY()
                    || from.getBlockZ() != to.getBlockZ();

            if (movedOutOfBlock) {
                player.teleport(new Location(from.getWorld(), from.getX(), from.getY(), from.getZ()));
                event.setCancelled(true);
            }
        }
    }

}
