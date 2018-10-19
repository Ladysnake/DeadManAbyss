package ladysnake.deadmanabyss.api.capability;

import net.minecraft.block.state.IBlockState;

public interface BlockHolderHandler {
    void setHeldBlock(IBlockState blockId);
    IBlockState getHeldBlock();
}
