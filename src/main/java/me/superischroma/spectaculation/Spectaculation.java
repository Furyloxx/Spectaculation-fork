package me.superischroma.spectaculation;

import lombok.Getter;
import lombok.SneakyThrows;
import me.superischroma.spectaculation.auction.AuctionBid;
import me.superischroma.spectaculation.auction.AuctionEscrow;
import me.superischroma.spectaculation.auction.AuctionItem;
import me.superischroma.spectaculation.command.*;
import me.superischroma.spectaculation.config.Config;
import me.superischroma.spectaculation.entity.EntityPopulator;
import me.superischroma.spectaculation.entity.EntitySpawner;
import me.superischroma.spectaculation.entity.SEntityType;
import me.superischroma.spectaculation.entity.StaticDragonManager;
import me.superischroma.spectaculation.gui.GUIListener;
import me.superischroma.spectaculation.item.*;
import me.superischroma.spectaculation.item.pet.Pet;
import me.superischroma.spectaculation.listener.BlockListener;
import me.superischroma.spectaculation.listener.PlayerListener;
import me.superischroma.spectaculation.listener.ServerPingListener;
import me.superischroma.spectaculation.listener.WorldListener;
import me.superischroma.spectaculation.merchant.MerchantItemHandler;
import me.superischroma.spectaculation.region.Region;
import me.superischroma.spectaculation.region.RegionType;
import me.superischroma.spectaculation.sql.*;
import me.superischroma.spectaculation.slayer.SlayerQuest;
import me.superischroma.spectaculation.user.AuctionSettings;
import me.superischroma.spectaculation.user.User;
import me.superischroma.spectaculation.util.*;
import net.skypixel.mortar.MortarLibrary;
import net.skypixel.mortar.command.CommandRegistry;
import net.skypixel.mortar.npc.NPCRegistry;
import me.superischroma.spectaculation.npc.hub.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandMap;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;

public final class Spectaculation extends JavaPlugin
{
    @Getter
    private static Spectaculation plugin;


    public Config config;
    public Config heads;
    public Config blocks;
    public Config spawners;
    public CommandMap commandMap;
    public SQLDatabase sql;

    @Getter
    private static NPCRegistry npcRegistry;
    @Getter
    public String ip;
     @Getter
    private static MortarLibrary mortar;
    @Getter
    private static CommandRegistry commandRegistry;
    @Getter
    public String serverName;
    public SQLRegionData regionData;
    public SQLWorldData worldData;
    public CommandLoader cl;
    public Repeater repeater;

    @Override
    public void onLoad()
    {
        SLog.info("Loading Bukkit-serializable classes...");
        loadSerializableClasses();
    }

    @SneakyThrows
    @Override
    public void onEnable()
    {
        plugin = this;
                mortar = MortarLibrary.link(this);

        commandRegistry = mortar.getCommandRegistry();
        npcRegistry = mortar.getNpcRegistry();
        
        SLog.info("Loading islands...");
        loadIslandWorld();
        saveResource("private_island.schematic" , true); // copy island schematic to plugin folder
        SLog.info("Loading YAML data...");
        config = new Config("config.yml");
        heads = new Config("heads.yml");
        blocks = new Config("blocks.yml");
        spawners = new Config("spawners.yml");
        SLog.info("Loading command map...");
        try
        {
            Field f = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            f.setAccessible(true);
            commandMap = (CommandMap) f.get(Bukkit.getServer());
        }
        catch (IllegalAccessException | NoSuchFieldException e)
        {
            SLog.severe("Couldn't load command map: ");
            e.printStackTrace();
        }
        SLog.info("Loading SQL database...");
        sql = new SQLDatabase();
        regionData = new SQLRegionData();
        worldData = new SQLWorldData();
        cl = new CommandLoader();
        SLog.info("Starting server loop...");
        repeater = new Repeater();
        SLog.info("Loading commands...");
        loadCommands();
        SLog.info("Loading listeners...");
        loadListeners();
        SLog.info("Registering NPCs...");
         loadNPCs();
        SLog.info("Starting entity spawners...");
        EntitySpawner.startSpawnerTask();
        SLog.info("Establishing player regions...");
        Region.cacheRegions();
        SLog.info("Loading merchants items...");
        MerchantItemHandler.init();
        SLog.info("Loading auction items from disk...");
        AuctionItem.loadAuctionsFromDisk();
        SkyBlockCalendar.ELAPSED = plugin.config.getLong("timeElapsed");
        SLog.info("Synchronizing world time with calendar time and removing world entities...");
        for (World world : Bukkit.getWorlds())
        {
            for (Entity entity : world.getEntities())
            {
                if (entity instanceof HumanEntity) continue;
                entity.remove();
            }
            // Time Validator
            int time = (int) ((SkyBlockCalendar.ELAPSED % 24000) - 6000);
            if (time < 0)
                time += 24000;
            world.setTime(time);
        }
        SLog.info("Loading items...");
        Class.forName("me.superischroma.spectaculation.item.SMaterial"); // ensuring materials are loaded prior to this
        for (SMaterial material : SMaterial.values())
        {
            if (material.hasClass())
                material.getStatistics().load();
        }
        SLog.info("Converting craft recipes into Spectaculation recipes...");
        for (Iterator<Recipe> iter = Bukkit.recipeIterator(); iter.hasNext();)
        {
            Recipe recipe = iter.next();
            if (recipe.getResult() == null)
                continue;
            Material result = recipe.getResult().getType();
            if (recipe instanceof ShapedRecipe)
            {
                ShapedRecipe shaped = (ShapedRecipe) recipe;
                me.superischroma.spectaculation.item.ShapedRecipe specShaped = new me.superischroma.spectaculation.item.ShapedRecipe(SItem.convert(shaped.getResult()),
                        Groups.EXCHANGEABLE_RECIPE_RESULTS.contains(result))
                        .shape(shaped.getShape());
                for (Map.Entry<Character, ItemStack> entry : shaped.getIngredientMap().entrySet())
                {
                    if (entry.getValue() == null)
                        continue;
                    ItemStack stack = entry.getValue();
                    specShaped.set(entry.getKey(), SMaterial.getSpecEquivalent(stack.getType(), stack.getDurability()), stack.getAmount());
                }
            }
            if (recipe instanceof ShapelessRecipe)
            {
                ShapelessRecipe shapeless = (ShapelessRecipe) recipe;
                me.superischroma.spectaculation.item.ShapelessRecipe specShapeless = new me.superischroma.spectaculation.item.ShapelessRecipe(SItem.convert(shapeless.getResult()),
                        Groups.EXCHANGEABLE_RECIPE_RESULTS.contains(result));
                for (ItemStack stack : shapeless.getIngredientList())
                    specShapeless.add(SMaterial.getSpecEquivalent(stack.getType(), stack.getDurability()), stack.getAmount());
            }
        }
        this.ip = (config.getString("ip") != null) ? config.getString("ip") : "mc.skypixel.net";
        this.serverName = (config.getString("name") != null) ? config.getString("name") : "SKYBLOCK";
        SLog.info("Enabled " + this.getDescription().getFullName());
    }

    @Override
    public void onDisable()
    {
      npcRegistry = null;
        SLog.info("Killing all non-human entities...");
        for (World world : Bukkit.getWorlds())
        {
            for (Entity entity : world.getEntities())
            {
                if (entity instanceof HumanEntity) continue;
                entity.remove();
            }
        }
        SLog.info("Stopping server loop...");
        repeater.stop();
        SLog.info("Stopping entity spawners...");
        EntitySpawner.stopSpawnerTask();
        SLog.info("Ending dragon fight... (if one is currently active)");
        StaticDragonManager.endFight();
        SLog.info("Saving calendar time...");
        SkyBlockCalendar.saveElapsed();
        SLog.info("Saving user data...");
        for (User user : User.getCachedUsers())
            user.save();
        SLog.info("Saving auction data...");
        for (AuctionItem item : AuctionItem.getAuctions())
            item.save();
        plugin = null;
        SLog.info("Disabled " + this.getDescription().getFullName());
    }

    private void loadCommands()
    {
        cl.register(new SpectaculationCommand());
        cl.register(new RegionCommand());
        cl.register(new PlayEnumSoundCommand());
        cl.register(new PlayEnumEffectCommand());
        cl.register(new SpawnSpecCommand());
        cl.register(new ItemCommand());
        cl.register(new SpecEnchantmentCommand());
        cl.register(new SpecPotionCommand());
        cl.register(new SpecEffectsCommand());
        cl.register(new SpecReforgeCommand());
        cl.register(new ManaCommand());
        cl.register(new CoinsCommand());
        cl.register(new GUICommand());
        cl.register(new ItemBrowseCommand());
        cl.register(new SpecRarityCommand());
        cl.register(new RecombobulateCommand());
        cl.register(new NBTCommand());
        cl.register(new IslandCommand());
        cl.register(new DataCommand());
        cl.register(new SpecTestCommand());
        cl.register(new SoundSequenceCommand());
        cl.register(new BatphoneCommand());
        cl.register(new AbsorptionCommand());
        cl.register(new SkillsCommand());
        cl.register(new CollectionsCommand());
        cl.register(new MaterialDataCommand());
        cl.register(new EntitySpawnersCommand());
        cl.register(new AuctionHouseCommand());
        cl.register(new HubCommand());
        cl.register(new VisitCommand());
        cl.register(new AnvilCommand());
        cl.register(new BankerCommand());
        cl.register(new ReforgeCommand());

        // todo : use reflection to register command and listener automatically
    }

    private void loadListeners()
    {
        new BlockListener();
        new PlayerListener();
        new ServerPingListener();
        new ItemListener();
        new GUIListener();
        new WorldListener();
    }

    private void startPopulators()
    {
        // Deep Caverns
        new EntityPopulator(5, 10, 200, SEntityType.ENCHANTED_DIAMOND_SKELETON, RegionType.OBSIDIAN_SANCTUARY).start();
        new EntityPopulator(5, 10, 200, SEntityType.ENCHANTED_DIAMOND_ZOMBIE, RegionType.OBSIDIAN_SANCTUARY).start();
        new EntityPopulator(5, 10, 200, SEntityType.DIAMOND_ZOMBIE, RegionType.DIAMOND_RESERVE).start();
        new EntityPopulator(5, 10, 200, SEntityType.DIAMOND_SKELETON, RegionType.DIAMOND_RESERVE).start();
        new EntityPopulator(5, 15, 200, SEntityType.SMALL_SLIME, RegionType.SLIMEHILL).start();
        new EntityPopulator(5, 10, 200, SEntityType.MEDIUM_SLIME, RegionType.SLIMEHILL).start();
        new EntityPopulator(5, 5, 400, SEntityType.LARGE_SLIME, RegionType.SLIMEHILL).start();
        new EntityPopulator(5, 30, 400, SEntityType.PIGMAN, RegionType.PIGMENS_DEN).start();
        new EntityPopulator(5, 30, 400, SEntityType.LAPIS_ZOMBIE, RegionType.LAPIS_QUARRY).start();
        new EntityPopulator(5, 10, 400, SEntityType.SNEAKY_CREEPER, RegionType.GUNPOWDER_MINES).start();

        // The End
        new EntityPopulator(6, 20, 300, SEntityType.WEAK_ENDERMAN, RegionType.THE_END_NEST).start();
        new EntityPopulator(6, 20, 300, SEntityType.ENDERMAN, RegionType.THE_END_NEST).start();
        new EntityPopulator(6, 20, 300, SEntityType.STRONG_ENDERMAN, RegionType.THE_END_NEST).start();
        new EntityPopulator(10, 30, 200, SEntityType.ZEALOT, RegionType.DRAGONS_NEST).start();
        new EntityPopulator(1, 5, 1200, SEntityType.ENDER_CHEST_ZEALOT, RegionType.DRAGONS_NEST).start();
        new EntityPopulator(5, 20, 200, SEntityType.WATCHER, RegionType.DRAGONS_NEST).start();
        new EntityPopulator(5, 10, 200, SEntityType.OBSIDIAN_DEFENDER, RegionType.DRAGONS_NEST).start();

        // Spider's Den
        new EntityPopulator(5, 20, 300, SEntityType.SPLITTER_SPIDER, RegionType.SPIDERS_DEN_HIVE).start();
        new EntityPopulator(5, 20, 300, SEntityType.WEAVER_SPIDER, RegionType.SPIDERS_DEN_HIVE).start();
        new EntityPopulator(5, 20, 300, SEntityType.VORACIOUS_SPIDER, RegionType.SPIDERS_DEN_HIVE).start();
        new EntityPopulator(5, 20, 300, SEntityType.SPIDER_JOCKEY, RegionType.SPIDERS_DEN_HIVE).start();
        new EntityPopulator(5, 20, 300, SEntityType.DASHER_SPIDER, RegionType.SPIDERS_DEN_HIVE).start();

        // Hub
        new EntityPopulator(5, 10, 300, SEntityType.HIGH_LEVEL_SKELETON, RegionType.HIGH_LEVEL, (world) -> world.getTime() >= 13188 && world.getTime() <= 22812).start();
        new EntityPopulator(5, 15, 200, SEntityType.ZOMBIE, RegionType.GRAVEYARD).start();
        new EntityPopulator(5, 15, 200, SEntityType.ZOMBIE_VILLAGER, RegionType.GRAVEYARD).start();
        new EntityPopulator(5, 20, 200, SEntityType.WOLF, RegionType.RUINS).start();
        new EntityPopulator(2, 4, 200, SEntityType.OLD_WOLF, RegionType.RUINS).start();
        new EntityPopulator(5, 30, 200, SEntityType.CRYPT_GHOUL, RegionType.COAL_MINE_CAVES).start();
        new EntityPopulator(1, 1, 200, SEntityType.GOLDEN_GHOUL, RegionType.COAL_MINE_CAVES).start();

        // Howling Cave
        new EntityPopulator(4, 4, 200, SEntityType.SOUL_OF_THE_ALPHA, RegionType.HOWLING_CAVE).start();
        new EntityPopulator(5, 15, 200, SEntityType.HOWLING_SPIRIT, RegionType.HOWLING_CAVE).start();
        new EntityPopulator(5, 15, 200, SEntityType.PACK_SPIRIT, RegionType.HOWLING_CAVE).start();
    }
    private void loadIslandWorld(){
        new BlankWorldCreator("islands").createWorld();
    }

    private void loadSerializableClasses()
    {
        ConfigurationSerialization.registerClass(SlayerQuest.class, "SlayerQuest");
        ConfigurationSerialization.registerClass(Pet.PetItem.class, "PetItem");
        ConfigurationSerialization.registerClass(SItem.class, "SItem");
        ConfigurationSerialization.registerClass(AuctionSettings.class, "AuctionSettings");
        ConfigurationSerialization.registerClass(AuctionEscrow.class, "AuctionEscrow");
        ConfigurationSerialization.registerClass(SerialNBTTagCompound.class, "SerialNBTTagCompound");
        ConfigurationSerialization.registerClass(AuctionBid.class, "AuctionBid");
    }
     public void loadNPCs() {
        SLog.info("Registering mortar npcs");
        npcRegistry.register(new BankerNPC(), false);
        npcRegistry.register(new AuctionMaster(), false);
        npcRegistry.startTasks();
    }
}
