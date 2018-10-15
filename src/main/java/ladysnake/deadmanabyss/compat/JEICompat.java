package ladysnake.deadmanabyss.compat;

import ladysnake.deadmanabyss.init.ModItems;
import mezz.jei.api.*;
import mezz.jei.api.ingredients.VanillaTypes;
import net.minecraft.item.ItemStack;

@JEIPlugin
public class JEICompat implements IModPlugin {

    @Override
    public void register(IModRegistry registry) {
        registry.addIngredientInfo(new ItemStack(ModItems.BLIND_QUARTZ), VanillaTypes.ITEM, "jei.description.dma.quartz_ritual");
    }

}
