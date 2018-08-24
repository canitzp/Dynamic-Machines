package de.canitzp.dynamicmachines.machine.energizedfurnace;

import com.google.common.collect.Lists;
import de.canitzp.dynamicmachines.DynamicMachines;
import de.canitzp.dynamicmachines.GuiHandler;
import de.canitzp.dynamicmachines.ICraftableMachine;
import de.canitzp.dynamicmachines.item.ItemMaterials;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nullable;
import java.util.List;

public class BlockEnergizedFurnace extends BlockContainer implements ICraftableMachine{
    
    public BlockEnergizedFurnace(){
        super(Material.IRON);
        this.setRegistryName(DynamicMachines.MODID, "energized_furnace");
        this.setTranslationKey(this.getRegistryName().toString());
    
        GameRegistry.registerTileEntity(TileEnergizedFurnace.class, this.getRegistryName());
    }
    
    @Override
    public EnumBlockRenderType getRenderType(IBlockState state){
        return EnumBlockRenderType.MODEL;
    }
    
    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ){
        if(!world.isRemote){
            player.openGui(DynamicMachines.INSTANCE, GuiHandler.ENERGIZED_FURNACE, world, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }
    
    @Nullable
    @Override
    public TileEntity createNewTileEntity(World world, int meta){
        return new TileEnergizedFurnace();
    }
    
    @Override
    public List<String> getMaterialTypes(){
        return Lists.newArrayList("case", "bat", "heater");
    }
}
