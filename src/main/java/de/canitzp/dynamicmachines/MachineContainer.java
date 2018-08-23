package de.canitzp.dynamicmachines;

import de.canitzp.dynamicmachines.api.IMachineGui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class MachineContainer extends Container{
    
    private IMachineGui machineGui;
    private EntityPlayer player;
    
    public MachineContainer(EntityPlayer player, IMachineGui machineGui){
        this.machineGui = machineGui;
        this.player = player;
        
        this.machineGui.initContainer(this);
    }
    
    public EntityPlayer getPlayer(){
        return player;
    }
    
    @Override
    public boolean canInteractWith(@Nonnull EntityPlayer player){
        return this.machineGui.canInteractWith(this, player);
    }
    
    @Override
    public Slot addSlotToContainer(Slot slotIn){
        return super.addSlotToContainer(slotIn);
    }
    
    @Override
    public void clearContainer(EntityPlayer playerIn, World worldIn, IInventory inventoryIn){
        super.clearContainer(playerIn, worldIn, inventoryIn);
    }
    
    @Override
    public boolean mergeItemStack(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection){
        return super.mergeItemStack(stack, startIndex, endIndex, reverseDirection);
    }
    
    @Override
    public void resetDrag(){
        super.resetDrag();
    }
    
    @Override
    public void slotChangedCraftingGrid(World p_192389_1_, EntityPlayer p_192389_2_, InventoryCrafting p_192389_3_, InventoryCraftResult p_192389_4_){
        super.slotChangedCraftingGrid(p_192389_1_, p_192389_2_, p_192389_3_, p_192389_4_);
    }
}
