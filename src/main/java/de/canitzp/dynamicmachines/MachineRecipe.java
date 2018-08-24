package de.canitzp.dynamicmachines;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class MachineRecipe extends ShapelessOreRecipe{
    
    public static int id = 0;
    
    public MachineRecipe(ResourceLocation group, Block result, Object... recipe){
        super(null, result, recipe);
        this.setRegistryName(DynamicMachines.MODID, "machine_recipe_" + id++);
    }
}
