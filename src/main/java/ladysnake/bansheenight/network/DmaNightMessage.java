package ladysnake.bansheenight.network;

import io.netty.buffer.ByteBuf;
import ladylib.misc.CalledThroughReflection;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class DmaNightMessage implements IMessage {
    // true if occurring, false if stopping
    int ticks;

    @CalledThroughReflection
    public DmaNightMessage() {
        super();
    }

    public DmaNightMessage(int ticks) {
        this.ticks = ticks;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        ticks = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(ticks);
    }
}
