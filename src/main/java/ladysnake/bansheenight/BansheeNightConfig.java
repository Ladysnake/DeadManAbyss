package ladysnake.bansheenight;

import net.minecraftforge.common.config.Config;

@Config(modid = BansheeNight.MOD_ID)
public class BansheeNightConfig {

    public static int minTicksBetweenNights = 96_000;
    @Config.Comment("The probability of a banshee night starting at any given sunset (1 in N)")
    public static int bansheeNightProbability = 100;
}
