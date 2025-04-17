package com.verdantartifice.primalmagick.common.advancements.critereon;

import com.verdantartifice.primalmagick.common.registries.IRegistryItem;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.advancements.CriterionTrigger;

import java.util.function.Supplier;

public class CriteriaTriggersPM {
    public static void init() {
        // Pass the service initialization through this class so it gets class loaded and fields registered
        Services.CRITERION_TRIGGERS_REGISTRY.init();
    }

    public static final IRegistryItem<CriterionTrigger<?>, ResearchCompletedTrigger> RESEARCH_COMPLETED = register("research_completed", ResearchCompletedTrigger::new);
    public static final IRegistryItem<CriterionTrigger<?>, StatValueTrigger> STAT_VALUE = register("stat_value", StatValueTrigger::new);
    public static final IRegistryItem<CriterionTrigger<?>, LinguisticsComprehensionTrigger> LINGUISTICS_COMPREHENSION = register("linguistics_comprehension", LinguisticsComprehensionTrigger::new);
    public static final IRegistryItem<CriterionTrigger<?>, RunescribingTrigger> RUNESCRIBING = register("runescribing", RunescribingTrigger::new);
    public static final IRegistryItem<CriterionTrigger<?>, RecallStoneTrigger> RECALL_STONE = register("recall_stone", RecallStoneTrigger::new);
    public static final IRegistryItem<CriterionTrigger<?>, EntityHurtPlayerTriggerExt> ENTITY_HURT_PLAYER_EXT = register("entity_hurt_player_ext", EntityHurtPlayerTriggerExt::new);
    public static final IRegistryItem<CriterionTrigger<?>, AttunementThresholdTrigger> ATTUNEMENT_THRESHOLD = register("attunement_threshold", AttunementThresholdTrigger::new);
    public static final IRegistryItem<CriterionTrigger<?>, RuneUseCountTrigger> RUNE_USE_COUNT = register("rune_use_count", RuneUseCountTrigger::new);
    public static final IRegistryItem<CriterionTrigger<?>, ScanLocationTrigger> SCAN_LOCATION = register("scan_location", ScanLocationTrigger::new);
    public static final IRegistryItem<CriterionTrigger<?>, ManaNetworkRouteLengthTrigger> MANA_NETWORK_ROUTE_LENGTH = register("mana_network_route_length", ManaNetworkRouteLengthTrigger::new);
    public static final IRegistryItem<CriterionTrigger<?>, ManaNetworkSiphonTrigger> MANA_NETWORK_SIPHON = register("mana_network_siphon", ManaNetworkSiphonTrigger::new);

    private static <T extends CriterionTrigger<?>> IRegistryItem<CriterionTrigger<?>, T> register(String name, Supplier<T> supplier) {
        return Services.CRITERION_TRIGGERS_REGISTRY.register(name, supplier);
    }
}
