package de.tzimom.varo;

import de.tzimom.varo.commands.TimeCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Main instance;

    public String prefix = "§cVaro §8| §r";
    public String noPlayer = "§cDu musst ein Spieler sein";

    public Main() {
        instance = this;
    }

    public void onEnable() {
        loadCommands();
    }

    private void loadCommands() {
        getCommand("time").setExecutor(new TimeCommand());
    }

    public static Main getInstance() {
        return instance;
    }

}
