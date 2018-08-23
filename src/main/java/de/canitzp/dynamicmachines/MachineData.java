package de.canitzp.dynamicmachines;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MachineData{
    
    private World world;
    private BlockPos pos;
    private NBTTagCompound attributes;
    
    public MachineData(World world, BlockPos pos, NBTTagCompound attributes){
        this.world = world;
        this.pos = pos;
        this.attributes = attributes;
    }
    
    public World getWorld(){
        return world;
    }
    
    public MachineData setWorld(World world){
        this.world = world;
        return this;
    }
    
    public BlockPos getPos(){
        return pos;
    }
    
    public MachineData setPos(BlockPos pos){
        this.pos = pos;
        return this;
    }
    
    public NBTTagCompound getAttributes(){
        return attributes;
    }
    
    public MachineData setAttributes(NBTTagCompound attributes){
        this.attributes = attributes;
        return this;
    }
}
