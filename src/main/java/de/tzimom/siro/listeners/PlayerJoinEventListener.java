package de.tzimom.siro.listeners;

import de.tzimom.siro.Main;
import de.tzimom.siro.utils.CustomPlayer;
import net.minecraft.server.v1_8_R1.EnumClientCommand;
import net.minecraft.server.v1_8_R1.PacketPlayInClientCommand;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class PlayerJoinEventListener implements Listener {

    private Main plugin = Main.getInstance();

    @EventHandler
    public void handlePlayerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        CustomPlayer customPlayer = CustomPlayer.getPlayer(uuid);

        if (player.isDead()) {
            CraftPlayer craftPlayer = (CraftPlayer) player;
            PacketPlayInClientCommand packet = new PacketPlayInClientCommand(EnumClientCommand.PERFORM_RESPAWN);
            craftPlayer.getHandle().playerConnection.a(packet);
        }

        if (plugin.getGameManager().isRunning()) customPlayer.onJoin();
        else customPlayer.prepare();
    }

}
