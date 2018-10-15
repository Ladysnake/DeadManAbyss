package ladysnake.deadmanabyss.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.EnumPlantType;

public class BlockLightBleb extends BlockMutatedBush {
    @Override
    public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
        return EnumPlantType.Cave;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }
}
