package ladysnake.deadmanabyss.item;

import ladysnake.deadmanabyss.DmaConfig;
import ladysnake.deadmanabyss.api.event.DmaEventHandler;
import ladysnake.deadmanabyss.capability.CapabilityDmaEvent;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.WorldServer;

public class ItemBlindQuartz extends Item {

    @Override
    public boolean onEntityItemUpdate(EntityItem entityItem) {
        if(!entityItem.world.isRemote && entityItem.getAge() > 100 && DmaConfig.triggers.quartzRitual && entityItem.isInWater() && entityItem.getEntityData().getBoolean("Trigger")) {
            DmaEventHandler cap = entityItem.world.getCapability(CapabilityDmaEvent.CAPABILITY_DMA_EVENT, null);
            if(cap != null && cap.getTicksSinceLastEvent() >= DmaConfig.minTicksBetweenEvents) {
                cap.startEvent();
                entityItem.setDead();

                //TODO adjust particles ^Up
                ((WorldServer) entityItem.world).spawnParticle(EnumParticleTypes.END_ROD, entityItem.posX, entityItem.posY, entityItem.posZ, 20, 0.0D, 0.0D, 0.0D, 0.005D);
            }
        }
        return false;
    }
}
