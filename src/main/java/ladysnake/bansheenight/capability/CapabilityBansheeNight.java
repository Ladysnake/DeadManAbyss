package ladysnake.bansheenight.capability;

import ladylib.capability.AutoCapability;
import ladylib.capability.SimpleProvider;
import ladysnake.bansheenight.BansheeNight;
import ladysnake.bansheenight.api.event.BansheeNightEvent;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * A world capability used to control whether a world can start a banshee night
 */
@AutoCapability(value = CapabilityBansheeNight.class, storage = Capability.IStorage.class)
@Mod.EventBusSubscriber(modid = BansheeNight.MOD_ID)
public class CapabilityBansheeNight {
    private int ticksSinceLastNight;
    private transient World owner;

    public CapabilityBansheeNight(World owner) {
        this.owner = owner;
    }

    public int getTicksSinceLastNight() {
        return ticksSinceLastNight;
    }

    public void startBansheeNight() {
        if (!MinecraftForge.EVENT_BUS.post(new BansheeNightEvent.Start(owner))) {
            this.ticksSinceLastNight = -1;
        }
    }

    public void stopBansheeNight() {
        if (!MinecraftForge.EVENT_BUS.post(new BansheeNightEvent.Stop(owner))) {
            this.ticksSinceLastNight = 0;
        }
    }

    public void tick() {
        if (!this.isBansheeNightOccurring()) {
            this.ticksSinceLastNight++;
        }
    }

    public boolean isBansheeNightOccurring() {
        return this.ticksSinceLastNight < 0;
    }

    private static final ResourceLocation BANSHEE_NIGHT = new ResourceLocation(BansheeNight.MOD_ID, "banshee_night");
    @CapabilityInject(CapabilityBansheeNight.class)
    public static Capability<CapabilityBansheeNight> CAPABILITY_BANSHEE_NIGHT;

    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<World> event) {
        event.addCapability(BANSHEE_NIGHT, new SimpleProvider<>(CAPABILITY_BANSHEE_NIGHT, new CapabilityBansheeNight(event.getObject())));
    }
}
