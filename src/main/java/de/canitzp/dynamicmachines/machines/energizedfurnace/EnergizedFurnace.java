package de.canitzp.dynamicmachines.machines.energizedfurnace;

import de.canitzp.dynamicmachines.DynamicMachines;
import de.canitzp.dynamicmachines.MachineContainer;
import de.canitzp.dynamicmachines.MachineGui;
import de.canitzp.dynamicmachines.api.BlockMachine;
import de.canitzp.dynamicmachines.api.IMachineGui;
import de.canitzp.dynamicmachines.api.Machine;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.Random;

@Machine
public class EnergizedFurnace extends BlockMachine{
    
    public EnergizedFurnace(){
        super(Material.IRON);
        this.setRegistryName(DynamicMachines.MODID, "energized_furnace");
        this.setTranslationKey(this.getRegistryName().toString());
    }
    
    @Override
    public void init(Side side){
        if(side.isClient()){
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(this.getRegistryName(), "inventory"));
        }
    }
    
    @Override
    public <T> T getCapability(World world, BlockPos pos, NBTTagCompound attributes, Capability<T> cap, @Nullable EnumFacing side){
        EnergyStorage energy = this.readVar("energy");
        if(energy != null){

        }
        return null;
    }
    
    @Override
    public void tick(World world, BlockPos pos, NBTTagCompound attributes){
    
    }
    
    @Nullable
    @Override
    public IMachineGui getGui(){
        return new IMachineGui(){
            @Override
            public ResourceLocation getLocation(MachineGui gui){
                return new ResourceLocation(DynamicMachines.MODID, "textures/gui/energized_furnace.png");
            }
    
            @Override
            public Pair<Integer, Integer> getSize(MachineGui gui){
                return Pair.of(176, 159);
            }
    
            @Override
            public void initContainer(MachineContainer container){
                for (int i = 0; i < 3; ++i){
                    for (int j = 0; j < 9; ++j){
                        container.addSlotToContainer(new Slot(container.getPlayer().inventory, j + i * 9 + 9, 8 + j * 18, 77 + i * 18));
                    }
                }
                for (int k = 0; k < 9; ++k){
                    container.addSlotToContainer(new Slot(container.getPlayer().inventory, k, 8 + k * 18, 135));
                }
            }
        };
    }
}
