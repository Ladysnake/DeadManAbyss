package ladysnake.bansheenight.capability;

import ladylib.capability.AutoCapability;
import ladylib.capability.SimpleProvider;
import ladylib.misc.CalledThroughReflection;
import ladysnake.bansheenight.DeadManAbyss;
import ladysnake.bansheenight.DmaConfig;
import ladysnake.bansheenight.api.event.DmaNightEvent;
import ladysnake.bansheenight.api.event.DmaEventHandler;
import ladysnake.bansheenight.network.DmaNightMessage;
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
 * A world capability used to control whether a world can start an event
 */
@AutoCapability(DmaEventHandler.class)
@Mod.EventBusSubscriber(modid = DeadManAbyss.MOD_ID)
public class CapabilityDmaEvent implements DmaEventHandler {
    private static final float TRANSITION_TIME = 600f;

    private int ticksSinceLastEvent;
    private transient World owner;

    @CalledThroughReflection
    public CapabilityDmaEvent() {
        super();
    }

    public CapabilityDmaEvent(World owner) {
        this.owner = owner;
    }

    @Override
    public int getTicksSinceLastEvent() {
        return ticksSinceLastEvent;
    }

    @Override
    public void setTicksSinceLastEvent(int ticksSinceLastEvent) {
        if (ticksSinceLastEvent < 0) {
            this.startEvent();
        } else {
            this.stopEvent();
        }
        this.ticksSinceLastEvent = ticksSinceLastEvent;
    }

    @Override
    public float getTransitionProgress() {
        return isEventOccuring() ? this.ticksSinceLastEvent / -TRANSITION_TIME : 0;
    }

    @Override
    public void startEvent() {
        if (!MinecraftForge.EVENT_BUS.post(new DmaNightEvent.Start(owner))) {
            if (owner instanceof WorldServer) {
                sendToWorld(-1);
                SunsetHandler.subscribe(owner.provider.getDimension());
                WorldMutationHandler.subscribe();
            }
            this.ticksSinceLastEvent = -1;
        }
    }

    @Override
    public void stopEvent() {
        if (!MinecraftForge.EVENT_BUS.post(new DmaNightEvent.Stop(owner))) {
            if (owner instanceof WorldServer) {
                sendToWorld(0);
            }
            this.ticksSinceLastEvent = 0;
        }
    }

    private void sendToWorld(int ticks) {
        for (EntityPlayer player : owner.playerEntities) {
            PacketHandler.NET.sendTo(new DmaNightMessage(ticks), (EntityPlayerMP) player);
        }
    }

    @Override
    public void tick() {
        if (!this.isEventOccuring()) {
            this.ticksSinceLastEvent++;
        } else if (this.ticksSinceLastEvent > -TRANSITION_TIME) {
            this.ticksSinceLastEvent--;
        }
    }

    @Override
    public boolean isEventOccuring() {
        return this.ticksSinceLastEvent < 0;
    }

    private static final ResourceLocation NIGHT_EVENT = new ResourceLocation(DeadManAbyss.MOD_ID, "night");
    @CapabilityInject(DmaEventHandler.class)
    public static Capability<DmaEventHandler> CAPABILITY_DMA_EVENT;

    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<World> event) {
        int dimension = event.getObject().provider.getDimension();
        for(int dim : DmaConfig.dimWhiteList) {
            if(dim == dimension) {
                event.addCapability(NIGHT_EVENT, new SimpleProvider<>(CAPABILITY_DMA_EVENT, new CapabilityDmaEvent(event.getObject())));
                break;
            }
        }
    }

    public static class Storage implements Capability.IStorage<DmaEventHandler> {

        @Nullable
        @Override
        public NBTBase writeNBT(Capability<DmaEventHandler> capability, DmaEventHandler instance, EnumFacing side) {
            return new NBTTagInt(instance.getTicksSinceLastEvent());
        }

        @Override
        public void readNBT(Capability<DmaEventHandler> capability, DmaEventHandler instance, EnumFacing side, NBTBase nbt) {
            if (nbt instanceof NBTTagInt) {
                instance.setTicksSinceLastEvent(((NBTTagInt)nbt).getInt());
            }
        }
    }
}
