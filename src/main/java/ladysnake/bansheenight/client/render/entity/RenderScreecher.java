package ladysnake.bansheenight.client.render.entity;

import ladysnake.bansheenight.BansheeNight;
import ladysnake.bansheenight.client.model.ModelScreecher;
import ladysnake.bansheenight.entity.EntityScreecher;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class RenderScreecher extends RenderLiving<EntityScreecher> {

    private static final ResourceLocation BANSHEE_TEXTURE = new ResourceLocation(BansheeNight.MOD_ID, "textures/entity/banshee.png");
    private static final ResourceLocation BANSHEE_TEXTURE_BLOODY = new ResourceLocation(BansheeNight.MOD_ID, "textures/entity/banshee_bloody.png");

    public RenderScreecher(RenderManager rendermanagerIn) {
        super(rendermanagerIn, new ModelScreecher(), 1.0F);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityScreecher entity) {
        return entity.isBloody() ? BANSHEE_TEXTURE_BLOODY : BANSHEE_TEXTURE;
    }
}
