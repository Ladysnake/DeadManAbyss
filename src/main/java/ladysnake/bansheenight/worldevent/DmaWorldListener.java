package ladysnake.bansheenight.worldevent;

import ladysnake.bansheenight.entity.EntityScreecher;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldEventListener;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import javax.annotation.Nullable;

public class DmaWorldListener implements IWorldEventListener {
    private WorldServer world;

    public DmaWorldListener(WorldServer world) {
        this.world = world;
    }

    @Override
    public void notifyBlockUpdate(World worldIn, BlockPos pos, IBlockState oldState, IBlockState newState, int flags) {
        // NO-OP
    }

    @Override
    public void notifyLightSet(BlockPos pos) {
        // NO-OP
    }

    @Override
    public void markBlockRangeForRenderUpdate(int x1, int y1, int z1, int x2, int y2, int z2) {
        // NO-OP
    }

    @Override
    public void playSoundToAllNearExcept(@Nullable EntityPlayer player, SoundEvent soundIn, SoundCategory category, double x, double y, double z, float volume, float pitch) {
        // get all banshees within a radius of up to 24 blocks from the sound.
        for (EntityScreecher banshee : world.getEntitiesWithinAABB(EntityScreecher.class, new AxisAlignedBB(x - 1, y - 1, z - 1, x + 1, y + 1, z + 1).grow(volume * 24))) {
            banshee.onSoundHeard(soundIn, category, x, y, z, volume);
        }
    }

    @Override
    public void playRecord(SoundEvent soundIn, BlockPos pos) {
        // NO-OP
    }

    @Override
    public void spawnParticle(int particleID, boolean ignoreRange, double xCoord, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed, int... parameters) {
        // NO-OP
    }

    @Override
    public void spawnParticle(int id, boolean ignoreRange, boolean minimiseParticleLevel, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, int... parameters) {
        // NO-OP
    }

    @Override
    public void onEntityAdded(Entity entityIn) {
        // NO-OP
    }

    @Override
    public void onEntityRemoved(Entity entityIn) {
        // NO-OP
    }

    @Override
    public void broadcastSound(int soundID, BlockPos pos, int data) {
        // NO-OP
    }

    @Override
    public void playEvent(EntityPlayer player, int type, BlockPos blockPosIn, int data) {
        // NO-OP
    }

    @Override
    public void sendBlockBreakProgress(int breakerId, BlockPos pos, int progress) {

    }
}
