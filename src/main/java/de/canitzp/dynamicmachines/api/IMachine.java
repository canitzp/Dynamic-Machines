package de.canitzp.dynamicmachines.api;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IMachine{
    
    default void init(Side side){}
    
    @Nonnull
    Block getBlock();
    
    @Nullable
    Item getItemBlock();
    
    boolean canTick(World world, BlockPos pos, NBTTagCompound attributes);
    
    void tick(World world, BlockPos pos, NBTTagCompound attributes);
    
    default void onPlaced(World world, BlockPos pos, NBTTagCompound attributes, IBlockState state, EntityLivingBase placer, ItemStack stack){}
    
    default <T> T getCapability(World world, BlockPos pos, NBTTagCompound attributes, Capability<T> cap, @Nullable EnumFacing side){
        return null;
    }
    
    @Nullable
    default IMachineGui getGui(){
        return null;
    }
    
}
