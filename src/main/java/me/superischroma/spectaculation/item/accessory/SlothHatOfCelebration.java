package me.superischroma.spectaculation.item.accessory;

import me.superischroma.spectaculation.item.*;
import me.superischroma.spectaculation.item.accessory.*;

import java.util.Arrays;
import java.util.List;

public class SlothHatOfCelebration implements AccessoryStatistics {

    @Override
    public String getDisplayName() {
        return "Sloth Hat of Celebration";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.SPECIAL;
    }

    @Override
    public double getBaseMagicFind() {
        return 0.01;
    }

    @Override
    public List<String> getListLore() {
        return Arrays.asList(
                "&6Ability: Party Time",
                "&7Gain &b+1 Intelligence&7 while",
                "&7on your head for each SkyBlock",
                "&7year you've been playing.",
                "&7",
                "&7Your Bonus: &b+0 Intelligence&7"
        );
    }

    @Override
    public String getURL() {
        return "70bfaf5ab3f817fbed7263fc1c150b571f01b2f7ef17bc579610a495bc4cfee9";
    }
}