package de.tzimom.siro.utils;

import com.sun.org.apache.xpath.internal.operations.String;
import de.tzimom.siro.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CustomPlayer {

    private static final Map<UUID, CustomPlayer> CUSTOM_PLAYERS = new HashMap<>();

    private static final int PROTECTION_TIME = 10;
//    private static final int PLAY_TIME = 60 * 20;
    private static final int PLAY_TIME = 20;

    private Main plugin = Main.getInstance();
    private final UUID uuid;

    private final Map<Long, Long> playTimes = new HashMap<>();
    private long joinTimestamp;
    private boolean nextDay = false;

    public static CustomPlayer getPlayer(UUID uuid) {
        if (!CUSTOM_PLAYERS.containsKey(uuid))
            CUSTOM_PLAYERS.put(uuid, new CustomPlayer(uuid));

        return CUSTOM_PLAYERS.get(uuid);
    }

    private CustomPlayer(UUID uuid) {
        this.uuid = uuid;
    }

    private Player getPlayer() {
        Player player = Bukkit.getPlayer(uuid);

        if (player == null || !player.isOnline())
            return null;

        return player;
    }

    public void onPreLogin() {
        joinTimestamp = System.currentTimeMillis();

        if (getRemainingTime() <= 0 && !nextDay)
            nextDay = true;
    }

    public void onJoin() {
        notifyPlayer();
    }

    public void onQuit() {
        long currentDay = PlayTimeLoop.getCurrentDay() + (nextDay ? 1 : 0);
        long currentPlayTime = playTimes.getOrDefault(currentDay, 0l);

        playTimes.put(currentDay, getPlayedTime() + currentPlayTime);
    }

    public boolean isProtected() {
        return System.currentTimeMillis() - joinTimestamp < 1000 * PROTECTION_TIME;
    }

    private long getPlayedTime() {
        int timePlayed = (int) (System.currentTimeMillis() - joinTimestamp);
        timePlayed += playTimes.getOrDefault(PlayTimeLoop.getCurrentDay() + (nextDay ? 1 : 0), 0l);
        return timePlayed;
    }

    public long getRemainingTime() {
        return PLAY_TIME * 1000 - getPlayedTime();
    }

    private int lastNotify;

    public void playTimeCheck() {
        long remainingTime = getRemainingTime();

        if (remainingTime <= 0) {
            kick();
            return;
        }

        int seconds = (int) Math.ceil(remainingTime / 1000d);

        if ((seconds % (60 * 5) == 0)
                || (seconds < 5 * 60 && seconds % 60 == 0)
                || (seconds <= 30 && seconds % 10 == 0)
                || (seconds <= 10))
            notifyPlayer();
    }

    private void kick() {
        Player player = getPlayer();

        if (player == null)
            return;

        player.kickPlayer("§cDeine Zeit ist abgelaufen!");
    }

    private void notifyPlayer() {
        Player player = getPlayer();

        if (player == null)
            return;

        int totalSeconds = (int) Math.ceil(getRemainingTime() / 1000d);

        if (lastNotify == totalSeconds)
            return;

        lastNotify = totalSeconds;

        int seconds = totalSeconds % 60;
        int minutes = totalSeconds / 60;

        StringBuilder remainingTime = new StringBuilder();

        if (minutes > 0) remainingTime.append(minutes).append(" ").append(minutes > 1 ? "Minuten" : "Minute").append(" ");
        if (seconds > 0) remainingTime.append(seconds).append(" ").append(seconds > 1 ? "Sekunden" : "Sekunde").append(" ");

        player.sendMessage(plugin.prefix + "§7Du kannst noch §6" + remainingTime.toString() + "§7spielen");
    }

}
