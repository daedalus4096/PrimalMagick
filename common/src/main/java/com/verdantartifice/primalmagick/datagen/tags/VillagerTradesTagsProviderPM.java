package com.verdantartifice.primalmagick.datagen.tags;

import com.verdantartifice.primalmagick.common.trading.VillagerTradesPM;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.KeyTagProvider;
import net.minecraft.tags.VillagerTradeTags;
import net.minecraft.world.item.trading.VillagerTrade;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class VillagerTradesTagsProviderPM extends KeyTagProvider<VillagerTrade> {
    public VillagerTradesTagsProviderPM(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, Registries.VILLAGER_TRADE, lookupProvider);
    }

    @Override
    protected void addTags(@NotNull HolderLookup.Provider registries) {
        this.tag(VillagerTradeTags.LIBRARIAN_LEVEL_1).add(VillagerTradesPM.LIBRARIAN_1_MYSTICAL_RELIC_FRAGMENT_EMERALD);
        this.tag(VillagerTradeTags.LIBRARIAN_LEVEL_2).add(VillagerTradesPM.LIBRARIAN_2_EMERALD_OBSERVATION_NOTES);
        this.tag(VillagerTradeTags.LIBRARIAN_LEVEL_3).add(VillagerTradesPM.LIBRARIAN_3_MYSTICAL_RELIC_EMERALD);
        this.tag(VillagerTradeTags.LIBRARIAN_LEVEL_4).add(VillagerTradesPM.LIBRARIAN_4_EMERALD_THEORY_NOTES);
        this.tag(VillagerTradeTags.WANDERING_TRADER_COMMON).add(VillagerTradesPM.WANDERING_TRADER_EMERALD_OBSERVATION_NOTES);
        this.tag(VillagerTradeTags.WANDERING_TRADER_UNCOMMON).add(VillagerTradesPM.WANDERING_TRADER_EMERALD_THEORY_NOTES);
    }
}
