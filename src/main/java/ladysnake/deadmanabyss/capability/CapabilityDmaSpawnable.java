package ladysnake.deadmanabyss.capability;

import ladylib.capability.AutoCapability;
import ladysnake.deadmanabyss.api.capability.DmaSpawnable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

@AutoCapability(DmaSpawnable.class)
public class CapabilityDmaSpawnable implements DmaSpawnable {
    @CapabilityInject(DmaSpawnable.class)
    public static Capability<DmaSpawnable> DMA_SPAWNABLE_CAPABILITY;
}
