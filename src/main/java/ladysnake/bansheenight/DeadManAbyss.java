package ladysnake.bansheenight;

import ladylib.LadyLib;
import ladysnake.bansheenight.command.CommandDma;
import ladysnake.bansheenight.compat.CompatMobSpawn;
import ladysnake.bansheenight.init.ModEntities;
import ladysnake.bansheenight.init.ModItems;
import ladysnake.bansheenight.network.PacketHandler;
import ladysnake.bansheenight.tileentity.TileEntityBlockHolder;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(
        modid = DeadManAbyss.MOD_ID,
        name = DeadManAbyss.MOD_NAME,
        version = DeadManAbyss.VERSION,
        certificateFingerprint = "@FINGERPRINTKEY@",
        dependencies = "required-after:forge;" +
                       "required-after:ladylib;" +
                       "after:jei"
)
public class DeadManAbyss {

    public static final String MOD_ID = "dma";
    public static final String MOD_NAME = "Dead Man's Abyss";
    public static final String VERSION = "@VERSION@";

    public static final DeadManAbyss INSTANCE = new DeadManAbyss();
    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);

    @Mod.InstanceFactory
    public static DeadManAbyss instance() {
        return INSTANCE;
    }

    /**
     * This is the first initialization event. Register tile entities here.
     * The registry events below will have fired prior to entry to this method.
     */
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LadyLib.INSTANCE.getContainer(MOD_ID).setCreativeTab(new CreativeTabs(MOD_ID) {
            @Override
            public ItemStack createIcon() {
                return new ItemStack(ModItems.ICHOR_SAC);
            }
        });
        if(event.getSide().isClient()) {
            ModEntities.registerRenders();
        }
    }

    /**
     * This is the second initialization event. Register custom recipes
     */
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        PacketHandler.initPackets();
        GameRegistry.registerTileEntity(TileEntityBlockHolder.class, new ResourceLocation(DeadManAbyss.MOD_ID, "block_holder"));
    }


    /**
     * This is the final initialization event. Register actions from other mods here
     */
    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        CompatMobSpawn.init();
    }

    @Mod.EventHandler
    public void serverStart(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandDma());
    }

}
