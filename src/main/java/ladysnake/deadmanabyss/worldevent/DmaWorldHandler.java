package ladysnake.deadmanabyss.worldevent;

import ladylib.compat.EnhancedBusSubscriber;
import ladysnake.deadmanabyss.*;
import ladysnake.deadmanabyss.api.event.DmaEventHandler;
import ladysnake.deadmanabyss.capability.*;
import ladysnake.deadmanabyss.item.ItemBlindQuartz;
import ladysnake.deadmanabyss.network.*;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.*;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.fml.common.gameevent.*;

@EnhancedBusSubscriber(owner = DeadManAbyss.MOD_ID)
public class DmaWorldHandler {
    private static final ResourceLocation NETHER_ADVANCEMENT = new ResourceLocation("minecraft:nether/root");

    @SubscribeEvent
    public void onReturnFromPortal(PlayerEvent.PlayerChangedDimensionEvent event) {
        if(!event.player.world.isRemote) {
            WorldServer world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(event.toDim);
            DmaEventHandler cap = world.getCapability(CapabilityDmaEvent.CAPABILITY_DMA_EVENT, null);
            if(cap != null) {
                if(DmaConfig.triggers.portal) {
                    if(cap.getTicksSinceLastEvent() >= DmaConfig.minTicksBetweenEvents && isPlayerReady((EntityPlayerMP) event.player) && world.rand.nextDouble() < DmaConfig.eventProbability) {
                        cap.startEvent(); //TODO randomize it a bit more? ^Up
                    }
                }
                PacketHandler.NET.sendTo(new DmaNightMessage(cap.getTicksSinceLastEvent()), (EntityPlayerMP) event.player);
            }
        }
    }

    private static void notifyPlayer(PlayerEvent event) {
        World world = event.player.world;
        if(!world.isRemote) {
            DmaEventHandler cap = world.getCapability(CapabilityDmaEvent.CAPABILITY_DMA_EVENT, null);
            if(cap != null) {
                PacketHandler.NET.sendTo(new DmaNightMessage(cap.getTicksSinceLastEvent()), (EntityPlayerMP) event.player);
            }
        }
    }

    @SubscribeEvent
    public void onRespawn(PlayerEvent.PlayerRespawnEvent event) {
        notifyPlayer(event);
    }

    @SubscribeEvent
    public void onLogIn(PlayerEvent.PlayerLoggedInEvent event) {
        notifyPlayer(event);
    }

    public static boolean isPlayerReady(EntityPlayerMP player) {
        return player.getAdvancements().getProgress(player.server.getAdvancementManager().getAdvancement(NETHER_ADVANCEMENT)).isDone();
    }

    private int tickCounter;

    @SubscribeEvent
    public void onTickWorldTick(TickEvent.WorldTickEvent event) {
        if(event.phase == TickEvent.Phase.START && !event.world.isRemote) {
            DmaEventHandler cap = event.world.getCapability(CapabilityDmaEvent.CAPABILITY_DMA_EVENT, null);
            if(cap != null) {
                cap.tick();
                if(!SunsetHandler.isActive(event.world.provider.getDimension())) {
                    if(cap.isEventOccuring()) {
                        if(++tickCounter % 3 == 0) event.world.setWorldTime(event.world.getWorldTime() - 2); //TODO adjust night duration rate? ^Up
                        float angle = event.world.getCelestialAngle(1.0F);

                        if(angle > 0.76F) cap.stopEvent(); //stop the night at dawn
                    }
                    else {
                        //TODO randomly start night if time is around sunset, but only check once per ingame day! ^Up
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        if(!event.getWorld().isRemote) {
            event.getWorld().addEventListener(new DmaWorldListener((WorldServer) event.getWorld()));
        }
    }

    @SubscribeEvent
    public void onLivingSpawnCheckSpawn(LivingSpawnEvent.CheckSpawn event) {
        DmaEventHandler cap = event.getWorld().getCapability(CapabilityDmaEvent.CAPABILITY_DMA_EVENT, null);
        if(cap != null && cap.isEventOccuring() && event.getEntity().getCapability(CapabilityDmaSpawnable.DMA_SPAWNABLE_CAPABILITY, null) == null) {
            event.setResult(Event.Result.DENY);
        }
    }

    @SubscribeEvent
    public void onPlayerDropQuartz(ItemTossEvent event) {
        if(!event.getPlayer().world.isRemote) {
            EntityItem item = event.getEntityItem();
            if(item.getItem().getItem() instanceof ItemBlindQuartz) {
                if(isPlayerReady((EntityPlayerMP) event.getPlayer())) { //prevent automation of the event
                    item.getEntityData().setBoolean("Trigger", true);
                }
            }
        }

    }
}
