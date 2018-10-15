package ladysnake.bansheenight.client.render.entity;

import ladysnake.bansheenight.DeadManAbyss;
import ladysnake.bansheenight.client.model.ModelScreecher;
import ladysnake.bansheenight.entity.EntityScreecher;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class RenderScreecher extends RenderLiving<EntityScreecher> {

    private static final ResourceLocation SCREECHER_TEXTURE = new ResourceLocation(DeadManAbyss.MOD_ID, "textures/entity/screecher.png");
    private static final ResourceLocation SCREECHER_TEXTURE_BLOODY = new ResourceLocation(DeadManAbyss.MOD_ID, "textures/entity/screecher_bloody.png");

    public RenderScreecher(RenderManager rendermanagerIn) {
        super(rendermanagerIn, new ModelScreecher(), 1.0F);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityScreecher entity) {
        return entity.isBloody() ? SCREECHER_TEXTURE_BLOODY : SCREECHER_TEXTURE;
    }
}
