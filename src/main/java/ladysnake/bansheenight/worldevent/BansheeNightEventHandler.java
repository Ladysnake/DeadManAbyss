package ladysnake.bansheenight.worldevent;

import ladylib.compat.EnhancedBusSubscriber;
import ladysnake.bansheenight.BansheeNightConfig;
import ladysnake.bansheenight.api.event.BansheeNightHandler;
import ladysnake.bansheenight.capability.*;
import net.minecraft.entity.player.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@EnhancedBusSubscriber
public class BansheeNightEventHandler {
    private static final ResourceLocation NETHER_ADVANCEMENT = new ResourceLocation("minecraft:nether/root");

    @SubscribeEvent
    public void onTickWorldTick(TickEvent.WorldTickEvent event) {
        if(event.phase == TickEvent.Phase.START && !event.world.isRemote) {
            BansheeNightHandler cap = event.world.getCapability(CapabilityBansheeNight.CAPABILITY_BANSHEE_NIGHT, null);
            if(cap != null) {
                cap.tick();
                if(cap.getTicksSinceLastNight() > BansheeNightConfig.minTicksBetweenNights &&
                   event.world.rand.nextInt(BansheeNightConfig.bansheeNightProbability) == 0) {
                    for(EntityPlayer player : event.world.playerEntities) {
                        if(isPlayerReady((EntityPlayerMP) player)) {
                            cap.startBansheeNight();
                            break;
                        }
                    }
                }
            }
        }
    }

    public static boolean isPlayerReady(EntityPlayerMP player) {
        return player.getAdvancements().getProgress(player.server.getAdvancementManager().getAdvancement(NETHER_ADVANCEMENT)).isDone();
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
