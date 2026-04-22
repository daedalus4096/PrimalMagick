package com.verdantartifice.primalmagick.datagen.atlas;

import com.google.common.collect.ImmutableMap;
import com.mojang.logging.LogUtils;
import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.client.renderers.itemstack.HallowsteelShieldISTER;
import com.verdantartifice.primalmagick.client.renderers.itemstack.HexiumShieldISTER;
import com.verdantartifice.primalmagick.client.renderers.itemstack.PixieHouseISTER;
import com.verdantartifice.primalmagick.client.renderers.itemstack.PrimaliteShieldISTER;
import com.verdantartifice.primalmagick.client.renderers.tile.ManaFontTER;
import com.verdantartifice.primalmagick.client.renderers.tile.ManaInjectorTER;
import com.verdantartifice.primalmagick.client.renderers.tile.ManaRelayTER;
import com.verdantartifice.primalmagick.client.renderers.tile.RitualBellTER;
import com.verdantartifice.primalmagick.client.renderers.tile.SpellcraftingAltarTER;
import com.verdantartifice.primalmagick.common.items.tools.SacredShieldItem;
import com.verdantartifice.primalmagick.common.menus.AbstractRunescribingAltarMenu;
import com.verdantartifice.primalmagick.common.menus.DesalinatorMenu;
import com.verdantartifice.primalmagick.common.menus.HoneyExtractorMenu;
import com.verdantartifice.primalmagick.common.menus.InfernalFurnaceMenu;
import com.verdantartifice.primalmagick.common.menus.ResearchTableMenu;
import com.verdantartifice.primalmagick.common.menus.RunecarvingTableMenu;
import com.verdantartifice.primalmagick.common.menus.WandAssemblyTableMenu;
import com.verdantartifice.primalmagick.common.menus.slots.IWandSlot;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.Sources;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.client.renderer.texture.atlas.sources.PalettedPermutations;
import net.minecraft.client.renderer.texture.atlas.sources.SingleFile;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.AtlasIds;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.data.SpriteSourceProvider;
import org.slf4j.Logger;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * Data provider for additions to the game's texture atlases.
 * 
 * @author Daedalus4096
 */
public class SpriteSourceProviderPMNeoforge extends SpriteSourceProvider {
    private static final Logger LOGGER = LogUtils.getLogger();

    protected final Set<Identifier> trackedSingles = new HashSet<>();

    public SpriteSourceProviderPMNeoforge(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider, Constants.MOD_ID);
    }

    @Override
    protected void gather() {
        this.addSources();
    }

    protected void addSingle(SourceList atlas, Identifier loc) {
        if (this.trackedSingles.add(loc)) {
            atlas.addSource(new SingleFile(loc));
        } else {
            LOGGER.warn("Attempted to register duplicate single texture {} to atlas", loc.toString());
        }
    }

    protected void addSources() {
        // TODO Can this be extracted into a common super layer?
        SourceList guiAtlas = this.atlas(AtlasIds.GUI);
        SourceList blockAtlas = this.atlas(AtlasIds.BLOCKS);
        SourceList itemAtlas = this.atlas(AtlasIds.ITEMS);
        SourceList armorTrimsAtlas = this.atlas(AtlasIds.ARMOR_TRIMS);
        
        // Add empty-slot background images to the GUI atlas
        this.addSingle(guiAtlas, WandAssemblyTableMenu.CORE_SLOT_TEXTURE);
        this.addSingle(guiAtlas, WandAssemblyTableMenu.CAP_SLOT_TEXTURE);
        this.addSingle(guiAtlas, WandAssemblyTableMenu.GEM_SLOT_TEXTURE);
        this.addSingle(guiAtlas, IWandSlot.TEXTURE);
        this.addSingle(guiAtlas, ResearchTableMenu.PAPER_SLOT_TEXTURE);
        this.addSingle(guiAtlas, ResearchTableMenu.PENCIL_SLOT_TEXTURE);
        this.addSingle(guiAtlas, AbstractRunescribingAltarMenu.RUNE_SLOT_TEXTURE);
        this.addSingle(guiAtlas, RunecarvingTableMenu.BASE_SLOT_TEXTURE);
        this.addSingle(guiAtlas, RunecarvingTableMenu.ETCHING_SLOT_TEXTURE);
        this.addSingle(guiAtlas, HoneyExtractorMenu.HONEYCOMB_SLOT_TEXTURE);
        this.addSingle(guiAtlas, HoneyExtractorMenu.BOTTLE_SLOT_TEXTURE);
        this.addSingle(guiAtlas, InfernalFurnaceMenu.IGNYX_SLOT_TEXTURE);
        this.addSingle(guiAtlas, DesalinatorMenu.BUCKET_SLOT_TEXTURE);
        this.addSingle(guiAtlas, DesalinatorMenu.FLASK_SLOT_TEXTURE);

        // Add block entity renderer textures to the block atlas
        this.addSingle(blockAtlas, ManaFontTER.TEXTURE);
        this.addSingle(blockAtlas, RitualBellTER.TEXTURE);
        this.addSingle(blockAtlas, SpellcraftingAltarTER.RING_TEXTURE);
        this.addSingle(blockAtlas, ManaRelayTER.CORE_TEXTURE);
        this.addSingle(blockAtlas, ManaRelayTER.BASIC_FRAME_TEXTURE);
        this.addSingle(blockAtlas, ManaRelayTER.ENCHANTED_FRAME_TEXTURE);
        this.addSingle(blockAtlas, ManaRelayTER.FORBIDDEN_FRAME_TEXTURE);
        this.addSingle(blockAtlas, ManaRelayTER.HEAVENLY_FRAME_TEXTURE);
        this.addSingle(blockAtlas, ManaInjectorTER.BASIC_FRAME_TEXTURE);
        this.addSingle(blockAtlas, ManaInjectorTER.ENCHANTED_FRAME_TEXTURE);
        this.addSingle(blockAtlas, ManaInjectorTER.FORBIDDEN_FRAME_TEXTURE);
        this.addSingle(blockAtlas, ManaInjectorTER.HEAVENLY_FRAME_TEXTURE);
        this.addSingle(blockAtlas, ManaInjectorTER.BOTTOM_FRAME_TEXTURE);

        // Add custom item stack renderer textures to the item atlas
        this.addSingle(itemAtlas, PrimaliteShieldISTER.TEXTURE_SHIELD_BASE);
        this.addSingle(itemAtlas, PrimaliteShieldISTER.TEXTURE_SHIELD_NO_PATTERN);
        this.addSingle(itemAtlas, HexiumShieldISTER.TEXTURE_SHIELD_BASE);
        this.addSingle(itemAtlas, HexiumShieldISTER.TEXTURE_SHIELD_NO_PATTERN);
        this.addSingle(itemAtlas, HallowsteelShieldISTER.TEXTURE_SHIELD_BASE);
        this.addSingle(itemAtlas, HallowsteelShieldISTER.TEXTURE_SHIELD_NO_PATTERN);
        this.addSingle(itemAtlas, SacredShieldItem.TEXTURE);
        this.addSingle(itemAtlas, PixieHouseISTER.TEXTURE);

        // Add source textures to the block atlas
        this.addSingle(guiAtlas, Source.getUnknownImage());
        Sources.getAllSorted().forEach(source -> this.addSingle(guiAtlas, source.getImage()));

        // Add robe armor trim item overlays to the item atlas
        itemAtlas.addSource(new PalettedPermutations(
                List.of(ResourceUtils.loc("trims/items/robe_chest_trim"),
                        ResourceUtils.loc("trims/items/robe_feet_trim"),
                        ResourceUtils.loc("trims/items/robe_head_trim"),
                        ResourceUtils.loc("trims/items/robe_legs_trim")),
                Identifier.withDefaultNamespace("trims/color_palettes/trim_palette"),
                ImmutableMap.<String, Identifier>builder()
                        .put("quartz", Identifier.withDefaultNamespace("trims/color_palettes/quartz"))
                        .put("iron", Identifier.withDefaultNamespace("trims/color_palettes/iron"))
                        .put("gold", Identifier.withDefaultNamespace("trims/color_palettes/gold"))
                        .put("diamond", Identifier.withDefaultNamespace("trims/color_palettes/diamond"))
                        .put("netherite", Identifier.withDefaultNamespace("trims/color_palettes/netherite"))
                        .put("redstone", Identifier.withDefaultNamespace("trims/color_palettes/redstone"))
                        .put("copper", Identifier.withDefaultNamespace("trims/color_palettes/copper"))
                        .put("emerald", Identifier.withDefaultNamespace("trims/color_palettes/emerald"))
                        .put("lapis", Identifier.withDefaultNamespace("trims/color_palettes/lapis"))
                        .put("amethyst", Identifier.withDefaultNamespace("trims/color_palettes/amethyst"))
                        .build()));
        
        // Add mod armor trim pattern model overlays to the armor trims atlas
        armorTrimsAtlas.addSource(new PalettedPermutations(
                List.of(ResourceUtils.loc("trims/models/armor/runic"),
                        ResourceUtils.loc("trims/models/armor/runic_leggings")),
                Identifier.withDefaultNamespace("trims/color_palettes/trim_palette"),
                ImmutableMap.<String, Identifier>builder()
                        .put("rune_earth", Identifier.withDefaultNamespace("trims/color_palettes/emerald"))
                        .put("rune_sea", Identifier.withDefaultNamespace("trims/color_palettes/lapis"))
                        .put("rune_sky", Identifier.withDefaultNamespace("trims/color_palettes/diamond"))
                        .put("rune_sun", Identifier.withDefaultNamespace("trims/color_palettes/gold"))
                        .put("rune_moon", Identifier.withDefaultNamespace("trims/color_palettes/iron"))
                        .put("rune_blood", Identifier.withDefaultNamespace("trims/color_palettes/redstone"))
                        .put("rune_infernal", Identifier.withDefaultNamespace("trims/color_palettes/copper"))
                        .put("rune_void", Identifier.withDefaultNamespace("trims/color_palettes/amethyst"))
                        .put("rune_hallowed", Identifier.withDefaultNamespace("trims/color_palettes/quartz"))
                        .build()));
    }
}
