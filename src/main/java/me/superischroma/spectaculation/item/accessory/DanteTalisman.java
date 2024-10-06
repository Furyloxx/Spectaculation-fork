package me.superischroma.spectaculation.item.accessory;

import me.superischroma.spectaculation.item.*;
import me.superischroma.spectaculation.item.accessory.*;
import org.bukkit.ChatColor;

import java.util.*;

public class DanteTalisman implements AccessoryStatistics, AccessoryFunction {

    @Override
    public String getURL()
    {
        return "cf92982f1a302310643a20ce51623f8199b7545e70dc6b93ed6bd61dc42ff213";
    }

    @Override
    public String getDisplayName()
    {
        return "Dante Talisman";
    }

    @Override
    public Rarity getRarity()
    {
        return Rarity.COMMON;
    }

    @Override
    public List<String> getListLore() {
        final ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Official medal proving your");
        lore.add(ChatColor.GRAY + "eternal support for Dante.");
        lore.add(ChatColor.GRAY + "&7");
        lore.add(ChatColor.GRAY + "When damaged, you");
        lore.add(ChatColor.GRAY + "occasionally spit straight");
        lore.add(ChatColor.GRAY + "facts into the chat.");
        lore.add(ChatColor.GRAY + "&7");
        lore.add(ChatColor.DARK_GRAY + "'dante best' - Goons, 2021 - 2021");
        return lore;
    }

}