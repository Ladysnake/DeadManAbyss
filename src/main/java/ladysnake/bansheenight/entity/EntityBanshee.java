package ladysnake.bansheenight.entity;

import ladysnake.bansheenight.api.event.BansheeNightHandler;
import ladysnake.bansheenight.capability.CapabilityBansheeNight;
import ladysnake.bansheenight.capability.CapabilityBansheeNightSpawnable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;
import java.util.*;

public class EntityBanshee extends EntityMob {
    // TODO make an AI that can use this information
    private List<SoundLocation> soundsHeard = new ArrayList<>();
    private static final DataParameter<Boolean> BLOODY = EntityDataManager.createKey(EntityBanshee.class, DataSerializers.BOOLEAN);

    public EntityBanshee(World worldIn) {
        super(worldIn);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(BLOODY, false);
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

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(60.0D);
    }

    @Override
    public boolean getCanSpawnHere() {
        BansheeNightHandler cap = this.world.getCapability(CapabilityBansheeNight.CAPABILITY_BANSHEE_NIGHT, null);
        return cap != null && cap.isBansheeNightOccurring() && super.getCanSpawnHere();
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityBansheeNightSpawnable.CAPABILITY_BANSHEE_NIGHT_SPAWN || super.hasCapability(capability, facing);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        // Make the banshee forget sounds after some time
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
        if (category != SoundCategory.AMBIENT) {
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
    }
}
