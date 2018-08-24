package de.canitzp.dynamicmachines.machine.energizedfurnace;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.tileentity.TileEntity;

public class ContainerEnergizedFurnace extends Container{
    
    public ContainerEnergizedFurnace(EntityPlayer player, TileEntity tile){
        for (int i = 0; i < 3; ++i){
            for (int j = 0; j < 9; ++j){
                this.addSlotToContainer(new Slot(player.inventory, j + i * 9 + 9, 8 + j * 18, 77 + i * 18));
            }
        }
        for (int k = 0; k < 9; ++k){
            this.addSlotToContainer(new Slot(player.inventory, k, 8 + k * 18, 135));
        }
    }
    
    @Override
    public boolean canInteractWith(EntityPlayer playerIn){
        return true;
    }
}
