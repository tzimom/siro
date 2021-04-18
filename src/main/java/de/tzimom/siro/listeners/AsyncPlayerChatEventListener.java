package de.tzimom.siro.listeners;

import de.tzimom.siro.utils.CustomPlayer;
import de.tzimom.siro.utils.Team;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.UUID;

public class AsyncPlayerChatEventListener implements Listener {

    @EventHandler
    public void handleAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        final Player player = event.getPlayer();
        final UUID uuid = player.getUniqueId();
        final CustomPlayer customPlayer = CustomPlayer.getPlayer(uuid);
        final Team team = customPlayer.getTeam();
        final String teamName = team == null ? "Kein Team" : team.getTeamName();

        event.setFormat("§8[§b" + teamName + "§8] §c" + player.getName() + "§8: §7" + event.getMessage());
    }

}
