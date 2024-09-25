package com.verdantartifice.primalmagick.datagen.atlas;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
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
import com.verdantartifice.primalmagick.common.menus.AbstractRunescribingAltarMenu;
import com.verdantartifice.primalmagick.common.menus.HoneyExtractorMenu;
import com.verdantartifice.primalmagick.common.menus.InfernalFurnaceMenu;
import com.verdantartifice.primalmagick.common.menus.ResearchTableMenu;
import com.verdantartifice.primalmagick.common.menus.RunecarvingTableMenu;
import com.verdantartifice.primalmagick.common.menus.WandAssemblyTableMenu;
import com.verdantartifice.primalmagick.common.menus.slots.WandSlot;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.Sources;

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
    protected static final ResourceLocation ARMOR_TRIMS_ATLAS = ResourceLocation.withDefaultNamespace("armor_trims");
    private static final Logger LOGGER = LogUtils.getLogger();

    protected final Set<ResourceLocation> trackedSingles = new HashSet<>();
    
    public SpriteSourceProviderPM(PackOutput packOutput, ExistingFileHelper helper) {
        super(packOutput, helper, Constants.MOD_ID);
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
        this.addSingle(blockAtlas, WandAssemblyTableMenu.CORE_SLOT_TEXTURE);
        this.addSingle(blockAtlas, WandAssemblyTableMenu.CAP_SLOT_TEXTURE);
        this.addSingle(blockAtlas, WandAssemblyTableMenu.GEM_SLOT_TEXTURE);
        this.addSingle(blockAtlas, WandSlot.TEXTURE);
        this.addSingle(blockAtlas, ResearchTableMenu.PAPER_SLOT_TEXTURE);
        this.addSingle(blockAtlas, ResearchTableMenu.PENCIL_SLOT_TEXTURE);
        this.addSingle(blockAtlas, AbstractRunescribingAltarMenu.RUNE_SLOT_TEXTURE);
        this.addSingle(blockAtlas, RunecarvingTableMenu.BASE_SLOT_TEXTURE);
        this.addSingle(blockAtlas, RunecarvingTableMenu.ETCHING_SLOT_TEXTURE);
        this.addSingle(blockAtlas, HoneyExtractorMenu.HONEYCOMB_SLOT_TEXTURE);
        this.addSingle(blockAtlas, HoneyExtractorMenu.BOTTLE_SLOT_TEXTURE);
        this.addSingle(blockAtlas, InfernalFurnaceMenu.IGNYX_SLOT_TEXTURE);
        
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
        for (Source source : Sources.getAllSorted()) {
            this.addSingle(blockAtlas, source.getAtlasLocation());
        }
        
        // Add robe armor trim item overlays to the block atlas
        blockAtlas.addSource(new PalettedPermutations(
                List.of(ResourceUtils.loc("trims/items/robe_chest_trim"),
                        ResourceUtils.loc("trims/items/robe_feet_trim"),
                        ResourceUtils.loc("trims/items/robe_head_trim"),
                        ResourceUtils.loc("trims/items/robe_legs_trim")),
                ResourceLocation.withDefaultNamespace("trims/color_palettes/trim_palette"),
                ImmutableMap.<String, ResourceLocation>builder()
                        .put("quartz", ResourceLocation.withDefaultNamespace("trims/color_palettes/quartz"))
                        .put("iron", ResourceLocation.withDefaultNamespace("trims/color_palettes/iron"))
                        .put("gold", ResourceLocation.withDefaultNamespace("trims/color_palettes/gold"))
                        .put("diamond", ResourceLocation.withDefaultNamespace("trims/color_palettes/diamond"))
                        .put("netherite", ResourceLocation.withDefaultNamespace("trims/color_palettes/netherite"))
                        .put("redstone", ResourceLocation.withDefaultNamespace("trims/color_palettes/redstone"))
                        .put("copper", ResourceLocation.withDefaultNamespace("trims/color_palettes/copper"))
                        .put("emerald", ResourceLocation.withDefaultNamespace("trims/color_palettes/emerald"))
                        .put("lapis", ResourceLocation.withDefaultNamespace("trims/color_palettes/lapis"))
                        .put("amethyst", ResourceLocation.withDefaultNamespace("trims/color_palettes/amethyst"))
                        .build()));
        
        // Add mod armor trim pattern model overlays to the armor trims atlas
        armorTrimsAtlas.addSource(new PalettedPermutations(
                List.of(ResourceUtils.loc("trims/models/armor/runic"),
                        ResourceUtils.loc("trims/models/armor/runic_leggings")),
                ResourceLocation.withDefaultNamespace("trims/color_palettes/trim_palette"),
                ImmutableMap.<String, ResourceLocation>builder()
                        .put("rune_earth", ResourceLocation.withDefaultNamespace("trims/color_palettes/emerald"))
                        .put("rune_sea", ResourceLocation.withDefaultNamespace("trims/color_palettes/lapis"))
                        .put("rune_sky", ResourceLocation.withDefaultNamespace("trims/color_palettes/diamond"))
                        .put("rune_sun", ResourceLocation.withDefaultNamespace("trims/color_palettes/gold"))
                        .put("rune_moon", ResourceLocation.withDefaultNamespace("trims/color_palettes/iron"))
                        .put("rune_blood", ResourceLocation.withDefaultNamespace("trims/color_palettes/redstone"))
                        .put("rune_infernal", ResourceLocation.withDefaultNamespace("trims/color_palettes/copper"))
                        .put("rune_void", ResourceLocation.withDefaultNamespace("trims/color_palettes/amethyst"))
                        .put("rune_hallowed", ResourceLocation.withDefaultNamespace("trims/color_palettes/quartz"))
                        .build()));
    }

}
