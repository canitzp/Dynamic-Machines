package de.canitzp.dynamicmachines.machine.energizedfurnace;

import de.canitzp.dynamicmachines.DynamicMachines;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class GuiEnergizedFurnace extends GuiContainer{
    
    public static final ResourceLocation LOC = new ResourceLocation(DynamicMachines.MODID, "textures/gui/energized_furnace.png");
    
    public GuiEnergizedFurnace(EntityPlayer player, TileEntity tile){
        super(new ContainerEnergizedFurnace(player, tile));
    }
    
    @Override
    public void initGui(){
        super.initGui();
        this.xSize = 176;
        this.ySize = 159;
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY){
        this.drawDefaultBackground();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(LOC);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
    }
}
