package me.superischroma.spectaculation.region;

import java.util.HashMap;
import java.lang.Object;
import org.bukkit.Bukkit;
import java.util.stream.Collectors;
import org.bukkit.entity.Player;
import java.util.Arrays;
import me.superischroma.spectaculation.util.SUtil;
import java.util.Random;
import org.bukkit.World;
import org.bukkit.Material;
import org.bukkit.block.Block;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;
import org.bukkit.entity.Entity;
import org.bukkit.block.BlockState;
import java.util.List;
import org.bukkit.Location;
import me.superischroma.spectaculation.Spectaculation;
import java.util.Map;

public class Region
{
    private static final Map<String, Region> REGION_CACHE;
    protected static final Spectaculation plugin;
    protected final String name;
    protected Location firstLocation;
    protected Location secondLocation;
    protected RegionType type;
    private List<BlockState> capture;
    
    public Region(final String name, final Location firstLocation, final Location secondLocation, final RegionType type) {
        this.name = name.toLowerCase();
        this.firstLocation = firstLocation;
        this.secondLocation = secondLocation;
        this.type = type;
        this.capture = null;
        Region.REGION_CACHE.put((Object)this.name, (Object)this);
    }
    
    public void save() {
        Region.plugin.regionData.save(this);
    }
    
    public void delete() {
        Region.REGION_CACHE.remove((Object)this.name);
        Region.plugin.regionData.delete(this);
    }
    
    public static List<Entity> getPlayersWithinRegionType(final RegionType type) {
        final List<Entity> players = (List<Entity>)new ArrayList();
        for (final Region region : getRegionsOfType(type)) {
            players.addAll((Collection)region.getPlayersWithinRegion());
        }
        return players;
    }
    
    public static Region getRegionOfEntity(final Entity entity) {
        final List<Region> possible = (List<Region>)new ArrayList();
        for (final Region region : getRegions()) {
            if (region.insideRegion(entity)) {
                possible.add((Object)region);
            }
        }
        possible.sort(Comparator.comparingInt(r -> r.getType().ordinal()));
        Collections.reverse((List)possible);
        return possible.isEmpty() ? null : ((Region)possible.get(0));
    }
    
    public static Region getQuickRegionOfEntity(final Entity entity) {
        for (final Region region : getRegions()) {
            if (region.insideRegion(entity)) {
                return region;
            }
        }
        return null;
    }
    
    public static Region getRegionOfBlock(final Block block) {
        final List<Region> possible = (List<Region>)new ArrayList();
        for (final Region region : getRegions()) {
            if (region.insideRegion(block)) {
                possible.add((Object)region);
            }
        }
        possible.sort(Comparator.comparingInt(r -> r.getType().ordinal()));
        Collections.reverse((List)possible);
        return (possible.size() != 0) ? ((Region)possible.get(0)) : null;
    }
    
    public boolean insideRegion(final Entity entity) {
        final List<Integer> bounds = this.getBounds();
        final Location location = entity.getLocation();
        final double x = location.getX();
        final double y = location.getY();
        final double z = location.getZ();
        return this.firstLocation != null && this.firstLocation.getWorld() != null && this.firstLocation.getWorld().getUID().equals((Object)location.getWorld().getUID()) && x >= (int)bounds.get(0) && x <= (int)bounds.get(1) && y >= (int)bounds.get(2) && y <= (int)bounds.get(3) && z >= (int)bounds.get(4) && z <= (int)bounds.get(5);
    }
    
    public boolean insideRegion(final Block block) {
        final List<Integer> bounds = this.getBounds();
        final Location location = block.getLocation();
        final double x = location.getX();
        final double y = location.getY();
        final double z = location.getZ();
        return this.firstLocation != null && this.firstLocation.getWorld() != null && this.firstLocation.getWorld().getUID().equals((Object)location.getWorld().getUID()) && x >= (int)bounds.get(0) && x <= (int)bounds.get(1) && y >= (int)bounds.get(2) && y <= (int)bounds.get(3) && z >= (int)bounds.get(4) && z <= (int)bounds.get(5);
    }
    
    public List<Location> getAvailableTeleportLocations() {
        final List<Location> locations = (List<Location>)new ArrayList();
        for (final Location location : this.getLocations()) {
            final Block block = location.getBlock();
            if (block.getType() != Material.AIR) {
                if (block.getType() == Material.WATER) {
                    continue;
                }
                final Block above = location.clone().add(0.0, 1.0, 0.0).getBlock();
                final Block top = location.clone().add(0.0, 2.0, 0.0).getBlock();
                if (above.getType() != Material.AIR && above.getType() != Material.WATER) {
                    continue;
                }
                if (top.getType() != Material.AIR && top.getType() != Material.WATER) {
                    continue;
                }
                locations.add((Object)above.getLocation());
            }
        }
        return locations;
    }
    
    public List<Location> getLocations() {
        if (!this.firstLocation.getWorld().getName().equals((Object)this.secondLocation.getWorld().getName())) {
            return null;
        }
        final List<Integer> bounds = this.getBounds();
        final World world = this.firstLocation.getWorld();
        final List<Location> locations = (List<Location>)new ArrayList();
        for (int y = (int)bounds.get(2); y <= (int)bounds.get(3); ++y) {
            for (int x = (int)bounds.get(0); x <= (int)bounds.get(1); ++x) {
                for (int z = (int)bounds.get(4); z <= (int)bounds.get(5); ++z) {
                    locations.add((Object)new Location(world, (double)x, (double)y, (double)z));
                }
            }
        }
        return locations;
    }
    
    public void captureRegion() {
        if (!this.firstLocation.getWorld().getName().equals((Object)this.secondLocation.getWorld().getName())) {
            return;
        }
        final List<Integer> bounds = this.getBounds();
        final World world = this.firstLocation.getWorld();
        final List<BlockState> states = (List<BlockState>)new ArrayList();
        for (int y = (int)bounds.get(2); y <= (int)bounds.get(3); ++y) {
            for (int x = (int)bounds.get(0); x <= (int)bounds.get(1); ++x) {
                for (int z = (int)bounds.get(4); z <= (int)bounds.get(5); ++z) {
                    states.add((Object)new Location(world, (double)x, (double)y, (double)z).getBlock().getState());
                }
            }
        }
        this.capture = states;
    }
    
    public void pasteRegionCapture() {
        if (this.capture == null) {
            return;
        }
        for (final BlockState state : this.capture) {
            state.getBlock().setType(state.getType());
            state.setRawData(state.getRawData());
            state.update();
        }
        this.capture = null;
    }
    
    public Location getRandomLocation() {
        final List<Integer> bounds = this.getBounds();
        final int minX = (int)bounds.get(0);
        final int maxX = (int)bounds.get(1);
        final int minY = (int)bounds.get(2);
        final int maxY = (int)bounds.get(3);
        final int minZ = (int)bounds.get(4);
        final int maxZ = (int)bounds.get(5);
        final int randomX = random(minX, maxX);
        final int randomY = random(minY, maxY);
        final int randomZ = random(minZ, maxZ);
        return new Location(this.firstLocation.getWorld(), (double)randomX, (double)randomY, (double)randomZ);
    }
    
    public static int random(final int min, final int max) {
        if (min < 0) {
            return new Random().nextInt(max + Math.abs(min) + 1) + min;
        }
        return new Random().nextInt(max - min + 1) + min;
    }
    
    public Location getRandomAvailableLocation() {
        final Location r = this.getRandomLocation();
        final List<Location> possible = (List<Location>)new ArrayList();
        for (int y = (int)this.getBounds().get(3); y >= (int)this.getBounds().get(2); --y) {
            final Block test = this.firstLocation.getWorld().getBlockAt(r.getBlockX(), y, r.getBlockZ());
            if (test.getType() != Material.AIR && test.getLocation().clone().add(0.0, 1.0, 0.0).getBlock().getType() == Material.AIR && test.getLocation().clone().add(0.0, 2.0, 0.0).getBlock().getType() == Material.AIR) {
                possible.add((Object)test.getLocation().clone().add(0.0, 1.0, 0.0));
            }
        }
        return possible.isEmpty() ? null : SUtil.getRandom(possible);
    }
    
    public List<Integer> getBounds() {
        final int sx = Math.min(this.firstLocation.getBlockX(), this.secondLocation.getBlockX());
        final int ex = Math.max(this.firstLocation.getBlockX(), this.secondLocation.getBlockX());
        final int sy = Math.min(this.firstLocation.getBlockY(), this.secondLocation.getBlockY());
        final int ey = Math.max(this.firstLocation.getBlockY(), this.secondLocation.getBlockY());
        final int sz = Math.min(this.firstLocation.getBlockZ(), this.secondLocation.getBlockZ());
        final int ez = Math.max(this.firstLocation.getBlockZ(), this.secondLocation.getBlockZ());
        return (List<Integer>)Arrays.asList((Object[])new Integer[] { sx, ex, sy, ey, sz, ez });
    }
    
    public List<Entity> getPlayersWithinRegion() {
        final List<Entity> entities = (List<Entity>)new ArrayList(this.firstLocation.getWorld().getEntitiesByClasses(new Class[] { Player.class }));
        return (List<Entity>)entities.stream().filter(this::insideRegion).collect(Collectors.toList());
    }
    
    public static Region create(final String name, final Location firstLocation, final Location secondLocation, final RegionType type) {
        return Region.plugin.regionData.create(name, firstLocation, secondLocation, type);
    }
    
    public static Region get(final String name) {
        if (Region.REGION_CACHE.containsKey((Object)name)) {
            return (Region)Region.REGION_CACHE.get((Object)name);
        }
        return Region.plugin.regionData.get(name);
    }
    
    public static List<Region> getRegions() {
        return (List<Region>)new ArrayList(Region.REGION_CACHE.values());
    }
    
    public static List<Region> getRegionsOfType(final RegionType type) {
        return (List<Region>)getRegions().stream().filter(region -> region.getType() == type).collect(Collectors.toList());
    }
    
    public static void cacheRegions() {
        for (final Region region : Region.plugin.regionData.getAll()) {
            Region.REGION_CACHE.put((Object)region.getName(), (Object)region);
        }
        final World islandWorld = Bukkit.getWorld("island");
        final double islandX = 0.0;
        final double islandY = 0.0;
        final double islandZ = 0.0;
        final Location islandFirstLocation = new Location(islandWorld, islandX, islandY, islandZ);
        final Location islandSecondLocation = new Location(islandWorld, islandX, islandY, islandZ);
        Region.REGION_CACHE.put((Object)"island", (Object)new Region("island", islandFirstLocation, islandSecondLocation, RegionType.PRIVATE_ISLAND));
        final World dungWorld = Bukkit.getWorld("f6");
        final double dungX = 0.0;
        final double dungY = 0.0;
        final double dungZ = 0.0;
        final Location dungLocation = new Location(dungWorld, dungX, dungY, dungZ);
        final Location dungSecondLocation = new Location(islandWorld, dungX, dungY, dungZ);
    }
    
    public static Region getIslandRegion() {
        return (Region)Region.REGION_CACHE.get((Object)"island");
    }
    
    public String getName() {
        return this.name;
    }
    
    public Location getFirstLocation() {
        return this.firstLocation;
    }
    
    public Location getSecondLocation() {
        return this.secondLocation;
    }
    
    public RegionType getType() {
        return this.type;
    }
    
    public void setFirstLocation(final Location firstLocation) {
        this.firstLocation = firstLocation;
    }
    
    public void setSecondLocation(final Location secondLocation) {
        this.secondLocation = secondLocation;
    }
    
    public void setType(final RegionType type) {
        this.type = type;
    }
    
    public List<BlockState> getCapture() {
        return this.capture;
    }
    
    static {
        REGION_CACHE = (Map)new HashMap();
        plugin = Spectaculation.getPlugin();
    }
}
