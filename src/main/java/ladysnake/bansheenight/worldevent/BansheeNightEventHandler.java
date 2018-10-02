package ladysnake.bansheenight.worldevent;

import ladylib.compat.EnhancedBusSubscriber;
import ladysnake.bansheenight.BansheeNightConfig;
import ladysnake.bansheenight.api.event.BansheeNightHandler;
import ladysnake.bansheenight.capability.CapabilityBansheeNight;
import ladysnake.bansheenight.entity.EntityBanshee;
import ladysnake.bansheenight.network.BansheeNightMessage;
import ladysnake.bansheenight.network.PacketHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@EnhancedBusSubscriber
public class BansheeNightEventHandler {
    private static final ResourceLocation NETHER_ADVANCEMENT = new ResourceLocation("minecraft:nether/root");

    public static boolean isPlayerReady(EntityPlayerMP player) {
        return player.getAdvancements().getProgress(player.server.getAdvancementManager().getAdvancement(NETHER_ADVANCEMENT)).isDone();
    }

    @SubscribeEvent
    public void onTickWorldTick(TickEvent.WorldTickEvent event) {
        if (event.phase == TickEvent.Phase.START && !event.world.isRemote) {
            BansheeNightHandler cap = event.world.getCapability(CapabilityBansheeNight.CAPABILITY_BANSHEE_NIGHT, null);
            if (cap != null) {
                cap.tick();
                if (cap.getTicksSinceLastNight() > BansheeNightConfig.minTicksBetweenNights &&
                                event.world.rand.nextInt(BansheeNightConfig.bansheeNightProbability) == 0) {
                    for (EntityPlayer player : event.world.playerEntities) {
                        if (isPlayerReady((EntityPlayerMP) player)) {
                            cap.startBansheeNight();
                            break;
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event) {
        BansheeNightHandler cap = event.getWorld().getCapability(CapabilityBansheeNight.CAPABILITY_BANSHEE_NIGHT, null);
        if (cap != null) {
            if (event.getEntity() instanceof EntityPlayerMP) {
                PacketHandler.NET.sendTo(new BansheeNightMessage(cap.isBansheeNightOccurring()), (EntityPlayerMP) event.getEntity());
            } else if (!(event.getEntity() instanceof EntityBanshee) && cap.isBansheeNightOccurring()) {
                event.setCanceled(true);
            }
        }
    }
}
