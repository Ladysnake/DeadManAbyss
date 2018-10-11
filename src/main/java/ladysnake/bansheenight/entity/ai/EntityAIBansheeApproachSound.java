package ladysnake.bansheenight.entity.ai;

import ladysnake.bansheenight.entity.EntityBanshee;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.math.Vec3d;

import java.util.Collections;
import java.util.Comparator;

public class EntityAIBansheeApproachSound extends EntityAIBase {
    private final EntityBanshee creature;
    private double movePosX;
    private double movePosY;
    private double movePosZ;
    private final double movementSpeed;

    public EntityAIBansheeApproachSound(EntityBanshee creatureIn, double speedIn) {
        this.creature = creatureIn;
        this.movementSpeed = speedIn;
        this.setMutexBits(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute() {
        if (this.creature.isWithinHomeDistanceCurrentPosition()) {
            return false;
        } else {
            EntityBanshee.SoundLocation loc = Collections.max(this.creature.getSoundsHeard(),
                    Comparator.comparingDouble(EntityBanshee.SoundLocation::getWeight)
                            .thenComparingDouble(sl -> this.creature.getDistanceSq(sl.getX(), sl.getY(), sl.getZ())));
            Vec3d vec3d = RandomPositionGenerator.findRandomTargetBlockTowards(this.creature, 5, 5, new Vec3d(loc.getX(), loc.getY(), loc.getZ()));

            if (vec3d == null) {
                return false;
            } else {
                this.movePosX = vec3d.x;
                this.movePosY = vec3d.y;
                this.movePosZ = vec3d.z;
                return true;
            }
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean shouldContinueExecuting() {
        return !this.creature.getNavigator().noPath();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting() {
        this.creature.getNavigator().tryMoveToXYZ(this.movePosX, this.movePosY, this.movePosZ, this.movementSpeed);
    }
}
