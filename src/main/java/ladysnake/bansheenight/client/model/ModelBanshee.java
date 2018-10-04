package ladysnake.bansheenight.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * NewProject - Undefined
 * Created using Tabula 7.0.0
 */
public class ModelBanshee extends ModelBase {
    private final ModelRenderer mainBox = new ModelRenderer(this, 0, 0);

    public ModelBanshee() {
        this.textureWidth = 128;
        this.textureHeight = 64;
        ModelRenderer shape17_2 = new ModelRenderer(this, 114, 0);
        shape17_2.setRotationPoint(-4.5F, 6.0F, 0.0F);
        shape17_2.addBox(-1.5F, 0.0F, -1.5F, 3, 14, 3, 0.0F);
        this.setRotateAngle(shape17_2, -0.10479588278595683F, 0.2724692952434879F, 0.2829488835220835F);
        ModelRenderer shape17_5 = new ModelRenderer(this, 95, 15);
        shape17_5.setRotationPoint(0.0F, 14.0F, 0.0F);
        shape17_5.addBox(-1.5F, 0.0F, -1.5F, 3, 14, 3, 0.0F);
        this.setRotateAngle(shape17_5, 0.7954282294561152F, 0.0F, 0.0F);
        ModelRenderer shape16 = new ModelRenderer(this, 40, 0);
        shape16.setRotationPoint(0.0F, 4.0F, 0.0F);
        shape16.addBox(-4.0F, 0.0F, -2.5F, 8, 12, 5, 0.0F);
        this.setRotateAngle(shape16, -0.36257079587867613F, 0.0F, 0.0F);
        ModelRenderer shape17 = new ModelRenderer(this, 66, 0);
        shape17.setRotationPoint(-7.0F, -4.0F, 0.0F);
        shape17.addBox(-2.0F, 0.0F, -1.0F, 2, 16, 2, 0.0F);
        this.setRotateAngle(shape17, -0.09874139472463977F, 0.0F, 0.49774697671033563F);
        ModelRenderer shape17_3 = new ModelRenderer(this, 71, 15);
        shape17_3.setRotationPoint(4.5F, 6.0F, 0.0F);
        shape17_3.addBox(-1.5F, 0.0F, -1.5F, 3, 14, 3, 0.0F);
        this.setRotateAngle(shape17_3, 0.15719382417893532F, -0.23055094212910504F, -0.20959176557191372F);
        ModelRenderer shape17_7 = new ModelRenderer(this, 44, 17);
        shape17_7.setRotationPoint(1.0F, 16.0F, 0.0F);
        shape17_7.addBox(-1.0F, 0.0F, -1.0F, 2, 18, 2, 0.0F);
        this.setRotateAngle(shape17_7, -0.8763298174263529F, 0.0F, 0.0F);
        ModelRenderer shape17_4 = new ModelRenderer(this, 83, 15);
        shape17_4.setRotationPoint(0.0F, 14.0F, 0.0F);
        shape17_4.addBox(-1.5F, 0.0F, -1.5F, 3, 14, 3, 0.0F);
        this.setRotateAngle(shape17_4, 0.6392548849943369F, 0.0F, 0.0F);
        ModelRenderer shape17_6 = new ModelRenderer(this, 36, 17);
        shape17_6.setRotationPoint(-1.0F, 16.0F, 0.0F);
        shape17_6.addBox(-1.0F, 0.0F, -1.0F, 2, 18, 2, 0.0F);
        this.setRotateAngle(shape17_6, -0.8206538142877338F, 0.0F, 0.0F);
        this.mainBox.setRotationPoint(0.0F, -26.0F, 0.0F);
        this.mainBox.addBox(-7.0F, -4.0F, -3.0F, 14, 8, 6, 0.0F);
        this.setRotateAngle(mainBox, 0.5387458531676815F, 0.0F, 0.0F);
        ModelRenderer shape26 = new ModelRenderer(this, 0, 14);
        shape26.setRotationPoint(0.0F, 12.0F, 0.0F);
        shape26.addBox(-6.0F, 0.0F, -3.0F, 12, 6, 6, 0.0F);
        this.setRotateAngle(shape26, -0.5599541238425108F, 0.0F, 0.0F);
        ModelRenderer shape31 = new ModelRenderer(this, 74, 0);
        shape31.setRotationPoint(0.0F, -4.0F, 0.0F);
        shape31.addBox(-4.0F, -7.0F, -4.0F, 8, 7, 8, 0.0F);
        this.setRotateAngle(shape31, -0.23726386183546194F, 0.0F, 0.0F);
        ModelRenderer shape17_1 = new ModelRenderer(this, 106, 0);
        shape17_1.setRotationPoint(7.0F, -4.0F, 0.0F);
        shape17_1.addBox(0.0F, 0.0F, -1.0F, 2, 16, 2, 0.0F);
        this.setRotateAngle(shape17_1, -0.04446097056613121F, 0.14346962445086142F, -0.5335151538014832F);
        shape26.addChild(shape17_2);
        shape17_3.addChild(shape17_5);
        this.mainBox.addChild(shape16);
        this.mainBox.addChild(shape17);
        shape26.addChild(shape17_3);
        shape17_1.addChild(shape17_7);
        shape17_2.addChild(shape17_4);
        shape17.addChild(shape17_6);
        shape16.addChild(shape26);
        this.mainBox.addChild(shape31);
        this.mainBox.addChild(shape17_1);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.mainBox.render(f5);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
