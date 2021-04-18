package de.tzimom.siro.listeners;

import de.tzimom.siro.utils.CustomPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.UUID;

public class EntityDamageByEntityEventListener implements Listener {

    @EventHandler
    public void handleEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        Entity damagerEntity = event.getDamager();

        if (!(damagerEntity instanceof Player))
            return;

        Player damager = (Player) damagerEntity;
        UUID damagerUuid = damager.getUniqueId();
        CustomPlayer customDamager = CustomPlayer.getPlayer(damagerUuid);

        if (customDamager.isProtected()) {
            event.setCancelled(true);
            return;
        }

        Entity entity = event.getEntity();

        if (!(entity instanceof Player))
            return;

        Player player = (Player) entity;
        UUID uuid = player.getUniqueId();
        CustomPlayer customPlayer = CustomPlayer.getPlayer(uuid);

        if (customPlayer.getTeam() != null && customDamager.getTeam() == customPlayer.getTeam())
            event.setCancelled(true);
        else {
            customDamager.setCombat(player.getUniqueId());
            customPlayer.setCombat(damager.getUniqueId());
        }
    }

}
