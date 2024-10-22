package me.superischroma.spectaculation.entity.nms;

import net.minecraft.server.v1_8_R3.World;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;

public class ProtectorDragon extends Dragon
{
    public ProtectorDragon(World world)
    {
        super(world, Dragon.DEFAULT_SPEED_MODIFIER, Dragon.DEFAULT_DAMAGE_DEGREE_RANGE, Dragon.DEFAULT_ATTACK_COOLDOWN);
    }

    public ProtectorDragon()
    {
        this(((CraftWorld) Bukkit.getWorlds().get(0)).getHandle());
    }

    @Override
    public String getEntityName()
    {
        return "Protector Dragon";
    }

    @Override
    public double getEntityMaxHealth()
    {
        return 9000000.0;
    }

    @Override
    public double getDamageDealt()
    {
        return 1300.0;
    }
    
    @Override
    public double getEntityLevel() {
      return 1.0;
    }
}