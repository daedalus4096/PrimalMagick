package com.verdantartifice.primalmagick.common.advancements.critereon;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.books.BookLanguage;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

/**
 * Advancement criterion that is triggered when the player reaches at least the given comprehension value.
 * 
 * @author Daedalus4096
 */
public class LinguisticsComprehensionTrigger extends SimpleCriterionTrigger<LinguisticsComprehensionTrigger.TriggerInstance> {
    public Codec<LinguisticsComprehensionTrigger.TriggerInstance> codec() {
        return LinguisticsComprehensionTrigger.TriggerInstance.codec();
    }
    
    public void trigger(ServerPlayer player, ResourceKey<BookLanguage> language, int value) {
        this.trigger(player, triggerInstance -> triggerInstance.matches(language, value));
    }
    
    public static record TriggerInstance(Optional<ContextAwarePredicate> player, ResourceKey<BookLanguage> language, int threshold) implements SimpleCriterionTrigger.SimpleInstance {
        public static Codec<LinguisticsComprehensionTrigger.TriggerInstance> codec() {
            return RecordCodecBuilder.create(instance -> instance.group(
                    EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(LinguisticsComprehensionTrigger.TriggerInstance::player), 
                    ResourceKey.codec(RegistryKeysPM.BOOK_LANGUAGES).fieldOf("language").forGetter(LinguisticsComprehensionTrigger.TriggerInstance::language),
                    Codec.INT.fieldOf("threshold").forGetter(LinguisticsComprehensionTrigger.TriggerInstance::threshold)
                ).apply(instance, LinguisticsComprehensionTrigger.TriggerInstance::new));
        }
        
        public static Criterion<LinguisticsComprehensionTrigger.TriggerInstance> atLeast(ResourceKey<BookLanguage> language, int threshold) {
            return CriteriaTriggersPM.LINGUISTICS_COMPREHENSION.createCriterion(new LinguisticsComprehensionTrigger.TriggerInstance(Optional.empty(), language, threshold));
        }
        
        public boolean matches(ResourceKey<BookLanguage> language, int value) {
            return this.language.equals(language) && value >= this.threshold;
        }

        public Optional<ContextAwarePredicate> player() {
           return this.player;
        }
    }
}
