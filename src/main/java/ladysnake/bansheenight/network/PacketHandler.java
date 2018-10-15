package ladysnake.bansheenight.network;

import ladysnake.bansheenight.DeadManAbyss;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler {
    public static final SimpleNetworkWrapper NET = NetworkRegistry.INSTANCE.newSimpleChannel(DeadManAbyss.MOD_ID.toUpperCase());

    public static void initPackets(FMLInitializationEvent event) {
        if(event.getSide() == Side.CLIENT) {
            NET.registerMessage(DmaNightPacket.class, DmaNightMessage.class, 0, Side.CLIENT);
        }
        else {
            NET.registerMessage(DummyHandler.class, DmaNightMessage.class, 0, Side.CLIENT);
        }

    }
}
