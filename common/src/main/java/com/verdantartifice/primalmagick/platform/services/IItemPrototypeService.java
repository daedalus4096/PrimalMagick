package com.verdantartifice.primalmagick.platform.services;

import com.verdantartifice.primalmagick.common.items.entities.PixieHouseItem;
import com.verdantartifice.primalmagick.common.items.misc.ArcanometerItem;
import com.verdantartifice.primalmagick.common.items.misc.BurnableBlockItem;
import com.verdantartifice.primalmagick.common.items.misc.IgnyxItem;
import com.verdantartifice.primalmagick.common.items.misc.ManaFontBlockItem;
import com.verdantartifice.primalmagick.common.items.misc.ManaInjectorBlockItem;
import com.verdantartifice.primalmagick.common.items.misc.ManaRelayBlockItem;
import com.verdantartifice.primalmagick.common.items.misc.SpellcraftingAltarBlockItem;
import com.verdantartifice.primalmagick.common.items.tools.AbstractTieredBowItem;
import com.verdantartifice.primalmagick.common.items.tools.ForbiddenBowItem;
import com.verdantartifice.primalmagick.common.items.tools.ForbiddenTridentItem;
import com.verdantartifice.primalmagick.common.items.tools.HallowsteelShieldItem;
import com.verdantartifice.primalmagick.common.items.tools.HallowsteelTridentItem;
import com.verdantartifice.primalmagick.common.items.tools.HexiumShieldItem;
import com.verdantartifice.primalmagick.common.items.tools.HexiumTridentItem;
import com.verdantartifice.primalmagick.common.items.tools.ManaOrbItem;
import com.verdantartifice.primalmagick.common.items.tools.PrimaliteShieldItem;
import com.verdantartifice.primalmagick.common.items.tools.PrimaliteTridentItem;
import com.verdantartifice.primalmagick.common.items.tools.SpelltomeItem;
import com.verdantartifice.primalmagick.common.items.wands.ModularStaffItem;
import com.verdantartifice.primalmagick.common.items.wands.ModularWandItem;
import com.verdantartifice.primalmagick.common.items.wands.MundaneWandItem;
import com.verdantartifice.primalmagick.common.misc.DeviceTier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.level.block.Block;

import java.util.function.Function;
import java.util.function.Supplier;

public interface IItemPrototypeService {
    <T extends Block> Function<Item.Properties, BurnableBlockItem> burnable(Supplier<T> block, int burnTicks);

    Function<Item.Properties, ArcanometerItem> arcanometer();
    Function<Item.Properties, IgnyxItem> ignyx();

    <T extends Block> Function<Item.Properties, ManaFontBlockItem> manaFont(Supplier<T> blockSupplier);
    <T extends Block> Function<Item.Properties, SpellcraftingAltarBlockItem> spellcraftingAltar(Supplier<T> blockSupplier);
    <T extends Block> Function<Item.Properties, ManaRelayBlockItem> manaRelay(Supplier<T> blockSupplier);
    <T extends Block> Function<Item.Properties, ManaInjectorBlockItem> manaInjector(Supplier<T> blockSupplier);

    Function<Item.Properties, PixieHouseItem> pixieHouse();

    Function<Item.Properties, PrimaliteShieldItem> primaliteShield();
    Function<Item.Properties, HexiumShieldItem> hexiumShield();
    Function<Item.Properties, HallowsteelShieldItem> hallowsteelShield();

    Function<Item.Properties, PrimaliteTridentItem> primaliteTrident();
    Function<Item.Properties, HexiumTridentItem> hexiumTrident();
    Function<Item.Properties, HallowsteelTridentItem> hallowsteelTrident();
    Function<Item.Properties, ForbiddenTridentItem> forbiddenTrident();

    Function<Item.Properties, AbstractTieredBowItem> tieredBow(ToolMaterial material);
    Function<Item.Properties, ForbiddenBowItem> forbiddenBow();

    Function<Item.Properties, SpelltomeItem> spelltome(DeviceTier tier);
    Function<Item.Properties, ManaOrbItem> manaOrb(DeviceTier tier);

    Function<Item.Properties, MundaneWandItem> mundaneWand();
    Function<Item.Properties, ModularWandItem> modularWand();
    Function<Item.Properties, ModularStaffItem> modularStaff();
}
