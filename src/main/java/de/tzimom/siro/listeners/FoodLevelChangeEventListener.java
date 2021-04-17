package de.tzimom.siro.listeners;

import de.tzimom.siro.Main;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class FoodLevelChangeEventListener implements Listener {

    private final Main plugin = Main.getInstance();

    @EventHandler
    public void handleFoodLevelChangeEvent(FoodLevelChangeEvent event) {
        if (plugin.getGameManager().isRunning())
            return;

        HumanEntity humanEntity = event.getEntity();

        if (!(humanEntity instanceof Player))
            return;

        Player player = (Player) humanEntity;
        player.setFoodLevel(20);
        event.setCancelled(true);
    }

}
