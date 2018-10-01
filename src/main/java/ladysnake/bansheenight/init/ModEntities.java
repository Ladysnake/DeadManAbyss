package ladysnake.bansheenight.init;

import ladylib.compat.EnhancedBusSubscriber;
import ladysnake.bansheenight.BansheeNight;
import ladysnake.bansheenight.entity.EntityBanshee;
import ladysnake.bansheenight.entity.EntityBlind;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

@EnhancedBusSubscriber(owner = BansheeNight.MOD_ID)
public class ModEntities {
    private int id = 0;

    @SubscribeEvent
    public void onRegistryRegister(RegistryEvent.Register<EntityEntry> event) {
        event.getRegistry().registerAll(
                EntityEntryBuilder.create()
                        .entity(EntityBanshee.class)
                        .id(new ResourceLocation(BansheeNight.MOD_ID, "banshee"), id++)
                        .name("Banshee")
                        .egg(0xFFFFFF, 0xAAAAAA)
                        .spawn(EnumCreatureType.MONSTER, 1, 1, 1, ForgeRegistries.BIOMES.getValuesCollection())
                        .tracker(64, 1, true)
                        .factory(EntityBanshee::new)
                        .build(),
                EntityEntryBuilder.create()
                        .entity(EntityBlind.class)
                        .id(new ResourceLocation(BansheeNight.MOD_ID, "blind"), id++)
                        .name("Blind")
                        .egg(0x111111, 0xAAAAAA)
                        .spawn(EnumCreatureType.MONSTER, 1, 1, 1, ForgeRegistries.BIOMES.getValuesCollection())
                        .tracker(64, 1, true)
                        .factory(EntityBlind::new)
                        .build()
        );
    }
}
