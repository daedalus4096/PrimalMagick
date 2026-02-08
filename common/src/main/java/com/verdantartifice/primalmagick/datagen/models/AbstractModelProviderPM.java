package com.verdantartifice.primalmagick.datagen.models;

import com.mojang.datafixers.util.Pair;
import com.verdantartifice.primalmagick.client.item.color.SourceTint;
import com.verdantartifice.primalmagick.client.item.properties.StackDyeColor;
import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import com.verdantartifice.primalmagick.common.blocks.misc.PillarBlock;
import com.verdantartifice.primalmagick.common.blockstates.properties.TimePhase;
import com.verdantartifice.primalmagick.common.items.EquipmentAssetsPM;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.items.entities.ManaArrowItem;
import com.verdantartifice.primalmagick.common.items.essence.EssenceItem;
import com.verdantartifice.primalmagick.common.items.food.AmbrosiaItem;
import com.verdantartifice.primalmagick.common.items.misc.AttunementShacklesItem;
import com.verdantartifice.primalmagick.common.items.misc.DrainedPixieItem;
import com.verdantartifice.primalmagick.common.items.misc.HummingArtifactItem;
import com.verdantartifice.primalmagick.common.items.misc.PixieItem;
import com.verdantartifice.primalmagick.common.items.misc.RuneItem;
import com.verdantartifice.primalmagick.common.items.misc.SanguineCoreItem;
import com.verdantartifice.primalmagick.common.items.wands.StaffCoreItem;
import com.verdantartifice.primalmagick.common.items.wands.WandCapItem;
import com.verdantartifice.primalmagick.common.items.wands.WandCoreItem;
import com.verdantartifice.primalmagick.common.items.wands.WandGemItem;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.client.color.item.Constant;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.MultiPartGenerator;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.client.renderer.block.model.VariantMutator;
import net.minecraft.client.renderer.block.model.multipart.CombinedCondition;
import net.minecraft.client.renderer.block.model.multipart.Condition;
import net.minecraft.client.renderer.item.BlockModelWrapper;
import net.minecraft.client.renderer.item.SelectItemModel;
import net.minecraft.core.Direction;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class AbstractModelProviderPM extends ModelProvider {
    static final Identifier SOLID_RENDER_TYPE = Identifier.withDefaultNamespace("solid");
    static final Identifier CUTOUT_RENDER_TYPE = Identifier.withDefaultNamespace("cutout");
    static final Identifier TRANSLUCENT_RENDER_TYPE = Identifier.withDefaultNamespace("translucent");

    public AbstractModelProviderPM(PackOutput output) {
        super(output);
    }

    protected void executeBlockModelGenerators(BlockModelGenerators blockModels) {
        // Generate models for defined block families
        BlockFamiliesPM.getAllFamilies().filter(BlockFamily::shouldGenerateModel).forEach(family -> blockModels.family(family.getBaseBlock()).generateFor(family));

        // Generate non-family marble blocks
        this.generatePillarBlock(BlocksPM.MARBLE_PILLAR.get(), blockModels);
        blockModels.createTrivialBlock(BlocksPM.MARBLE_RUNED.get(), TexturedModel.COLUMN);
        blockModels.createTrivialCube(BlocksPM.MARBLE_TILES.get());
        this.createCarvedBookshelf(BlocksPM.MARBLE_BOOKSHELF.get(), blockModels);

        // Generate enchanted marble blocks
        this.generatePillarBlock(BlocksPM.MARBLE_ENCHANTED_PILLAR.get(), blockModels);
        blockModels.createTrivialBlock(BlocksPM.MARBLE_ENCHANTED_RUNED.get(), TexturedModel.COLUMN);
        this.createCarvedBookshelf(BlocksPM.MARBLE_ENCHANTED_BOOKSHELF.get(), blockModels);

        // Generate smoked marble blocks
        this.generatePillarBlock(BlocksPM.MARBLE_SMOKED_PILLAR.get(), blockModels);
        blockModels.createTrivialBlock(BlocksPM.MARBLE_SMOKED_RUNED.get(), TexturedModel.COLUMN);
        this.createCarvedBookshelf(BlocksPM.MARBLE_SMOKED_BOOKSHELF.get(), blockModels);

        // Generate hallowed marble blocks
        this.generatePillarBlock(BlocksPM.MARBLE_HALLOWED_PILLAR.get(), blockModels);
        blockModels.createTrivialBlock(BlocksPM.MARBLE_HALLOWED_RUNED.get(), TexturedModel.COLUMN);
        this.createCarvedBookshelf(BlocksPM.MARBLE_HALLOWED_BOOKSHELF.get(), blockModels);

        // TODO Generate sunwood blocks
        this.phasingWoodProvider(BlocksPM.SUNWOOD_LOG.get(), blockModels).logWithHorizontal(BlocksPM.SUNWOOD_LOG.get()).wood(BlocksPM.SUNWOOD_WOOD.get());
        this.phasingWoodProvider(BlocksPM.STRIPPED_SUNWOOD_LOG.get(), blockModels).logWithHorizontal(BlocksPM.STRIPPED_SUNWOOD_LOG.get()).wood(BlocksPM.STRIPPED_SUNWOOD_WOOD.get());
        this.createPhasingLeaves(BlocksPM.SUNWOOD_LEAVES.get(), blockModels);
        blockModels.createPlantWithDefaultItem(BlocksPM.SUNWOOD_SAPLING.get(), BlocksPM.POTTED_SUNWOOD_SAPLING.get(), BlockModelGenerators.PlantType.NOT_TINTED);

        // TODO Generate moonwood blocks
        this.phasingWoodProvider(BlocksPM.MOONWOOD_LOG.get(), blockModels).logWithHorizontal(BlocksPM.MOONWOOD_LOG.get()).wood(BlocksPM.MOONWOOD_WOOD.get());
        this.phasingWoodProvider(BlocksPM.STRIPPED_MOONWOOD_LOG.get(), blockModels).logWithHorizontal(BlocksPM.STRIPPED_MOONWOOD_LOG.get()).wood(BlocksPM.STRIPPED_MOONWOOD_WOOD.get());
        this.createPhasingLeaves(BlocksPM.MOONWOOD_LEAVES.get(), blockModels);
        blockModels.createPlantWithDefaultItem(BlocksPM.MOONWOOD_SAPLING.get(), BlocksPM.POTTED_MOONWOOD_SAPLING.get(), BlockModelGenerators.PlantType.NOT_TINTED);

        // TODO Generate hallowood blocks
        blockModels.woodProvider(BlocksPM.HALLOWOOD_LOG.get()).logWithHorizontal(BlocksPM.HALLOWOOD_LOG.get()).wood(BlocksPM.HALLOWOOD_WOOD.get());
        blockModels.woodProvider(BlocksPM.STRIPPED_HALLOWOOD_LOG.get()).logWithHorizontal(BlocksPM.STRIPPED_HALLOWOOD_LOG.get()).wood(BlocksPM.STRIPPED_HALLOWOOD_WOOD.get());
        blockModels.createTrivialBlock(BlocksPM.HALLOWOOD_LEAVES.get(), TexturedModel.LEAVES);
        blockModels.createPlantWithDefaultItem(BlocksPM.HALLOWOOD_SAPLING.get(), BlocksPM.POTTED_HALLOWOOD_SAPLING.get(), BlockModelGenerators.PlantType.NOT_TINTED);

        // TODO Generate crop blocks
        // TODO Generate infused stone blocks
        // TODO Generate budding gem blocks
        // TODO Generate skyglass blocks
        // TODO Generate skyglass pane blocks
        // TODO Generate ritual candle blocks
        // TODO Generate mana font blocks
        // TODO Generate device blocks
        // TODO Generate misc blocks
    }

    protected void executeItemModelGenerators(ItemModelGenerators itemModels) {
        // Generate crop items
        itemModels.generateFlatItem(ItemsPM.HYDROMELON_SEEDS.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsPM.HYDROMELON_SLICE.get(), ModelTemplates.FLAT_ITEM);

        // Generate salted food items
        itemModels.generateFlatItem(ItemsPM.SALTED_BAKED_POTATO.get(), Items.BAKED_POTATO, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsPM.SALTED_COOKED_BEEF.get(), Items.COOKED_BEEF, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsPM.SALTED_COOKED_CHICKEN.get(), Items.COOKED_CHICKEN, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsPM.SALTED_COOKED_COD.get(), Items.COOKED_COD,ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsPM.SALTED_COOKED_MUTTON.get(), Items.COOKED_MUTTON, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsPM.SALTED_COOKED_PORKCHOP.get(), Items.COOKED_PORKCHOP, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsPM.SALTED_COOKED_RABBIT.get(), Items.COOKED_RABBIT, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsPM.SALTED_COOKED_SALMON.get(), Items.COOKED_SALMON, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsPM.SALTED_BEETROOT_SOUP.get(), Items.BEETROOT_SOUP, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsPM.SALTED_MUSHROOM_STEW.get(), Items.MUSHROOM_STEW, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsPM.SALTED_RABBIT_STEW.get(), Items.RABBIT_STEW, ModelTemplates.FLAT_ITEM);

        // Generate mineral items
        itemModels.generateFlatItem(ItemsPM.IRON_GRIT.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsPM.GOLD_GRIT.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsPM.COPPER_GRIT.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsPM.SALT_PINCH.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsPM.PRIMALITE_INGOT.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsPM.HEXIUM_INGOT.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsPM.HALLOWSTEEL_INGOT.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsPM.PRIMALITE_NUGGET.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsPM.HEXIUM_NUGGET.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsPM.HALLOWSTEEL_NUGGET.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsPM.QUARTZ_NUGGET.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsPM.ENERGIZED_AMETHYST.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsPM.ENERGIZED_DIAMOND.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsPM.ENERGIZED_EMERALD.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsPM.ENERGIZED_QUARTZ.get(), ModelTemplates.FLAT_ITEM);

        // Generate tool items
        itemModels.generateFlatItem(ItemsPM.PRIMALITE_SWORD.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateTrident(ItemsPM.PRIMALITE_TRIDENT.get());
        itemModels.generateBow(ItemsPM.PRIMALITE_BOW.get());
        itemModels.generateFlatItem(ItemsPM.PRIMALITE_SHOVEL.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ItemsPM.PRIMALITE_PICKAXE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ItemsPM.PRIMALITE_AXE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ItemsPM.PRIMALITE_HOE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFishingRod(ItemsPM.PRIMALITE_FISHING_ROD.get());
        itemModels.generateShield(ItemsPM.PRIMALITE_SHIELD.get());
        itemModels.generateFlatItem(ItemsPM.HEXIUM_SWORD.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateTrident(ItemsPM.HEXIUM_TRIDENT.get());
        itemModels.generateBow(ItemsPM.HEXIUM_BOW.get());
        itemModels.generateFlatItem(ItemsPM.HEXIUM_SHOVEL.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ItemsPM.HEXIUM_PICKAXE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ItemsPM.HEXIUM_AXE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ItemsPM.HEXIUM_HOE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFishingRod(ItemsPM.HEXIUM_FISHING_ROD.get());
        itemModels.generateShield(ItemsPM.HEXIUM_SHIELD.get());
        itemModels.generateFlatItem(ItemsPM.HALLOWSTEEL_SWORD.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateTrident(ItemsPM.HALLOWSTEEL_TRIDENT.get());
        itemModels.generateBow(ItemsPM.HALLOWSTEEL_BOW.get());
        itemModels.generateFlatItem(ItemsPM.HALLOWSTEEL_SHOVEL.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ItemsPM.HALLOWSTEEL_PICKAXE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ItemsPM.HALLOWSTEEL_AXE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ItemsPM.HALLOWSTEEL_HOE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFishingRod(ItemsPM.HALLOWSTEEL_FISHING_ROD.get());
        itemModels.generateShield(ItemsPM.HALLOWSTEEL_SHIELD.get());
        itemModels.generateFlatItem(ItemsPM.FORBIDDEN_SWORD.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateTrident(ItemsPM.FORBIDDEN_TRIDENT.get());
        itemModels.generateBow(ItemsPM.FORBIDDEN_BOW.get());
        itemModels.generateFlatItem(ItemsPM.PRIMAL_SHOVEL.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ItemsPM.PRIMAL_PICKAXE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ItemsPM.PRIMAL_AXE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ItemsPM.PRIMAL_HOE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFishingRod(ItemsPM.PRIMAL_FISHING_ROD.get());
        // TODO Generate client item for sacred shield?
        this.generateSpelltomeItem(itemModels, ItemsPM.SPELLTOME_APPRENTICE.get(), ItemsPM.STATIC_BOOK.get());
        this.generateSpelltomeItem(itemModels, ItemsPM.SPELLTOME_ADEPT.get(), ItemsPM.STATIC_BOOK_UNCOMMON.get());
        this.generateSpelltomeItem(itemModels, ItemsPM.SPELLTOME_WIZARD.get(), ItemsPM.STATIC_BOOK_RARE.get());
        this.generateSpelltomeItem(itemModels, ItemsPM.SPELLTOME_ARCHMAGE.get(), ItemsPM.GRIMOIRE.get());
        this.generateManaOrbItem(itemModels, ItemsPM.MANA_ORB_APPRENTICE.get(), ItemsPM.APPRENTICE_WAND_GEM_ITEM.get());
        this.generateManaOrbItem(itemModels, ItemsPM.MANA_ORB_ADEPT.get(), ItemsPM.ADEPT_WAND_GEM_ITEM.get());
        this.generateManaOrbItem(itemModels, ItemsPM.MANA_ORB_WIZARD.get(), ItemsPM.WIZARD_WAND_GEM_ITEM.get());
        this.generateManaOrbItem(itemModels, ItemsPM.MANA_ORB_ARCHMAGE.get(), ItemsPM.ARCHMAGE_WAND_GEM_ITEM.get());

        // Generate mana arrow items
        ManaArrowItem.getManaArrows().forEach(item ->
                this.generateSourceTintedLayeredItem(itemModels, item, ResourceUtils.loc("mana_arrow_head").withPrefix("item/"), ResourceUtils.loc("mana_arrow_base").withPrefix("item/")));

        // Generate armor items
        itemModels.generateTrimmableItem(ItemsPM.IMBUED_WOOL_HEAD.get(), EquipmentAssetsPM.IMBUED_WOOL, ItemModelGenerators.TRIM_PREFIX_HELMET, false);
        itemModels.generateTrimmableItem(ItemsPM.IMBUED_WOOL_CHEST.get(), EquipmentAssetsPM.IMBUED_WOOL, ItemModelGenerators.TRIM_PREFIX_CHESTPLATE, false);
        itemModels.generateTrimmableItem(ItemsPM.IMBUED_WOOL_LEGS.get(), EquipmentAssetsPM.IMBUED_WOOL, ItemModelGenerators.TRIM_PREFIX_LEGGINGS, false);
        itemModels.generateTrimmableItem(ItemsPM.IMBUED_WOOL_FEET.get(), EquipmentAssetsPM.IMBUED_WOOL, ItemModelGenerators.TRIM_PREFIX_BOOTS, false);
        itemModels.generateTrimmableItem(ItemsPM.SPELLCLOTH_HEAD.get(), EquipmentAssetsPM.SPELLCLOTH, ItemModelGenerators.TRIM_PREFIX_HELMET, false);
        itemModels.generateTrimmableItem(ItemsPM.SPELLCLOTH_CHEST.get(), EquipmentAssetsPM.SPELLCLOTH, ItemModelGenerators.TRIM_PREFIX_CHESTPLATE, false);
        itemModels.generateTrimmableItem(ItemsPM.SPELLCLOTH_LEGS.get(), EquipmentAssetsPM.SPELLCLOTH, ItemModelGenerators.TRIM_PREFIX_LEGGINGS, false);
        itemModels.generateTrimmableItem(ItemsPM.SPELLCLOTH_FEET.get(), EquipmentAssetsPM.SPELLCLOTH, ItemModelGenerators.TRIM_PREFIX_BOOTS, false);
        itemModels.generateTrimmableItem(ItemsPM.HEXWEAVE_HEAD.get(), EquipmentAssetsPM.HEXWEAVE, ItemModelGenerators.TRIM_PREFIX_HELMET, false);
        itemModels.generateTrimmableItem(ItemsPM.HEXWEAVE_CHEST.get(), EquipmentAssetsPM.HEXWEAVE, ItemModelGenerators.TRIM_PREFIX_CHESTPLATE, false);
        itemModels.generateTrimmableItem(ItemsPM.HEXWEAVE_LEGS.get(), EquipmentAssetsPM.HEXWEAVE, ItemModelGenerators.TRIM_PREFIX_LEGGINGS, false);
        itemModels.generateTrimmableItem(ItemsPM.HEXWEAVE_FEET.get(), EquipmentAssetsPM.HEXWEAVE, ItemModelGenerators.TRIM_PREFIX_BOOTS, false);
        itemModels.generateTrimmableItem(ItemsPM.SAINTSWOOL_HEAD.get(), EquipmentAssetsPM.SAINTSWOOL, ItemModelGenerators.TRIM_PREFIX_HELMET, false);
        itemModels.generateTrimmableItem(ItemsPM.SAINTSWOOL_CHEST.get(), EquipmentAssetsPM.SAINTSWOOL, ItemModelGenerators.TRIM_PREFIX_CHESTPLATE, false);
        itemModels.generateTrimmableItem(ItemsPM.SAINTSWOOL_LEGS.get(), EquipmentAssetsPM.SAINTSWOOL, ItemModelGenerators.TRIM_PREFIX_LEGGINGS, false);
        itemModels.generateTrimmableItem(ItemsPM.SAINTSWOOL_FEET.get(), EquipmentAssetsPM.SAINTSWOOL, ItemModelGenerators.TRIM_PREFIX_BOOTS, false);
        itemModels.generateTrimmableItem(ItemsPM.PRIMALITE_HEAD.get(), EquipmentAssetsPM.PRIMALITE, ItemModelGenerators.TRIM_PREFIX_HELMET, false);
        itemModels.generateTrimmableItem(ItemsPM.PRIMALITE_CHEST.get(), EquipmentAssetsPM.PRIMALITE, ItemModelGenerators.TRIM_PREFIX_CHESTPLATE, false);
        itemModels.generateTrimmableItem(ItemsPM.PRIMALITE_LEGS.get(), EquipmentAssetsPM.PRIMALITE, ItemModelGenerators.TRIM_PREFIX_LEGGINGS, false);
        itemModels.generateTrimmableItem(ItemsPM.PRIMALITE_FEET.get(), EquipmentAssetsPM.PRIMALITE, ItemModelGenerators.TRIM_PREFIX_BOOTS, false);
        itemModels.generateTrimmableItem(ItemsPM.HEXIUM_HEAD.get(), EquipmentAssetsPM.HEXIUM, ItemModelGenerators.TRIM_PREFIX_HELMET, false);
        itemModels.generateTrimmableItem(ItemsPM.HEXIUM_CHEST.get(), EquipmentAssetsPM.HEXIUM, ItemModelGenerators.TRIM_PREFIX_CHESTPLATE, false);
        itemModels.generateTrimmableItem(ItemsPM.HEXIUM_LEGS.get(), EquipmentAssetsPM.HEXIUM, ItemModelGenerators.TRIM_PREFIX_LEGGINGS, false);
        itemModels.generateTrimmableItem(ItemsPM.HEXIUM_FEET.get(), EquipmentAssetsPM.HEXIUM, ItemModelGenerators.TRIM_PREFIX_BOOTS, false);
        itemModels.generateTrimmableItem(ItemsPM.HALLOWSTEEL_HEAD.get(), EquipmentAssetsPM.HALLOWSTEEL, ItemModelGenerators.TRIM_PREFIX_HELMET, false);
        itemModels.generateTrimmableItem(ItemsPM.HALLOWSTEEL_CHEST.get(), EquipmentAssetsPM.HALLOWSTEEL, ItemModelGenerators.TRIM_PREFIX_CHESTPLATE, false);
        itemModels.generateTrimmableItem(ItemsPM.HALLOWSTEEL_LEGS.get(), EquipmentAssetsPM.HALLOWSTEEL, ItemModelGenerators.TRIM_PREFIX_LEGGINGS, false);
        itemModels.generateTrimmableItem(ItemsPM.HALLOWSTEEL_FEET.get(), EquipmentAssetsPM.HALLOWSTEEL, ItemModelGenerators.TRIM_PREFIX_BOOTS, false);

        // Generate miscellaneous items
        itemModels.generateFlatItem(ItemsPM.GRIMOIRE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsPM.CREATIVE_GRIMOIRE.get(), ModelTemplates.FLAT_ITEM);
        // TODO Generate arcanometer
        itemModels.generateFlatItem(ItemsPM.MAGNIFYING_GLASS.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsPM.ALCHEMICAL_WASTE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsPM.BLOODY_FLESH.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsPM.HALLOWED_ORB.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsPM.HEARTWOOD.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsPM.ENCHANTED_INK.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsPM.ENCHANTED_INK_AND_QUILL.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsPM.SEASCRIBE_PEN.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsPM.ROCK_SALT.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsPM.EARTHSHATTER_HAMMER.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsPM.MANA_PRISM.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsPM.TALLOW.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsPM.BEESWAX.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsPM.MANA_SALTS.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsPM.MANAFRUIT.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsPM.INCENSE_STICK.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsPM.SOUL_GEM.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsPM.SOUL_GEM_SLIVER.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsPM.SPELLCLOTH.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsPM.HEXWEAVE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsPM.SAINTSWOOL.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsPM.MAGITECH_PARTS_BASIC.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsPM.MAGITECH_PARTS_ENCHANTED.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsPM.MAGITECH_PARTS_FORBIDDEN.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsPM.MAGITECH_PARTS_HEAVENLY.get(), ModelTemplates.FLAT_ITEM);
        this.generateColorSelectItem(itemModels, ItemsPM.FLYING_CARPET.get(), DyeColor.WHITE);
        itemModels.generateFlatItem(ItemsPM.DREAM_VISION_TALISMAN.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsPM.IGNYX.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsPM.DOWSING_ROD.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsPM.FOUR_LEAF_CLOVER.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsPM.RECALL_STONE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsPM.RUNIC_ARMOR_TRIM_SMITHING_TEMPLATE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsPM.BASIC_WARDING_MODULE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsPM.GREATER_WARDING_MODULE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsPM.SUPREME_WARDING_MODULE.get(), ModelTemplates.FLAT_ITEM);
        // TODO Generate pixie house item

        // Generate knowledge items
        itemModels.generateFlatItem(ItemsPM.OBSERVATION_NOTES.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsPM.THEORY_NOTES.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsPM.MYSTICAL_RELIC.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsPM.MYSTICAL_RELIC_FRAGMENT.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsPM.BLOOD_NOTES.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsPM.SHEEP_TOME.get(), ModelTemplates.FLAT_ITEM);

        // Generate essence items
        EssenceItem.getAllEssences().forEach(item -> itemModels.generateFlatItem(item, ModelTemplates.FLAT_ITEM));

        // Generate rune items
        itemModels.generateFlatItem(ItemsPM.RUNE_UNATTUNED.get(), ModelTemplates.FLAT_ITEM);
        RuneItem.getAllRunes().forEach(item -> itemModels.generateFlatItem(item, ModelTemplates.FLAT_ITEM));

        // Generate ambrosia items
        AmbrosiaItem.getAllAmbrosiasOfType(AmbrosiaItem.Type.BASIC).forEach(item ->
                this.generateSourceTintedLayeredItem(itemModels, item, ResourceUtils.loc("ambrosia_overlay").withPrefix("item/"), ResourceUtils.loc("ambrosia").withPrefix("item/")));
        AmbrosiaItem.getAllAmbrosiasOfType(AmbrosiaItem.Type.GREATER).forEach(item ->
                this.generateSourceTintedLayeredItem(itemModels, item, ResourceUtils.loc("ambrosia_overlay").withPrefix("item/"), ResourceUtils.loc("ambrosia_greater").withPrefix("item/")));
        AmbrosiaItem.getAllAmbrosiasOfType(AmbrosiaItem.Type.SUPREME).forEach(item ->
                this.generateSourceTintedLayeredItem(itemModels, item, ResourceUtils.loc("ambrosia_overlay").withPrefix("item/"), ResourceUtils.loc("ambrosia_supreme").withPrefix("item/")));

        // Generate attunement shackles items
        AttunementShacklesItem.getAllShackles().forEach(item ->
                this.generateSourceTintedLayeredItem(itemModels, item, ResourceUtils.loc("attunement_shackles_overlay").withPrefix("item/"), ResourceUtils.loc("attunement_shackles").withPrefix("item/")));

        // Generate humming artifact items
        itemModels.generateFlatItem(ItemsPM.HUMMING_ARTIFACT_UNATTUNED.get(), ModelTemplates.FLAT_ITEM);
        HummingArtifactItem.getAllHummingArtifacts().forEach(item ->
                this.generateSourceTintedLayeredItem(itemModels, item, ResourceUtils.loc("humming_artifact_glow").withPrefix("item/"), ResourceUtils.loc("humming_artifact_case").withPrefix("item/")));

        // Generate sanguine core items
        itemModels.generateFlatItem(ItemsPM.SANGUINE_CORE_BLANK.get(), ModelTemplates.FLAT_ITEM);
        SanguineCoreItem.getAllCores().forEach(item -> itemModels.generateFlatItem(item, ItemsPM.SANGUINE_CORE_BLANK.get(), ModelTemplates.FLAT_ITEM));

        // Generate concoction items
        itemModels.generateFlatItem(ItemsPM.SKYGLASS_FLASK.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsPM.BOMB_CASING.get(), ModelTemplates.FLAT_ITEM);
        this.generateConcoctionItem(itemModels, ItemsPM.CONCOCTION.get());
        this.generateConcoctionItem(itemModels, ItemsPM.ALCHEMICAL_BOMB.get());

        // Generate caster items
        itemModels.generateFlatItem(ItemsPM.SPELL_SCROLL_BLANK.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsPM.SPELL_SCROLL_FILLED.get(), ModelTemplates.FLAT_ITEM);
        // TODO Generate mundane wand
        // TODO Generate modular wand
        // TODO Generate modular staff

        // Generate wand component items
        WandCoreItem.getAllCores().forEach(item -> itemModels.generateFlatItem(item, ModelTemplates.FLAT_ITEM));
        StaffCoreItem.getAllCores().forEach(item -> itemModels.generateFlatItem(item, ModelTemplates.FLAT_ITEM));
        WandCapItem.getAllCaps().forEach(item -> itemModels.generateFlatItem(item, ModelTemplates.FLAT_ITEM));
        WandGemItem.getAllGems().forEach(item -> itemModels.generateFlatItem(item, ModelTemplates.FLAT_ITEM));

        // Generate spawn items
        this.generateSpawnItem(itemModels, ItemsPM.TREEFOLK_SPAWN_EGG.get(), 0x76440F, 0x007302);
        this.generateSpawnItem(itemModels, ItemsPM.PRIMALITE_GOLEM_SPAWN_EGG.get(), 0x27E1C7, 0x026278);
        this.generateSpawnItem(itemModels, ItemsPM.HEXIUM_GOLEM_SPAWN_EGG.get(), 0x791E29, 0x100736);
        this.generateSpawnItem(itemModels, ItemsPM.HALLOWSTEEL_GOLEM_SPAWN_EGG.get(), 0xFDFFE0, 0xEDE1A2);

        // Generate pixie and drained pixie items
        PixieItem.getAllPixies().forEach(item ->
                this.generateSourceTintedLayeredItem(itemModels, item, ResourceUtils.loc("pixie_overlay").withPrefix("item/"), ResourceUtils.loc("pixie").withPrefix("item/")));
        DrainedPixieItem.getAllDrainedPixies().forEach(item -> this.generateDrainedPixieItem(itemModels, item));

        // Generate book items
        itemModels.generateFlatItem(ItemsPM.STATIC_BOOK.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsPM.STATIC_BOOK_UNCOMMON.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsPM.STATIC_BOOK_RARE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsPM.STATIC_TABLET.get(), ItemsPM.MYSTICAL_RELIC.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsPM.LORE_TABLET_FRAGMENT.get(), ItemsPM.MYSTICAL_RELIC_FRAGMENT.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsPM.LORE_TABLET_DIRTY.get(), ItemsPM.MYSTICAL_RELIC.get(), ModelTemplates.FLAT_ITEM);

        // Generate debug items
        itemModels.generateFlatItem(ItemsPM.TICK_STICK.get(), Items.STICK, ModelTemplates.FLAT_ITEM);
    }

    private void generateSourceTintedLayeredItem(ItemModelGenerators itemModels, Item item, Identifier overlayModel, Identifier baseModel) {
        Identifier modelLoc = itemModels.generateLayeredItem(item, overlayModel, baseModel);
        itemModels.itemModelOutput.accept(item, ItemModelUtils.tintedModel(modelLoc, new SourceTint()));
    }

    private void generateConcoctionItem(ItemModelGenerators itemModels, Item item) {
        Identifier modelLoc = itemModels.generateLayeredItem(item, ModelLocationUtils.getModelLocation(item, "_overlay"), ModelLocationUtils.getModelLocation(item));
        itemModels.addPotionTint(item, modelLoc);
    }

    private void generateSpawnItem(ItemModelGenerators itemModels, Item item, int baseColor, int overlayColor) {
        Identifier spawnEggLoc = ResourceUtils.loc("spawn_egg").withPrefix("item/");
        Identifier modelLoc = itemModels.generateLayeredItem(item, spawnEggLoc, spawnEggLoc.withSuffix("_overlay"));
        itemModels.itemModelOutput.accept(item, ItemModelUtils.tintedModel(modelLoc, new Constant(baseColor), new Constant(overlayColor)));
    }

    private void generateDrainedPixieItem(ItemModelGenerators itemModels, Item item) {
        Identifier modelLoc = ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(item), TextureMapping.layer0(ResourceUtils.loc("drained_pixie").withPrefix("item/")), itemModels.modelOutput);
        itemModels.itemModelOutput.accept(item, ItemModelUtils.plainModel(modelLoc));
    }

    private void generateColorSelectItem(ItemModelGenerators itemModels, Item item, DyeColor defaultColor) {
        BlockModelWrapper.Unbaked defaultModel = null;
        Map<DyeColor, SelectItemModel.SwitchCase<DyeColor>> cases = new HashMap<>();
        for (DyeColor color : DyeColor.values()) {
            var wrapper = new BlockModelWrapper.Unbaked(ModelLocationUtils.getModelLocation(item, "_" + color.getName()), List.of());
            cases.put(color, new SelectItemModel.SwitchCase<>(List.of(color), wrapper));
            if (color.equals(defaultColor)) {
                defaultModel = wrapper;
            }
        }
        itemModels.itemModelOutput.accept(item, new SelectItemModel.Unbaked(
                new SelectItemModel.UnbakedSwitch<>(new StackDyeColor(), cases.values().stream().toList()),
                Optional.ofNullable(defaultModel)));
    }

    private void generateManaOrbItem(ItemModelGenerators itemModels, Item item, Item particleItem) {
        Identifier modelLoc = ModelTemplatesPM.MANA_ORB.create(item, TextureMapping.particleFromItem(particleItem), itemModels.modelOutput);
        itemModels.itemModelOutput.accept(item, ItemModelUtils.plainModel(modelLoc));
    }

    private void generateSpelltomeItem(ItemModelGenerators itemModels, Item item, Item particleItem) {
        Identifier modelLoc = ModelTemplatesPM.SPELLTOME.create(item, TextureMapping.particleFromItem(particleItem), itemModels.modelOutput);
        itemModels.itemModelOutput.accept(item, ItemModelUtils.plainModel(modelLoc));
    }

    private void generatePillarBlock(Block block, BlockModelGenerators blockModels) {
        MultiVariant baseMultiVariant = BlockModelGenerators.plainVariant(TexturedModelsPM.PILLAR.create(block, blockModels.modelOutput));
        MultiVariant bottomMultiVariant = BlockModelGenerators.plainVariant(TexturedModelsPM.PILLAR_BOTTOM.create(block, blockModels.modelOutput));
        MultiVariant topMultiVariant = BlockModelGenerators.plainVariant(TexturedModelsPM.PILLAR_TOP.create(block, blockModels.modelOutput));
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
                .with(PropertyDispatch.initial(PillarBlock.PROPERTY_TYPE)
                        .select(PillarBlock.Type.BASE, baseMultiVariant)
                        .select(PillarBlock.Type.BOTTOM, bottomMultiVariant)
                        .select(PillarBlock.Type.TOP, topMultiVariant)));
    }

    private void createCarvedBookshelf(Block block, BlockModelGenerators blockModels) {
        MultiVariant multivariant = BlockModelGenerators.plainVariant(ModelLocationUtils.getModelLocation(block));
        MultiPartGenerator multipartgenerator = MultiPartGenerator.multiPart(block);
        List.of(Pair.of(Direction.NORTH, BlockModelGenerators.NOP), Pair.of(Direction.EAST, BlockModelGenerators.Y_ROT_90), Pair.of(Direction.SOUTH, BlockModelGenerators.Y_ROT_180), Pair.of(Direction.WEST, BlockModelGenerators.Y_ROT_270)).forEach((pair) -> {
            Direction direction = pair.getFirst();
            VariantMutator variantmutator = pair.getSecond();
            Condition condition = BlockModelGenerators.condition().term(BlockStateProperties.HORIZONTAL_FACING, direction).build();
            multipartgenerator.with(condition, multivariant.with(variantmutator).with(BlockModelGenerators.UV_LOCK));
            this.addSlotStateAndRotationVariants(block, blockModels, multipartgenerator, condition, variantmutator);
        });
        blockModels.blockStateOutput.accept(multipartgenerator);
        blockModels.registerSimpleItemModel(block, ModelLocationUtils.getModelLocation(block, "_inventory"));
        BlockModelGenerators.CHISELED_BOOKSHELF_SLOT_MODEL_CACHE.clear();
    }

    private void addSlotStateAndRotationVariants(Block block, BlockModelGenerators blockModels, MultiPartGenerator generator, Condition condition, VariantMutator rotation) {
        List.of(Pair.of(BlockStateProperties.SLOT_0_OCCUPIED, ModelTemplates.CHISELED_BOOKSHELF_SLOT_TOP_LEFT), Pair.of(BlockStateProperties.SLOT_1_OCCUPIED, ModelTemplates.CHISELED_BOOKSHELF_SLOT_TOP_MID), Pair.of(BlockStateProperties.SLOT_2_OCCUPIED, ModelTemplates.CHISELED_BOOKSHELF_SLOT_TOP_RIGHT), Pair.of(BlockStateProperties.SLOT_3_OCCUPIED, ModelTemplates.CHISELED_BOOKSHELF_SLOT_BOTTOM_LEFT), Pair.of(BlockStateProperties.SLOT_4_OCCUPIED, ModelTemplates.CHISELED_BOOKSHELF_SLOT_BOTTOM_MID), Pair.of(BlockStateProperties.SLOT_5_OCCUPIED, ModelTemplates.CHISELED_BOOKSHELF_SLOT_BOTTOM_RIGHT)).forEach((pair) -> {
            BooleanProperty booleanproperty = pair.getFirst();
            ModelTemplate modeltemplate = pair.getSecond();
            this.addBookSlotModel(block, blockModels, generator, condition, rotation, booleanproperty, modeltemplate, true);
            this.addBookSlotModel(block, blockModels, generator, condition, rotation, booleanproperty, modeltemplate, false);
        });
    }

    private void addBookSlotModel(Block block, BlockModelGenerators blockModels, MultiPartGenerator generator, Condition condition, VariantMutator rotation, BooleanProperty hasBookProperty, ModelTemplate template, boolean hasBook) {
        String suffix = hasBook ? "_occupied" : "_empty";
        TextureMapping texturemapping = new TextureMapping().put(TextureSlot.TEXTURE, TextureMapping.getBlockTexture(block, suffix));
        BlockModelGenerators.BookSlotModelCacheKey blockmodelgenerators$bookslotmodelcachekey = new BlockModelGenerators.BookSlotModelCacheKey(template, suffix);
        MultiVariant multivariant = BlockModelGenerators.plainVariant(BlockModelGenerators.CHISELED_BOOKSHELF_SLOT_MODEL_CACHE.computeIfAbsent(blockmodelgenerators$bookslotmodelcachekey, (cacheKey) -> template.createWithSuffix(Blocks.CHISELED_BOOKSHELF, suffix, texturemapping, blockModels.modelOutput)));
        generator.with(new CombinedCondition(CombinedCondition.Operation.AND, List.of(condition, BlockModelGenerators.condition().term(hasBookProperty, hasBook).build())), multivariant.with(rotation));
    }

    private PhasingWoodProvider phasingWoodProvider(Block block, BlockModelGenerators blockModels) {
        return new PhasingWoodProvider(PhasingTextureMapping.logColumn(block), blockModels);
    }

    private void createPhasingLeaves(Block block, BlockModelGenerators blockModels) {
        PhasingTextureMapping leavesMapping = PhasingTextureMapping.leaves(block);
        Arrays.stream(TimePhase.values()).forEach(phase -> {
            Identifier modelId = Services.MODEL_TEMPLATES.extend(ModelTemplates.LEAVES)
                    .withRenderType(phase == TimePhase.FULL ? CUTOUT_RENDER_TYPE : TRANSLUCENT_RENDER_TYPE)
                    .createWithSuffix(block, "_" + phase, leavesMapping.resolve(phase), blockModels.modelOutput);
            blockModels.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block, BlockModelGenerators.plainVariant(modelId)));
            if (phase == TimePhase.FULL) {
                blockModels.registerSimpleItemModel(block, modelId);
            }
        });
    }
}
