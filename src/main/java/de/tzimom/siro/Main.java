package de.tzimom.siro;

import de.tzimom.siro.commands.TeamCommand;
import de.tzimom.siro.listeners.EntityDamageEventListener;
import de.tzimom.siro.listeners.PlayerJoinEventListener;
import de.tzimom.siro.listeners.AsyncPlayerPreLoginEventListener;
import de.tzimom.siro.listeners.PlayerQuitEventListener;
import de.tzimom.siro.utils.CustomPlayer;
import de.tzimom.siro.utils.PlayTimeLoop;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Main instance;

    public String prefix = "§cSiro §8| §r";
    public String noPlayer = "§cDu musst ein Spieler sein";
    public String noPermission = "§cDu hast nicht die Berechtigung dazu";

    public Main() {
        instance = this;
    }

    public void onEnable() {
        new PlayTimeLoop().start();
        System.out.println(PlayTimeLoop.getCurrentDay());

        loadCommands();
        loadListeners();

        Bukkit.getOnlinePlayers().forEach(player -> {
            CustomPlayer.getPlayer(player.getUniqueId()).onPreLogin();
            CustomPlayer.getPlayer(player.getUniqueId()).onJoin();
        });
    }

    public void onDisable() {
        Bukkit.getOnlinePlayers().forEach(player -> CustomPlayer.getPlayer(player.getUniqueId()).onQuit());
    }

    private void loadCommands() {
        getCommand("team").setExecutor(new TeamCommand());
    }

    private void loadListeners() {
        PluginManager pluginManager = Bukkit.getPluginManager();

        pluginManager.registerEvents(new AsyncPlayerPreLoginEventListener(), this);
        pluginManager.registerEvents(new EntityDamageEventListener(), this);
        pluginManager.registerEvents(new PlayerJoinEventListener(), this);
        pluginManager.registerEvents(new PlayerQuitEventListener(), this);
    }

    public static Main getInstance() {
        return instance;
    }

}
