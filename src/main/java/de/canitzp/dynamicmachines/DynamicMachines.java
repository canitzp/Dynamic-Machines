package de.canitzp.dynamicmachines;

import de.canitzp.dynamicmachines.item.ItemMaterials;
import de.canitzp.dynamicmachines.machine.energizedfurnace.BlockEnergizedFurnace;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Mod.EventBusSubscriber
@Mod(modid = DynamicMachines.MODID, name = DynamicMachines.MODNAME, version = DynamicMachines.MODVERSION)
public class DynamicMachines{
    
    public static final String MODID = "dynamicmachines";
    public static final String MODNAME = "Dynamic Machines";
    public static final String MODVERSION = "@VERSION@";
    
    public static DynamicMachines INSTANCE;
    
    public static final Block BLOCK_ENERGIZED_FURNACE = new BlockEnergizedFurnace();
    
    public static final Item ITEM_MATERIALS = new ItemMaterials();
    
    public DynamicMachines(){
        INSTANCE = this;
    }
    
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event){
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
    }
    
    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event){
        event.getRegistry().register(BLOCK_ENERGIZED_FURNACE);
    }
    
    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event){
        event.getRegistry().register(new ItemBlock(BLOCK_ENERGIZED_FURNACE).setRegistryName(BLOCK_ENERGIZED_FURNACE.getRegistryName()));
        event.getRegistry().register(ITEM_MATERIALS);
    }
    
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event){
        Map<ItemMaterials.Material, ResourceLocation> loc = new HashMap<>();
        for(ItemMaterials.Material material : ItemMaterials.Material.values()){
            loc.put(material, new ResourceLocation(DynamicMachines.MODID, "material_" + material.name().toLowerCase(Locale.ENGLISH)));
        }
        ModelLoader.registerItemVariants(ITEM_MATERIALS, loc.values().toArray(new ResourceLocation[0]));
        ModelLoader.setCustomMeshDefinition(ITEM_MATERIALS, stack -> {
            if(stack.hasTagCompound()){
                ItemMaterials.Material material = ItemMaterials.Material.values()[stack.getTagCompound().getInteger("EnumId")];
                return new ModelResourceLocation(loc.get(material), "inventory");
            }
            return null;
        });
    }
    
}
