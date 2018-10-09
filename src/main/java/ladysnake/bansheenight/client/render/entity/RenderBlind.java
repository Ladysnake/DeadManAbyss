package ladysnake.bansheenight.client.render.entity;

import ladysnake.bansheenight.BansheeNight;
import ladysnake.bansheenight.entity.EntityBlind;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class RenderBlind extends RenderBiped<EntityBlind> {

    private static final ResourceLocation BLIND_TEXTURE = new ResourceLocation(BansheeNight.MOD_ID, "textures/entity/blind.png");

    public RenderBlind(RenderManager rendermanagerIn) {
        super(rendermanagerIn, new ModelZombie(), 0.5F);
        this.addLayer(new LayerBipedArmor(this));
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityBlind entity) {
        return BLIND_TEXTURE;
    }
}
