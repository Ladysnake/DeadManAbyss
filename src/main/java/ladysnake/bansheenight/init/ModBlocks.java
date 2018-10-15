package ladysnake.bansheenight.init;

import ladylib.registration.AutoRegister;
import ladysnake.bansheenight.DeadManAbyss;
import ladysnake.bansheenight.block.BlockLightBleb;
import ladysnake.bansheenight.block.BlockMutatedBush;
import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.GameRegistry;

@AutoRegister(DeadManAbyss.MOD_ID)
@GameRegistry.ObjectHolder(DeadManAbyss.MOD_ID)
public class ModBlocks {
    public static final Block LIGHTBLEB = new BlockLightBleb().setLightLevel(1f);
    public static final Block LIGHTBLEB_EMPTY = new BlockMutatedBush();
}
