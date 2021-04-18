package de.tzimom.siro.listeners;

import de.tzimom.siro.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

public class EntitySpawnEventListener implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler
    public void handleEntitySpawnEvent(EntitySpawnEvent event) {
        if (!main.getGameManager().isRunning())
            event.setCancelled(true);
    }

}
