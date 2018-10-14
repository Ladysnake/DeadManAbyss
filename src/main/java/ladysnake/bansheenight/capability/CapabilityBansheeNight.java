package ladysnake.bansheenight.capability;

import ladylib.capability.AutoCapability;
import ladylib.capability.SimpleProvider;
import ladylib.misc.CalledThroughReflection;
import ladysnake.bansheenight.BansheeNight;
import ladysnake.bansheenight.BansheeNightConfig;
import ladysnake.bansheenight.api.event.BansheeNightEvent;
import ladysnake.bansheenight.api.event.BansheeNightHandler;
import ladysnake.bansheenight.network.BansheeNightMessage;
import ladysnake.bansheenight.network.PacketHandler;
import ladysnake.bansheenight.worldevent.SunsetHandler;
import ladysnake.bansheenight.worldevent.WorldMutationHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nullable;

/**
 * A world capability used to control whether a world can start a banshee night
 */
@AutoCapability(BansheeNightHandler.class)
@Mod.EventBusSubscriber(modid = BansheeNight.MOD_ID)
public class CapabilityBansheeNight implements BansheeNightHandler {
    private static final float TRANSITION_TIME = 600f;

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
    public void setTicksSinceLastNight(int ticksSinceLastNight) {
        if (ticksSinceLastNight < 0) {
            this.startBansheeNight();
        } else {
            this.stopBansheeNight();
        }
        this.ticksSinceLastNight = ticksSinceLastNight;
    }

    @Override
    public float getTransitionProgress() {
        return isBansheeNightOccurring() ? this.ticksSinceLastNight / -TRANSITION_TIME : 0;
    }

    @Override
    public void startBansheeNight() {
        if (!MinecraftForge.EVENT_BUS.post(new BansheeNightEvent.Start(owner))) {
            if (owner instanceof WorldServer) {
                sendToWorld(-1);
                SunsetHandler.subscribe(owner.provider.getDimension());
                WorldMutationHandler.subscribe();
            }
            this.ticksSinceLastNight = -1;
        }
    }

    @Override
    public void stopBansheeNight() {
        if (!MinecraftForge.EVENT_BUS.post(new BansheeNightEvent.Stop(owner))) {
            if (owner instanceof WorldServer) {
                sendToWorld(0);
            }
            this.ticksSinceLastNight = 0;
        }
    }

    private void sendToWorld(int ticks) {
        for (EntityPlayer player : owner.playerEntities) {
            PacketHandler.NET.sendTo(new BansheeNightMessage(ticks), (EntityPlayerMP) player);
        }
    }

    @Override
    public void tick() {
        if (!this.isBansheeNightOccurring()) {
            this.ticksSinceLastNight++;
        } else if (this.ticksSinceLastNight > -TRANSITION_TIME) {
            this.ticksSinceLastNight--;
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

    public static class Storage implements Capability.IStorage<BansheeNightHandler> {

        @Nullable
        @Override
        public NBTBase writeNBT(Capability<BansheeNightHandler> capability, BansheeNightHandler instance, EnumFacing side) {
            return new NBTTagInt(instance.getTicksSinceLastNight());
        }

        @Override
        public void readNBT(Capability<BansheeNightHandler> capability, BansheeNightHandler instance, EnumFacing side, NBTBase nbt) {
            if (nbt instanceof NBTTagInt) {
                instance.setTicksSinceLastNight(((NBTTagInt)nbt).getInt());
            }
        }
    }
}
