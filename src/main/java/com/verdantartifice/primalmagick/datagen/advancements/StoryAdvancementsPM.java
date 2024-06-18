package com.verdantartifice.primalmagick.datagen.advancements;

import java.util.function.Consumer;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.worldgen.structures.StructuresPM;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider.AdvancementGenerator;

/**
 * Data sub-provider for generating mod advancements.
 * 
 * @author Daedalus4096
 */
public class StoryAdvancementsPM implements AdvancementGenerator {
    @Override
    public void generate(Provider registries, Consumer<AdvancementHolder> saver, ExistingFileHelper existingFileHelper) {
        AdvancementHolder root = Advancement.Builder.advancement().display(DisplayInfoBuilder.id("root").icon(ItemsPM.GRIMOIRE.get()).background(PrimalMagick.resource("textures/block/marble_raw.png")).build())
                .requirements(AdvancementRequirements.Strategy.OR)
                .addCriterion("earth_shrine", PlayerTrigger.TriggerInstance.located(LocationPredicate.Builder.inStructure(StructuresPM.EARTH_SHRINE)))
                .addCriterion("sea_shrine", PlayerTrigger.TriggerInstance.located(LocationPredicate.Builder.inStructure(StructuresPM.SEA_SHRINE)))
                .addCriterion("sky_shrine", PlayerTrigger.TriggerInstance.located(LocationPredicate.Builder.inStructure(StructuresPM.SKY_SHRINE)))
                .addCriterion("sun_shrine", PlayerTrigger.TriggerInstance.located(LocationPredicate.Builder.inStructure(StructuresPM.SUN_SHRINE)))
                .addCriterion("moon_shrine", PlayerTrigger.TriggerInstance.located(LocationPredicate.Builder.inStructure(StructuresPM.MOON_SHRINE)))
                .save(saver, PrimalMagick.resource("story/root"));
        AdvancementHolder craftMundaneWand = Advancement.Builder.advancement().display(DisplayInfoBuilder.id("craft_mundane_wand").icon(ItemsPM.MUNDANE_WAND.get()).build())
                .parent(root)
                .addCriterion("has_mundane_wand", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.MUNDANE_WAND.get()))
                .save(saver, PrimalMagick.resource("story/craft_mundane_wand"));
        AdvancementHolder craftGrimoire = Advancement.Builder.advancement().display(DisplayInfoBuilder.id("craft_grimoire").icon(Items.BOOKSHELF).build())
                .parent(craftMundaneWand)
                .addCriterion("has_grimoire", InventoryChangeTrigger.TriggerInstance.hasItems(ItemsPM.GRIMOIRE.get()))
                .save(saver, PrimalMagick.resource("story/craft_grimoire"));
    }
}
