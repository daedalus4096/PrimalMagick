package com.verdantartifice.primalmagic.datagen.tags;

import java.nio.file.Path;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.items.ItemsPM;
import com.verdantartifice.primalmagic.common.tags.ItemTagsForgeExt;
import com.verdantartifice.primalmagic.common.tags.ItemTagsPM;

import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

/**
 * Data provider for all of the mod's item tags, both original tags and modifications to vanilla tags.
 * 
 * @author Daedalus4096
 */
public class ItemTagsProvider extends TagsProvider<Item> {
    @SuppressWarnings("deprecation")
    public ItemTagsProvider(DataGenerator generator, ExistingFileHelper helper) {
        super(generator, Registry.ITEM, PrimalMagic.MODID, helper);
    }

    @Override
    public String getName() {
        return "Primal Magic Item Tags";
    }

    @Override
    protected void addTags() {
        // Add entries to vanilla tags
        this.tag(ItemTags.BEACON_PAYMENT_ITEMS).addTag(ItemTagsPM.INGOTS_PRIMALITE).addTag(ItemTagsPM.INGOTS_HEXIUM).addTag(ItemTagsPM.INGOTS_HALLOWSTEEL);
        this.tag(ItemTags.LOGS).addTag(ItemTagsPM.MOONWOOD_LOGS).addTag(ItemTagsPM.SUNWOOD_LOGS).addTag(ItemTagsPM.HALLOWOOD_LOGS);
        this.tag(ItemTags.LEAVES).add(ItemsPM.MOONWOOD_LEAVES.get(), ItemsPM.SUNWOOD_LEAVES.get(), ItemsPM.HALLOWOOD_LEAVES.get());
        this.tag(ItemTags.PLANKS).add(ItemsPM.MOONWOOD_PLANKS.get(), ItemsPM.SUNWOOD_PLANKS.get(), ItemsPM.HALLOWOOD_PLANKS.get());
        this.tag(ItemTags.SAPLINGS).add(ItemsPM.MOONWOOD_SAPLING.get(), ItemsPM.SUNWOOD_SAPLING.get(), ItemsPM.HALLOWOOD_SAPLING.get());
        this.tag(ItemTags.WALLS).add(ItemsPM.MARBLE_WALL.get(), ItemsPM.MARBLE_BRICK_WALL.get(), ItemsPM.MARBLE_ENCHANTED_WALL.get(), ItemsPM.MARBLE_ENCHANTED_BRICK_WALL.get(), ItemsPM.MARBLE_SMOKED_WALL.get(), ItemsPM.MARBLE_SMOKED_BRICK_WALL.get());
        this.tag(ItemTags.WOODEN_SLABS).add(ItemsPM.MOONWOOD_SLAB.get(), ItemsPM.SUNWOOD_SLAB.get(), ItemsPM.HALLOWOOD_SLAB.get());
        this.tag(ItemTags.WOODEN_STAIRS).add(ItemsPM.MOONWOOD_STAIRS.get(), ItemsPM.SUNWOOD_STAIRS.get(), ItemsPM.HALLOWOOD_STAIRS.get());
        
        // Add entries to Forge tags
        this.tag(Tags.Items.DUSTS).addTag(ItemTagsForgeExt.DUSTS_IRON).addTag(ItemTagsForgeExt.DUSTS_GOLD);
        this.tag(Tags.Items.INGOTS).addTag(ItemTagsPM.INGOTS_PRIMALITE).addTag(ItemTagsPM.INGOTS_HEXIUM).addTag(ItemTagsPM.INGOTS_HALLOWSTEEL);
        this.tag(Tags.Items.NUGGETS).addTag(ItemTagsPM.NUGGETS_PRIMALITE).addTag(ItemTagsPM.NUGGETS_HEXIUM).addTag(ItemTagsPM.NUGGETS_HALLOWSTEEL).addTag(ItemTagsForgeExt.NUGGETS_QUARTZ);
        this.tag(Tags.Items.ORES_QUARTZ).add(ItemsPM.QUARTZ_ORE.get());
        this.tag(Tags.Items.STORAGE_BLOCKS).addTag(ItemTagsPM.STORAGE_BLOCKS_PRIMALITE).addTag(ItemTagsPM.STORAGE_BLOCKS_HEXIUM).addTag(ItemTagsPM.STORAGE_BLOCKS_HALLOWSTEEL);
        
        this.tag(Tags.Items.GLASS_COLORLESS).add(ItemsPM.SKYGLASS.get());
        this.tag(Tags.Items.GLASS_BLACK).add(ItemsPM.STAINED_SKYGLASS_BLACK.get());
        this.tag(Tags.Items.GLASS_BLUE).add(ItemsPM.STAINED_SKYGLASS_BLUE.get());
        this.tag(Tags.Items.GLASS_BROWN).add(ItemsPM.STAINED_SKYGLASS_BROWN.get());
        this.tag(Tags.Items.GLASS_CYAN).add(ItemsPM.STAINED_SKYGLASS_CYAN.get());
        this.tag(Tags.Items.GLASS_GRAY).add(ItemsPM.STAINED_SKYGLASS_GRAY.get());
        this.tag(Tags.Items.GLASS_GREEN).add(ItemsPM.STAINED_SKYGLASS_GREEN.get());
        this.tag(Tags.Items.GLASS_LIGHT_BLUE).add(ItemsPM.STAINED_SKYGLASS_LIGHT_BLUE.get());
        this.tag(Tags.Items.GLASS_LIGHT_GRAY).add(ItemsPM.STAINED_SKYGLASS_LIGHT_GRAY.get());
        this.tag(Tags.Items.GLASS_LIME).add(ItemsPM.STAINED_SKYGLASS_LIME.get());
        this.tag(Tags.Items.GLASS_MAGENTA).add(ItemsPM.STAINED_SKYGLASS_MAGENTA.get());
        this.tag(Tags.Items.GLASS_ORANGE).add(ItemsPM.STAINED_SKYGLASS_ORANGE.get());
        this.tag(Tags.Items.GLASS_PINK).add(ItemsPM.STAINED_SKYGLASS_PINK.get());
        this.tag(Tags.Items.GLASS_PURPLE).add(ItemsPM.STAINED_SKYGLASS_PURPLE.get());
        this.tag(Tags.Items.GLASS_RED).add(ItemsPM.STAINED_SKYGLASS_RED.get());
        this.tag(Tags.Items.GLASS_WHITE).add(ItemsPM.STAINED_SKYGLASS_WHITE.get());
        this.tag(Tags.Items.GLASS_YELLOW).add(ItemsPM.STAINED_SKYGLASS_YELLOW.get());
        this.tag(Tags.Items.STAINED_GLASS).addTag(ItemTagsPM.STAINED_SKYGLASS);
        
        this.tag(Tags.Items.GLASS_PANES_COLORLESS).add(ItemsPM.SKYGLASS_PANE.get());
        this.tag(Tags.Items.GLASS_PANES_BLACK).add(ItemsPM.STAINED_SKYGLASS_PANE_BLACK.get());
        this.tag(Tags.Items.GLASS_PANES_BLUE).add(ItemsPM.STAINED_SKYGLASS_PANE_BLUE.get());
        this.tag(Tags.Items.GLASS_PANES_BROWN).add(ItemsPM.STAINED_SKYGLASS_PANE_BROWN.get());
        this.tag(Tags.Items.GLASS_PANES_CYAN).add(ItemsPM.STAINED_SKYGLASS_PANE_CYAN.get());
        this.tag(Tags.Items.GLASS_PANES_GRAY).add(ItemsPM.STAINED_SKYGLASS_PANE_GRAY.get());
        this.tag(Tags.Items.GLASS_PANES_GREEN).add(ItemsPM.STAINED_SKYGLASS_PANE_GREEN.get());
        this.tag(Tags.Items.GLASS_PANES_LIGHT_BLUE).add(ItemsPM.STAINED_SKYGLASS_PANE_LIGHT_BLUE.get());
        this.tag(Tags.Items.GLASS_PANES_LIGHT_GRAY).add(ItemsPM.STAINED_SKYGLASS_PANE_LIGHT_GRAY.get());
        this.tag(Tags.Items.GLASS_PANES_LIME).add(ItemsPM.STAINED_SKYGLASS_PANE_LIME.get());
        this.tag(Tags.Items.GLASS_PANES_MAGENTA).add(ItemsPM.STAINED_SKYGLASS_PANE_MAGENTA.get());
        this.tag(Tags.Items.GLASS_PANES_ORANGE).add(ItemsPM.STAINED_SKYGLASS_PANE_ORANGE.get());
        this.tag(Tags.Items.GLASS_PANES_PINK).add(ItemsPM.STAINED_SKYGLASS_PANE_PINK.get());
        this.tag(Tags.Items.GLASS_PANES_PURPLE).add(ItemsPM.STAINED_SKYGLASS_PANE_PURPLE.get());
        this.tag(Tags.Items.GLASS_PANES_RED).add(ItemsPM.STAINED_SKYGLASS_PANE_RED.get());
        this.tag(Tags.Items.GLASS_PANES_WHITE).add(ItemsPM.STAINED_SKYGLASS_PANE_WHITE.get());
        this.tag(Tags.Items.GLASS_PANES_YELLOW).add(ItemsPM.STAINED_SKYGLASS_PANE_YELLOW.get());
        this.tag(Tags.Items.STAINED_GLASS_PANES).addTag(ItemTagsPM.STAINED_SKYGLASS_PANES);

        // Add entries to Forge extension tags
        this.tag(ItemTagsForgeExt.DUSTS_GOLD).add(ItemsPM.GOLD_GRIT.get());
        this.tag(ItemTagsForgeExt.DUSTS_IRON).add(ItemsPM.IRON_GRIT.get());
        this.tag(ItemTagsForgeExt.NUGGETS_QUARTZ).add(ItemsPM.QUARTZ_NUGGET.get());

        // Create custom tags
        this.tag(ItemTagsPM.COLORED_SHULKER_BOXES).add(Items.BLACK_SHULKER_BOX, Items.BLUE_SHULKER_BOX, Items.BROWN_SHULKER_BOX, Items.CYAN_SHULKER_BOX, Items.GRAY_SHULKER_BOX, Items.GREEN_SHULKER_BOX, Items.LIGHT_BLUE_SHULKER_BOX, Items.LIGHT_GRAY_SHULKER_BOX, Items.LIME_SHULKER_BOX, Items.MAGENTA_SHULKER_BOX, Items.ORANGE_SHULKER_BOX, Items.PINK_SHULKER_BOX, Items.PURPLE_SHULKER_BOX, Items.RED_SHULKER_BOX, Items.WHITE_SHULKER_BOX, Items.YELLOW_SHULKER_BOX);
        this.tag(ItemTagsPM.CONCRETE).add(Items.BLACK_CONCRETE, Items.BLUE_CONCRETE, Items.BROWN_CONCRETE, Items.CYAN_CONCRETE, Items.GRAY_CONCRETE, Items.GREEN_CONCRETE, Items.LIGHT_BLUE_CONCRETE, Items.LIGHT_GRAY_CONCRETE, Items.LIME_CONCRETE, Items.MAGENTA_CONCRETE, Items.ORANGE_CONCRETE, Items.PINK_CONCRETE, Items.PURPLE_CONCRETE, Items.RED_CONCRETE, Items.WHITE_CONCRETE, Items.YELLOW_CONCRETE);
        this.tag(ItemTagsPM.CORAL_BLOCKS).add(Items.BRAIN_CORAL_BLOCK, Items.BUBBLE_CORAL_BLOCK, Items.FIRE_CORAL_BLOCK, Items.HORN_CORAL_BLOCK, Items.TUBE_CORAL_BLOCK);
        this.tag(ItemTagsPM.DEAD_CORAL_BLOCKS).add(Items.DEAD_BRAIN_CORAL_BLOCK, Items.DEAD_BUBBLE_CORAL_BLOCK, Items.DEAD_FIRE_CORAL_BLOCK, Items.DEAD_HORN_CORAL_BLOCK, Items.DEAD_TUBE_CORAL_BLOCK);
        this.tag(ItemTagsPM.DEAD_CORAL_PLANTS).add(Items.DEAD_BRAIN_CORAL, Items.DEAD_BUBBLE_CORAL, Items.DEAD_FIRE_CORAL, Items.DEAD_HORN_CORAL, Items.DEAD_TUBE_CORAL);
        this.tag(ItemTagsPM.DEAD_CORALS).addTag(ItemTagsPM.DEAD_CORAL_PLANTS).add(Items.DEAD_BRAIN_CORAL_FAN, Items.DEAD_BUBBLE_CORAL_FAN, Items.DEAD_FIRE_CORAL_FAN, Items.DEAD_HORN_CORAL_FAN, Items.DEAD_TUBE_CORAL_FAN);
        this.tag(ItemTagsPM.ESSENCES).addTag(ItemTagsPM.ESSENCES_DUSTS).addTag(ItemTagsPM.ESSENCES_SHARDS).addTag(ItemTagsPM.ESSENCES_CRYSTALS).addTag(ItemTagsPM.ESSENCES_CLUSTERS);
        this.tag(ItemTagsPM.HALLOWOOD_LOGS).add(ItemsPM.HALLOWOOD_LOG.get(), ItemsPM.STRIPPED_HALLOWOOD_LOG.get(), ItemsPM.HALLOWOOD_WOOD.get(), ItemsPM.STRIPPED_HALLOWOOD_WOOD.get());
        this.tag(ItemTagsPM.MAGICAL_CLOTH).add(ItemsPM.SPELLCLOTH.get(), ItemsPM.HEXWEAVE.get(), ItemsPM.SAINTSWOOL.get());
        this.tag(ItemTagsPM.MOONWOOD_LOGS).add(ItemsPM.MOONWOOD_LOG.get(), ItemsPM.STRIPPED_MOONWOOD_LOG.get(), ItemsPM.MOONWOOD_WOOD.get(), ItemsPM.STRIPPED_MOONWOOD_WOOD.get());
        this.tag(ItemTagsPM.RITUAL_CANDLES).add(ItemsPM.RITUAL_CANDLE_BLACK.get(), ItemsPM.RITUAL_CANDLE_BLUE.get(), ItemsPM.RITUAL_CANDLE_BROWN.get(), ItemsPM.RITUAL_CANDLE_CYAN.get(), ItemsPM.RITUAL_CANDLE_GRAY.get(), ItemsPM.RITUAL_CANDLE_GREEN.get(), ItemsPM.RITUAL_CANDLE_LIGHT_BLUE.get(), ItemsPM.RITUAL_CANDLE_LIGHT_GRAY.get(), ItemsPM.RITUAL_CANDLE_LIME.get(), ItemsPM.RITUAL_CANDLE_MAGENTA.get(), ItemsPM.RITUAL_CANDLE_ORANGE.get(), ItemsPM.RITUAL_CANDLE_PINK.get(), ItemsPM.RITUAL_CANDLE_PURPLE.get(), ItemsPM.RITUAL_CANDLE_RED.get(), ItemsPM.RITUAL_CANDLE_WHITE.get(), ItemsPM.RITUAL_CANDLE_YELLOW.get());
        this.tag(ItemTagsPM.SHULKER_BOXES).addTag(ItemTagsPM.COLORED_SHULKER_BOXES).add(Items.SHULKER_BOX);
        this.tag(ItemTagsPM.SKYGLASS).add(ItemsPM.SKYGLASS.get()).addTag(ItemTagsPM.STAINED_SKYGLASS);
        this.tag(ItemTagsPM.SKYGLASS_PANES).add(ItemsPM.SKYGLASS_PANE.get()).addTag(ItemTagsPM.STAINED_SKYGLASS_PANES);
        this.tag(ItemTagsPM.STAINED_SKYGLASS).add(ItemsPM.STAINED_SKYGLASS_BLACK.get(), ItemsPM.STAINED_SKYGLASS_BLUE.get(), ItemsPM.STAINED_SKYGLASS_BROWN.get(), ItemsPM.STAINED_SKYGLASS_CYAN.get(), ItemsPM.STAINED_SKYGLASS_GRAY.get(), ItemsPM.STAINED_SKYGLASS_GREEN.get(), ItemsPM.STAINED_SKYGLASS_LIGHT_BLUE.get(), ItemsPM.STAINED_SKYGLASS_LIGHT_GRAY.get(), ItemsPM.STAINED_SKYGLASS_LIME.get(), ItemsPM.STAINED_SKYGLASS_MAGENTA.get(), ItemsPM.STAINED_SKYGLASS_ORANGE.get(), ItemsPM.STAINED_SKYGLASS_PINK.get(), ItemsPM.STAINED_SKYGLASS_PURPLE.get(), ItemsPM.STAINED_SKYGLASS_RED.get(), ItemsPM.STAINED_SKYGLASS_WHITE.get(), ItemsPM.STAINED_SKYGLASS_YELLOW.get());
        this.tag(ItemTagsPM.STAINED_SKYGLASS_PANES).add(ItemsPM.STAINED_SKYGLASS_PANE_BLACK.get(), ItemsPM.STAINED_SKYGLASS_PANE_BLUE.get(), ItemsPM.STAINED_SKYGLASS_PANE_BROWN.get(), ItemsPM.STAINED_SKYGLASS_PANE_CYAN.get(), ItemsPM.STAINED_SKYGLASS_PANE_GRAY.get(), ItemsPM.STAINED_SKYGLASS_PANE_GREEN.get(), ItemsPM.STAINED_SKYGLASS_PANE_LIGHT_BLUE.get(), ItemsPM.STAINED_SKYGLASS_PANE_LIGHT_GRAY.get(), ItemsPM.STAINED_SKYGLASS_PANE_LIME.get(), ItemsPM.STAINED_SKYGLASS_PANE_MAGENTA.get(), ItemsPM.STAINED_SKYGLASS_PANE_ORANGE.get(), ItemsPM.STAINED_SKYGLASS_PANE_PINK.get(), ItemsPM.STAINED_SKYGLASS_PANE_PURPLE.get(), ItemsPM.STAINED_SKYGLASS_PANE_RED.get(), ItemsPM.STAINED_SKYGLASS_PANE_WHITE.get(), ItemsPM.STAINED_SKYGLASS_PANE_YELLOW.get());
        this.tag(ItemTagsPM.SUNWOOD_LOGS).add(ItemsPM.SUNWOOD_LOG.get(), ItemsPM.STRIPPED_SUNWOOD_LOG.get(), ItemsPM.SUNWOOD_WOOD.get(), ItemsPM.STRIPPED_SUNWOOD_WOOD.get());
        
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
    }

    @Override
    protected Path getPath(ResourceLocation id) {
        return this.generator.getOutputFolder().resolve("data/" + id.getNamespace() + "/tags/items/" + id.getPath() + ".json");
    }
}
