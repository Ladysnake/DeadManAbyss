package ladysnake.bansheenight.init;

import ladysnake.bansheenight.DeadManAbyss;
import ladysnake.bansheenight.client.render.entity.RenderBlind;
import ladysnake.bansheenight.client.render.entity.RenderScreecher;
import ladysnake.bansheenight.entity.EntityBlind;
import ladysnake.bansheenight.entity.EntityScreecher;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(modid = DeadManAbyss.MOD_ID)
public class ModEntities {
    private static int id = 0;

    @SubscribeEvent
    public static void onRegistryRegister(RegistryEvent.Register<EntityEntry> event) {
        event.getRegistry().registerAll(
                EntityEntryBuilder.create()
                        .entity(EntityScreecher.class)
                        .id(new ResourceLocation(DeadManAbyss.MOD_ID, "screecher"), id++)
                        .name(DeadManAbyss.MOD_ID + ".screecher")
                        .egg(0xFFFFFF, 0xAAAAAA)
                        .spawn(EnumCreatureType.MONSTER, 1, 1, 1, ForgeRegistries.BIOMES.getValuesCollection())
                        .tracker(64, 1, true)
                        .factory(EntityScreecher::new)
                        .build(),
                EntityEntryBuilder.create()
                        .entity(EntityBlind.class)
                        .id(new ResourceLocation(DeadManAbyss.MOD_ID, "blind"), id++)
                        .name(DeadManAbyss.MOD_ID + ".blind")
                        .egg(0x111111, 0xAAAAAA)
                        .spawn(EnumCreatureType.MONSTER, 1, 1, 1, ForgeRegistries.BIOMES.getValuesCollection())
                        .tracker(64, 1, true)
                        .factory(EntityBlind::new)
                        .build()
        );
    }

    @SideOnly(Side.CLIENT)
    public static void registerRenders() {
        RenderingRegistry.registerEntityRenderingHandler(EntityScreecher.class, RenderScreecher::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityBlind.class, RenderBlind::new);
    }
}
