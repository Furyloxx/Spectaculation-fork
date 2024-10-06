package me.superischroma.spectaculation.item.accessory;

import me.superischroma.spectaculation.item.*;
import me.superischroma.spectaculation.item.accessory.*;

public class BatArtifact implements AccessoryStatistics {

    @Override
    public String getDisplayName() {
        return "Bat Artifact";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.LEGENDARY;
    }

    @Override
    public double getBaseIntelligence() {
        return 3;
    }

    @Override
    public double getBaseSpeed() {
        return 0.3;
    }

    @Override
    public double getBaseHealth() {
        return 5;
    }

    @Override
    public String getURL() {
        return "6681a72da7263ca9aef066542ecca7a180c40e328c0463fcb114cb3b83057552";
    }
}
