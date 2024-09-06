package me.furyloxx.detroitx;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.World;
import java.util.ArrayList;
import java.util.List;

public class Detroitx extends JavaPlugin {

        public void onEnable() {

                getLogger().info("§eLoading configuration...");

                this.loadConfig();

                getLogger().info("§aLoaded configuration!");

                getLogger().info("§eRegistering commands...");

                initializeCommands();

                getLogger().info("§aRegistered commands!");

                getLogger().info("§eInitializing Gamerules...");

                initializeGameRules();

                getLogger().info("§aInitizlized Gamerules!");

                getLogger().info("§e------------------------------------");
        getLogger().info("§bDetroitx plugin has been enabled!");
                getLogger().info("§bAuthor: Furyloxx");
        getLogger().info("§e------------------------------------");
        }

        @Override
        public void onDisable() {
                getLogger().info("§e-------------------------------------");
        getLogger().info("§bDetroitx plugin has been disabled!");
                getLogger().info("§bAuthor: Classy");
        getLogger().info("§e-------------------------------------");
        }
}
