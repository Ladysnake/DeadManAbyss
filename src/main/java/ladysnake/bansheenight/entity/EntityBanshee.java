package ladysnake.bansheenight.entity;

import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.world.World;

public class EntityBanshee extends EntityMob {
    public EntityBanshee(World worldIn) {
        super(worldIn);
    }

    @Override
    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.UNDEAD;
    }


}
