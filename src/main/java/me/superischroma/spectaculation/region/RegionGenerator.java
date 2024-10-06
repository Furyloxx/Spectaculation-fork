package me.superischroma.spectaculation.region;

import org.bukkit.Location;

public class RegionGenerator
{
    private final String modificationType;
    private String name;
    private Location firstLocation;
    private Location secondLocation;
    private RegionType type;
    private int phase;
    
    public RegionGenerator(final String modificationType, final String name, final RegionType type) {
        this.modificationType = modificationType;
        this.name = name;
        this.type = type;
        this.phase = 1;
    }
    
    public String getModificationType() {
        return this.modificationType;
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
    
    public int getPhase() {
        return this.phase;
    }
    
    public void setName(final String name) {
        this.name = name;
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
    
    public void setPhase(final int phase) {
        this.phase = phase;
    }
}
