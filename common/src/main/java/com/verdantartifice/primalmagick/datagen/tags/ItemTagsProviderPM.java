package com.verdantartifice.primalmagick.datagen.tags;

import java.util.concurrent.CompletableFuture;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.items.ItemRegistration;
import com.verdantartifice.primalmagick.common.items.armor.WardingModuleItem;
import com.verdantartifice.primalmagick.common.tags.ItemTagsForgeExt;
import com.verdantartifice.primalmagick.common.tags.ItemTagsPM;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

/**
 * Data provider for all of the mod's item tags, both original tags and modifications to vanilla tags.
 * 
 * @author Daedalus4096
 */
public class ItemTagsProviderPM extends ItemTagsProvider {
    public ItemTagsProviderPM(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagsProvider.TagLookup<Block>> blockTagLookup, ExistingFileHelper helper) {
        super(packOutput, lookupProvider, blockTagLookup, Constants.MOD_ID, helper);
    }

    @Override
    public String getName() {
        return "Primal Magick Item Tags";
    }

    @Override
    protected void addTags(HolderLookup.Provider lookupProvider) {
        // Add entries to vanilla tags
        this.tag(ItemTags.ARROWS).add(ItemRegistration.MANA_ARROW_EARTH.get(), ItemRegistration.MANA_ARROW_SEA.get(), ItemRegistration.MANA_ARROW_SKY.get(), ItemRegistration.MANA_ARROW_SUN.get(), ItemRegistration.MANA_ARROW_MOON.get(), ItemRegistration.MANA_ARROW_BLOOD.get(), ItemRegistration.MANA_ARROW_INFERNAL.get(), ItemRegistration.MANA_ARROW_VOID.get(), ItemRegistration.MANA_ARROW_HALLOWED.get());
        this.tag(ItemTags.AXES).add(ItemRegistration.PRIMALITE_AXE.get(), ItemRegistration.HEXIUM_AXE.get(), ItemRegistration.HALLOWSTEEL_AXE.get(), ItemRegistration.PRIMAL_AXE.get());
        this.tag(ItemTags.BEACON_PAYMENT_ITEMS).addTag(ItemTagsPM.INGOTS_PRIMALITE).addTag(ItemTagsPM.INGOTS_HEXIUM).addTag(ItemTagsPM.INGOTS_HALLOWSTEEL);
        this.tag(ItemTags.BOOKSHELF_BOOKS).add(ItemRegistration.GRIMOIRE.get(), ItemRegistration.CREATIVE_GRIMOIRE.get(), ItemRegistration.STATIC_BOOK.get(), ItemRegistration.STATIC_BOOK_UNCOMMON.get(), ItemRegistration.STATIC_BOOK_RARE.get());
        this.tag(ItemTags.CLUSTER_MAX_HARVESTABLES).add(ItemRegistration.PRIMALITE_PICKAXE.get(), ItemRegistration.HEXIUM_PICKAXE.get(), ItemRegistration.HALLOWSTEEL_PICKAXE.get(), ItemRegistration.PRIMAL_PICKAXE.get());
        this.tag(ItemTags.FREEZE_IMMUNE_WEARABLES).addTag(ItemTagsPM.ROBES);
        this.tag(ItemTags.HOES).add(ItemRegistration.PRIMALITE_HOE.get(), ItemRegistration.HEXIUM_HOE.get(), ItemRegistration.HALLOWSTEEL_HOE.get(), ItemRegistration.PRIMAL_HOE.get());
        this.tag(ItemTags.LOGS_THAT_BURN).addTag(ItemTagsPM.MOONWOOD_LOGS).addTag(ItemTagsPM.SUNWOOD_LOGS).addTag(ItemTagsPM.HALLOWOOD_LOGS);
        this.tag(ItemTags.LEAVES).add(ItemRegistration.MOONWOOD_LEAVES.get(), ItemRegistration.SUNWOOD_LEAVES.get(), ItemRegistration.HALLOWOOD_LEAVES.get());
        this.tag(ItemTags.LECTERN_BOOKS).add(Items.ENCHANTED_BOOK, ItemRegistration.GRIMOIRE.get(), ItemRegistration.CREATIVE_GRIMOIRE.get(), ItemRegistration.STATIC_BOOK.get(), ItemRegistration.STATIC_BOOK_UNCOMMON.get(), ItemRegistration.STATIC_BOOK_RARE.get());
        this.tag(ItemTags.MEAT).add(ItemRegistration.SALTED_COOKED_BEEF.get(), ItemRegistration.SALTED_COOKED_CHICKEN.get(), ItemRegistration.SALTED_COOKED_MUTTON.get(), ItemRegistration.SALTED_COOKED_PORKCHOP.get(), ItemRegistration.SALTED_COOKED_RABBIT.get(), ItemRegistration.BLOODY_FLESH.get());
        this.tag(ItemTags.PICKAXES).add(ItemRegistration.PRIMALITE_PICKAXE.get(), ItemRegistration.HEXIUM_PICKAXE.get(), ItemRegistration.HALLOWSTEEL_PICKAXE.get(), ItemRegistration.PRIMAL_PICKAXE.get());
        this.tag(ItemTags.PLANKS).add(ItemRegistration.MOONWOOD_PLANKS.get(), ItemRegistration.SUNWOOD_PLANKS.get(), ItemRegistration.HALLOWOOD_PLANKS.get());
        this.tag(ItemTags.SAPLINGS).add(ItemRegistration.MOONWOOD_SAPLING.get(), ItemRegistration.SUNWOOD_SAPLING.get(), ItemRegistration.HALLOWOOD_SAPLING.get());
        this.tag(ItemTags.SHOVELS).add(ItemRegistration.PRIMALITE_SHOVEL.get(), ItemRegistration.HEXIUM_SHOVEL.get(), ItemRegistration.HALLOWSTEEL_SHOVEL.get(), ItemRegistration.PRIMAL_SHOVEL.get());
        this.tag(ItemTags.SWORDS).add(ItemRegistration.PRIMALITE_SWORD.get(), ItemRegistration.HEXIUM_SWORD.get(), ItemRegistration.HALLOWSTEEL_SWORD.get(), ItemRegistration.FORBIDDEN_SWORD.get());
        this.tag(ItemTags.TRIMMABLE_ARMOR).addTag(ItemTagsPM.RUNIC_TRIMMABLE_ARMOR).add(ItemRegistration.PRIMALITE_CHEST.get(), ItemRegistration.PRIMALITE_FEET.get(), ItemRegistration.PRIMALITE_HEAD.get(), ItemRegistration.PRIMALITE_LEGS.get(), ItemRegistration.HEXIUM_CHEST.get(), ItemRegistration.HEXIUM_FEET.get(), ItemRegistration.HEXIUM_HEAD.get(), ItemRegistration.HEXIUM_LEGS.get(), ItemRegistration.HALLOWSTEEL_CHEST.get(), ItemRegistration.HALLOWSTEEL_FEET.get(), ItemRegistration.HALLOWSTEEL_HEAD.get(), ItemRegistration.HALLOWSTEEL_LEGS.get());
        this.tag(ItemTags.WALLS).add(ItemRegistration.MARBLE_WALL.get(), ItemRegistration.MARBLE_BRICK_WALL.get(), ItemRegistration.MARBLE_ENCHANTED_WALL.get(), ItemRegistration.MARBLE_ENCHANTED_BRICK_WALL.get(), ItemRegistration.MARBLE_SMOKED_WALL.get(), ItemRegistration.MARBLE_SMOKED_BRICK_WALL.get());
        this.tag(ItemTags.WOODEN_SLABS).add(ItemRegistration.MOONWOOD_SLAB.get(), ItemRegistration.SUNWOOD_SLAB.get(), ItemRegistration.HALLOWOOD_SLAB.get());
        this.tag(ItemTags.WOODEN_STAIRS).add(ItemRegistration.MOONWOOD_STAIRS.get(), ItemRegistration.SUNWOOD_STAIRS.get(), ItemRegistration.HALLOWOOD_STAIRS.get());

        this.tag(ItemTags.HEAD_ARMOR).addTag(ItemTagsPM.ROBES_HELMETS).add(ItemRegistration.PRIMALITE_HEAD.get(), ItemRegistration.HEXIUM_HEAD.get(), ItemRegistration.HALLOWSTEEL_HEAD.get());
        this.tag(ItemTags.CHEST_ARMOR).addTag(ItemTagsPM.ROBES_CHESTPLATES).add(ItemRegistration.PRIMALITE_CHEST.get(), ItemRegistration.HEXIUM_CHEST.get(), ItemRegistration.HALLOWSTEEL_CHEST.get());
        this.tag(ItemTags.LEG_ARMOR).addTag(ItemTagsPM.ROBES_LEGGINGS).add(ItemRegistration.PRIMALITE_LEGS.get(), ItemRegistration.HEXIUM_LEGS.get(), ItemRegistration.HALLOWSTEEL_LEGS.get());
        this.tag(ItemTags.FOOT_ARMOR).addTag(ItemTagsPM.ROBES_BOOTS).add(ItemRegistration.PRIMALITE_FEET.get(), ItemRegistration.HEXIUM_FEET.get(), ItemRegistration.HALLOWSTEEL_FEET.get());

        this.tag(ItemTags.BOW_ENCHANTABLE).addTag(Tags.Items.TOOLS_BOWS);
        this.tag(ItemTags.CROSSBOW_ENCHANTABLE).addTag(Tags.Items.TOOLS_CROSSBOWS);
        this.tag(ItemTags.DURABILITY_ENCHANTABLE).addTag(Tags.Items.TOOLS_BOWS).addTag(Tags.Items.TOOLS_CROSSBOWS).addTag(Tags.Items.TOOLS_FISHING_RODS).addTag(Tags.Items.TOOLS_TRIDENTS).addTag(Tags.Items.TOOLS_SHIELDS);
        this.tag(ItemTags.FISHING_ENCHANTABLE).addTag(Tags.Items.TOOLS_FISHING_RODS);
        this.tag(ItemTags.TRIDENT_ENCHANTABLE).addTag(Tags.Items.TOOLS_TRIDENTS);

        // Add entries to Forge tags
        this.tag(Tags.Items.ARMORS_HELMETS).addTag(ItemTagsPM.ROBES_HELMETS).add(ItemRegistration.PRIMALITE_HEAD.get(), ItemRegistration.HEXIUM_HEAD.get(), ItemRegistration.HALLOWSTEEL_HEAD.get());
        this.tag(Tags.Items.ARMORS_CHESTPLATES).addTag(ItemTagsPM.ROBES_CHESTPLATES).add(ItemRegistration.PRIMALITE_CHEST.get(), ItemRegistration.HEXIUM_CHEST.get(), ItemRegistration.HALLOWSTEEL_CHEST.get());
        this.tag(Tags.Items.ARMORS_LEGGINGS).addTag(ItemTagsPM.ROBES_LEGGINGS).add(ItemRegistration.PRIMALITE_LEGS.get(), ItemRegistration.HEXIUM_LEGS.get(), ItemRegistration.HALLOWSTEEL_LEGS.get());
        this.tag(Tags.Items.ARMORS_BOOTS).addTag(ItemTagsPM.ROBES_BOOTS).add(ItemRegistration.PRIMALITE_FEET.get(), ItemRegistration.HEXIUM_FEET.get(), ItemRegistration.HALLOWSTEEL_FEET.get());
        this.tag(Tags.Items.DUSTS).addTag(ItemTagsForgeExt.DUSTS_IRON).addTag(ItemTagsForgeExt.DUSTS_GOLD).addTag(ItemTagsForgeExt.DUSTS_COPPER).addOptionalTag(ItemTagsForgeExt.DUSTS_TIN).addOptionalTag(ItemTagsForgeExt.DUSTS_LEAD).addOptionalTag(ItemTagsForgeExt.DUSTS_SILVER).addOptionalTag(ItemTagsForgeExt.DUSTS_URANIUM);
        this.tag(Tags.Items.INGOTS).addTag(ItemTagsPM.INGOTS_PRIMALITE).addTag(ItemTagsPM.INGOTS_HEXIUM).addTag(ItemTagsPM.INGOTS_HALLOWSTEEL);
        this.tag(Tags.Items.NUGGETS).addTag(ItemTagsPM.NUGGETS_PRIMALITE).addTag(ItemTagsPM.NUGGETS_HEXIUM).addTag(ItemTagsPM.NUGGETS_HALLOWSTEEL).addTag(ItemTagsForgeExt.NUGGETS_QUARTZ);
        this.tag(Tags.Items.ORE_RATES_DENSE).add(ItemRegistration.ROCK_SALT_ORE.get());
        this.tag(Tags.Items.ORE_RATES_SINGULAR).add(ItemRegistration.QUARTZ_ORE.get());
        this.tag(Tags.Items.ORES).addTag(ItemTagsForgeExt.ORES_ROCK_SALT).addOptionalTag(ItemTagsForgeExt.ORES_TIN).addOptionalTag(ItemTagsForgeExt.ORES_LEAD).addOptionalTag(ItemTagsForgeExt.ORES_SILVER).addOptionalTag(ItemTagsForgeExt.ORES_URANIUM);
        this.tag(Tags.Items.ORES_QUARTZ).add(ItemRegistration.QUARTZ_ORE.get());
        this.tag(Tags.Items.ORES_IN_GROUND_STONE).add(ItemRegistration.QUARTZ_ORE.get(), ItemRegistration.ROCK_SALT_ORE.get());
        this.tag(Tags.Items.RAW_MATERIALS).addOptionalTag(ItemTagsForgeExt.RAW_MATERIALS_TIN).addOptionalTag(ItemTagsForgeExt.RAW_MATERIALS_LEAD).addOptionalTag(ItemTagsForgeExt.RAW_MATERIALS_SILVER).addOptionalTag(ItemTagsForgeExt.RAW_MATERIALS_URANIUM);
        this.tag(Tags.Items.STORAGE_BLOCKS).add(ItemRegistration.IGNYX_BLOCK.get()).addTag(ItemTagsPM.STORAGE_BLOCKS_PRIMALITE).addTag(ItemTagsPM.STORAGE_BLOCKS_HEXIUM).addTag(ItemTagsPM.STORAGE_BLOCKS_HALLOWSTEEL);
        this.tag(Tags.Items.TOOLS_BOWS).add(ItemRegistration.PRIMALITE_BOW.get(), ItemRegistration.HEXIUM_BOW.get(), ItemRegistration.HALLOWSTEEL_BOW.get(), ItemRegistration.FORBIDDEN_BOW.get());
        this.tag(Tags.Items.TOOLS_FISHING_RODS).add(ItemRegistration.PRIMALITE_FISHING_ROD.get(), ItemRegistration.HEXIUM_FISHING_ROD.get(), ItemRegistration.HALLOWSTEEL_FISHING_ROD.get(), ItemRegistration.PRIMAL_FISHING_ROD.get());
        this.tag(Tags.Items.TOOLS_SHIELDS).add(ItemRegistration.PRIMALITE_SHIELD.get(), ItemRegistration.HEXIUM_SHIELD.get(), ItemRegistration.HALLOWSTEEL_SHIELD.get(), ItemRegistration.SACRED_SHIELD.get());
        this.tag(Tags.Items.TOOLS_TRIDENTS).add(ItemRegistration.PRIMALITE_TRIDENT.get(), ItemRegistration.HEXIUM_TRIDENT.get(), ItemRegistration.HALLOWSTEEL_TRIDENT.get(), ItemRegistration.FORBIDDEN_TRIDENT.get());
        
        this.tag(Tags.Items.GLASS_COLORLESS).add(ItemRegistration.SKYGLASS.get());
        this.tag(Tags.Items.GLASS_BLACK).add(ItemRegistration.STAINED_SKYGLASS_BLACK.get());
        this.tag(Tags.Items.GLASS_BLUE).add(ItemRegistration.STAINED_SKYGLASS_BLUE.get());
        this.tag(Tags.Items.GLASS_BROWN).add(ItemRegistration.STAINED_SKYGLASS_BROWN.get());
        this.tag(Tags.Items.GLASS_CYAN).add(ItemRegistration.STAINED_SKYGLASS_CYAN.get());
        this.tag(Tags.Items.GLASS_GRAY).add(ItemRegistration.STAINED_SKYGLASS_GRAY.get());
        this.tag(Tags.Items.GLASS_GREEN).add(ItemRegistration.STAINED_SKYGLASS_GREEN.get());
        this.tag(Tags.Items.GLASS_LIGHT_BLUE).add(ItemRegistration.STAINED_SKYGLASS_LIGHT_BLUE.get());
        this.tag(Tags.Items.GLASS_LIGHT_GRAY).add(ItemRegistration.STAINED_SKYGLASS_LIGHT_GRAY.get());
        this.tag(Tags.Items.GLASS_LIME).add(ItemRegistration.STAINED_SKYGLASS_LIME.get());
        this.tag(Tags.Items.GLASS_MAGENTA).add(ItemRegistration.STAINED_SKYGLASS_MAGENTA.get());
        this.tag(Tags.Items.GLASS_ORANGE).add(ItemRegistration.STAINED_SKYGLASS_ORANGE.get());
        this.tag(Tags.Items.GLASS_PINK).add(ItemRegistration.STAINED_SKYGLASS_PINK.get());
        this.tag(Tags.Items.GLASS_PURPLE).add(ItemRegistration.STAINED_SKYGLASS_PURPLE.get());
        this.tag(Tags.Items.GLASS_RED).add(ItemRegistration.STAINED_SKYGLASS_RED.get());
        this.tag(Tags.Items.GLASS_WHITE).add(ItemRegistration.STAINED_SKYGLASS_WHITE.get());
        this.tag(Tags.Items.GLASS_YELLOW).add(ItemRegistration.STAINED_SKYGLASS_YELLOW.get());
        this.tag(Tags.Items.STAINED_GLASS).addTag(ItemTagsPM.STAINED_SKYGLASS);
        
        this.tag(Tags.Items.GLASS_PANES_COLORLESS).add(ItemRegistration.SKYGLASS_PANE.get());
        this.tag(Tags.Items.GLASS_PANES_BLACK).add(ItemRegistration.STAINED_SKYGLASS_PANE_BLACK.get());
        this.tag(Tags.Items.GLASS_PANES_BLUE).add(ItemRegistration.STAINED_SKYGLASS_PANE_BLUE.get());
        this.tag(Tags.Items.GLASS_PANES_BROWN).add(ItemRegistration.STAINED_SKYGLASS_PANE_BROWN.get());
        this.tag(Tags.Items.GLASS_PANES_CYAN).add(ItemRegistration.STAINED_SKYGLASS_PANE_CYAN.get());
        this.tag(Tags.Items.GLASS_PANES_GRAY).add(ItemRegistration.STAINED_SKYGLASS_PANE_GRAY.get());
        this.tag(Tags.Items.GLASS_PANES_GREEN).add(ItemRegistration.STAINED_SKYGLASS_PANE_GREEN.get());
        this.tag(Tags.Items.GLASS_PANES_LIGHT_BLUE).add(ItemRegistration.STAINED_SKYGLASS_PANE_LIGHT_BLUE.get());
        this.tag(Tags.Items.GLASS_PANES_LIGHT_GRAY).add(ItemRegistration.STAINED_SKYGLASS_PANE_LIGHT_GRAY.get());
        this.tag(Tags.Items.GLASS_PANES_LIME).add(ItemRegistration.STAINED_SKYGLASS_PANE_LIME.get());
        this.tag(Tags.Items.GLASS_PANES_MAGENTA).add(ItemRegistration.STAINED_SKYGLASS_PANE_MAGENTA.get());
        this.tag(Tags.Items.GLASS_PANES_ORANGE).add(ItemRegistration.STAINED_SKYGLASS_PANE_ORANGE.get());
        this.tag(Tags.Items.GLASS_PANES_PINK).add(ItemRegistration.STAINED_SKYGLASS_PANE_PINK.get());
        this.tag(Tags.Items.GLASS_PANES_PURPLE).add(ItemRegistration.STAINED_SKYGLASS_PANE_PURPLE.get());
        this.tag(Tags.Items.GLASS_PANES_RED).add(ItemRegistration.STAINED_SKYGLASS_PANE_RED.get());
        this.tag(Tags.Items.GLASS_PANES_WHITE).add(ItemRegistration.STAINED_SKYGLASS_PANE_WHITE.get());
        this.tag(Tags.Items.GLASS_PANES_YELLOW).add(ItemRegistration.STAINED_SKYGLASS_PANE_YELLOW.get());
        this.tag(Tags.Items.STAINED_GLASS_PANES).addTag(ItemTagsPM.STAINED_SKYGLASS_PANES);

        // Add entries to Forge extension tags
        this.tag(ItemTagsForgeExt.DUSTS_COPPER).add(ItemRegistration.COPPER_GRIT.get());
        this.tag(ItemTagsForgeExt.DUSTS_GOLD).add(ItemRegistration.GOLD_GRIT.get());
        this.tag(ItemTagsForgeExt.DUSTS_IRON).add(ItemRegistration.IRON_GRIT.get());
        this.tag(ItemTagsForgeExt.MILK).add(Items.MILK_BUCKET);
        this.tag(ItemTagsForgeExt.NUGGETS_QUARTZ).add(ItemRegistration.QUARTZ_NUGGET.get());
        this.tag(ItemTagsForgeExt.ORES_ROCK_SALT).add(ItemRegistration.ROCK_SALT_ORE.get());

        // Create custom tags
        this.tag(ItemTagsPM.ANALYSIS_TABLE_FORBIDDEN).add(Items.DRAGON_EGG, ItemRegistration.HALLOWED_ORB.get());
        this.tag(ItemTagsPM.COLORED_SHULKER_BOXES).add(Items.BLACK_SHULKER_BOX, Items.BLUE_SHULKER_BOX, Items.BROWN_SHULKER_BOX, Items.CYAN_SHULKER_BOX, Items.GRAY_SHULKER_BOX, Items.GREEN_SHULKER_BOX, Items.LIGHT_BLUE_SHULKER_BOX, Items.LIGHT_GRAY_SHULKER_BOX, Items.LIME_SHULKER_BOX, Items.MAGENTA_SHULKER_BOX, Items.ORANGE_SHULKER_BOX, Items.PINK_SHULKER_BOX, Items.PURPLE_SHULKER_BOX, Items.RED_SHULKER_BOX, Items.WHITE_SHULKER_BOX, Items.YELLOW_SHULKER_BOX);
        this.tag(ItemTagsPM.CONCRETE).add(Items.BLACK_CONCRETE, Items.BLUE_CONCRETE, Items.BROWN_CONCRETE, Items.CYAN_CONCRETE, Items.GRAY_CONCRETE, Items.GREEN_CONCRETE, Items.LIGHT_BLUE_CONCRETE, Items.LIGHT_GRAY_CONCRETE, Items.LIME_CONCRETE, Items.MAGENTA_CONCRETE, Items.ORANGE_CONCRETE, Items.PINK_CONCRETE, Items.PURPLE_CONCRETE, Items.RED_CONCRETE, Items.WHITE_CONCRETE, Items.YELLOW_CONCRETE);
        this.tag(ItemTagsPM.CORAL_BLOCKS).add(Items.BRAIN_CORAL_BLOCK, Items.BUBBLE_CORAL_BLOCK, Items.FIRE_CORAL_BLOCK, Items.HORN_CORAL_BLOCK, Items.TUBE_CORAL_BLOCK);
        this.tag(ItemTagsPM.DEAD_CORAL_BLOCKS).add(Items.DEAD_BRAIN_CORAL_BLOCK, Items.DEAD_BUBBLE_CORAL_BLOCK, Items.DEAD_FIRE_CORAL_BLOCK, Items.DEAD_HORN_CORAL_BLOCK, Items.DEAD_TUBE_CORAL_BLOCK);
        this.tag(ItemTagsPM.DEAD_CORAL_PLANTS).add(Items.DEAD_BRAIN_CORAL, Items.DEAD_BUBBLE_CORAL, Items.DEAD_FIRE_CORAL, Items.DEAD_HORN_CORAL, Items.DEAD_TUBE_CORAL);
        this.tag(ItemTagsPM.DEAD_CORALS).addTag(ItemTagsPM.DEAD_CORAL_PLANTS).add(Items.DEAD_BRAIN_CORAL_FAN, Items.DEAD_BUBBLE_CORAL_FAN, Items.DEAD_FIRE_CORAL_FAN, Items.DEAD_HORN_CORAL_FAN, Items.DEAD_TUBE_CORAL_FAN);
        this.tag(ItemTagsPM.DEEP_STONE).add(Items.DEEPSLATE, Items.POLISHED_DEEPSLATE, Items.INFESTED_DEEPSLATE, Items.TUFF);
        this.tag(ItemTagsPM.ENCHANTING_TABLES).add(Items.ENCHANTING_TABLE).addOptional(ResourceLocation.fromNamespaceAndPath("quark", "matrix_enchanter"));
        this.tag(ItemTagsPM.ESSENCES).addTag(ItemTagsPM.ESSENCES_DUSTS).addTag(ItemTagsPM.ESSENCES_SHARDS).addTag(ItemTagsPM.ESSENCES_CRYSTALS).addTag(ItemTagsPM.ESSENCES_CLUSTERS);
        this.tag(ItemTagsPM.HALLOWOOD_LOGS).add(ItemRegistration.HALLOWOOD_LOG.get(), ItemRegistration.STRIPPED_HALLOWOOD_LOG.get(), ItemRegistration.HALLOWOOD_WOOD.get(), ItemRegistration.STRIPPED_HALLOWOOD_WOOD.get());
        this.tag(ItemTagsPM.INFERNAL_SUPERCHARGE_FUEL).add(ItemRegistration.IGNYX.get(), ItemRegistration.IGNYX_BLOCK.get());
        this.tag(ItemTagsPM.MAGICKAL_CLOTH).add(ItemRegistration.SPELLCLOTH.get(), ItemRegistration.HEXWEAVE.get(), ItemRegistration.SAINTSWOOL.get());
        this.tag(ItemTagsPM.MOONWOOD_LOGS).add(ItemRegistration.MOONWOOD_LOG.get(), ItemRegistration.STRIPPED_MOONWOOD_LOG.get(), ItemRegistration.MOONWOOD_WOOD.get(), ItemRegistration.STRIPPED_MOONWOOD_WOOD.get());
        this.tag(ItemTagsPM.RITUAL_CANDLES).add(ItemRegistration.RITUAL_CANDLE_BLACK.get(), ItemRegistration.RITUAL_CANDLE_BLUE.get(), ItemRegistration.RITUAL_CANDLE_BROWN.get(), ItemRegistration.RITUAL_CANDLE_CYAN.get(), ItemRegistration.RITUAL_CANDLE_GRAY.get(), ItemRegistration.RITUAL_CANDLE_GREEN.get(), ItemRegistration.RITUAL_CANDLE_LIGHT_BLUE.get(), ItemRegistration.RITUAL_CANDLE_LIGHT_GRAY.get(), ItemRegistration.RITUAL_CANDLE_LIME.get(), ItemRegistration.RITUAL_CANDLE_MAGENTA.get(), ItemRegistration.RITUAL_CANDLE_ORANGE.get(), ItemRegistration.RITUAL_CANDLE_PINK.get(), ItemRegistration.RITUAL_CANDLE_PURPLE.get(), ItemRegistration.RITUAL_CANDLE_RED.get(), ItemRegistration.RITUAL_CANDLE_WHITE.get(), ItemRegistration.RITUAL_CANDLE_YELLOW.get());
        this.tag(ItemTagsPM.RUNE_BASES).add(Items.STONE_SLAB);
        this.tag(ItemTagsPM.RUNE_ETCHINGS).addTag(Tags.Items.GEMS_LAPIS);
        this.tag(ItemTagsPM.RUNIC_TRIMMABLE_ARMOR).addTag(ItemTagsPM.ROBES);
        this.tag(ItemTagsPM.RUNIC_TRIM_MATERIALS).add(ItemRegistration.RUNE_EARTH.get(), ItemRegistration.RUNE_SEA.get(), ItemRegistration.RUNE_SKY.get(), ItemRegistration.RUNE_SUN.get(), ItemRegistration.RUNE_MOON.get(), ItemRegistration.RUNE_BLOOD.get(), ItemRegistration.RUNE_INFERNAL.get(), ItemRegistration.RUNE_VOID.get(), ItemRegistration.RUNE_HALLOWED.get());
        this.tag(ItemTagsPM.SHULKER_BOXES).addTag(ItemTagsPM.COLORED_SHULKER_BOXES).add(Items.SHULKER_BOX);
        this.tag(ItemTagsPM.SKYGLASS).add(ItemRegistration.SKYGLASS.get()).addTag(ItemTagsPM.STAINED_SKYGLASS);
        this.tag(ItemTagsPM.SKYGLASS_PANES).add(ItemRegistration.SKYGLASS_PANE.get()).addTag(ItemTagsPM.STAINED_SKYGLASS_PANES);
        this.tag(ItemTagsPM.STAINED_SKYGLASS).add(ItemRegistration.STAINED_SKYGLASS_BLACK.get(), ItemRegistration.STAINED_SKYGLASS_BLUE.get(), ItemRegistration.STAINED_SKYGLASS_BROWN.get(), ItemRegistration.STAINED_SKYGLASS_CYAN.get(), ItemRegistration.STAINED_SKYGLASS_GRAY.get(), ItemRegistration.STAINED_SKYGLASS_GREEN.get(), ItemRegistration.STAINED_SKYGLASS_LIGHT_BLUE.get(), ItemRegistration.STAINED_SKYGLASS_LIGHT_GRAY.get(), ItemRegistration.STAINED_SKYGLASS_LIME.get(), ItemRegistration.STAINED_SKYGLASS_MAGENTA.get(), ItemRegistration.STAINED_SKYGLASS_ORANGE.get(), ItemRegistration.STAINED_SKYGLASS_PINK.get(), ItemRegistration.STAINED_SKYGLASS_PURPLE.get(), ItemRegistration.STAINED_SKYGLASS_RED.get(), ItemRegistration.STAINED_SKYGLASS_WHITE.get(), ItemRegistration.STAINED_SKYGLASS_YELLOW.get());
        this.tag(ItemTagsPM.STAINED_SKYGLASS_PANES).add(ItemRegistration.STAINED_SKYGLASS_PANE_BLACK.get(), ItemRegistration.STAINED_SKYGLASS_PANE_BLUE.get(), ItemRegistration.STAINED_SKYGLASS_PANE_BROWN.get(), ItemRegistration.STAINED_SKYGLASS_PANE_CYAN.get(), ItemRegistration.STAINED_SKYGLASS_PANE_GRAY.get(), ItemRegistration.STAINED_SKYGLASS_PANE_GREEN.get(), ItemRegistration.STAINED_SKYGLASS_PANE_LIGHT_BLUE.get(), ItemRegistration.STAINED_SKYGLASS_PANE_LIGHT_GRAY.get(), ItemRegistration.STAINED_SKYGLASS_PANE_LIME.get(), ItemRegistration.STAINED_SKYGLASS_PANE_MAGENTA.get(), ItemRegistration.STAINED_SKYGLASS_PANE_ORANGE.get(), ItemRegistration.STAINED_SKYGLASS_PANE_PINK.get(), ItemRegistration.STAINED_SKYGLASS_PANE_PURPLE.get(), ItemRegistration.STAINED_SKYGLASS_PANE_RED.get(), ItemRegistration.STAINED_SKYGLASS_PANE_WHITE.get(), ItemRegistration.STAINED_SKYGLASS_PANE_YELLOW.get());
        this.tag(ItemTagsPM.STATIC_BOOKS).add(ItemRegistration.STATIC_BOOK.get(), ItemRegistration.STATIC_BOOK_UNCOMMON.get(), ItemRegistration.STATIC_BOOK_RARE.get(), ItemRegistration.STATIC_TABLET.get());
        this.tag(ItemTagsPM.SUNWOOD_LOGS).add(ItemRegistration.SUNWOOD_LOG.get(), ItemRegistration.STRIPPED_SUNWOOD_LOG.get(), ItemRegistration.SUNWOOD_WOOD.get(), ItemRegistration.STRIPPED_SUNWOOD_WOOD.get());
        this.tag(ItemTagsPM.SURFACE_STONE).add(Items.ANDESITE, Items.DIORITE, Items.GRANITE, Items.INFESTED_STONE, Items.STONE, Items.POLISHED_ANDESITE, Items.POLISHED_DIORITE, Items.POLISHED_GRANITE);
        this.tag(ItemTagsPM.TREEFOLK_LOVED).add(Items.BONE_MEAL);
        
        // FIXME Use the WARDABLE_ARMOR tag as the source of truth if/when the RegisterItemDecorationsEvent is made to fire *after* tag data loads
        var wardableArmorTag = this.tag(ItemTagsPM.WARDABLE_ARMOR);
        WardingModuleItem.getApplicableItems().forEach(itemSupplier -> wardableArmorTag.add(itemSupplier.get()));
        
        this.tag(ItemTagsPM.FOOD_BAKED_POTATO).add(Items.BAKED_POTATO, ItemRegistration.SALTED_BAKED_POTATO.get());
        this.tag(ItemTagsPM.FOOD_COOKED_BEEF).add(Items.COOKED_BEEF, ItemRegistration.SALTED_COOKED_BEEF.get());
        
        this.tag(ItemTagsPM.ROBES).addTag(ItemTagsPM.ROBES_HELMETS).addTag(ItemTagsPM.ROBES_CHESTPLATES).addTag(ItemTagsPM.ROBES_LEGGINGS).addTag(ItemTagsPM.ROBES_BOOTS);
        this.tag(ItemTagsPM.ROBES_HELMETS).add(ItemRegistration.IMBUED_WOOL_HEAD.get(), ItemRegistration.SPELLCLOTH_HEAD.get(), ItemRegistration.HEXWEAVE_HEAD.get(), ItemRegistration.SAINTSWOOL_HEAD.get());
        this.tag(ItemTagsPM.ROBES_CHESTPLATES).add(ItemRegistration.IMBUED_WOOL_CHEST.get(), ItemRegistration.SPELLCLOTH_CHEST.get(), ItemRegistration.HEXWEAVE_CHEST.get(), ItemRegistration.SAINTSWOOL_CHEST.get());
        this.tag(ItemTagsPM.ROBES_LEGGINGS).add(ItemRegistration.IMBUED_WOOL_LEGS.get(), ItemRegistration.SPELLCLOTH_LEGS.get(), ItemRegistration.HEXWEAVE_LEGS.get(), ItemRegistration.SAINTSWOOL_LEGS.get());
        this.tag(ItemTagsPM.ROBES_BOOTS).add(ItemRegistration.IMBUED_WOOL_FEET.get(), ItemRegistration.SPELLCLOTH_FEET.get(), ItemRegistration.HEXWEAVE_FEET.get(), ItemRegistration.SAINTSWOOL_FEET.get());
        
        this.tag(ItemTagsPM.ESSENCES_DUSTS).addTag(ItemTagsPM.ESSENCES_TERRESTRIAL_DUSTS).addTag(ItemTagsPM.ESSENCES_FORBIDDEN_DUSTS).add(ItemRegistration.ESSENCE_DUST_HALLOWED.get());
        this.tag(ItemTagsPM.ESSENCES_TERRESTRIAL_DUSTS).add(ItemRegistration.ESSENCE_DUST_EARTH.get(), ItemRegistration.ESSENCE_DUST_SEA.get(), ItemRegistration.ESSENCE_DUST_SKY.get(), ItemRegistration.ESSENCE_DUST_SUN.get(), ItemRegistration.ESSENCE_DUST_MOON.get());
        this.tag(ItemTagsPM.ESSENCES_FORBIDDEN_DUSTS).add(ItemRegistration.ESSENCE_DUST_BLOOD.get(), ItemRegistration.ESSENCE_DUST_INFERNAL.get(), ItemRegistration.ESSENCE_DUST_VOID.get());
        this.tag(ItemTagsPM.ESSENCES_SHARDS).addTag(ItemTagsPM.ESSENCES_TERRESTRIAL_SHARDS).addTag(ItemTagsPM.ESSENCES_FORBIDDEN_SHARDS).add(ItemRegistration.ESSENCE_SHARD_HALLOWED.get());
        this.tag(ItemTagsPM.ESSENCES_TERRESTRIAL_SHARDS).add(ItemRegistration.ESSENCE_SHARD_EARTH.get(), ItemRegistration.ESSENCE_SHARD_SEA.get(), ItemRegistration.ESSENCE_SHARD_SKY.get(), ItemRegistration.ESSENCE_SHARD_SUN.get(), ItemRegistration.ESSENCE_SHARD_MOON.get());
        this.tag(ItemTagsPM.ESSENCES_FORBIDDEN_SHARDS).add(ItemRegistration.ESSENCE_SHARD_BLOOD.get(), ItemRegistration.ESSENCE_SHARD_INFERNAL.get(), ItemRegistration.ESSENCE_SHARD_VOID.get());
        this.tag(ItemTagsPM.ESSENCES_CRYSTALS).addTag(ItemTagsPM.ESSENCES_TERRESTRIAL_CRYSTALS).addTag(ItemTagsPM.ESSENCES_FORBIDDEN_CRYSTALS).add(ItemRegistration.ESSENCE_CRYSTAL_HALLOWED.get());
        this.tag(ItemTagsPM.ESSENCES_TERRESTRIAL_CRYSTALS).add(ItemRegistration.ESSENCE_CRYSTAL_EARTH.get(), ItemRegistration.ESSENCE_CRYSTAL_SEA.get(), ItemRegistration.ESSENCE_CRYSTAL_SKY.get(), ItemRegistration.ESSENCE_CRYSTAL_SUN.get(), ItemRegistration.ESSENCE_CRYSTAL_MOON.get());
        this.tag(ItemTagsPM.ESSENCES_FORBIDDEN_CRYSTALS).add(ItemRegistration.ESSENCE_CRYSTAL_BLOOD.get(), ItemRegistration.ESSENCE_CRYSTAL_INFERNAL.get(), ItemRegistration.ESSENCE_CRYSTAL_VOID.get());
        this.tag(ItemTagsPM.ESSENCES_CLUSTERS).addTag(ItemTagsPM.ESSENCES_TERRESTRIAL_CLUSTERS).addTag(ItemTagsPM.ESSENCES_FORBIDDEN_CLUSTERS).add(ItemRegistration.ESSENCE_CLUSTER_HALLOWED.get());
        this.tag(ItemTagsPM.ESSENCES_TERRESTRIAL_CLUSTERS).add(ItemRegistration.ESSENCE_CLUSTER_EARTH.get(), ItemRegistration.ESSENCE_CLUSTER_SEA.get(), ItemRegistration.ESSENCE_CLUSTER_SKY.get(), ItemRegistration.ESSENCE_CLUSTER_SUN.get(), ItemRegistration.ESSENCE_CLUSTER_MOON.get());
        this.tag(ItemTagsPM.ESSENCES_FORBIDDEN_CLUSTERS).add(ItemRegistration.ESSENCE_CLUSTER_BLOOD.get(), ItemRegistration.ESSENCE_CLUSTER_INFERNAL.get(), ItemRegistration.ESSENCE_CLUSTER_VOID.get());

        this.tag(ItemTagsPM.INGOTS_HALLOWSTEEL).add(ItemRegistration.HALLOWSTEEL_INGOT.get());
        this.tag(ItemTagsPM.INGOTS_HEXIUM).add(ItemRegistration.HEXIUM_INGOT.get());
        this.tag(ItemTagsPM.INGOTS_PRIMALITE).add(ItemRegistration.PRIMALITE_INGOT.get());
        this.tag(ItemTagsPM.NUGGETS_HALLOWSTEEL).add(ItemRegistration.HALLOWSTEEL_NUGGET.get());
        this.tag(ItemTagsPM.NUGGETS_HEXIUM).add(ItemRegistration.HEXIUM_NUGGET.get());
        this.tag(ItemTagsPM.NUGGETS_PRIMALITE).add(ItemRegistration.PRIMALITE_NUGGET.get());
        this.tag(ItemTagsPM.STORAGE_BLOCKS_HALLOWSTEEL).add(ItemRegistration.HALLOWSTEEL_BLOCK.get());
        this.tag(ItemTagsPM.STORAGE_BLOCKS_HEXIUM).add(ItemRegistration.HEXIUM_BLOCK.get());
        this.tag(ItemTagsPM.STORAGE_BLOCKS_PRIMALITE).add(ItemRegistration.PRIMALITE_BLOCK.get());
        
        this.tag(ItemTagsPM.MELEE_ENCHANTABLE).addTag(ItemTags.WEAPON_ENCHANTABLE).addTag(ItemTags.TRIDENT_ENCHANTABLE).addTag(ItemTagsPM.STAFF_ENCHANTABLE);
        this.tag(ItemTagsPM.ARCHERY_ENCHANTABLE).addTag(ItemTags.BOW_ENCHANTABLE).addTag(ItemTags.CROSSBOW_ENCHANTABLE);
        this.tag(ItemTagsPM.SHIELD_ENCHANTABLE).addTag(Tags.Items.TOOLS_SHIELDS);
        this.tag(ItemTagsPM.STAFF_ENCHANTABLE).add(ItemRegistration.MODULAR_STAFF.get());
        this.tag(ItemTagsPM.WAND_ENCHANTABLE).addTag(ItemTagsPM.STAFF_ENCHANTABLE).add(ItemRegistration.MUNDANE_WAND.get(), ItemRegistration.MODULAR_WAND.get());
        this.tag(ItemTagsPM.HOE_ENCHANTABLE).addTag(ItemTags.HOES);
        this.tag(ItemTagsPM.BOUNTY_ENCHANTABLE).addTag(ItemTags.FISHING_ENCHANTABLE).addTag(ItemTagsPM.HOE_ENCHANTABLE);
        this.tag(ItemTagsPM.RENDING_ENCHANTABLE).addTag(ItemTags.SHARP_WEAPON_ENCHANTABLE).addTag(ItemTags.TRIDENT_ENCHANTABLE);
        this.tag(ItemTagsPM.ESSENCE_THIEF_ENCHANTABLE).addTag(ItemTags.WEAPON_ENCHANTABLE).addTag(ItemTags.TRIDENT_ENCHANTABLE).addTag(ItemTagsPM.WAND_ENCHANTABLE);
    }
}
