package me.superischroma.spectaculation.entity.zombie;

import me.superischroma.spectaculation.entity.*;
import me.superischroma.spectaculation.item.SMaterial;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class FloridaMan extends BaseZombie
{
    @Override
    public String getEntityName()
    {
        return "Florida Man";
    }

    @Override
    public double getEntityMaxHealth()
    {
        return 120000000.0;
    }
    
    @Override
    public double getEntityLevel() {
      return 1000;
    }

    @Override
    public double getDamageDealt()
    {
        return 0.0;
    }

    @Override
    public double getMovementSpeed()
    {
        return 0.00;
    }

    public double getXPDropped()
    {
        return 0.0;
    }

    @Override
    public boolean isBaby()
    {
        return false;
    }

    @Override
    public boolean isVillager()
    {
        return false;
    }
}