package ladysnake.bansheenight.network;

import ladysnake.bansheenight.BansheeNight;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler {
    public static final SimpleNetworkWrapper NET = NetworkRegistry.INSTANCE.newSimpleChannel(BansheeNight.MOD_ID.toUpperCase());

    private static int nextPacketId = 0;

    public static void initPackets() {
        NET.registerMessage(BansheeNightPacket.class, BansheeNightMessage.class, nextPacketId++, Side.CLIENT);
    }}
