package ladysnake.deadmanabyss.entity;

import ladysnake.deadmanabyss.DmaConfig;
import ladysnake.deadmanabyss.api.capability.DmaEventHandler;
import ladysnake.deadmanabyss.api.capability.DmaSpawnable;
import ladysnake.deadmanabyss.capability.CapabilityDmaEvent;
import ladysnake.deadmanabyss.capability.CapabilityDmaSpawnable;
import ladysnake.deadmanabyss.entity.ai.EntityAIScreecherApproachSound;
import ladysnake.deadmanabyss.entity.ai.EntityAIScreecherNearestAttackableTarget;
import ladysnake.deadmanabyss.worldevent.DmaWorldHandler;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EntityScreecher extends EntityMob {
    private static final DataParameter<Boolean> BLOODY = EntityDataManager.createKey(EntityScreecher.class, DataSerializers.BOOLEAN);
    public static final int BASE_TRACKED_DISTANCE_FROM_SOUND_SQ = 9;

    private final DmaSpawnable capabilitySpawn = CapabilityDmaSpawnable.DMA_SPAWNABLE_CAPABILITY.getDefaultInstance();
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
            if (entityIn.getDistanceSq(soundLocation.getX(), soundLocation.getY(), soundLocation.getZ()) < BASE_TRACKED_DISTANCE_FROM_SOUND_SQ * soundLocation.getWeight()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityDmaSpawnable.DMA_SPAWNABLE_CAPABILITY || super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityDmaSpawnable.DMA_SPAWNABLE_CAPABILITY ? CapabilityDmaSpawnable.DMA_SPAWNABLE_CAPABILITY.cast(capabilitySpawn) : super.getCapability(capability, facing);
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
        // Die if there is no event occuring
        DmaEventHandler handler = this.world.getCapability(CapabilityDmaEvent.CAPABILITY_DMA_EVENT, null);
        if (handler == null || !handler.isEventOccuring()) {
            this.setFire(4);
            this.dealFireDamage(5);
        } else {
            // Add new sounds
            for (SoundLocation sound : DmaWorldHandler.getSoundsPlayed(world)) {
                float radius = Math.min(sound.getWeight() * 24, DmaConfig.maxScreecherHearingDistance);
                double distanceSq = this.getDistanceSq(sound.getX(), sound.getY(), sound.getZ());
                // Check that the sound is not outside of range or from the screecher itself
                if (distanceSq < radius * radius && distanceSq > 4) {
                    this.onSoundHeard(sound);
                }
            }
        }
    }

    public void onSoundHeard(SoundLocation loc) {
        SoundLocation nearest = null;
        double sqDistanceToNearest = Double.POSITIVE_INFINITY;
        // Look for the nearest known sound that can be merged with this one
        for (SoundLocation existing : soundsHeard) {
            double sqDistance = loc.squareDistanceTo(existing);
            if (sqDistance < SoundLocation.MAX_SOUND_MERGE_DISTANCE_SQ) {
                if (nearest == null || sqDistance < sqDistanceToNearest) {
                    nearest = existing;
                    sqDistanceToNearest = sqDistance;
                }
            }
        }
        if (nearest != null) {
            nearest.merge(loc);
        } else {
            soundsHeard.add(loc);
        }
    }

}
