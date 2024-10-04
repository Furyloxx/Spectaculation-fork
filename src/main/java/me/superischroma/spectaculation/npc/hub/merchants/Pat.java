package me.superischroma.spectaculation.npc.hub.merchants;

import me.superischroma.spectaculation.merchant.PatGUI;
import me.superischroma.spectaculation.npc.NPCParameters;
import me.superischroma.spectaculation.npc.SkyblockNPC;
import org.bukkit.entity.Player;

public class Pat extends SkyblockNPC {
    public Pat() {
        super(new NPCParameters() {

            @Override
            public String name() {
                return "Pat";
            }
            
            @Override
            public String[] holograms() {
                return new String[]{
                        "&fPat",
                        "&e&lCLICK",
                };
            }

            @Override
            public String texture() {
                return "ewogICJ0aW1lc3RhbXAiIDogMTU5Mzk0MjQ2ODIyMywKICAicHJvZmlsZUlkIiA6ICJiN2FjOTIxNDRmZTY0ZjQzYTQzOTRkMmM4MTYxYzY2NyIsCiAgInByb2ZpbGVOYW1lIiA6ICJvTXF0dCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS83OWNkNzQ0ZTc1ZTVjYTlhODA3NDhmODFlYmY5Mzg5YmI5NjRkYmVkYzYyM2U5Mzg5ZDUxODc1OWRmZmRmNzdkIgogICAgfQogIH0KfQ==";
            }

            @Override
            public String signature() {
                return "uaihg7+DYr+E9T0Hb2mPX7071yFP7Iu9Si+V9JbkA94Pxf2UIDM5iS5AYOGpLHSCQEqtnHMIElL9+JYtH7Tpqobf3YbRs9GEtFdKjnuFAA/UY83ui/mfZHBwwMa5hLxK4BXUCp2X808RcKgq/CclhWVnoRsGzuhR+HVDZ3CioiamBrohZYrllmL11tw89VLIrslfqZK4Ih0w9dvRReQLEQRMRf4sQWhQxZdE3nUb+IlhAE3rHVCmq/ZsOHbO6ZrRhM3XEyksiIPCGeh613nKiI2sDmkmkWm4RTY9nmM7/W6h+xbIb32LoXQ46KXhtYCkwiFc1DtIxPcwlSiHZn3lhexKT4TxWUZLRhWq7MhV/6wCh0VKBSkdzxntO6rD9qhTk63wG1UA+g4PWLGNpXisb3SnUwYKmNeGZ8xGwnnYYl+bTK2bH+k5gnUgiwCD/v2HyFVyTgpU/SKTBNkDxZCn5NSUYIPiL2/Ya3DduyOEDEnpNNopKUl8q20DTu8oV1Cg1z74Ii9WlIc3w33qe7MK9Kp1VNMwDFN6ZmQz5fDp4M9h0VdUHiTW6XAaSL7BBEG/U5ftJpvJVhmYuIrVqrr794lxnVk5AFrjXvPG0sEn9X32MKi0Qe1EKo9WDuxZoXnCI3M2YHK0OTvuw7SCcN6SeR8QACEaNNV26oFXNvZsRM0=";
            }

            @Override
            public String world() {
                return "world";
            }

            @Override
            public double x() {
                return -134.5;
            }

            @Override
            public double y() {
                return 73.0;
            }

            @Override
            public double z() {
                return -97.5;
            }

            @Override
            public float yaw() {
                return 97.8751f;
            }

            @Override
            public float pitch() {
                return 0.0f;
            }

            @Override
            public boolean looking() {
                return true;
            }

            @Override
            public void onInteract(Player player, SkyblockNPC npc) {
                PatGUI gui = new PatGUI();
                gui.open(player);
            }
        });
    }
}