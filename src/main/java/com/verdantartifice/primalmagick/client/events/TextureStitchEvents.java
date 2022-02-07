package com.verdantartifice.primalmagick.client.events;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.client.renderers.itemstack.HallowsteelShieldISTER;
import com.verdantartifice.primalmagick.client.renderers.itemstack.HexiumShieldISTER;
import com.verdantartifice.primalmagick.client.renderers.itemstack.PrimaliteShieldISTER;
import com.verdantartifice.primalmagick.client.renderers.tile.ManaFontTER;
import com.verdantartifice.primalmagick.client.renderers.tile.RitualBellTER;
import com.verdantartifice.primalmagick.client.renderers.tile.SpellcraftingAltarTER;
import com.verdantartifice.primalmagick.common.containers.slots.BottleSlot;
import com.verdantartifice.primalmagick.common.containers.slots.HoneycombSlot;
import com.verdantartifice.primalmagick.common.containers.slots.LapisLazuliSlot;
import com.verdantartifice.primalmagick.common.containers.slots.PaperSlot;
import com.verdantartifice.primalmagick.common.containers.slots.RuneSlot;
import com.verdantartifice.primalmagick.common.containers.slots.StoneSlabSlot;
import com.verdantartifice.primalmagick.common.containers.slots.WandCapSlot;
import com.verdantartifice.primalmagick.common.containers.slots.WandCoreSlot;
import com.verdantartifice.primalmagick.common.containers.slots.WandGemSlot;
import com.verdantartifice.primalmagick.common.containers.slots.WandSlot;
import com.verdantartifice.primalmagick.common.containers.slots.WritingImplementSlot;
import com.verdantartifice.primalmagick.common.sources.Source;

import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Respond to client-only texture stitching events.
 * 
 * @author Daedalus4096
 */
@Mod.EventBusSubscriber(modid=PrimalMagick.MODID, value=Dist.CLIENT, bus=Mod.EventBusSubscriber.Bus.MOD)
public class TextureStitchEvents {
    @SuppressWarnings("deprecation")
    @SubscribeEvent
    public static void onPreTextureStitch(TextureStitchEvent.Pre event) {
        if (TextureAtlas.LOCATION_BLOCKS.equals(event.getAtlas().location())) {
            // Add empty-slot background images to the block atlas texture
            event.addSprite(WandCoreSlot.TEXTURE);
            event.addSprite(WandCapSlot.TEXTURE);
            event.addSprite(WandGemSlot.TEXTURE);
            event.addSprite(WandSlot.TEXTURE);
            event.addSprite(PaperSlot.TEXTURE);
            event.addSprite(WritingImplementSlot.TEXTURE);
            event.addSprite(RuneSlot.TEXTURE);
            event.addSprite(StoneSlabSlot.TEXTURE);
            event.addSprite(LapisLazuliSlot.TEXTURE);
            event.addSprite(HoneycombSlot.TEXTURE);
            event.addSprite(BottleSlot.TEXTURE);

            // Add other sprites to the block atlas texture
            event.addSprite(ManaFontTER.TEXTURE);
            event.addSprite(RitualBellTER.TEXTURE);
            event.addSprite(SpellcraftingAltarTER.RING_TEXTURE);
            event.addSprite(PrimaliteShieldISTER.TEXTURE_SHIELD_BASE);
            event.addSprite(PrimaliteShieldISTER.TEXTURE_SHIELD_NO_PATTERN);
            event.addSprite(HexiumShieldISTER.TEXTURE_SHIELD_BASE);
            event.addSprite(HexiumShieldISTER.TEXTURE_SHIELD_NO_PATTERN);
            event.addSprite(HallowsteelShieldISTER.TEXTURE_SHIELD_BASE);
            event.addSprite(HallowsteelShieldISTER.TEXTURE_SHIELD_NO_PATTERN);
            event.addSprite(Source.getUnknownAtlasLocation());
            for (Source source : Source.SORTED_SOURCES) {
                event.addSprite(source.getAtlasLocation());
            }
        }
    }
}
