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
                return "AQPJtP1C02FBvFZQlkE2K+sThg8GlOy9JrZJCu27PRECsZtc4UQkoJQRanPKiJn7aw6iilv/VZH9uKuYY9X51gSfmI13lNL5/t279V8CVsNIgh6z7bsLgS0jtqTHrMe2hhDc5hZ4UEV7THDtszs+6nNY+Ua56kU3S1RFx30mPccjLg3tB50vPgT7npHaqsCsS90qs1iJzy9FrQ2p2+tM3tQmeSIgb0zqjWbTw3JDIZPnsleYEZ2nfhLRym4YTOTuv/dc3HsQvB8rxmGhjEkZNm5ki8o3uCRkkA9umrbb/t3iLY07l8yg90/EHItmgTXcBDj3h9QWbtQqiESQi9WzAYD/A1yN0tIZRz9Wi0iK8BIy9iRWXBiw4tnpmBGz7Xx8gMJVvV5znatL1AB/NZbbdheCtvlwGRAM+sRGU6lPicydhXayAjd7UpfavQS6RECtqRCq7GOss/GP3QZZQALpKHrTMRIDSVtK/wJ74FGx01gjJck9JkRqw48SDgvjxg6G2nzCkuDvNljVw3isOgT/TqranvwBTrIlzQmb8QfnvPcP7yUImqvGGW1R1bq9sQM1FyFufmq6QS3nBhTsEqSzDP8ADz5uty3qP6gaCmKy1dN6cdBeABpJOxgt5ySsBHjyDcIbk5a7Jh+YHWNP3Tq5QfrVdl+6pXVnQqYpnmYMbMU=";
            }

            @Override
            public String getTexture() {
                return "AQPJtP1C02FBvFZQlkE2K+sThg8GlOy9JrZJCu27PRECsZtc4UQkoJQRanPKiJn7aw6iilv/VZH9uKuYY9X51gSfmI13lNL5/t279V8CVsNIgh6z7bsLgS0jtqTHrMe2hhDc5hZ4UEV7THDtszs+6nNY+Ua56kU3S1RFx30mPccjLg3tB50vPgT7npHaqsCsS90qs1iJzy9FrQ2p2+tM3tQmeSIgb0zqjWbTw3JDIZPnsleYEZ2nfhLRym4YTOTuv/dc3HsQvB8rxmGhjEkZNm5ki8o3uCRkkA9umrbb/t3iLY07l8yg90/EHItmgTXcBDj3h9QWbtQqiESQi9WzAYD/A1yN0tIZRz9Wi0iK8BIy9iRWXBiw4tnpmBGz7Xx8gMJVvV5znatL1AB/NZbbdheCtvlwGRAM+sRGU6lPicydhXayAjd7UpfavQS6RECtqRCq7GOss/GP3QZZQALpKHrTMRIDSVtK/wJ74FGx01gjJck9JkRqw48SDgvjxg6G2nzCkuDvNljVw3isOgT/TqranvwBTrIlzQmb8QfnvPcP7yUImqvGGW1R1bq9sQM1FyFufmq6QS3nBhTsEqSzDP8ADz5uty3qP6gaCmKy1dN6cdBeABpJOxgt5ySsBHjyDcIbk5a7Jh+YHWNP3Tq5QfrVdl+6pXVnQqYpnmYMbMU=";
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
