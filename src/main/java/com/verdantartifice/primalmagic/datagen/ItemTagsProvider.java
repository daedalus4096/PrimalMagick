package com.verdantartifice.primalmagic.datagen;

import java.nio.file.Path;

import com.verdantartifice.primalmagic.common.items.ItemsPM;
import com.verdantartifice.primalmagic.common.tags.ItemTagsForgeExt;
import com.verdantartifice.primalmagic.common.tags.ItemTagsPM;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.TagsProvider;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagCollection;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.Tags;

/**
 * Data provider for all of the mod's item tags, both original tags and modifications to vanilla tags.
 * 
 * @author Daedalus4096
 */
public class ItemTagsProvider extends TagsProvider<Item> {
    @SuppressWarnings("deprecation")
    public ItemTagsProvider(DataGenerator generator) {
        super(generator, Registry.ITEM);
    }

    @Override
    public String getName() {
        return "Primal Magic Item Tags";
    }

    @Override
    protected void registerTags() {
        // Add entries to vanilla tags
        this.getBuilder(ItemTags.LOGS).add(ItemTagsPM.MOONWOOD_LOGS, ItemTagsPM.SUNWOOD_LOGS);
        this.getBuilder(ItemTags.LEAVES).add(ItemsPM.MOONWOOD_LEAVES.get(), ItemsPM.SUNWOOD_LEAVES.get());
        this.getBuilder(ItemTags.PLANKS).add(ItemsPM.MOONWOOD_PLANKS.get(), ItemsPM.SUNWOOD_PLANKS.get());
        this.getBuilder(ItemTags.SAPLINGS).add(ItemsPM.MOONWOOD_SAPLING.get(), ItemsPM.SUNWOOD_SAPLING.get());
        this.getBuilder(ItemTags.WALLS).add(ItemsPM.MARBLE_WALL.get(), ItemsPM.MARBLE_BRICK_WALL.get(), ItemsPM.MARBLE_ENCHANTED_WALL.get(), ItemsPM.MARBLE_ENCHANTED_BRICK_WALL.get(), ItemsPM.MARBLE_SMOKED_WALL.get(), ItemsPM.MARBLE_SMOKED_BRICK_WALL.get());
        this.getBuilder(ItemTags.WOODEN_SLABS).add(ItemsPM.MOONWOOD_SLAB.get(), ItemsPM.SUNWOOD_SLAB.get());
        this.getBuilder(ItemTags.WOODEN_STAIRS).add(ItemsPM.MOONWOOD_STAIRS.get(), ItemsPM.SUNWOOD_STAIRS.get());
        
        // Add entries to Forge tags
        this.getBuilder(Tags.Items.BEACON_PAYMENT).add(ItemTagsPM.INGOTS_PRIMALITE, ItemTagsPM.INGOTS_HEXIUM, ItemTagsPM.INGOTS_HALLOWSTEEL);
        this.getBuilder(Tags.Items.DUSTS).add(ItemTagsForgeExt.DUSTS_IRON, ItemTagsForgeExt.DUSTS_GOLD);
        this.getBuilder(Tags.Items.INGOTS).add(ItemTagsPM.INGOTS_PRIMALITE, ItemTagsPM.INGOTS_HEXIUM, ItemTagsPM.INGOTS_HALLOWSTEEL);
        this.getBuilder(Tags.Items.NUGGETS).add(ItemTagsPM.NUGGETS_PRIMALITE, ItemTagsPM.NUGGETS_HEXIUM, ItemTagsPM.NUGGETS_HALLOWSTEEL, ItemTagsForgeExt.NUGGETS_QUARTZ);
        this.getBuilder(Tags.Items.ORES_QUARTZ).add(ItemsPM.QUARTZ_ORE.get());
        this.getBuilder(Tags.Items.STORAGE_BLOCKS).add(ItemTagsPM.STORAGE_BLOCKS_PRIMALITE, ItemTagsPM.STORAGE_BLOCKS_HEXIUM, ItemTagsPM.STORAGE_BLOCKS_HALLOWSTEEL);
        this.getBuilder(Tags.Items.SUPPORTS_BEACON).add(ItemTagsPM.STORAGE_BLOCKS_PRIMALITE, ItemTagsPM.STORAGE_BLOCKS_HEXIUM, ItemTagsPM.STORAGE_BLOCKS_HALLOWSTEEL);
        
        this.getBuilder(Tags.Items.GLASS_COLORLESS).add(ItemsPM.SKYGLASS.get());
        this.getBuilder(Tags.Items.GLASS_BLACK).add(ItemsPM.STAINED_SKYGLASS_BLACK.get());
        this.getBuilder(Tags.Items.GLASS_BLUE).add(ItemsPM.STAINED_SKYGLASS_BLUE.get());
        this.getBuilder(Tags.Items.GLASS_BROWN).add(ItemsPM.STAINED_SKYGLASS_BROWN.get());
        this.getBuilder(Tags.Items.GLASS_CYAN).add(ItemsPM.STAINED_SKYGLASS_CYAN.get());
        this.getBuilder(Tags.Items.GLASS_GRAY).add(ItemsPM.STAINED_SKYGLASS_GRAY.get());
        this.getBuilder(Tags.Items.GLASS_GREEN).add(ItemsPM.STAINED_SKYGLASS_GREEN.get());
        this.getBuilder(Tags.Items.GLASS_LIGHT_BLUE).add(ItemsPM.STAINED_SKYGLASS_LIGHT_BLUE.get());
        this.getBuilder(Tags.Items.GLASS_LIGHT_GRAY).add(ItemsPM.STAINED_SKYGLASS_LIGHT_GRAY.get());
        this.getBuilder(Tags.Items.GLASS_LIME).add(ItemsPM.STAINED_SKYGLASS_LIME.get());
        this.getBuilder(Tags.Items.GLASS_MAGENTA).add(ItemsPM.STAINED_SKYGLASS_MAGENTA.get());
        this.getBuilder(Tags.Items.GLASS_ORANGE).add(ItemsPM.STAINED_SKYGLASS_ORANGE.get());
        this.getBuilder(Tags.Items.GLASS_PINK).add(ItemsPM.STAINED_SKYGLASS_PINK.get());
        this.getBuilder(Tags.Items.GLASS_PURPLE).add(ItemsPM.STAINED_SKYGLASS_PURPLE.get());
        this.getBuilder(Tags.Items.GLASS_RED).add(ItemsPM.STAINED_SKYGLASS_RED.get());
        this.getBuilder(Tags.Items.GLASS_WHITE).add(ItemsPM.STAINED_SKYGLASS_WHITE.get());
        this.getBuilder(Tags.Items.GLASS_YELLOW).add(ItemsPM.STAINED_SKYGLASS_YELLOW.get());
        this.getBuilder(Tags.Items.STAINED_GLASS).add(ItemTagsPM.STAINED_SKYGLASS);
        
        this.getBuilder(Tags.Items.GLASS_PANES_COLORLESS).add(ItemsPM.SKYGLASS_PANE.get());
        this.getBuilder(Tags.Items.GLASS_PANES_BLACK).add(ItemsPM.STAINED_SKYGLASS_PANE_BLACK.get());
        this.getBuilder(Tags.Items.GLASS_PANES_BLUE).add(ItemsPM.STAINED_SKYGLASS_PANE_BLUE.get());
        this.getBuilder(Tags.Items.GLASS_PANES_BROWN).add(ItemsPM.STAINED_SKYGLASS_PANE_BROWN.get());
        this.getBuilder(Tags.Items.GLASS_PANES_CYAN).add(ItemsPM.STAINED_SKYGLASS_PANE_CYAN.get());
        this.getBuilder(Tags.Items.GLASS_PANES_GRAY).add(ItemsPM.STAINED_SKYGLASS_PANE_GRAY.get());
        this.getBuilder(Tags.Items.GLASS_PANES_GREEN).add(ItemsPM.STAINED_SKYGLASS_PANE_GREEN.get());
        this.getBuilder(Tags.Items.GLASS_PANES_LIGHT_BLUE).add(ItemsPM.STAINED_SKYGLASS_PANE_LIGHT_BLUE.get());
        this.getBuilder(Tags.Items.GLASS_PANES_LIGHT_GRAY).add(ItemsPM.STAINED_SKYGLASS_PANE_LIGHT_GRAY.get());
        this.getBuilder(Tags.Items.GLASS_PANES_LIME).add(ItemsPM.STAINED_SKYGLASS_PANE_LIME.get());
        this.getBuilder(Tags.Items.GLASS_PANES_MAGENTA).add(ItemsPM.STAINED_SKYGLASS_PANE_MAGENTA.get());
        this.getBuilder(Tags.Items.GLASS_PANES_ORANGE).add(ItemsPM.STAINED_SKYGLASS_PANE_ORANGE.get());
        this.getBuilder(Tags.Items.GLASS_PANES_PINK).add(ItemsPM.STAINED_SKYGLASS_PANE_PINK.get());
        this.getBuilder(Tags.Items.GLASS_PANES_PURPLE).add(ItemsPM.STAINED_SKYGLASS_PANE_PURPLE.get());
        this.getBuilder(Tags.Items.GLASS_PANES_RED).add(ItemsPM.STAINED_SKYGLASS_PANE_RED.get());
        this.getBuilder(Tags.Items.GLASS_PANES_WHITE).add(ItemsPM.STAINED_SKYGLASS_PANE_WHITE.get());
        this.getBuilder(Tags.Items.GLASS_PANES_YELLOW).add(ItemsPM.STAINED_SKYGLASS_PANE_YELLOW.get());
        this.getBuilder(Tags.Items.STAINED_GLASS_PANES).add(ItemTagsPM.STAINED_SKYGLASS_PANES);

        // Add entries to Forge extension tags
        this.getBuilder(ItemTagsForgeExt.DUSTS_GOLD).add(ItemsPM.GOLD_GRIT.get());
        this.getBuilder(ItemTagsForgeExt.DUSTS_IRON).add(ItemsPM.IRON_GRIT.get());
        this.getBuilder(ItemTagsForgeExt.NUGGETS_QUARTZ).add(ItemsPM.QUARTZ_NUGGET.get());

        // Create custom tags
        this.getBuilder(ItemTagsPM.COLORED_SHULKER_BOXES).add(Items.BLACK_SHULKER_BOX, Items.BLUE_SHULKER_BOX, Items.BROWN_SHULKER_BOX, Items.CYAN_SHULKER_BOX, Items.GRAY_SHULKER_BOX, Items.GREEN_SHULKER_BOX, Items.LIGHT_BLUE_SHULKER_BOX, Items.LIGHT_GRAY_SHULKER_BOX, Items.LIME_SHULKER_BOX, Items.MAGENTA_SHULKER_BOX, Items.ORANGE_SHULKER_BOX, Items.PINK_SHULKER_BOX, Items.PURPLE_SHULKER_BOX, Items.RED_SHULKER_BOX, Items.WHITE_SHULKER_BOX, Items.YELLOW_SHULKER_BOX);
        this.getBuilder(ItemTagsPM.CONCRETE).add(Items.BLACK_CONCRETE, Items.BLUE_CONCRETE, Items.BROWN_CONCRETE, Items.CYAN_CONCRETE, Items.GRAY_CONCRETE, Items.GREEN_CONCRETE, Items.LIGHT_BLUE_CONCRETE, Items.LIGHT_GRAY_CONCRETE, Items.LIME_CONCRETE, Items.MAGENTA_CONCRETE, Items.ORANGE_CONCRETE, Items.PINK_CONCRETE, Items.PURPLE_CONCRETE, Items.RED_CONCRETE, Items.WHITE_CONCRETE, Items.YELLOW_CONCRETE);
        this.getBuilder(ItemTagsPM.CORAL_BLOCKS).add(Items.BRAIN_CORAL_BLOCK, Items.BUBBLE_CORAL_BLOCK, Items.FIRE_CORAL_BLOCK, Items.HORN_CORAL_BLOCK, Items.TUBE_CORAL_BLOCK);
        this.getBuilder(ItemTagsPM.DEAD_CORAL_BLOCKS).add(Items.DEAD_BRAIN_CORAL_BLOCK, Items.DEAD_BUBBLE_CORAL_BLOCK, Items.DEAD_FIRE_CORAL_BLOCK, Items.DEAD_HORN_CORAL_BLOCK, Items.DEAD_TUBE_CORAL_BLOCK);
        this.getBuilder(ItemTagsPM.DEAD_CORAL_PLANTS).add(Items.DEAD_BRAIN_CORAL, Items.DEAD_BUBBLE_CORAL, Items.DEAD_FIRE_CORAL, Items.DEAD_HORN_CORAL, Items.DEAD_TUBE_CORAL);
        this.getBuilder(ItemTagsPM.DEAD_CORALS).add(ItemTagsPM.DEAD_CORAL_PLANTS).add(Items.DEAD_BRAIN_CORAL_FAN, Items.DEAD_BUBBLE_CORAL_FAN, Items.DEAD_FIRE_CORAL_FAN, Items.DEAD_HORN_CORAL_FAN, Items.DEAD_TUBE_CORAL_FAN);
        this.getBuilder(ItemTagsPM.ESSENCES).add(ItemTagsPM.ESSENCES_DUSTS, ItemTagsPM.ESSENCES_SHARDS, ItemTagsPM.ESSENCES_CRYSTALS, ItemTagsPM.ESSENCES_CLUSTERS);
        this.getBuilder(ItemTagsPM.MOONWOOD_LOGS).add(ItemsPM.MOONWOOD_LOG.get(), ItemsPM.STRIPPED_MOONWOOD_LOG.get(), ItemsPM.MOONWOOD_WOOD.get(), ItemsPM.STRIPPED_MOONWOOD_WOOD.get());
        this.getBuilder(ItemTagsPM.RITUAL_CANDLES).add(ItemsPM.RITUAL_CANDLE_BLACK.get(), ItemsPM.RITUAL_CANDLE_BLUE.get(), ItemsPM.RITUAL_CANDLE_BROWN.get(), ItemsPM.RITUAL_CANDLE_CYAN.get(), ItemsPM.RITUAL_CANDLE_GRAY.get(), ItemsPM.RITUAL_CANDLE_GREEN.get(), ItemsPM.RITUAL_CANDLE_LIGHT_BLUE.get(), ItemsPM.RITUAL_CANDLE_LIGHT_GRAY.get(), ItemsPM.RITUAL_CANDLE_LIME.get(), ItemsPM.RITUAL_CANDLE_MAGENTA.get(), ItemsPM.RITUAL_CANDLE_ORANGE.get(), ItemsPM.RITUAL_CANDLE_PINK.get(), ItemsPM.RITUAL_CANDLE_PURPLE.get(), ItemsPM.RITUAL_CANDLE_RED.get(), ItemsPM.RITUAL_CANDLE_WHITE.get(), ItemsPM.RITUAL_CANDLE_YELLOW.get());
        this.getBuilder(ItemTagsPM.SHULKER_BOXES).add(ItemTagsPM.COLORED_SHULKER_BOXES).add(Items.SHULKER_BOX);
        this.getBuilder(ItemTagsPM.SKYGLASS).add(ItemsPM.SKYGLASS.get()).add(ItemTagsPM.STAINED_SKYGLASS);
        this.getBuilder(ItemTagsPM.SKYGLASS_PANES).add(ItemsPM.SKYGLASS_PANE.get()).add(ItemTagsPM.STAINED_SKYGLASS_PANES);
        this.getBuilder(ItemTagsPM.STAINED_SKYGLASS).add(ItemsPM.STAINED_SKYGLASS_BLACK.get(), ItemsPM.STAINED_SKYGLASS_BLUE.get(), ItemsPM.STAINED_SKYGLASS_BROWN.get(), ItemsPM.STAINED_SKYGLASS_CYAN.get(), ItemsPM.STAINED_SKYGLASS_GRAY.get(), ItemsPM.STAINED_SKYGLASS_GREEN.get(), ItemsPM.STAINED_SKYGLASS_LIGHT_BLUE.get(), ItemsPM.STAINED_SKYGLASS_LIGHT_GRAY.get(), ItemsPM.STAINED_SKYGLASS_LIME.get(), ItemsPM.STAINED_SKYGLASS_MAGENTA.get(), ItemsPM.STAINED_SKYGLASS_ORANGE.get(), ItemsPM.STAINED_SKYGLASS_PINK.get(), ItemsPM.STAINED_SKYGLASS_PURPLE.get(), ItemsPM.STAINED_SKYGLASS_RED.get(), ItemsPM.STAINED_SKYGLASS_WHITE.get(), ItemsPM.STAINED_SKYGLASS_YELLOW.get());
        this.getBuilder(ItemTagsPM.STAINED_SKYGLASS_PANES).add(ItemsPM.STAINED_SKYGLASS_PANE_BLACK.get(), ItemsPM.STAINED_SKYGLASS_PANE_BLUE.get(), ItemsPM.STAINED_SKYGLASS_PANE_BROWN.get(), ItemsPM.STAINED_SKYGLASS_PANE_CYAN.get(), ItemsPM.STAINED_SKYGLASS_PANE_GRAY.get(), ItemsPM.STAINED_SKYGLASS_PANE_GREEN.get(), ItemsPM.STAINED_SKYGLASS_PANE_LIGHT_BLUE.get(), ItemsPM.STAINED_SKYGLASS_PANE_LIGHT_GRAY.get(), ItemsPM.STAINED_SKYGLASS_PANE_LIME.get(), ItemsPM.STAINED_SKYGLASS_PANE_MAGENTA.get(), ItemsPM.STAINED_SKYGLASS_PANE_ORANGE.get(), ItemsPM.STAINED_SKYGLASS_PANE_PINK.get(), ItemsPM.STAINED_SKYGLASS_PANE_PURPLE.get(), ItemsPM.STAINED_SKYGLASS_PANE_RED.get(), ItemsPM.STAINED_SKYGLASS_PANE_WHITE.get(), ItemsPM.STAINED_SKYGLASS_PANE_YELLOW.get());
        this.getBuilder(ItemTagsPM.SUNWOOD_LOGS).add(ItemsPM.SUNWOOD_LOG.get(), ItemsPM.STRIPPED_SUNWOOD_LOG.get(), ItemsPM.SUNWOOD_WOOD.get(), ItemsPM.STRIPPED_SUNWOOD_WOOD.get());
        
        this.getBuilder(ItemTagsPM.ESSENCES_DUSTS).add(ItemTagsPM.ESSENCES_TERRESTRIAL_DUSTS).add(ItemsPM.ESSENCE_DUST_BLOOD.get(), ItemsPM.ESSENCE_DUST_INFERNAL.get(), ItemsPM.ESSENCE_DUST_VOID.get(), ItemsPM.ESSENCE_DUST_HALLOWED.get());
        this.getBuilder(ItemTagsPM.ESSENCES_TERRESTRIAL_DUSTS).add(ItemsPM.ESSENCE_DUST_EARTH.get(), ItemsPM.ESSENCE_DUST_SEA.get(), ItemsPM.ESSENCE_DUST_SKY.get(), ItemsPM.ESSENCE_DUST_SUN.get(), ItemsPM.ESSENCE_DUST_MOON.get());
        this.getBuilder(ItemTagsPM.ESSENCES_SHARDS).add(ItemsPM.ESSENCE_SHARD_EARTH.get(), ItemsPM.ESSENCE_SHARD_SEA.get(), ItemsPM.ESSENCE_SHARD_SKY.get(), ItemsPM.ESSENCE_SHARD_SUN.get(), ItemsPM.ESSENCE_SHARD_MOON.get(), ItemsPM.ESSENCE_SHARD_BLOOD.get(), ItemsPM.ESSENCE_SHARD_INFERNAL.get(), ItemsPM.ESSENCE_SHARD_VOID.get(), ItemsPM.ESSENCE_SHARD_HALLOWED.get());
        this.getBuilder(ItemTagsPM.ESSENCES_CRYSTALS).add(ItemsPM.ESSENCE_CRYSTAL_EARTH.get(), ItemsPM.ESSENCE_CRYSTAL_SEA.get(), ItemsPM.ESSENCE_CRYSTAL_SKY.get(), ItemsPM.ESSENCE_CRYSTAL_SUN.get(), ItemsPM.ESSENCE_CRYSTAL_MOON.get(), ItemsPM.ESSENCE_CRYSTAL_BLOOD.get(), ItemsPM.ESSENCE_CRYSTAL_INFERNAL.get(), ItemsPM.ESSENCE_CRYSTAL_VOID.get(), ItemsPM.ESSENCE_CRYSTAL_HALLOWED.get());
        this.getBuilder(ItemTagsPM.ESSENCES_CLUSTERS).add(ItemsPM.ESSENCE_CLUSTER_EARTH.get(), ItemsPM.ESSENCE_CLUSTER_SEA.get(), ItemsPM.ESSENCE_CLUSTER_SKY.get(), ItemsPM.ESSENCE_CLUSTER_SUN.get(), ItemsPM.ESSENCE_CLUSTER_MOON.get(), ItemsPM.ESSENCE_CLUSTER_BLOOD.get(), ItemsPM.ESSENCE_CLUSTER_INFERNAL.get(), ItemsPM.ESSENCE_CLUSTER_VOID.get(), ItemsPM.ESSENCE_CLUSTER_HALLOWED.get());

        this.getBuilder(ItemTagsPM.INGOTS_HALLOWSTEEL).add(ItemsPM.HALLOWSTEEL_INGOT.get());
        this.getBuilder(ItemTagsPM.INGOTS_HEXIUM).add(ItemsPM.HEXIUM_INGOT.get());
        this.getBuilder(ItemTagsPM.INGOTS_PRIMALITE).add(ItemsPM.PRIMALITE_INGOT.get());
        this.getBuilder(ItemTagsPM.NUGGETS_HALLOWSTEEL).add(ItemsPM.HALLOWSTEEL_NUGGET.get());
        this.getBuilder(ItemTagsPM.NUGGETS_HEXIUM).add(ItemsPM.HEXIUM_NUGGET.get());
        this.getBuilder(ItemTagsPM.NUGGETS_PRIMALITE).add(ItemsPM.PRIMALITE_NUGGET.get());
        this.getBuilder(ItemTagsPM.STORAGE_BLOCKS_HALLOWSTEEL).add(ItemsPM.HALLOWSTEEL_BLOCK.get());
        this.getBuilder(ItemTagsPM.STORAGE_BLOCKS_HEXIUM).add(ItemsPM.HEXIUM_BLOCK.get());
        this.getBuilder(ItemTagsPM.STORAGE_BLOCKS_PRIMALITE).add(ItemsPM.PRIMALITE_BLOCK.get());
    }

    @Override
    protected void setCollection(TagCollection<Item> colectionIn) {
        // Do nothing
    }

    @Override
    protected Path makePath(ResourceLocation id) {
        return this.generator.getOutputFolder().resolve("data/" + id.getNamespace() + "/tags/items/" + id.getPath() + ".json");
    }
}
