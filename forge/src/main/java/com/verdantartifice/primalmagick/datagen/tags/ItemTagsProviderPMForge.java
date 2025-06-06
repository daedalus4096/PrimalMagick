package com.verdantartifice.primalmagick.datagen.tags;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.items.armor.WardingModuleItem;
import com.verdantartifice.primalmagick.common.tags.CommonTags;
import com.verdantartifice.primalmagick.common.tags.ItemExtensionTags;
import com.verdantartifice.primalmagick.common.tags.ItemTagsPM;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

/**
 * Data provider for all of the mod's item tags, both original tags and modifications to vanilla tags.
 * 
 * @author Daedalus4096
 */
public class ItemTagsProviderPMForge extends ItemTagsProvider {
    public ItemTagsProviderPMForge(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagsProvider.TagLookup<Block>> blockTagLookup, ExistingFileHelper helper) {
        super(packOutput, lookupProvider, blockTagLookup, Constants.MOD_ID, helper);
    }

    @Override
    public String getName() {
        return "Primal Magick Item Tags";
    }

    @Override
    protected void addTags(HolderLookup.Provider lookupProvider) {
        // Add entries to vanilla tags
        this.tag(ItemTags.ARROWS).add(ItemsPM.MANA_ARROW_EARTH.get(), ItemsPM.MANA_ARROW_SEA.get(), ItemsPM.MANA_ARROW_SKY.get(), ItemsPM.MANA_ARROW_SUN.get(), ItemsPM.MANA_ARROW_MOON.get(), ItemsPM.MANA_ARROW_BLOOD.get(), ItemsPM.MANA_ARROW_INFERNAL.get(), ItemsPM.MANA_ARROW_VOID.get(), ItemsPM.MANA_ARROW_HALLOWED.get());
        this.tag(ItemTags.AXES).add(ItemsPM.PRIMALITE_AXE.get(), ItemsPM.HEXIUM_AXE.get(), ItemsPM.HALLOWSTEEL_AXE.get(), ItemsPM.PRIMAL_AXE.get());
        this.tag(ItemTags.BEACON_PAYMENT_ITEMS).addTag(ItemTagsPM.INGOTS_PRIMALITE).addTag(ItemTagsPM.INGOTS_HEXIUM).addTag(ItemTagsPM.INGOTS_HALLOWSTEEL);
        this.tag(ItemTags.BOOKSHELF_BOOKS).add(ItemsPM.GRIMOIRE.get(), ItemsPM.CREATIVE_GRIMOIRE.get(), ItemsPM.STATIC_BOOK.get(), ItemsPM.STATIC_BOOK_UNCOMMON.get(), ItemsPM.STATIC_BOOK_RARE.get());
        this.tag(ItemTags.CLUSTER_MAX_HARVESTABLES).add(ItemsPM.PRIMALITE_PICKAXE.get(), ItemsPM.HEXIUM_PICKAXE.get(), ItemsPM.HALLOWSTEEL_PICKAXE.get(), ItemsPM.PRIMAL_PICKAXE.get());
        this.tag(ItemTags.FREEZE_IMMUNE_WEARABLES).addTag(ItemTagsPM.ROBES);
        this.tag(ItemTags.HOES).add(ItemsPM.PRIMALITE_HOE.get(), ItemsPM.HEXIUM_HOE.get(), ItemsPM.HALLOWSTEEL_HOE.get(), ItemsPM.PRIMAL_HOE.get());
        this.tag(ItemTags.LOGS_THAT_BURN).addTag(ItemTagsPM.MOONWOOD_LOGS).addTag(ItemTagsPM.SUNWOOD_LOGS).addTag(ItemTagsPM.HALLOWOOD_LOGS);
        this.tag(ItemTags.LEAVES).add(ItemsPM.MOONWOOD_LEAVES.get(), ItemsPM.SUNWOOD_LEAVES.get(), ItemsPM.HALLOWOOD_LEAVES.get());
        this.tag(ItemTags.LECTERN_BOOKS).add(Items.ENCHANTED_BOOK, ItemsPM.GRIMOIRE.get(), ItemsPM.CREATIVE_GRIMOIRE.get(), ItemsPM.STATIC_BOOK.get(), ItemsPM.STATIC_BOOK_UNCOMMON.get(), ItemsPM.STATIC_BOOK_RARE.get());
        this.tag(ItemTags.MEAT).add(ItemsPM.SALTED_COOKED_BEEF.get(), ItemsPM.SALTED_COOKED_CHICKEN.get(), ItemsPM.SALTED_COOKED_MUTTON.get(), ItemsPM.SALTED_COOKED_PORKCHOP.get(), ItemsPM.SALTED_COOKED_RABBIT.get(), ItemsPM.BLOODY_FLESH.get());
        this.tag(ItemTags.PICKAXES).add(ItemsPM.PRIMALITE_PICKAXE.get(), ItemsPM.HEXIUM_PICKAXE.get(), ItemsPM.HALLOWSTEEL_PICKAXE.get(), ItemsPM.PRIMAL_PICKAXE.get());
        this.tag(ItemTags.PLANKS).add(ItemsPM.MOONWOOD_PLANKS.get(), ItemsPM.SUNWOOD_PLANKS.get(), ItemsPM.HALLOWOOD_PLANKS.get());
        this.tag(ItemTags.SAPLINGS).add(ItemsPM.MOONWOOD_SAPLING.get(), ItemsPM.SUNWOOD_SAPLING.get(), ItemsPM.HALLOWOOD_SAPLING.get());
        this.tag(ItemTags.SHOVELS).add(ItemsPM.PRIMALITE_SHOVEL.get(), ItemsPM.HEXIUM_SHOVEL.get(), ItemsPM.HALLOWSTEEL_SHOVEL.get(), ItemsPM.PRIMAL_SHOVEL.get());
        this.tag(ItemTags.SWORDS).add(ItemsPM.PRIMALITE_SWORD.get(), ItemsPM.HEXIUM_SWORD.get(), ItemsPM.HALLOWSTEEL_SWORD.get(), ItemsPM.FORBIDDEN_SWORD.get());
        this.tag(ItemTags.TRIMMABLE_ARMOR).addTag(ItemTagsPM.RUNIC_TRIMMABLE_ARMOR).add(ItemsPM.PRIMALITE_CHEST.get(), ItemsPM.PRIMALITE_FEET.get(), ItemsPM.PRIMALITE_HEAD.get(), ItemsPM.PRIMALITE_LEGS.get(), ItemsPM.HEXIUM_CHEST.get(), ItemsPM.HEXIUM_FEET.get(), ItemsPM.HEXIUM_HEAD.get(), ItemsPM.HEXIUM_LEGS.get(), ItemsPM.HALLOWSTEEL_CHEST.get(), ItemsPM.HALLOWSTEEL_FEET.get(), ItemsPM.HALLOWSTEEL_HEAD.get(), ItemsPM.HALLOWSTEEL_LEGS.get());
        this.tag(ItemTags.WALLS).add(ItemsPM.MARBLE_WALL.get(), ItemsPM.MARBLE_BRICK_WALL.get(), ItemsPM.MARBLE_ENCHANTED_WALL.get(), ItemsPM.MARBLE_ENCHANTED_BRICK_WALL.get(), ItemsPM.MARBLE_SMOKED_WALL.get(), ItemsPM.MARBLE_SMOKED_BRICK_WALL.get());
        this.tag(ItemTags.WOODEN_SLABS).add(ItemsPM.MOONWOOD_SLAB.get(), ItemsPM.SUNWOOD_SLAB.get(), ItemsPM.HALLOWOOD_SLAB.get());
        this.tag(ItemTags.WOODEN_STAIRS).add(ItemsPM.MOONWOOD_STAIRS.get(), ItemsPM.SUNWOOD_STAIRS.get(), ItemsPM.HALLOWOOD_STAIRS.get());

        this.tag(ItemTags.HEAD_ARMOR).addTag(ItemTagsPM.ROBES_HELMETS).add(ItemsPM.PRIMALITE_HEAD.get(), ItemsPM.HEXIUM_HEAD.get(), ItemsPM.HALLOWSTEEL_HEAD.get());
        this.tag(ItemTags.CHEST_ARMOR).addTag(ItemTagsPM.ROBES_CHESTPLATES).add(ItemsPM.PRIMALITE_CHEST.get(), ItemsPM.HEXIUM_CHEST.get(), ItemsPM.HALLOWSTEEL_CHEST.get());
        this.tag(ItemTags.LEG_ARMOR).addTag(ItemTagsPM.ROBES_LEGGINGS).add(ItemsPM.PRIMALITE_LEGS.get(), ItemsPM.HEXIUM_LEGS.get(), ItemsPM.HALLOWSTEEL_LEGS.get());
        this.tag(ItemTags.FOOT_ARMOR).addTag(ItemTagsPM.ROBES_BOOTS).add(ItemsPM.PRIMALITE_FEET.get(), ItemsPM.HEXIUM_FEET.get(), ItemsPM.HALLOWSTEEL_FEET.get());

        this.tag(ItemTags.BOW_ENCHANTABLE).addTag(CommonTags.Items.TOOLS_BOW);
        this.tag(ItemTags.CROSSBOW_ENCHANTABLE).addTag(CommonTags.Items.TOOLS_CROSSBOW);
        this.tag(ItemTags.DURABILITY_ENCHANTABLE).addTag(CommonTags.Items.TOOLS_BOW).addTag(CommonTags.Items.TOOLS_CROSSBOW).addTag(CommonTags.Items.TOOLS_FISHING_ROD).addTag(CommonTags.Items.TOOLS_SPEAR).addTag(CommonTags.Items.TOOLS_SHIELD);
        this.tag(ItemTags.FISHING_ENCHANTABLE).addTag(CommonTags.Items.TOOLS_FISHING_ROD);
        this.tag(ItemTags.TRIDENT_ENCHANTABLE).addTag(CommonTags.Items.TOOLS_SPEAR);

        // Add entries to Forge tags
        this.tag(CommonTags.Items.ARMORS).addTag(ItemTagsPM.ROBES_HELMETS).add(ItemsPM.PRIMALITE_HEAD.get(), ItemsPM.HEXIUM_HEAD.get(), ItemsPM.HALLOWSTEEL_HEAD.get());
        this.tag(CommonTags.Items.ARMORS).addTag(ItemTagsPM.ROBES_CHESTPLATES).add(ItemsPM.PRIMALITE_CHEST.get(), ItemsPM.HEXIUM_CHEST.get(), ItemsPM.HALLOWSTEEL_CHEST.get());
        this.tag(CommonTags.Items.ARMORS).addTag(ItemTagsPM.ROBES_LEGGINGS).add(ItemsPM.PRIMALITE_LEGS.get(), ItemsPM.HEXIUM_LEGS.get(), ItemsPM.HALLOWSTEEL_LEGS.get());
        this.tag(CommonTags.Items.ARMORS).addTag(ItemTagsPM.ROBES_BOOTS).add(ItemsPM.PRIMALITE_FEET.get(), ItemsPM.HEXIUM_FEET.get(), ItemsPM.HALLOWSTEEL_FEET.get());
        this.tag(CommonTags.Items.DUSTS).addTag(ItemExtensionTags.DUSTS_IRON).addTag(ItemExtensionTags.DUSTS_GOLD).addTag(ItemExtensionTags.DUSTS_COPPER).addOptionalTag(ItemExtensionTags.DUSTS_TIN.location()).addOptionalTag(ItemExtensionTags.DUSTS_LEAD.location()).addOptionalTag(ItemExtensionTags.DUSTS_SILVER.location()).addOptionalTag(ItemExtensionTags.DUSTS_URANIUM.location());
        this.tag(CommonTags.Items.INGOTS).addTag(ItemTagsPM.INGOTS_PRIMALITE).addTag(ItemTagsPM.INGOTS_HEXIUM).addTag(ItemTagsPM.INGOTS_HALLOWSTEEL);
        this.tag(CommonTags.Items.NUGGETS).addTag(ItemTagsPM.NUGGETS_PRIMALITE).addTag(ItemTagsPM.NUGGETS_HEXIUM).addTag(ItemTagsPM.NUGGETS_HALLOWSTEEL).addTag(ItemExtensionTags.NUGGETS_QUARTZ);
        this.tag(CommonTags.Items.ORE_RATES_DENSE).add(ItemsPM.ROCK_SALT_ORE.get());
        this.tag(CommonTags.Items.ORE_RATES_SINGULAR).add(ItemsPM.QUARTZ_ORE.get());
        this.tag(CommonTags.Items.ORES).addTag(ItemExtensionTags.ORES_ROCK_SALT).addOptionalTag(ItemExtensionTags.ORES_TIN.location()).addOptionalTag(ItemExtensionTags.ORES_LEAD.location()).addOptionalTag(ItemExtensionTags.ORES_SILVER.location()).addOptionalTag(ItemExtensionTags.ORES_URANIUM.location());
        this.tag(CommonTags.Items.ORES_QUARTZ).add(ItemsPM.QUARTZ_ORE.get());
        this.tag(CommonTags.Items.ORES_IN_GROUND_STONE).add(ItemsPM.QUARTZ_ORE.get(), ItemsPM.ROCK_SALT_ORE.get());
        this.tag(CommonTags.Items.RAW_MATERIALS).addOptionalTag(ItemExtensionTags.RAW_MATERIALS_TIN.location()).addOptionalTag(ItemExtensionTags.RAW_MATERIALS_LEAD.location()).addOptionalTag(ItemExtensionTags.RAW_MATERIALS_SILVER.location()).addOptionalTag(ItemExtensionTags.RAW_MATERIALS_URANIUM.location());
        this.tag(CommonTags.Items.STORAGE_BLOCKS).add(ItemsPM.IGNYX_BLOCK.get()).addTag(ItemTagsPM.STORAGE_BLOCKS_PRIMALITE).addTag(ItemTagsPM.STORAGE_BLOCKS_HEXIUM).addTag(ItemTagsPM.STORAGE_BLOCKS_HALLOWSTEEL);
        this.tag(CommonTags.Items.TOOLS_BOW).add(ItemsPM.PRIMALITE_BOW.get(), ItemsPM.HEXIUM_BOW.get(), ItemsPM.HALLOWSTEEL_BOW.get(), ItemsPM.FORBIDDEN_BOW.get());
        this.tag(CommonTags.Items.TOOLS_FISHING_ROD).add(ItemsPM.PRIMALITE_FISHING_ROD.get(), ItemsPM.HEXIUM_FISHING_ROD.get(), ItemsPM.HALLOWSTEEL_FISHING_ROD.get(), ItemsPM.PRIMAL_FISHING_ROD.get());
        this.tag(CommonTags.Items.TOOLS_SHIELD).add(ItemsPM.PRIMALITE_SHIELD.get(), ItemsPM.HEXIUM_SHIELD.get(), ItemsPM.HALLOWSTEEL_SHIELD.get(), ItemsPM.SACRED_SHIELD.get());
        this.tag(CommonTags.Items.TOOLS_SPEAR).add(ItemsPM.PRIMALITE_TRIDENT.get(), ItemsPM.HEXIUM_TRIDENT.get(), ItemsPM.HALLOWSTEEL_TRIDENT.get(), ItemsPM.FORBIDDEN_TRIDENT.get());
        
        this.tag(CommonTags.Items.GLASS_BLOCKS).add(ItemsPM.STAINED_SKYGLASS_BLACK.get(), ItemsPM.STAINED_SKYGLASS_BLUE.get(), ItemsPM.STAINED_SKYGLASS_BROWN.get(), ItemsPM.STAINED_SKYGLASS_CYAN.get(), ItemsPM.STAINED_SKYGLASS_GRAY.get(), ItemsPM.STAINED_SKYGLASS_GREEN.get(), ItemsPM.STAINED_SKYGLASS_LIGHT_BLUE.get(), ItemsPM.STAINED_SKYGLASS_LIGHT_GRAY.get(), ItemsPM.STAINED_SKYGLASS_LIME.get(), ItemsPM.STAINED_SKYGLASS_MAGENTA.get(), ItemsPM.STAINED_SKYGLASS_ORANGE.get(), ItemsPM.STAINED_SKYGLASS_PINK.get(), ItemsPM.STAINED_SKYGLASS_PURPLE.get(), ItemsPM.STAINED_SKYGLASS_RED.get(), ItemsPM.STAINED_SKYGLASS_WHITE.get(), ItemsPM.STAINED_SKYGLASS_YELLOW.get());
        this.tag(CommonTags.Items.GLASS_BLOCKS_COLORLESS).add(ItemsPM.SKYGLASS.get());

        this.tag(CommonTags.Items.GLASS_PANES).add(ItemsPM.STAINED_SKYGLASS_PANE_BLACK.get(), ItemsPM.STAINED_SKYGLASS_PANE_BLUE.get(), ItemsPM.STAINED_SKYGLASS_PANE_BROWN.get(), ItemsPM.STAINED_SKYGLASS_PANE_CYAN.get(), ItemsPM.STAINED_SKYGLASS_PANE_GRAY.get(), ItemsPM.STAINED_SKYGLASS_PANE_GREEN.get(), ItemsPM.STAINED_SKYGLASS_PANE_LIGHT_BLUE.get(), ItemsPM.STAINED_SKYGLASS_PANE_LIGHT_GRAY.get(), ItemsPM.STAINED_SKYGLASS_PANE_LIME.get(), ItemsPM.STAINED_SKYGLASS_PANE_MAGENTA.get(), ItemsPM.STAINED_SKYGLASS_PANE_ORANGE.get(), ItemsPM.STAINED_SKYGLASS_PANE_PINK.get(), ItemsPM.STAINED_SKYGLASS_PANE_PURPLE.get(), ItemsPM.STAINED_SKYGLASS_PANE_RED.get(), ItemsPM.STAINED_SKYGLASS_PANE_WHITE.get(), ItemsPM.STAINED_SKYGLASS_PANE_YELLOW.get());
        this.tag(CommonTags.Items.GLASS_PANES_COLORLESS).add(ItemsPM.SKYGLASS_PANE.get());

        // Add entries to Forge extension tags
        this.tag(ItemExtensionTags.DUSTS_COPPER).add(ItemsPM.COPPER_GRIT.get());
        this.tag(ItemExtensionTags.DUSTS_GOLD).add(ItemsPM.GOLD_GRIT.get());
        this.tag(ItemExtensionTags.DUSTS_IRON).add(ItemsPM.IRON_GRIT.get());
        this.tag(ItemExtensionTags.MILK).add(Items.MILK_BUCKET);
        this.tag(ItemExtensionTags.NUGGETS_QUARTZ).add(ItemsPM.QUARTZ_NUGGET.get());
        this.tag(ItemExtensionTags.ORES_ROCK_SALT).add(ItemsPM.ROCK_SALT_ORE.get());

        // Create custom tags
        this.tag(ItemTagsPM.ANALYSIS_TABLE_FORBIDDEN).add(Items.DRAGON_EGG, ItemsPM.HALLOWED_ORB.get());
        this.tag(ItemTagsPM.COLORED_SHULKER_BOXES).add(Items.BLACK_SHULKER_BOX, Items.BLUE_SHULKER_BOX, Items.BROWN_SHULKER_BOX, Items.CYAN_SHULKER_BOX, Items.GRAY_SHULKER_BOX, Items.GREEN_SHULKER_BOX, Items.LIGHT_BLUE_SHULKER_BOX, Items.LIGHT_GRAY_SHULKER_BOX, Items.LIME_SHULKER_BOX, Items.MAGENTA_SHULKER_BOX, Items.ORANGE_SHULKER_BOX, Items.PINK_SHULKER_BOX, Items.PURPLE_SHULKER_BOX, Items.RED_SHULKER_BOX, Items.WHITE_SHULKER_BOX, Items.YELLOW_SHULKER_BOX);
        this.tag(ItemTagsPM.CONCRETE).add(Items.BLACK_CONCRETE, Items.BLUE_CONCRETE, Items.BROWN_CONCRETE, Items.CYAN_CONCRETE, Items.GRAY_CONCRETE, Items.GREEN_CONCRETE, Items.LIGHT_BLUE_CONCRETE, Items.LIGHT_GRAY_CONCRETE, Items.LIME_CONCRETE, Items.MAGENTA_CONCRETE, Items.ORANGE_CONCRETE, Items.PINK_CONCRETE, Items.PURPLE_CONCRETE, Items.RED_CONCRETE, Items.WHITE_CONCRETE, Items.YELLOW_CONCRETE);
        this.tag(ItemTagsPM.CORAL_BLOCKS).add(Items.BRAIN_CORAL_BLOCK, Items.BUBBLE_CORAL_BLOCK, Items.FIRE_CORAL_BLOCK, Items.HORN_CORAL_BLOCK, Items.TUBE_CORAL_BLOCK);
        this.tag(ItemTagsPM.DEAD_CORAL_BLOCKS).add(Items.DEAD_BRAIN_CORAL_BLOCK, Items.DEAD_BUBBLE_CORAL_BLOCK, Items.DEAD_FIRE_CORAL_BLOCK, Items.DEAD_HORN_CORAL_BLOCK, Items.DEAD_TUBE_CORAL_BLOCK);
        this.tag(ItemTagsPM.DEAD_CORAL_PLANTS).add(Items.DEAD_BRAIN_CORAL, Items.DEAD_BUBBLE_CORAL, Items.DEAD_FIRE_CORAL, Items.DEAD_HORN_CORAL, Items.DEAD_TUBE_CORAL);
        this.tag(ItemTagsPM.DEAD_CORALS).addTag(ItemTagsPM.DEAD_CORAL_PLANTS).add(Items.DEAD_BRAIN_CORAL_FAN, Items.DEAD_BUBBLE_CORAL_FAN, Items.DEAD_FIRE_CORAL_FAN, Items.DEAD_HORN_CORAL_FAN, Items.DEAD_TUBE_CORAL_FAN);
        this.tag(ItemTagsPM.DEEP_STONE).add(Items.DEEPSLATE, Items.POLISHED_DEEPSLATE, Items.INFESTED_DEEPSLATE, Items.TUFF);
        this.tag(ItemTagsPM.ENCHANTING_TABLES).add(Items.ENCHANTING_TABLE).addOptional(ResourceLocation.fromNamespaceAndPath("quark", "matrix_enchanter"));
        this.tag(ItemTagsPM.ESSENCES).addTag(ItemTagsPM.ESSENCES_DUSTS).addTag(ItemTagsPM.ESSENCES_SHARDS).addTag(ItemTagsPM.ESSENCES_CRYSTALS).addTag(ItemTagsPM.ESSENCES_CLUSTERS);
        this.tag(ItemTagsPM.HALLOWOOD_LOGS).add(ItemsPM.HALLOWOOD_LOG.get(), ItemsPM.STRIPPED_HALLOWOOD_LOG.get(), ItemsPM.HALLOWOOD_WOOD.get(), ItemsPM.STRIPPED_HALLOWOOD_WOOD.get());
        this.tag(ItemTagsPM.INFERNAL_SUPERCHARGE_FUEL).add(ItemsPM.IGNYX.get(), ItemsPM.IGNYX_BLOCK.get());
        this.tag(ItemTagsPM.MAGICKAL_CLOTH).add(ItemsPM.SPELLCLOTH.get(), ItemsPM.HEXWEAVE.get(), ItemsPM.SAINTSWOOL.get());
        this.tag(ItemTagsPM.MANA_ORBS).add(ItemsPM.MANA_ORB_APPRENTICE.get(), ItemsPM.MANA_ORB_ADEPT.get(), ItemsPM.MANA_ORB_WIZARD.get(), ItemsPM.MANA_ORB_ARCHMAGE.get());
        this.tag(ItemTagsPM.MOONWOOD_LOGS).add(ItemsPM.MOONWOOD_LOG.get(), ItemsPM.STRIPPED_MOONWOOD_LOG.get(), ItemsPM.MOONWOOD_WOOD.get(), ItemsPM.STRIPPED_MOONWOOD_WOOD.get());
        this.tag(ItemTagsPM.NO_REPAIR).add(ItemsPM.EARTHSHATTER_HAMMER.get());
        this.tag(ItemTagsPM.RITUAL_CANDLES).add(ItemsPM.RITUAL_CANDLE_BLACK.get(), ItemsPM.RITUAL_CANDLE_BLUE.get(), ItemsPM.RITUAL_CANDLE_BROWN.get(), ItemsPM.RITUAL_CANDLE_CYAN.get(), ItemsPM.RITUAL_CANDLE_GRAY.get(), ItemsPM.RITUAL_CANDLE_GREEN.get(), ItemsPM.RITUAL_CANDLE_LIGHT_BLUE.get(), ItemsPM.RITUAL_CANDLE_LIGHT_GRAY.get(), ItemsPM.RITUAL_CANDLE_LIME.get(), ItemsPM.RITUAL_CANDLE_MAGENTA.get(), ItemsPM.RITUAL_CANDLE_ORANGE.get(), ItemsPM.RITUAL_CANDLE_PINK.get(), ItemsPM.RITUAL_CANDLE_PURPLE.get(), ItemsPM.RITUAL_CANDLE_RED.get(), ItemsPM.RITUAL_CANDLE_WHITE.get(), ItemsPM.RITUAL_CANDLE_YELLOW.get());
        this.tag(ItemTagsPM.RUNE_BASES).add(Items.STONE_SLAB);
        this.tag(ItemTagsPM.RUNE_ETCHINGS).addTag(CommonTags.Items.GEMS_LAPIS);
        this.tag(ItemTagsPM.RUNIC_TRIMMABLE_ARMOR).addTag(ItemTagsPM.ROBES);
        this.tag(ItemTagsPM.RUNIC_TRIM_MATERIALS).add(ItemsPM.RUNE_EARTH.get(), ItemsPM.RUNE_SEA.get(), ItemsPM.RUNE_SKY.get(), ItemsPM.RUNE_SUN.get(), ItemsPM.RUNE_MOON.get(), ItemsPM.RUNE_BLOOD.get(), ItemsPM.RUNE_INFERNAL.get(), ItemsPM.RUNE_VOID.get(), ItemsPM.RUNE_HALLOWED.get());
        this.tag(ItemTagsPM.SHULKER_BOXES).addTag(ItemTagsPM.COLORED_SHULKER_BOXES).add(Items.SHULKER_BOX);
        this.tag(ItemTagsPM.SKYGLASS).add(ItemsPM.SKYGLASS.get()).addTag(ItemTagsPM.STAINED_SKYGLASS);
        this.tag(ItemTagsPM.SKYGLASS_PANES).add(ItemsPM.SKYGLASS_PANE.get()).addTag(ItemTagsPM.STAINED_SKYGLASS_PANES);
        this.tag(ItemTagsPM.SPELLTOMES).add(ItemsPM.SPELLTOME_APPRENTICE.get(), ItemsPM.SPELLTOME_ADEPT.get(), ItemsPM.SPELLTOME_WIZARD.get(), ItemsPM.SPELLTOME_ARCHMAGE.get());
        this.tag(ItemTagsPM.STAINED_SKYGLASS).add(ItemsPM.STAINED_SKYGLASS_BLACK.get(), ItemsPM.STAINED_SKYGLASS_BLUE.get(), ItemsPM.STAINED_SKYGLASS_BROWN.get(), ItemsPM.STAINED_SKYGLASS_CYAN.get(), ItemsPM.STAINED_SKYGLASS_GRAY.get(), ItemsPM.STAINED_SKYGLASS_GREEN.get(), ItemsPM.STAINED_SKYGLASS_LIGHT_BLUE.get(), ItemsPM.STAINED_SKYGLASS_LIGHT_GRAY.get(), ItemsPM.STAINED_SKYGLASS_LIME.get(), ItemsPM.STAINED_SKYGLASS_MAGENTA.get(), ItemsPM.STAINED_SKYGLASS_ORANGE.get(), ItemsPM.STAINED_SKYGLASS_PINK.get(), ItemsPM.STAINED_SKYGLASS_PURPLE.get(), ItemsPM.STAINED_SKYGLASS_RED.get(), ItemsPM.STAINED_SKYGLASS_WHITE.get(), ItemsPM.STAINED_SKYGLASS_YELLOW.get());
        this.tag(ItemTagsPM.STAINED_SKYGLASS_PANES).add(ItemsPM.STAINED_SKYGLASS_PANE_BLACK.get(), ItemsPM.STAINED_SKYGLASS_PANE_BLUE.get(), ItemsPM.STAINED_SKYGLASS_PANE_BROWN.get(), ItemsPM.STAINED_SKYGLASS_PANE_CYAN.get(), ItemsPM.STAINED_SKYGLASS_PANE_GRAY.get(), ItemsPM.STAINED_SKYGLASS_PANE_GREEN.get(), ItemsPM.STAINED_SKYGLASS_PANE_LIGHT_BLUE.get(), ItemsPM.STAINED_SKYGLASS_PANE_LIGHT_GRAY.get(), ItemsPM.STAINED_SKYGLASS_PANE_LIME.get(), ItemsPM.STAINED_SKYGLASS_PANE_MAGENTA.get(), ItemsPM.STAINED_SKYGLASS_PANE_ORANGE.get(), ItemsPM.STAINED_SKYGLASS_PANE_PINK.get(), ItemsPM.STAINED_SKYGLASS_PANE_PURPLE.get(), ItemsPM.STAINED_SKYGLASS_PANE_RED.get(), ItemsPM.STAINED_SKYGLASS_PANE_WHITE.get(), ItemsPM.STAINED_SKYGLASS_PANE_YELLOW.get());
        this.tag(ItemTagsPM.STATIC_BOOKS).add(ItemsPM.STATIC_BOOK.get(), ItemsPM.STATIC_BOOK_UNCOMMON.get(), ItemsPM.STATIC_BOOK_RARE.get(), ItemsPM.STATIC_TABLET.get());
        this.tag(ItemTagsPM.SUNWOOD_LOGS).add(ItemsPM.SUNWOOD_LOG.get(), ItemsPM.STRIPPED_SUNWOOD_LOG.get(), ItemsPM.SUNWOOD_WOOD.get(), ItemsPM.STRIPPED_SUNWOOD_WOOD.get());
        this.tag(ItemTagsPM.SURFACE_STONE).add(Items.ANDESITE, Items.DIORITE, Items.GRANITE, Items.INFESTED_STONE, Items.STONE, Items.POLISHED_ANDESITE, Items.POLISHED_DIORITE, Items.POLISHED_GRANITE);
        this.tag(ItemTagsPM.TREEFOLK_LOVED).add(Items.BONE_MEAL);
        
        // FIXME Use the WARDABLE_ARMOR tag as the source of truth if/when the RegisterItemDecorationsEvent is made to fire *after* tag data loads
        var wardableArmorTag = this.tag(ItemTagsPM.WARDABLE_ARMOR);
        WardingModuleItem.getApplicableItems().forEach(itemSupplier -> wardableArmorTag.add(itemSupplier.get()));
        
        this.tag(ItemTagsPM.FOOD_BAKED_POTATO).add(Items.BAKED_POTATO, ItemsPM.SALTED_BAKED_POTATO.get());
        this.tag(ItemTagsPM.FOOD_COOKED_BEEF).add(Items.COOKED_BEEF, ItemsPM.SALTED_COOKED_BEEF.get());
        
        this.tag(ItemTagsPM.ROBES).addTag(ItemTagsPM.ROBES_HELMETS).addTag(ItemTagsPM.ROBES_CHESTPLATES).addTag(ItemTagsPM.ROBES_LEGGINGS).addTag(ItemTagsPM.ROBES_BOOTS);
        this.tag(ItemTagsPM.ROBES_HELMETS).add(ItemsPM.IMBUED_WOOL_HEAD.get(), ItemsPM.SPELLCLOTH_HEAD.get(), ItemsPM.HEXWEAVE_HEAD.get(), ItemsPM.SAINTSWOOL_HEAD.get());
        this.tag(ItemTagsPM.ROBES_CHESTPLATES).add(ItemsPM.IMBUED_WOOL_CHEST.get(), ItemsPM.SPELLCLOTH_CHEST.get(), ItemsPM.HEXWEAVE_CHEST.get(), ItemsPM.SAINTSWOOL_CHEST.get());
        this.tag(ItemTagsPM.ROBES_LEGGINGS).add(ItemsPM.IMBUED_WOOL_LEGS.get(), ItemsPM.SPELLCLOTH_LEGS.get(), ItemsPM.HEXWEAVE_LEGS.get(), ItemsPM.SAINTSWOOL_LEGS.get());
        this.tag(ItemTagsPM.ROBES_BOOTS).add(ItemsPM.IMBUED_WOOL_FEET.get(), ItemsPM.SPELLCLOTH_FEET.get(), ItemsPM.HEXWEAVE_FEET.get(), ItemsPM.SAINTSWOOL_FEET.get());
        
        this.tag(ItemTagsPM.ESSENCES_DUSTS).addTag(ItemTagsPM.ESSENCES_TERRESTRIAL_DUSTS).addTag(ItemTagsPM.ESSENCES_FORBIDDEN_DUSTS).add(ItemsPM.ESSENCE_DUST_HALLOWED.get());
        this.tag(ItemTagsPM.ESSENCES_TERRESTRIAL_DUSTS).add(ItemsPM.ESSENCE_DUST_EARTH.get(), ItemsPM.ESSENCE_DUST_SEA.get(), ItemsPM.ESSENCE_DUST_SKY.get(), ItemsPM.ESSENCE_DUST_SUN.get(), ItemsPM.ESSENCE_DUST_MOON.get());
        this.tag(ItemTagsPM.ESSENCES_FORBIDDEN_DUSTS).add(ItemsPM.ESSENCE_DUST_BLOOD.get(), ItemsPM.ESSENCE_DUST_INFERNAL.get(), ItemsPM.ESSENCE_DUST_VOID.get());
        this.tag(ItemTagsPM.ESSENCES_SHARDS).addTag(ItemTagsPM.ESSENCES_TERRESTRIAL_SHARDS).addTag(ItemTagsPM.ESSENCES_FORBIDDEN_SHARDS).add(ItemsPM.ESSENCE_SHARD_HALLOWED.get());
        this.tag(ItemTagsPM.ESSENCES_TERRESTRIAL_SHARDS).add(ItemsPM.ESSENCE_SHARD_EARTH.get(), ItemsPM.ESSENCE_SHARD_SEA.get(), ItemsPM.ESSENCE_SHARD_SKY.get(), ItemsPM.ESSENCE_SHARD_SUN.get(), ItemsPM.ESSENCE_SHARD_MOON.get());
        this.tag(ItemTagsPM.ESSENCES_FORBIDDEN_SHARDS).add(ItemsPM.ESSENCE_SHARD_BLOOD.get(), ItemsPM.ESSENCE_SHARD_INFERNAL.get(), ItemsPM.ESSENCE_SHARD_VOID.get());
        this.tag(ItemTagsPM.ESSENCES_CRYSTALS).addTag(ItemTagsPM.ESSENCES_TERRESTRIAL_CRYSTALS).addTag(ItemTagsPM.ESSENCES_FORBIDDEN_CRYSTALS).add(ItemsPM.ESSENCE_CRYSTAL_HALLOWED.get());
        this.tag(ItemTagsPM.ESSENCES_TERRESTRIAL_CRYSTALS).add(ItemsPM.ESSENCE_CRYSTAL_EARTH.get(), ItemsPM.ESSENCE_CRYSTAL_SEA.get(), ItemsPM.ESSENCE_CRYSTAL_SKY.get(), ItemsPM.ESSENCE_CRYSTAL_SUN.get(), ItemsPM.ESSENCE_CRYSTAL_MOON.get());
        this.tag(ItemTagsPM.ESSENCES_FORBIDDEN_CRYSTALS).add(ItemsPM.ESSENCE_CRYSTAL_BLOOD.get(), ItemsPM.ESSENCE_CRYSTAL_INFERNAL.get(), ItemsPM.ESSENCE_CRYSTAL_VOID.get());
        this.tag(ItemTagsPM.ESSENCES_CLUSTERS).addTag(ItemTagsPM.ESSENCES_TERRESTRIAL_CLUSTERS).addTag(ItemTagsPM.ESSENCES_FORBIDDEN_CLUSTERS).add(ItemsPM.ESSENCE_CLUSTER_HALLOWED.get());
        this.tag(ItemTagsPM.ESSENCES_TERRESTRIAL_CLUSTERS).add(ItemsPM.ESSENCE_CLUSTER_EARTH.get(), ItemsPM.ESSENCE_CLUSTER_SEA.get(), ItemsPM.ESSENCE_CLUSTER_SKY.get(), ItemsPM.ESSENCE_CLUSTER_SUN.get(), ItemsPM.ESSENCE_CLUSTER_MOON.get());
        this.tag(ItemTagsPM.ESSENCES_FORBIDDEN_CLUSTERS).add(ItemsPM.ESSENCE_CLUSTER_BLOOD.get(), ItemsPM.ESSENCE_CLUSTER_INFERNAL.get(), ItemsPM.ESSENCE_CLUSTER_VOID.get());

        this.tag(ItemTagsPM.INGOTS_HALLOWSTEEL).add(ItemsPM.HALLOWSTEEL_INGOT.get());
        this.tag(ItemTagsPM.INGOTS_HEXIUM).add(ItemsPM.HEXIUM_INGOT.get());
        this.tag(ItemTagsPM.INGOTS_PRIMALITE).add(ItemsPM.PRIMALITE_INGOT.get());
        this.tag(ItemTagsPM.NUGGETS_HALLOWSTEEL).add(ItemsPM.HALLOWSTEEL_NUGGET.get());
        this.tag(ItemTagsPM.NUGGETS_HEXIUM).add(ItemsPM.HEXIUM_NUGGET.get());
        this.tag(ItemTagsPM.NUGGETS_PRIMALITE).add(ItemsPM.PRIMALITE_NUGGET.get());
        this.tag(ItemTagsPM.STORAGE_BLOCKS_HALLOWSTEEL).add(ItemsPM.HALLOWSTEEL_BLOCK.get());
        this.tag(ItemTagsPM.STORAGE_BLOCKS_HEXIUM).add(ItemsPM.HEXIUM_BLOCK.get());
        this.tag(ItemTagsPM.STORAGE_BLOCKS_PRIMALITE).add(ItemsPM.PRIMALITE_BLOCK.get());
        
        this.tag(ItemTagsPM.MELEE_ENCHANTABLE).addTag(ItemTags.WEAPON_ENCHANTABLE).addTag(ItemTags.TRIDENT_ENCHANTABLE).addTag(ItemTagsPM.STAFF_ENCHANTABLE);
        this.tag(ItemTagsPM.ARCHERY_ENCHANTABLE).addTag(ItemTags.BOW_ENCHANTABLE).addTag(ItemTags.CROSSBOW_ENCHANTABLE);
        this.tag(ItemTagsPM.SHIELD_ENCHANTABLE).addTag(CommonTags.Items.TOOLS_SHIELD);
        this.tag(ItemTagsPM.STAFF_ENCHANTABLE).add(ItemsPM.MODULAR_STAFF.get());
        this.tag(ItemTagsPM.WAND_ENCHANTABLE).addTag(ItemTagsPM.STAFF_ENCHANTABLE).add(ItemsPM.MUNDANE_WAND.get(), ItemsPM.MODULAR_WAND.get());
        this.tag(ItemTagsPM.HOE_ENCHANTABLE).addTag(ItemTags.HOES);
        this.tag(ItemTagsPM.BOUNTY_ENCHANTABLE).addTag(ItemTags.FISHING_ENCHANTABLE).addTag(ItemTagsPM.HOE_ENCHANTABLE);
        this.tag(ItemTagsPM.RENDING_ENCHANTABLE).addTag(ItemTags.SHARP_WEAPON_ENCHANTABLE).addTag(ItemTags.TRIDENT_ENCHANTABLE);
        this.tag(ItemTagsPM.ESSENCE_THIEF_ENCHANTABLE).addTag(ItemTags.WEAPON_ENCHANTABLE).addTag(ItemTags.TRIDENT_ENCHANTABLE).addTag(ItemTagsPM.WAND_ENCHANTABLE);
        this.tag(ItemTagsPM.MANA_EFFICIENCY_ENCHANTABLE).addTag(ItemTagsPM.WAND_ENCHANTABLE).addTag(ItemTagsPM.SPELLTOMES).addTag(ItemTagsPM.MANA_ORBS).addTag(ItemTagsPM.ROBES);
        this.tag(ItemTagsPM.SPELL_POWER_ENCHANTABLE).addTag(ItemTagsPM.WAND_ENCHANTABLE).addTag(ItemTagsPM.SPELLTOMES);
        this.tag(ItemTagsPM.PONDERING_ENCHANTABLE).addTag(ItemTagsPM.MANA_ORBS);
    }
}
