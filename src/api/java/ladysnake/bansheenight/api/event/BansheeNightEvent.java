package ladysnake.bansheenight.api.event;

import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

/**
 * fired on the {@link MinecraftForge#EVENT_BUS} for notifying about the banshee night status
 */
public class BansheeNightEvent extends WorldEvent {

    public BansheeNightEvent(World world) {
        super(world);
    }

    @Cancelable
    public static class Start extends BansheeNightEvent {
        public Start(World world) {
            super(world);
        }
    }

    @Cancelable
    public static class Stop extends BansheeNightEvent {
        public Stop(World world) {
            super(world);
        }
    }
}
