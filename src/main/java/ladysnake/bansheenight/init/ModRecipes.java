package ladysnake.bansheenight.init;

import ladysnake.bansheenight.BansheeNight;
import ladysnake.bansheenight.api.event.RegisterMutationsEvent;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = BansheeNight.MOD_ID)
public class ModRecipes {
    @SubscribeEvent
    public static void addMutations(RegisterMutationsEvent event) {
        event.registerMutation(new ResourceLocation("minecraft:brown_mushroom"), new ResourceLocation("bansheenight:light_bleb"));
        event.registerMutation(new ResourceLocation("minecraft:red_mushroom"), new ResourceLocation("bansheenight:light_bleb"));
    }
}
