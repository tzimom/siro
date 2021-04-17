package de.tzimom.siro;

import de.tzimom.siro.commands.BanCommand;
import de.tzimom.siro.commands.SiroCommand;
import de.tzimom.siro.commands.TeamCommand;
import de.tzimom.siro.commands.TimeCommand;
import de.tzimom.siro.listeners.*;
import de.tzimom.siro.managers.FileManager;
import de.tzimom.siro.managers.GameManager;
import de.tzimom.siro.utils.CustomPlayer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Main instance;

    public String prefix = "§cSiro §8| §r";
    public String noPlayer = "§cDu musst ein Spieler sein";
    public String noPermission = "§cDu hast nicht die Berechtigung dazu";

    private GameManager gameManager;

    public Main() {
        instance = this;
    }

    public void onEnable() {
        gameManager = new GameManager();

        loadCommands();
        loadListeners();

        Bukkit.getOnlinePlayers().forEach(player -> {
            CustomPlayer.getPlayer(player.getUniqueId()).onPreLogin(false);
            CustomPlayer.getPlayer(player.getUniqueId()).onJoin();
        });
    }

    public void onDisable() {
        Bukkit.getOnlinePlayers().forEach(player -> CustomPlayer.getPlayer(player.getUniqueId()).onQuit());

        FileManager.saveAll();
    }

    private void loadCommands() {
        getCommand("ban").setExecutor(new BanCommand());
        getCommand("siro").setExecutor(new SiroCommand());
        getCommand("team").setExecutor(new TeamCommand());
        getCommand("time").setExecutor(new TimeCommand());
    }

    private void loadListeners() {
        PluginManager pluginManager = Bukkit.getPluginManager();

        pluginManager.registerEvents(new AsyncPlayerPreLoginEventListener(), this);
        pluginManager.registerEvents(new EntityDamageEventListener(), this);
        pluginManager.registerEvents(new FoodLevelChangeEventListener(), this);
        pluginManager.registerEvents(new PlayerDeathEventListener(), this);
        pluginManager.registerEvents(new PlayerInteractEventListener(), this);
        pluginManager.registerEvents(new PlayerJoinEventListener(), this);
        pluginManager.registerEvents(new PlayerMoveEventListener(), this);
        pluginManager.registerEvents(new PlayerQuitEventListener(), this);
    }

    public static Main getInstance() {
        return instance;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

}
