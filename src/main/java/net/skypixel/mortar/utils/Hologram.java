/*
 * Copyright (C) 2023 by Ruby Game Studios
 * Mortar is apart of Skyblock, which is licensed under the Creative Commons Non-Commercial 4.0 International License.
 *
 * You may not use this software for commercial use, however you are free
 * to modify, copy, redistribute, or build upon our codebase. You must give
 * appropriate credit, provide a link to the license, and indicate
 * if changes were made.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * For more information, visit https://creativecommons.org/licenses/by-nc/4.0/legalcode
 */
package net.skypixel.mortar.utils;

import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;

import net.skypixel.mortar.MortarLibrary;

import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Hologram {
    private static Map<UUID, HashMap<String, Hologram>> HOLOGRAM_LIST = new HashMap<>();

    private List<EntityArmorStand> ARMOR_STANDS = new ArrayList<>();
    private List<PacketPlayOutSpawnEntityLiving> SHOW_PACKETS = new ArrayList<>();
    private List<PacketPlayOutEntityDestroy> HIDE_PACKETS = new ArrayList<>();
    private List<Object> META_PACKETS = new ArrayList<>();
    private List<String> LINES;

    private Location start;
    private Object worldServer;
    private String name;

    public Hologram(String name, Location location, List<String> lines) {
        this.name = name;

        start = location;
        LINES = lines;
        worldServer = ((CraftWorld) location.getWorld()).getHandle();

        createPackets();
    }

    private void createPackets() {
        Location location = start.clone().add(0, 0.3 * LINES.size() + 1, 0);

        LINES.forEach(line -> {
            EntityArmorStand stand = new EntityArmorStand(((CraftWorld) location.getWorld()).getHandle(), location.getX(), location.getY(), location.getZ());

            stand.setCustomName(MortarUtils.color(line));
            stand.setSmall(true);
            stand.setBasePlate(false);
            stand.setArms(false);
            stand.setGravity(false);
            stand.setCustomNameVisible(true);
            stand.setInvisible(true);
            ((ArmorStand) stand.getBukkitEntity()).setMarker(true);

            ARMOR_STANDS.add(stand);
            SHOW_PACKETS.add(new PacketPlayOutSpawnEntityLiving(stand));
            HIDE_PACKETS.add(new PacketPlayOutEntityDestroy(stand.getId()));
            location.subtract(0, 0.3, 0);
        });
    }

    public void refresh(Player player) {
        hide(player);

        new BukkitRunnable() {
            @Override
            public void run() {
                show(player);
            }
        }.runTaskLater(MortarLibrary.getLibrary().getPlugin(), 5L);
    }

    public List<Object> getMetaPackets(List<String> text) {
        List<Object> metaPackets = new ArrayList<>();

        if (LINES.size() != text.size()) {
            throw new UnsupportedOperationException("You can't update lines that don't exist! TL;DR- A hologram attempted to update its text with a difference in size of lines.");
        }

        text.forEach(newLine -> {
            int index = text.indexOf(newLine);

            EntityArmorStand stand = ARMOR_STANDS.get(index);
            String oldLine = LINES.get(index);

            SHOW_PACKETS.set(index, new PacketPlayOutSpawnEntityLiving(stand));

            if (newLine.isEmpty() && !oldLine.isEmpty()) {
                metaPackets.add(HIDE_PACKETS.get(index));
            } else if (!newLine.isEmpty() && oldLine.isEmpty()) {
                metaPackets.add(SHOW_PACKETS.get(index));
            } else if (!oldLine.equals(newLine)) {
                metaPackets.add(new PacketPlayOutEntityMetadata(stand.getId(), stand.getDataWatcher(), true));
            }
        });

        LINES = text;
        return metaPackets;
    }

    public void show(Player player) {
        show(player, false);
    }

    public void hide(Player player) {
        hide(player, false);
    }

    public void show(Player player, boolean add) {
        LINES.stream()
                .filter(line -> !line.isEmpty())
                .forEach(line -> ((CraftPlayer) player).getHandle().playerConnection.sendPacket(SHOW_PACKETS.get(LINES.indexOf(line))));

        if (add) {
            HashMap<String, Hologram> holograms = HOLOGRAM_LIST.getOrDefault(player.getUniqueId(), new HashMap<>());

            holograms.put(this.name, this);

            HOLOGRAM_LIST.put(player.getUniqueId(), holograms);
        }
    }

    public void hide(Player player, boolean remove) {
        LINES.stream()
                .filter(line -> !line.isEmpty())
                .forEach(line -> ((CraftPlayer) player).getHandle().playerConnection.sendPacket(HIDE_PACKETS.get(LINES.indexOf(line))));

        if (remove) {
            HashMap<String, Hologram> holograms = HOLOGRAM_LIST.getOrDefault(player.getUniqueId(), new HashMap<>());

            if (holograms.isEmpty()) {
                HOLOGRAM_LIST.put(player.getUniqueId(), holograms);
                return;
            }

            holograms.remove(name);
            HOLOGRAM_LIST.put(player.getUniqueId(), holograms);
        }
    }
}
