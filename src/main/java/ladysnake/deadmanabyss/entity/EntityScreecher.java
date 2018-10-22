package ladysnake.deadmanabyss.entity;

import ladysnake.deadmanabyss.api.capability.DmaEventHandler;
import ladysnake.deadmanabyss.api.capability.DmaSpawnable;
import ladysnake.deadmanabyss.capability.CapabilityDmaEvent;
import ladysnake.deadmanabyss.capability.CapabilityDmaSpawnable;
import ladysnake.deadmanabyss.entity.ai.EntityAIScreecherApproachSound;
import ladysnake.deadmanabyss.entity.ai.EntityAIScreecherNearestAttackableTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;
import java.util.*;

public class EntityScreecher extends EntityMob {
    private static final DataParameter<Boolean> BLOODY = EntityDataManager.createKey(EntityScreecher.class, DataSerializers.BOOLEAN);
    public static final int BASE_TRACKED_DISTANCE_FROM_SOUND_SQ = 9;

    private List<SoundLocation> soundsHeard = new ArrayList<>();

    public EntityScreecher(World worldIn) {
        super(worldIn);
        this.setSize(1.5f, 3.6f);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(BLOODY, false);
    }

    @Override
    protected void initEntityAI() {
        this.tasks.addTask(2, new EntityAIAttackMelee(this, 1.5D, true));
        this.tasks.addTask(5, new EntityAIScreecherApproachSound(this, 0.7));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
        this.targetTasks.addTask(2, new EntityAIScreecherNearestAttackableTarget<>(this, EntityPlayer.class, true));
        this.targetTasks.addTask(6, new EntityAIScreecherNearestAttackableTarget<>(this, EntityLivingBase.class, true));
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        if(this.isBloody()) compound.setBoolean("Bloody", true);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        if(compound.getBoolean("Bloody")) this.setBloody(true);
    }

    @Override
    public void onKillEntity(EntityLivingBase entityLivingIn) {
        super.onKillEntity(entityLivingIn);
        this.heal(entityLivingIn.getMaxHealth() / 2);
        this.setBloody(true);
    }

    public void setBloody(boolean bloody) {
        this.dataManager.set(BLOODY, bloody);
    }

    public boolean isBloody() {
        return this.dataManager.get(BLOODY);
    }

    public List<SoundLocation> getSoundsHeard() {
        return soundsHeard;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.2D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.5D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(60.0D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(16);
    }

    @Override
    public boolean getCanSpawnHere() {
        DmaEventHandler cap = this.world.getCapability(CapabilityDmaEvent.CAPABILITY_DMA_EVENT, null);
        return cap != null && cap.isEventOccuring() && super.getCanSpawnHere();
    }

    @Override
    protected boolean isValidLightLevel() {
        BlockPos blockpos = new BlockPos(this.posX, this.getEntityBoundingBox().minY, this.posZ);
        return this.world.getLightFromNeighbors(blockpos) <= this.world.rand.nextInt(10);
    }

    @Override
    public boolean canEntityBeSeen(Entity entityIn) {
        for (SoundLocation soundLocation : soundsHeard) {
            if (entityIn.getDistanceSq(soundLocation.x, soundLocation.y, soundLocation.z) < BASE_TRACKED_DISTANCE_FROM_SOUND_SQ * soundLocation.getWeight()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        // Make the screecher forget sounds after some time
        // Single sounds are forgotten after 5 seconds on average
        if (world.rand.nextInt(100) == 0) {
            for (Iterator<SoundLocation> iterator = soundsHeard.iterator(); iterator.hasNext(); ) {
                SoundLocation loc = iterator.next();
                loc.fade(0.1f);
                if (loc.getWeight() <= 0) {
                    iterator.remove();
                }
            }
        }
    }

    public void onSoundHeard(SoundEvent soundIn, SoundCategory category, double x, double y, double z, float volume) {
        // Check that the sound is not from the screecher itself
        if (category != SoundCategory.AMBIENT && this.getDistanceSq(x, y, z) > 4) {
            SoundLocation loc = new SoundLocation(x, y, z, volume);
            Optional<SoundLocation> existing = soundsHeard.stream()
                    .filter(sl -> sl.squareDistanceTo(loc) < SoundLocation.MAX_SOUND_MERGE_DISTANCE_SQ)
                    .min(Comparator.comparing(loc::squareDistanceTo));
            if (existing.isPresent()) {
                existing.get().merge(loc);
            } else {
                soundsHeard.add(loc);
            }
        }
    }

    public static class SoundLocation {
        public static final int MAX_SOUND_MERGE_DISTANCE_SQ = 25;

        private double x, y, z;
        private float weight;

        public SoundLocation(double x, double y, double z, float volume) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.weight = volume;
        }

        public double squareDistanceTo(SoundLocation vec) {
            double d0 = vec.x - this.x;
            double d1 = vec.y - this.y;
            double d2 = vec.z - this.z;
            return d0 * d0 + d1 * d1 + d2 * d2;
        }

        public void merge(SoundLocation that) {
            this.x = ((this.x * this.weight) + (that.x * that.weight)) / (this.weight + that.weight);
            this.y = ((this.y * this.weight) + (that.y * that.weight)) / (this.weight + that.weight);
            this.z = ((this.z * this.weight) + (that.z * that.weight)) / (this.weight + that.weight);
            this.weight += that.weight;
        }

        public float getWeight() {
            return this.weight;
        }

        public void fade(float v) {
            this.weight -= v;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public double getZ() {
            return z;
        }
    }
}
