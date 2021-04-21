package de.tzimom.siro.utils;

import de.tzimom.siro.Main;
import de.tzimom.siro.managers.GameManager;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CustomPlayer {

    private static final Map<UUID, CustomPlayer> CUSTOM_PLAYERS = new HashMap<>();

    private static final int PROTECTION_TIME = 10;
    private static final int PLAY_TIME = 60 * 20;
    private static final int MAX_COMBAT_DISTANCE = 25;

    private final Main plugin = Main.getInstance();
    private final UUID uuid;

    private final Map<Long, Long> playTimes = new HashMap<>();
    private long joinTimestamp;
    private boolean nextDay = false;
    private int lastNotify = 0;

    private boolean banned;
    private Team team;
    private UUID combat;

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

    public void onPreLogin(boolean protect) {
        if (nextDay && (!playTimes.containsKey(GameManager.getCurrentDay()) || getRemainingTime() > 0))
            nextDay = false;

        long remainingTime = getRemainingTime();

        if (remainingTime <= 0 && !nextDay)
            nextDay = true;

        joinTimestamp = System.currentTimeMillis();

        if (protect) joinTimestamp += PROTECTION_TIME * 1000;
    }

    public void onJoin() {
        if (!plugin.getGameManager().isRunning())
            return;

        Player player = getPlayer();

        if (player == null)
            return;

        if (isProtected())
            player.sendMessage(plugin.prefix + "§7Du hast §6" + PROTECTION_TIME + " Sekunden §7Schutzzeit");

        notifyPlayer(player);
    }

    public void onQuit() {
        if (!plugin.getGameManager().isRunning())
            return;

        long currentDay = GameManager.getCurrentDay() + (nextDay ? 1 : 0);

        playTimes.put(currentDay, getPlayedTime());
        combat = null;
    }

    public void onDie() {
        if (!plugin.getGameManager().isRunning())
            return;

        ban();
    }

    public void prepare() {
        if (plugin.getGameManager().isRunning() && team == null) kick("§cDu wurdest keinem Team zugeteilt", true);

        Player player = getPlayer();

        if (player == null)
            return;

        if (player.isOp() && team == null)
            return;

        player.getInventory().clear();
        player.setHealthScale(20d);
        player.setHealth(20d);
        player.setFoodLevel(20);
        player.setSaturation(0f);
        player.setAllowFlight(false);
        player.setLevel(0);
        player.setExp(0);
        player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));

        for (Achievement achievement : Achievement.values()) {
            if (player.hasAchievement(achievement))
                player.removeAchievement(achievement);
        }

        player.setGameMode(plugin.getGameManager().isRunning() ? GameMode.SURVIVAL : GameMode.ADVENTURE);
    }

    public void reset() {
        playTimes.clear();
        banned = false;
        nextDay = false;
        joinTimestamp = System.currentTimeMillis();
        lastNotify = 0;
    }

    public boolean isProtected() {
        return plugin.getGameManager().isRunning() && System.currentTimeMillis() - joinTimestamp < 0;
    }

    private long getPlayedTime() {
        int timePlayed = Math.max((int) (System.currentTimeMillis() - joinTimestamp), 0);
        timePlayed += playTimes.getOrDefault(GameManager.getCurrentDay() + (nextDay ? 1 : 0), 0l);
        return timePlayed;
    }

    public long getRemainingTime() {
        return PLAY_TIME * 1000 - getPlayedTime();
    }

    public void playTimeCheck() {
        long remainingTime = getRemainingTime();

        Player player = getPlayer();

        if (player == null)
            return;

        if (remainingTime <= 0) {
            kick("§cDeine Zeit ist abgelaufen", false);
            return;
        }

        int seconds = (int) Math.ceil(remainingTime / 1000d);

        if ((seconds % (60 * 5) == 0)
                || (seconds < 5 * 60 && seconds % 60 == 0)
                || (seconds <= 30 && seconds % 10 == 0)
                || (seconds <= 10))
            notifyPlayer(player);
    }

    public void kick(String reason, boolean force) {
        Player player = getPlayer();

        if (player == null)
            return;

        if (player.isOp() && team == null) {
            player.sendMessage(plugin.prefix + "§7Kick durch OP verhindert: " + reason);
            return;
        }

        if (force || !isInCombat())
            player.kickPlayer(reason);
    }

    public boolean isInCombat() {
        final Player player = getPlayer();

        if (player == null || !player.isOnline()) {
            combat = null;
            return false;
        }

        final Player combatPlayer = Bukkit.getPlayer(combat);

        if (combatPlayer == null || !combatPlayer.isOnline()) {
            combat = null;
            return false;
        }

        final Location playerLocation = player.getLocation();
        final Location combatPlayerLocation = combatPlayer.getLocation();

        final double distanceX = playerLocation.getX() - combatPlayerLocation.getX();
        final double distanceY = playerLocation.getY() - combatPlayerLocation.getY();
        final double distanceZ = playerLocation.getZ() - combatPlayerLocation.getZ();
        final double distanceSquared = distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ;

        if (distanceSquared >= MAX_COMBAT_DISTANCE * MAX_COMBAT_DISTANCE) {
            combat = null;
            return false;
        }

        return true;
    }

    public void setCombat(UUID uuid) {
        combat = uuid;
    }

    public void ban() {
        kick("§cDu bist aus dem Projekt ausgeschieden", true);
        banned = true;
    }

    public void notifyPlayer(Player player) {
        int totalSeconds = (int) Math.ceil(getRemainingTime() / 1000d);

        if (lastNotify == totalSeconds)
            return;

        lastNotify = totalSeconds;

        int seconds = totalSeconds % 60;
        int minutes = totalSeconds / 60;

        StringBuilder remainingTime = new StringBuilder();

        if (minutes != 0) remainingTime.append(minutes).append(" ").append(Math.abs(minutes) == 1 ? "Minuten" : "Minute").append(" ");
        if (seconds != 0) remainingTime.append(seconds).append(" ").append(Math.abs(seconds) == 1 ? "Sekunden" : "Sekunde").append(" ");

        player.sendMessage(plugin.prefix + "§7Du kannst noch §6" + remainingTime.toString() + "§7spielen");
    }

    public void playSound(Sound sound) {
        Player player = getPlayer();

        if (player == null)
            return;

        player.playSound(player.getLocation(), sound, 1f, 1f);
    }

    public boolean isBanned() {
        return banned;
    }

    public Map<Long, Long> getPlayTimes() {
        return playTimes;
    }

    public void setNextDay(boolean nextDay) {
        this.nextDay = nextDay;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
    }

    public boolean isNextDay() {
        return nextDay;
    }

    public static Map<UUID, CustomPlayer> getCustomPlayers() {
        return CUSTOM_PLAYERS;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

}
