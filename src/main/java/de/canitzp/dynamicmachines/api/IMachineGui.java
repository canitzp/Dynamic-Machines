package de.canitzp.dynamicmachines.api;

import de.canitzp.dynamicmachines.MachineContainer;
import de.canitzp.dynamicmachines.MachineGui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;

public interface IMachineGui{
    
    ResourceLocation getLocation(MachineGui gui);
    
    Pair<Integer, Integer> getSize(MachineGui gui);
    
    default void initGui(MachineGui gui){}
    
    default void initContainer(MachineContainer container){}
    
    default boolean canInteractWith(MachineContainer container, EntityPlayer player){
        return true;
    }
    
    default void renderForegroundPre(MachineGui gui, int mouseX, int mouseY, float partialTicks){}
    
    default void renderForegroundPost(MachineGui gui, int mouseX, int mouseY, float partialTicks){}
    
    default void renderBackground(MachineGui gui, int mouseX, int mouseY, float partialTicks){}
}
