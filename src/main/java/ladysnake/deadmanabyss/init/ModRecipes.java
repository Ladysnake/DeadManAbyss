package ladysnake.deadmanabyss.init;

import ladysnake.deadmanabyss.DeadManAbyss;
import ladysnake.deadmanabyss.api.event.RegisterMutationsEvent;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = DeadManAbyss.MOD_ID)
public class ModRecipes {
    @SubscribeEvent
    public static void addMutations(RegisterMutationsEvent event) {
        event.registerMutation(new ResourceLocation("minecraft:brown_mushroom"), new ResourceLocation("dma:lightbleb"));
        event.registerMutation(new ResourceLocation("minecraft:red_mushroom"), new ResourceLocation("dma:lightbleb"));
    }
}
