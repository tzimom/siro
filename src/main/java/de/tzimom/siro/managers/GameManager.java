package de.tzimom.siro.managers;

import de.tzimom.siro.Main;
import de.tzimom.siro.utils.CustomPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Sound;

public class GameManager extends FileManager {

    private static final int COUNTDOWN_TIME = 30;

    private final Main plugin = Main.getInstance();
    private boolean running = false;

    private final PlayerManager playerManager = new PlayerManager();
    private final CountDown countDown = new CountDown();

    public GameManager() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            if (!plugin.getGameManager().isRunning())
                return;

            Bukkit.getOnlinePlayers().forEach(player -> CustomPlayer.getPlayer(player.getUniqueId()).playTimeCheck());
        }, 0, 1);
    }

    public boolean startCountdown() {
        if (running || countDown.running)
            return false;

        countDown.start();
        return true;
    }

    private void startGame() {
        running = true;

        Bukkit.getOnlinePlayers().forEach(player -> {
            CustomPlayer customPlayer = CustomPlayer.getPlayer(player.getUniqueId());
            customPlayer.reset();
            customPlayer.prepare();
            customPlayer.onPreLogin(false );
            customPlayer.onJoin();

            player.playSound(player.getLocation(), Sound.LEVEL_UP, 1f, 1f);
        });
    }

    public boolean stopGame() {
        if (running) {
            Bukkit.getOnlinePlayers().forEach(player -> CustomPlayer.getPlayer(player.getUniqueId()).reset());
            Bukkit.broadcastMessage(plugin.prefix + "§cDas Spiel wurde beendet");

            running = false;
            return true;
        }

        if (countDown.running) {
            Bukkit.getOnlinePlayers().forEach(player -> {
                CustomPlayer.getPlayer(player.getUniqueId()).prepare();
                player.playSound(player.getLocation(), Sound.NOTE_BASS, 1f, 1f);
            });

            Bukkit.broadcastMessage(plugin.prefix + "§cDer Countdown wurde abgebrochen");
            countDown.cancel();

            return true;
        }

        return false;
    }

    public boolean isRunning() {
        return running;
    }

    public static long getCurrentDay() {
        long currentTime = System.currentTimeMillis();
        return (long) Math.floor(currentTime / 1000d / 60d / 60d / 24d);
    }

    private class CountDown {
        private boolean running;
        private int task;
        private int timeLeft;

        private void start() {
            if (running)
                return;

            running = true;
            reset();

            task = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
                if (timeLeft <= 0) {
                    Bukkit.broadcastMessage(plugin.prefix + "§aDas Spiel startet §6jetzt");

                    startGame();
                    cancel();
                    return;
                }

                Bukkit.getOnlinePlayers().forEach(player -> player.setLevel(timeLeft));

                if (timeLeft % 10 == 0 || timeLeft <= 5) {
                    Bukkit.getOnlinePlayers().forEach(player -> {
                        player.sendMessage(plugin.prefix + "§aDas Spiel startet in §6" + timeLeft + " Sekunden");
                        player.playSound(player.getLocation(), Sound.NOTE_PLING, 1f, 1f);
                    });
                }

                timeLeft--;
            }, 0, 20);
        }

        private void cancel() {
            if (!running)
                return;

            running = false;
            Bukkit.getScheduler().cancelTask(task);
        }

        private void reset() {
            timeLeft = COUNTDOWN_TIME;
        }
    }

    public String getFileName() {
        return "game";
    }

}
