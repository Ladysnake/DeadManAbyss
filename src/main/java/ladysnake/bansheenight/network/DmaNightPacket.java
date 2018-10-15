package ladysnake.bansheenight.network;

import ladysnake.bansheenight.api.event.DmaEventHandler;
import ladysnake.bansheenight.capability.CapabilityDmaEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import javax.annotation.Nullable;

public class DmaNightPacket implements IMessageHandler<DmaNightMessage, IMessage> {
    @Nullable
    @Override
    public IMessage onMessage(DmaNightMessage message, MessageContext ctx) {
        if (ctx.side.isClient()) {
            World world = Minecraft.getMinecraft().world;
            if (world != null) {
                DmaEventHandler cap = world.getCapability(CapabilityDmaEvent.CAPABILITY_DMA_EVENT, null);
                if (cap != null) {
                    cap.setTicksSinceLastEvent(message.ticks);
                }
            }
        }
        return null;
    }
}
