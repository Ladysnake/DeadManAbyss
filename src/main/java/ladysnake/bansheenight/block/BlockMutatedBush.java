package ladysnake.bansheenight.block;

import ladysnake.bansheenight.api.event.DmaEventHandler;
import ladysnake.bansheenight.api.event.BlockHolderHandler;
import ladysnake.bansheenight.capability.CapabilityDmaEvent;
import ladysnake.bansheenight.capability.CapabilityBlockHolder;
import ladysnake.bansheenight.tileentity.TileEntityBlockHolder;
import net.minecraft.block.BlockBush;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

import static net.minecraft.block.BlockLeaves.DECAYABLE;

public class BlockMutatedBush extends BlockBush {

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer.Builder(this).add(DECAYABLE).build();
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand).withProperty(DECAYABLE, false);
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (state.getValue(DECAYABLE)) {
            TileEntity te = worldIn.getTileEntity(pos);
            if (te != null) {
                DmaEventHandler cap = worldIn.getCapability(CapabilityDmaEvent.CAPABILITY_DMA_EVENT, null);
                if (cap != null && !cap.isEventOccuring()) {
                    BlockHolderHandler blockHolder = te.getCapability(CapabilityBlockHolder.CAPABILITY_BLOCK_HOLDER, null);
                    worldIn.setBlockState(pos, blockHolder.getHeldBlock(), 2);
                    return;
                }
            }
        }
        super.updateTick(worldIn, pos, state, rand);
    }

    @Override
    @Deprecated
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(DECAYABLE, meta > 0);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(DECAYABLE) ? 1 : 0;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return state.getValue(DECAYABLE);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return hasTileEntity(state) ? new TileEntityBlockHolder() : null;
    }
}
