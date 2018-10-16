package ladysnake.deadmanabyss.entity;

import ladysnake.deadmanabyss.DeadManAbyss;
import ladysnake.deadmanabyss.api.event.DmaSpawnable;
import ladysnake.deadmanabyss.capability.CapabilityDmaSpawnable;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;
import java.util.UUID;

public class EntityBlind extends EntityMob {

    private static final ResourceLocation LOOT = new ResourceLocation(DeadManAbyss.MOD_ID, "entities/blind");
    private static final DataParameter<Boolean> IS_CHILD = EntityDataManager.createKey(EntityBlind.class, DataSerializers.BOOLEAN);
    private static final UUID BABY_SPEED_BOOST_ID = UUID.fromString("54b625b3-e51b-4612-9d14-d7d1b5f55002");
    private static final AttributeModifier BABY_SPEED_BOOST = new AttributeModifier(BABY_SPEED_BOOST_ID, "Baby speed boost", 0.5D, 1);
    private static final DataParameter<Boolean> ARMS_RAISED = EntityDataManager.createKey(EntityZombie.class, DataSerializers.BOOLEAN);
    private final DmaSpawnable CAPABILITY_SPAWN = CapabilityDmaSpawnable.DMA_SPAWNABLE_CAPABILITY.getDefaultInstance();
    private static final float width = 0.6F;
    private static final float height = 1.95F;

    public EntityBlind(World worldIn) {
        super(worldIn);
        this.setSize(width, height);
    }

    @Override
    protected void initEntityAI() {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIAttackMelee(this, 1.0D, false));
        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
        this.tasks.addTask(7, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.tasks.addTask(6, new EntityAIMoveThroughVillage(this, 1.0D, false));
        this.tasks.addTask(5, new EntityAIBreakDoor(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, true));
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(IS_CHILD, false);
        this.dataManager.register(ARMS_RAISED, false);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityDmaSpawnable.DMA_SPAWNABLE_CAPABILITY || super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityDmaSpawnable.DMA_SPAWNABLE_CAPABILITY ? CapabilityDmaSpawnable.DMA_SPAWNABLE_CAPABILITY.cast(CAPABILITY_SPAWN) : super.getCapability(capability, facing);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        if(this.isChild()) compound.setBoolean("IsBaby", true);
        if(this.isArmsRaised()) compound.setBoolean("ArmsRaised", true);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        if(compound.getBoolean("IsBaby")) this.setChild(true);
        if(compound.getBoolean("ArmsRaised")) this.setArmsRaised(true);
    }

    @Nullable
    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        if(rand.nextFloat() < 0.1F) {
            this.setArmsRaised(true);
        }
        else if(rand.nextFloat() < 0.05F) {
            this.setChild(true);
        }
        return super.onInitialSpawn(difficulty, livingdata);
    }

    private void setChildSize(boolean isChild) {
        if(isChild) {
            this.setSize(width / 2.0F, height / 2.0F);
        }
        else {
            this.setSize(width, height);
        }
    }

    public boolean isChild() {
        return this.dataManager.get(IS_CHILD);
    }

    public void setChild(boolean isChild) {
        this.dataManager.set(IS_CHILD, isChild);
        if(this.world != null && !this.world.isRemote) {
            IAttributeInstance attribute = this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
            attribute.removeModifier(BABY_SPEED_BOOST);

            if(isChild) {
                attribute.applyModifier(BABY_SPEED_BOOST);
            }
        }
        this.setChildSize(isChild);
    }

    @Override
    public void notifyDataManagerChange(DataParameter<?> key) {
        if(IS_CHILD.equals(key)) this.setChildSize(this.isChild());
        super.notifyDataManagerChange(key);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.LUCK);

        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(15.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.23000001217232513D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.5D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(4.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(14.0D);
        this.getEntityAttribute(SharedMonsterAttributes.LUCK).setBaseValue(-1024.0D);
    }

    public boolean isArmsRaised() {
        return this.dataManager.get(ARMS_RAISED);
    }

    public void setArmsRaised(boolean armsRaised) {
        this.dataManager.set(ARMS_RAISED, armsRaised);
    }

    @Nullable
    @Override
    protected ResourceLocation getLootTable() {
        return LOOT;
    }
}
