package ladysnake.bansheenight.init;

import ladylib.registration.AutoRegister;
import ladysnake.bansheenight.BansheeNight;
import ladysnake.bansheenight.item.ItemLotus;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

@SuppressWarnings("unused")
@AutoRegister(BansheeNight.MOD_ID)
@GameRegistry.ObjectHolder(BansheeNight.MOD_ID)
public class ModItems {
    public static final Item BLIND_TEAR = new Item();
    public static final Item BLIND_QUARTZ = new ItemLotus();
    public static final Item ICHOR_SAC = new Item();
    public static final Item LIGHTBLEB_MEMBRANE = new Item();
    @AutoRegister.Ignore
    public static final Item LIGHTBLEB = null;
}
