package com.verdantartifice.primalmagick.common.trading;

import com.verdantartifice.primalmagick.common.tags.VillagerTradeTagsPM;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.trading.TradeSet;
import net.minecraft.world.item.trading.TradeSets;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

public class TradeSetsPM {
    public static final ResourceKey<TradeSet> FRIENDLY_WITCH_SELLING = key("friendly_witch/selling");

    public static void bootstrap(BootstrapContext<TradeSet> context) {
        TradeSets.register(context, FRIENDLY_WITCH_SELLING, VillagerTradeTagsPM.FRIENDLY_WITCH_SELLING, ConstantValue.exactly(3F));
    }

    public static ResourceKey<TradeSet> key(String path) {
        return ResourceKey.create(Registries.TRADE_SET, ResourceUtils.loc(path));
    }
}
