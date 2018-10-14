package ladysnake.bansheenight.compat;

import ladysnake.bansheenight.init.ModItems;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.VanillaTypes;
import net.minecraft.item.ItemStack;

@JEIPlugin
public class JEICompat implements IModPlugin {

    @Override
    public void register(IModRegistry registry) {
        registry.addIngredientInfo(new ItemStack(ModItems.BLIND_QUARTZ), VanillaTypes.ITEM, "jei.description.bansheenight.lotus_ritual");
    }

}
