package de.canitzp.dynamicmachines;

import de.canitzp.dynamicmachines.machine.energizedfurnace.ContainerEnergizedFurnace;
import de.canitzp.dynamicmachines.machine.energizedfurnace.GuiEnergizedFurnace;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class GuiHandler implements IGuiHandler{
    
    public static final int ENERGIZED_FURNACE = 0;
    
    public static Map<Integer, Class<? extends GuiContainer>> GUIS = new HashMap<Integer, Class<? extends GuiContainer>>(){{
        put(ENERGIZED_FURNACE, GuiEnergizedFurnace.class);
    }};
    public static Map<Integer, Class<? extends Container>> CONTAINER = new HashMap<Integer, Class<? extends Container>>(){{
        put(ENERGIZED_FURNACE, ContainerEnergizedFurnace.class);
    }};
    
    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z){
        try{
            return CONTAINER.get(ID).getConstructor(EntityPlayer.class, TileEntity.class).newInstance(player, getTile(world, x, y, z));
        }catch(InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e){
            e.printStackTrace();
        }
        return null;
    }
    
    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z){
        try{
            return GUIS.get(ID).getConstructor(EntityPlayer.class, TileEntity.class).newInstance(player, getTile(world, x, y, z));
        }catch(InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e){
            e.printStackTrace();
        }
        return null;
    }
    
    private TileEntity getTile(World world, int x, int y, int z){
        return world.getTileEntity(new BlockPos(x, y, z));
    }
}
