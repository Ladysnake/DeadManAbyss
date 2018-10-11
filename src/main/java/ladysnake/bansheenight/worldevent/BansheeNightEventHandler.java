package ladysnake.bansheenight.worldevent;

import ladylib.compat.EnhancedBusSubscriber;
import ladysnake.bansheenight.*;
import ladysnake.bansheenight.api.event.BansheeNightHandler;
import ladysnake.bansheenight.capability.*;
import net.minecraft.entity.player.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@EnhancedBusSubscriber(owner = BansheeNight.MOD_ID)
public class BansheeNightEventHandler {
    private static final ResourceLocation NETHER_ADVANCEMENT = new ResourceLocation("minecraft:nether/root");

    @SubscribeEvent
    public void onReturnFromPortal(EntityTravelToDimensionEvent event) {
        if(BansheeNightConfig.triggers.portal && !event.getEntity().world.isRemote && event.getEntity() instanceof EntityPlayerMP) { //Entity#world is NOT the target world, but it's fine for the side check
            WorldServer world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(event.getDimension());
            BansheeNightHandler cap = world.getCapability(CapabilityBansheeNight.CAPABILITY_BANSHEE_NIGHT, null);
            if(cap != null && cap.getTicksSinceLastNight() > BansheeNightConfig.minTicksBetweenNights && isPlayerReady((EntityPlayerMP) event.getEntity()) && world.rand.nextInt(BansheeNightConfig.bansheeNightProbability) == 0) {
                cap.startBansheeNight(); //TODO randomize it a bit more? ^Up
            }
        }
    }

    public static boolean isPlayerReady(EntityPlayerMP player) {
        return player.getAdvancements().getProgress(player.server.getAdvancementManager().getAdvancement(NETHER_ADVANCEMENT)).isDone();
    }

    @SubscribeEvent
    public void onTickWorldTick(TickEvent.WorldTickEvent event) {
        if(event.phase == TickEvent.Phase.START && !event.world.isRemote) {
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
        if(cap != null && cap.isBansheeNightOccurring() && !(event.getEntity().hasCapability(CapabilityBansheeNightSpawnable.CAPABILITY_BANSHEE_NIGHT_SPAWN, null))) {
            event.setResult(Event.Result.DENY);
        }
    }
}
