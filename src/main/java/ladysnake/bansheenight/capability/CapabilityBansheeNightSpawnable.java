package ladysnake.bansheenight.capability;

import ladylib.capability.AutoCapability;
import ladysnake.bansheenight.api.event.BansheeNightSpawnable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

@AutoCapability(BansheeNightSpawnable.class)
public class CapabilityBansheeNightSpawnable implements BansheeNightSpawnable {
    @CapabilityInject(BansheeNightSpawnable.class)
    public static Capability<BansheeNightSpawnable> CAPABILITY_BANSHEE_NIGHT_SPAWN;
}
