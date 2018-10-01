package ladysnake.bansheenight.client;

import ladylib.compat.EnhancedBusSubscriber;
import ladylib.compat.StateEventReceiver;
import ladysnake.bansheenight.BansheeNight;
import ladysnake.bansheenight.capability.CapabilityBansheeNight;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.resource.IResourceType;
import net.minecraftforge.client.resource.ISelectiveResourceReloadListener;
import net.minecraftforge.client.resource.VanillaResourceType;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.function.Predicate;

@EnhancedBusSubscriber(side = Side.CLIENT)
public class BansheeNightShaderManager implements StateEventReceiver, ISelectiveResourceReloadListener {
    public static final ResourceLocation NIGHT_SHADER = new ResourceLocation(BansheeNight.MOD_ID, "darkness");
    private ShaderGroup shader;

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        ((IReloadableResourceManager)Minecraft.getMinecraft().getResourceManager()).registerReloadListener(this);
    }

    /**
     * Applies the darkness shader after the world has been fully rendered
     */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        if (this.shader != null) {
            CapabilityBansheeNight cap = Minecraft.getMinecraft().world.getCapability(CapabilityBansheeNight.CAPABILITY_BANSHEE_NIGHT, null);
            if (cap != null && cap.isBansheeNightOccurring()) {
                GlStateManager.matrixMode(GL11.GL_TEXTURE);
                GlStateManager.loadIdentity();
                shader.render(event.getPartialTicks());
                Minecraft.getMinecraft().getFramebuffer().bindFramebuffer(true);
            }
        }
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager, Predicate<IResourceType> resourcePredicate) {
        if (resourcePredicate.test(VanillaResourceType.SHADERS)) {
            try {
                this.shader = new ShaderGroup(Minecraft.getMinecraft().renderEngine, resourceManager, Minecraft.getMinecraft().getFramebuffer(), NIGHT_SHADER);
            } catch (IOException e) {
                BansheeNight.LOGGER.error("Could not load darkness shader", e);
            }
        }
    }
}
