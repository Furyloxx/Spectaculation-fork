package me.superischroma.spectaculation.item.accessory;

import me.superischroma.spectaculation.item.*;
import me.superischroma.spectaculation.item.accessory.*;

import java.util.Arrays;
import java.util.List;

public class ArtifactOfControl implements AccessoryStatistics {

    @Override
    public String getDisplayName() {
        return "Artifact of Control";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.LEGENDARY;
    }

    @Override
    public List<String> getListLore() {
        return Arrays.asList(
                "&7Holding this artifact will",
                "&7grant you &a2x&7 voting power in",
                "&7Mayor Elections!"
        );
    }

    @Override
    public String getURL() {
        return "31f748a43f3b3ada04f44d5d290a8b9bf583d93e1c83ab93c60c4dec1fde1c5c";
    }
}
