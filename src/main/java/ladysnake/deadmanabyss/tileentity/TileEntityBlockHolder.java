package ladysnake.deadmanabyss.tileentity;

import ladysnake.deadmanabyss.api.capability.BlockHolderHandler;
import ladysnake.deadmanabyss.capability.CapabilityBlockHolder;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class TileEntityBlockHolder extends TileEntity {
    private BlockHolderHandler holder = CapabilityBlockHolder.CAPABILITY_BLOCK_HOLDER.getDefaultInstance();

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityBlockHolder.CAPABILITY_BLOCK_HOLDER || super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityBlockHolder.CAPABILITY_BLOCK_HOLDER) {
            return CapabilityBlockHolder.CAPABILITY_BLOCK_HOLDER.cast(holder);
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        CapabilityBlockHolder.CAPABILITY_BLOCK_HOLDER.readNBT(holder, null, compound.getTag("heldBlock"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setTag("heldBlock", CapabilityBlockHolder.CAPABILITY_BLOCK_HOLDER.writeNBT(holder, null));
        return compound;
    }
}
