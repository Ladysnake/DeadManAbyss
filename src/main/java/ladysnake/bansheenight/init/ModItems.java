package ladysnake.bansheenight.init;

import ladylib.registration.AutoRegister;
import ladysnake.bansheenight.DeadManAbyss;
import ladysnake.bansheenight.item.ItemBlindQuartz;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

@SuppressWarnings("unused")
@AutoRegister(DeadManAbyss.MOD_ID)
@GameRegistry.ObjectHolder(DeadManAbyss.MOD_ID)
public class ModItems {
    public static final Item BLIND_TEAR = new Item();
    public static final Item BLIND_QUARTZ = new ItemBlindQuartz();
    public static final Item ICHOR_SAC = new Item();
    public static final Item LIGHTBLEB_MEMBRANE = new Item();
    @AutoRegister.Ignore
    public static final Item LIGHTBLEB = null;
}
