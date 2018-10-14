package ladysnake.bansheenight.network;

import ladysnake.bansheenight.api.event.BansheeNightHandler;
import ladysnake.bansheenight.capability.CapabilityBansheeNight;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import javax.annotation.Nullable;

public class BansheeNightPacket implements IMessageHandler<BansheeNightMessage, IMessage> {
    @Nullable
    @Override
    public IMessage onMessage(BansheeNightMessage message, MessageContext ctx) {
        if (ctx.side.isClient()) {
            World world = Minecraft.getMinecraft().world;
            if (world != null) {
                BansheeNightHandler cap = world.getCapability(CapabilityBansheeNight.CAPABILITY_BANSHEE_NIGHT, null);
                if (cap != null) {
                    cap.setTicksSinceLastNight(message.ticks);
                }
            }
        }
        return null;
    }
}
