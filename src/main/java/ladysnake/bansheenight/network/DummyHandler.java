package ladysnake.bansheenight.network;

import net.minecraftforge.fml.common.network.simpleimpl.*;

public class DummyHandler implements IMessageHandler<DmaNightMessage, IMessage> {

    @Override
    public IMessage onMessage(DmaNightMessage message, MessageContext ctx) {
        throw new IllegalStateException("This should never have been called!");
    }
}
