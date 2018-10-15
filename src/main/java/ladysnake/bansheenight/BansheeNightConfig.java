package ladysnake.bansheenight;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = BansheeNight.MOD_ID)
@Mod.EventBusSubscriber(modid = BansheeNight.MOD_ID)
public class BansheeNightConfig {

    @Config.Name("Minimum Ticks Between Nights")
    @Config.Comment("The minimum time between two (naturally occuring) banshee nights, in ticks")
    public static int minTicksBetweenNights = 96_000;

    @Config.RangeDouble(min = 0.0D, max = 1.0D)
    @Config.Name("Banshee Night Probability")
    @Config.Comment("The probability of a banshee night starting at any given trigger")
    public static double bansheeNightProbability = 0.01D;

    @Config.RequiresMcRestart
    @Config.Name("Dimension Whitelist")
    @Config.Comment("The whitelist for dimensions in which the event can occur")
    public static int[] dimWhiteList = {0};

    @Config.Name("Trigger Config")
    @Config.Comment("Configure the specific triggers separately")
    public static final Triggers triggers = new Triggers();

    @Config.Name("Client Config")
    @Config.Comment("Configure client settings")
    public static final Client client = new Client();

    public static class Client {
        public boolean fancyShader = true;
    }

    public static class Triggers {

        @Config.Name("On Return From Portal")
        @Config.Comment("Trigger a banshee night when returning from another dimension")
        public boolean portal = false;

        //FIXME implement! ^Up
        @Config.Name("On Sunset")
        @Config.Comment("Trigger a banshee night when the sun sets")
        public boolean sunset = false;

        @Config.Name("On Blind Quartz Ritual")
        @Config.Comment({"Trigger a banshee night when tossing a blind quartz into water around midnight", "(this does NOT respect the probability, but the minimum tick cooldown)"})
        public boolean lotusRitual = true;
    }


    @SubscribeEvent
    public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if(event.getModID().equals(BansheeNight.MOD_ID))
            ConfigManager.sync(BansheeNight.MOD_ID, Config.Type.INSTANCE);
    }
}
