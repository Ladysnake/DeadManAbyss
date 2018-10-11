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
    public static final Item BLIND_LEATHER = new Item();
    public static final Item BLACK_CRYSTAL_LOTUS = new ItemLotus();

}
