package ladysnake.bansheenight.network;

import io.netty.buffer.ByteBuf;
import ladylib.misc.CalledThroughReflection;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class BansheeNightMessage implements IMessage {
    // true if occurring, false if stopping
    boolean occurring;

    @CalledThroughReflection
    public BansheeNightMessage() {
        super();
    }

    public BansheeNightMessage(boolean occurring) {
        this.occurring = occurring;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        occurring = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(occurring);
    }
}
