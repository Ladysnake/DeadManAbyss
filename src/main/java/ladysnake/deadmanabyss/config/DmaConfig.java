package ladysnake.deadmanabyss.config;

import ladysnake.deadmanabyss.DeadManAbyss;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

//TODO add lang keys for all config entries
@Config(modid = DeadManAbyss.MOD_ID)
@Mod.EventBusSubscriber(modid = DeadManAbyss.MOD_ID)
public class DmaConfig {

    @Config.Name("Minimum Ticks Between Events")
    @Config.Comment("The minimum time between two (naturally occuring or forced) events, in ticks")
    public static int minTicksBetweenEvents = 96_000;

    @Config.RangeDouble(min = 0.0D, max = 1.0D)
    @Config.Name("Event Probability")
    @Config.Comment("The probability of an event starting at any given trigger")
    public static double eventProbability = 0.025D;

    @Config.RequiresMcRestart
    @Config.Name("Dimension Whitelist")
    @Config.Comment({"The whitelist for dimensions in which the event can occur", "by default, the event only affects the vanilla overworld, but other dimensions can be added here"})
    public static int[] dimWhiteList = {
            0
    };

    @Config.RequiresMcRestart
    @Config.Name(("Entity Whitelist"))
    @Config.Comment("The whitelist for entities that can spawn during the event")
    public static String[] entityWhiteList = {
            "dma:screecher",
            "dma:blind",
            "harvestersnight:harvester",
            "eyesinthedarkness:eyes",
            "scarecrows:scarecrow"
    };

    @Config.Name("Max Hearing Distance")
    @Config.Comment({"The maximum distance screechers at which screechers can possibly hear a sound"})
    public static int maxScreecherHearingDistance = 64;

    @Config.Name("Trigger Config")
    @Config.Comment("Configure the specific triggers separately")
    public static final Triggers triggers = new Triggers();

    @Config.Name("Client Config")
    @Config.Comment("Configure client settings")
    public static final Client client = new Client();

    @Config.Name("Entity Spawn Config")
    @Config.Comment("Configure the spawn rates of entities")
    public static final Spawn spawns = new Spawn();

    public static class Client {
        @Config.Name("Fancy Shader")
        @Config.Comment("If set to false, a barebones more performant shader will be used for the event.")
        public boolean fancyShader = true;
    }

    public static class Triggers {

        @Config.Name("On Return From Portal")
        @Config.Comment("Trigger an event when returning from another dimension")
        public boolean portal = true;

        //FIXME implement! ^Up
        @Config.Name("On Sunset")
        @Config.Comment("Trigger an event when the sun sets")
        public boolean sunset = false;

        @Config.Name("On Blind Quartz Ritual")
        @Config.Comment({"Trigger an event when tossing a blind quartz into water", "(this does NOT respect the probability, but the minimum tick cooldown)"})
        public boolean quartzRitual = true;
    }

    public static class Spawn {

        @Config.RequiresMcRestart
        @Config.Name("Screecher")
        public final EntitySpawn screecher = new EntitySpawn(1, 1, 1);

        @Config.RequiresMcRestart
        @Config.Name("Blind")
        public final EntitySpawn blind = new EntitySpawn(2, 1, 1);

    }

    @SubscribeEvent
    public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if(event.getModID().equals(DeadManAbyss.MOD_ID))
            ConfigManager.sync(DeadManAbyss.MOD_ID, Config.Type.INSTANCE);
    }
}
