package ladysnake.bansheenight.compat;

import bl4ckscor3.mod.scarecrows.entity.EntityScarecrow;
import com.github.upcraftlp.glasspane.api.capability.CapabilityProviderSimple;
import gigaherz.eyes.entity.EntityEyes;
import ladysnake.bansheenight.BansheeNight;
import ladysnake.bansheenight.capability.CapabilityBansheeNightSpawnable;
import lykrast.harvestersnight.common.EntityHarvester;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CompatMobSpawn {

    private static boolean HARVESTERS_NIGHT;
    private static boolean EYES_IN_THE_DARK;
    private static boolean SCARECROWS;

    private static final ResourceLocation SPAWN_CAPABILITY = new ResourceLocation(BansheeNight.MOD_ID, "spawn");

    public static void init() {
        HARVESTERS_NIGHT = Loader.isModLoaded("harvestersnight");
        EYES_IN_THE_DARK = Loader.isModLoaded("eyesinthedarkness");
        SCARECROWS = Loader.isModLoaded("scarecrows");
    }

    @SubscribeEvent
    public static void onAttachCapabiltity(AttachCapabilitiesEvent<Entity> event) {
        boolean flag = false;
        if(HARVESTERS_NIGHT) {
            if(event.getObject() instanceof EntityHarvester) flag = true;
        }
        if(EYES_IN_THE_DARK) {
            if(event.getObject() instanceof EntityEyes) flag = true;
        }
        if(SCARECROWS) {
            if(event.getObject() instanceof EntityScarecrow) flag = true;
        }

        if(flag) event.addCapability(SPAWN_CAPABILITY, new CapabilityProviderSimple<>(CapabilityBansheeNightSpawnable.CAPABILITY_BANSHEE_NIGHT_SPAWN));
    }
}
