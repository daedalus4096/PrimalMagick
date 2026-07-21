package com.verdantartifice.primalmagick.common.trading;

import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.TradeCost;
import net.minecraft.world.item.trading.VillagerTrade;

import java.util.List;
import java.util.Optional;

public class VillagerTradesPM {
    public static final ResourceKey<VillagerTrade> LIBRARIAN_1_MYSTICAL_RELIC_FRAGMENT_EMERALD = key("librarian/1/mystical_relic_fragment_emerald");
    public static final ResourceKey<VillagerTrade> LIBRARIAN_2_EMERALD_OBSERVATION_NOTES = key("librarian/2/emerald_observation_notes");
    public static final ResourceKey<VillagerTrade> LIBRARIAN_3_MYSTICAL_RELIC_EMERALD = key("librarian/3/mystical_relic_emerald");
    public static final ResourceKey<VillagerTrade> LIBRARIAN_4_EMERALD_THEORY_NOTES = key("librarian/4/emerald_theory_notes");
    public static final ResourceKey<VillagerTrade> WANDERING_TRADER_EMERALD_OBSERVATION_NOTES = key("wandering_trader/emerald_observation_notes");
    public static final ResourceKey<VillagerTrade> WANDERING_TRADER_EMERALD_THEORY_NOTES = key("wandering_trader/emerald_theory_notes");

    public static void bootstrap(BootstrapContext<VillagerTrade> context) {
        register(context, LIBRARIAN_1_MYSTICAL_RELIC_FRAGMENT_EMERALD,
                new VillagerTrade(new TradeCost(ItemsPM.MYSTICAL_RELIC_FRAGMENT.get(), 3), new ItemStackTemplate(Items.EMERALD), 12, 2, 0.05F, Optional.empty(), List.of()));
        register(context, LIBRARIAN_2_EMERALD_OBSERVATION_NOTES,
                new VillagerTrade(new TradeCost(Items.EMERALD, 8), new ItemStackTemplate(ItemsPM.OBSERVATION_NOTES.get()), 12, 5, 0.05F, Optional.empty(), List.of()));
        register(context, LIBRARIAN_3_MYSTICAL_RELIC_EMERALD,
                new VillagerTrade(new TradeCost(ItemsPM.MYSTICAL_RELIC.get(), 1), new ItemStackTemplate(Items.EMERALD, 5), 12, 20, 0.05F, Optional.empty(), List.of()));
        register(context, LIBRARIAN_4_EMERALD_THEORY_NOTES,
                new VillagerTrade(new TradeCost(Items.EMERALD, 16), new ItemStackTemplate(ItemsPM.THEORY_NOTES.get()), 12, 15, 0.05F, Optional.empty(), List.of()));
        register(context, WANDERING_TRADER_EMERALD_OBSERVATION_NOTES,
                new VillagerTrade(new TradeCost(Items.EMERALD, 8), new ItemStackTemplate(ItemsPM.OBSERVATION_NOTES.get()), 12, 5, 0.05F, Optional.empty(), List.of()));
        register(context, WANDERING_TRADER_EMERALD_THEORY_NOTES,
                new VillagerTrade(new TradeCost(Items.EMERALD, 16), new ItemStackTemplate(ItemsPM.THEORY_NOTES.get()), 12, 15, 0.05F, Optional.empty(), List.of()));
    }

    public static Holder.Reference<VillagerTrade> register(BootstrapContext<VillagerTrade> context, ResourceKey<VillagerTrade> resourceKey, VillagerTrade villagerTrade) {
        return context.register(resourceKey, villagerTrade);
    }

    private static ResourceKey<VillagerTrade> key(String name) {
        return ResourceKey.create(Registries.VILLAGER_TRADE, ResourceUtils.loc(name));
    }
}
