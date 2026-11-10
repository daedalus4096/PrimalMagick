package com.verdantartifice.primalmagick.platform;

import com.verdantartifice.primalmagick.common.items.entities.PixieHouseItem;
import com.verdantartifice.primalmagick.common.items.misc.ArcanometerItem;
import com.verdantartifice.primalmagick.common.items.misc.ArcanometerItemNeoforge;
import com.verdantartifice.primalmagick.common.items.misc.BurnableBlockItem;
import com.verdantartifice.primalmagick.common.items.misc.BurnableBlockItemNeoforge;
import com.verdantartifice.primalmagick.common.items.misc.IgnyxItem;
import com.verdantartifice.primalmagick.common.items.misc.IgnyxItemNeoforge;
import com.verdantartifice.primalmagick.common.items.misc.ManaFontBlockItem;
import com.verdantartifice.primalmagick.common.items.misc.ManaFontBlockItemNeoforge;
import com.verdantartifice.primalmagick.common.items.misc.ManaInjectorBlockItem;
import com.verdantartifice.primalmagick.common.items.misc.ManaInjectorBlockItemNeoforge;
import com.verdantartifice.primalmagick.common.items.misc.ManaRelayBlockItem;
import com.verdantartifice.primalmagick.common.items.misc.ManaRelayBlockItemNeoforge;
import com.verdantartifice.primalmagick.common.items.misc.PixieHouseItemNeoforge;
import com.verdantartifice.primalmagick.common.items.misc.SpellcraftingAltarBlockItem;
import com.verdantartifice.primalmagick.common.items.misc.SpellcraftingAltarBlockItemNeoforge;
import com.verdantartifice.primalmagick.common.items.tools.AbstractTieredBowItem;
import com.verdantartifice.primalmagick.common.items.tools.ForbiddenBowItem;
import com.verdantartifice.primalmagick.common.items.tools.ForbiddenBowItemNeoforge;
import com.verdantartifice.primalmagick.common.items.tools.ForbiddenTridentItem;
import com.verdantartifice.primalmagick.common.items.tools.ForbiddenTridentItemNeoforge;
import com.verdantartifice.primalmagick.common.items.tools.HallowsteelShieldItem;
import com.verdantartifice.primalmagick.common.items.tools.HallowsteelShieldItemNeoforge;
import com.verdantartifice.primalmagick.common.items.tools.HallowsteelTridentItem;
import com.verdantartifice.primalmagick.common.items.tools.HallowsteelTridentItemNeoforge;
import com.verdantartifice.primalmagick.common.items.tools.HexiumShieldItem;
import com.verdantartifice.primalmagick.common.items.tools.HexiumShieldItemNeoforge;
import com.verdantartifice.primalmagick.common.items.tools.HexiumTridentItem;
import com.verdantartifice.primalmagick.common.items.tools.HexiumTridentItemNeoforge;
import com.verdantartifice.primalmagick.common.items.tools.ManaOrbItem;
import com.verdantartifice.primalmagick.common.items.tools.ManaOrbItemNeoforge;
import com.verdantartifice.primalmagick.common.items.tools.PrimaliteShieldItem;
import com.verdantartifice.primalmagick.common.items.tools.PrimaliteShieldItemNeoforge;
import com.verdantartifice.primalmagick.common.items.tools.PrimaliteTridentItem;
import com.verdantartifice.primalmagick.common.items.tools.PrimaliteTridentItemNeoforge;
import com.verdantartifice.primalmagick.common.items.tools.SpelltomeItem;
import com.verdantartifice.primalmagick.common.items.tools.SpelltomeItemNeoforge;
import com.verdantartifice.primalmagick.common.items.tools.TieredBowItemNeoforge;
import com.verdantartifice.primalmagick.common.items.wands.ModularStaffItem;
import com.verdantartifice.primalmagick.common.items.wands.ModularStaffItemNeoforge;
import com.verdantartifice.primalmagick.common.items.wands.ModularWandItem;
import com.verdantartifice.primalmagick.common.items.wands.ModularWandItemNeoforge;
import com.verdantartifice.primalmagick.common.items.wands.MundaneWandItem;
import com.verdantartifice.primalmagick.common.items.wands.MundaneWandItemNeoforge;
import com.verdantartifice.primalmagick.common.misc.DeviceTier;
import com.verdantartifice.primalmagick.platform.services.IItemPrototypeService;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.level.block.Block;

import java.util.function.Function;
import java.util.function.Supplier;

public class ItemPrototypeServiceNeoforge implements IItemPrototypeService {
    @Override
    public <T extends Block> Function<Item.Properties, BurnableBlockItem> burnable(Supplier<T> blockSupplier, int burnTicks) {
        return props -> new BurnableBlockItemNeoforge(blockSupplier.get(), burnTicks, props);
    }

    @Override
    public Supplier<ArcanometerItem> arcanometer() {
        return ArcanometerItemNeoforge::new;
    }

    @Override
    public Supplier<IgnyxItem> ignyx(Item.Properties properties) {
        return () -> new IgnyxItemNeoforge(properties);
    }

    @Override
    public <T extends Block> Function<Item.Properties, ManaFontBlockItem> manaFont(Supplier<T> blockSupplier) {
        return props -> new ManaFontBlockItemNeoforge(blockSupplier.get(), props);
    }

    @Override
    public <T extends Block> Function<Item.Properties, SpellcraftingAltarBlockItem> spellcraftingAltar(Supplier<T> blockSupplier) {
        return properties -> new SpellcraftingAltarBlockItemNeoforge(blockSupplier.get(), properties);
    }

    @Override
    public <T extends Block> Function<Item.Properties, ManaRelayBlockItem> manaRelay(Supplier<T> blockSupplier) {
        return properties -> new ManaRelayBlockItemNeoforge(blockSupplier.get(), properties);
    }

    @Override
    public <T extends Block> Function<Item.Properties, ManaInjectorBlockItem> manaInjector(Supplier<T> blockSupplier) {
        return properties -> new ManaInjectorBlockItemNeoforge(blockSupplier.get(), properties);
    }

    @Override
    public Function<Item.Properties, PixieHouseItem> pixieHouse() {
        return PixieHouseItemNeoforge::new;
    }

    @Override
    public Function<Item.Properties, PrimaliteShieldItem> primaliteShield() {
        return PrimaliteShieldItemNeoforge::new;
    }

    @Override
    public Function<Item.Properties, HexiumShieldItem> hexiumShield() {
        return HexiumShieldItemNeoforge::new;
    }

    @Override
    public Function<Item.Properties, HallowsteelShieldItem> hallowsteelShield() {
        return HallowsteelShieldItemNeoforge::new;
    }

    @Override
    public Function<Item.Properties, PrimaliteTridentItem> primaliteTrident() {
        return PrimaliteTridentItemNeoforge::new;
    }

    @Override
    public Function<Item.Properties, HexiumTridentItem> hexiumTrident() {
        return HexiumTridentItemNeoforge::new;
    }

    @Override
    public Function<Item.Properties, HallowsteelTridentItem> hallowsteelTrident() {
        return HallowsteelTridentItemNeoforge::new;
    }

    @Override
    public Function<Item.Properties, ForbiddenTridentItem> forbiddenTrident() {
        return ForbiddenTridentItemNeoforge::new;
    }

    @Override
    public Function<Item.Properties, AbstractTieredBowItem> tieredBow(ToolMaterial material) {
        return properties -> new TieredBowItemNeoforge(material, properties);
    }

    @Override
    public Function<Item.Properties, ForbiddenBowItem> forbiddenBow() {
        return ForbiddenBowItemNeoforge::new;
    }

    @Override
    public Function<Item.Properties, SpelltomeItem> spelltome(DeviceTier tier) {
        return properties -> new SpelltomeItemNeoforge(tier, properties);
    }

    @Override
    public Function<Item.Properties, ManaOrbItem> manaOrb(DeviceTier tier) {
        return properties -> new ManaOrbItemNeoforge(tier, properties);
    }

    @Override
    public Function<Item.Properties, MundaneWandItem> mundaneWand() {
        return MundaneWandItemNeoforge::new;
    }

    @Override
    public Function<Item.Properties, ModularWandItem> modularWand() {
        return ModularWandItemNeoforge::new;
    }

    @Override
    public Function<Item.Properties, ModularStaffItem> modularStaff() {
        return ModularStaffItemNeoforge::new;
    }
}
