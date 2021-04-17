package de.tzimom.siro.listeners;

import de.tzimom.siro.utils.CustomPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.UUID;

public class EntityDamageEventListener implements Listener {

    @EventHandler
    public void handleEntityDamageEvent(EntityDamageEvent event) {
        Entity entity = event.getEntity();

        if (!(entity instanceof Player))
            return;

        Player player = (Player) entity;
        UUID uuid = player.getUniqueId();

        if (CustomPlayer.getPlayer(uuid).isProtected())
            event.setCancelled(true);
    }

}
