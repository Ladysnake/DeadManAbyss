package ladysnake.bansheenight.api.event;

import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

/**
 * fired on the {@link MinecraftForge#EVENT_BUS} for notifying about the dma event status
 */
public class DmaNightEvent extends WorldEvent {

    public DmaNightEvent(World world) {
        super(world);
    }

    @Cancelable
    public static class Start extends DmaNightEvent {
        public Start(World world) {
            super(world);
        }
    }

    @Cancelable
    public static class Stop extends DmaNightEvent {
        public Stop(World world) {
            super(world);
        }
    }
}
