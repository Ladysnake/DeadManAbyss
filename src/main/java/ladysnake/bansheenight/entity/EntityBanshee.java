package ladysnake.bansheenight.entity;

import ladysnake.bansheenight.api.event.BansheeNightHandler;
import ladysnake.bansheenight.capability.CapabilityBansheeNight;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.world.World;

public class EntityBanshee extends EntityMob {
    public EntityBanshee(World worldIn) {
        super(worldIn);
    }

    @Override
    public boolean getCanSpawnHere() {
        BansheeNightHandler cap = this.world.getCapability(CapabilityBansheeNight.CAPABILITY_BANSHEE_NIGHT, null);
        return cap != null && cap.isBansheeNightOccurring() && super.getCanSpawnHere();
    }
}
