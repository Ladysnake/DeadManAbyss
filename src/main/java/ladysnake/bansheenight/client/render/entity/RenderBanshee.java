package ladysnake.bansheenight.client.render.entity;

import ladysnake.bansheenight.BansheeNight;
import ladysnake.bansheenight.client.model.ModelBanshee;
import ladysnake.bansheenight.entity.EntityBanshee;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class RenderBanshee extends RenderLiving<EntityBanshee> {

    private static final ResourceLocation BANSHEE_TEXTURE = new ResourceLocation(BansheeNight.MOD_ID, "textures/entity/banshee.png");
    private static final ResourceLocation BANSHEE_TEXTURE_BLOODY = new ResourceLocation(BansheeNight.MOD_ID, "textures/entity/banshee_bloody.png");

    public RenderBanshee(RenderManager rendermanagerIn) {
        super(rendermanagerIn, new ModelBanshee(), 1.0F);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityBanshee entity) {
        return entity.isBloody() ? BANSHEE_TEXTURE_BLOODY : BANSHEE_TEXTURE;
    }
}
