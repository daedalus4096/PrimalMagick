package com.verdantartifice.primalmagick.datagen.atlas;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.client.renderers.itemstack.HallowsteelShieldISTER;
import com.verdantartifice.primalmagick.client.renderers.itemstack.HexiumShieldISTER;
import com.verdantartifice.primalmagick.client.renderers.itemstack.PrimaliteShieldISTER;
import com.verdantartifice.primalmagick.client.renderers.tile.ManaFontTER;
import com.verdantartifice.primalmagick.client.renderers.tile.RitualBellTER;
import com.verdantartifice.primalmagick.client.renderers.tile.SpellcraftingAltarTER;
import com.verdantartifice.primalmagick.common.containers.slots.BottleSlot;
import com.verdantartifice.primalmagick.common.containers.slots.HoneycombSlot;
import com.verdantartifice.primalmagick.common.containers.slots.PaperSlot;
import com.verdantartifice.primalmagick.common.containers.slots.RuneBaseSlot;
import com.verdantartifice.primalmagick.common.containers.slots.RuneEtchingSlot;
import com.verdantartifice.primalmagick.common.containers.slots.RuneSlot;
import com.verdantartifice.primalmagick.common.containers.slots.WandCapSlot;
import com.verdantartifice.primalmagick.common.containers.slots.WandCoreSlot;
import com.verdantartifice.primalmagick.common.containers.slots.WandGemSlot;
import com.verdantartifice.primalmagick.common.containers.slots.WandSlot;
import com.verdantartifice.primalmagick.common.containers.slots.WritingImplementSlot;
import com.verdantartifice.primalmagick.common.sources.Source;

import net.minecraft.client.renderer.texture.atlas.sources.SingleFile;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SpriteSourceProvider;

/**
 * Data provider for additions to the game's texture atlases.
 * 
 * @author Daedalus4096
 */
public class SpriteSourceProviderPM extends SpriteSourceProvider {
    private static final Logger LOGGER = LogUtils.getLogger();

    protected final Set<ResourceLocation> trackedSingles = new HashSet<>();
    
    public SpriteSourceProviderPM(PackOutput packOutput, ExistingFileHelper helper) {
        super(packOutput, helper, PrimalMagick.MODID);
    }
    
    protected void addSingle(SourceList atlas, ResourceLocation loc) {
        if (this.trackedSingles.add(loc)) {
            atlas.addSource(new SingleFile(loc, Optional.empty()));
        } else {
            LOGGER.warn("Attempted to register duplicate single texture {} to atlas", loc.toString());
        }
    }

    @Override
    protected void addSources() {
        SourceList blockAtlas = this.atlas(BLOCKS_ATLAS);
        
        // Add empty-slot background images to the block atlas
        this.addSingle(blockAtlas, WandCoreSlot.TEXTURE);
        this.addSingle(blockAtlas, WandCapSlot.TEXTURE);
        this.addSingle(blockAtlas, WandGemSlot.TEXTURE);
        this.addSingle(blockAtlas, WandSlot.TEXTURE);
        this.addSingle(blockAtlas, PaperSlot.TEXTURE);
        this.addSingle(blockAtlas, WritingImplementSlot.TEXTURE);
        this.addSingle(blockAtlas, RuneSlot.TEXTURE);
        this.addSingle(blockAtlas, RuneBaseSlot.TEXTURE);
        this.addSingle(blockAtlas, RuneEtchingSlot.TEXTURE);
        this.addSingle(blockAtlas, HoneycombSlot.TEXTURE);
        this.addSingle(blockAtlas, BottleSlot.TEXTURE);
        
        // Add block entity renderer textures to the block atlas
        this.addSingle(blockAtlas, ManaFontTER.TEXTURE);
        this.addSingle(blockAtlas, RitualBellTER.TEXTURE);
        this.addSingle(blockAtlas, SpellcraftingAltarTER.RING_TEXTURE);
        
        // Add custom item stack renderer textures to the block atlas
        this.addSingle(blockAtlas, PrimaliteShieldISTER.TEXTURE_SHIELD_BASE);
        this.addSingle(blockAtlas, PrimaliteShieldISTER.TEXTURE_SHIELD_NO_PATTERN);
        this.addSingle(blockAtlas, HexiumShieldISTER.TEXTURE_SHIELD_BASE);
        this.addSingle(blockAtlas, HexiumShieldISTER.TEXTURE_SHIELD_NO_PATTERN);
        this.addSingle(blockAtlas, HallowsteelShieldISTER.TEXTURE_SHIELD_BASE);
        this.addSingle(blockAtlas, HallowsteelShieldISTER.TEXTURE_SHIELD_NO_PATTERN);
        
        // Add source textures to the block atlas
        this.addSingle(blockAtlas, Source.getUnknownAtlasLocation());
        for (Source source : Source.SORTED_SOURCES) {
            this.addSingle(blockAtlas, source.getAtlasLocation());
        }
    }

}
