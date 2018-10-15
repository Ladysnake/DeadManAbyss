package ladysnake.deadmanabyss.api.event;

import net.minecraft.block.state.IBlockState;

public interface BlockHolderHandler {
    void setHeldBlock(IBlockState blockId);
    IBlockState getHeldBlock();
}
