package ladysnake.bansheenight.init;

import ladylib.registration.AutoRegister;
import ladysnake.bansheenight.BansheeNight;
import ladysnake.bansheenight.block.BlockLightBleb;
import ladysnake.bansheenight.block.BlockMutatedBush;
import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.GameRegistry;

@AutoRegister(BansheeNight.MOD_ID)
@GameRegistry.ObjectHolder(BansheeNight.MOD_ID)
public class ModBlocks {
    public static final Block LIGHTBLEB = new BlockLightBleb().setLightLevel(1f);
    public static final Block LIGHTBLEB_EMPTY = new BlockMutatedBush();
}
