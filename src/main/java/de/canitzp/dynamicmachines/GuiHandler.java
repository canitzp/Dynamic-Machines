package de.canitzp.dynamicmachines;

import de.canitzp.dynamicmachines.api.IMachine;
import de.canitzp.dynamicmachines.api.IMachineGui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

public class GuiHandler implements IGuiHandler{
    
    public static final Map<IMachineGui, Integer> GUI_IDS = new WeakHashMap<>();
    
    public static int getIdFor(IMachineGui gui){
        int id = GUI_IDS.size();
        GUI_IDS.put(gui, id);
        return id;
    }
    
    public static IMachineGui getForId(int id){
        for(Map.Entry<IMachineGui, Integer> entry : GUI_IDS.entrySet()){
            if(entry.getValue() == id){
                return entry.getKey();
            }
        }
        return null;
    }
    
    public static void closeGui(IMachineGui gui){
        GUI_IDS.remove(gui);
    }
    
    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z){
        return new MachineContainer(player, getForId(ID));
    }
    
    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z){
        return new MachineGui(player, getForId(ID));
    }
}
