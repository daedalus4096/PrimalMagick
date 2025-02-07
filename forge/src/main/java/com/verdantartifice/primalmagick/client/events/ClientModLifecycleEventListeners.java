package com.verdantartifice.primalmagick.client.events;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.client.gui.hud.WandHudOverlay;
import com.verdantartifice.primalmagick.client.gui.hud.WardingHudOverlay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * Forge listeners for client-only mod lifecycle events for setup.
 * 
 * @author Daedalus4096
 */
@Mod.EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientModLifecycleEventListeners {
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        ClientModLifecycleEvents.clientSetup(event::enqueueWork);
        registerHudOverlays();  // FIXME Move this to a dedicated event if/when Forge implements one
    }

    private static void registerHudOverlays() {
        Minecraft mc = Minecraft.getInstance();

        // Register the wand HUD overlay
        // FIXME Register above hotbar layer instead of at the top
        mc.gui.layers.add(WandHudOverlay::render);

        // Register the ward health bar overlay
        // FIXME Register above player health layer instead of at the top
        mc.gui.layers.add(WardingHudOverlay::render);
    }
}
