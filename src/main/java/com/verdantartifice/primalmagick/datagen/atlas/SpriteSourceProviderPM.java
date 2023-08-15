package com.verdantartifice.primalmagick.datagen.atlas;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;

import com.google.common.collect.ImmutableMap;
import com.mojang.logging.LogUtils;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.client.renderers.itemstack.HallowsteelShieldISTER;
import com.verdantartifice.primalmagick.client.renderers.itemstack.HexiumShieldISTER;
import com.verdantartifice.primalmagick.client.renderers.itemstack.PrimaliteShieldISTER;
import com.verdantartifice.primalmagick.client.renderers.tile.ManaFontTER;
import com.verdantartifice.primalmagick.client.renderers.tile.RitualBellTER;
import com.verdantartifice.primalmagick.client.renderers.tile.SpellcraftingAltarTER;
import com.verdantartifice.primalmagick.common.items.tools.SacredShieldItem;
import com.verdantartifice.primalmagick.common.menus.HoneyExtractorMenu;
import com.verdantartifice.primalmagick.common.menus.slots.PaperSlot;
import com.verdantartifice.primalmagick.common.menus.slots.RuneBaseSlot;
import com.verdantartifice.primalmagick.common.menus.slots.RuneEtchingSlot;
import com.verdantartifice.primalmagick.common.menus.slots.RuneSlot;
import com.verdantartifice.primalmagick.common.menus.slots.WandCapSlot;
import com.verdantartifice.primalmagick.common.menus.slots.WandCoreSlot;
import com.verdantartifice.primalmagick.common.menus.slots.WandGemSlot;
import com.verdantartifice.primalmagick.common.menus.slots.WandSlot;
import com.verdantartifice.primalmagick.common.menus.slots.WritingImplementSlot;
import com.verdantartifice.primalmagick.common.sources.Source;

import net.minecraft.client.renderer.texture.atlas.sources.PalettedPermutations;
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
    protected static final ResourceLocation ARMOR_TRIMS_ATLAS = new ResourceLocation("armor_trims");
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
        SourceList armorTrimsAtlas = this.atlas(ARMOR_TRIMS_ATLAS);
        
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
        this.addSingle(blockAtlas, HoneyExtractorMenu.HONEYCOMB_SLOT_TEXTURE);
        this.addSingle(blockAtlas, HoneyExtractorMenu.BOTTLE_SLOT_TEXTURE);
        
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
        this.addSingle(blockAtlas, SacredShieldItem.TEXTURE);
        
        // Add source textures to the block atlas
        this.addSingle(blockAtlas, Source.getUnknownAtlasLocation());
        for (Source source : Source.SORTED_SOURCES) {
            this.addSingle(blockAtlas, source.getAtlasLocation());
        }
        
        // Add robe armor trim item overlays to the block atlas
        blockAtlas.addSource(new PalettedPermutations(
                List.of(new ResourceLocation(PrimalMagick.MODID, "trims/items/robe_chest_trim"), 
                        new ResourceLocation(PrimalMagick.MODID, "trims/items/robe_feet_trim"), 
                        new ResourceLocation(PrimalMagick.MODID, "trims/items/robe_head_trim"), 
                        new ResourceLocation(PrimalMagick.MODID, "trims/items/robe_legs_trim")),
                new ResourceLocation("trims/color_palettes/trim_palette"),
                ImmutableMap.<String, ResourceLocation>builder()
                        .put("quartz", new ResourceLocation("trims/color_palettes/quartz"))
                        .put("iron", new ResourceLocation("trims/color_palettes/iron"))
                        .put("gold", new ResourceLocation("trims/color_palettes/gold"))
                        .put("diamond", new ResourceLocation("trims/color_palettes/diamond"))
                        .put("netherite", new ResourceLocation("trims/color_palettes/netherite"))
                        .put("redstone", new ResourceLocation("trims/color_palettes/redstone"))
                        .put("copper", new ResourceLocation("trims/color_palettes/copper"))
                        .put("emerald", new ResourceLocation("trims/color_palettes/emerald"))
                        .put("lapis", new ResourceLocation("trims/color_palettes/lapis"))
                        .put("amethyst", new ResourceLocation("trims/color_palettes/amethyst"))
                        .build()));
        
        // Add mod armor trim pattern model overlays to the armor trims atlas
        armorTrimsAtlas.addSource(new PalettedPermutations(
                List.of(new ResourceLocation(PrimalMagick.MODID, "trims/models/armor/runic"), 
                        new ResourceLocation(PrimalMagick.MODID, "trims/models/armor/runic_leggings")),
                new ResourceLocation("trims/color_palettes/trim_palette"),
                ImmutableMap.<String, ResourceLocation>builder()
                        .put("rune_earth", new ResourceLocation("trims/color_palettes/emerald"))
                        .put("rune_sea", new ResourceLocation("trims/color_palettes/lapis"))
                        .put("rune_sky", new ResourceLocation("trims/color_palettes/diamond"))
                        .put("rune_sun", new ResourceLocation("trims/color_palettes/gold"))
                        .put("rune_moon", new ResourceLocation("trims/color_palettes/iron"))
                        .put("rune_blood", new ResourceLocation("trims/color_palettes/redstone"))
                        .put("rune_infernal", new ResourceLocation("trims/color_palettes/copper"))
                        .put("rune_void", new ResourceLocation("trims/color_palettes/amethyst"))
                        .put("rune_hallowed", new ResourceLocation("trims/color_palettes/quartz"))
                        .build()));
    }

}
