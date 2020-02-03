package com.verdantartifice.primalmagic.datagen;

import java.nio.file.Path;

import com.verdantartifice.primalmagic.common.blocks.BlocksPM;
import com.verdantartifice.primalmagic.common.items.ItemsPM;
import com.verdantartifice.primalmagic.common.tags.ItemTagsPM;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.TagsProvider;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagCollection;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

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
        this.getBuilder(ItemTags.LEAVES).add(BlocksPM.MOONWOOD_LEAVES.asItem(), BlocksPM.SUNWOOD_LEAVES.asItem());
        this.getBuilder(ItemTags.PLANKS).add(BlocksPM.MOONWOOD_PLANKS.asItem(), BlocksPM.SUNWOOD_PLANKS.asItem());
        this.getBuilder(ItemTags.SAPLINGS).add(BlocksPM.MOONWOOD_SAPLING.asItem(), BlocksPM.SUNWOOD_SAPLING.asItem());
        this.getBuilder(ItemTags.WALLS).add(BlocksPM.MARBLE_WALL.asItem(), BlocksPM.MARBLE_BRICK_WALL.asItem(), BlocksPM.MARBLE_ENCHANTED_WALL.asItem(), BlocksPM.MARBLE_ENCHANTED_BRICK_WALL.asItem(), BlocksPM.MARBLE_SMOKED_WALL.asItem(), BlocksPM.MARBLE_SMOKED_BRICK_WALL.asItem());
        this.getBuilder(ItemTags.WOODEN_SLABS).add(BlocksPM.MOONWOOD_SLAB.asItem(), BlocksPM.SUNWOOD_SLAB.asItem());
        this.getBuilder(ItemTags.WOODEN_STAIRS).add(BlocksPM.MOONWOOD_STAIRS.asItem(), BlocksPM.SUNWOOD_STAIRS.asItem());

        // Create custom tags
        this.getBuilder(ItemTagsPM.COLORED_SHULKER_BOXES).add(Items.BLACK_SHULKER_BOX, Items.BLUE_SHULKER_BOX, Items.BROWN_SHULKER_BOX, Items.CYAN_SHULKER_BOX, Items.GRAY_SHULKER_BOX, Items.GREEN_SHULKER_BOX, Items.LIGHT_BLUE_SHULKER_BOX, Items.LIGHT_GRAY_SHULKER_BOX, Items.LIME_SHULKER_BOX, Items.MAGENTA_SHULKER_BOX, Items.ORANGE_SHULKER_BOX, Items.PINK_SHULKER_BOX, Items.PURPLE_SHULKER_BOX, Items.RED_SHULKER_BOX, Items.WHITE_SHULKER_BOX, Items.YELLOW_SHULKER_BOX);
        this.getBuilder(ItemTagsPM.CONCRETE).add(Items.BLACK_CONCRETE, Items.BLUE_CONCRETE, Items.BROWN_CONCRETE, Items.CYAN_CONCRETE, Items.GRAY_CONCRETE, Items.GREEN_CONCRETE, Items.LIGHT_BLUE_CONCRETE, Items.LIGHT_GRAY_CONCRETE, Items.LIME_CONCRETE, Items.MAGENTA_CONCRETE, Items.ORANGE_CONCRETE, Items.PINK_CONCRETE, Items.PURPLE_CONCRETE, Items.RED_CONCRETE, Items.WHITE_CONCRETE, Items.YELLOW_CONCRETE);
        this.getBuilder(ItemTagsPM.CORAL_BLOCKS).add(Items.BRAIN_CORAL_BLOCK, Items.BUBBLE_CORAL_BLOCK, Items.FIRE_CORAL_BLOCK, Items.HORN_CORAL_BLOCK, Items.TUBE_CORAL_BLOCK);
        this.getBuilder(ItemTagsPM.DEAD_CORAL_BLOCKS).add(Items.DEAD_BRAIN_CORAL_BLOCK, Items.DEAD_BUBBLE_CORAL_BLOCK, Items.DEAD_FIRE_CORAL_BLOCK, Items.DEAD_HORN_CORAL_BLOCK, Items.DEAD_TUBE_CORAL_BLOCK);
        this.getBuilder(ItemTagsPM.DEAD_CORAL_PLANTS).add(Items.DEAD_BRAIN_CORAL, Items.DEAD_BUBBLE_CORAL, Items.DEAD_FIRE_CORAL, Items.DEAD_HORN_CORAL, Items.DEAD_TUBE_CORAL);
        this.getBuilder(ItemTagsPM.DEAD_CORALS).add(ItemTagsPM.DEAD_CORAL_PLANTS).add(Items.DEAD_BRAIN_CORAL_FAN, Items.DEAD_BUBBLE_CORAL_FAN, Items.DEAD_FIRE_CORAL_FAN, Items.DEAD_HORN_CORAL_FAN, Items.DEAD_TUBE_CORAL_FAN);
        this.getBuilder(ItemTagsPM.ESSENCES).add(ItemTagsPM.ESSENCES_DUSTS, ItemTagsPM.ESSENCES_SHARDS, ItemTagsPM.ESSENCES_CRYSTALS, ItemTagsPM.ESSENCES_CLUSTERS);
        this.getBuilder(ItemTagsPM.MOONWOOD_LOGS).add(BlocksPM.MOONWOOD_LOG.asItem(), BlocksPM.STRIPPED_MOONWOOD_LOG.asItem(), BlocksPM.MOONWOOD_WOOD.asItem(), BlocksPM.STRIPPED_MOONWOOD_WOOD.asItem());
        this.getBuilder(ItemTagsPM.SHULKER_BOXES).add(ItemTagsPM.COLORED_SHULKER_BOXES).add(Items.SHULKER_BOX);
        this.getBuilder(ItemTagsPM.SUNWOOD_LOGS).add(BlocksPM.SUNWOOD_LOG.asItem(), BlocksPM.STRIPPED_SUNWOOD_LOG.asItem(), BlocksPM.SUNWOOD_WOOD.asItem(), BlocksPM.STRIPPED_SUNWOOD_WOOD.asItem());
        
        this.getBuilder(ItemTagsPM.ESSENCES_DUSTS).add(ItemTagsPM.ESSENCES_TERRESTRIAL_DUSTS).add(ItemsPM.ESSENCE_DUST_BLOOD, ItemsPM.ESSENCE_DUST_INFERNAL, ItemsPM.ESSENCE_DUST_VOID, ItemsPM.ESSENCE_DUST_HALLOWED);
        this.getBuilder(ItemTagsPM.ESSENCES_TERRESTRIAL_DUSTS).add(ItemsPM.ESSENCE_DUST_EARTH, ItemsPM.ESSENCE_DUST_SEA, ItemsPM.ESSENCE_DUST_SKY, ItemsPM.ESSENCE_DUST_SUN, ItemsPM.ESSENCE_DUST_MOON);
        this.getBuilder(ItemTagsPM.ESSENCES_SHARDS).add(ItemsPM.ESSENCE_SHARD_EARTH, ItemsPM.ESSENCE_SHARD_SEA, ItemsPM.ESSENCE_SHARD_SKY, ItemsPM.ESSENCE_SHARD_SUN, ItemsPM.ESSENCE_SHARD_MOON, ItemsPM.ESSENCE_SHARD_BLOOD, ItemsPM.ESSENCE_SHARD_INFERNAL, ItemsPM.ESSENCE_SHARD_VOID, ItemsPM.ESSENCE_SHARD_HALLOWED);
        this.getBuilder(ItemTagsPM.ESSENCES_CRYSTALS).add(ItemsPM.ESSENCE_CRYSTAL_EARTH, ItemsPM.ESSENCE_CRYSTAL_SEA, ItemsPM.ESSENCE_CRYSTAL_SKY, ItemsPM.ESSENCE_CRYSTAL_SUN, ItemsPM.ESSENCE_CRYSTAL_MOON, ItemsPM.ESSENCE_CRYSTAL_BLOOD, ItemsPM.ESSENCE_CRYSTAL_INFERNAL, ItemsPM.ESSENCE_CRYSTAL_VOID, ItemsPM.ESSENCE_CRYSTAL_HALLOWED);
        this.getBuilder(ItemTagsPM.ESSENCES_CLUSTERS).add(ItemsPM.ESSENCE_CLUSTER_EARTH, ItemsPM.ESSENCE_CLUSTER_SEA, ItemsPM.ESSENCE_CLUSTER_SKY, ItemsPM.ESSENCE_CLUSTER_SUN, ItemsPM.ESSENCE_CLUSTER_MOON, ItemsPM.ESSENCE_CLUSTER_BLOOD, ItemsPM.ESSENCE_CLUSTER_INFERNAL, ItemsPM.ESSENCE_CLUSTER_VOID, ItemsPM.ESSENCE_CLUSTER_HALLOWED);
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
