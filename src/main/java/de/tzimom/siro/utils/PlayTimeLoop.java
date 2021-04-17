package de.tzimom.siro.utils;

import de.tzimom.siro.Main;
import org.bukkit.Bukkit;

public class PlayTimeLoop {

    private Main plugin = Main.getInstance();

    public void start() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () ->
                Bukkit.getOnlinePlayers().forEach(player ->
                        CustomPlayer.getPlayer(player.getUniqueId()).playTimeCheck()), 0, 1);
    }

    public static long getCurrentDay() {
        long currentTime = System.currentTimeMillis();
        return (long) Math.floor(currentTime / 1000d / 60d / 60d / 24d);
    }

}
