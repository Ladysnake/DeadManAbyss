package ladysnake.bansheenight.item;

import ladysnake.bansheenight.BansheeNightConfig;
import ladysnake.bansheenight.api.event.BansheeNightHandler;
import ladysnake.bansheenight.capability.CapabilityBansheeNight;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.WorldServer;

public class ItemLotus extends Item {

    @Override
    public boolean onEntityItemUpdate(EntityItem entityItem) {
        if (!entityItem.world.isRemote && entityItem.getAge() > 100 && BansheeNightConfig.triggers.lotusRitual && entityItem.isInWater() && entityItem.world.provider.getCurrentMoonPhaseFactor() == 0.0F) {
            BansheeNightHandler cap = entityItem.world.getCapability(CapabilityBansheeNight.CAPABILITY_BANSHEE_NIGHT, null);
            if(cap != null && !cap.isBansheeNightOccurring()) {
                float skyAngle = entityItem.world.getCelestialAngle(1.0F);
                if(skyAngle > 0.49F && skyAngle < 0.51F) {
                    cap.startBansheeNight();
                    entityItem.setDead();

                    //TODO adjust particles ^Up
                    ((WorldServer) entityItem.world).spawnParticle(EnumParticleTypes.END_ROD, entityItem.posX, entityItem.posY, entityItem.posZ, 20, 0.0D, 0.0D, 0.0D, 0.005D);
                }
            }
        }
        return false;
    }
}
