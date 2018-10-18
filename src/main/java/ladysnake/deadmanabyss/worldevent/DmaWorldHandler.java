package ladysnake.deadmanabyss.worldevent;

import ladysnake.deadmanabyss.DeadManAbyss;
import ladysnake.deadmanabyss.DmaConfig;
import ladysnake.deadmanabyss.api.event.DmaEventHandler;
import ladysnake.deadmanabyss.capability.CapabilityDmaEvent;
import ladysnake.deadmanabyss.capability.CapabilityDmaSpawnable;
import ladysnake.deadmanabyss.item.ItemBlindQuartz;
import ladysnake.deadmanabyss.network.DmaNightMessage;
import ladysnake.deadmanabyss.network.PacketHandler;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Mod.EventBusSubscriber(modid = DeadManAbyss.MOD_ID)
public class DmaWorldHandler {
    private static final ResourceLocation NETHER_ADVANCEMENT = new ResourceLocation("minecraft:nether/root");

    @SubscribeEvent
    public static void onReturnFromPortal(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.player instanceof EntityPlayerMP && ((EntityPlayerMP) event.player).connection != null) {
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
    public static void onRespawn(PlayerEvent.PlayerRespawnEvent event) {
        notifyPlayer(event);
    }

    @SubscribeEvent
    public static void onLogIn(PlayerEvent.PlayerLoggedInEvent event) {
        // FIXME y u no work on dedotated servers - pyro
        if (FMLCommonHandler.instance().getSide().isClient()) {
            notifyPlayer(event);
        }
    }

    public static boolean isPlayerReady(EntityPlayerMP player) {
        return player.isCreative() || player.getAdvancements().getProgress(player.server.getAdvancementManager().getAdvancement(NETHER_ADVANCEMENT)).isDone();
    }

    private static int tickCounter;

    @SubscribeEvent
    public static void onTickWorldTick(TickEvent.WorldTickEvent event) {
        if(event.phase == TickEvent.Phase.START && !event.world.isRemote) {
            DmaEventHandler cap = event.world.getCapability(CapabilityDmaEvent.CAPABILITY_DMA_EVENT, null);
            if(cap != null) {
                cap.tick();
                if(!SunsetHandler.isActive(event.world.provider.getDimension())) {
                    if(cap.isEventOccuring()) {
                        if(++tickCounter % 3 == 0) event.world.setWorldTime(event.world.getWorldTime() - 2); //TODO adjust night duration rate? ^Up
                        float angle = event.world.getCelestialAngle(1.0F);

                        if(angle > 0.76F) cap.stopEvent(); //stop the night at dawn
                    } else {
                        //TODO randomly start night if time is around sunset, but only check once per ingame day! ^Up
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onWorldLoad(WorldEvent.Load event) {
        if(!event.getWorld().isRemote) {
            DmaWorldListener worldListener = new DmaWorldListener((WorldServer) event.getWorld());
            event.getWorld().addEventListener(worldListener);
        }
    }

    @SubscribeEvent
    public static void onLivingSpawnCheckSpawn(LivingSpawnEvent.CheckSpawn event) {
        DmaEventHandler cap = event.getWorld().getCapability(CapabilityDmaEvent.CAPABILITY_DMA_EVENT, null);
        if(cap != null && cap.isEventOccuring() && event.getEntity().getCapability(CapabilityDmaSpawnable.DMA_SPAWNABLE_CAPABILITY, null) == null) {
            event.setResult(Event.Result.DENY);
        }
    }

    @SubscribeEvent
    public static void onPlayerDropQuartz(ItemTossEvent event) {
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
