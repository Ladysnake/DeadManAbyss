package ladysnake.bansheenight.worldevent;

import ladylib.compat.EnhancedBusSubscriber;
import ladysnake.bansheenight.*;
import ladysnake.bansheenight.api.event.BansheeNightHandler;
import ladysnake.bansheenight.capability.*;
import ladysnake.bansheenight.item.ItemLotus;
import ladysnake.bansheenight.network.*;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.*;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.fml.common.gameevent.*;

@EnhancedBusSubscriber(owner = BansheeNight.MOD_ID)
public class BansheeNightEventHandler {
    private static final ResourceLocation NETHER_ADVANCEMENT = new ResourceLocation("minecraft:nether/root");

    @SubscribeEvent
    public void onReturnFromPortal(EntityTravelToDimensionEvent event) {
        if(!event.getEntity().world.isRemote && event.getEntity() instanceof EntityPlayerMP) { //Entity#world is NOT the target world, but it's fine for the side check
            WorldServer world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(event.getDimension());
            BansheeNightHandler cap = world.getCapability(CapabilityBansheeNight.CAPABILITY_BANSHEE_NIGHT, null);
            if(cap != null) {
                if(BansheeNightConfig.triggers.portal) {
                    if(cap.getTicksSinceLastNight() >= BansheeNightConfig.minTicksBetweenNights && isPlayerReady((EntityPlayerMP) event.getEntity()) && world.rand.nextDouble() < BansheeNightConfig.bansheeNightProbability) {
                        cap.startBansheeNight(); //TODO randomize it a bit more? ^Up
                    }
                }
                if(cap.isBansheeNightOccurring()) {
                    PacketHandler.NET.sendTo(new BansheeNightMessage(cap.getTicksSinceLastNight()), (EntityPlayerMP) event.getEntity());
                }
            }

        }
    }

    @SubscribeEvent
    public void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
        World world = event.player.world;
        if(!world.isRemote) {
            BansheeNightHandler cap = world.getCapability(CapabilityBansheeNight.CAPABILITY_BANSHEE_NIGHT, null);
            if(cap != null) {
                PacketHandler.NET.sendTo(new BansheeNightMessage(cap.getTicksSinceLastNight()), (EntityPlayerMP) event.player);
            }
        }
    }

    public static boolean isPlayerReady(EntityPlayerMP player) {
        return player.getAdvancements().getProgress(player.server.getAdvancementManager().getAdvancement(NETHER_ADVANCEMENT)).isDone();
    }

    private int tickCounter;

    @SubscribeEvent
    public void onTickWorldTick(TickEvent.WorldTickEvent event) {
        if(event.phase == TickEvent.Phase.START) {
            BansheeNightHandler cap = event.world.getCapability(CapabilityBansheeNight.CAPABILITY_BANSHEE_NIGHT, null);
            if(cap != null) {
                cap.tick();
                if(cap.isBansheeNightOccurring()) {
                    if(++tickCounter % 3 == 0) event.world.setWorldTime(event.world.getWorldTime() - 2); //TODO adjust night duration rate? ^Up
                    float angle = event.world.getCelestialAngle(1.0F);

                    if(angle > 0.76F) cap.stopBansheeNight(); //stop the night at dawn
                }
                else {
                    //TODO randomly start night if time is around sunset, but only check once per ingame day! ^Up
                }
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
