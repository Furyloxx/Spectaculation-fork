package me.superischroma.spectaculation.command;

import me.superischroma.spectaculation.region.Region;
import me.superischroma.spectaculation.region.RegionGenerator;
import me.superischroma.spectaculation.region.RegionType;
import java.util.HashMap;
import java.util.Iterator;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import java.util.Map;

@CommandParameters(description = "Manage world regions.", usage = "/<command> [create <name> <type> | update <name> [type] | delete <name>]", aliases = "reg", permission = "spt.region")
public class RegionCommand extends SCommand
{
    public static Map<CommandSender, RegionGenerator> REGION_GENERATION_MAP;
    
    @Override
    public void run(final CommandSource sender, final String[] args) {
        if (args.length == 3) {
            final String name = args[1];
            final RegionType type = RegionType.valueOf(args[2].toUpperCase());
            final String lowerCase2;
            final String lowerCase = lowerCase2 = args[0].toLowerCase();
            int n = -1;
            switch (lowerCase2.hashCode()) {
                case -1352294148: {
                    if (lowerCase2.equals((Object)"create")) {
                        n = 0;
                        break;
                    }
                    break;
                }
            }
            switch (n) {
                case 0: {
                    if (name.length() > 100) {
                        throw new CommandFailException("Name too long!");
                    }
                    if (RegionCommand.plugin.regionData.exists(name)) {
                        throw new CommandFailException("There is already a region named that!");
                    }
                    RegionCommand.REGION_GENERATION_MAP.put((Object)sender.getSender(), (Object)new RegionGenerator("create", name, type));
                    this.send("Created a region named \"" + name + "\"");
                    this.send((Object)ChatColor.DARK_AQUA + "Click the first corner of your region.");
                    return;
                }
            }
        }
        if (args.length == 2 && args[0].equalsIgnoreCase("delete")) {
            final String name = args[1];
            final Region region = Region.get(name);
            if (region == null) {
                throw new CommandFailException("There is no region named that!");
            }
            region.delete();
            this.send("Deleted region \"" + name + "\" successfully.");
        }
        else if (args.length == 2 || args.length == 3) {
            final String name = args[1];
            final Region region = Region.get(name);
            if (region == null) {
                throw new CommandFailException("There is no region named that!");
            }
            RegionType type2 = region.getType();
            if (args.length == 3) {
                type2 = RegionType.valueOf(args[2].toUpperCase());
            }
            if (!args[0].equalsIgnoreCase("update")) {
                throw new CommandArgumentException();
            }
            RegionCommand.REGION_GENERATION_MAP.put((Object)sender.getSender(), (Object)new RegionGenerator("update", name, type2));
            this.send("Updating \"" + name + "\"");
            this.send((Object)ChatColor.DARK_AQUA + "Click the first corner of your region.");
        }
        else {
            if (args.length != 0) {
                throw new CommandArgumentException();
            }
            final StringBuilder result = new StringBuilder().append("Regions");
            for (final Region region2 : Region.getRegions()) {
                result.append("\n").append(" - ").append(region2.getName()).append(" (").append(region2.getType().name().toLowerCase()).append(")");
            }
            this.send(result.toString());
        }
    }
    
    static {
        RegionCommand.REGION_GENERATION_MAP = (Map<CommandSender, RegionGenerator>)new HashMap();
    }
}
