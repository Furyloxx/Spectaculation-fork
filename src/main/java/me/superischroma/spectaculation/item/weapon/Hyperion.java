package me.superischroma.spectaculation.item.weapon;

import me.superischroma.spectaculation.Spectaculation;
import me.superischroma.spectaculation.item.*;
import me.superischroma.spectaculation.util.SUtil;
import me.superischroma.spectaculation.util.PacketParticle;
import me.superischroma.spectaculation.user.PlayerUtils;
import me.superischroma.spectaculation.user.PlayerStatistics;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import com.google.common.util.concurrent.AtomicDouble;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Hyperion implements ToolStatistics, MaterialFunction, Ability {
    @Override
    public int getBaseDamage() {
        return 26000000;
    }

    @Override
    public double getBaseStrength() {
        return 1500000.0;
    }

    @Override
    public double getBaseIntelligence() {
        return 3500000.0;
    }

    public String getDisplayName() {
        return "Hyperion";
    }

    @Override
    public Rarity getRarity() {
        return Rarity.LEGENDARY;
    }

    @Override
    public GenericItemType getType() {
        return GenericItemType.WEAPON;
    }

    @Override
    public SpecificItemType getSpecificType() {
        return SpecificItemType.DUNGEON_SWORD;
    }

    @Override
    public String getLore() {
        return "§7Deals +§a50% §7damage to Withers. Grants §c+1 Damage §7and §a+2 §bIntelligence §7per §cCatacombs §7level.";
    }

    @Override
    public String getAbilityName() {
        return "Wither Impact";
    }

    @Override
    public String getAbilityDescription() {
        return "§7Teleports §a10 Blocks §7ahead of you. Then implode dealing damage to nearby enemies. Also applies the §6wither shield §7scroll ability reducing damage taken and granting an absorption shield for §e5 §7seconds.";
    }

    @Override
    public void onAbilityUse(Player player, SItem sItem) {
     PlayerStatistics statistics = PlayerUtils.STATISTICS_CACHE.get(player.getUniqueId());

     int range = 6;

     int entitiesHit = 0;
     for (Entity entity : player.getWorld().getNearbyEntities(player.getLocation(), range, range, range)) {
         if (entity instanceof LivingEntity) {
                     ++entitiesHit;
                     PlayerUtils.handleSpecEntity(entity, player, new AtomicDouble(SUtil.getAbilityDamage(10000, statistics.getIntelligence().addAll(), 0, player, entity)));
                     ((LivingEntity) entity).damage(SUtil.getAbilityDamage(10000, statistics.getIntelligence().addAll(), 0, player, entity));
                 }
             }
         }
     }

     if (entitiesHit >= 1) {
         DecimalFormat format = new DecimalFormat("#,###");
         int damage = (int) (SUtil.getAbilityDamage(10000, statistics.getIntelligence().addAll(), 0, player, player) * entitiesHit);
         player.sendMessage("§7Your Implosion hit §c" + entitiesHit + "§7 enemies dealing §c" + format.format(damage) + " damage§7.");
     }

        ArrayList<Block> Blocks = (ArrayList<Block>)player.getLineOfSight((HashSet<Byte>) null, 11);
        Vector vec = player.getLocation().getDirection();
        Location playerLoc = player.getLocation().add(vec);
        boolean solidBlockFound = false;
        for (int i = 0; i < 11; i++) {
            if (((Block)Blocks.get(i)).getType().isSolid() || ((Block)Blocks.get(i)).getRelative(BlockFace.DOWN).getType().isSolid()) {
                if (i - 1 >= 0) {
                    Location loc = ((Block)Blocks.get(i - 1)).getLocation();
                    loc.setYaw(playerLoc.getYaw());
                    loc.setPitch(playerLoc.getPitch());
                    loc.setX(loc.getX() + 0.5D);
                    loc.setY(loc.getY() - 1.0D);
                    loc.setZ(loc.getZ() + 0.5D);
                    player.teleport(loc);
                    player.playSound(loc, Sound.ENDERMAN_TELEPORT, 1.0F, 1.0F);
                    player.setFallDistance(0.0F);
                }
                solidBlockFound = true;
                break;
            }
        }
        if (!solidBlockFound) {
            Location loc = ((Block)Blocks.get(10)).getLocation();
            loc.setYaw(playerLoc.getYaw());
            loc.setPitch(playerLoc.getPitch());
            loc.setX(loc.getX() + 0.5D);
            loc.setY(loc.getY() - 1.0D);
            loc.setZ(loc.getZ() + 0.5D);
            player.teleport(loc);
            player.playSound(loc, Sound.ENDERMAN_TELEPORT, 1.0F, 1.0F);
            player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 3.0F, 1.0F);
            PlayerUtils.boostPlayer((PlayerStatistics)PlayerUtils.STATISTICS_CACHE.get(player.getUniqueId()), new PlayerBoostStatistics() {
                public String getDisplayName() {
                    return null;
                }

                public Rarity getRarity() {
                    return null;
                }

                public String getLore() {
                    return null;
                }

                public GenericItemType getType() {
                    return null;
                }

                public double getBaseSpeed() {
                    return 0.5D;
                }
            },  60L);
        }
        (new PacketParticle(EnumParticle.EXPLOSION_HUGE, player.getLocation(), false, 0.0F, 0.0F, 0.0F, 0.0F, 2)).sendAllPlayers();
        double absorptionShield = statistics.getCritDamage().next() * 1.5D / 2.0D;
        final EntityPlayer human = ((CraftPlayer)player).getHandle();
        human.setAbsorptionHearts((float)absorptionShield);
        (new BukkitRunnable() {
            public void run() {
                human.setAbsorptionHearts(0.0F);
            }
        }).runTaskLater((Plugin) Spectaculation.getPlugin(), 100L);

    }

    @Override
    public int getAbilityCooldownTicks() {
        return 3;
    }

    @Override
    public int getManaCost() {
        return 300;
    }
}