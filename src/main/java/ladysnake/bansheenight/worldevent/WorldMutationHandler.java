package ladysnake.bansheenight.worldevent;

import ladysnake.bansheenight.api.MutationRegistry;
import ladysnake.bansheenight.api.event.BansheeNightHandler;
import ladysnake.bansheenight.capability.CapabilityBansheeNight;
import ladysnake.bansheenight.capability.CapabilityBlockHolder;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Iterator;

public class WorldMutationHandler {
    public static final WorldMutationHandler INSTANCE = new WorldMutationHandler();

    public static void subscribe() {
        MinecraftForge.EVENT_BUS.register(INSTANCE);
    }

    @SubscribeEvent
    public void onTickWorldTick(TickEvent.WorldTickEvent event) {
        if (event.world instanceof WorldServer && event.phase == TickEvent.Phase.END) {
            BansheeNightHandler cap = event.world.getCapability(CapabilityBansheeNight.CAPABILITY_BANSHEE_NIGHT, null);
            if (cap != null && cap.isBansheeNightOccurring() && event.world.getWorldInfo().getTerrainType() != WorldType.DEBUG_ALL_BLOCK_STATES) {
                mutateBlocks((WorldServer) event.world);
            }
        }
    }

    private void mutateBlocks(WorldServer world) {
        int randomTickSpeed = world.getGameRules().getInt("randomTickSpeed");
        world.profiler.startSection("pollingChunks");

        if (randomTickSpeed > 0) {
            for (Iterator<Chunk> iterator = world.getPersistentChunkIterable(world.getPlayerChunkMap().getChunkIterator()); iterator.hasNext(); ) {
                world.profiler.startSection("getChunk");
                Chunk chunk = iterator.next();
                int x = chunk.x * 16;
                int z = chunk.z * 16;

                world.profiler.endStartSection("mutateBlocks");

                for (ExtendedBlockStorage extendedblockstorage : chunk.getBlockStorageArray()) {
                    if (extendedblockstorage != Chunk.NULL_BLOCK_STORAGE && extendedblockstorage.needsRandomTick()) {
                        for (int i1 = 0; i1 < randomTickSpeed; ++i1) {
                            int randomInt = world.updateLCG >> 2;
                            int randomX = randomInt & 0xF;
                            int randomY = randomInt >> 8 & 0xF;
                            int randomZ = randomInt >> 16 & 0xF;
                            IBlockState state = extendedblockstorage.get(randomX, randomZ, randomY);
                            Block block = state.getBlock();

                            Block result = MutationRegistry.getMutation(block);
                            if (result != null) {
                                BlockPos pos = new BlockPos(randomX + x, randomZ + extendedblockstorage.getYLocation(), randomY + z);
                                world.setBlockState(pos, result.getDefaultState(), 2);
                                TileEntity te = world.getTileEntity(pos);
                                if (te != null && te.hasCapability(CapabilityBlockHolder.CAPABILITY_BLOCK_HOLDER, null)) {
                                    te.getCapability(CapabilityBlockHolder.CAPABILITY_BLOCK_HOLDER, null).setHeldBlock(state);
                                }
                            }
                        }
                    }
                }
            }
            world.profiler.endSection();
        }
        world.profiler.endSection();
    }
}
