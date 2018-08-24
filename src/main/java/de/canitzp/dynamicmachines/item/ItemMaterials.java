package de.canitzp.dynamicmachines.item;

import de.canitzp.dynamicmachines.DynamicMachines;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;

public class ItemMaterials extends Item{
    
    public ItemMaterials(){
        this.setRegistryName(DynamicMachines.MODID, "materials");
        this.setCreativeTab(CreativeTabs.MISC);
    }
    
    @Override
    public String getTranslationKey(ItemStack stack){
        if(!stack.isEmpty() && stack.hasTagCompound()){
            int id = stack.getTagCompound().getInteger("EnumId");
            return "item." + DynamicMachines.MODID + ":material_" + Material.values()[id].name().toLowerCase(Locale.ENGLISH);
        }
        return super.getTranslationKey(stack);
    }
    
    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items){
        if(this.isInCreativeTab(tab)){
            for(Material material : Material.values()){
                NBTTagCompound nbt = new NBTTagCompound();
                nbt.setTag("Material", material.propertiesToNbt());
                nbt.setInteger("EnumId", material.ordinal());
                ItemStack stack = new ItemStack(this);
                stack.setTagCompound(nbt);
                items.add(stack);
            }
        }
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flagIn){
        if(stack.hasTagCompound() && stack.getTagCompound().hasKey("Material", Constants.NBT.TAG_COMPOUND)){
            NBTTagCompound material = stack.getTagCompound().getCompoundTag("Material");
            if(material.hasKey("Tier", Constants.NBT.TAG_INT)){
                Material.Tier tier = Material.Tier.values()[material.getInteger("Tier")];
                tooltip.add(String.format("%s%s %s", "Tier:", TextFormatting.GOLD, tier.name().toLowerCase(Locale.ENGLISH)));
            }
            if(material.hasKey("Speed", Constants.NBT.TAG_FLOAT)){
                int percent = Math.round(material.getFloat("Speed") * 100);
                tooltip.add(String.format("%s%s %s%%", "Speed:", TextFormatting.GREEN, percent));
            }
            if(material.hasKey("EnergyCap", Constants.NBT.TAG_INT)){
                int cap = material.getInteger("EnergyCap");
                tooltip.add(String.format("%s%s %sCF", "Energy capacity:", TextFormatting.RED, cap));
            }
            if(material.hasKey("EnergyUsage", Constants.NBT.TAG_FLOAT)){
                int percent = Math.round(material.getFloat("EnergyUsage") * 100);
                tooltip.add(String.format("%s%s %s%%", "Energy usage:", TextFormatting.RED, percent));
            }
        }
    }
    
    public enum Material{
        CASE_WOOD("type: case; tier: 0"),
        BAT_WOOD("type: bat; tier: 0; energy_cap: 10000"),
        HEATER_WATER("type: heater; tier: 0; speed: 1.0; energy: 5.0");
        
        String properties;
    
        Material(String properties){
            this.properties = properties;
        }
    
        public String getProperties(){
            return properties;
        }
        
        public NBTTagCompound propertiesToNbt(){
            NBTTagCompound nbt = new NBTTagCompound();
            for(String prop : this.getProperties().split(";")){
                prop = prop.replace(" ", "");
                String[] split = prop.split(":");
                if(split.length == 2){
                    String key = split[0];
                    String val = split[1];
                    switch(key){
                        case "tier": {
                            nbt.setInteger("Tier", Integer.parseInt(val));
                            break;
                        }
                        case "speed": {
                            nbt.setFloat("Speed", Float.parseFloat(val));
                            break;
                        }
                        case "energy_cap": {
                            nbt.setInteger("EnergyCap", Integer.parseInt(val));
                            break;
                        }
                        case "energy": {
                            nbt.setFloat("EnergyUsage", Float.parseFloat(val));
                            break;
                        }
                    }
                }
            }
            return nbt;
        }
        
        public enum Tier {
            WOOD,
            STONE,
            IRON,
            REDSTONE,
            DIAMOND,
            EMERALD,
            CHEAT
        }
    }
}
