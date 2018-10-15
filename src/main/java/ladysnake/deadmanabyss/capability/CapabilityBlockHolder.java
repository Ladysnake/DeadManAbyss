package ladysnake.deadmanabyss.capability;

import ladylib.capability.AutoCapability;
import ladysnake.deadmanabyss.api.event.BlockHolderHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

import javax.annotation.Nullable;

@AutoCapability(value = BlockHolderHandler.class, storage = CapabilityBlockHolder.Storage.class)
public class CapabilityBlockHolder implements BlockHolderHandler {
    @CapabilityInject(BlockHolderHandler.class)
    public static Capability<BlockHolderHandler> CAPABILITY_BLOCK_HOLDER;

    private IBlockState heldBlock = Blocks.AIR.getDefaultState();

    @Override
    public IBlockState getHeldBlock() {
        return heldBlock;
    }

    @Override
    public void setHeldBlock(IBlockState heldBlock) {
        this.heldBlock = heldBlock;
    }

    public static class Storage implements Capability.IStorage<BlockHolderHandler> {

        @Nullable
        @Override
        public NBTBase writeNBT(Capability<BlockHolderHandler> capability, BlockHolderHandler instance, EnumFacing side) {
            IBlockState state = instance.getHeldBlock();
            return NBTUtil.writeBlockState(new NBTTagCompound(), state);
        }

        @Override
        public void readNBT(Capability<BlockHolderHandler> capability, BlockHolderHandler instance, EnumFacing side, NBTBase nbt) {
            if (nbt instanceof NBTTagCompound) {
                instance.setHeldBlock(NBTUtil.readBlockState((NBTTagCompound) nbt));
            }
        }
    }
}
