package ladysnake.deadmanabyss.compat;

import ladysnake.deadmanabyss.DeadManAbyss;
import ladysnake.deadmanabyss.DmaConfig;
import ladysnake.deadmanabyss.api.event.DmaSpawnable;
import ladysnake.deadmanabyss.capability.CapabilityDmaSpawnable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class CompatMobSpawn {

    private static Set<ResourceLocation> compatMobs;

    private static final ResourceLocation SPAWN_CAPABILITY = new ResourceLocation(DeadManAbyss.MOD_ID, "spawn");

    public static void init() {
        compatMobs = Arrays.stream(DmaConfig.entityWhiteList).map(ResourceLocation::new).filter(ForgeRegistries.ENTITIES::containsKey).collect(Collectors.toSet());
        if (!compatMobs.isEmpty()) {
            MinecraftForge.EVENT_BUS.register(CompatMobSpawn.class);
        }
    }

    @SubscribeEvent
    public static void onAttachCapabiltity(AttachCapabilitiesEvent<Entity> event) {
        // preliminary check, only living entities can spawn naturally
        if (event.getObject() instanceof EntityLiving) {
            boolean flag = compatMobs.contains(EntityList.getKey(event.getObject()));

            if(flag) event.addCapability(SPAWN_CAPABILITY, new ICapabilityProvider() {

                DmaSpawnable instance = CapabilityDmaSpawnable.DMA_SPAWNABLE_CAPABILITY.getDefaultInstance();
                @Override
                public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
                    return capability == CapabilityDmaSpawnable.DMA_SPAWNABLE_CAPABILITY;
                }

                @Nullable
                @Override
                public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
                    return capability == CapabilityDmaSpawnable.DMA_SPAWNABLE_CAPABILITY ? CapabilityDmaSpawnable.DMA_SPAWNABLE_CAPABILITY.cast(instance) : null;
                }
            });
        }
    }
}
