package com.verdantartifice.primalmagick.platform;

import com.verdantartifice.primalmagick.common.entities.companions.pixies.AbstractPixieEntity;
import com.verdantartifice.primalmagick.common.items.misc.ArcanometerItem;
import com.verdantartifice.primalmagick.common.items.misc.ArcanometerItemNeoforge;
import com.verdantartifice.primalmagick.common.items.misc.BurnableBlockItem;
import com.verdantartifice.primalmagick.common.items.misc.BurnableBlockItemNeoforge;
import com.verdantartifice.primalmagick.common.items.misc.IgnyxItem;
import com.verdantartifice.primalmagick.common.items.misc.IgnyxItemNeoforge;
import com.verdantartifice.primalmagick.common.items.misc.ManaFontBlockItem;
import com.verdantartifice.primalmagick.common.items.misc.ManaFontBlockItemNeoforge;
import com.verdantartifice.primalmagick.common.items.misc.PixieItemNeoforge;
import com.verdantartifice.primalmagick.common.items.misc.SpellcraftingAltarBlockItem;
import com.verdantartifice.primalmagick.common.items.misc.SpellcraftingAltarBlockItemNeoforge;
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
import com.verdantartifice.primalmagick.common.items.tools.PrimaliteShieldItem;
import com.verdantartifice.primalmagick.common.items.tools.PrimaliteShieldItemNeoforge;
import com.verdantartifice.primalmagick.common.items.tools.PrimaliteTridentItem;
import com.verdantartifice.primalmagick.common.items.tools.PrimaliteTridentItemNeoforge;
import com.verdantartifice.primalmagick.common.items.tools.TieredBowItem;
import com.verdantartifice.primalmagick.common.items.tools.TieredBowItemNeoforge;
import com.verdantartifice.primalmagick.common.items.wands.ModularStaffItem;
import com.verdantartifice.primalmagick.common.items.wands.ModularStaffItemNeoforge;
import com.verdantartifice.primalmagick.common.items.wands.ModularWandItem;
import com.verdantartifice.primalmagick.common.items.wands.ModularWandItemNeoforge;
import com.verdantartifice.primalmagick.common.items.wands.MundaneWandItem;
import com.verdantartifice.primalmagick.common.items.wands.MundaneWandItemNeoforge;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.platform.services.IItemPrototypeService;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;

import java.util.function.Supplier;

public class ItemPrototypeServiceNeoforge implements IItemPrototypeService {
    @Override
    public Supplier<BurnableBlockItem> burnable(Block block, int burnTicks, Item.Properties properties) {
        return () -> new BurnableBlockItemNeoforge(block, burnTicks, properties);
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
    public <T extends Block> Supplier<ManaFontBlockItem> manaFont(Supplier<T> blockSupplier, Item.Properties properties) {
        return () -> new ManaFontBlockItemNeoforge(blockSupplier.get(), properties);
    }

    @Override
    public <T extends Block> Supplier<SpellcraftingAltarBlockItem> spellcraftingAltar(Supplier<T> blockSupplier, Item.Properties properties) {
        return () -> new SpellcraftingAltarBlockItemNeoforge(blockSupplier.get(), properties);
    }

    @Override
    public Supplier<PrimaliteShieldItem> primaliteShield(Item.Properties properties) {
        return () -> new PrimaliteShieldItemNeoforge(properties);
    }

    @Override
    public Supplier<HexiumShieldItem> hexiumShield(Item.Properties properties) {
        return () -> new HexiumShieldItemNeoforge(properties);
    }

    @Override
    public Supplier<HallowsteelShieldItem> hallowsteelShield(Item.Properties properties) {
        return () -> new HallowsteelShieldItemNeoforge(properties);
    }

    @Override
    public Supplier<PrimaliteTridentItem> primaliteTrident(Item.Properties properties) {
        return () -> new PrimaliteTridentItemNeoforge(properties);
    }

    @Override
    public Supplier<HexiumTridentItem> hexiumTrident(Item.Properties properties) {
        return () -> new HexiumTridentItemNeoforge(properties);
    }

    @Override
    public Supplier<HallowsteelTridentItem> hallowsteelTrident(Item.Properties properties) {
        return () -> new HallowsteelTridentItemNeoforge(properties);
    }

    @Override
    public Supplier<ForbiddenTridentItem> forbiddenTrident(Item.Properties properties) {
        return () -> new ForbiddenTridentItemNeoforge(properties);
    }

    @Override
    public Supplier<TieredBowItem> tieredBow(Tier tier, Item.Properties properties) {
        return () -> new TieredBowItemNeoforge(tier, properties);
    }

    @Override
    public Supplier<ForbiddenBowItem> forbiddenBow(Item.Properties properties) {
        return () -> new ForbiddenBowItemNeoforge(properties);
    }

    @Override
    public Supplier<MundaneWandItem> mundaneWand() {
        return MundaneWandItemNeoforge::new;
    }

    @Override
    public Supplier<ModularWandItem> modularWand(Item.Properties properties) {
        return () -> new ModularWandItemNeoforge(properties);
    }

    @Override
    public Supplier<ModularStaffItem> modularStaff(Item.Properties properties) {
        return () -> new ModularStaffItemNeoforge(properties);
    }

    @Override
    public Supplier<SpawnEggItem> deferredSpawnEgg(Supplier<? extends EntityType<? extends Mob>> type, int backgroundColor, int highlightColor, Item.Properties props) {
        return () -> new DeferredSpawnEggItem(type, backgroundColor, highlightColor, props);
    }

    @Override
    public Supplier<SpawnEggItem> pixie(Supplier<EntityType<? extends AbstractPixieEntity>> typeSupplier, Source source, Item.Properties properties) {
        return () -> new PixieItemNeoforge(typeSupplier, source, properties);
    }
}
