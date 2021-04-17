package de.tzimom.siro.commands;

import de.tzimom.siro.Main;
import de.tzimom.siro.teams.Team;
import de.tzimom.siro.utils.Permission;
import de.tzimom.siro.utils.Usage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class TeamCommand implements CommandExecutor {

    private Main plugin = Main.getInstance();

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission(Permission.COMMAND_TEAM)) {
            sender.sendMessage(plugin.prefix + plugin.noPermission);
            return true;
        }

        if ((args.length == 3 || args.length == 4) && args[0].equalsIgnoreCase("create")) {
            String teamName = args[1];
            String member1 = args[2];
            String member2 = args.length == 4 ? args[3] : null;

            if (teamName.length() > Team.MAX_NAME_LENGTH) {
                sender.sendMessage(plugin.prefix + "§cDer Teamname darf nicht länger als " + Team.MAX_NAME_LENGTH +
                        " Zeichen sein");
            }

//            Team team = new Team(teamName, member1, member2);
        } else {
            Usage.TEAM.send(sender);
        }

        return true;
    }

}
