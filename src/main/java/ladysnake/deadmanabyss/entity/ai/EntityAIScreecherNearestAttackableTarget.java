package ladysnake.deadmanabyss.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class EntityAIScreecherNearestAttackableTarget<T extends EntityLivingBase> extends EntityAINearestAttackableTarget<T> {

    public EntityAIScreecherNearestAttackableTarget(EntityCreature creature, Class<T> classTarget, boolean checkSight) {
        super(creature, classTarget, checkSight);
    }

    @Override
    public boolean shouldExecute() {
        if (super.shouldExecute()) {
            float localDifficulty = this.taskOwner.world.getDifficultyForLocation(new BlockPos(this.taskOwner)).getAdditionalDifficulty();
            double distance = this.taskOwner.getDistanceSq(this.targetEntity);
            double rangeModifier;
            if (this.targetEntity instanceof EntityPlayer) {
                rangeModifier = interpolateDifficulty(localDifficulty) * 1.5 + 0.5;
            } else {
                // entities other than players will only be attacked if the banshee is really close
                rangeModifier = 0.5;
            }
            if (this.targetEntity.isSneaking()) {
                rangeModifier *= 0.5;
            }
            double range = this.taskOwner.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).getAttributeValue() * rangeModifier;
            return distance < range * range;
        }
        return false;
    }

    /**
     * Performs smooth hermite interpolation between 0 and 1 of the regional difficulty
     */
    private double interpolateDifficulty(double difficulty) {
        double t = MathHelper.clamp((difficulty - 0) / 6.75, 0.0, 1.0);
        return t * t * (3.0 - 2.0 * t);
    }

}
