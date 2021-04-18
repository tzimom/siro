package de.tzimom.siro.managers;

import de.tzimom.siro.Main;
import net.minecraft.server.v1_8_R1.EnumParticle;
import net.minecraft.server.v1_8_R1.PacketPlayOutWorldParticles;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class SpawnPointManager extends FileManager {

    private final Main plugin = Main.getInstance();

    private final Map<Integer, Location> spawns = new HashMap<>();

    public SpawnPointManager() {
        if (!getConfig().contains("spawns"))
            return;

        if (!getConfig().isConfigurationSection("spawns"))
            return;

        getConfig().getConfigurationSection("spawns").getKeys(false).forEach(idString -> {
            try {
                int id = Integer.parseInt(idString);
                Location location = (Location) getConfig().get("spawns." + idString);

                spawns.put(id, location);
            } catch (NumberFormatException ignored) {
            }
        });
    }

    protected void saveConfig() {
        getConfig().set("spawns", null);

        spawns.forEach((id, location) -> getConfig().set("spawns." + id, location));
        super.saveConfig();
    }

    public boolean addSpawn(Location location) {
        for (Location current : spawns.values()) {
            if (location.getBlock().getLocation().equals(current.getBlock().getLocation()))
                return false;
        }

        spawns.put(getNextFreeId(), location);
        return true;
    }

    public boolean removeSpawn(int id) {
        if (!spawns.containsKey(id))
            return false;

        spawns.remove(id);
        return true;
    }

    public boolean removeAll() {
        if (spawns.isEmpty())
            return false;

        spawns.clear();
        return true;
    }

    private int getNextFreeId() {
        int id = 1;

        for (int currentId : spawns.keySet().stream().sorted().collect(Collectors.toList())) {
            if (currentId == id) id++;
            else return id;
        }

        return id;
    }

    public int distanceFromCenterSquared(Location spawn) {
        int distanceX = spawn.getBlockX();
        int distanceZ = spawn.getBlockZ();

        return distanceX * distanceX + distanceZ * distanceZ;
    }

    public void display(int id, Location location, CommandSender sender) {
        final Player player = sender instanceof Player ? (Player) sender : null;

        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();
        sender.sendMessage(plugin.prefix + "§8- §6" + id + " §8[§b" + x + " " + y + " " + z + "§8]");

        if (player != null) {
            final CraftPlayer craftPlayer = (CraftPlayer) player;
            final double shift = 1d;

            for (int i = 0; i <= 3; i ++) {
                final Location shiftedLocation = location.clone().add(0, i * shift, 0);
                final PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.FLAME,
                        true, x + .5f, (float) shiftedLocation.getY(), z + .5f, .2f, .2f, .2f, 0f, 50);
                craftPlayer.getHandle().playerConnection.sendPacket(packet);
            }
        }
    }

    public Map<Integer, Location> getSpawns() {
        return spawns;
    }

    public String getFileName() {
        return "spawns";
    }

}
