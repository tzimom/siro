package de.tzimom.siro.listeners;

import de.tzimom.siro.utils.CustomPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.UUID;

public class EntityDamageByEntityEventListener implements Listener {

    public void handleEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();

        if (!(damager instanceof Player))
            return;

        Player player = (Player) damager;
        UUID uuid = player.getUniqueId();
        CustomPlayer customPlayer = CustomPlayer.getPlayer(uuid);

        if (customPlayer.isProtected())
            event.setCancelled(true);
    }

}
