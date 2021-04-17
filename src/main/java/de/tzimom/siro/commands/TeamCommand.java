package de.tzimom.siro.commands;

import de.tzimom.siro.Main;
import de.tzimom.siro.teams.Team;
import de.tzimom.siro.utils.CustomPlayer;
import de.tzimom.siro.utils.Permission;
import de.tzimom.siro.utils.Usage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.UUID;

public class TeamCommand implements CommandExecutor {

    private Main plugin = Main.getInstance();

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission(Permission.COMMAND_TEAM)) {
            sender.sendMessage(plugin.prefix + plugin.noPermission);
            return true;
        }

        if ((args.length == 3 || args.length == 4) && args[0].equalsIgnoreCase("create")) {
            String teamName = args[1];
            String[] membersStrings = new String[] { args[2], args.length == 4 ? args[3] : null };

            if (teamName.length() > Team.MAX_NAME_LENGTH) {
                sender.sendMessage(plugin.prefix + "§cDer Teamname darf nicht länger als " + Team.MAX_NAME_LENGTH +
                        " Zeichen sein");
                return true;
            }

            UUID[] members = new UUID[membersStrings.length];

            for (int i = 0; i < membersStrings.length; i++) {
                if (membersStrings[i] == null || membersStrings[i].isEmpty())
                    continue;

                Player player = Bukkit.getPlayer(membersStrings[i]);

                if (player == null) {
                    sender.sendMessage(plugin.prefix + "§cDer Spieler " + membersStrings[i] + " ist nicht online");
                    return true;
                }

                CustomPlayer customPlayer = CustomPlayer.getPlayer(player.getUniqueId());

                if (plugin.getGameManager().getTeamManager().getTeam(teamName) != null) {
                    sender.sendMessage(plugin.prefix + "§cDas Team existiert bereits");
                    return true;
                }

                if (customPlayer.getTeam() != null) {
                    sender.sendMessage(plugin.prefix + "§cDer Spieler " + player.getName() + " ist bereits in einem Team: "
                            + customPlayer.getTeam().getTeamName());
                    return true;
                }

                members[i] = player.getUniqueId();
            }

            if (members[0] == members[1])
                members[1] = null;

            Team team = new Team(teamName, members);
            plugin.getGameManager().getTeamManager().registerTeam(team);
            sender.sendMessage(plugin.prefix + "§7Das Team §6" + teamName + " §7wurde erstellt");
        } else if (args.length == 2 && args[0].equalsIgnoreCase("delete")) {
            String teamName = args[1];
            Team team = plugin.getGameManager().getTeamManager().getTeam(teamName);

            if (team == null) {
                sender.sendMessage(plugin.prefix + "§cDas Team existiert nicht");
                return true;
            }

            plugin.getGameManager().getTeamManager().deleteTeam(team);
            sender.sendMessage(plugin.prefix + "§cDas Team " + team.getTeamName() + " wurde gelöscht");
        } else if (args.length == 1 && args[0].equalsIgnoreCase("list")) {
            Set<Team> teams = plugin.getGameManager().getTeamManager().getTeams();

            if (teams.isEmpty()) {
                sender.sendMessage(plugin.prefix + "§cEs gibt keine Teams");
            } else {
                sender.sendMessage(plugin.prefix + "§6§lTeams:");
                teams.forEach(team -> sender.sendMessage(plugin.prefix + "§8- §6" + team.getTeamName()));
            }
        } else {
            Usage.TEAM.send(sender);
        }

        return true;
    }

}
