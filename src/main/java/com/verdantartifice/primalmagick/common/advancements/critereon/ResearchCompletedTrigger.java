package com.verdantartifice.primalmagick.common.advancements.critereon;

import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.research.ResearchEntry;
import com.verdantartifice.primalmagick.common.research.keys.AbstractResearchKey;
import com.verdantartifice.primalmagick.common.research.keys.EntityScanKey;
import com.verdantartifice.primalmagick.common.research.keys.ItemScanKey;
import com.verdantartifice.primalmagick.common.research.keys.ResearchEntryKey;
import com.verdantartifice.primalmagick.common.research.keys.RuneEnchantmentKey;
import com.verdantartifice.primalmagick.common.research.keys.StackCraftedKey;
import com.verdantartifice.primalmagick.common.research.keys.TagCraftedKey;

import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.ItemLike;

/**
 * Advancement criterion that is triggered when the player completes a given research key.
 * 
 * @author Daedalus4096
 */
public class ResearchCompletedTrigger extends SimpleCriterionTrigger<ResearchCompletedTrigger.TriggerInstance> {
    public Codec<ResearchCompletedTrigger.TriggerInstance> codec() {
        return ResearchCompletedTrigger.TriggerInstance.codec();
    }
    
    public void trigger(ServerPlayer player, AbstractResearchKey<?> completedKey) {
        this.trigger(player, triggerInstance -> triggerInstance.matches(completedKey));
    }
    
    public static record TriggerInstance(Optional<ContextAwarePredicate> player, AbstractResearchKey<?> researchKey) implements SimpleCriterionTrigger.SimpleInstance {
        public static Codec<ResearchCompletedTrigger.TriggerInstance> codec() {
            return RecordCodecBuilder.create(instance -> instance.group(
                    EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(ResearchCompletedTrigger.TriggerInstance::player), 
                    AbstractResearchKey.dispatchCodec().fieldOf("researchKey").forGetter(ResearchCompletedTrigger.TriggerInstance::researchKey)
                ).apply(instance, ResearchCompletedTrigger.TriggerInstance::new));
        }
        
        public static Criterion<ResearchCompletedTrigger.TriggerInstance> entityScanned(EntityType<?> entityType) {
            return CriteriaTriggersPM.RESEARCH_COMPLETED.createCriterion(new ResearchCompletedTrigger.TriggerInstance(Optional.empty(), new EntityScanKey(entityType)));
        }
        
        public static Criterion<ResearchCompletedTrigger.TriggerInstance> itemScanned(ItemLike itemLike) {
            return CriteriaTriggersPM.RESEARCH_COMPLETED.createCriterion(new ResearchCompletedTrigger.TriggerInstance(Optional.empty(), new ItemScanKey(itemLike)));
        }
        
        public static Criterion<ResearchCompletedTrigger.TriggerInstance> itemScanned(ItemStack stack) {
            return CriteriaTriggersPM.RESEARCH_COMPLETED.createCriterion(new ResearchCompletedTrigger.TriggerInstance(Optional.empty(), new ItemScanKey(stack)));
        }
        
        public static Criterion<ResearchCompletedTrigger.TriggerInstance> researchEntry(ResourceKey<ResearchEntry> rawKey) {
            return CriteriaTriggersPM.RESEARCH_COMPLETED.createCriterion(new ResearchCompletedTrigger.TriggerInstance(Optional.empty(), new ResearchEntryKey(rawKey)));
        }
        
        public static Criterion<ResearchCompletedTrigger.TriggerInstance> runescribed(Holder<Enchantment> ench) {
            return CriteriaTriggersPM.RESEARCH_COMPLETED.createCriterion(new ResearchCompletedTrigger.TriggerInstance(Optional.empty(), new RuneEnchantmentKey(ench)));
        }
        
        public static Criterion<ResearchCompletedTrigger.TriggerInstance> stackCrafted(ItemLike itemLike) {
            return CriteriaTriggersPM.RESEARCH_COMPLETED.createCriterion(new ResearchCompletedTrigger.TriggerInstance(Optional.empty(), new StackCraftedKey(itemLike)));
        }
        
        public static Criterion<ResearchCompletedTrigger.TriggerInstance> stackCrafted(ItemStack stack) {
            return CriteriaTriggersPM.RESEARCH_COMPLETED.createCriterion(new ResearchCompletedTrigger.TriggerInstance(Optional.empty(), new StackCraftedKey(stack)));
        }
        
        public static Criterion<ResearchCompletedTrigger.TriggerInstance> tagCrafted(TagKey<Item> tagKey) {
            return CriteriaTriggersPM.RESEARCH_COMPLETED.createCriterion(new ResearchCompletedTrigger.TriggerInstance(Optional.empty(), new TagCraftedKey(tagKey)));
        }
        
        public boolean matches(AbstractResearchKey<?> completedKey) {
            return this.researchKey.equals(completedKey);
        }

        public Optional<ContextAwarePredicate> player() {
           return this.player;
        }
    }
}
