package ladysnake.bansheenight.worldevent;

import ladylib.compat.EnhancedBusSubscriber;
import ladysnake.bansheenight.BansheeNight;
import ladysnake.bansheenight.BansheeNightConfig;
import ladysnake.bansheenight.api.event.BansheeNightHandler;
import ladysnake.bansheenight.capability.CapabilityBansheeNight;
import ladysnake.bansheenight.capability.CapabilityBansheeNightSpawnable;
import ladysnake.bansheenight.item.ItemLotus;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@EnhancedBusSubscriber(owner = BansheeNight.MOD_ID)
public class BansheeNightEventHandler {
    private static final ResourceLocation NETHER_ADVANCEMENT = new ResourceLocation("minecraft:nether/root");

    @SubscribeEvent
    public void onReturnFromPortal(EntityTravelToDimensionEvent event) {
        if(BansheeNightConfig.triggers.portal && !event.getEntity().world.isRemote && event.getEntity() instanceof EntityPlayerMP) { //Entity#world is NOT the target world, but it's fine for the side check
            WorldServer world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(event.getDimension());
            BansheeNightHandler cap = world.getCapability(CapabilityBansheeNight.CAPABILITY_BANSHEE_NIGHT, null);
            if(cap != null && cap.getTicksSinceLastNight() >= BansheeNightConfig.minTicksBetweenNights && isPlayerReady((EntityPlayerMP) event.getEntity()) && world.rand.nextDouble() < BansheeNightConfig.bansheeNightProbability) {
                cap.startBansheeNight(); //TODO randomize it a bit more? ^Up
            }
        }
    }

    public static boolean isPlayerReady(EntityPlayerMP player) {
        return player.getAdvancements().getProgress(player.server.getAdvancementManager().getAdvancement(NETHER_ADVANCEMENT)).isDone();
    }

    @SubscribeEvent
    public void onTickWorldTick(TickEvent.WorldTickEvent event) {
        if(event.phase == TickEvent.Phase.START) {
            BansheeNightHandler cap = event.world.getCapability(CapabilityBansheeNight.CAPABILITY_BANSHEE_NIGHT, null);
            if(cap != null) {
                cap.tick();
                //TODO randomly start night if time is around sunset, but only check once per ingame day! ^Up
            }
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        if(!event.getWorld().isRemote) {
            event.getWorld().addEventListener(new BansheeWorldListener((WorldServer) event.getWorld()));
        }
    }

    @SubscribeEvent
    public void onLivingSpawnCheckSpawn(LivingSpawnEvent.CheckSpawn event) {
        BansheeNightHandler cap = event.getWorld().getCapability(CapabilityBansheeNight.CAPABILITY_BANSHEE_NIGHT, null);
        if(cap != null && cap.isBansheeNightOccurring() && event.getEntity().getCapability(CapabilityBansheeNightSpawnable.CAPABILITY_BANSHEE_NIGHT_SPAWN, null) == null) {
            event.setResult(Event.Result.DENY);
        }
    }

    @SubscribeEvent
    public void onPLayerDropLotus(ItemTossEvent event) {
        if(!event.getPlayer().world.isRemote) {
            EntityItem item = event.getEntityItem();
            if(item.getItem().getItem() instanceof ItemLotus) {
                if(isPlayerReady((EntityPlayerMP) event.getPlayer())) { //prevent automation of the event
                    item.getEntityData().setBoolean("Trigger", true);
                }
            }
        }

    }
}
