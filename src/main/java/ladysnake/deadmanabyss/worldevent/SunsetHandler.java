package ladysnake.deadmanabyss.worldevent;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.*;

public class SunsetHandler {

    private static boolean subscribed;
    private static final Set<Integer> dimensions = new HashSet<>();

    public static boolean isActive(int dimension) {
        return dimensions.contains(dimension);
    }

    public static void subscribe(int dimension) {
        if(!subscribed) {
            MinecraftForge.EVENT_BUS.register(SunsetHandler.class);
            subscribed = true;
        }
        dimensions.add(dimension);
    }

    public static void unsubscribe(int dimension) {
        dimensions.remove(dimension);
        if(dimensions.isEmpty()) {
            MinecraftForge.EVENT_BUS.unregister(SunsetHandler.class);
            subscribed = false;
        }
    }

    /**
     * hw many ticks we add to the current world time each tick
     */
    private static final int SPEED = 50; //TODO adjust value ^Up

    @SubscribeEvent
    public static void onTick(TickEvent.WorldTickEvent event) {
        int dim = event.world.provider.getDimension();
        if(!event.world.isRemote && event.phase == TickEvent.Phase.START && dimensions.contains(dim)) {
            WorldServer world = (WorldServer) event.world;
            float angle = world.getCelestialAngle(1.0F);
            if(angle >= 0.48F && angle < 0.52F) { //-> 0.5 = midnight
                unsubscribe(dim);
            }
            else {
                //speed up time
                //TODO speed up at start, slow down near end ^Up
                //TODO change direction? idk ^Up
                world.setWorldTime(world.getWorldTime() + SPEED);

                //TODO is there a better way than this? ^Up
                world.playerEntities.stream().map(player -> (EntityPlayerMP) player).forEach(player -> player.connection.sendPacket(new SPacketTimeUpdate(world.getTotalWorldTime(), world.getWorldTime(), world.getGameRules().getBoolean("doDaylightCycle"))));
            }
        }
    }
}
