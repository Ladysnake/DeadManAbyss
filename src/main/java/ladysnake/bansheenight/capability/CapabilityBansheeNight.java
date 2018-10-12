package ladysnake.bansheenight.capability;

import ladylib.capability.*;
import ladylib.misc.CalledThroughReflection;
import ladysnake.bansheenight.*;
import ladysnake.bansheenight.api.event.*;
import ladysnake.bansheenight.network.*;
import ladysnake.bansheenight.worldevent.SunsetHandler;
import net.minecraft.entity.player.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * A world capability used to control whether a world can start a banshee night
 */
@AutoCapability(BansheeNightHandler.class)
@Mod.EventBusSubscriber(modid = BansheeNight.MOD_ID)
public class CapabilityBansheeNight implements BansheeNightHandler {
    private int ticksSinceLastNight;
    private transient World owner;

    @CalledThroughReflection
    public CapabilityBansheeNight() {
        super();
    }

    public CapabilityBansheeNight(World owner) {
        this.owner = owner;
    }

    @Override
    public int getTicksSinceLastNight() {
        return ticksSinceLastNight;
    }

    @Override
    public void startBansheeNight() {
        if (!MinecraftForge.EVENT_BUS.post(new BansheeNightEvent.Start(owner))) {
            if (owner instanceof WorldServer) {
                sendToWorld(true);
                SunsetHandler.subscribe(owner.provider.getDimension());
            }
            this.ticksSinceLastNight = -1;
        }
    }

    @Override
    public void stopBansheeNight() {
        if (!MinecraftForge.EVENT_BUS.post(new BansheeNightEvent.Stop(owner))) {
            if (owner instanceof WorldServer) {
                sendToWorld(false);
            }
            this.ticksSinceLastNight = 0;
        }
    }

    private void sendToWorld(boolean start) {
        for (EntityPlayer player : owner.playerEntities) {
            PacketHandler.NET.sendTo(new BansheeNightMessage(start), (EntityPlayerMP) player);
        }
    }

    @Override
    public void tick() {
        if (!this.isBansheeNightOccurring()) {
            this.ticksSinceLastNight++;
        }
    }

    @Override
    public boolean isBansheeNightOccurring() {
        return this.ticksSinceLastNight < 0;
    }

    private static final ResourceLocation BANSHEE_NIGHT = new ResourceLocation(BansheeNight.MOD_ID, "banshee_night");
    @CapabilityInject(BansheeNightHandler.class)
    public static Capability<BansheeNightHandler> CAPABILITY_BANSHEE_NIGHT;

    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<World> event) {
        int dimension = event.getObject().provider.getDimension();
        for(int dim : BansheeNightConfig.dimWhiteList) {
            if(dim == dimension) {
                event.addCapability(BANSHEE_NIGHT, new SimpleProvider<>(CAPABILITY_BANSHEE_NIGHT, new CapabilityBansheeNight(event.getObject())));
                break;
            }
        }
    }
}
