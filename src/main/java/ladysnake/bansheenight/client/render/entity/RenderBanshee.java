package ladysnake.bansheenight.client.render.entity;

import ladysnake.bansheenight.entity.EntityBanshee;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class RenderBanshee extends RenderLiving<EntityBanshee> {
    public RenderBanshee(RenderManager rendermanagerIn) {
        super(rendermanagerIn, new ModelBiped(), 1f);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityBanshee entity) {
        return null;
    }
}
