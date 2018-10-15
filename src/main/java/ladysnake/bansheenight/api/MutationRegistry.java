package ladysnake.bansheenight.api;

import ladylib.misc.PublicApi;
import ladysnake.bansheenight.DeadManAbyss;
import ladysnake.bansheenight.api.event.RegisterMutationsEvent;
import net.minecraft.block.Block;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = DeadManAbyss.MOD_ID)
public class MutationRegistry {
    private static Map<Block, Block> mutations;

    @PublicApi
    public static Block getMutation(Block mutated) {
        return mutations.get(mutated);
    }

    @SubscribeEvent
    public static void onAddRecipes(RegistryEvent.Register<IRecipe> event) {
        Map<ResourceLocation, ResourceLocation> recipes = new HashMap<>();
        MinecraftForge.EVENT_BUS.post(new RegisterMutationsEvent(recipes));
        // should be safe to resolve now, as block registration is done
        mutations = recipes.entrySet().stream().collect(Collectors.toMap(extract(Map.Entry::getKey), extract(Map.Entry::getValue)));
    }

    private static Function<Map.Entry<ResourceLocation, ResourceLocation>, Block> extract(Function<Map.Entry<ResourceLocation, ResourceLocation>, ResourceLocation> extractor) {
        return extractor.andThen(v -> Objects.requireNonNull(ForgeRegistries.BLOCKS.getValue(v), v + " is not a registered block"));
    }
}
