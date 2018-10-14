package ladysnake.bansheenight.api.event;

import net.minecraft.block.state.IBlockState;

public interface BlockHolderHandler {
    void setHeldBlock(IBlockState blockId);
    IBlockState getHeldBlock();
}
