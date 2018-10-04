package ladysnake.bansheenight.entity;

import ladysnake.bansheenight.api.event.BansheeNightHandler;
import ladysnake.bansheenight.capability.CapabilityBansheeNight;
import ladysnake.bansheenight.capability.CapabilityBansheeNightSpawnable;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class EntityBanshee extends EntityMob {
    // TODO make an AI that can use this information
    // TODO make the sounds' weight fade over time, and remove them when it gets to 0 -- pyrofab
    private List<SoundLocation> soundsHeard = new ArrayList<>();

    public EntityBanshee(World worldIn) {
        super(worldIn);
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
    }
}
