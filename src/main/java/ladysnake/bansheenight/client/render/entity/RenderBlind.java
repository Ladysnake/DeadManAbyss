package ladysnake.bansheenight.client.render.entity;

import ladysnake.bansheenight.client.model.ModelBlind;
import ladysnake.bansheenight.entity.EntityBlind;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.entity.layers.LayerVillagerArmor;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class RenderBlind extends RenderBiped<EntityBlind> {

    //TODO add texture (and model) ^Up
    private static final ResourceLocation ZOMBIE_VILLAGER_PRIEST_LOCATION = new ResourceLocation("textures/entity/zombie_villager/zombie_priest.png");

    public RenderBlind(RenderManager rendermanagerIn) {
        super(rendermanagerIn, new ModelBlind(), 0.5F);
        this.addLayer(new LayerVillagerArmor(this));
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityBlind entity) {
        return ZOMBIE_VILLAGER_PRIEST_LOCATION;
    }
}
