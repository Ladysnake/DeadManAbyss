package ladysnake.bansheenight.init;

import ladylib.registration.AutoRegister;
import ladysnake.bansheenight.BansheeNight;
import ladysnake.bansheenight.block.BlockMutatedBush;
import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.GameRegistry;

@AutoRegister(BansheeNight.MOD_ID)
@GameRegistry.ObjectHolder(BansheeNight.MOD_ID)
public class ModBlocks {
    public static final Block LIGHT_BLEB = new BlockMutatedBush().setLightLevel(1f);
}
