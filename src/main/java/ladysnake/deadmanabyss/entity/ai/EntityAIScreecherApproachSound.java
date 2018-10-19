package ladysnake.deadmanabyss.entity.ai;

import ladysnake.deadmanabyss.entity.EntityScreecher;
import ladysnake.deadmanabyss.entity.SoundLocation;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.math.Vec3d;

import java.util.Iterator;

public class EntityAIScreecherApproachSound extends EntityAIBase {
    public static final int MIN_SEEK_DISTANCE_SQ = 64;
    private final EntityScreecher creature;
    private double movePosX;
    private double movePosY;
    private double movePosZ;
    private final double movementSpeed;

    public EntityAIScreecherApproachSound(EntityScreecher creatureIn, double speedIn) {
        this.creature = creatureIn;
        this.movementSpeed = speedIn;
        this.setMutexBits(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute() {
        Iterator<SoundLocation> iterator = this.creature.getSoundsHeard().iterator();
        if (!iterator.hasNext()) {
            return false;
        }
        SoundLocation loc = iterator.next();
        double locDistanceSq = this.creature.getDistanceSq(loc.getX(), loc.getY(), loc.getZ());
        while (iterator.hasNext()) {
            SoundLocation next = iterator.next();
            double distanceSq = this.creature.getDistanceSq(next.getX(), next.getY(), next.getZ());
            // don't seek any sound closer than 8 blocks + max by weight then min distance
            if (distanceSq > MIN_SEEK_DISTANCE_SQ && (locDistanceSq < MIN_SEEK_DISTANCE_SQ ||
                    (loc.getWeight() < next.getWeight() ||
                    (loc.getWeight() == next.getWeight() && locDistanceSq > distanceSq)))) {
                loc = next;
                locDistanceSq = distanceSq;
            }
        }

        // All sounds heard are too close
        if (locDistanceSq < MIN_SEEK_DISTANCE_SQ) {
            return false;
        }
        Vec3d vec3d = RandomPositionGenerator.findRandomTargetBlockTowards(this.creature, 5, 5, new Vec3d(loc.getX(), loc.getY(), loc.getZ()));

        if (vec3d != null) {
            this.movePosX = vec3d.x;
            this.movePosY = vec3d.y;
            this.movePosZ = vec3d.z;
            return true;
        }
        return false;
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
