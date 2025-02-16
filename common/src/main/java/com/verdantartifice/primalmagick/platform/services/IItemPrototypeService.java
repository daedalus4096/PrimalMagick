package com.verdantartifice.primalmagick.platform.services;

import com.verdantartifice.primalmagick.common.entities.companions.pixies.AbstractPixieEntity;
import com.verdantartifice.primalmagick.common.items.misc.ArcanometerItem;
import com.verdantartifice.primalmagick.common.items.misc.BurnableBlockItem;
import com.verdantartifice.primalmagick.common.items.misc.EarthshatterHammerItem;
import com.verdantartifice.primalmagick.common.items.misc.IgnyxItem;
import com.verdantartifice.primalmagick.common.items.misc.ManaFontBlockItem;
import com.verdantartifice.primalmagick.common.items.misc.SpellcraftingAltarBlockItem;
import com.verdantartifice.primalmagick.common.items.tools.ForbiddenBowItem;
import com.verdantartifice.primalmagick.common.items.tools.ForbiddenTridentItem;
import com.verdantartifice.primalmagick.common.items.tools.HallowsteelShieldItem;
import com.verdantartifice.primalmagick.common.items.tools.HallowsteelTridentItem;
import com.verdantartifice.primalmagick.common.items.tools.HexiumShieldItem;
import com.verdantartifice.primalmagick.common.items.tools.HexiumTridentItem;
import com.verdantartifice.primalmagick.common.items.tools.PrimaliteShieldItem;
import com.verdantartifice.primalmagick.common.items.tools.PrimaliteTridentItem;
import com.verdantartifice.primalmagick.common.items.tools.TieredBowItem;
import com.verdantartifice.primalmagick.common.items.wands.ModularStaffItem;
import com.verdantartifice.primalmagick.common.items.wands.ModularWandItem;
import com.verdantartifice.primalmagick.common.items.wands.MundaneWandItem;
import com.verdantartifice.primalmagick.common.sources.Source;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public interface IItemPrototypeService {
    <T extends Block> Supplier<BurnableBlockItem> burnable(Supplier<T> block, int burnTicks, Item.Properties properties);

    Supplier<ArcanometerItem> arcanometer();
    Supplier<EarthshatterHammerItem> earthshatterHammer();
    Supplier<IgnyxItem> ignyx(Item.Properties properties);

    <T extends Block> Supplier<ManaFontBlockItem> manaFont(Supplier<T> blockSupplier, Item.Properties properties);
    <T extends Block> Supplier<SpellcraftingAltarBlockItem> spellcraftingAltar(Supplier<T> blockSupplier, Item.Properties properties);

    Supplier<PrimaliteShieldItem> primaliteShield(Item.Properties properties);
    Supplier<HexiumShieldItem> hexiumShield(Item.Properties properties);
    Supplier<HallowsteelShieldItem> hallowsteelShield(Item.Properties properties);

    Supplier<PrimaliteTridentItem> primaliteTrident(Item.Properties properties);
    Supplier<HexiumTridentItem> hexiumTrident(Item.Properties properties);
    Supplier<HallowsteelTridentItem> hallowsteelTrident(Item.Properties properties);
    Supplier<ForbiddenTridentItem> forbiddenTrident(Item.Properties properties);

    Supplier<TieredBowItem> tieredBow(Tier tier, Item.Properties properties);
    Supplier<ForbiddenBowItem> forbiddenBow(Item.Properties properties);

    Supplier<MundaneWandItem> mundaneWand();
    Supplier<ModularWandItem> modularWand(Item.Properties properties);
    Supplier<ModularStaffItem> modularStaff(Item.Properties properties);

    Supplier<SpawnEggItem> deferredSpawnEgg(Supplier<? extends EntityType<? extends Mob>> type, int backgroundColor, int highlightColor, Item.Properties props);
    Supplier<SpawnEggItem> pixie(Supplier<EntityType<? extends AbstractPixieEntity>> typeSupplier, Source source, Item.Properties properties);
}
