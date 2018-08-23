package de.canitzp.dynamicmachines;

import de.canitzp.dynamicmachines.api.IMachine;
import de.canitzp.dynamicmachines.api.IMachineGui;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;

public class MachineGui extends GuiContainer{
    
    private final ResourceLocation res;
    private IMachineGui machineGui;
    private EntityPlayer player;
    
    public MachineGui(EntityPlayer player, IMachineGui machineGui){
        super(new MachineContainer(player, machineGui));
        this.machineGui = machineGui;
        this.player = player;
        this.res = this.machineGui.getLocation(this);
    }
    
    public EntityPlayer getPlayer(){
        return player;
    }
    
    @Override
    public void initGui(){
        super.initGui();
        Pair<Integer, Integer> size = this.machineGui.getSize(this);
        this.xSize = size.getKey();
        this.ySize = size.getValue();
        this.machineGui.initGui(this);
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY){
        this.drawDefaultBackground();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(this.res);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        this.machineGui.renderBackground(this, mouseX, mouseY, partialTicks);
    }
    
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks){
        this.machineGui.renderForegroundPre(this, mouseX, mouseY, partialTicks);
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.machineGui.renderForegroundPost(this, mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }
    
    @Override
    public void onGuiClosed(){
        super.onGuiClosed();
        GuiHandler.closeGui(this.machineGui);
    }
}
