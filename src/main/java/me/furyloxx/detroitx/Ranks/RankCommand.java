package me.furyloxx.detroitx.Ranks;

import me.furyloxx.detroitx.Ranks.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RankCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
            Player player = (Player) sender;

            if (sender.isOp() || player.hasPermission("skyblock.admin") || player.hasPermission("skyblock.owner")) {
                if (args.length >= 2) {
                    try {
                        Player target = Bukkit.getPlayer(args[0]);
                        PlayerRank newRank = PlayerRank.valueOf(args[1].toUpperCase().replace("+", "P"));
                        String prefix = newRank == PlayerRank.DEFAULT ? "&7Default" : newRank.getPrefix().replace("[", "").replace("]", "");
                        sender.sendMessage("&aSet " + args[0] + "'s Rank To " + prefix + "&a!");
                        return true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    sender.sendMessage("&cUsage: /setrank <player> <rank>");
                    return false;
                }
            } else {
                sender.sendMessage("&cYou need ADMIN rank to use this command.");
                return false;
            }
        return false;
    }
}
