package de.tzimom.siro.managers;

import de.tzimom.siro.Main;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public abstract class FileManager {

    private static final Set<FileManager> FILE_MANAGERS = new HashSet<>();

    private final Main plugin = Main.getInstance();

    private File file;
    private YamlConfiguration config;

    public FileManager() {
        if (!plugin.getDataFolder().exists())
            plugin.getDataFolder().mkdirs();

        file = new File(plugin.getDataFolder(), getFullFileName());

        try {
            if (!file.exists())
                file.createNewFile();

            config = YamlConfiguration.loadConfiguration(file);
            FILE_MANAGERS.add(this);
        } catch (IOException ignored) {
            Bukkit.getConsoleSender().sendMessage(plugin.prefix + "§cUnable to create and load config file \"" + getFullFileName() + "\"");
        }
    }

    protected void saveConfig() {
        try {
            config.save(file);
        } catch (IOException ignored) {
            Bukkit.getConsoleSender().sendMessage(plugin.prefix + "§cUnable to save config file \"" + getFullFileName() + "\"");
        }
    }

    public static void saveAll() {
        FILE_MANAGERS.forEach(FileManager::saveConfig);
    }

    private String getFullFileName() {
        return getFileName() + ".yml";
    }

    public abstract String getFileName();

    public YamlConfiguration getConfig() {
        return config;
    }

}
