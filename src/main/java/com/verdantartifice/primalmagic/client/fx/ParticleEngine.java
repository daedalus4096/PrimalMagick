package com.verdantartifice.primalmagic.client.fx;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.platform.GlStateManager;
import com.verdantartifice.primalmagic.PrimalMagic;

import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
@OnlyIn(Dist.CLIENT)
public class ParticleEngine {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(PrimalMagic.MODID, "textures/misc/particles.png");
    protected static final List<Map<Integer, List<Particle>>> LAYERS = Arrays.asList(new HashMap<>(), new HashMap<>(), new HashMap<>(), new HashMap<>(), new HashMap<>(), new HashMap<>());
    
    @SubscribeEvent
    public static void renderTick(TickEvent.RenderTickEvent event) {
        if (Minecraft.getInstance().world == null || event.phase != TickEvent.Phase.END) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        float partialTicks = event.renderTickTime;
        ActiveRenderInfo ari = new ActiveRenderInfo();
        ari.update(mc.world, (mc.getRenderViewEntity() == null ? mc.player : mc.getRenderViewEntity()), (mc.gameSettings.thirdPersonView > 0), (mc.gameSettings.thirdPersonView == 2), partialTicks);
        TextureManager renderer = mc.getTextureManager();
        int dim = mc.world.getDimension().getType().getId();
        
        GlStateManager.pushMatrix();
        MainWindow mw = mc.mainWindow;
        GL11.glClear(256);
        GL11.glMatrixMode(5889);
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0D, mw.getScaledWidth(), mw.getScaledHeight(), 0.0D, 1000.0D, 3000.0D);
        GL11.glMatrixMode(5888);
        GL11.glLoadIdentity();
        GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableBlend();
        GL11.glEnable(3042);
        GL11.glAlphaFunc(516, 0.003921569F);
        renderer.bindTexture(TEXTURE);
        GlStateManager.depthMask(false);
        
        for (int layer = 5; layer >= 4; layer--) {
            if (LAYERS.get(layer).containsKey(Integer.valueOf(dim))) {
                List<Particle> particles = LAYERS.get(layer).get(Integer.valueOf(dim));
                if (!particles.isEmpty()) {
                    if (layer == 4) {
                        GlStateManager.blendFunc(770, 1);
                    } else if (layer == 5) {
                        GlStateManager.blendFunc(770, 771);
                    }
                    Tessellator tess = Tessellator.getInstance();
                    BufferBuilder bb = tess.getBuffer();
                    for (Particle p : particles) {
                        if (p != null) {
                            try {
                                p.renderParticle(bb, ari, partialTicks, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
                            } catch (Throwable t) {
                                CrashReport report = CrashReport.makeCrashReport(t, "Rendering particle");
                                CrashReportCategory category = report.makeCategory("Particle being rendered");
                                category.addDetail("Particle", () -> { return p.toString(); });
                                category.addDetail("Particle Type", () -> { return "ENTITY_PARTICLE_TEXTURE"; });
                                throw new ReportedException(report);
                            }
                        }
                    }
                }
            }
        }

        GlStateManager.depthMask(true);
        GlStateManager.blendFunc(770, 771);
        GlStateManager.disableBlend();
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.popMatrix();
    }
}
