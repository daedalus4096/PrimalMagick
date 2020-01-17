package com.verdantartifice.primalmagic.client.events;

import com.verdantartifice.primalmagic.PrimalMagic;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Respond to client-only texture stitching events.
 * 
 * @author Michael Bunting
 */
@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid=PrimalMagic.MODID, value=Dist.CLIENT, bus=Mod.EventBusSubscriber.Bus.MOD)
public class TextureStitchEvents {
    @SubscribeEvent
    public static void onPreTextureStitch(TextureStitchEvent.Pre event) {
        // Add empty-slot background images to the base atlas texture
        if ("textures".equals(event.getMap().getBasePath())) {
            event.addSprite(new ResourceLocation(PrimalMagic.MODID, "item/empty_wand_core_slot"));
            event.addSprite(new ResourceLocation(PrimalMagic.MODID, "item/empty_wand_cap_slot"));
            event.addSprite(new ResourceLocation(PrimalMagic.MODID, "item/empty_wand_gem_slot"));
            event.addSprite(new ResourceLocation(PrimalMagic.MODID, "item/empty_wand_slot"));
        }
    }
}
