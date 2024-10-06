package me.superischroma.spectaculation.item.accessory;

import me.superischroma.spectaculation.item.*;
import me.superischroma.spectaculation.item.accessory.*;

import java.util.Arrays;
import java.util.List;

public class CandyRelic implements AccessoryStatistics {

    @Override
    public String getDisplayName() {
        return "Candy Relic";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.LEGENDARY;
    }

    @Override
    public List<String> getListLore() {
        return Arrays.asList(
                "&7Increases the drop chance of",
                "&7candies from mobs by &a20%",
                "&7during the &6Spooky",
                "&6Festival&7."
        );
    }

    @Override
    public String getURL() {
        return "94ff88f21e0149c4de89aa31c7dc0cd3429cb6c9ab237bbf94bf60910f789b99";
    }
}
