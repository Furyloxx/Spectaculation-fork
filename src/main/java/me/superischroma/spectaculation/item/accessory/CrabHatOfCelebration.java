package me.superischroma.spectaculation.item.accessory;

import me.superischroma.spectaculation.item.*;
import me.superischroma.spectaculation.item.accessory.*;

import java.util.Arrays;
import java.util.List;

public class CrabHatOfCelebration implements AccessoryStatistics {

    @Override
    public String getDisplayName() {
        return "Red Crab Hat of Celebration";
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
        return "67df75185a0dca0b5cede4064b787a3a14004fdd4dca4942246db00c0c03e51b";
    }
}
