package ladysnake.deadmanabyss.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

/**
 * NewProject - Undefined
 * Created using Tabula 7.0.0
 */
public class ModelScreecher extends ModelBase {
    private final ModelRenderer mainBox = new ModelRenderer(this, 0, 0);
    private final ModelRenderer head;
    private final ModelRenderer pelvis;
    private final ModelRenderer abdomen;
    private final ModelRenderer upperArmRight;
    private final ModelRenderer lowerArmRight;
    private final ModelRenderer upperArmLeft;
    private final ModelRenderer lowerArmLeft;
    private final ModelRenderer lowerLegRight;
    private final ModelRenderer upperLegRight;
    private final ModelRenderer upperLegLeft;
    private final ModelRenderer lowerLegLeft;

    public ModelScreecher() {
        this.textureWidth = 128;
        this.textureHeight = 64;
        upperLegRight = new ModelRenderer(this, 114, 0);
        upperLegRight.setRotationPoint(-4.5F, 6.0F, 0.0F);
        upperLegRight.addBox(-1.5F, 0.0F, -1.5F, 3, 14, 3, 0.0F);
        this.setRotateAngle(upperLegRight, -0.10479588278595683F, 0.2724692952434879F, 0.2829488835220835F);
        lowerLegLeft = new ModelRenderer(this, 95, 15);
        lowerLegLeft.setRotationPoint(0.0F, 14.0F, 0.0F);
        lowerLegLeft.addBox(-1.5F, 0.0F, -1.5F, 3, 14, 3, 0.0F);
        this.setRotateAngle(lowerLegLeft, 0.7954282294561152F, 0.0F, 0.0F);
        abdomen = new ModelRenderer(this, 40, 0);
        abdomen.setRotationPoint(0.0F, 4.0F, 0.0F);
        abdomen.addBox(-4.0F, 0.0F, -2.5F, 8, 12, 5, 0.0F);
        this.setRotateAngle(abdomen, -0.36257079587867613F, 0.0F, 0.0F);
        upperArmRight = new ModelRenderer(this, 66, 0);
        upperArmRight.setRotationPoint(-7.0F, -4.0F, 0.0F);
        upperArmRight.addBox(-2.0F, 0.0F, -1.0F, 2, 16, 2, 0.0F);
        this.setRotateAngle(upperArmRight, -0.09874139472463977F, 0.0F, 0.49774697671033563F);
        upperLegLeft = new ModelRenderer(this, 71, 15);
        upperLegLeft.setRotationPoint(4.5F, 6.0F, 0.0F);
        upperLegLeft.addBox(-1.5F, 0.0F, -1.5F, 3, 14, 3, 0.0F);
        this.setRotateAngle(upperLegLeft, 0.15719382417893532F, -0.23055094212910504F, -0.20959176557191372F);
        lowerArmLeft = new ModelRenderer(this, 44, 17);
        lowerArmLeft.setRotationPoint(1.0F, 16.0F, 0.0F);
        lowerArmLeft.addBox(-1.0F, 0.0F, -1.0F, 2, 18, 2, 0.0F);
        this.setRotateAngle(lowerArmLeft, -0.8763298174263529F, 0.0F, 0.0F);
        lowerLegRight = new ModelRenderer(this, 83, 15);
        lowerLegRight.setRotationPoint(0.0F, 14.0F, 0.0F);
        lowerLegRight.addBox(-1.5F, 0.0F, -1.5F, 3, 14, 3, 0.0F);
        this.setRotateAngle(lowerLegRight, 0.6392548849943369F, 0.0F, 0.0F);
        lowerArmRight = new ModelRenderer(this, 36, 17);
        lowerArmRight.setRotationPoint(-1.0F, 16.0F, 0.0F);
        lowerArmRight.addBox(-1.0F, 0.0F, -1.0F, 2, 18, 2, 0.0F);
        this.setRotateAngle(lowerArmRight, -0.8206538142877338F, 0.0F, 0.0F);
        this.mainBox.setRotationPoint(0.0F, -26.0F, 0.0F);
        this.mainBox.addBox(-7.0F, -4.0F, -3.0F, 14, 8, 6, 0.0F);
        this.setRotateAngle(mainBox, 0.5387458531676815F, 0.0F, 0.0F);
        pelvis = new ModelRenderer(this, 0, 14);
        pelvis.setRotationPoint(0.0F, 12.0F, 0.0F);
        pelvis.addBox(-6.0F, 0.0F, -3.0F, 12, 6, 6, 0.0F);
        this.setRotateAngle(pelvis, -0.5599541238425108F, 0.0F, 0.0F);
        head = new ModelRenderer(this, 74, 0);
        head.setRotationPoint(0.0F, -4.0F, 0.0F);
        head.addBox(-4.0F, -7.0F, -4.0F, 8, 7, 8, 0.0F);
        this.setRotateAngle(head, -0.23726386183546194F, 0.0F, 0.0F);
        upperArmLeft = new ModelRenderer(this, 106, 0);
        upperArmLeft.setRotationPoint(7.0F, -4.0F, 0.0F);
        upperArmLeft.addBox(0.0F, 0.0F, -1.0F, 2, 16, 2, 0.0F);
        this.setRotateAngle(upperArmLeft, -0.04446097056613121F, 0.14346962445086142F, -0.5335151538014832F);
        pelvis.addChild(upperLegRight);
        upperLegLeft.addChild(lowerLegLeft);
        this.mainBox.addChild(abdomen);
        this.mainBox.addChild(upperArmRight);
        pelvis.addChild(upperLegLeft);
        upperArmLeft.addChild(lowerArmLeft);
        upperLegRight.addChild(lowerLegRight);
        upperArmRight.addChild(lowerArmRight);
        abdomen.addChild(pelvis);
        this.mainBox.addChild(head);
        this.mainBox.addChild(upperArmLeft);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.mainBox.render(f5);
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float age, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
        this.head.rotateAngleY = (float) Math.toRadians(netHeadYaw);
        this.head.rotateAngleX = (float) Math.toRadians(headPitch);

//        this.upperArmRight.rotateAngleZ = 0;//athis.computeAnimation(0.1F, 0.4F, false, 0.0F, -1.0F, age, -0.1F);
//        this.upperArmLeft.rotateAngleZ = 0;//this.computeAnimation(0.1F, 0.4F, true, 0.0F, -1.0F, age, -0.1F);
//
//        this.lowerArmRight.rotateAngleZ = 0;//this.computeAnimation(0.1F, 0.4F, true, 0.4F, -1.0F, age, -0.1F);
//        this.lowerArmLeft.rotateAngleZ = 0;//this.computeAnimation(0.1F, 0.4F, false, 0.4F, -1.0F, age, -0.1F);
//
//        boolean dragging = false;
//
        float globalSpeed = 0.6F;
        float globalDegree = 1.4F;

//        this.abdomen.rotationPointY = 0; // 7.0F + this.computeAnimation(globalSpeed * 2.0F, globalDegree, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
//
        this.upperLegRight.rotateAngleX =  this.computeAnimation(globalSpeed, globalDegree * 1.2F, false, 0.0F, -0.6F, limbSwing, limbSwingAmount);
        this.upperLegLeft.rotateAngleX =  this.computeAnimation(globalSpeed, globalDegree * 1.2F, true, 0.0F, 0.6F, limbSwing, limbSwingAmount);
//
        this.lowerLegRight.rotateAngleX = 0.6392548849943369F;//this.computeAnimation(globalSpeed, globalDegree * 0.65F, false, -2.2F, 1.0F, limbSwing, limbSwingAmount);
        this.lowerLegLeft.rotateAngleX = 0.7954282294561152F;//this.computeAnimation(globalSpeed, globalDegree * 0.65F, true, -2.2F, -1.0F, limbSwing, limbSwingAmount);
//
        this.upperArmRight.rotateAngleX = -0.09874139472463977F;//this.computeAnimation(globalSpeed, globalDegree * 0.8F, true, 0.0F, 0.0F, limbSwing, limbSwingAmount);
        this.upperArmLeft.rotateAngleX = -0.04446097056613121F;//this.computeAnimation(globalSpeed, globalDegree * 0.8F, false, 0.0F, 0.0F, limbSwing, limbSwingAmount);
//
        this.lowerArmRight.rotateAngleX = -0.8206538142877338F;//this.computeAnimation(globalSpeed, globalDegree * 0.6F, true, -1.4F, 0.7F, limbSwing, limbSwingAmount);
        this.lowerArmLeft.rotateAngleX = -0.8206538142877338F;//this.computeAnimation(globalSpeed, globalDegree * 0.6F, false, -1.4F, -0.7F, limbSwing, limbSwingAmount);
    }

    private float computeAnimation(float speed, float degree, boolean invert, float offset, float weight, float limbSwing, float limbSwingAmount) {
        float theta = limbSwing * speed + offset;
        float scaledWeight = weight * limbSwingAmount;
        float rotation = (MathHelper.cos(theta) * degree * limbSwingAmount) + scaledWeight;
        return invert ? -rotation : rotation;
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
