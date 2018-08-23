package de.canitzp.dynamicmachines;

import de.canitzp.dynamicmachines.api.IMachine;
import de.canitzp.dynamicmachines.api.Machine;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.Sys;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

@Mod.EventBusSubscriber
@Mod(modid = DynamicMachines.MODID, name = DynamicMachines.MODNAME, version = DynamicMachines.MODVERSION)
public class DynamicMachines{
    
    public static final String MODID = "dynamicmachines";
    public static final String MODNAME = "Dynamic Machines";
    public static final String MODVERSION = "@VERSION@";
    public static DynamicMachines INSTANCE;
    
    public static final List<MachineData> machines = new ArrayList<>();
    
    public DynamicMachines(){
        INSTANCE = this;
    }
    
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event){
        for(ASMDataTable.ASMData asm : event.getAsmData().getAll(Machine.class.getName())){
            try{
                Class c = Class.forName(asm.getClassName(), false, Thread.currentThread().getContextClassLoader());
                if(IMachine.class.isAssignableFrom(c)){
                    IMachine machine = (IMachine) c.newInstance();
                    registerMachines(machine);
                }
            } catch(Exception e){
                e.printStackTrace();
            }
        }
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
    }
    
    @Mod.EventHandler
    public void init(FMLInitializationEvent event){
        ForgeRegistries.BLOCKS.getValuesCollection().stream()
                              .filter(entry -> entry instanceof IMachine)
                              .forEach(entry -> ((IMachine) entry).init(event.getSide()));
        GameRegistry.registerTileEntity(TileDummy.class, new ResourceLocation(MODID, "dummy"));
    }
    
    public static void registerMachines(IMachine... machines){
        for(IMachine machine : machines){
            Block block = machine.getBlock();
            Item item = machine.getItemBlock();
            if(item == null){
                item = new ItemBlock(block).setRegistryName(block.getRegistryName());
            }
            ForgeRegistries.BLOCKS.register(block);
            ForgeRegistries.ITEMS.register(item);
        }
    }
    
    public static NBTTagCompound addMachine(World world, BlockPos pos){
        NBTTagCompound attributes = new NBTTagCompound();
        machines.add(new MachineData(world, pos, attributes));
        return attributes;
    }
    
    @SubscribeEvent
    public static void worldTick(TickEvent.WorldTickEvent event){
        World world = event.world;
        if(!world.isRemote && event.phase == TickEvent.Phase.END){
            //WeakHashMap<Pair<IMachine, MachineData>, Long> tickTimes = new WeakHashMap<>();
            for(MachineData data : machines){
                if(data.getWorld() == world){
                    IBlockState state = world.getBlockState(data.getPos());
                    if(state.getBlock() instanceof IMachine){
                        IMachine machine = (IMachine) state.getBlock();
                        if(machine.canTick(world, data.getPos(), data.getAttributes())){
                            //long before = System.nanoTime();
                            machine.tick(world, data.getPos(), data.getAttributes());
                            //long after = System.nanoTime();
                            //tickTimes.put(Pair.of(machine, data), after - before);
                        }
                    }
                }
            }
            /*tickTimes.entrySet().stream()
                     .filter(entry -> entry.getValue() >= 500000L)
                     .forEach(entry -> System.out.println("A machine needed more than half a milli second (" + entry.getValue() + ") to process! (Probably a System.out.println(), cause they need a lot of time)" + entry.getKey().toString()));
        */}
    }
    
    @SubscribeEvent
    public static void onWorldLoad(WorldEvent.Load event){
        if(!event.getWorld().isRemote){
            machines.clear();
            World world = event.getWorld();
            String saveFolder = world.provider.getSaveFolder();
            File saveFile = new File(new File(world.getSaveHandler().getWorldDirectory(), saveFolder != null ? saveFolder : ""), "dynamicmachines.dat");
            if(saveFile.exists()){
                try{
                    NBTTagCompound dummy = CompressedStreamTools.read(saveFile);
                    if(dummy != null){
                        NBTTagList mainNbt = dummy.getTagList("Machines", Constants.NBT.TAG_COMPOUND);
                        for(NBTBase aMainNbt : mainNbt){
                            NBTTagCompound nbt = (NBTTagCompound) aMainNbt;
                            int dimId = nbt.getInteger("Dimension");
                            BlockPos pos = BlockPos.fromLong(nbt.getLong("Position"));
                            NBTTagCompound attributes = nbt.getCompoundTag("Attributes");
                            if(world.provider.getDimension() == dimId){
                                IBlockState state = world.getBlockState(pos);
                                if(state.getBlock() instanceof IMachine){
                                    machines.add(new MachineData(world, pos, attributes));
                                }
                            }
                        }
                    }
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }
    }
    
    @SubscribeEvent
    public static void onWorldSave(WorldEvent.Save event){
        World world = event.getWorld();
        if(!world.isRemote){
            String saveFolder = world.provider.getSaveFolder();
            File saveFile = new File(new File(world.getSaveHandler().getWorldDirectory(), saveFolder != null ? saveFolder : ""), "dynamicmachines.dat");
            NBTTagList mainNbt = new NBTTagList();
            for(MachineData data : machines){
                if(data.getWorld() == world){
                    IBlockState state = world.getBlockState(data.getPos());
                    if(state.getBlock() instanceof IMachine){ // check this so we only save the blocks that are actually in the world
                        NBTTagCompound nbt = new NBTTagCompound();
                        nbt.setInteger("Dimension", world.provider.getDimension());
                        nbt.setLong("Position", data.getPos().toLong());
                        nbt.setTag("Attributes", data.getAttributes());
                        mainNbt.appendTag(nbt);
                    }
                }
            }
            NBTTagCompound dummy = new NBTTagCompound();
            dummy.setTag("Machines", mainNbt);
            try{
                if(saveFile.exists()){
                    saveFile.delete();
                }
                saveFile.getParentFile().mkdirs();
                saveFile.createNewFile();
                CompressedStreamTools.write(dummy, saveFile);
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
    
}
