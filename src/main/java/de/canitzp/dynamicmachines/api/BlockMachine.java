package de.canitzp.dynamicmachines.api;

import de.canitzp.dynamicmachines.DynamicMachines;
import de.canitzp.dynamicmachines.GuiHandler;
import de.canitzp.dynamicmachines.TileDummy;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public abstract class BlockMachine extends Block implements IMachine, ITileEntityProvider{
    
    private static final Map<IMachine, Map<String, Object>> SPECIAL_ATTRIBUTES = new HashMap<>();
    
    public BlockMachine(Material material, MapColor mapColor){
        super(material, mapColor);
        this.hasTileEntity = true;
    }
    
    public BlockMachine(Material material){
        super(material);
        this.hasTileEntity = true;
    }
    
    protected void saveVar(String key, Object value){
        Map<String, Object> lowerMap = SPECIAL_ATTRIBUTES.getOrDefault(this, new HashMap<>());
        lowerMap.put(key, value);
        SPECIAL_ATTRIBUTES.put(this, lowerMap);
    }
    
    protected <T> T readVar(String key){
        Map<String, Object> lowerMap = SPECIAL_ATTRIBUTES.getOrDefault(this, new HashMap<>());
        Object o = lowerMap.get(key);
        return o != null ? (T) o : null;
    }
    
    @Nonnull
    @Override
    public Block getBlock(){
        return this;
    }
    
    @Nullable
    @Override
    public Item getItemBlock(){
        return null;
    }
    
    @Override
    public boolean canTick(World world, BlockPos pos, NBTTagCompound attributes){
        return true;
    }
    
    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack){
        if(!world.isRemote){
            NBTTagCompound attributes = DynamicMachines.addMachine(world, pos);
            this.onPlaced(world, pos, attributes, state, placer, stack);
        }
    }
    
    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state){
        if(!world.isRemote){
            //DynamicMachines.addMachine(world, pos);
        }
    }
    
    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ){
        IMachineGui gui = this.getGui();
        if(gui != null){
            int id = GuiHandler.getIdFor(gui); // getIdFor also puts the gui onto the collector map
            if(!world.isRemote){
                player.openGui(DynamicMachines.INSTANCE, id, world, pos.getX(), pos.getY(), pos.getZ());
            }
            return true;
        }
        return false;
    }
    
    @Override
    public boolean hasTileEntity(IBlockState state){
        return super.hasTileEntity(state);
    }
    
    @Nullable
    @Override
    public TileEntity createNewTileEntity(World world, int meta){
        return new TileDummy();
    }
}
