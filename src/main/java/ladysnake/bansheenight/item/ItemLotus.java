package ladysnake.bansheenight.item;

import ladysnake.bansheenight.BansheeNightConfig;
import ladysnake.bansheenight.api.event.BansheeNightHandler;
import ladysnake.bansheenight.capability.CapabilityBansheeNight;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;

public class ItemLotus extends Item {

    //ritual: at new moon, toss a lotus into water around midnight. does not need to be perfect, you have a small time window
    @Override
    public boolean onEntityItemUpdate(EntityItem entityItem) {
        if (!entityItem.world.isRemote && entityItem.getAge() > 100 && BansheeNightConfig.triggers.lotusRitual && entityItem.isInWater() && entityItem.world.provider.getCurrentMoonPhaseFactor() == 0.0F) {
            BansheeNightHandler cap = entityItem.world.getCapability(CapabilityBansheeNight.CAPABILITY_BANSHEE_NIGHT, null);
            if(cap != null && !cap.isBansheeNightOccurring()) {
                float skyAngle = entityItem.world.getCelestialAngle(1.0F);
                if(skyAngle > 0.49F && skyAngle < 0.51F) {
                    cap.startBansheeNight();
                }
            }
        }
        return false;
    }
}
