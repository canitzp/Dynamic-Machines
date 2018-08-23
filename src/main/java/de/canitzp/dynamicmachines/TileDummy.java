package de.canitzp.dynamicmachines;

import de.canitzp.dynamicmachines.api.IMachine;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class TileDummy extends TileEntity{
    
    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing){
        return getCapability(capability, facing) != null;
    }
    
    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing){
        IBlockState state = this.getWorld().getBlockState(this.getPos());
        if(state.getBlock() instanceof IMachine){
            for(MachineData data : DynamicMachines.machines){
                if(data.getWorld() == this.getWorld() && data.getPos().equals(this.getPos())){
                    return ((IMachine) state.getBlock()).getCapability(this.world, this.pos, data.getAttributes(), capability, facing);
                }
            }
        }
        return super.getCapability(capability, facing);
    }
}
