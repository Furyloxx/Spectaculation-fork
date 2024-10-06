package me.superischroma.spectaculation.npc.hub;

import me.superischroma.spectaculation.gui.BankerGUI;
import me.superischroma.spectaculation.util.SUtil;
import net.skypixel.mortar.npc.MortarNPC;
import net.skypixel.mortar.npc.NPCInteractionEvent;
import net.skypixel.mortar.npc.NPCMeta;

import java.util.Arrays;

public class BankerNPC extends MortarNPC {
    public BankerNPC() {
        super(new NPCMeta() {
            @Override
            public String getName() {
                return "hub_banker";
            }

            @Override
            public int getId() {
                return 1;
            }

            @Override
            public String getSignature() {
                return "iLrTZiVANLmw8dSM3A59OLKAWYNkvg+MocfzluRU2xYYKW1682hCHBian6MMf+Ke+oHlihtkc5OSA//oCA8zuyP/FpFni+jzOTBUJ8CA/Kh9zeGBKRpKw2P0ChAwIfsBnqG6PEyo5HGCt4ImqBGgWQ8qLizZqaA60qpKWLt78FPX0ruJ+wg35B+BN4vN1MoXv/WFX2BSBdD+GBk9hOysAYoPCIop2F3d2qBPebI8OCawGXZcUegR/N4lCE5Q9bbowdCqUuzr301fPwKK3eSTD4gMuha8fcfD8o4DI88xV9lhU5WpairEBzG/yF/6wFu40A8+HoOlFLlmDAggGibtShHrkowa2JuclYYyMFsbcQVW3LG1XjdneUBR9Owd6B0JmNYKI2Uuh4vaQi05Oy4QbdqpjoJAr/sP73pj6nc2HdwC3phHuoIh/Cry7Q5sxNyDBsCEzLbwEDxROPcvL3FUbvgaCB+LmZ6dwtga1hTT8gEbXkjrrBvhUk6qvCgERvG93AeuPWZwANMn1lR5klN5aOxlsjGSmeiSwI1BgdlVybV1AAiQdN9JcNNETtFvef31J+wiU1QsWTewmMs/Ltrr5EI4FKWT+tFxDbvl1jcAeCc2NYmINVLDOR4viNfEdg4MWBV+pzGKcgydniUv0Nh4xehWd9ZLml/nZrcmDKy2Wpk=";
            }

            @Override
            public String getTexture() {
                return "ewogICJ0aW1lc3RhbXAiIDogMTYzODAwMDI0NzY2NSwKICAicHJvZmlsZUlkIiA6ICI2Njg5MDJmYjI1YTY0NDBhODBmM2Y2MjZhYTk0MzBmYyIsCiAgInByb2ZpbGVOYW1lIiA6ICJCYW5rZXIiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmZkODNhY2NhOWFmM2JiYWQ3MDVmNzE0MzU1ZDk0MTA3NDEyY2E0ZWJiZDRjZTkzOTE2MGMxYmUxMGNjZDFhMiIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9LAogICAgIkNBUEUiIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2U3ZGZlYTE2ZGM4M2M5N2RmMDFhMTJmYWJiZDEyMTYzNTljMGNkMGVhNDJmOTk5OWI2ZTk3YzU4NDk2M2U5ODAiCiAgICB9CiAgfQp9";
            }

            @Override
            public String getDisplayName() {
                return "Banker";
            }

            @Override
            public String getWorldName() {
                return "world";
            }

            @Override
            public double getX() {
                return -24.504D;
            }

            @Override
            public double getY() {
                return 71D;
            }

            @Override
            public double getZ() {
                return -58.657D;
            }

            @Override
            public float getYaw() {
                return -179.1F;
            }

            @Override
            public float getPitch() {
                return 0.4F;
            }

            @Override
            public boolean canLook() {
                return true;
            }
        });
    }

    @Override
    public void onInteract(NPCInteractionEvent event) {
        speak(event.getPlayer(), Arrays.asList(
                "Hello &e" + event.getPlayer().getName() + "&f!",
                "You can deposit or withdraw your &6coins&f here and earn interest on them!"
        ));

        SUtil.delay(() -> new BankerGUI().open(event.getPlayer()), 60);
    }
}
