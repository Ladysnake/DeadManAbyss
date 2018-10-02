package ladysnake.bansheenight.client;

import ladylib.compat.EnhancedBusSubscriber;
import ladysnake.bansheenight.BansheeNight;
import ladysnake.bansheenight.api.event.BansheeNightHandler;
import ladysnake.bansheenight.capability.CapabilityBansheeNight;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.resource.IResourceType;
import net.minecraftforge.client.resource.ISelectiveResourceReloadListener;
import net.minecraftforge.client.resource.VanillaResourceType;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.function.Predicate;

@EnhancedBusSubscriber(side = Side.CLIENT)
public class BansheeNightShaderManager implements ISelectiveResourceReloadListener {
    public static final ResourceLocation NIGHT_SHADER = new ResourceLocation(BansheeNight.MOD_ID, "shaders/post/darkness.json");
    private ShaderGroup shader;
    private int oldDisplayWidth, oldDisplayHeight;

    /**
     * Applies the darkness shader after the world has been fully rendered
     */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        BansheeNightHandler cap = Minecraft.getMinecraft().world.getCapability(CapabilityBansheeNight.CAPABILITY_BANSHEE_NIGHT, null);
        if (cap != null && cap.isBansheeNightOccurring()) {
            if (this.shader != null) {
                Minecraft mc = Minecraft.getMinecraft();
                if (mc.displayWidth != oldDisplayWidth || mc.displayHeight != oldDisplayHeight) {
                    this.shader.createBindFramebuffers(mc.displayWidth, mc.displayHeight);
                    oldDisplayWidth = mc.displayWidth;
                    oldDisplayHeight = mc.displayHeight;
                }
                GlStateManager.matrixMode(GL11.GL_TEXTURE);
                GlStateManager.loadIdentity();
                shader.render(event.getPartialTicks());
                Minecraft.getMinecraft().getFramebuffer().bindFramebuffer(true);
                GlStateManager.enableDepth();
            } else if (OpenGlHelper.areShadersSupported()){
                ((IReloadableResourceManager)Minecraft.getMinecraft().getResourceManager()).registerReloadListener(this);
            }
        }
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager, Predicate<IResourceType> resourcePredicate) {
        if (resourcePredicate.test(VanillaResourceType.SHADERS)) {
            Minecraft mc = Minecraft.getMinecraft();
            try {
                this.shader = new ShaderGroup(mc.renderEngine, resourceManager, mc.getFramebuffer(), NIGHT_SHADER);
                this.oldDisplayWidth = -1; // reset framebuffers next tick
            } catch (IOException e) {
                BansheeNight.LOGGER.error("Could not load darkness shader", e);
            }
        }
    }
}
