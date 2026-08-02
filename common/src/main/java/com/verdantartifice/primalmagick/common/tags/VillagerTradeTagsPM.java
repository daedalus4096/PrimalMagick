package com.verdantartifice.primalmagick.common.tags;

import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.trading.VillagerTrade;

public class VillagerTradeTagsPM {
    public static final TagKey<VillagerTrade> FRIENDLY_WITCH_SELLING = create("friendly_witch/selling");

    private static TagKey<VillagerTrade> create(String name) {
        return TagKey.create(Registries.VILLAGER_TRADE, ResourceUtils.loc(name));
    }
}
