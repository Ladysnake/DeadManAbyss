package ladysnake.bansheenight.init;

import ladysnake.bansheenight.BansheeNight;
import ladysnake.bansheenight.client.render.entity.*;
import ladysnake.bansheenight.entity.*;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.*;
import net.minecraftforge.fml.relauncher.*;

@Mod.EventBusSubscriber(modid = BansheeNight.MOD_ID)
public class ModEntities {
    private static int id = 0;

    @SubscribeEvent
    public static void onRegistryRegister(RegistryEvent.Register<EntityEntry> event) {
        event.getRegistry().registerAll(
                EntityEntryBuilder.create()
                        .entity(EntityBanshee.class)
                        .id(new ResourceLocation(BansheeNight.MOD_ID, "banshee"), id++)
                        .name("banshee")
                        .egg(0xFFFFFF, 0xAAAAAA)
                        .spawn(EnumCreatureType.MONSTER, 1, 1, 1, ForgeRegistries.BIOMES.getValuesCollection())
                        .tracker(64, 1, true)
                        .factory(EntityBanshee::new)
                        .build(),
                EntityEntryBuilder.create()
                        .entity(EntityBlind.class)
                        .id(new ResourceLocation(BansheeNight.MOD_ID, "blind"), id++)
                        .name("blind")
                        .egg(0x111111, 0xAAAAAA)
                        .spawn(EnumCreatureType.MONSTER, 1, 1, 1, ForgeRegistries.BIOMES.getValuesCollection())
                        .tracker(64, 1, true)
                        .factory(EntityBlind::new)
                        .build()
        );
    }

    @SideOnly(Side.CLIENT)
    public static void registerRenders() {
        RenderingRegistry.registerEntityRenderingHandler(EntityBanshee.class, RenderBanshee::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityBlind.class, RenderBlind::new);
    }
}
