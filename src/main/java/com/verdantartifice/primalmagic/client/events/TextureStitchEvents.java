package com.verdantartifice.primalmagic.client.events;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.client.renderers.itemstack.HallowsteelShieldISTER;
import com.verdantartifice.primalmagic.client.renderers.itemstack.HexiumShieldISTER;
import com.verdantartifice.primalmagic.client.renderers.itemstack.PrimaliteShieldISTER;
import com.verdantartifice.primalmagic.client.renderers.tile.AncientManaFontTER;
import com.verdantartifice.primalmagic.client.renderers.tile.RitualBellTER;
import com.verdantartifice.primalmagic.common.containers.slots.BottleSlot;
import com.verdantartifice.primalmagic.common.containers.slots.HoneycombSlot;
import com.verdantartifice.primalmagic.common.containers.slots.LapisLazuliSlot;
import com.verdantartifice.primalmagic.common.containers.slots.PaperSlot;
import com.verdantartifice.primalmagic.common.containers.slots.RuneSlot;
import com.verdantartifice.primalmagic.common.containers.slots.StoneSlabSlot;
import com.verdantartifice.primalmagic.common.containers.slots.WandCapSlot;
import com.verdantartifice.primalmagic.common.containers.slots.WandCoreSlot;
import com.verdantartifice.primalmagic.common.containers.slots.WandGemSlot;
import com.verdantartifice.primalmagic.common.containers.slots.WandSlot;
import com.verdantartifice.primalmagic.common.containers.slots.WritingImplementSlot;
import com.verdantartifice.primalmagic.common.sources.Source;

import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Respond to client-only texture stitching events.
 * 
 * @author Daedalus4096
 */
@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid=PrimalMagic.MODID, value=Dist.CLIENT, bus=Mod.EventBusSubscriber.Bus.MOD)
public class TextureStitchEvents {
    @SuppressWarnings("deprecation")
    @SubscribeEvent
    public static void onPreTextureStitch(TextureStitchEvent.Pre event) {
        if (AtlasTexture.LOCATION_BLOCKS_TEXTURE.equals(event.getMap().getTextureLocation())) {
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
            event.addSprite(AncientManaFontTER.TEXTURE);
            event.addSprite(RitualBellTER.TEXTURE);
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
