package com.verdantartifice.primalmagick.common.items;

import com.verdantartifice.primalmagick.common.armortrim.TrimPatternsPM;
import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import com.verdantartifice.primalmagick.common.books.BookType;
import com.verdantartifice.primalmagick.common.components.DataComponentsPM;
import com.verdantartifice.primalmagick.common.creative.CreativeModeTabsPM;
import com.verdantartifice.primalmagick.common.entities.EntityTypesPM;
import com.verdantartifice.primalmagick.common.entities.pixies.PixieRank;
import com.verdantartifice.primalmagick.common.items.armor.ArmorMaterialsPM;
import com.verdantartifice.primalmagick.common.items.armor.RobeArmorItem;
import com.verdantartifice.primalmagick.common.items.armor.WardingModuleItem;
import com.verdantartifice.primalmagick.common.items.books.LootGeneratorItem;
import com.verdantartifice.primalmagick.common.items.books.StaticBookItem;
import com.verdantartifice.primalmagick.common.items.concoctions.AlchemicalBombItem;
import com.verdantartifice.primalmagick.common.items.concoctions.BombCasingItem;
import com.verdantartifice.primalmagick.common.items.concoctions.ConcoctionItem;
import com.verdantartifice.primalmagick.common.items.concoctions.SkyglassFlaskItem;
import com.verdantartifice.primalmagick.common.items.entities.FlyingCarpetItem;
import com.verdantartifice.primalmagick.common.items.entities.ManaArrowItem;
import com.verdantartifice.primalmagick.common.items.entities.PixieHouseItem;
import com.verdantartifice.primalmagick.common.items.essence.EssenceItem;
import com.verdantartifice.primalmagick.common.items.essence.EssenceType;
import com.verdantartifice.primalmagick.common.items.food.AmbrosiaItem;
import com.verdantartifice.primalmagick.common.items.food.BloodyFleshItem;
import com.verdantartifice.primalmagick.common.items.food.FoodsPM;
import com.verdantartifice.primalmagick.common.items.food.ManafruitItem;
import com.verdantartifice.primalmagick.common.items.minerals.EnergizedGemItem;
import com.verdantartifice.primalmagick.common.items.misc.ArcanometerItem;
import com.verdantartifice.primalmagick.common.items.misc.AttunementShacklesItem;
import com.verdantartifice.primalmagick.common.items.misc.BurnableBlockItem;
import com.verdantartifice.primalmagick.common.items.misc.DowsingRodItem;
import com.verdantartifice.primalmagick.common.items.misc.DrainedPixieItem;
import com.verdantartifice.primalmagick.common.items.misc.DreamVisionTalismanItem;
import com.verdantartifice.primalmagick.common.items.misc.EnchantedInkAndQuillItem;
import com.verdantartifice.primalmagick.common.items.misc.ForbiddenSourceGainItem;
import com.verdantartifice.primalmagick.common.items.misc.GrimoireItem;
import com.verdantartifice.primalmagick.common.items.misc.HallowedOrbItem;
import com.verdantartifice.primalmagick.common.items.misc.HummingArtifactItem;
import com.verdantartifice.primalmagick.common.items.misc.IgnyxItem;
import com.verdantartifice.primalmagick.common.items.misc.KnowledgeGainItem;
import com.verdantartifice.primalmagick.common.items.misc.ManaFontBlockItem;
import com.verdantartifice.primalmagick.common.items.misc.ManaInjectorBlockItem;
import com.verdantartifice.primalmagick.common.items.misc.ManaRelayBlockItem;
import com.verdantartifice.primalmagick.common.items.misc.PixieItem;
import com.verdantartifice.primalmagick.common.items.misc.RecallStoneItem;
import com.verdantartifice.primalmagick.common.items.misc.ResearchGainItem;
import com.verdantartifice.primalmagick.common.items.misc.RuneItem;
import com.verdantartifice.primalmagick.common.items.misc.SanguineCoreItem;
import com.verdantartifice.primalmagick.common.items.misc.SeascribePenItem;
import com.verdantartifice.primalmagick.common.items.misc.SpellcraftingAltarBlockItem;
import com.verdantartifice.primalmagick.common.items.misc.TickStickItem;
import com.verdantartifice.primalmagick.common.items.tools.AbstractTieredBowItem;
import com.verdantartifice.primalmagick.common.items.tools.ForbiddenBowItem;
import com.verdantartifice.primalmagick.common.items.tools.ForbiddenSwordItem;
import com.verdantartifice.primalmagick.common.items.tools.ForbiddenTridentItem;
import com.verdantartifice.primalmagick.common.items.tools.HallowsteelShieldItem;
import com.verdantartifice.primalmagick.common.items.tools.HallowsteelTridentItem;
import com.verdantartifice.primalmagick.common.items.tools.HexiumShieldItem;
import com.verdantartifice.primalmagick.common.items.tools.HexiumTridentItem;
import com.verdantartifice.primalmagick.common.items.tools.ManaOrbItem;
import com.verdantartifice.primalmagick.common.items.tools.PrimalAxeItem;
import com.verdantartifice.primalmagick.common.items.tools.PrimalFishingRodItem;
import com.verdantartifice.primalmagick.common.items.tools.PrimalHoeItem;
import com.verdantartifice.primalmagick.common.items.tools.PrimalPickaxeItem;
import com.verdantartifice.primalmagick.common.items.tools.PrimalShovelItem;
import com.verdantartifice.primalmagick.common.items.tools.PrimaliteShieldItem;
import com.verdantartifice.primalmagick.common.items.tools.PrimaliteTridentItem;
import com.verdantartifice.primalmagick.common.items.tools.SacredShieldItem;
import com.verdantartifice.primalmagick.common.items.tools.SpelltomeItem;
import com.verdantartifice.primalmagick.common.items.tools.TieredFishingRodItem;
import com.verdantartifice.primalmagick.common.items.tools.ToolMaterialsPM;
import com.verdantartifice.primalmagick.common.items.wands.ModularStaffItem;
import com.verdantartifice.primalmagick.common.items.wands.ModularWandItem;
import com.verdantartifice.primalmagick.common.items.wands.MundaneWandItem;
import com.verdantartifice.primalmagick.common.items.wands.SpellScrollItem;
import com.verdantartifice.primalmagick.common.items.wands.StaffCoreItem;
import com.verdantartifice.primalmagick.common.items.wands.WandCapItem;
import com.verdantartifice.primalmagick.common.items.wands.WandCoreItem;
import com.verdantartifice.primalmagick.common.items.wands.WandGemItem;
import com.verdantartifice.primalmagick.common.loot.LootTablesPM;
import com.verdantartifice.primalmagick.common.misc.DeviceTier;
import com.verdantartifice.primalmagick.common.registries.IRegistryItem;
import com.verdantartifice.primalmagick.common.research.KnowledgeType;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.keys.ResearchEntryKey;
import com.verdantartifice.primalmagick.common.runes.Rune;
import com.verdantartifice.primalmagick.common.sources.Sources;
import com.verdantartifice.primalmagick.common.wands.WandCap;
import com.verdantartifice.primalmagick.common.wands.WandCore;
import com.verdantartifice.primalmagick.common.wands.WandGem;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DoubleHighBlockItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.HoneycombItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SmithingTemplateItem;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.level.block.Block;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Common repository for block registry objects.
 *
 * @author Daedalus4096
 */
public class ItemsPM {
    public static void init() {
        // Pass the service initialization through this class so it gets class loaded and fields registered
        Services.ITEMS_REGISTRY.init();
    }

    protected static <I extends Item> IRegistryItem<Item, I> registerSupplier(String name, Function<Item.Properties, ? extends I> itemFactory, Supplier<Item.Properties> propertiesSupplier) {
        IRegistryItem<Item, I> obj = registerWithoutTab(name, itemFactory, propertiesSupplier);
        CreativeModeTabsPM.registerSupplier(obj);
        return obj;
    }

    protected static <I extends Item> IRegistryItem<Item, I> registerSupplier(String name, Function<Item.Properties, ? extends I> itemFactory) {
        IRegistryItem<Item, I> obj = registerWithoutTab(name, itemFactory);
        CreativeModeTabsPM.registerSupplier(obj);
        return obj;
    }

    protected static IRegistryItem<Item, BlockItem> registerSimpleBlockItemSupplier(String name, Supplier<? extends Block> blockSupplier) {
        IRegistryItem<Item, BlockItem> obj = registerSimpleBlockItemWithoutTab(name, blockSupplier);
        CreativeModeTabsPM.registerSupplier(obj);
        return obj;
    }

    protected static IRegistryItem<Item, BlockItem> registerSimpleBlockItemSupplier(String name, Supplier<? extends Block> blockSupplier, Supplier<Item.Properties> propertiesSupplier) {
        IRegistryItem<Item, BlockItem> obj = registerSimpleBlockItemWithoutTab(name, blockSupplier, propertiesSupplier);
        CreativeModeTabsPM.registerSupplier(obj);
        return obj;
    }

    protected static IRegistryItem<Item, Item> registerSimpleItemSupplier(String name, Supplier<Item.Properties> propertiesSupplier) {
        IRegistryItem<Item, Item> obj = registerSimpleItemWithoutTab(name, propertiesSupplier);
        CreativeModeTabsPM.registerSupplier(obj);
        return obj;
    }

    protected static IRegistryItem<Item, Item> registerSimpleItemSupplier(String name) {
        IRegistryItem<Item, Item> obj = registerSimpleItemWithoutTab(name);
        CreativeModeTabsPM.registerSupplier(obj);
        return obj;
    }

    protected static <I extends Item> IRegistryItem<Item, I> registerDefaultInstance(String name, Function<Item.Properties, I> itemFactory, Supplier<Item.Properties> propertiesSupplier) {
        IRegistryItem<Item, I> obj = registerWithoutTab(name, itemFactory, propertiesSupplier);
        CreativeModeTabsPM.registerDefaultInstance(obj);
        return obj;
    }

    protected static <I extends Item> IRegistryItem<Item, I> registerCustom(String name, CreativeModeTabsPM.CustomTabRegistrar registrar, Supplier<I> itemSupplier) {
        IRegistryItem<Item, I> obj = registerWithoutTab(name, itemSupplier);
        CreativeModeTabsPM.registerCustom(obj, registrar);
        return obj;
    }

    protected static IRegistryItem<Item, BlockItem> registerSimpleBlockItemWithoutTab(String name, Supplier<? extends Block> blockSupplier) {
        return Services.ITEMS_REGISTRY.registerSimpleBlockItem(name, blockSupplier);
    }

    protected static IRegistryItem<Item, BlockItem> registerSimpleBlockItemWithoutTab(String name, Supplier<? extends Block> blockSupplier, Supplier<Item.Properties> propertiesSupplier) {
        return Services.ITEMS_REGISTRY.registerSimpleBlockItem(name, blockSupplier, propertiesSupplier);
    }

    protected static <I extends Item> IRegistryItem<Item, I> registerWithoutTab(String name, Function<Item.Properties, ? extends I> itemFactory, Supplier<Item.Properties> propertiesSupplier) {
        return Services.ITEMS_REGISTRY.registerItem(name, itemFactory, propertiesSupplier);
    }

    protected static <I extends Item> IRegistryItem<Item, I> registerWithoutTab(String name, Function<Item.Properties, ? extends I> itemFactory) {
        return Services.ITEMS_REGISTRY.registerItem(name, itemFactory);
    }

    protected static IRegistryItem<Item, Item> registerSimpleItemWithoutTab(String name, Supplier<Item.Properties> propertiesSupplier) {
        return Services.ITEMS_REGISTRY.registerSimpleItem(name, propertiesSupplier);
    }

    protected static IRegistryItem<Item, Item> registerSimpleItemWithoutTab(String name) {
        return Services.ITEMS_REGISTRY.registerSimpleItem(name);
    }

    // Register grimoire items
    public static final IRegistryItem<Item, GrimoireItem> GRIMOIRE = registerSupplier("grimoire", props -> new GrimoireItem(false, props), () -> new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON));
    public static final IRegistryItem<Item, GrimoireItem> CREATIVE_GRIMOIRE = registerSupplier("grimoire_creative", props -> new GrimoireItem(true, props), () -> new Item.Properties().stacksTo(1).rarity(Rarity.EPIC));

    // Register raw marble block items
    public static final IRegistryItem<Item, BlockItem> MARBLE = registerSimpleBlockItemSupplier("marble", BlocksPM.MARBLE);
    public static final IRegistryItem<Item, BlockItem> MARBLE_SLAB = registerSimpleBlockItemSupplier("marble_slab", BlocksPM.MARBLE_SLAB);
    public static final IRegistryItem<Item, BlockItem> MARBLE_STAIRS = registerSimpleBlockItemSupplier("marble_stairs", BlocksPM.MARBLE_STAIRS);
    public static final IRegistryItem<Item, BlockItem> MARBLE_WALL = registerSimpleBlockItemSupplier("marble_wall", BlocksPM.MARBLE_WALL);
    public static final IRegistryItem<Item, BlockItem> MARBLE_BRICKS = registerSimpleBlockItemSupplier("marble_bricks", BlocksPM.MARBLE_BRICKS);
    public static final IRegistryItem<Item, BlockItem> MARBLE_BRICK_SLAB = registerSimpleBlockItemSupplier("marble_brick_slab", BlocksPM.MARBLE_BRICK_SLAB);
    public static final IRegistryItem<Item, BlockItem> MARBLE_BRICK_STAIRS = registerSimpleBlockItemSupplier("marble_brick_stairs", BlocksPM.MARBLE_BRICK_STAIRS);
    public static final IRegistryItem<Item, BlockItem> MARBLE_BRICK_WALL = registerSimpleBlockItemSupplier("marble_brick_wall", BlocksPM.MARBLE_BRICK_WALL);
    public static final IRegistryItem<Item, BlockItem> MARBLE_PILLAR = registerSimpleBlockItemSupplier("marble_pillar", BlocksPM.MARBLE_PILLAR);
    public static final IRegistryItem<Item, BlockItem> MARBLE_CHISELED = registerSimpleBlockItemSupplier("marble_chiseled", BlocksPM.MARBLE_CHISELED);
    public static final IRegistryItem<Item, BlockItem> MARBLE_RUNED = registerSimpleBlockItemSupplier("marble_runed", BlocksPM.MARBLE_RUNED);
    public static final IRegistryItem<Item, BlockItem> MARBLE_TILES = registerSimpleBlockItemSupplier("marble_tiles", BlocksPM.MARBLE_TILES);
    public static final IRegistryItem<Item, BlockItem> MARBLE_BOOKSHELF = registerSimpleBlockItemSupplier("marble_bookshelf", BlocksPM.MARBLE_BOOKSHELF);

    // Register enchanted marble block items
    public static final IRegistryItem<Item, BlockItem> MARBLE_ENCHANTED = registerSimpleBlockItemSupplier("marble_enchanted", BlocksPM.MARBLE_ENCHANTED);
    public static final IRegistryItem<Item, BlockItem> MARBLE_ENCHANTED_SLAB = registerSimpleBlockItemSupplier("marble_enchanted_slab", BlocksPM.MARBLE_ENCHANTED_SLAB);
    public static final IRegistryItem<Item, BlockItem> MARBLE_ENCHANTED_STAIRS = registerSimpleBlockItemSupplier("marble_enchanted_stairs", BlocksPM.MARBLE_ENCHANTED_STAIRS);
    public static final IRegistryItem<Item, BlockItem> MARBLE_ENCHANTED_WALL = registerSimpleBlockItemSupplier("marble_enchanted_wall", BlocksPM.MARBLE_ENCHANTED_WALL);
    public static final IRegistryItem<Item, BlockItem> MARBLE_ENCHANTED_BRICKS = registerSimpleBlockItemSupplier("marble_enchanted_bricks", BlocksPM.MARBLE_ENCHANTED_BRICKS);
    public static final IRegistryItem<Item, BlockItem> MARBLE_ENCHANTED_BRICK_SLAB = registerSimpleBlockItemSupplier("marble_enchanted_brick_slab", BlocksPM.MARBLE_ENCHANTED_BRICK_SLAB);
    public static final IRegistryItem<Item, BlockItem> MARBLE_ENCHANTED_BRICK_STAIRS = registerSimpleBlockItemSupplier("marble_enchanted_brick_stairs", BlocksPM.MARBLE_ENCHANTED_BRICK_STAIRS);
    public static final IRegistryItem<Item, BlockItem> MARBLE_ENCHANTED_BRICK_WALL = registerSimpleBlockItemSupplier("marble_enchanted_brick_wall", BlocksPM.MARBLE_ENCHANTED_BRICK_WALL);
    public static final IRegistryItem<Item, BlockItem> MARBLE_ENCHANTED_PILLAR = registerSimpleBlockItemSupplier("marble_enchanted_pillar", BlocksPM.MARBLE_ENCHANTED_PILLAR);
    public static final IRegistryItem<Item, BlockItem> MARBLE_ENCHANTED_CHISELED = registerSimpleBlockItemSupplier("marble_enchanted_chiseled", BlocksPM.MARBLE_ENCHANTED_CHISELED);
    public static final IRegistryItem<Item, BlockItem> MARBLE_ENCHANTED_RUNED = registerSimpleBlockItemSupplier("marble_enchanted_runed", BlocksPM.MARBLE_ENCHANTED_RUNED);
    public static final IRegistryItem<Item, BlockItem> MARBLE_ENCHANTED_BOOKSHELF = registerSimpleBlockItemSupplier("marble_enchanted_bookshelf", BlocksPM.MARBLE_ENCHANTED_BOOKSHELF);

    // Register smoked marble block items
    public static final IRegistryItem<Item, BlockItem> MARBLE_SMOKED = registerSimpleBlockItemSupplier("marble_smoked", BlocksPM.MARBLE_SMOKED);
    public static final IRegistryItem<Item, BlockItem> MARBLE_SMOKED_SLAB = registerSimpleBlockItemSupplier("marble_smoked_slab", BlocksPM.MARBLE_SMOKED_SLAB);
    public static final IRegistryItem<Item, BlockItem> MARBLE_SMOKED_STAIRS = registerSimpleBlockItemSupplier("marble_smoked_stairs", BlocksPM.MARBLE_SMOKED_STAIRS);
    public static final IRegistryItem<Item, BlockItem> MARBLE_SMOKED_WALL = registerSimpleBlockItemSupplier("marble_smoked_wall", BlocksPM.MARBLE_SMOKED_WALL);
    public static final IRegistryItem<Item, BlockItem> MARBLE_SMOKED_BRICKS = registerSimpleBlockItemSupplier("marble_smoked_bricks", BlocksPM.MARBLE_SMOKED_BRICKS);
    public static final IRegistryItem<Item, BlockItem> MARBLE_SMOKED_BRICK_SLAB = registerSimpleBlockItemSupplier("marble_smoked_brick_slab", BlocksPM.MARBLE_SMOKED_BRICK_SLAB);
    public static final IRegistryItem<Item, BlockItem> MARBLE_SMOKED_BRICK_STAIRS = registerSimpleBlockItemSupplier("marble_smoked_brick_stairs", BlocksPM.MARBLE_SMOKED_BRICK_STAIRS);
    public static final IRegistryItem<Item, BlockItem> MARBLE_SMOKED_BRICK_WALL = registerSimpleBlockItemSupplier("marble_smoked_brick_wall", BlocksPM.MARBLE_SMOKED_BRICK_WALL);
    public static final IRegistryItem<Item, BlockItem> MARBLE_SMOKED_PILLAR = registerSimpleBlockItemSupplier("marble_smoked_pillar", BlocksPM.MARBLE_SMOKED_PILLAR);
    public static final IRegistryItem<Item, BlockItem> MARBLE_SMOKED_CHISELED = registerSimpleBlockItemSupplier("marble_smoked_chiseled", BlocksPM.MARBLE_SMOKED_CHISELED);
    public static final IRegistryItem<Item, BlockItem> MARBLE_SMOKED_RUNED = registerSimpleBlockItemSupplier("marble_smoked_runed", BlocksPM.MARBLE_SMOKED_RUNED);
    public static final IRegistryItem<Item, BlockItem> MARBLE_SMOKED_BOOKSHELF = registerSimpleBlockItemSupplier("marble_smoked_bookshelf", BlocksPM.MARBLE_SMOKED_BOOKSHELF);

    // Register hallowed marble block items
    public static final IRegistryItem<Item, BlockItem> MARBLE_HALLOWED = registerSimpleBlockItemSupplier("marble_hallowed", BlocksPM.MARBLE_HALLOWED);
    public static final IRegistryItem<Item, BlockItem> MARBLE_HALLOWED_SLAB = registerSimpleBlockItemSupplier("marble_hallowed_slab", BlocksPM.MARBLE_HALLOWED_SLAB);
    public static final IRegistryItem<Item, BlockItem> MARBLE_HALLOWED_STAIRS = registerSimpleBlockItemSupplier("marble_hallowed_stairs", BlocksPM.MARBLE_HALLOWED_STAIRS);
    public static final IRegistryItem<Item, BlockItem> MARBLE_HALLOWED_WALL = registerSimpleBlockItemSupplier("marble_hallowed_wall", BlocksPM.MARBLE_HALLOWED_WALL);
    public static final IRegistryItem<Item, BlockItem> MARBLE_HALLOWED_BRICKS = registerSimpleBlockItemSupplier("marble_hallowed_bricks", BlocksPM.MARBLE_HALLOWED_BRICKS);
    public static final IRegistryItem<Item, BlockItem> MARBLE_HALLOWED_BRICK_SLAB = registerSimpleBlockItemSupplier("marble_hallowed_brick_slab", BlocksPM.MARBLE_HALLOWED_BRICK_SLAB);
    public static final IRegistryItem<Item, BlockItem> MARBLE_HALLOWED_BRICK_STAIRS = registerSimpleBlockItemSupplier("marble_hallowed_brick_stairs", BlocksPM.MARBLE_HALLOWED_BRICK_STAIRS);
    public static final IRegistryItem<Item, BlockItem> MARBLE_HALLOWED_BRICK_WALL = registerSimpleBlockItemSupplier("marble_hallowed_brick_wall", BlocksPM.MARBLE_HALLOWED_BRICK_WALL);
    public static final IRegistryItem<Item, BlockItem> MARBLE_HALLOWED_PILLAR = registerSimpleBlockItemSupplier("marble_hallowed_pillar", BlocksPM.MARBLE_HALLOWED_PILLAR);
    public static final IRegistryItem<Item, BlockItem> MARBLE_HALLOWED_CHISELED = registerSimpleBlockItemSupplier("marble_hallowed_chiseled", BlocksPM.MARBLE_HALLOWED_CHISELED);
    public static final IRegistryItem<Item, BlockItem> MARBLE_HALLOWED_RUNED = registerSimpleBlockItemSupplier("marble_hallowed_runed", BlocksPM.MARBLE_HALLOWED_RUNED);
    public static final IRegistryItem<Item, BlockItem> MARBLE_HALLOWED_BOOKSHELF = registerSimpleBlockItemSupplier("marble_hallowed_bookshelf", BlocksPM.MARBLE_HALLOWED_BOOKSHELF);

    // Register sunwood block items
    public static final IRegistryItem<Item, BlockItem> SUNWOOD_LOG = registerSimpleBlockItemSupplier("sunwood_log", BlocksPM.SUNWOOD_LOG);
    public static final IRegistryItem<Item, BlockItem> STRIPPED_SUNWOOD_LOG = registerSimpleBlockItemSupplier("stripped_sunwood_log", BlocksPM.STRIPPED_SUNWOOD_LOG);
    public static final IRegistryItem<Item, BlockItem> SUNWOOD_WOOD = registerSimpleBlockItemSupplier("sunwood_wood", BlocksPM.SUNWOOD_WOOD);
    public static final IRegistryItem<Item, BlockItem> STRIPPED_SUNWOOD_WOOD = registerSimpleBlockItemSupplier("stripped_sunwood_wood", BlocksPM.STRIPPED_SUNWOOD_WOOD);
    public static final IRegistryItem<Item, BlockItem> SUNWOOD_LEAVES = registerSimpleBlockItemSupplier("sunwood_leaves", BlocksPM.SUNWOOD_LEAVES);
    public static final IRegistryItem<Item, BlockItem> SUNWOOD_SAPLING = registerSimpleBlockItemSupplier("sunwood_sapling", BlocksPM.SUNWOOD_SAPLING);
    public static final IRegistryItem<Item, BlockItem> SUNWOOD_PLANKS = registerSimpleBlockItemSupplier("sunwood_planks", BlocksPM.SUNWOOD_PLANKS);
    public static final IRegistryItem<Item, BlockItem> SUNWOOD_SLAB = registerSimpleBlockItemSupplier("sunwood_slab", BlocksPM.SUNWOOD_SLAB);
    public static final IRegistryItem<Item, BlockItem> SUNWOOD_STAIRS = registerSimpleBlockItemSupplier("sunwood_stairs", BlocksPM.SUNWOOD_STAIRS);
    public static final IRegistryItem<Item, BurnableBlockItem> SUNWOOD_PILLAR = registerSupplier("sunwood_pillar", Services.ITEM_PROTOTYPES.burnable(BlocksPM.SUNWOOD_PILLAR, 300));

    // Register moonwood block items
    public static final IRegistryItem<Item, BlockItem> MOONWOOD_LOG = registerSimpleBlockItemSupplier("moonwood_log", BlocksPM.MOONWOOD_LOG);
    public static final IRegistryItem<Item, BlockItem> STRIPPED_MOONWOOD_LOG = registerSimpleBlockItemSupplier("stripped_moonwood_log", BlocksPM.STRIPPED_MOONWOOD_LOG);
    public static final IRegistryItem<Item, BlockItem> MOONWOOD_WOOD = registerSimpleBlockItemSupplier("moonwood_wood", BlocksPM.MOONWOOD_WOOD);
    public static final IRegistryItem<Item, BlockItem> STRIPPED_MOONWOOD_WOOD = registerSimpleBlockItemSupplier("stripped_moonwood_wood", BlocksPM.STRIPPED_MOONWOOD_WOOD);
    public static final IRegistryItem<Item, BlockItem> MOONWOOD_LEAVES = registerSimpleBlockItemSupplier("moonwood_leaves", BlocksPM.MOONWOOD_LEAVES);
    public static final IRegistryItem<Item, BlockItem> MOONWOOD_SAPLING = registerSimpleBlockItemSupplier("moonwood_sapling", BlocksPM.MOONWOOD_SAPLING);
    public static final IRegistryItem<Item, BlockItem> MOONWOOD_PLANKS = registerSimpleBlockItemSupplier("moonwood_planks", BlocksPM.MOONWOOD_PLANKS);
    public static final IRegistryItem<Item, BlockItem> MOONWOOD_SLAB = registerSimpleBlockItemSupplier("moonwood_slab", BlocksPM.MOONWOOD_SLAB);
    public static final IRegistryItem<Item, BlockItem> MOONWOOD_STAIRS = registerSimpleBlockItemSupplier("moonwood_stairs", BlocksPM.MOONWOOD_STAIRS);
    public static final IRegistryItem<Item, BurnableBlockItem> MOONWOOD_PILLAR = registerSupplier("moonwood_pillar", Services.ITEM_PROTOTYPES.burnable(BlocksPM.MOONWOOD_PILLAR, 300));

    // Register hallowood block items
    public static final IRegistryItem<Item, BlockItem> HALLOWOOD_LOG = registerSimpleBlockItemSupplier("hallowood_log", BlocksPM.HALLOWOOD_LOG);
    public static final IRegistryItem<Item, BlockItem> STRIPPED_HALLOWOOD_LOG = registerSimpleBlockItemSupplier("stripped_hallowood_log", BlocksPM.STRIPPED_HALLOWOOD_LOG);
    public static final IRegistryItem<Item, BlockItem> HALLOWOOD_WOOD = registerSimpleBlockItemSupplier("hallowood_wood", BlocksPM.HALLOWOOD_WOOD);
    public static final IRegistryItem<Item, BlockItem> STRIPPED_HALLOWOOD_WOOD = registerSimpleBlockItemSupplier("stripped_hallowood_wood", BlocksPM.STRIPPED_HALLOWOOD_WOOD);
    public static final IRegistryItem<Item, BlockItem> HALLOWOOD_LEAVES = registerSimpleBlockItemSupplier("hallowood_leaves", BlocksPM.HALLOWOOD_LEAVES);
    public static final IRegistryItem<Item, BlockItem> HALLOWOOD_SAPLING = registerSimpleBlockItemSupplier("hallowood_sapling", BlocksPM.HALLOWOOD_SAPLING);
    public static final IRegistryItem<Item, BlockItem> HALLOWOOD_PLANKS = registerSimpleBlockItemSupplier("hallowood_planks", BlocksPM.HALLOWOOD_PLANKS);
    public static final IRegistryItem<Item, BlockItem> HALLOWOOD_SLAB = registerSimpleBlockItemSupplier("hallowood_slab", BlocksPM.HALLOWOOD_SLAB);
    public static final IRegistryItem<Item, BlockItem> HALLOWOOD_STAIRS = registerSimpleBlockItemSupplier("hallowood_stairs", BlocksPM.HALLOWOOD_STAIRS);
    public static final IRegistryItem<Item, BurnableBlockItem> HALLOWOOD_PILLAR = registerSupplier("hallowood_pillar", Services.ITEM_PROTOTYPES.burnable(BlocksPM.HALLOWOOD_PILLAR, 300));

    // Register crop items
    public static final IRegistryItem<Item, BlockItem> HYDROMELON_SEEDS = registerSimpleBlockItemSupplier("hydromelon_seeds", BlocksPM.HYDROMELON_STEM, () -> new Item.Properties().useItemDescriptionPrefix());
    public static final IRegistryItem<Item, BlockItem> HYDROMELON = registerSimpleBlockItemSupplier("hydromelon", BlocksPM.HYDROMELON);
    public static final IRegistryItem<Item, Item> HYDROMELON_SLICE = registerSupplier("hydromelon_slice", Item::new, () -> new Item.Properties().food(FoodsPM.HYDROMELON_SLICE));
    public static final IRegistryItem<Item, DoubleHighBlockItem> BLOOD_ROSE = registerSupplier("blood_rose", props -> new DoubleHighBlockItem(BlocksPM.BLOOD_ROSE.get(), props));
    public static final IRegistryItem<Item, DoubleHighBlockItem> EMBERFLOWER = registerSupplier("emberflower", props -> new DoubleHighBlockItem(BlocksPM.EMBERFLOWER.get(), props));

    // Register infused stone block items
    public static final IRegistryItem<Item, BlockItem> INFUSED_STONE_EARTH = registerSimpleBlockItemSupplier("infused_stone_earth", BlocksPM.INFUSED_STONE_EARTH);
    public static final IRegistryItem<Item, BlockItem> INFUSED_STONE_SEA = registerSimpleBlockItemSupplier("infused_stone_sea", BlocksPM.INFUSED_STONE_SEA);
    public static final IRegistryItem<Item, BlockItem> INFUSED_STONE_SKY = registerSimpleBlockItemSupplier("infused_stone_sky", BlocksPM.INFUSED_STONE_SKY);
    public static final IRegistryItem<Item, BlockItem> INFUSED_STONE_SUN = registerSimpleBlockItemSupplier("infused_stone_sun", BlocksPM.INFUSED_STONE_SUN);
    public static final IRegistryItem<Item, BlockItem> INFUSED_STONE_MOON = registerSimpleBlockItemSupplier("infused_stone_moon", BlocksPM.INFUSED_STONE_MOON);

    // Register budding gem block items
    public static final IRegistryItem<Item, BlockItem> SYNTHETIC_AMETHYST_CLUSTER = registerSimpleBlockItemSupplier("synthetic_amethyst_cluster", BlocksPM.SYNTHETIC_AMETHYST_CLUSTER);
    public static final IRegistryItem<Item, BlockItem> LARGE_SYNTHETIC_AMETHYST_BUD = registerSimpleBlockItemSupplier("large_synthetic_amethyst_bud", BlocksPM.LARGE_SYNTHETIC_AMETHYST_BUD);
    public static final IRegistryItem<Item, BlockItem> MEDIUM_SYNTHETIC_AMETHYST_BUD = registerSimpleBlockItemSupplier("medium_synthetic_amethyst_bud", BlocksPM.MEDIUM_SYNTHETIC_AMETHYST_BUD);
    public static final IRegistryItem<Item, BlockItem> SMALL_SYNTHETIC_AMETHYST_BUD = registerSimpleBlockItemSupplier("small_synthetic_amethyst_bud", BlocksPM.SMALL_SYNTHETIC_AMETHYST_BUD);
    public static final IRegistryItem<Item, BlockItem> DAMAGED_BUDDING_AMETHYST_BLOCK = registerSimpleBlockItemSupplier("damaged_budding_amethyst_block", BlocksPM.DAMAGED_BUDDING_AMETHYST_BLOCK);
    public static final IRegistryItem<Item, BlockItem> CHIPPED_BUDDING_AMETHYST_BLOCK = registerSimpleBlockItemSupplier("chipped_budding_amethyst_block", BlocksPM.CHIPPED_BUDDING_AMETHYST_BLOCK);
    public static final IRegistryItem<Item, BlockItem> FLAWED_BUDDING_AMETHYST_BLOCK = registerSimpleBlockItemSupplier("flawed_budding_amethyst_block", BlocksPM.FLAWED_BUDDING_AMETHYST_BLOCK);
    public static final IRegistryItem<Item, BlockItem> SYNTHETIC_DIAMOND_CLUSTER = registerSimpleBlockItemSupplier("synthetic_diamond_cluster", BlocksPM.SYNTHETIC_DIAMOND_CLUSTER);
    public static final IRegistryItem<Item, BlockItem> LARGE_SYNTHETIC_DIAMOND_BUD = registerSimpleBlockItemSupplier("large_synthetic_diamond_bud", BlocksPM.LARGE_SYNTHETIC_DIAMOND_BUD);
    public static final IRegistryItem<Item, BlockItem> MEDIUM_SYNTHETIC_DIAMOND_BUD = registerSimpleBlockItemSupplier("medium_synthetic_diamond_bud", BlocksPM.MEDIUM_SYNTHETIC_DIAMOND_BUD);
    public static final IRegistryItem<Item, BlockItem> SMALL_SYNTHETIC_DIAMOND_BUD = registerSimpleBlockItemSupplier("small_synthetic_diamond_bud", BlocksPM.SMALL_SYNTHETIC_DIAMOND_BUD);
    public static final IRegistryItem<Item, BlockItem> DAMAGED_BUDDING_DIAMOND_BLOCK = registerSimpleBlockItemSupplier("damaged_budding_diamond_block", BlocksPM.DAMAGED_BUDDING_DIAMOND_BLOCK);
    public static final IRegistryItem<Item, BlockItem> CHIPPED_BUDDING_DIAMOND_BLOCK = registerSimpleBlockItemSupplier("chipped_budding_diamond_block", BlocksPM.CHIPPED_BUDDING_DIAMOND_BLOCK);
    public static final IRegistryItem<Item, BlockItem> FLAWED_BUDDING_DIAMOND_BLOCK = registerSimpleBlockItemSupplier("flawed_budding_diamond_block", BlocksPM.FLAWED_BUDDING_DIAMOND_BLOCK);
    public static final IRegistryItem<Item, BlockItem> SYNTHETIC_EMERALD_CLUSTER = registerSimpleBlockItemSupplier("synthetic_emerald_cluster", BlocksPM.SYNTHETIC_EMERALD_CLUSTER);
    public static final IRegistryItem<Item, BlockItem> LARGE_SYNTHETIC_EMERALD_BUD = registerSimpleBlockItemSupplier("large_synthetic_emerald_bud", BlocksPM.LARGE_SYNTHETIC_EMERALD_BUD);
    public static final IRegistryItem<Item, BlockItem> MEDIUM_SYNTHETIC_EMERALD_BUD = registerSimpleBlockItemSupplier("medium_synthetic_emerald_bud", BlocksPM.MEDIUM_SYNTHETIC_EMERALD_BUD);
    public static final IRegistryItem<Item, BlockItem> SMALL_SYNTHETIC_EMERALD_BUD = registerSimpleBlockItemSupplier("small_synthetic_emerald_bud", BlocksPM.SMALL_SYNTHETIC_EMERALD_BUD);
    public static final IRegistryItem<Item, BlockItem> DAMAGED_BUDDING_EMERALD_BLOCK = registerSimpleBlockItemSupplier("damaged_budding_emerald_block", BlocksPM.DAMAGED_BUDDING_EMERALD_BLOCK);
    public static final IRegistryItem<Item, BlockItem> CHIPPED_BUDDING_EMERALD_BLOCK = registerSimpleBlockItemSupplier("chipped_budding_emerald_block", BlocksPM.CHIPPED_BUDDING_EMERALD_BLOCK);
    public static final IRegistryItem<Item, BlockItem> FLAWED_BUDDING_EMERALD_BLOCK = registerSimpleBlockItemSupplier("flawed_budding_emerald_block", BlocksPM.FLAWED_BUDDING_EMERALD_BLOCK);
    public static final IRegistryItem<Item, BlockItem> SYNTHETIC_QUARTZ_CLUSTER = registerSimpleBlockItemSupplier("synthetic_quartz_cluster", BlocksPM.SYNTHETIC_QUARTZ_CLUSTER);
    public static final IRegistryItem<Item, BlockItem> LARGE_SYNTHETIC_QUARTZ_BUD = registerSimpleBlockItemSupplier("large_synthetic_quartz_bud", BlocksPM.LARGE_SYNTHETIC_QUARTZ_BUD);
    public static final IRegistryItem<Item, BlockItem> MEDIUM_SYNTHETIC_QUARTZ_BUD = registerSimpleBlockItemSupplier("medium_synthetic_quartz_bud", BlocksPM.MEDIUM_SYNTHETIC_QUARTZ_BUD);
    public static final IRegistryItem<Item, BlockItem> SMALL_SYNTHETIC_QUARTZ_BUD = registerSimpleBlockItemSupplier("small_synthetic_quartz_bud", BlocksPM.SMALL_SYNTHETIC_QUARTZ_BUD);
    public static final IRegistryItem<Item, BlockItem> DAMAGED_BUDDING_QUARTZ_BLOCK = registerSimpleBlockItemSupplier("damaged_budding_quartz_block", BlocksPM.DAMAGED_BUDDING_QUARTZ_BLOCK);
    public static final IRegistryItem<Item, BlockItem> CHIPPED_BUDDING_QUARTZ_BLOCK = registerSimpleBlockItemSupplier("chipped_budding_quartz_block", BlocksPM.CHIPPED_BUDDING_QUARTZ_BLOCK);
    public static final IRegistryItem<Item, BlockItem> FLAWED_BUDDING_QUARTZ_BLOCK = registerSimpleBlockItemSupplier("flawed_budding_quartz_block", BlocksPM.FLAWED_BUDDING_QUARTZ_BLOCK);

    // Register skyglass block items
    public static final IRegistryItem<Item, BlockItem> SKYGLASS = registerSimpleBlockItemSupplier("skyglass", BlocksPM.SKYGLASS);
    public static final IRegistryItem<Item, BlockItem> STAINED_SKYGLASS_BLACK = registerSimpleBlockItemSupplier("stained_skyglass_black", BlocksPM.STAINED_SKYGLASS_BLACK);
    public static final IRegistryItem<Item, BlockItem> STAINED_SKYGLASS_BLUE = registerSimpleBlockItemSupplier("stained_skyglass_blue", BlocksPM.STAINED_SKYGLASS_BLUE);
    public static final IRegistryItem<Item, BlockItem> STAINED_SKYGLASS_BROWN = registerSimpleBlockItemSupplier("stained_skyglass_brown", BlocksPM.STAINED_SKYGLASS_BROWN);
    public static final IRegistryItem<Item, BlockItem> STAINED_SKYGLASS_CYAN = registerSimpleBlockItemSupplier("stained_skyglass_cyan", BlocksPM.STAINED_SKYGLASS_CYAN);
    public static final IRegistryItem<Item, BlockItem> STAINED_SKYGLASS_GRAY = registerSimpleBlockItemSupplier("stained_skyglass_gray", BlocksPM.STAINED_SKYGLASS_GRAY);
    public static final IRegistryItem<Item, BlockItem> STAINED_SKYGLASS_GREEN = registerSimpleBlockItemSupplier("stained_skyglass_green", BlocksPM.STAINED_SKYGLASS_GREEN);
    public static final IRegistryItem<Item, BlockItem> STAINED_SKYGLASS_LIGHT_BLUE = registerSimpleBlockItemSupplier("stained_skyglass_light_blue", BlocksPM.STAINED_SKYGLASS_LIGHT_BLUE);
    public static final IRegistryItem<Item, BlockItem> STAINED_SKYGLASS_LIGHT_GRAY = registerSimpleBlockItemSupplier("stained_skyglass_light_gray", BlocksPM.STAINED_SKYGLASS_LIGHT_GRAY);
    public static final IRegistryItem<Item, BlockItem> STAINED_SKYGLASS_LIME = registerSimpleBlockItemSupplier("stained_skyglass_lime", BlocksPM.STAINED_SKYGLASS_LIME);
    public static final IRegistryItem<Item, BlockItem> STAINED_SKYGLASS_MAGENTA = registerSimpleBlockItemSupplier("stained_skyglass_magenta", BlocksPM.STAINED_SKYGLASS_MAGENTA);
    public static final IRegistryItem<Item, BlockItem> STAINED_SKYGLASS_ORANGE = registerSimpleBlockItemSupplier("stained_skyglass_orange", BlocksPM.STAINED_SKYGLASS_ORANGE);
    public static final IRegistryItem<Item, BlockItem> STAINED_SKYGLASS_PINK = registerSimpleBlockItemSupplier("stained_skyglass_pink", BlocksPM.STAINED_SKYGLASS_PINK);
    public static final IRegistryItem<Item, BlockItem> STAINED_SKYGLASS_PURPLE = registerSimpleBlockItemSupplier("stained_skyglass_purple", BlocksPM.STAINED_SKYGLASS_PURPLE);
    public static final IRegistryItem<Item, BlockItem> STAINED_SKYGLASS_RED = registerSimpleBlockItemSupplier("stained_skyglass_red", BlocksPM.STAINED_SKYGLASS_RED);
    public static final IRegistryItem<Item, BlockItem> STAINED_SKYGLASS_WHITE = registerSimpleBlockItemSupplier("stained_skyglass_white", BlocksPM.STAINED_SKYGLASS_WHITE);
    public static final IRegistryItem<Item, BlockItem> STAINED_SKYGLASS_YELLOW = registerSimpleBlockItemSupplier("stained_skyglass_yellow", BlocksPM.STAINED_SKYGLASS_YELLOW);

    // Register skyglass pane block items
    public static final IRegistryItem<Item, BlockItem> SKYGLASS_PANE = registerSimpleBlockItemSupplier("skyglass_pane", BlocksPM.SKYGLASS_PANE);
    public static final IRegistryItem<Item, BlockItem> STAINED_SKYGLASS_PANE_BLACK = registerSimpleBlockItemSupplier("stained_skyglass_pane_black", BlocksPM.STAINED_SKYGLASS_PANE_BLACK);
    public static final IRegistryItem<Item, BlockItem> STAINED_SKYGLASS_PANE_BLUE = registerSimpleBlockItemSupplier("stained_skyglass_pane_blue", BlocksPM.STAINED_SKYGLASS_PANE_BLUE);
    public static final IRegistryItem<Item, BlockItem> STAINED_SKYGLASS_PANE_BROWN = registerSimpleBlockItemSupplier("stained_skyglass_pane_brown", BlocksPM.STAINED_SKYGLASS_PANE_BROWN);
    public static final IRegistryItem<Item, BlockItem> STAINED_SKYGLASS_PANE_CYAN = registerSimpleBlockItemSupplier("stained_skyglass_pane_cyan", BlocksPM.STAINED_SKYGLASS_PANE_CYAN);
    public static final IRegistryItem<Item, BlockItem> STAINED_SKYGLASS_PANE_GRAY = registerSimpleBlockItemSupplier("stained_skyglass_pane_gray", BlocksPM.STAINED_SKYGLASS_PANE_GRAY);
    public static final IRegistryItem<Item, BlockItem> STAINED_SKYGLASS_PANE_GREEN = registerSimpleBlockItemSupplier("stained_skyglass_pane_green", BlocksPM.STAINED_SKYGLASS_PANE_GREEN);
    public static final IRegistryItem<Item, BlockItem> STAINED_SKYGLASS_PANE_LIGHT_BLUE = registerSimpleBlockItemSupplier("stained_skyglass_pane_light_blue", BlocksPM.STAINED_SKYGLASS_PANE_LIGHT_BLUE);
    public static final IRegistryItem<Item, BlockItem> STAINED_SKYGLASS_PANE_LIGHT_GRAY = registerSimpleBlockItemSupplier("stained_skyglass_pane_light_gray", BlocksPM.STAINED_SKYGLASS_PANE_LIGHT_GRAY);
    public static final IRegistryItem<Item, BlockItem> STAINED_SKYGLASS_PANE_LIME = registerSimpleBlockItemSupplier("stained_skyglass_pane_lime", BlocksPM.STAINED_SKYGLASS_PANE_LIME);
    public static final IRegistryItem<Item, BlockItem> STAINED_SKYGLASS_PANE_MAGENTA = registerSimpleBlockItemSupplier("stained_skyglass_pane_magenta", BlocksPM.STAINED_SKYGLASS_PANE_MAGENTA);
    public static final IRegistryItem<Item, BlockItem> STAINED_SKYGLASS_PANE_ORANGE = registerSimpleBlockItemSupplier("stained_skyglass_pane_orange", BlocksPM.STAINED_SKYGLASS_PANE_ORANGE);
    public static final IRegistryItem<Item, BlockItem> STAINED_SKYGLASS_PANE_PINK = registerSimpleBlockItemSupplier("stained_skyglass_pane_pink", BlocksPM.STAINED_SKYGLASS_PANE_PINK);
    public static final IRegistryItem<Item, BlockItem> STAINED_SKYGLASS_PANE_PURPLE = registerSimpleBlockItemSupplier("stained_skyglass_pane_purple", BlocksPM.STAINED_SKYGLASS_PANE_PURPLE);
    public static final IRegistryItem<Item, BlockItem> STAINED_SKYGLASS_PANE_RED = registerSimpleBlockItemSupplier("stained_skyglass_pane_red", BlocksPM.STAINED_SKYGLASS_PANE_RED);
    public static final IRegistryItem<Item, BlockItem> STAINED_SKYGLASS_PANE_WHITE = registerSimpleBlockItemSupplier("stained_skyglass_pane_white", BlocksPM.STAINED_SKYGLASS_PANE_WHITE);
    public static final IRegistryItem<Item, BlockItem> STAINED_SKYGLASS_PANE_YELLOW = registerSimpleBlockItemSupplier("stained_skyglass_pane_yellow", BlocksPM.STAINED_SKYGLASS_PANE_YELLOW);

    // Register ritual candle block items
    public static final IRegistryItem<Item, BlockItem> RITUAL_CANDLE_BLACK = registerSimpleBlockItemSupplier("ritual_candle_black", BlocksPM.RITUAL_CANDLE_BLACK);
    public static final IRegistryItem<Item, BlockItem> RITUAL_CANDLE_BLUE = registerSimpleBlockItemSupplier("ritual_candle_blue", BlocksPM.RITUAL_CANDLE_BLUE);
    public static final IRegistryItem<Item, BlockItem> RITUAL_CANDLE_BROWN = registerSimpleBlockItemSupplier("ritual_candle_brown", BlocksPM.RITUAL_CANDLE_BROWN);
    public static final IRegistryItem<Item, BlockItem> RITUAL_CANDLE_CYAN = registerSimpleBlockItemSupplier("ritual_candle_cyan", BlocksPM.RITUAL_CANDLE_CYAN);
    public static final IRegistryItem<Item, BlockItem> RITUAL_CANDLE_GRAY = registerSimpleBlockItemSupplier("ritual_candle_gray", BlocksPM.RITUAL_CANDLE_GRAY);
    public static final IRegistryItem<Item, BlockItem> RITUAL_CANDLE_GREEN = registerSimpleBlockItemSupplier("ritual_candle_green", BlocksPM.RITUAL_CANDLE_GREEN);
    public static final IRegistryItem<Item, BlockItem> RITUAL_CANDLE_LIGHT_BLUE = registerSimpleBlockItemSupplier("ritual_candle_light_blue", BlocksPM.RITUAL_CANDLE_LIGHT_BLUE);
    public static final IRegistryItem<Item, BlockItem> RITUAL_CANDLE_LIGHT_GRAY = registerSimpleBlockItemSupplier("ritual_candle_light_gray", BlocksPM.RITUAL_CANDLE_LIGHT_GRAY);
    public static final IRegistryItem<Item, BlockItem> RITUAL_CANDLE_LIME = registerSimpleBlockItemSupplier("ritual_candle_lime", BlocksPM.RITUAL_CANDLE_LIME);
    public static final IRegistryItem<Item, BlockItem> RITUAL_CANDLE_MAGENTA = registerSimpleBlockItemSupplier("ritual_candle_magenta", BlocksPM.RITUAL_CANDLE_MAGENTA);
    public static final IRegistryItem<Item, BlockItem> RITUAL_CANDLE_ORANGE = registerSimpleBlockItemSupplier("ritual_candle_orange", BlocksPM.RITUAL_CANDLE_ORANGE);
    public static final IRegistryItem<Item, BlockItem> RITUAL_CANDLE_PINK = registerSimpleBlockItemSupplier("ritual_candle_pink", BlocksPM.RITUAL_CANDLE_PINK);
    public static final IRegistryItem<Item, BlockItem> RITUAL_CANDLE_PURPLE = registerSimpleBlockItemSupplier("ritual_candle_purple", BlocksPM.RITUAL_CANDLE_PURPLE);
    public static final IRegistryItem<Item, BlockItem> RITUAL_CANDLE_RED = registerSimpleBlockItemSupplier("ritual_candle_red", BlocksPM.RITUAL_CANDLE_RED);
    public static final IRegistryItem<Item, BlockItem> RITUAL_CANDLE_WHITE = registerSimpleBlockItemSupplier("ritual_candle_white", BlocksPM.RITUAL_CANDLE_WHITE);
    public static final IRegistryItem<Item, BlockItem> RITUAL_CANDLE_YELLOW = registerSimpleBlockItemSupplier("ritual_candle_yellow", BlocksPM.RITUAL_CANDLE_YELLOW);

    // Register mana font block items
    public static final IRegistryItem<Item, ManaFontBlockItem> ANCIENT_FONT_EARTH = registerSupplier("ancient_font_earth", Services.ITEM_PROTOTYPES.manaFont(BlocksPM.ANCIENT_FONT_EARTH), () -> new Item.Properties().stacksTo(16).rarity(Rarity.RARE));
    public static final IRegistryItem<Item, ManaFontBlockItem> ANCIENT_FONT_SEA = registerSupplier("ancient_font_sea", Services.ITEM_PROTOTYPES.manaFont(BlocksPM.ANCIENT_FONT_SEA), () -> new Item.Properties().stacksTo(16).rarity(Rarity.RARE));
    public static final IRegistryItem<Item, ManaFontBlockItem> ANCIENT_FONT_SKY = registerSupplier("ancient_font_sky", Services.ITEM_PROTOTYPES.manaFont(BlocksPM.ANCIENT_FONT_SKY), () -> new Item.Properties().stacksTo(16).rarity(Rarity.RARE));
    public static final IRegistryItem<Item, ManaFontBlockItem> ANCIENT_FONT_SUN = registerSupplier("ancient_font_sun", Services.ITEM_PROTOTYPES.manaFont(BlocksPM.ANCIENT_FONT_SUN), () -> new Item.Properties().stacksTo(16).rarity(Rarity.RARE));
    public static final IRegistryItem<Item, ManaFontBlockItem> ANCIENT_FONT_MOON = registerSupplier("ancient_font_moon", Services.ITEM_PROTOTYPES.manaFont(BlocksPM.ANCIENT_FONT_MOON), () -> new Item.Properties().stacksTo(16).rarity(Rarity.RARE));
    public static final IRegistryItem<Item, ManaFontBlockItem> ARTIFICIAL_FONT_EARTH = registerSupplier("artificial_font_earth", Services.ITEM_PROTOTYPES.manaFont(BlocksPM.ARTIFICIAL_FONT_EARTH), () -> new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON));
    public static final IRegistryItem<Item, ManaFontBlockItem> ARTIFICIAL_FONT_SEA = registerSupplier("artificial_font_sea", Services.ITEM_PROTOTYPES.manaFont(BlocksPM.ARTIFICIAL_FONT_SEA), () -> new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON));
    public static final IRegistryItem<Item, ManaFontBlockItem> ARTIFICIAL_FONT_SKY = registerSupplier("artificial_font_sky", Services.ITEM_PROTOTYPES.manaFont(BlocksPM.ARTIFICIAL_FONT_SKY), () -> new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON));
    public static final IRegistryItem<Item, ManaFontBlockItem> ARTIFICIAL_FONT_SUN = registerSupplier("artificial_font_sun", Services.ITEM_PROTOTYPES.manaFont(BlocksPM.ARTIFICIAL_FONT_SUN), () -> new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON));
    public static final IRegistryItem<Item, ManaFontBlockItem> ARTIFICIAL_FONT_MOON = registerSupplier("artificial_font_moon", Services.ITEM_PROTOTYPES.manaFont(BlocksPM.ARTIFICIAL_FONT_MOON), () -> new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON));
    public static final IRegistryItem<Item, ManaFontBlockItem> ARTIFICIAL_FONT_BLOOD = registerSupplier("artificial_font_blood", Services.ITEM_PROTOTYPES.manaFont(BlocksPM.ARTIFICIAL_FONT_BLOOD), () -> new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON));
    public static final IRegistryItem<Item, ManaFontBlockItem> ARTIFICIAL_FONT_INFERNAL = registerSupplier("artificial_font_infernal", Services.ITEM_PROTOTYPES.manaFont(BlocksPM.ARTIFICIAL_FONT_INFERNAL), () -> new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON));
    public static final IRegistryItem<Item, ManaFontBlockItem> ARTIFICIAL_FONT_VOID = registerSupplier("artificial_font_void", Services.ITEM_PROTOTYPES.manaFont(BlocksPM.ARTIFICIAL_FONT_VOID), () -> new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON));
    public static final IRegistryItem<Item, ManaFontBlockItem> ARTIFICIAL_FONT_HALLOWED = registerSupplier("artificial_font_hallowed", Services.ITEM_PROTOTYPES.manaFont(BlocksPM.ARTIFICIAL_FONT_HALLOWED), () -> new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON));
    public static final IRegistryItem<Item, ManaFontBlockItem> FORBIDDEN_FONT_EARTH = registerSupplier("forbidden_font_earth", Services.ITEM_PROTOTYPES.manaFont(BlocksPM.FORBIDDEN_FONT_EARTH), () -> new Item.Properties().stacksTo(16).rarity(Rarity.RARE));
    public static final IRegistryItem<Item, ManaFontBlockItem> FORBIDDEN_FONT_SEA = registerSupplier("forbidden_font_sea", Services.ITEM_PROTOTYPES.manaFont(BlocksPM.FORBIDDEN_FONT_SEA), () -> new Item.Properties().stacksTo(16).rarity(Rarity.RARE));
    public static final IRegistryItem<Item, ManaFontBlockItem> FORBIDDEN_FONT_SKY = registerSupplier("forbidden_font_sky", Services.ITEM_PROTOTYPES.manaFont(BlocksPM.FORBIDDEN_FONT_SKY), () -> new Item.Properties().stacksTo(16).rarity(Rarity.RARE));
    public static final IRegistryItem<Item, ManaFontBlockItem> FORBIDDEN_FONT_SUN = registerSupplier("forbidden_font_sun", Services.ITEM_PROTOTYPES.manaFont(BlocksPM.FORBIDDEN_FONT_SUN), () -> new Item.Properties().stacksTo(16).rarity(Rarity.RARE));
    public static final IRegistryItem<Item, ManaFontBlockItem> FORBIDDEN_FONT_MOON = registerSupplier("forbidden_font_moon", Services.ITEM_PROTOTYPES.manaFont(BlocksPM.FORBIDDEN_FONT_MOON), () -> new Item.Properties().stacksTo(16).rarity(Rarity.RARE));
    public static final IRegistryItem<Item, ManaFontBlockItem> FORBIDDEN_FONT_BLOOD = registerSupplier("forbidden_font_blood", Services.ITEM_PROTOTYPES.manaFont(BlocksPM.FORBIDDEN_FONT_BLOOD), () -> new Item.Properties().stacksTo(16).rarity(Rarity.RARE));
    public static final IRegistryItem<Item, ManaFontBlockItem> FORBIDDEN_FONT_INFERNAL = registerSupplier("forbidden_font_infernal", Services.ITEM_PROTOTYPES.manaFont(BlocksPM.FORBIDDEN_FONT_INFERNAL), () -> new Item.Properties().stacksTo(16).rarity(Rarity.RARE));
    public static final IRegistryItem<Item, ManaFontBlockItem> FORBIDDEN_FONT_VOID = registerSupplier("forbidden_font_void", Services.ITEM_PROTOTYPES.manaFont(BlocksPM.FORBIDDEN_FONT_VOID), () -> new Item.Properties().stacksTo(16).rarity(Rarity.RARE));
    public static final IRegistryItem<Item, ManaFontBlockItem> FORBIDDEN_FONT_HALLOWED = registerSupplier("forbidden_font_hallowed", Services.ITEM_PROTOTYPES.manaFont(BlocksPM.FORBIDDEN_FONT_HALLOWED), () -> new Item.Properties().stacksTo(16).rarity(Rarity.RARE));
    public static final IRegistryItem<Item, ManaFontBlockItem> HEAVENLY_FONT_EARTH = registerSupplier("heavenly_font_earth", Services.ITEM_PROTOTYPES.manaFont(BlocksPM.HEAVENLY_FONT_EARTH), () -> new Item.Properties().stacksTo(16).rarity(Rarity.EPIC));
    public static final IRegistryItem<Item, ManaFontBlockItem> HEAVENLY_FONT_SEA = registerSupplier("heavenly_font_sea", Services.ITEM_PROTOTYPES.manaFont(BlocksPM.HEAVENLY_FONT_SEA), () -> new Item.Properties().stacksTo(16).rarity(Rarity.EPIC));
    public static final IRegistryItem<Item, ManaFontBlockItem> HEAVENLY_FONT_SKY = registerSupplier("heavenly_font_sky", Services.ITEM_PROTOTYPES.manaFont(BlocksPM.HEAVENLY_FONT_SKY), () -> new Item.Properties().stacksTo(16).rarity(Rarity.EPIC));
    public static final IRegistryItem<Item, ManaFontBlockItem> HEAVENLY_FONT_SUN = registerSupplier("heavenly_font_sun", Services.ITEM_PROTOTYPES.manaFont(BlocksPM.HEAVENLY_FONT_SUN), () -> new Item.Properties().stacksTo(16).rarity(Rarity.EPIC));
    public static final IRegistryItem<Item, ManaFontBlockItem> HEAVENLY_FONT_MOON = registerSupplier("heavenly_font_moon", Services.ITEM_PROTOTYPES.manaFont(BlocksPM.HEAVENLY_FONT_MOON), () -> new Item.Properties().stacksTo(16).rarity(Rarity.EPIC));
    public static final IRegistryItem<Item, ManaFontBlockItem> HEAVENLY_FONT_BLOOD = registerSupplier("heavenly_font_blood", Services.ITEM_PROTOTYPES.manaFont(BlocksPM.HEAVENLY_FONT_BLOOD), () -> new Item.Properties().stacksTo(16).rarity(Rarity.EPIC));
    public static final IRegistryItem<Item, ManaFontBlockItem> HEAVENLY_FONT_INFERNAL = registerSupplier("heavenly_font_infernal", Services.ITEM_PROTOTYPES.manaFont(BlocksPM.HEAVENLY_FONT_INFERNAL), () -> new Item.Properties().stacksTo(16).rarity(Rarity.EPIC));
    public static final IRegistryItem<Item, ManaFontBlockItem> HEAVENLY_FONT_VOID = registerSupplier("heavenly_font_void", Services.ITEM_PROTOTYPES.manaFont(BlocksPM.HEAVENLY_FONT_VOID), () -> new Item.Properties().stacksTo(16).rarity(Rarity.EPIC));
    public static final IRegistryItem<Item, ManaFontBlockItem> HEAVENLY_FONT_HALLOWED = registerSupplier("heavenly_font_hallowed", Services.ITEM_PROTOTYPES.manaFont(BlocksPM.HEAVENLY_FONT_HALLOWED), () -> new Item.Properties().stacksTo(16).rarity(Rarity.EPIC));

    // Register device block items
    public static final IRegistryItem<Item, BurnableBlockItem> ARCANE_WORKBENCH = registerSupplier("arcane_workbench", Services.ITEM_PROTOTYPES.burnable(BlocksPM.ARCANE_WORKBENCH, 300));
    public static final IRegistryItem<Item, BlockItem> WAND_ASSEMBLY_TABLE = registerSimpleBlockItemSupplier("wand_assembly_table", BlocksPM.WAND_ASSEMBLY_TABLE);
    public static final IRegistryItem<Item, BurnableBlockItem> WOOD_TABLE = registerSupplier("wood_table", Services.ITEM_PROTOTYPES.burnable(BlocksPM.WOOD_TABLE, 300));
    public static final IRegistryItem<Item, BurnableBlockItem> ANALYSIS_TABLE = registerSupplier("analysis_table", Services.ITEM_PROTOTYPES.burnable(BlocksPM.ANALYSIS_TABLE, 300));
    public static final IRegistryItem<Item, BlockItem> ESSENCE_FURNACE = registerSimpleBlockItemSupplier("essence_furnace", BlocksPM.ESSENCE_FURNACE);
    public static final IRegistryItem<Item, BlockItem> CALCINATOR_BASIC = registerSimpleBlockItemSupplier("calcinator_basic", BlocksPM.CALCINATOR_BASIC, () -> new Item.Properties().rarity(Rarity.COMMON));
    public static final IRegistryItem<Item, BlockItem> CALCINATOR_ENCHANTED = registerSimpleBlockItemSupplier("calcinator_enchanted", BlocksPM.CALCINATOR_ENCHANTED, () -> new Item.Properties().rarity(Rarity.UNCOMMON));
    public static final IRegistryItem<Item, BlockItem> CALCINATOR_FORBIDDEN = registerSimpleBlockItemSupplier("calcinator_forbidden", BlocksPM.CALCINATOR_FORBIDDEN, () -> new Item.Properties().rarity(Rarity.RARE));
    public static final IRegistryItem<Item, BlockItem> CALCINATOR_HEAVENLY = registerSimpleBlockItemSupplier("calcinator_heavenly", BlocksPM.CALCINATOR_HEAVENLY, () -> new Item.Properties().rarity(Rarity.EPIC));
    public static final IRegistryItem<Item, BurnableBlockItem> WAND_INSCRIPTION_TABLE = registerSupplier("wand_inscription_table", Services.ITEM_PROTOTYPES.burnable(BlocksPM.WAND_INSCRIPTION_TABLE, 300));
    public static final IRegistryItem<Item, SpellcraftingAltarBlockItem> SPELLCRAFTING_ALTAR = registerSupplier("spellcrafting_altar", Services.ITEM_PROTOTYPES.spellcraftingAltar(BlocksPM.SPELLCRAFTING_ALTAR));
    public static final IRegistryItem<Item, BlockItem> WAND_CHARGER = registerSimpleBlockItemSupplier("wand_charger", BlocksPM.WAND_CHARGER);
    public static final IRegistryItem<Item, BurnableBlockItem> RESEARCH_TABLE = registerSupplier("research_table", Services.ITEM_PROTOTYPES.burnable(BlocksPM.RESEARCH_TABLE, 300));
    public static final IRegistryItem<Item, BlockItem> SUNLAMP = registerSimpleBlockItemSupplier("sunlamp", BlocksPM.SUNLAMP);
    public static final IRegistryItem<Item, BlockItem> SPIRIT_LANTERN = registerSimpleBlockItemSupplier("spirit_lantern", BlocksPM.SPIRIT_LANTERN);
    public static final IRegistryItem<Item, BlockItem> RITUAL_ALTAR = registerSimpleBlockItemSupplier("ritual_altar", BlocksPM.RITUAL_ALTAR, () -> new Item.Properties().rarity(Rarity.UNCOMMON));
    public static final IRegistryItem<Item, BlockItem> OFFERING_PEDESTAL = registerSimpleBlockItemSupplier("offering_pedestal", BlocksPM.OFFERING_PEDESTAL);
    public static final IRegistryItem<Item, BlockItem> INCENSE_BRAZIER = registerSimpleBlockItemSupplier("incense_brazier", BlocksPM.INCENSE_BRAZIER);
    public static final IRegistryItem<Item, BurnableBlockItem> RITUAL_LECTERN = registerSupplier("ritual_lectern", Services.ITEM_PROTOTYPES.burnable(BlocksPM.RITUAL_LECTERN, 300));
    public static final IRegistryItem<Item, BlockItem> RITUAL_BELL = registerSimpleBlockItemSupplier("ritual_bell", BlocksPM.RITUAL_BELL);
    public static final IRegistryItem<Item, BlockItem> BLOODLETTER = registerSimpleBlockItemSupplier("bloodletter", BlocksPM.BLOODLETTER);
    public static final IRegistryItem<Item, BlockItem> SOUL_ANVIL = registerSimpleBlockItemSupplier("soul_anvil", BlocksPM.SOUL_ANVIL);
    public static final IRegistryItem<Item, BlockItem> RUNESCRIBING_ALTAR_BASIC = registerSimpleBlockItemSupplier("runescribing_altar_basic", BlocksPM.RUNESCRIBING_ALTAR_BASIC, () -> new Item.Properties().rarity(Rarity.COMMON));
    public static final IRegistryItem<Item, BlockItem> RUNESCRIBING_ALTAR_ENCHANTED = registerSimpleBlockItemSupplier("runescribing_altar_enchanted", BlocksPM.RUNESCRIBING_ALTAR_ENCHANTED, () -> new Item.Properties().rarity(Rarity.UNCOMMON));
    public static final IRegistryItem<Item, BlockItem> RUNESCRIBING_ALTAR_FORBIDDEN = registerSimpleBlockItemSupplier("runescribing_altar_forbidden", BlocksPM.RUNESCRIBING_ALTAR_FORBIDDEN, () -> new Item.Properties().rarity(Rarity.RARE));
    public static final IRegistryItem<Item, BlockItem> RUNESCRIBING_ALTAR_HEAVENLY = registerSimpleBlockItemSupplier("runescribing_altar_heavenly", BlocksPM.RUNESCRIBING_ALTAR_HEAVENLY, () -> new Item.Properties().rarity(Rarity.EPIC));
    public static final IRegistryItem<Item, BurnableBlockItem> RUNECARVING_TABLE = registerSupplier("runecarving_table", Services.ITEM_PROTOTYPES.burnable(BlocksPM.RUNECARVING_TABLE, 300));
    public static final IRegistryItem<Item, BlockItem> RUNIC_GRINDSTONE = registerSimpleBlockItemSupplier("runic_grindstone", BlocksPM.RUNIC_GRINDSTONE);
    public static final IRegistryItem<Item, BlockItem> HONEY_EXTRACTOR = registerSimpleBlockItemSupplier("honey_extractor", BlocksPM.HONEY_EXTRACTOR);
    public static final IRegistryItem<Item, BlockItem> PRIMALITE_GOLEM_CONTROLLER = registerSimpleBlockItemSupplier("primalite_golem_controller", BlocksPM.PRIMALITE_GOLEM_CONTROLLER, () -> new Item.Properties().rarity(Rarity.UNCOMMON));
    public static final IRegistryItem<Item, BlockItem> HEXIUM_GOLEM_CONTROLLER = registerSimpleBlockItemSupplier("hexium_golem_controller", BlocksPM.HEXIUM_GOLEM_CONTROLLER, () -> new Item.Properties().rarity(Rarity.RARE));
    public static final IRegistryItem<Item, BlockItem> HALLOWSTEEL_GOLEM_CONTROLLER = registerSimpleBlockItemSupplier("hallowsteel_golem_controller", BlocksPM.HALLOWSTEEL_GOLEM_CONTROLLER, () -> new Item.Properties().rarity(Rarity.EPIC));
    public static final IRegistryItem<Item, BlockItem> SANGUINE_CRUCIBLE = registerSimpleBlockItemSupplier("sanguine_crucible", BlocksPM.SANGUINE_CRUCIBLE);
    public static final IRegistryItem<Item, BlockItem> CONCOCTER = registerSimpleBlockItemSupplier("concocter", BlocksPM.CONCOCTER);
    public static final IRegistryItem<Item, BurnableBlockItem> CELESTIAL_HARP = registerSupplier("celestial_harp", Services.ITEM_PROTOTYPES.burnable(BlocksPM.CELESTIAL_HARP, 300));
    public static final IRegistryItem<Item, BlockItem> ENTROPY_SINK = registerSimpleBlockItemSupplier("entropy_sink", BlocksPM.ENTROPY_SINK);
    public static final IRegistryItem<Item, BlockItem> AUTO_CHARGER = registerSimpleBlockItemSupplier("auto_charger", BlocksPM.AUTO_CHARGER);
    public static final IRegistryItem<Item, BlockItem> ESSENCE_TRANSMUTER = registerSimpleBlockItemSupplier("essence_transmuter", BlocksPM.ESSENCE_TRANSMUTER);
    public static final IRegistryItem<Item, BlockItem> DISSOLUTION_CHAMBER = registerSimpleBlockItemSupplier("dissolution_chamber", BlocksPM.DISSOLUTION_CHAMBER);
    public static final IRegistryItem<Item, BlockItem> ZEPHYR_ENGINE = registerSimpleBlockItemSupplier("zephyr_engine", BlocksPM.ZEPHYR_ENGINE);
    public static final IRegistryItem<Item, BlockItem> VOID_TURBINE = registerSimpleBlockItemSupplier("void_turbine", BlocksPM.VOID_TURBINE);
    public static final IRegistryItem<Item, BurnableBlockItem> ESSENCE_CASK_ENCHANTED = registerSupplier("essence_cask_enchanted", Services.ITEM_PROTOTYPES.burnable(BlocksPM.ESSENCE_CASK_ENCHANTED, 300), () -> new Item.Properties().rarity(Rarity.UNCOMMON));
    public static final IRegistryItem<Item, BurnableBlockItem> ESSENCE_CASK_FORBIDDEN = registerSupplier("essence_cask_forbidden", Services.ITEM_PROTOTYPES.burnable(BlocksPM.ESSENCE_CASK_FORBIDDEN, 300), () -> new Item.Properties().rarity(Rarity.RARE));
    public static final IRegistryItem<Item, BurnableBlockItem> ESSENCE_CASK_HEAVENLY = registerSupplier("essence_cask_heavenly", Services.ITEM_PROTOTYPES.burnable(BlocksPM.ESSENCE_CASK_HEAVENLY, 300), () -> new Item.Properties().rarity(Rarity.EPIC));
    public static final IRegistryItem<Item, BurnableBlockItem> WAND_GLAMOUR_TABLE = registerSupplier("wand_glamour_table", Services.ITEM_PROTOTYPES.burnable(BlocksPM.WAND_GLAMOUR_TABLE, 300));
    public static final IRegistryItem<Item, BlockItem> INFERNAL_FURNACE = registerSimpleBlockItemSupplier("infernal_furnace", BlocksPM.INFERNAL_FURNACE);
    public static final IRegistryItem<Item, BlockItem> MANA_NEXUS = registerSimpleBlockItemSupplier("mana_nexus", BlocksPM.MANA_NEXUS, () -> new Item.Properties().rarity(Rarity.RARE));
    public static final IRegistryItem<Item, BlockItem> MANA_SINGULARITY = registerSimpleBlockItemSupplier("mana_singularity", BlocksPM.MANA_SINGULARITY, () -> new Item.Properties().rarity(Rarity.EPIC));
    public static final IRegistryItem<Item, BlockItem> MANA_SINGULARITY_CREATIVE = registerSimpleBlockItemSupplier("mana_singularity_creative", BlocksPM.MANA_SINGULARITY_CREATIVE, () -> new Item.Properties().rarity(Rarity.EPIC));
    public static final IRegistryItem<Item, BurnableBlockItem> SCRIBE_TABLE = registerSupplier("scribe_table", Services.ITEM_PROTOTYPES.burnable(BlocksPM.SCRIBE_TABLE, 300));
    public static final IRegistryItem<Item, ManaRelayBlockItem> MANA_RELAY_BASIC = registerSupplier("mana_relay_basic", Services.ITEM_PROTOTYPES.manaRelay(BlocksPM.MANA_RELAY_BASIC));
    public static final IRegistryItem<Item, ManaRelayBlockItem> MANA_RELAY_ENCHANTED = registerSupplier("mana_relay_enchanted", Services.ITEM_PROTOTYPES.manaRelay(BlocksPM.MANA_RELAY_ENCHANTED), () -> new Item.Properties().rarity(Rarity.UNCOMMON));
    public static final IRegistryItem<Item, ManaRelayBlockItem> MANA_RELAY_FORBIDDEN = registerSupplier("mana_relay_forbidden", Services.ITEM_PROTOTYPES.manaRelay(BlocksPM.MANA_RELAY_FORBIDDEN), () -> new Item.Properties().rarity(Rarity.RARE));
    public static final IRegistryItem<Item, ManaRelayBlockItem> MANA_RELAY_HEAVENLY = registerSupplier("mana_relay_heavenly", Services.ITEM_PROTOTYPES.manaRelay(BlocksPM.MANA_RELAY_HEAVENLY), () -> new Item.Properties().rarity(Rarity.EPIC));
    public static final IRegistryItem<Item, ManaInjectorBlockItem> MANA_INJECTOR_BASIC = registerSupplier("mana_injector_basic", Services.ITEM_PROTOTYPES.manaInjector(BlocksPM.MANA_INJECTOR_BASIC));
    public static final IRegistryItem<Item, ManaInjectorBlockItem> MANA_INJECTOR_ENCHANTED = registerSupplier("mana_injector_enchanted", Services.ITEM_PROTOTYPES.manaInjector(BlocksPM.MANA_INJECTOR_ENCHANTED), () -> new Item.Properties().rarity(Rarity.UNCOMMON));
    public static final IRegistryItem<Item, ManaInjectorBlockItem> MANA_INJECTOR_FORBIDDEN = registerSupplier("mana_injector_forbidden", Services.ITEM_PROTOTYPES.manaInjector(BlocksPM.MANA_INJECTOR_FORBIDDEN), () -> new Item.Properties().rarity(Rarity.RARE));
    public static final IRegistryItem<Item, ManaInjectorBlockItem> MANA_INJECTOR_HEAVENLY = registerSupplier("mana_injector_heavenly", Services.ITEM_PROTOTYPES.manaInjector(BlocksPM.MANA_INJECTOR_HEAVENLY), () -> new Item.Properties().rarity(Rarity.EPIC));
    public static final IRegistryItem<Item, BlockItem> DESALINATOR = registerSimpleBlockItemSupplier("desalinator", BlocksPM.DESALINATOR);

    // Register pixie structure items
    public static final IRegistryItem<Item, PixieHouseItem> PIXIE_HOUSE = registerSupplier("pixie_house", Services.ITEM_PROTOTYPES.pixieHouse(), () -> new Item.Properties().stacksTo(16));

    // Register miscellaneous block items
    public static final IRegistryItem<Item, BlockItem> REFINED_SALT = registerSimpleBlockItemSupplier("refined_salt", BlocksPM.SALT_TRAIL, () -> new Item.Properties().useItemDescriptionPrefix());
    public static final IRegistryItem<Item, BlockItem> ROCK_SALT_ORE = registerSimpleBlockItemSupplier("rock_salt_ore", BlocksPM.ROCK_SALT_ORE);
    public static final IRegistryItem<Item, BlockItem> QUARTZ_ORE = registerSimpleBlockItemSupplier("quartz_ore", BlocksPM.QUARTZ_ORE);
    public static final IRegistryItem<Item, BlockItem> PRIMALITE_BLOCK = registerSimpleBlockItemSupplier("primalite_block", BlocksPM.PRIMALITE_BLOCK);
    public static final IRegistryItem<Item, BlockItem> HEXIUM_BLOCK = registerSimpleBlockItemSupplier("hexium_block", BlocksPM.HEXIUM_BLOCK);
    public static final IRegistryItem<Item, BlockItem> HALLOWSTEEL_BLOCK = registerSimpleBlockItemSupplier("hallowsteel_block", BlocksPM.HALLOWSTEEL_BLOCK);
    public static final IRegistryItem<Item, BurnableBlockItem> IGNYX_BLOCK = registerSupplier("ignyx_block", Services.ITEM_PROTOTYPES.burnable(BlocksPM.IGNYX_BLOCK, 128000));
    public static final IRegistryItem<Item, BlockItem> SALT_BLOCK = registerSimpleBlockItemSupplier("salt_block", BlocksPM.SALT_BLOCK);
    public static final IRegistryItem<Item, BlockItem> TREEFOLK_SEED = registerSimpleBlockItemSupplier("treefolk_seed", BlocksPM.TREEFOLK_SPROUT, () -> new Item.Properties().useItemDescriptionPrefix());
    public static final IRegistryItem<Item, BlockItem> ENDERWARD = registerSimpleBlockItemSupplier("enderward", BlocksPM.ENDERWARD, () -> new Item.Properties().stacksTo(16));

    // Register salted food items
    public static final IRegistryItem<Item, Item> SALTED_BAKED_POTATO = registerSimpleItemSupplier("salted_baked_potato", () -> new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.72F).build()));
    public static final IRegistryItem<Item, Item> SALTED_COOKED_BEEF = registerSimpleItemSupplier("salted_cooked_beef", () -> new Item.Properties().food(new FoodProperties.Builder().nutrition(10).saturationModifier(0.96F).build()));
    public static final IRegistryItem<Item, Item> SALTED_COOKED_CHICKEN = registerSimpleItemSupplier("salted_cooked_chicken", () -> new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationModifier(0.72F).build()));
    public static final IRegistryItem<Item, Item> SALTED_COOKED_COD = registerSimpleItemSupplier("salted_cooked_cod", () -> new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.72F).build()));
    public static final IRegistryItem<Item, Item> SALTED_COOKED_MUTTON = registerSimpleItemSupplier("salted_cooked_mutton", () -> new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationModifier(0.96F).build()));
    public static final IRegistryItem<Item, Item> SALTED_COOKED_PORKCHOP = registerSimpleItemSupplier("salted_cooked_porkchop", () -> new Item.Properties().food(new FoodProperties.Builder().nutrition(10).saturationModifier(0.96F).build()));
    public static final IRegistryItem<Item, Item> SALTED_COOKED_RABBIT = registerSimpleItemSupplier("salted_cooked_rabbit", () -> new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.72F).build()));
    public static final IRegistryItem<Item, Item> SALTED_COOKED_SALMON = registerSimpleItemSupplier("salted_cooked_salmon", () -> new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationModifier(0.96F).build()));
    public static final IRegistryItem<Item, Item> SALTED_BEETROOT_SOUP = registerSimpleItemSupplier("salted_beetroot_soup", () -> new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(7).saturationModifier(0.72F).build()).usingConvertsTo(Items.BOWL));
    public static final IRegistryItem<Item, Item> SALTED_MUSHROOM_STEW = registerSimpleItemSupplier("salted_mushroom_stew", () -> new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(7).saturationModifier(0.72F).build()).usingConvertsTo(Items.BOWL));
    public static final IRegistryItem<Item, Item> SALTED_RABBIT_STEW = registerSimpleItemSupplier("salted_rabbit_stew", () -> new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(12).saturationModifier(0.72F).build()).usingConvertsTo(Items.BOWL));

    // Register mineral items
    public static final IRegistryItem<Item, Item> IRON_GRIT = registerSimpleItemSupplier("iron_grit");
    public static final IRegistryItem<Item, Item> GOLD_GRIT = registerSimpleItemSupplier("gold_grit");
    public static final IRegistryItem<Item, Item> COPPER_GRIT = registerSimpleItemSupplier("copper_grit");
    public static final IRegistryItem<Item, Item> SALT_PINCH = registerSimpleItemSupplier("salt_pinch");
    public static final IRegistryItem<Item, Item> PRIMALITE_INGOT = registerSimpleItemSupplier("primalite_ingot");
    public static final IRegistryItem<Item, Item> HEXIUM_INGOT = registerSimpleItemSupplier("hexium_ingot");
    public static final IRegistryItem<Item, Item> HALLOWSTEEL_INGOT = registerSimpleItemSupplier("hallowsteel_ingot");
    public static final IRegistryItem<Item, Item> PRIMALITE_NUGGET = registerSimpleItemSupplier("primalite_nugget");
    public static final IRegistryItem<Item, Item> HEXIUM_NUGGET = registerSimpleItemSupplier("hexium_nugget");
    public static final IRegistryItem<Item, Item> HALLOWSTEEL_NUGGET = registerSimpleItemSupplier("hallowsteel_nugget");
    public static final IRegistryItem<Item, Item> QUARTZ_NUGGET = registerSimpleItemSupplier("quartz_nugget");
    public static final IRegistryItem<Item, EnergizedGemItem> ENERGIZED_AMETHYST = registerSupplier("energized_amethyst", EnergizedGemItem::new);
    public static final IRegistryItem<Item, EnergizedGemItem> ENERGIZED_DIAMOND = registerSupplier("energized_diamond", EnergizedGemItem::new);
    public static final IRegistryItem<Item, EnergizedGemItem> ENERGIZED_EMERALD = registerSupplier("energized_emerald", EnergizedGemItem::new);
    public static final IRegistryItem<Item, EnergizedGemItem> ENERGIZED_QUARTZ = registerSupplier("energized_quartz", EnergizedGemItem::new);

    // Register tool items
    public static final IRegistryItem<Item, Item> PRIMALITE_SWORD = registerSimpleItemSupplier("primalite_sword", () -> new Item.Properties().rarity(Rarity.UNCOMMON).sword(ToolMaterialsPM.PRIMALITE, 3, -2.4F));
    public static final IRegistryItem<Item, PrimaliteTridentItem> PRIMALITE_TRIDENT = registerSupplier("primalite_trident", Services.ITEM_PROTOTYPES.primaliteTrident(), () -> new Item.Properties().rarity(Rarity.UNCOMMON));
    public static final IRegistryItem<Item, AbstractTieredBowItem> PRIMALITE_BOW = registerSupplier("primalite_bow", Services.ITEM_PROTOTYPES.tieredBow(ToolMaterialsPM.PRIMALITE), () -> new Item.Properties().rarity(Rarity.UNCOMMON));
    public static final IRegistryItem<Item, ShovelItem> PRIMALITE_SHOVEL = registerSupplier("primalite_shovel", props -> new ShovelItem(ToolMaterialsPM.PRIMALITE, 1.5F, -3.0F, props), () -> new Item.Properties().rarity(Rarity.UNCOMMON));
    public static final IRegistryItem<Item, Item> PRIMALITE_PICKAXE = registerSimpleItemSupplier("primalite_pickaxe", () -> new Item.Properties().rarity(Rarity.UNCOMMON).pickaxe(ToolMaterialsPM.PRIMALITE, 1.0F, -2.8F));
    public static final IRegistryItem<Item, AxeItem> PRIMALITE_AXE = registerSupplier("primalite_axe", props -> new AxeItem(ToolMaterialsPM.PRIMALITE, 5.5F, -3.0F, props), () -> new Item.Properties().rarity(Rarity.UNCOMMON));
    public static final IRegistryItem<Item, HoeItem> PRIMALITE_HOE = registerSupplier("primalite_hoe", props -> new HoeItem(ToolMaterialsPM.PRIMALITE, -2.0F, 0.0F, props), () -> new Item.Properties().rarity(Rarity.UNCOMMON));
    public static final IRegistryItem<Item, TieredFishingRodItem> PRIMALITE_FISHING_ROD = registerSupplier("primalite_fishing_rod", props -> new TieredFishingRodItem(ToolMaterialsPM.PRIMALITE, props), () -> new Item.Properties().rarity(Rarity.UNCOMMON));
    public static final IRegistryItem<Item, PrimaliteShieldItem> PRIMALITE_SHIELD = registerSupplier("primalite_shield", Services.ITEM_PROTOTYPES.primaliteShield(), () -> new Item.Properties().rarity(Rarity.UNCOMMON));
    public static final IRegistryItem<Item, Item> HEXIUM_SWORD = registerSimpleItemSupplier("hexium_sword", () -> new Item.Properties().rarity(Rarity.RARE).sword(ToolMaterialsPM.HEXIUM, 3, -2.4F));
    public static final IRegistryItem<Item, HexiumTridentItem> HEXIUM_TRIDENT = registerSupplier("hexium_trident", Services.ITEM_PROTOTYPES.hexiumTrident(), () -> new Item.Properties().rarity(Rarity.RARE));
    public static final IRegistryItem<Item, AbstractTieredBowItem> HEXIUM_BOW = registerSupplier("hexium_bow", Services.ITEM_PROTOTYPES.tieredBow(ToolMaterialsPM.HEXIUM), () -> new Item.Properties().rarity(Rarity.RARE));
    public static final IRegistryItem<Item, ShovelItem> HEXIUM_SHOVEL = registerSupplier("hexium_shovel", props -> new ShovelItem(ToolMaterialsPM.HEXIUM, 1.5F, -3.0F, props), () -> new Item.Properties().rarity(Rarity.RARE));
    public static final IRegistryItem<Item, Item> HEXIUM_PICKAXE = registerSimpleItemSupplier("hexium_pickaxe", () -> new Item.Properties().rarity(Rarity.RARE).pickaxe(ToolMaterialsPM.HEXIUM, 1.0F, -2.8F));
    public static final IRegistryItem<Item, AxeItem> HEXIUM_AXE = registerSupplier("hexium_axe", props -> new AxeItem(ToolMaterialsPM.HEXIUM, 4.0F, -3.0F, props), () -> new Item.Properties().rarity(Rarity.RARE));
    public static final IRegistryItem<Item, HoeItem> HEXIUM_HOE = registerSupplier("hexium_hoe", props -> new HoeItem(ToolMaterialsPM.HEXIUM, -4.0F, 0.0F, props), () -> new Item.Properties().rarity(Rarity.RARE));
    public static final IRegistryItem<Item, TieredFishingRodItem> HEXIUM_FISHING_ROD = registerSupplier("hexium_fishing_rod", props -> new TieredFishingRodItem(ToolMaterialsPM.HEXIUM, props), () -> new Item.Properties().rarity(Rarity.RARE));
    public static final IRegistryItem<Item, HexiumShieldItem> HEXIUM_SHIELD = registerSupplier("hexium_shield", Services.ITEM_PROTOTYPES.hexiumShield(), () -> new Item.Properties().rarity(Rarity.RARE));
    public static final IRegistryItem<Item, Item> HALLOWSTEEL_SWORD = registerSimpleItemSupplier("hallowsteel_sword", () -> new Item.Properties().rarity(Rarity.EPIC).sword(ToolMaterialsPM.HALLOWSTEEL, 3, -2.4F));
    public static final IRegistryItem<Item, HallowsteelTridentItem> HALLOWSTEEL_TRIDENT = registerSupplier("hallowsteel_trident", Services.ITEM_PROTOTYPES.hallowsteelTrident(), () -> new Item.Properties().rarity(Rarity.EPIC));
    public static final IRegistryItem<Item, AbstractTieredBowItem> HALLOWSTEEL_BOW = registerSupplier("hallowsteel_bow", Services.ITEM_PROTOTYPES.tieredBow(ToolMaterialsPM.HALLOWSTEEL), () -> new Item.Properties().rarity(Rarity.EPIC));
    public static final IRegistryItem<Item, ShovelItem> HALLOWSTEEL_SHOVEL = registerSupplier("hallowsteel_shovel", props -> new ShovelItem(ToolMaterialsPM.HALLOWSTEEL, 1.5F, -3.0F, props), () -> new Item.Properties().rarity(Rarity.EPIC));
    public static final IRegistryItem<Item, Item> HALLOWSTEEL_PICKAXE = registerSimpleItemSupplier("hallowsteel_pickaxe", () -> new Item.Properties().rarity(Rarity.EPIC).pickaxe(ToolMaterialsPM.HALLOWSTEEL, 1.0F, -2.8F));
    public static final IRegistryItem<Item, AxeItem> HALLOWSTEEL_AXE = registerSupplier("hallowsteel_axe", props -> new AxeItem(ToolMaterialsPM.HALLOWSTEEL, 3.5F, -3.0F, props), () -> new Item.Properties().rarity(Rarity.EPIC));
    public static final IRegistryItem<Item, HoeItem> HALLOWSTEEL_HOE = registerSupplier("hallowsteel_hoe", props -> new HoeItem(ToolMaterialsPM.HALLOWSTEEL, -5.0F, 0.0F, props), () -> new Item.Properties().rarity(Rarity.EPIC));
    public static final IRegistryItem<Item, TieredFishingRodItem> HALLOWSTEEL_FISHING_ROD = registerSupplier("hallowsteel_fishing_rod", props -> new TieredFishingRodItem(ToolMaterialsPM.HALLOWSTEEL, props), () -> new Item.Properties().rarity(Rarity.EPIC));
    public static final IRegistryItem<Item, HallowsteelShieldItem> HALLOWSTEEL_SHIELD = registerSupplier("hallowsteel_shield", Services.ITEM_PROTOTYPES.hallowsteelShield(), () -> new Item.Properties().rarity(Rarity.EPIC));
    public static final IRegistryItem<Item, PrimalShovelItem> PRIMAL_SHOVEL = registerDefaultInstance("primal_shovel", props -> new PrimalShovelItem(ToolMaterialsPM.PRIMALITE, 1.5F, -3.0F, props), () -> new Item.Properties().rarity(Rarity.UNCOMMON));
    public static final IRegistryItem<Item, PrimalFishingRodItem> PRIMAL_FISHING_ROD = registerDefaultInstance("primal_fishing_rod", props -> new PrimalFishingRodItem(ToolMaterialsPM.PRIMALITE, props), () -> new Item.Properties().rarity(Rarity.UNCOMMON));
    public static final IRegistryItem<Item, PrimalAxeItem> PRIMAL_AXE = registerDefaultInstance("primal_axe", props -> new PrimalAxeItem(ToolMaterialsPM.PRIMALITE, 5.5F, -3.0F, props), () -> new Item.Properties().rarity(Rarity.UNCOMMON));
    public static final IRegistryItem<Item, PrimalHoeItem> PRIMAL_HOE = registerDefaultInstance("primal_hoe", props -> new PrimalHoeItem(ToolMaterialsPM.PRIMALITE, -2.0F, 0.0F, props), () -> new Item.Properties().rarity(Rarity.UNCOMMON));
    public static final IRegistryItem<Item, PrimalPickaxeItem> PRIMAL_PICKAXE = registerDefaultInstance("primal_pickaxe", PrimalPickaxeItem::new, () -> new Item.Properties().rarity(Rarity.UNCOMMON).pickaxe(ToolMaterialsPM.PRIMALITE, 1.0F, -2.8F));
    public static final IRegistryItem<Item, ForbiddenTridentItem> FORBIDDEN_TRIDENT = registerDefaultInstance("forbidden_trident", Services.ITEM_PROTOTYPES.forbiddenTrident(), () -> new Item.Properties().rarity(Rarity.RARE));
    public static final IRegistryItem<Item, ForbiddenBowItem> FORBIDDEN_BOW = registerDefaultInstance("forbidden_bow", Services.ITEM_PROTOTYPES.forbiddenBow(), () -> new Item.Properties().rarity(Rarity.RARE));
    public static final IRegistryItem<Item, ForbiddenSwordItem> FORBIDDEN_SWORD = registerDefaultInstance("forbidden_sword", ForbiddenSwordItem::new, () -> new Item.Properties().rarity(Rarity.RARE).sword(ToolMaterialsPM.HEXIUM, 3, -2.4F));
    public static final IRegistryItem<Item, SacredShieldItem> SACRED_SHIELD = registerDefaultInstance("sacred_shield", SacredShieldItem::new, () -> new Item.Properties().rarity(Rarity.EPIC));
    public static final IRegistryItem<Item, SpelltomeItem> SPELLTOME_APPRENTICE = registerSupplier("spelltome_apprentice", Services.ITEM_PROTOTYPES.spelltome(DeviceTier.BASIC), () -> new Item.Properties().rarity(Rarity.COMMON).enchantable(13).stacksTo(1));
    public static final IRegistryItem<Item, SpelltomeItem> SPELLTOME_ADEPT = registerSupplier("spelltome_adept", Services.ITEM_PROTOTYPES.spelltome(DeviceTier.ENCHANTED), () -> new Item.Properties().rarity(Rarity.UNCOMMON).enchantable(18).stacksTo(1));
    public static final IRegistryItem<Item, SpelltomeItem> SPELLTOME_WIZARD = registerSupplier("spelltome_wizard", Services.ITEM_PROTOTYPES.spelltome(DeviceTier.FORBIDDEN), () -> new Item.Properties().rarity(Rarity.RARE).enchantable(23).stacksTo(1));
    public static final IRegistryItem<Item, SpelltomeItem> SPELLTOME_ARCHMAGE = registerSupplier("spelltome_archmage", Services.ITEM_PROTOTYPES.spelltome(DeviceTier.HEAVENLY), () -> new Item.Properties().rarity(Rarity.EPIC).enchantable(28).stacksTo(1));
    public static final IRegistryItem<Item, ManaOrbItem> MANA_ORB_APPRENTICE = registerSupplier("mana_orb_apprentice", Services.ITEM_PROTOTYPES.manaOrb(DeviceTier.BASIC), () -> new Item.Properties().rarity(Rarity.COMMON).enchantable(13).stacksTo(1));
    public static final IRegistryItem<Item, ManaOrbItem> MANA_ORB_ADEPT = registerSupplier("mana_orb_adept", Services.ITEM_PROTOTYPES.manaOrb(DeviceTier.ENCHANTED), () -> new Item.Properties().rarity(Rarity.UNCOMMON).enchantable(18).stacksTo(1));
    public static final IRegistryItem<Item, ManaOrbItem> MANA_ORB_WIZARD = registerSupplier("mana_orb_wizard", Services.ITEM_PROTOTYPES.manaOrb(DeviceTier.FORBIDDEN), () -> new Item.Properties().rarity(Rarity.RARE).enchantable(23).stacksTo(1));
    public static final IRegistryItem<Item, ManaOrbItem> MANA_ORB_ARCHMAGE = registerSupplier("mana_orb_archmage", Services.ITEM_PROTOTYPES.manaOrb(DeviceTier.HEAVENLY), () -> new Item.Properties().rarity(Rarity.EPIC).enchantable(28).stacksTo(1));

    // Register mana arrow items
    public static final IRegistryItem<Item, ManaArrowItem> MANA_ARROW_EARTH = registerSupplier("mana_arrow_earth", props -> new ManaArrowItem(Sources.EARTH, props));
    public static final IRegistryItem<Item, ManaArrowItem> MANA_ARROW_SEA = registerSupplier("mana_arrow_sea", props -> new ManaArrowItem(Sources.SEA, props));
    public static final IRegistryItem<Item, ManaArrowItem> MANA_ARROW_SKY = registerSupplier("mana_arrow_sky", props -> new ManaArrowItem(Sources.SKY, props));
    public static final IRegistryItem<Item, ManaArrowItem> MANA_ARROW_SUN = registerSupplier("mana_arrow_sun", props -> new ManaArrowItem(Sources.SUN, props));
    public static final IRegistryItem<Item, ManaArrowItem> MANA_ARROW_MOON = registerSupplier("mana_arrow_moon", props -> new ManaArrowItem(Sources.MOON, props));
    public static final IRegistryItem<Item, ManaArrowItem> MANA_ARROW_BLOOD = registerSupplier("mana_arrow_blood", props -> new ManaArrowItem(Sources.BLOOD, props));
    public static final IRegistryItem<Item, ManaArrowItem> MANA_ARROW_INFERNAL = registerSupplier("mana_arrow_infernal", props -> new ManaArrowItem(Sources.INFERNAL, props));
    public static final IRegistryItem<Item, ManaArrowItem> MANA_ARROW_VOID = registerSupplier("mana_arrow_void", props -> new ManaArrowItem(Sources.VOID, props));
    public static final IRegistryItem<Item, ManaArrowItem> MANA_ARROW_HALLOWED = registerSupplier("mana_arrow_hallowed", props -> new ManaArrowItem(Sources.HALLOWED, props));

    // Register armor items
    public static final IRegistryItem<Item, RobeArmorItem> IMBUED_WOOL_HEAD = registerSupplier("imbued_wool_head", RobeArmorItem::new, () -> new Item.Properties().rarity(Rarity.COMMON).humanoidArmor(ArmorMaterialsPM.IMBUED_WOOL, ArmorType.HELMET).component(DataComponentsPM.MANA_DISCOUNT.get(), 1));
    public static final IRegistryItem<Item, RobeArmorItem> IMBUED_WOOL_CHEST = registerSupplier("imbued_wool_chest", RobeArmorItem::new, () -> new Item.Properties().rarity(Rarity.COMMON).humanoidArmor(ArmorMaterialsPM.IMBUED_WOOL, ArmorType.CHESTPLATE).component(DataComponentsPM.MANA_DISCOUNT.get(), 2));
    public static final IRegistryItem<Item, RobeArmorItem> IMBUED_WOOL_LEGS = registerSupplier("imbued_wool_legs", RobeArmorItem::new, () -> new Item.Properties().rarity(Rarity.COMMON).humanoidArmor(ArmorMaterialsPM.IMBUED_WOOL, ArmorType.LEGGINGS).component(DataComponentsPM.MANA_DISCOUNT.get(), 1));
    public static final IRegistryItem<Item, RobeArmorItem> IMBUED_WOOL_FEET = registerSupplier("imbued_wool_feet", RobeArmorItem::new, () -> new Item.Properties().rarity(Rarity.COMMON).humanoidArmor(ArmorMaterialsPM.IMBUED_WOOL, ArmorType.BOOTS).component(DataComponentsPM.MANA_DISCOUNT.get(), 1));
    public static final IRegistryItem<Item, RobeArmorItem> SPELLCLOTH_HEAD = registerSupplier("spellcloth_head", RobeArmorItem::new, () -> new Item.Properties().rarity(Rarity.UNCOMMON).humanoidArmor(ArmorMaterialsPM.SPELLCLOTH, ArmorType.HELMET).component(DataComponentsPM.MANA_DISCOUNT.get(), 2));
    public static final IRegistryItem<Item, RobeArmorItem> SPELLCLOTH_CHEST = registerSupplier("spellcloth_chest", RobeArmorItem::new, () -> new Item.Properties().rarity(Rarity.UNCOMMON).humanoidArmor(ArmorMaterialsPM.SPELLCLOTH, ArmorType.CHESTPLATE).component(DataComponentsPM.MANA_DISCOUNT.get(), 3));
    public static final IRegistryItem<Item, RobeArmorItem> SPELLCLOTH_LEGS = registerSupplier("spellcloth_legs", RobeArmorItem::new, () -> new Item.Properties().rarity(Rarity.UNCOMMON).humanoidArmor(ArmorMaterialsPM.SPELLCLOTH, ArmorType.LEGGINGS).component(DataComponentsPM.MANA_DISCOUNT.get(), 3));
    public static final IRegistryItem<Item, RobeArmorItem> SPELLCLOTH_FEET = registerSupplier("spellcloth_feet", RobeArmorItem::new, () -> new Item.Properties().rarity(Rarity.UNCOMMON).humanoidArmor(ArmorMaterialsPM.SPELLCLOTH, ArmorType.BOOTS).component(DataComponentsPM.MANA_DISCOUNT.get(), 2));
    public static final IRegistryItem<Item, RobeArmorItem> HEXWEAVE_HEAD = registerSupplier("hexweave_head", RobeArmorItem::new, () -> new Item.Properties().rarity(Rarity.RARE).humanoidArmor(ArmorMaterialsPM.HEXWEAVE, ArmorType.HELMET).component(DataComponentsPM.MANA_DISCOUNT.get(), 3));
    public static final IRegistryItem<Item, RobeArmorItem> HEXWEAVE_CHEST = registerSupplier("hexweave_chest", RobeArmorItem::new, () -> new Item.Properties().rarity(Rarity.RARE).humanoidArmor(ArmorMaterialsPM.HEXWEAVE, ArmorType.CHESTPLATE).component(DataComponentsPM.MANA_DISCOUNT.get(), 5));
    public static final IRegistryItem<Item, RobeArmorItem> HEXWEAVE_LEGS = registerSupplier("hexweave_legs", RobeArmorItem::new, () -> new Item.Properties().rarity(Rarity.RARE).humanoidArmor(ArmorMaterialsPM.HEXWEAVE, ArmorType.LEGGINGS).component(DataComponentsPM.MANA_DISCOUNT.get(), 4));
    public static final IRegistryItem<Item, RobeArmorItem> HEXWEAVE_FEET = registerSupplier("hexweave_feet", RobeArmorItem::new, () -> new Item.Properties().rarity(Rarity.RARE).humanoidArmor(ArmorMaterialsPM.HEXWEAVE, ArmorType.BOOTS).component(DataComponentsPM.MANA_DISCOUNT.get(), 3));
    public static final IRegistryItem<Item, RobeArmorItem> SAINTSWOOL_HEAD = registerSupplier("saintswool_head", RobeArmorItem::new, () -> new Item.Properties().rarity(Rarity.EPIC).humanoidArmor(ArmorMaterialsPM.SAINTSWOOL, ArmorType.HELMET).component(DataComponentsPM.MANA_DISCOUNT.get(), 4));
    public static final IRegistryItem<Item, RobeArmorItem> SAINTSWOOL_CHEST = registerSupplier("saintswool_chest", RobeArmorItem::new, () -> new Item.Properties().rarity(Rarity.EPIC).humanoidArmor(ArmorMaterialsPM.SAINTSWOOL, ArmorType.CHESTPLATE).component(DataComponentsPM.MANA_DISCOUNT.get(), 6));
    public static final IRegistryItem<Item, RobeArmorItem> SAINTSWOOL_LEGS = registerSupplier("saintswool_legs", RobeArmorItem::new, () -> new Item.Properties().rarity(Rarity.EPIC).humanoidArmor(ArmorMaterialsPM.SAINTSWOOL, ArmorType.LEGGINGS).component(DataComponentsPM.MANA_DISCOUNT.get(), 6));
    public static final IRegistryItem<Item, RobeArmorItem> SAINTSWOOL_FEET = registerSupplier("saintswool_feet", RobeArmorItem::new, () -> new Item.Properties().rarity(Rarity.EPIC).humanoidArmor(ArmorMaterialsPM.SAINTSWOOL, ArmorType.BOOTS).component(DataComponentsPM.MANA_DISCOUNT.get(), 4));
    public static final IRegistryItem<Item, Item> PRIMALITE_HEAD = registerSimpleItemSupplier("primalite_head", () -> new Item.Properties().rarity(Rarity.UNCOMMON).humanoidArmor(ArmorMaterialsPM.PRIMALITE, ArmorType.HELMET));
    public static final IRegistryItem<Item, Item> PRIMALITE_CHEST = registerSimpleItemSupplier("primalite_chest", () -> new Item.Properties().rarity(Rarity.UNCOMMON).humanoidArmor(ArmorMaterialsPM.PRIMALITE, ArmorType.CHESTPLATE));
    public static final IRegistryItem<Item, Item> PRIMALITE_LEGS = registerSimpleItemSupplier("primalite_legs", () -> new Item.Properties().rarity(Rarity.UNCOMMON).humanoidArmor(ArmorMaterialsPM.PRIMALITE, ArmorType.LEGGINGS));
    public static final IRegistryItem<Item, Item> PRIMALITE_FEET = registerSimpleItemSupplier("primalite_feet", () -> new Item.Properties().rarity(Rarity.UNCOMMON).humanoidArmor(ArmorMaterialsPM.PRIMALITE, ArmorType.BOOTS));
    public static final IRegistryItem<Item, Item> HEXIUM_HEAD = registerSimpleItemSupplier("hexium_head", () -> new Item.Properties().rarity(Rarity.RARE).humanoidArmor(ArmorMaterialsPM.HEXIUM, ArmorType.HELMET));
    public static final IRegistryItem<Item, Item> HEXIUM_CHEST = registerSimpleItemSupplier("hexium_chest", () -> new Item.Properties().rarity(Rarity.RARE).humanoidArmor(ArmorMaterialsPM.HEXIUM, ArmorType.CHESTPLATE));
    public static final IRegistryItem<Item, Item> HEXIUM_LEGS = registerSimpleItemSupplier("hexium_legs", () -> new Item.Properties().rarity(Rarity.RARE).humanoidArmor(ArmorMaterialsPM.HEXIUM, ArmorType.LEGGINGS));
    public static final IRegistryItem<Item, Item> HEXIUM_FEET = registerSimpleItemSupplier("hexium_feet", () -> new Item.Properties().rarity(Rarity.RARE).humanoidArmor(ArmorMaterialsPM.HEXIUM, ArmorType.BOOTS));
    public static final IRegistryItem<Item, Item> HALLOWSTEEL_HEAD = registerSimpleItemSupplier("hallowsteel_head", () -> new Item.Properties().rarity(Rarity.EPIC).humanoidArmor(ArmorMaterialsPM.HALLOWSTEEL, ArmorType.HELMET));
    public static final IRegistryItem<Item, Item> HALLOWSTEEL_CHEST = registerSimpleItemSupplier("hallowsteel_chest", () -> new Item.Properties().rarity(Rarity.EPIC).humanoidArmor(ArmorMaterialsPM.HALLOWSTEEL, ArmorType.CHESTPLATE));
    public static final IRegistryItem<Item, Item> HALLOWSTEEL_LEGS = registerSimpleItemSupplier("hallowsteel_legs", () -> new Item.Properties().rarity(Rarity.EPIC).humanoidArmor(ArmorMaterialsPM.HALLOWSTEEL, ArmorType.LEGGINGS));
    public static final IRegistryItem<Item, Item> HALLOWSTEEL_FEET = registerSimpleItemSupplier("hallowsteel_feet", () -> new Item.Properties().rarity(Rarity.EPIC).humanoidArmor(ArmorMaterialsPM.HALLOWSTEEL, ArmorType.BOOTS));

    // Register miscellaneous items
    public static final IRegistryItem<Item, ArcanometerItem> ARCANOMETER = registerSupplier("arcanometer", Services.ITEM_PROTOTYPES.arcanometer(), () -> new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON));
    public static final IRegistryItem<Item, Item> MAGNIFYING_GLASS = registerSimpleItemSupplier("magnifying_glass");
    public static final IRegistryItem<Item, Item> ALCHEMICAL_WASTE = registerSimpleItemSupplier("alchemical_waste");
    public static final IRegistryItem<Item, BloodyFleshItem> BLOODY_FLESH = registerSupplier("bloody_flesh", BloodyFleshItem::new, () -> new Item.Properties().rarity(Rarity.UNCOMMON).food(FoodsPM.BLOODY_FLESH));
    public static final IRegistryItem<Item, HallowedOrbItem> HALLOWED_ORB = registerSupplier("hallowed_orb", HallowedOrbItem::new, () -> new Item.Properties().rarity(Rarity.RARE));
    public static final IRegistryItem<Item, Item> HEARTWOOD = registerSimpleItemSupplier("heartwood");
    public static final IRegistryItem<Item, Item> ENCHANTED_INK = registerSimpleItemSupplier("enchanted_ink");
    public static final IRegistryItem<Item, Item> ENCHANTED_INK_AND_QUILL = registerSupplier("enchanted_ink_and_quill", EnchantedInkAndQuillItem::new, () -> new Item.Properties().durability(63));
    public static final IRegistryItem<Item, SeascribePenItem> SEASCRIBE_PEN = registerSupplier("seascribe_pen", SeascribePenItem::new);
    public static final IRegistryItem<Item, Item> ROCK_SALT = registerSimpleItemSupplier("rock_salt");
    public static final IRegistryItem<Item, Item> EARTHSHATTER_HAMMER = registerSimpleItemSupplier("earthshatter_hammer", () -> new Item.Properties().rarity(Rarity.UNCOMMON));
    public static final IRegistryItem<Item, Item> MANA_PRISM = registerSimpleItemSupplier("mana_prism");
    public static final IRegistryItem<Item, Item> TALLOW = registerSimpleItemSupplier("tallow");
    public static final IRegistryItem<Item, Item> BEESWAX = registerSupplier("beeswax", HoneycombItem::new);
    public static final IRegistryItem<Item, Item> MANA_SALTS = registerSimpleItemSupplier("mana_salts");
    public static final IRegistryItem<Item, ManafruitItem> MANAFRUIT = registerSupplier("manafruit", props -> new ManafruitItem(0, props), () -> new Item.Properties().food(new FoodProperties.Builder().nutrition(8).saturationModifier(0.8F).build()));
    public static final IRegistryItem<Item, Item> INCENSE_STICK = registerSimpleItemSupplier("incense_stick");
    public static final IRegistryItem<Item, Item> SOUL_GEM = registerSimpleItemSupplier("soul_gem");
    public static final IRegistryItem<Item, Item> SOUL_GEM_SLIVER = registerSimpleItemSupplier("soul_gem_sliver");
    public static final IRegistryItem<Item, Item> SPELLCLOTH = registerSimpleItemSupplier("spellcloth");
    public static final IRegistryItem<Item, Item> HEXWEAVE = registerSimpleItemSupplier("hexweave");
    public static final IRegistryItem<Item, Item> SAINTSWOOL = registerSimpleItemSupplier("saintswool");
    public static final IRegistryItem<Item, Item> MAGITECH_PARTS_BASIC = registerSimpleItemSupplier("magitech_parts_basic");
    public static final IRegistryItem<Item, Item> MAGITECH_PARTS_ENCHANTED = registerSimpleItemSupplier("magitech_parts_enchanted");
    public static final IRegistryItem<Item, Item> MAGITECH_PARTS_FORBIDDEN = registerSimpleItemSupplier("magitech_parts_forbidden");
    public static final IRegistryItem<Item, Item> MAGITECH_PARTS_HEAVENLY = registerSimpleItemSupplier("magitech_parts_heavenly");
    public static final IRegistryItem<Item, FlyingCarpetItem> FLYING_CARPET = registerSupplier("flying_carpet", FlyingCarpetItem::new, () -> new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON));
    public static final IRegistryItem<Item, DreamVisionTalismanItem> DREAM_VISION_TALISMAN = registerSupplier("dream_vision_talisman", DreamVisionTalismanItem::new, () -> new Item.Properties().durability(64));
    public static final IRegistryItem<Item, IgnyxItem> IGNYX = registerSupplier("ignyx", Services.ITEM_PROTOTYPES.ignyx());
    public static final IRegistryItem<Item, DowsingRodItem> DOWSING_ROD = registerSupplier("dowsing_rod", DowsingRodItem::new, () -> new Item.Properties().stacksTo(1).durability(63));
    public static final IRegistryItem<Item, Item> FOUR_LEAF_CLOVER = registerSimpleItemSupplier("four_leaf_clover");
    public static final IRegistryItem<Item, RecallStoneItem> RECALL_STONE = registerSupplier("recall_stone", RecallStoneItem::new);
    public static final IRegistryItem<Item, SmithingTemplateItem> RUNIC_ARMOR_TRIM_SMITHING_TEMPLATE = registerSupplier("runic_armor_trim_smithing_template", TrimPatternsPM::createRunicArmorTrimTemplate);
    public static final IRegistryItem<Item, WardingModuleItem> BASIC_WARDING_MODULE = registerSupplier("warding_module_basic", props -> new WardingModuleItem(DeviceTier.ENCHANTED, props), () -> new Item.Properties().rarity(Rarity.UNCOMMON));
    public static final IRegistryItem<Item, WardingModuleItem> GREATER_WARDING_MODULE = registerSupplier("warding_module_greater", props -> new WardingModuleItem(DeviceTier.FORBIDDEN, props), () -> new Item.Properties().rarity(Rarity.RARE));
    public static final IRegistryItem<Item, WardingModuleItem> SUPREME_WARDING_MODULE = registerSupplier("warding_module_supreme", props -> new WardingModuleItem(DeviceTier.HEAVENLY, props), () -> new Item.Properties().rarity(Rarity.EPIC));

    // Register knowledge items
    public static final IRegistryItem<Item, KnowledgeGainItem> OBSERVATION_NOTES = registerSimpleBlockItemSupplier("observation_notes", () -> new KnowledgeGainItem(KnowledgeType.OBSERVATION, KnowledgeType.OBSERVATION.getProgression(), new Item.Properties()));
    public static final IRegistryItem<Item, KnowledgeGainItem> THEORY_NOTES = registerSimpleBlockItemSupplier("theory_notes", () -> new KnowledgeGainItem(KnowledgeType.THEORY, KnowledgeType.THEORY.getProgression(), new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final IRegistryItem<Item, KnowledgeGainItem> MYSTICAL_RELIC = registerSimpleBlockItemSupplier("mystical_relic", () -> new KnowledgeGainItem(KnowledgeType.THEORY, KnowledgeType.THEORY.getProgression(), new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final IRegistryItem<Item, Item> MYSTICAL_RELIC_FRAGMENT = registerSimpleItemSupplier("mystical_relic_fragment");
    public static final IRegistryItem<Item, ForbiddenSourceGainItem> BLOOD_NOTES = registerSimpleBlockItemSupplier("blood_notes", () -> new ForbiddenSourceGainItem(Sources.BLOOD, new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final IRegistryItem<Item, ResearchGainItem> SHEEP_TOME = registerSimpleBlockItemSupplier("sheep_tome", () -> new ResearchGainItem(() -> new ResearchEntryKey(ResearchEntries.SPELL_PAYLOAD_POLYMORPH_SHEEP), new Item.Properties().rarity(Rarity.UNCOMMON)));

    // Register dust essence items
    public static final IRegistryItem<Item, EssenceItem> ESSENCE_DUST_EARTH = registerSimpleBlockItemSupplier("essence_dust_earth", () -> new EssenceItem(EssenceType.DUST, Sources.EARTH));
    public static final IRegistryItem<Item, EssenceItem> ESSENCE_DUST_SEA = registerSimpleBlockItemSupplier("essence_dust_sea", () -> new EssenceItem(EssenceType.DUST, Sources.SEA));
    public static final IRegistryItem<Item, EssenceItem> ESSENCE_DUST_SKY = registerSimpleBlockItemSupplier("essence_dust_sky", () -> new EssenceItem(EssenceType.DUST, Sources.SKY));
    public static final IRegistryItem<Item, EssenceItem> ESSENCE_DUST_SUN = registerSimpleBlockItemSupplier("essence_dust_sun", () -> new EssenceItem(EssenceType.DUST, Sources.SUN));
    public static final IRegistryItem<Item, EssenceItem> ESSENCE_DUST_MOON = registerSimpleBlockItemSupplier("essence_dust_moon", () -> new EssenceItem(EssenceType.DUST, Sources.MOON));
    public static final IRegistryItem<Item, EssenceItem> ESSENCE_DUST_BLOOD = registerSimpleBlockItemSupplier("essence_dust_blood", () -> new EssenceItem(EssenceType.DUST, Sources.BLOOD));
    public static final IRegistryItem<Item, EssenceItem> ESSENCE_DUST_INFERNAL = registerSimpleBlockItemSupplier("essence_dust_infernal", () -> new EssenceItem(EssenceType.DUST, Sources.INFERNAL));
    public static final IRegistryItem<Item, EssenceItem> ESSENCE_DUST_VOID = registerSimpleBlockItemSupplier("essence_dust_void", () -> new EssenceItem(EssenceType.DUST, Sources.VOID));
    public static final IRegistryItem<Item, EssenceItem> ESSENCE_DUST_HALLOWED = registerSimpleBlockItemSupplier("essence_dust_hallowed", () -> new EssenceItem(EssenceType.DUST, Sources.HALLOWED));

    // Register shard essence items
    public static final IRegistryItem<Item, EssenceItem> ESSENCE_SHARD_EARTH = registerSimpleBlockItemSupplier("essence_shard_earth", () -> new EssenceItem(EssenceType.SHARD, Sources.EARTH));
    public static final IRegistryItem<Item, EssenceItem> ESSENCE_SHARD_SEA = registerSimpleBlockItemSupplier("essence_shard_sea", () -> new EssenceItem(EssenceType.SHARD, Sources.SEA));
    public static final IRegistryItem<Item, EssenceItem> ESSENCE_SHARD_SKY = registerSimpleBlockItemSupplier("essence_shard_sky", () -> new EssenceItem(EssenceType.SHARD, Sources.SKY));
    public static final IRegistryItem<Item, EssenceItem> ESSENCE_SHARD_SUN = registerSimpleBlockItemSupplier("essence_shard_sun", () -> new EssenceItem(EssenceType.SHARD, Sources.SUN));
    public static final IRegistryItem<Item, EssenceItem> ESSENCE_SHARD_MOON = registerSimpleBlockItemSupplier("essence_shard_moon", () -> new EssenceItem(EssenceType.SHARD, Sources.MOON));
    public static final IRegistryItem<Item, EssenceItem> ESSENCE_SHARD_BLOOD = registerSimpleBlockItemSupplier("essence_shard_blood", () -> new EssenceItem(EssenceType.SHARD, Sources.BLOOD));
    public static final IRegistryItem<Item, EssenceItem> ESSENCE_SHARD_INFERNAL = registerSimpleBlockItemSupplier("essence_shard_infernal", () -> new EssenceItem(EssenceType.SHARD, Sources.INFERNAL));
    public static final IRegistryItem<Item, EssenceItem> ESSENCE_SHARD_VOID = registerSimpleBlockItemSupplier("essence_shard_void", () -> new EssenceItem(EssenceType.SHARD, Sources.VOID));
    public static final IRegistryItem<Item, EssenceItem> ESSENCE_SHARD_HALLOWED = registerSimpleBlockItemSupplier("essence_shard_hallowed", () -> new EssenceItem(EssenceType.SHARD, Sources.HALLOWED));

    // Register crystal essence items
    public static final IRegistryItem<Item, EssenceItem> ESSENCE_CRYSTAL_EARTH = registerSimpleBlockItemSupplier("essence_crystal_earth", () -> new EssenceItem(EssenceType.CRYSTAL, Sources.EARTH));
    public static final IRegistryItem<Item, EssenceItem> ESSENCE_CRYSTAL_SEA = registerSimpleBlockItemSupplier("essence_crystal_sea", () -> new EssenceItem(EssenceType.CRYSTAL, Sources.SEA));
    public static final IRegistryItem<Item, EssenceItem> ESSENCE_CRYSTAL_SKY = registerSimpleBlockItemSupplier("essence_crystal_sky", () -> new EssenceItem(EssenceType.CRYSTAL, Sources.SKY));
    public static final IRegistryItem<Item, EssenceItem> ESSENCE_CRYSTAL_SUN = registerSimpleBlockItemSupplier("essence_crystal_sun", () -> new EssenceItem(EssenceType.CRYSTAL, Sources.SUN));
    public static final IRegistryItem<Item, EssenceItem> ESSENCE_CRYSTAL_MOON = registerSimpleBlockItemSupplier("essence_crystal_moon", () -> new EssenceItem(EssenceType.CRYSTAL, Sources.MOON));
    public static final IRegistryItem<Item, EssenceItem> ESSENCE_CRYSTAL_BLOOD = registerSimpleBlockItemSupplier("essence_crystal_blood", () -> new EssenceItem(EssenceType.CRYSTAL, Sources.BLOOD));
    public static final IRegistryItem<Item, EssenceItem> ESSENCE_CRYSTAL_INFERNAL = registerSimpleBlockItemSupplier("essence_crystal_infernal", () -> new EssenceItem(EssenceType.CRYSTAL, Sources.INFERNAL));
    public static final IRegistryItem<Item, EssenceItem> ESSENCE_CRYSTAL_VOID = registerSimpleBlockItemSupplier("essence_crystal_void", () -> new EssenceItem(EssenceType.CRYSTAL, Sources.VOID));
    public static final IRegistryItem<Item, EssenceItem> ESSENCE_CRYSTAL_HALLOWED = registerSimpleBlockItemSupplier("essence_crystal_hallowed", () -> new EssenceItem(EssenceType.CRYSTAL, Sources.HALLOWED));

    // Register cluster essence items
    public static final IRegistryItem<Item, EssenceItem> ESSENCE_CLUSTER_EARTH = registerSimpleBlockItemSupplier("essence_cluster_earth", () -> new EssenceItem(EssenceType.CLUSTER, Sources.EARTH));
    public static final IRegistryItem<Item, EssenceItem> ESSENCE_CLUSTER_SEA = registerSimpleBlockItemSupplier("essence_cluster_sea", () -> new EssenceItem(EssenceType.CLUSTER, Sources.SEA));
    public static final IRegistryItem<Item, EssenceItem> ESSENCE_CLUSTER_SKY = registerSimpleBlockItemSupplier("essence_cluster_sky", () -> new EssenceItem(EssenceType.CLUSTER, Sources.SKY));
    public static final IRegistryItem<Item, EssenceItem> ESSENCE_CLUSTER_SUN = registerSimpleBlockItemSupplier("essence_cluster_sun", () -> new EssenceItem(EssenceType.CLUSTER, Sources.SUN));
    public static final IRegistryItem<Item, EssenceItem> ESSENCE_CLUSTER_MOON = registerSimpleBlockItemSupplier("essence_cluster_moon", () -> new EssenceItem(EssenceType.CLUSTER, Sources.MOON));
    public static final IRegistryItem<Item, EssenceItem> ESSENCE_CLUSTER_BLOOD = registerSimpleBlockItemSupplier("essence_cluster_blood", () -> new EssenceItem(EssenceType.CLUSTER, Sources.BLOOD));
    public static final IRegistryItem<Item, EssenceItem> ESSENCE_CLUSTER_INFERNAL = registerSimpleBlockItemSupplier("essence_cluster_infernal", () -> new EssenceItem(EssenceType.CLUSTER, Sources.INFERNAL));
    public static final IRegistryItem<Item, EssenceItem> ESSENCE_CLUSTER_VOID = registerSimpleBlockItemSupplier("essence_cluster_void", () -> new EssenceItem(EssenceType.CLUSTER, Sources.VOID));
    public static final IRegistryItem<Item, EssenceItem> ESSENCE_CLUSTER_HALLOWED = registerSimpleBlockItemSupplier("essence_cluster_hallowed", () -> new EssenceItem(EssenceType.CLUSTER, Sources.HALLOWED));

    // Register rune items
    public static final IRegistryItem<Item, Item> RUNE_UNATTUNED = registerSimpleItemSupplier("rune_unattuned");
    public static final IRegistryItem<Item, RuneItem> RUNE_EARTH = registerSimpleBlockItemSupplier("rune_earth", () -> new RuneItem(Rune.EARTH));
    public static final IRegistryItem<Item, RuneItem> RUNE_SEA = registerSimpleBlockItemSupplier("rune_sea", () -> new RuneItem(Rune.SEA));
    public static final IRegistryItem<Item, RuneItem> RUNE_SKY = registerSimpleBlockItemSupplier("rune_sky", () -> new RuneItem(Rune.SKY));
    public static final IRegistryItem<Item, RuneItem> RUNE_SUN = registerSimpleBlockItemSupplier("rune_sun", () -> new RuneItem(Rune.SUN));
    public static final IRegistryItem<Item, RuneItem> RUNE_MOON = registerSimpleBlockItemSupplier("rune_moon", () -> new RuneItem(Rune.MOON));
    public static final IRegistryItem<Item, RuneItem> RUNE_BLOOD = registerSimpleBlockItemSupplier("rune_blood", () -> new RuneItem(Rune.BLOOD));
    public static final IRegistryItem<Item, RuneItem> RUNE_INFERNAL = registerSimpleBlockItemSupplier("rune_infernal", () -> new RuneItem(Rune.INFERNAL));
    public static final IRegistryItem<Item, RuneItem> RUNE_VOID = registerSimpleBlockItemSupplier("rune_void", () -> new RuneItem(Rune.VOID));
    public static final IRegistryItem<Item, RuneItem> RUNE_HALLOWED = registerSimpleBlockItemSupplier("rune_hallowed", () -> new RuneItem(Rune.HALLOWED));
    public static final IRegistryItem<Item, RuneItem> RUNE_ABSORB = registerSimpleBlockItemSupplier("rune_absorb", () -> new RuneItem(Rune.ABSORB));
    public static final IRegistryItem<Item, RuneItem> RUNE_DISPEL = registerSimpleBlockItemSupplier("rune_dispel", () -> new RuneItem(Rune.DISPEL));
    public static final IRegistryItem<Item, RuneItem> RUNE_PROJECT = registerSimpleBlockItemSupplier("rune_project", () -> new RuneItem(Rune.PROJECT));
    public static final IRegistryItem<Item, RuneItem> RUNE_PROTECT = registerSimpleBlockItemSupplier("rune_protect", () -> new RuneItem(Rune.PROTECT));
    public static final IRegistryItem<Item, RuneItem> RUNE_SUMMON = registerSimpleBlockItemSupplier("rune_summon", () -> new RuneItem(Rune.SUMMON));
    public static final IRegistryItem<Item, RuneItem> RUNE_AREA = registerSimpleBlockItemSupplier("rune_area", () -> new RuneItem(Rune.AREA));
    public static final IRegistryItem<Item, RuneItem> RUNE_CREATURE = registerSimpleBlockItemSupplier("rune_creature", () -> new RuneItem(Rune.CREATURE));
    public static final IRegistryItem<Item, RuneItem> RUNE_ITEM = registerSimpleBlockItemSupplier("rune_item", () -> new RuneItem(Rune.ITEM));
    public static final IRegistryItem<Item, RuneItem> RUNE_SELF = registerSimpleBlockItemSupplier("rune_self", () -> new RuneItem(Rune.SELF));
    public static final IRegistryItem<Item, RuneItem> RUNE_INSIGHT = registerSimpleBlockItemSupplier("rune_insight", () -> new RuneItem(Rune.INSIGHT));
    public static final IRegistryItem<Item, RuneItem> RUNE_POWER = registerSimpleBlockItemSupplier("rune_power", () -> new RuneItem(Rune.POWER));
    public static final IRegistryItem<Item, RuneItem> RUNE_GRACE = registerSimpleBlockItemSupplier("rune_grace", () -> new RuneItem(Rune.GRACE));

    // Register ambrosia items
    public static final IRegistryItem<Item, AmbrosiaItem> BASIC_EARTH_AMBROSIA = registerSimpleBlockItemSupplier("ambrosia_basic_earth", () -> new AmbrosiaItem(Sources.EARTH, AmbrosiaItem.Type.BASIC, new Item.Properties().rarity(Rarity.UNCOMMON).food(FoodsPM.AMBROSIA)));
    public static final IRegistryItem<Item, AmbrosiaItem> BASIC_SEA_AMBROSIA = registerSimpleBlockItemSupplier("ambrosia_basic_sea", () -> new AmbrosiaItem(Sources.SEA, AmbrosiaItem.Type.BASIC, new Item.Properties().rarity(Rarity.UNCOMMON).food(FoodsPM.AMBROSIA)));
    public static final IRegistryItem<Item, AmbrosiaItem> BASIC_SKY_AMBROSIA = registerSimpleBlockItemSupplier("ambrosia_basic_sky", () -> new AmbrosiaItem(Sources.SKY, AmbrosiaItem.Type.BASIC, new Item.Properties().rarity(Rarity.UNCOMMON).food(FoodsPM.AMBROSIA)));
    public static final IRegistryItem<Item, AmbrosiaItem> BASIC_SUN_AMBROSIA = registerSimpleBlockItemSupplier("ambrosia_basic_sun", () -> new AmbrosiaItem(Sources.SUN, AmbrosiaItem.Type.BASIC, new Item.Properties().rarity(Rarity.UNCOMMON).food(FoodsPM.AMBROSIA)));
    public static final IRegistryItem<Item, AmbrosiaItem> BASIC_MOON_AMBROSIA = registerSimpleBlockItemSupplier("ambrosia_basic_moon", () -> new AmbrosiaItem(Sources.MOON, AmbrosiaItem.Type.BASIC, new Item.Properties().rarity(Rarity.UNCOMMON).food(FoodsPM.AMBROSIA)));
    public static final IRegistryItem<Item, AmbrosiaItem> BASIC_BLOOD_AMBROSIA = registerSimpleBlockItemSupplier("ambrosia_basic_blood", () -> new AmbrosiaItem(Sources.BLOOD, AmbrosiaItem.Type.BASIC, new Item.Properties().rarity(Rarity.UNCOMMON).food(FoodsPM.AMBROSIA)));
    public static final IRegistryItem<Item, AmbrosiaItem> BASIC_INFERNAL_AMBROSIA = registerSimpleBlockItemSupplier("ambrosia_basic_infernal", () -> new AmbrosiaItem(Sources.INFERNAL, AmbrosiaItem.Type.BASIC, new Item.Properties().rarity(Rarity.UNCOMMON).food(FoodsPM.AMBROSIA)));
    public static final IRegistryItem<Item, AmbrosiaItem> BASIC_VOID_AMBROSIA = registerSimpleBlockItemSupplier("ambrosia_basic_void", () -> new AmbrosiaItem(Sources.VOID, AmbrosiaItem.Type.BASIC, new Item.Properties().rarity(Rarity.UNCOMMON).food(FoodsPM.AMBROSIA)));
    public static final IRegistryItem<Item, AmbrosiaItem> BASIC_HALLOWED_AMBROSIA = registerSimpleBlockItemSupplier("ambrosia_basic_hallowed", () -> new AmbrosiaItem(Sources.HALLOWED, AmbrosiaItem.Type.BASIC, new Item.Properties().rarity(Rarity.UNCOMMON).food(FoodsPM.AMBROSIA)));
    public static final IRegistryItem<Item, AmbrosiaItem> GREATER_EARTH_AMBROSIA = registerSimpleBlockItemSupplier("ambrosia_greater_earth", () -> new AmbrosiaItem(Sources.EARTH, AmbrosiaItem.Type.GREATER, new Item.Properties().rarity(Rarity.RARE).food(FoodsPM.AMBROSIA)));
    public static final IRegistryItem<Item, AmbrosiaItem> GREATER_SEA_AMBROSIA = registerSimpleBlockItemSupplier("ambrosia_greater_sea", () -> new AmbrosiaItem(Sources.SEA, AmbrosiaItem.Type.GREATER, new Item.Properties().rarity(Rarity.RARE).food(FoodsPM.AMBROSIA)));
    public static final IRegistryItem<Item, AmbrosiaItem> GREATER_SKY_AMBROSIA = registerSimpleBlockItemSupplier("ambrosia_greater_sky", () -> new AmbrosiaItem(Sources.SKY, AmbrosiaItem.Type.GREATER, new Item.Properties().rarity(Rarity.RARE).food(FoodsPM.AMBROSIA)));
    public static final IRegistryItem<Item, AmbrosiaItem> GREATER_SUN_AMBROSIA = registerSimpleBlockItemSupplier("ambrosia_greater_sun", () -> new AmbrosiaItem(Sources.SUN, AmbrosiaItem.Type.GREATER, new Item.Properties().rarity(Rarity.RARE).food(FoodsPM.AMBROSIA)));
    public static final IRegistryItem<Item, AmbrosiaItem> GREATER_MOON_AMBROSIA = registerSimpleBlockItemSupplier("ambrosia_greater_moon", () -> new AmbrosiaItem(Sources.MOON, AmbrosiaItem.Type.GREATER, new Item.Properties().rarity(Rarity.RARE).food(FoodsPM.AMBROSIA)));
    public static final IRegistryItem<Item, AmbrosiaItem> GREATER_BLOOD_AMBROSIA = registerSimpleBlockItemSupplier("ambrosia_greater_blood", () -> new AmbrosiaItem(Sources.BLOOD, AmbrosiaItem.Type.GREATER, new Item.Properties().rarity(Rarity.RARE).food(FoodsPM.AMBROSIA)));
    public static final IRegistryItem<Item, AmbrosiaItem> GREATER_INFERNAL_AMBROSIA = registerSimpleBlockItemSupplier("ambrosia_greater_infernal", () -> new AmbrosiaItem(Sources.INFERNAL, AmbrosiaItem.Type.GREATER, new Item.Properties().rarity(Rarity.RARE).food(FoodsPM.AMBROSIA)));
    public static final IRegistryItem<Item, AmbrosiaItem> GREATER_VOID_AMBROSIA = registerSimpleBlockItemSupplier("ambrosia_greater_void", () -> new AmbrosiaItem(Sources.VOID, AmbrosiaItem.Type.GREATER, new Item.Properties().rarity(Rarity.RARE).food(FoodsPM.AMBROSIA)));
    public static final IRegistryItem<Item, AmbrosiaItem> GREATER_HALLOWED_AMBROSIA = registerSimpleBlockItemSupplier("ambrosia_greater_hallowed", () -> new AmbrosiaItem(Sources.HALLOWED, AmbrosiaItem.Type.GREATER, new Item.Properties().rarity(Rarity.RARE).food(FoodsPM.AMBROSIA)));
    public static final IRegistryItem<Item, AmbrosiaItem> SUPREME_EARTH_AMBROSIA = registerSimpleBlockItemSupplier("ambrosia_supreme_earth", () -> new AmbrosiaItem(Sources.EARTH, AmbrosiaItem.Type.SUPREME, new Item.Properties().rarity(Rarity.EPIC).food(FoodsPM.AMBROSIA)));
    public static final IRegistryItem<Item, AmbrosiaItem> SUPREME_SEA_AMBROSIA = registerSimpleBlockItemSupplier("ambrosia_supreme_sea", () -> new AmbrosiaItem(Sources.SEA, AmbrosiaItem.Type.SUPREME, new Item.Properties().rarity(Rarity.EPIC).food(FoodsPM.AMBROSIA)));
    public static final IRegistryItem<Item, AmbrosiaItem> SUPREME_SKY_AMBROSIA = registerSimpleBlockItemSupplier("ambrosia_supreme_sky", () -> new AmbrosiaItem(Sources.SKY, AmbrosiaItem.Type.SUPREME, new Item.Properties().rarity(Rarity.EPIC).food(FoodsPM.AMBROSIA)));
    public static final IRegistryItem<Item, AmbrosiaItem> SUPREME_SUN_AMBROSIA = registerSimpleBlockItemSupplier("ambrosia_supreme_sun", () -> new AmbrosiaItem(Sources.SUN, AmbrosiaItem.Type.SUPREME, new Item.Properties().rarity(Rarity.EPIC).food(FoodsPM.AMBROSIA)));
    public static final IRegistryItem<Item, AmbrosiaItem> SUPREME_MOON_AMBROSIA = registerSimpleBlockItemSupplier("ambrosia_supreme_moon", () -> new AmbrosiaItem(Sources.MOON, AmbrosiaItem.Type.SUPREME, new Item.Properties().rarity(Rarity.EPIC).food(FoodsPM.AMBROSIA)));
    public static final IRegistryItem<Item, AmbrosiaItem> SUPREME_BLOOD_AMBROSIA = registerSimpleBlockItemSupplier("ambrosia_supreme_blood", () -> new AmbrosiaItem(Sources.BLOOD, AmbrosiaItem.Type.SUPREME, new Item.Properties().rarity(Rarity.EPIC).food(FoodsPM.AMBROSIA)));
    public static final IRegistryItem<Item, AmbrosiaItem> SUPREME_INFERNAL_AMBROSIA = registerSimpleBlockItemSupplier("ambrosia_supreme_infernal", () -> new AmbrosiaItem(Sources.INFERNAL, AmbrosiaItem.Type.SUPREME, new Item.Properties().rarity(Rarity.EPIC).food(FoodsPM.AMBROSIA)));
    public static final IRegistryItem<Item, AmbrosiaItem> SUPREME_VOID_AMBROSIA = registerSimpleBlockItemSupplier("ambrosia_supreme_void", () -> new AmbrosiaItem(Sources.VOID, AmbrosiaItem.Type.SUPREME, new Item.Properties().rarity(Rarity.EPIC).food(FoodsPM.AMBROSIA)));
    public static final IRegistryItem<Item, AmbrosiaItem> SUPREME_HALLOWED_AMBROSIA = registerSimpleBlockItemSupplier("ambrosia_supreme_hallowed", () -> new AmbrosiaItem(Sources.HALLOWED, AmbrosiaItem.Type.SUPREME, new Item.Properties().rarity(Rarity.EPIC).food(FoodsPM.AMBROSIA)));

    // Register attunement shackle items
    public static final IRegistryItem<Item, AttunementShacklesItem> ATTUNEMENT_SHACKLES_EARTH = registerSimpleBlockItemSupplier("attunement_shackles_earth", () -> new AttunementShacklesItem(Sources.EARTH, new Item.Properties()));
    public static final IRegistryItem<Item, AttunementShacklesItem> ATTUNEMENT_SHACKLES_SEA = registerSimpleBlockItemSupplier("attunement_shackles_sea", () -> new AttunementShacklesItem(Sources.SEA, new Item.Properties()));
    public static final IRegistryItem<Item, AttunementShacklesItem> ATTUNEMENT_SHACKLES_SKY = registerSimpleBlockItemSupplier("attunement_shackles_sky", () -> new AttunementShacklesItem(Sources.SKY, new Item.Properties()));
    public static final IRegistryItem<Item, AttunementShacklesItem> ATTUNEMENT_SHACKLES_SUN = registerSimpleBlockItemSupplier("attunement_shackles_sun", () -> new AttunementShacklesItem(Sources.SUN, new Item.Properties()));
    public static final IRegistryItem<Item, AttunementShacklesItem> ATTUNEMENT_SHACKLES_MOON = registerSimpleBlockItemSupplier("attunement_shackles_moon", () -> new AttunementShacklesItem(Sources.MOON, new Item.Properties()));
    public static final IRegistryItem<Item, AttunementShacklesItem> ATTUNEMENT_SHACKLES_BLOOD = registerSimpleBlockItemSupplier("attunement_shackles_blood", () -> new AttunementShacklesItem(Sources.BLOOD, new Item.Properties()));
    public static final IRegistryItem<Item, AttunementShacklesItem> ATTUNEMENT_SHACKLES_INFERNAL = registerSimpleBlockItemSupplier("attunement_shackles_infernal", () -> new AttunementShacklesItem(Sources.INFERNAL, new Item.Properties()));
    public static final IRegistryItem<Item, AttunementShacklesItem> ATTUNEMENT_SHACKLES_VOID = registerSimpleBlockItemSupplier("attunement_shackles_void", () -> new AttunementShacklesItem(Sources.VOID, new Item.Properties()));
    public static final IRegistryItem<Item, AttunementShacklesItem> ATTUNEMENT_SHACKLES_HALLOWED = registerSimpleBlockItemSupplier("attunement_shackles_hallowed", () -> new AttunementShacklesItem(Sources.HALLOWED, new Item.Properties()));

    // Register humming artifact items
    public static final IRegistryItem<Item, Item> HUMMING_ARTIFACT_UNATTUNED = registerSimpleBlockItemSupplier("humming_artifact_unattuned", () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final IRegistryItem<Item, HummingArtifactItem> HUMMING_ARTIFACT_EARTH = registerSimpleBlockItemSupplier("humming_artifact_earth", () -> new HummingArtifactItem(Sources.EARTH, new Item.Properties().rarity(Rarity.RARE)));
    public static final IRegistryItem<Item, HummingArtifactItem> HUMMING_ARTIFACT_SEA = registerSimpleBlockItemSupplier("humming_artifact_sea", () -> new HummingArtifactItem(Sources.SEA, new Item.Properties().rarity(Rarity.RARE)));
    public static final IRegistryItem<Item, HummingArtifactItem> HUMMING_ARTIFACT_SKY = registerSimpleBlockItemSupplier("humming_artifact_sky", () -> new HummingArtifactItem(Sources.SKY, new Item.Properties().rarity(Rarity.RARE)));
    public static final IRegistryItem<Item, HummingArtifactItem> HUMMING_ARTIFACT_SUN = registerSimpleBlockItemSupplier("humming_artifact_sun", () -> new HummingArtifactItem(Sources.SUN, new Item.Properties().rarity(Rarity.RARE)));
    public static final IRegistryItem<Item, HummingArtifactItem> HUMMING_ARTIFACT_MOON = registerSimpleBlockItemSupplier("humming_artifact_moon", () -> new HummingArtifactItem(Sources.MOON, new Item.Properties().rarity(Rarity.RARE)));
    public static final IRegistryItem<Item, HummingArtifactItem> HUMMING_ARTIFACT_BLOOD = registerSimpleBlockItemSupplier("humming_artifact_blood", () -> new HummingArtifactItem(Sources.BLOOD, new Item.Properties().rarity(Rarity.RARE)));
    public static final IRegistryItem<Item, HummingArtifactItem> HUMMING_ARTIFACT_INFERNAL = registerSimpleBlockItemSupplier("humming_artifact_infernal", () -> new HummingArtifactItem(Sources.INFERNAL, new Item.Properties().rarity(Rarity.RARE)));
    public static final IRegistryItem<Item, HummingArtifactItem> HUMMING_ARTIFACT_VOID = registerSimpleBlockItemSupplier("humming_artifact_void", () -> new HummingArtifactItem(Sources.VOID, new Item.Properties().rarity(Rarity.RARE)));
    public static final IRegistryItem<Item, HummingArtifactItem> HUMMING_ARTIFACT_HALLOWED = registerSimpleBlockItemSupplier("humming_artifact_hallowed", () -> new HummingArtifactItem(Sources.HALLOWED, new Item.Properties().rarity(Rarity.RARE)));

    // Register sanguine core items
    public static final IRegistryItem<Item, Item> SANGUINE_CORE_BLANK = registerSimpleItemSupplier("sanguine_core_blank");
    public static final IRegistryItem<Item, SanguineCoreItem> SANGUINE_CORE_ALLAY = registerSimpleBlockItemSupplier("sanguine_core_allay", () -> new SanguineCoreItem(() -> EntityType.ALLAY, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final IRegistryItem<Item, SanguineCoreItem> SANGUINE_CORE_ARMADILLO = registerSimpleBlockItemSupplier("sanguine_core_armadillo", () -> new SanguineCoreItem(() -> EntityType.ARMADILLO, 2, new Item.Properties().rarity(Rarity.UNCOMMON).durability(31)));
    public static final IRegistryItem<Item, SanguineCoreItem> SANGUINE_CORE_AXOLOTL = registerSimpleBlockItemSupplier("sanguine_core_axolotl", () -> new SanguineCoreItem(() -> EntityType.AXOLOTL, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final IRegistryItem<Item, SanguineCoreItem> SANGUINE_CORE_BAT = registerSimpleBlockItemSupplier("sanguine_core_bat", () -> new SanguineCoreItem(() -> EntityType.BAT, 1, new Item.Properties().rarity(Rarity.UNCOMMON).durability(63)));
    public static final IRegistryItem<Item, SanguineCoreItem> SANGUINE_CORE_BEE = registerSimpleBlockItemSupplier("sanguine_core_bee", () -> new SanguineCoreItem(() -> EntityType.BEE, 2, new Item.Properties().rarity(Rarity.UNCOMMON).durability(31)));
    public static final IRegistryItem<Item, SanguineCoreItem> SANGUINE_CORE_BLAZE = registerSimpleBlockItemSupplier("sanguine_core_blaze", () -> new SanguineCoreItem(() -> EntityType.BLAZE, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final IRegistryItem<Item, SanguineCoreItem> SANGUINE_CORE_BOGGED = registerSimpleBlockItemSupplier("sanguine_core_bogged", () -> new SanguineCoreItem(() -> EntityType.BOGGED, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final IRegistryItem<Item, SanguineCoreItem> SANGUINE_CORE_BREEZE = registerSimpleBlockItemSupplier("sanguine_core_breeze", () -> new SanguineCoreItem(() -> EntityType.BREEZE, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final IRegistryItem<Item, SanguineCoreItem> SANGUINE_CORE_CAMEL = registerSimpleBlockItemSupplier("sanguine_core_camel", () -> new SanguineCoreItem(() -> EntityType.CAMEL, 2, new Item.Properties().rarity(Rarity.UNCOMMON).durability(31)));
    public static final IRegistryItem<Item, SanguineCoreItem> SANGUINE_CORE_CAT = registerSimpleBlockItemSupplier("sanguine_core_cat", () -> new SanguineCoreItem(() -> EntityType.CAT, 1, new Item.Properties().rarity(Rarity.UNCOMMON).durability(63)));
    public static final IRegistryItem<Item, SanguineCoreItem> SANGUINE_CORE_CAVE_SPIDER = registerSimpleBlockItemSupplier("sanguine_core_cave_spider", () -> new SanguineCoreItem(() -> EntityType.CAVE_SPIDER, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final IRegistryItem<Item, SanguineCoreItem> SANGUINE_CORE_CHICKEN = registerSimpleBlockItemSupplier("sanguine_core_chicken", () -> new SanguineCoreItem(() -> EntityType.CHICKEN, 2, new Item.Properties().rarity(Rarity.UNCOMMON).durability(31)));
    public static final IRegistryItem<Item, SanguineCoreItem> SANGUINE_CORE_COD = registerSimpleBlockItemSupplier("sanguine_core_cod", () -> new SanguineCoreItem(() -> EntityType.COD, 2, new Item.Properties().rarity(Rarity.UNCOMMON).durability(31)));
    public static final IRegistryItem<Item, SanguineCoreItem> SANGUINE_CORE_COW = registerSimpleBlockItemSupplier("sanguine_core_cow", () -> new SanguineCoreItem(() -> EntityType.COW, 2, new Item.Properties().rarity(Rarity.UNCOMMON).durability(31)));
    public static final IRegistryItem<Item, SanguineCoreItem> SANGUINE_CORE_CREEPER = registerSimpleBlockItemSupplier("sanguine_core_creeper", () -> new SanguineCoreItem(() -> EntityType.CREEPER, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final IRegistryItem<Item, SanguineCoreItem> SANGUINE_CORE_DOLPHIN = registerSimpleBlockItemSupplier("sanguine_core_dolphin", () -> new SanguineCoreItem(() -> EntityType.DOLPHIN, 2, new Item.Properties().rarity(Rarity.UNCOMMON).durability(31)));
    public static final IRegistryItem<Item, SanguineCoreItem> SANGUINE_CORE_DONKEY = registerSimpleBlockItemSupplier("sanguine_core_donkey", () -> new SanguineCoreItem(() -> EntityType.DONKEY, 2, new Item.Properties().rarity(Rarity.UNCOMMON).durability(31)));
    public static final IRegistryItem<Item, SanguineCoreItem> SANGUINE_CORE_DROWNED = registerSimpleBlockItemSupplier("sanguine_core_drowned", () -> new SanguineCoreItem(() -> EntityType.DROWNED, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final IRegistryItem<Item, SanguineCoreItem> SANGUINE_CORE_ELDER_GUARDIAN = registerSimpleBlockItemSupplier("sanguine_core_elder_guardian", () -> new SanguineCoreItem(() -> EntityType.ELDER_GUARDIAN, 16, new Item.Properties().rarity(Rarity.UNCOMMON).durability(3)));
    public static final IRegistryItem<Item, SanguineCoreItem> SANGUINE_CORE_ENDERMAN = registerSimpleBlockItemSupplier("sanguine_core_enderman", () -> new SanguineCoreItem(() -> EntityType.ENDERMAN, 8, new Item.Properties().rarity(Rarity.UNCOMMON).durability(7)));
    public static final IRegistryItem<Item, SanguineCoreItem> SANGUINE_CORE_ENDERMITE = registerSimpleBlockItemSupplier("sanguine_core_endermite", () -> new SanguineCoreItem(() -> EntityType.ENDERMITE, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final IRegistryItem<Item, SanguineCoreItem> SANGUINE_CORE_EVOKER = registerSimpleBlockItemSupplier("sanguine_core_evoker", () -> new SanguineCoreItem(() -> EntityType.EVOKER, 16, new Item.Properties().rarity(Rarity.UNCOMMON).durability(3)));
    public static final IRegistryItem<Item, SanguineCoreItem> SANGUINE_CORE_FOX = registerSimpleBlockItemSupplier("sanguine_core_fox", () -> new SanguineCoreItem(() -> EntityType.FOX, 2, new Item.Properties().rarity(Rarity.UNCOMMON).durability(31)));
    public static final IRegistryItem<Item, SanguineCoreItem> SANGUINE_CORE_FROG = registerSimpleBlockItemSupplier("sanguine_core_frog", () -> new SanguineCoreItem(() -> EntityType.FROG, 2, new Item.Properties().rarity(Rarity.UNCOMMON).durability(31)));
    public static final IRegistryItem<Item, SanguineCoreItem> SANGUINE_CORE_GHAST = registerSimpleBlockItemSupplier("sanguine_core_ghast", () -> new SanguineCoreItem(() -> EntityType.GHAST, 8, new Item.Properties().rarity(Rarity.UNCOMMON).durability(7)));
    public static final IRegistryItem<Item, SanguineCoreItem> SANGUINE_CORE_GLOW_SQUID = registerSimpleBlockItemSupplier("sanguine_core_glow_squid", () -> new SanguineCoreItem(() -> EntityType.GLOW_SQUID, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final IRegistryItem<Item, SanguineCoreItem> SANGUINE_CORE_GOAT = registerSimpleBlockItemSupplier("sanguine_core_goat", () -> new SanguineCoreItem(() -> EntityType.GOAT, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final IRegistryItem<Item, SanguineCoreItem> SANGUINE_CORE_GUARDIAN = registerSimpleBlockItemSupplier("sanguine_core_guardian", () -> new SanguineCoreItem(() -> EntityType.GUARDIAN, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final IRegistryItem<Item, SanguineCoreItem> SANGUINE_CORE_HOGLIN = registerSimpleBlockItemSupplier("sanguine_core_hoglin", () -> new SanguineCoreItem(() -> EntityType.HOGLIN, 2, new Item.Properties().rarity(Rarity.UNCOMMON).durability(31)));
    public static final IRegistryItem<Item, SanguineCoreItem> SANGUINE_CORE_HORSE = registerSimpleBlockItemSupplier("sanguine_core_horse", () -> new SanguineCoreItem(() -> EntityType.HORSE, 2, new Item.Properties().rarity(Rarity.UNCOMMON).durability(31)));
    public static final IRegistryItem<Item, SanguineCoreItem> SANGUINE_CORE_HUSK = registerSimpleBlockItemSupplier("sanguine_core_husk", () -> new SanguineCoreItem(() -> EntityType.HUSK, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final IRegistryItem<Item, SanguineCoreItem> SANGUINE_CORE_LLAMA = registerSimpleBlockItemSupplier("sanguine_core_llama", () -> new SanguineCoreItem(() -> EntityType.LLAMA, 2, new Item.Properties().rarity(Rarity.UNCOMMON).durability(31)));
    public static final IRegistryItem<Item, SanguineCoreItem> SANGUINE_CORE_MAGMA_CUBE = registerSimpleBlockItemSupplier("sanguine_core_magma_cube", () -> new SanguineCoreItem(() -> EntityType.MAGMA_CUBE, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final IRegistryItem<Item, SanguineCoreItem> SANGUINE_CORE_MOOSHROOM = registerSimpleBlockItemSupplier("sanguine_core_mooshroom", () -> new SanguineCoreItem(() -> EntityType.MOOSHROOM, 2, new Item.Properties().rarity(Rarity.UNCOMMON).durability(31)));
    public static final IRegistryItem<Item, SanguineCoreItem> SANGUINE_CORE_OCELOT = registerSimpleBlockItemSupplier("sanguine_core_ocelot", () -> new SanguineCoreItem(() -> EntityType.OCELOT, 1, new Item.Properties().rarity(Rarity.UNCOMMON).durability(63)));
    public static final IRegistryItem<Item, SanguineCoreItem> SANGUINE_CORE_PANDA = registerSimpleBlockItemSupplier("sanguine_core_panda", () -> new SanguineCoreItem(() -> EntityType.PANDA, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final IRegistryItem<Item, SanguineCoreItem> SANGUINE_CORE_PARROT = registerSimpleBlockItemSupplier("sanguine_core_parrot", () -> new SanguineCoreItem(() -> EntityType.PARROT, 1, new Item.Properties().rarity(Rarity.UNCOMMON).durability(63)));
    public static final IRegistryItem<Item, SanguineCoreItem> SANGUINE_CORE_PHANTOM = registerSimpleBlockItemSupplier("sanguine_core_phantom", () -> new SanguineCoreItem(() -> EntityType.PHANTOM, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final IRegistryItem<Item, SanguineCoreItem> SANGUINE_CORE_PIG = registerSimpleBlockItemSupplier("sanguine_core_pig", () -> new SanguineCoreItem(() -> EntityType.PIG, 2, new Item.Properties().rarity(Rarity.UNCOMMON).durability(31)));
    public static final IRegistryItem<Item, SanguineCoreItem> SANGUINE_CORE_PIGLIN = registerSimpleBlockItemSupplier("sanguine_core_piglin", () -> new SanguineCoreItem(() -> EntityType.PIGLIN, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final IRegistryItem<Item, SanguineCoreItem> SANGUINE_CORE_PIGLIN_BRUTE = registerSimpleBlockItemSupplier("sanguine_core_piglin_brute", () -> new SanguineCoreItem(() -> EntityType.PIGLIN_BRUTE, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final IRegistryItem<Item, SanguineCoreItem> SANGUINE_CORE_PILLAGER = registerSimpleBlockItemSupplier("sanguine_core_pillager", () -> new SanguineCoreItem(() -> EntityType.PILLAGER, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final IRegistryItem<Item, SanguineCoreItem> SANGUINE_CORE_POLAR_BEAR = registerSimpleBlockItemSupplier("sanguine_core_polar_bear", () -> new SanguineCoreItem(() -> EntityType.POLAR_BEAR, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final IRegistryItem<Item, SanguineCoreItem> SANGUINE_CORE_PUFFERFISH = registerSimpleBlockItemSupplier("sanguine_core_pufferfish", () -> new SanguineCoreItem(() -> EntityType.PUFFERFISH, 2, new Item.Properties().rarity(Rarity.UNCOMMON).durability(31)));
    public static final IRegistryItem<Item, SanguineCoreItem> SANGUINE_CORE_RABBIT = registerSimpleBlockItemSupplier("sanguine_core_rabbit", () -> new SanguineCoreItem(() -> EntityType.RABBIT, 2, new Item.Properties().rarity(Rarity.UNCOMMON).durability(31)));
    public static final IRegistryItem<Item, SanguineCoreItem> SANGUINE_CORE_RAVAGER = registerSimpleBlockItemSupplier("sanguine_core_ravager", () -> new SanguineCoreItem(() -> EntityType.RAVAGER, 8, new Item.Properties().rarity(Rarity.UNCOMMON).durability(7)));
    public static final IRegistryItem<Item, SanguineCoreItem> SANGUINE_CORE_SALMON = registerSimpleBlockItemSupplier("sanguine_core_salmon", () -> new SanguineCoreItem(() -> EntityType.SALMON, 2, new Item.Properties().rarity(Rarity.UNCOMMON).durability(31)));
    public static final IRegistryItem<Item, SanguineCoreItem> SANGUINE_CORE_SHEEP = registerSimpleBlockItemSupplier("sanguine_core_sheep", () -> new SanguineCoreItem(() -> EntityType.SHEEP, 2, new Item.Properties().rarity(Rarity.UNCOMMON).durability(31)));
    public static final IRegistryItem<Item, SanguineCoreItem> SANGUINE_CORE_SHULKER = registerSimpleBlockItemSupplier("sanguine_core_shulker", () -> new SanguineCoreItem(() -> EntityType.SHULKER, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final IRegistryItem<Item, SanguineCoreItem> SANGUINE_CORE_SILVERFISH = registerSimpleBlockItemSupplier("sanguine_core_silverfish", () -> new SanguineCoreItem(() -> EntityType.SILVERFISH, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final IRegistryItem<Item, SanguineCoreItem> SANGUINE_CORE_SKELETON = registerSimpleBlockItemSupplier("sanguine_core_skeleton", () -> new SanguineCoreItem(() -> EntityType.SKELETON, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final IRegistryItem<Item, SanguineCoreItem> SANGUINE_CORE_SKELETON_HORSE = registerSimpleBlockItemSupplier("sanguine_core_skeleton_horse", () -> new SanguineCoreItem(() -> EntityType.SKELETON_HORSE, 2, new Item.Properties().rarity(Rarity.UNCOMMON).durability(31)));
    public static final IRegistryItem<Item, SanguineCoreItem> SANGUINE_CORE_SLIME = registerSimpleBlockItemSupplier("sanguine_core_slime", () -> new SanguineCoreItem(() -> EntityType.SLIME, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final IRegistryItem<Item, SanguineCoreItem> SANGUINE_CORE_SNIFFER = registerSimpleBlockItemSupplier("sanguine_core_sniffer", () -> new SanguineCoreItem(() -> EntityType.SNIFFER, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final IRegistryItem<Item, SanguineCoreItem> SANGUINE_CORE_SPIDER = registerSimpleBlockItemSupplier("sanguine_core_spider", () -> new SanguineCoreItem(() -> EntityType.SPIDER, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final IRegistryItem<Item, SanguineCoreItem> SANGUINE_CORE_SQUID = registerSimpleBlockItemSupplier("sanguine_core_squid", () -> new SanguineCoreItem(() -> EntityType.SQUID, 2, new Item.Properties().rarity(Rarity.UNCOMMON).durability(31)));
    public static final IRegistryItem<Item, SanguineCoreItem> SANGUINE_CORE_STRAY = registerSimpleBlockItemSupplier("sanguine_core_stray", () -> new SanguineCoreItem(() -> EntityType.STRAY, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final IRegistryItem<Item, SanguineCoreItem> SANGUINE_CORE_STRIDER = registerSimpleBlockItemSupplier("sanguine_core_strider", () -> new SanguineCoreItem(() -> EntityType.STRIDER, 2, new Item.Properties().rarity(Rarity.UNCOMMON).durability(31)));
    public static final IRegistryItem<Item, SanguineCoreItem> SANGUINE_CORE_TROPICAL_FISH = registerSimpleBlockItemSupplier("sanguine_core_tropical_fish", () -> new SanguineCoreItem(() -> EntityType.TROPICAL_FISH, 2, new Item.Properties().rarity(Rarity.UNCOMMON).durability(31)));
    public static final IRegistryItem<Item, SanguineCoreItem> SANGUINE_CORE_TURTLE = registerSimpleBlockItemSupplier("sanguine_core_turtle", () -> new SanguineCoreItem(() -> EntityType.TURTLE, 2, new Item.Properties().rarity(Rarity.UNCOMMON).durability(31)));
    public static final IRegistryItem<Item, SanguineCoreItem> SANGUINE_CORE_VEX = registerSimpleBlockItemSupplier("sanguine_core_vex", () -> new SanguineCoreItem(() -> EntityType.VEX, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final IRegistryItem<Item, SanguineCoreItem> SANGUINE_CORE_VILLAGER = registerSimpleBlockItemSupplier("sanguine_core_villager", () -> new SanguineCoreItem(() -> EntityType.VILLAGER, 8, new Item.Properties().rarity(Rarity.UNCOMMON).durability(7)));
    public static final IRegistryItem<Item, SanguineCoreItem> SANGUINE_CORE_VINDICATOR = registerSimpleBlockItemSupplier("sanguine_core_vindicator", () -> new SanguineCoreItem(() -> EntityType.VINDICATOR, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final IRegistryItem<Item, SanguineCoreItem> SANGUINE_CORE_WITCH = registerSimpleBlockItemSupplier("sanguine_core_witch", () -> new SanguineCoreItem(() -> EntityType.WITCH, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final IRegistryItem<Item, SanguineCoreItem> SANGUINE_CORE_WITHER_SKELETON = registerSimpleBlockItemSupplier("sanguine_core_wither_skeleton", () -> new SanguineCoreItem(() -> EntityType.WITHER_SKELETON, 8, new Item.Properties().rarity(Rarity.UNCOMMON).durability(7)));
    public static final IRegistryItem<Item, SanguineCoreItem> SANGUINE_CORE_WOLF = registerSimpleBlockItemSupplier("sanguine_core_wolf", () -> new SanguineCoreItem(() -> EntityType.WOLF, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final IRegistryItem<Item, SanguineCoreItem> SANGUINE_CORE_ZOGLIN = registerSimpleBlockItemSupplier("sanguine_core_zoglin", () -> new SanguineCoreItem(() -> EntityType.ZOGLIN, 2, new Item.Properties().rarity(Rarity.UNCOMMON).durability(31)));
    public static final IRegistryItem<Item, SanguineCoreItem> SANGUINE_CORE_ZOMBIE = registerSimpleBlockItemSupplier("sanguine_core_zombie", () -> new SanguineCoreItem(() -> EntityType.ZOMBIE, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final IRegistryItem<Item, SanguineCoreItem> SANGUINE_CORE_ZOMBIE_HORSE = registerSimpleBlockItemSupplier("sanguine_core_zombie_horse", () -> new SanguineCoreItem(() -> EntityType.ZOMBIE_HORSE, 2, new Item.Properties().rarity(Rarity.UNCOMMON).durability(31)));
    public static final IRegistryItem<Item, SanguineCoreItem> SANGUINE_CORE_ZOMBIE_VILLAGER = registerSimpleBlockItemSupplier("sanguine_core_zombie_villager", () -> new SanguineCoreItem(() -> EntityType.ZOMBIE_VILLAGER, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final IRegistryItem<Item, SanguineCoreItem> SANGUINE_CORE_ZOMBIFIED_PIGLIN = registerSimpleBlockItemSupplier("sanguine_core_zombified_piglin", () -> new SanguineCoreItem(() -> EntityType.ZOMBIFIED_PIGLIN, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final IRegistryItem<Item, SanguineCoreItem> SANGUINE_CORE_TREEFOLK = registerSimpleBlockItemSupplier("sanguine_core_treefolk", () -> new SanguineCoreItem(EntityTypesPM.TREEFOLK::get, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final IRegistryItem<Item, SanguineCoreItem> SANGUINE_CORE_INNER_DEMON = registerSimpleBlockItemSupplier("sanguine_core_inner_demon", () -> new SanguineCoreItem(EntityTypesPM.INNER_DEMON::get, 64, new Item.Properties().rarity(Rarity.UNCOMMON).durability(0).stacksTo(1)));

    // Register concoction items
    public static final IRegistryItem<Item, SkyglassFlaskItem> SKYGLASS_FLASK = registerSimpleBlockItemSupplier("skyglass_flask", () -> new SkyglassFlaskItem(new Item.Properties()));
    public static final IRegistryItem<Item, ConcoctionItem> CONCOCTION = registerCustom("concoction", ConcoctionItem::registerCreativeTabItems, () -> new ConcoctionItem(new Item.Properties().craftRemainder(SKYGLASS_FLASK.get()).usingConvertsTo(SKYGLASS_FLASK.get()).stacksTo(1)));
    public static final IRegistryItem<Item, BombCasingItem> BOMB_CASING = registerSimpleBlockItemSupplier("bomb_casing", () -> new BombCasingItem(new Item.Properties()));
    public static final IRegistryItem<Item, AlchemicalBombItem> ALCHEMICAL_BOMB = registerCustom("alchemical_bomb", AlchemicalBombItem::registerCreativeTabItems, () -> new AlchemicalBombItem(new Item.Properties().stacksTo(1)));

    // Register caster/wand items
    public static final IRegistryItem<Item, Item> SPELL_SCROLL_BLANK = registerSimpleItemSupplier("spell_scroll_blank");
    public static final IRegistryItem<Item, SpellScrollItem> SPELL_SCROLL_FILLED = registerSimpleBlockItemWithoutTab("spell_scroll_filled", SpellScrollItem::new);
    public static final IRegistryItem<Item, MundaneWandItem> MUNDANE_WAND = registerSupplier("mundane_wand", Services.ITEM_PROTOTYPES.mundaneWand());
    public static final IRegistryItem<Item, ModularWandItem> MODULAR_WAND = registerCustom("modular_wand", ModularWandItem::registerCreativeTabItems, Services.ITEM_PROTOTYPES.modularWand(new Item.Properties().stacksTo(1)));
    public static final IRegistryItem<Item, ModularStaffItem> MODULAR_STAFF = registerCustom("modular_staff", ModularWandItem::registerCreativeTabItems, Services.ITEM_PROTOTYPES.modularStaff(new Item.Properties().stacksTo(1).attributes(ModularStaffItem.createAttributes(5, -2.4)).component(DataComponents.TOOL, ModularStaffItem.createToolProperties())));
    public static final IRegistryItem<Item, WandCoreItem> HEARTWOOD_WAND_CORE_ITEM = registerSimpleBlockItemSupplier("heartwood_wand_core_item", () -> new WandCoreItem(WandCore.HEARTWOOD, new Item.Properties()));
    public static final IRegistryItem<Item, WandCoreItem> OBSIDIAN_WAND_CORE_ITEM = registerSimpleBlockItemSupplier("obsidian_wand_core_item", () -> new WandCoreItem(WandCore.OBSIDIAN, new Item.Properties()));
    public static final IRegistryItem<Item, WandCoreItem> CORAL_WAND_CORE_ITEM = registerSimpleBlockItemSupplier("coral_wand_core_item", () -> new WandCoreItem(WandCore.CORAL, new Item.Properties()));
    public static final IRegistryItem<Item, WandCoreItem> BAMBOO_WAND_CORE_ITEM = registerSimpleBlockItemSupplier("bamboo_wand_core_item", () -> new WandCoreItem(WandCore.BAMBOO, new Item.Properties()));
    public static final IRegistryItem<Item, WandCoreItem> SUNWOOD_WAND_CORE_ITEM = registerSimpleBlockItemSupplier("sunwood_wand_core_item", () -> new WandCoreItem(WandCore.SUNWOOD, new Item.Properties()));
    public static final IRegistryItem<Item, WandCoreItem> MOONWOOD_WAND_CORE_ITEM = registerSimpleBlockItemSupplier("moonwood_wand_core_item", () -> new WandCoreItem(WandCore.MOONWOOD, new Item.Properties()));
    public static final IRegistryItem<Item, WandCoreItem> BONE_WAND_CORE_ITEM = registerSimpleBlockItemSupplier("bone_wand_core_item", () -> new WandCoreItem(WandCore.BONE, new Item.Properties()));
    public static final IRegistryItem<Item, WandCoreItem> BLAZE_ROD_WAND_CORE_ITEM = registerSimpleBlockItemSupplier("blaze_rod_wand_core_item", () -> new WandCoreItem(WandCore.BLAZE_ROD, new Item.Properties()));
    public static final IRegistryItem<Item, WandCoreItem> PURPUR_WAND_CORE_ITEM = registerSimpleBlockItemSupplier("purpur_wand_core_item", () -> new WandCoreItem(WandCore.PURPUR, new Item.Properties()));
    public static final IRegistryItem<Item, WandCoreItem> PRIMAL_WAND_CORE_ITEM = registerSimpleBlockItemSupplier("primal_wand_core_item", () -> new WandCoreItem(WandCore.PRIMAL, new Item.Properties()));
    public static final IRegistryItem<Item, WandCoreItem> DARK_PRIMAL_WAND_CORE_ITEM = registerSimpleBlockItemSupplier("dark_primal_wand_core_item", () -> new WandCoreItem(WandCore.DARK_PRIMAL, new Item.Properties()));
    public static final IRegistryItem<Item, WandCoreItem> PURE_PRIMAL_WAND_CORE_ITEM = registerSimpleBlockItemSupplier("pure_primal_wand_core_item", () -> new WandCoreItem(WandCore.PURE_PRIMAL, new Item.Properties()));
    public static final IRegistryItem<Item, WandCapItem> IRON_WAND_CAP_ITEM = registerSimpleBlockItemSupplier("iron_wand_cap_item", () -> new WandCapItem(WandCap.IRON, new Item.Properties()));
    public static final IRegistryItem<Item, WandCapItem> GOLD_WAND_CAP_ITEM = registerSimpleBlockItemSupplier("gold_wand_cap_item", () -> new WandCapItem(WandCap.GOLD, new Item.Properties()));
    public static final IRegistryItem<Item, WandCapItem> PRIMALITE_WAND_CAP_ITEM = registerSimpleBlockItemSupplier("primalite_wand_cap_item", () -> new WandCapItem(WandCap.PRIMALITE, new Item.Properties()));
    public static final IRegistryItem<Item, WandCapItem> HEXIUM_WAND_CAP_ITEM = registerSimpleBlockItemSupplier("hexium_wand_cap_item", () -> new WandCapItem(WandCap.HEXIUM, new Item.Properties()));
    public static final IRegistryItem<Item, WandCapItem> HALLOWSTEEL_WAND_CAP_ITEM = registerSimpleBlockItemSupplier("hallowsteel_wand_cap_item", () -> new WandCapItem(WandCap.HALLOWSTEEL, new Item.Properties()));
    public static final IRegistryItem<Item, WandGemItem> APPRENTICE_WAND_GEM_ITEM = registerSimpleBlockItemSupplier("apprentice_wand_gem_item", () -> new WandGemItem(WandGem.APPRENTICE, new Item.Properties()));
    public static final IRegistryItem<Item, WandGemItem> ADEPT_WAND_GEM_ITEM = registerSimpleBlockItemSupplier("adept_wand_gem_item", () -> new WandGemItem(WandGem.ADEPT, new Item.Properties()));
    public static final IRegistryItem<Item, WandGemItem> WIZARD_WAND_GEM_ITEM = registerSimpleBlockItemSupplier("wizard_wand_gem_item", () -> new WandGemItem(WandGem.WIZARD, new Item.Properties()));
    public static final IRegistryItem<Item, WandGemItem> ARCHMAGE_WAND_GEM_ITEM = registerSimpleBlockItemSupplier("archmage_wand_gem_item", () -> new WandGemItem(WandGem.ARCHMAGE, new Item.Properties()));
    public static final IRegistryItem<Item, WandGemItem> CREATIVE_WAND_GEM_ITEM = registerSimpleBlockItemSupplier("creative_wand_gem_item", () -> new WandGemItem(WandGem.CREATIVE, new Item.Properties()));
    public static final IRegistryItem<Item, StaffCoreItem> HEARTWOOD_STAFF_CORE_ITEM = registerSimpleBlockItemSupplier("heartwood_staff_core_item", () -> new StaffCoreItem(WandCore.HEARTWOOD, new Item.Properties()));
    public static final IRegistryItem<Item, StaffCoreItem> OBSIDIAN_STAFF_CORE_ITEM = registerSimpleBlockItemSupplier("obsidian_staff_core_item", () -> new StaffCoreItem(WandCore.OBSIDIAN, new Item.Properties()));
    public static final IRegistryItem<Item, StaffCoreItem> CORAL_STAFF_CORE_ITEM = registerSimpleBlockItemSupplier("coral_staff_core_item", () -> new StaffCoreItem(WandCore.CORAL, new Item.Properties()));
    public static final IRegistryItem<Item, StaffCoreItem> BAMBOO_STAFF_CORE_ITEM = registerSimpleBlockItemSupplier("bamboo_staff_core_item", () -> new StaffCoreItem(WandCore.BAMBOO, new Item.Properties()));
    public static final IRegistryItem<Item, StaffCoreItem> SUNWOOD_STAFF_CORE_ITEM = registerSimpleBlockItemSupplier("sunwood_staff_core_item", () -> new StaffCoreItem(WandCore.SUNWOOD, new Item.Properties()));
    public static final IRegistryItem<Item, StaffCoreItem> MOONWOOD_STAFF_CORE_ITEM = registerSimpleBlockItemSupplier("moonwood_staff_core_item", () -> new StaffCoreItem(WandCore.MOONWOOD, new Item.Properties()));
    public static final IRegistryItem<Item, StaffCoreItem> BONE_STAFF_CORE_ITEM = registerSimpleBlockItemSupplier("bone_staff_core_item", () -> new StaffCoreItem(WandCore.BONE, new Item.Properties()));
    public static final IRegistryItem<Item, StaffCoreItem> BLAZE_ROD_STAFF_CORE_ITEM = registerSimpleBlockItemSupplier("blaze_rod_staff_core_item", () -> new StaffCoreItem(WandCore.BLAZE_ROD, new Item.Properties()));
    public static final IRegistryItem<Item, StaffCoreItem> PURPUR_STAFF_CORE_ITEM = registerSimpleBlockItemSupplier("purpur_staff_core_item", () -> new StaffCoreItem(WandCore.PURPUR, new Item.Properties()));
    public static final IRegistryItem<Item, StaffCoreItem> PRIMAL_STAFF_CORE_ITEM = registerSimpleBlockItemSupplier("primal_staff_core_item", () -> new StaffCoreItem(WandCore.PRIMAL, new Item.Properties()));
    public static final IRegistryItem<Item, StaffCoreItem> DARK_PRIMAL_STAFF_CORE_ITEM = registerSimpleBlockItemSupplier("dark_primal_staff_core_item", () -> new StaffCoreItem(WandCore.DARK_PRIMAL, new Item.Properties()));
    public static final IRegistryItem<Item, StaffCoreItem> PURE_PRIMAL_STAFF_CORE_ITEM = registerSimpleBlockItemSupplier("pure_primal_staff_core_item", () -> new StaffCoreItem(WandCore.PURE_PRIMAL, new Item.Properties()));

    // Register spawn egg items
    public static final IRegistryItem<Item, SpawnEggItem> TREEFOLK_SPAWN_EGG = registerSimpleBlockItemSupplier("treefolk_spawn_egg", () -> new SpawnEggItem(new Item.Properties().spawnEgg(EntityTypesPM.TREEFOLK.get())));
    public static final IRegistryItem<Item, SpawnEggItem> PRIMALITE_GOLEM_SPAWN_EGG = registerSimpleBlockItemSupplier("primalite_golem_spawn_egg", () -> new SpawnEggItem(new Item.Properties().spawnEgg(EntityTypesPM.PRIMALITE_GOLEM.get())));
    public static final IRegistryItem<Item, SpawnEggItem> HEXIUM_GOLEM_SPAWN_EGG = registerSimpleBlockItemSupplier("hexium_golem_spawn_egg", () -> new SpawnEggItem(new Item.Properties().spawnEgg(EntityTypesPM.HEXIUM_GOLEM.get())));
    public static final IRegistryItem<Item, SpawnEggItem> HALLOWSTEEL_GOLEM_SPAWN_EGG = registerSimpleBlockItemSupplier("hallowsteel_golem_spawn_egg", () -> new SpawnEggItem(new Item.Properties().spawnEgg(EntityTypesPM.HALLOWSTEEL_GOLEM.get())));
    public static final IRegistryItem<Item, SpawnEggItem> BASIC_EARTH_PIXIE = registerSimpleBlockItemSupplier("pixie_basic_earth", () -> new PixieItem(EntityTypesPM.BASIC_EARTH_PIXIE.get(), PixieRank.BASIC, Sources.EARTH, new Item.Properties().stacksTo(16).rarity(Rarity.COMMON)));
    public static final IRegistryItem<Item, SpawnEggItem> GRAND_EARTH_PIXIE = registerSimpleBlockItemSupplier("pixie_grand_earth", () -> new PixieItem(EntityTypesPM.GRAND_EARTH_PIXIE.get(), PixieRank.GRAND, Sources.EARTH, new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON)));
    public static final IRegistryItem<Item, SpawnEggItem> MAJESTIC_EARTH_PIXIE = registerSimpleBlockItemSupplier("pixie_majestic_earth", () -> new PixieItem(EntityTypesPM.MAJESTIC_EARTH_PIXIE.get(), PixieRank.MAJESTIC, Sources.EARTH, new Item.Properties().stacksTo(16).rarity(Rarity.RARE)));
    public static final IRegistryItem<Item, SpawnEggItem> BASIC_SEA_PIXIE = registerSimpleBlockItemSupplier("pixie_basic_sea", () -> new PixieItem(EntityTypesPM.BASIC_SEA_PIXIE.get(), PixieRank.BASIC, Sources.SEA, new Item.Properties().stacksTo(16).rarity(Rarity.COMMON)));
    public static final IRegistryItem<Item, SpawnEggItem> GRAND_SEA_PIXIE = registerSimpleBlockItemSupplier("pixie_grand_sea", () -> new PixieItem(EntityTypesPM.GRAND_SEA_PIXIE.get(), PixieRank.GRAND, Sources.SEA, new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON)));
    public static final IRegistryItem<Item, SpawnEggItem> MAJESTIC_SEA_PIXIE = registerSimpleBlockItemSupplier("pixie_majestic_sea", () -> new PixieItem(EntityTypesPM.MAJESTIC_SEA_PIXIE.get(), PixieRank.MAJESTIC, Sources.SEA, new Item.Properties().stacksTo(16).rarity(Rarity.RARE)));
    public static final IRegistryItem<Item, SpawnEggItem> BASIC_SKY_PIXIE = registerSimpleBlockItemSupplier("pixie_basic_sky", () -> new PixieItem(EntityTypesPM.BASIC_SKY_PIXIE.get(), PixieRank.BASIC, Sources.SKY, new Item.Properties().stacksTo(16).rarity(Rarity.COMMON)));
    public static final IRegistryItem<Item, SpawnEggItem> GRAND_SKY_PIXIE = registerSimpleBlockItemSupplier("pixie_grand_sky", () -> new PixieItem(EntityTypesPM.GRAND_SKY_PIXIE.get(), PixieRank.GRAND, Sources.SKY, new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON)));
    public static final IRegistryItem<Item, SpawnEggItem> MAJESTIC_SKY_PIXIE = registerSimpleBlockItemSupplier("pixie_majestic_sky", () -> new PixieItem(EntityTypesPM.MAJESTIC_SKY_PIXIE.get(), PixieRank.MAJESTIC, Sources.SKY, new Item.Properties().stacksTo(16).rarity(Rarity.RARE)));
    public static final IRegistryItem<Item, SpawnEggItem> BASIC_SUN_PIXIE = registerSimpleBlockItemSupplier("pixie_basic_sun", () -> new PixieItem(EntityTypesPM.BASIC_SUN_PIXIE.get(), PixieRank.BASIC, Sources.SUN, new Item.Properties().stacksTo(16).rarity(Rarity.COMMON)));
    public static final IRegistryItem<Item, SpawnEggItem> GRAND_SUN_PIXIE = registerSimpleBlockItemSupplier("pixie_grand_sun", () -> new PixieItem(EntityTypesPM.GRAND_SUN_PIXIE.get(), PixieRank.GRAND, Sources.SUN, new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON)));
    public static final IRegistryItem<Item, SpawnEggItem> MAJESTIC_SUN_PIXIE = registerSimpleBlockItemSupplier("pixie_majestic_sun", () -> new PixieItem(EntityTypesPM.MAJESTIC_SUN_PIXIE.get(), PixieRank.MAJESTIC, Sources.SUN, new Item.Properties().stacksTo(16).rarity(Rarity.RARE)));
    public static final IRegistryItem<Item, SpawnEggItem> BASIC_MOON_PIXIE = registerSimpleBlockItemSupplier("pixie_basic_moon", () -> new PixieItem(EntityTypesPM.BASIC_MOON_PIXIE.get(), PixieRank.BASIC, Sources.MOON, new Item.Properties().stacksTo(16).rarity(Rarity.COMMON)));
    public static final IRegistryItem<Item, SpawnEggItem> GRAND_MOON_PIXIE = registerSimpleBlockItemSupplier("pixie_grand_moon", () -> new PixieItem(EntityTypesPM.GRAND_MOON_PIXIE.get(), PixieRank.GRAND, Sources.MOON, new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON)));
    public static final IRegistryItem<Item, SpawnEggItem> MAJESTIC_MOON_PIXIE = registerSimpleBlockItemSupplier("pixie_majestic_moon", () -> new PixieItem(EntityTypesPM.MAJESTIC_MOON_PIXIE.get(), PixieRank.MAJESTIC, Sources.MOON, new Item.Properties().stacksTo(16).rarity(Rarity.RARE)));
    public static final IRegistryItem<Item, SpawnEggItem> BASIC_BLOOD_PIXIE = registerSimpleBlockItemSupplier("pixie_basic_blood", () -> new PixieItem(EntityTypesPM.BASIC_BLOOD_PIXIE.get(), PixieRank.BASIC, Sources.BLOOD, new Item.Properties().stacksTo(16).rarity(Rarity.COMMON)));
    public static final IRegistryItem<Item, SpawnEggItem> GRAND_BLOOD_PIXIE = registerSimpleBlockItemSupplier("pixie_grand_blood", () -> new PixieItem(EntityTypesPM.GRAND_BLOOD_PIXIE.get(), PixieRank.GRAND, Sources.BLOOD, new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON)));
    public static final IRegistryItem<Item, SpawnEggItem> MAJESTIC_BLOOD_PIXIE = registerSimpleBlockItemSupplier("pixie_majestic_blood", () -> new PixieItem(EntityTypesPM.MAJESTIC_BLOOD_PIXIE.get(), PixieRank.MAJESTIC, Sources.BLOOD, new Item.Properties().stacksTo(16).rarity(Rarity.RARE)));
    public static final IRegistryItem<Item, SpawnEggItem> BASIC_INFERNAL_PIXIE = registerSimpleBlockItemSupplier("pixie_basic_infernal", () -> new PixieItem(EntityTypesPM.BASIC_INFERNAL_PIXIE.get(), PixieRank.BASIC, Sources.INFERNAL, new Item.Properties().stacksTo(16).rarity(Rarity.COMMON)));
    public static final IRegistryItem<Item, SpawnEggItem> GRAND_INFERNAL_PIXIE = registerSimpleBlockItemSupplier("pixie_grand_infernal", () -> new PixieItem(EntityTypesPM.GRAND_INFERNAL_PIXIE.get(), PixieRank.GRAND, Sources.INFERNAL, new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON)));
    public static final IRegistryItem<Item, SpawnEggItem> MAJESTIC_INFERNAL_PIXIE = registerSimpleBlockItemSupplier("pixie_majestic_infernal", () -> new PixieItem(EntityTypesPM.MAJESTIC_INFERNAL_PIXIE.get(), PixieRank.MAJESTIC, Sources.INFERNAL, new Item.Properties().stacksTo(16).rarity(Rarity.RARE)));
    public static final IRegistryItem<Item, SpawnEggItem> BASIC_VOID_PIXIE = registerSimpleBlockItemSupplier("pixie_basic_void", () -> new PixieItem(EntityTypesPM.BASIC_VOID_PIXIE.get(), PixieRank.BASIC, Sources.VOID, new Item.Properties().stacksTo(16).rarity(Rarity.COMMON)));
    public static final IRegistryItem<Item, SpawnEggItem> GRAND_VOID_PIXIE = registerSimpleBlockItemSupplier("pixie_grand_void", () -> new PixieItem(EntityTypesPM.GRAND_VOID_PIXIE.get(), PixieRank.GRAND, Sources.VOID, new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON)));
    public static final IRegistryItem<Item, SpawnEggItem> MAJESTIC_VOID_PIXIE = registerSimpleBlockItemSupplier("pixie_majestic_void", () -> new PixieItem(EntityTypesPM.MAJESTIC_VOID_PIXIE.get(), PixieRank.MAJESTIC, Sources.VOID, new Item.Properties().stacksTo(16).rarity(Rarity.RARE)));
    public static final IRegistryItem<Item, SpawnEggItem> BASIC_HALLOWED_PIXIE = registerSimpleBlockItemSupplier("pixie_basic_hallowed", () -> new PixieItem(EntityTypesPM.BASIC_HALLOWED_PIXIE.get(), PixieRank.BASIC, Sources.HALLOWED, new Item.Properties().stacksTo(16).rarity(Rarity.COMMON)));
    public static final IRegistryItem<Item, SpawnEggItem> GRAND_HALLOWED_PIXIE = registerSimpleBlockItemSupplier("pixie_grand_hallowed", () -> new PixieItem(EntityTypesPM.GRAND_HALLOWED_PIXIE.get(), PixieRank.GRAND, Sources.HALLOWED, new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON)));
    public static final IRegistryItem<Item, SpawnEggItem> MAJESTIC_HALLOWED_PIXIE = registerSimpleBlockItemSupplier("pixie_majestic_hallowed", () -> new PixieItem(EntityTypesPM.MAJESTIC_HALLOWED_PIXIE.get(), PixieRank.MAJESTIC, Sources.HALLOWED, new Item.Properties().stacksTo(16).rarity(Rarity.RARE)));

    // Register drained pixie items
    public static final IRegistryItem<Item, DrainedPixieItem> DRAINED_BASIC_EARTH_PIXIE = registerSimpleBlockItemWithoutTab("drained_pixie_basic_earth", () -> new DrainedPixieItem(PixieRank.BASIC, Sources.EARTH, new Item.Properties().stacksTo(16).rarity(Rarity.COMMON)));
    public static final IRegistryItem<Item, DrainedPixieItem> DRAINED_GRAND_EARTH_PIXIE = registerSimpleBlockItemWithoutTab("drained_pixie_grand_earth", () -> new DrainedPixieItem(PixieRank.GRAND, Sources.EARTH, new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON)));
    public static final IRegistryItem<Item, DrainedPixieItem> DRAINED_MAJESTIC_EARTH_PIXIE = registerSimpleBlockItemWithoutTab("drained_pixie_majestic_earth", () -> new DrainedPixieItem(PixieRank.MAJESTIC, Sources.EARTH, new Item.Properties().stacksTo(16).rarity(Rarity.RARE)));
    public static final IRegistryItem<Item, DrainedPixieItem> DRAINED_BASIC_SEA_PIXIE = registerSimpleBlockItemWithoutTab("drained_pixie_basic_sea", () -> new DrainedPixieItem(PixieRank.BASIC, Sources.SEA, new Item.Properties().stacksTo(16).rarity(Rarity.COMMON)));
    public static final IRegistryItem<Item, DrainedPixieItem> DRAINED_GRAND_SEA_PIXIE = registerSimpleBlockItemWithoutTab("drained_pixie_grand_sea", () -> new DrainedPixieItem(PixieRank.GRAND, Sources.SEA, new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON)));
    public static final IRegistryItem<Item, DrainedPixieItem> DRAINED_MAJESTIC_SEA_PIXIE = registerSimpleBlockItemWithoutTab("drained_pixie_majestic_sea", () -> new DrainedPixieItem(PixieRank.MAJESTIC, Sources.SEA, new Item.Properties().stacksTo(16).rarity(Rarity.RARE)));
    public static final IRegistryItem<Item, DrainedPixieItem> DRAINED_BASIC_SKY_PIXIE = registerSimpleBlockItemWithoutTab("drained_pixie_basic_sky", () -> new DrainedPixieItem(PixieRank.BASIC, Sources.SKY, new Item.Properties().stacksTo(16).rarity(Rarity.COMMON)));
    public static final IRegistryItem<Item, DrainedPixieItem> DRAINED_GRAND_SKY_PIXIE = registerSimpleBlockItemWithoutTab("drained_pixie_grand_sky", () -> new DrainedPixieItem(PixieRank.GRAND, Sources.SKY, new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON)));
    public static final IRegistryItem<Item, DrainedPixieItem> DRAINED_MAJESTIC_SKY_PIXIE = registerSimpleBlockItemWithoutTab("drained_pixie_majestic_sky", () -> new DrainedPixieItem(PixieRank.MAJESTIC, Sources.SKY, new Item.Properties().stacksTo(16).rarity(Rarity.RARE)));
    public static final IRegistryItem<Item, DrainedPixieItem> DRAINED_BASIC_SUN_PIXIE = registerSimpleBlockItemWithoutTab("drained_pixie_basic_sun", () -> new DrainedPixieItem(PixieRank.BASIC, Sources.SUN, new Item.Properties().stacksTo(16).rarity(Rarity.COMMON)));
    public static final IRegistryItem<Item, DrainedPixieItem> DRAINED_GRAND_SUN_PIXIE = registerSimpleBlockItemWithoutTab("drained_pixie_grand_sun", () -> new DrainedPixieItem(PixieRank.GRAND, Sources.SUN, new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON)));
    public static final IRegistryItem<Item, DrainedPixieItem> DRAINED_MAJESTIC_SUN_PIXIE = registerSimpleBlockItemWithoutTab("drained_pixie_majestic_sun", () -> new DrainedPixieItem(PixieRank.MAJESTIC, Sources.SUN, new Item.Properties().stacksTo(16).rarity(Rarity.RARE)));
    public static final IRegistryItem<Item, DrainedPixieItem> DRAINED_BASIC_MOON_PIXIE = registerSimpleBlockItemWithoutTab("drained_pixie_basic_moon", () -> new DrainedPixieItem(PixieRank.BASIC, Sources.MOON, new Item.Properties().stacksTo(16).rarity(Rarity.COMMON)));
    public static final IRegistryItem<Item, DrainedPixieItem> DRAINED_GRAND_MOON_PIXIE = registerSimpleBlockItemWithoutTab("drained_pixie_grand_moon", () -> new DrainedPixieItem(PixieRank.GRAND, Sources.MOON, new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON)));
    public static final IRegistryItem<Item, DrainedPixieItem> DRAINED_MAJESTIC_MOON_PIXIE = registerSimpleBlockItemWithoutTab("drained_pixie_majestic_moon", () -> new DrainedPixieItem(PixieRank.MAJESTIC, Sources.MOON, new Item.Properties().stacksTo(16).rarity(Rarity.RARE)));
    public static final IRegistryItem<Item, DrainedPixieItem> DRAINED_BASIC_BLOOD_PIXIE = registerSimpleBlockItemWithoutTab("drained_pixie_basic_blood", () -> new DrainedPixieItem(PixieRank.BASIC, Sources.BLOOD, new Item.Properties().stacksTo(16).rarity(Rarity.COMMON)));
    public static final IRegistryItem<Item, DrainedPixieItem> DRAINED_GRAND_BLOOD_PIXIE = registerSimpleBlockItemWithoutTab("drained_pixie_grand_blood", () -> new DrainedPixieItem(PixieRank.GRAND, Sources.BLOOD, new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON)));
    public static final IRegistryItem<Item, DrainedPixieItem> DRAINED_MAJESTIC_BLOOD_PIXIE = registerSimpleBlockItemWithoutTab("drained_pixie_majestic_blood", () -> new DrainedPixieItem(PixieRank.MAJESTIC, Sources.BLOOD, new Item.Properties().stacksTo(16).rarity(Rarity.RARE)));
    public static final IRegistryItem<Item, DrainedPixieItem> DRAINED_BASIC_INFERNAL_PIXIE = registerSimpleBlockItemWithoutTab("drained_pixie_basic_infernal", () -> new DrainedPixieItem(PixieRank.BASIC, Sources.INFERNAL, new Item.Properties().stacksTo(16).fireResistant().rarity(Rarity.COMMON)));
    public static final IRegistryItem<Item, DrainedPixieItem> DRAINED_GRAND_INFERNAL_PIXIE = registerSimpleBlockItemWithoutTab("drained_pixie_grand_infernal", () -> new DrainedPixieItem(PixieRank.GRAND, Sources.INFERNAL, new Item.Properties().stacksTo(16).fireResistant().rarity(Rarity.UNCOMMON)));
    public static final IRegistryItem<Item, DrainedPixieItem> DRAINED_MAJESTIC_INFERNAL_PIXIE = registerSimpleBlockItemWithoutTab("drained_pixie_majestic_infernal", () -> new DrainedPixieItem(PixieRank.MAJESTIC, Sources.INFERNAL, new Item.Properties().stacksTo(16).fireResistant().rarity(Rarity.RARE)));
    public static final IRegistryItem<Item, DrainedPixieItem> DRAINED_BASIC_VOID_PIXIE = registerSimpleBlockItemWithoutTab("drained_pixie_basic_void", () -> new DrainedPixieItem(PixieRank.BASIC, Sources.VOID, new Item.Properties().stacksTo(16).rarity(Rarity.COMMON)));
    public static final IRegistryItem<Item, DrainedPixieItem> DRAINED_GRAND_VOID_PIXIE = registerSimpleBlockItemWithoutTab("drained_pixie_grand_void", () -> new DrainedPixieItem(PixieRank.GRAND, Sources.VOID, new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON)));
    public static final IRegistryItem<Item, DrainedPixieItem> DRAINED_MAJESTIC_VOID_PIXIE = registerSimpleBlockItemWithoutTab("drained_pixie_majestic_void", () -> new DrainedPixieItem(PixieRank.MAJESTIC, Sources.VOID, new Item.Properties().stacksTo(16).rarity(Rarity.RARE)));
    public static final IRegistryItem<Item, DrainedPixieItem> DRAINED_BASIC_HALLOWED_PIXIE = registerSimpleBlockItemWithoutTab("drained_pixie_basic_hallowed", () -> new DrainedPixieItem(PixieRank.BASIC, Sources.HALLOWED, new Item.Properties().stacksTo(16).rarity(Rarity.COMMON)));
    public static final IRegistryItem<Item, DrainedPixieItem> DRAINED_GRAND_HALLOWED_PIXIE = registerSimpleBlockItemWithoutTab("drained_pixie_grand_hallowed", () -> new DrainedPixieItem(PixieRank.GRAND, Sources.HALLOWED, new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON)));
    public static final IRegistryItem<Item, DrainedPixieItem> DRAINED_MAJESTIC_HALLOWED_PIXIE = registerSimpleBlockItemWithoutTab("drained_pixie_majestic_hallowed", () -> new DrainedPixieItem(PixieRank.MAJESTIC, Sources.HALLOWED, new Item.Properties().stacksTo(16).rarity(Rarity.RARE)));

    // Register book items
    public static final IRegistryItem<Item, StaticBookItem> STATIC_BOOK = registerSimpleBlockItemWithoutTab("static_book", () -> new StaticBookItem(BookType.BOOK, new Item.Properties().stacksTo(16)));
    public static final IRegistryItem<Item, StaticBookItem> STATIC_BOOK_UNCOMMON = registerSimpleBlockItemWithoutTab("static_book_uncommon", () -> new StaticBookItem(BookType.BOOK, new Item.Properties().stacksTo(16)));  // Deliberately not setting rarity here
    public static final IRegistryItem<Item, StaticBookItem> STATIC_BOOK_RARE = registerSimpleBlockItemWithoutTab("static_book_rare", () -> new StaticBookItem(BookType.BOOK, new Item.Properties().stacksTo(16)));  // Deliberately not setting rarity here
    public static final IRegistryItem<Item, StaticBookItem> STATIC_TABLET = registerSimpleBlockItemWithoutTab("static_tablet", () -> new StaticBookItem(BookType.TABLET, new Item.Properties().stacksTo(16)));
    public static final IRegistryItem<Item, Item> LORE_TABLET_FRAGMENT = registerSimpleItemSupplier("lore_tablet_fragment");
    public static final IRegistryItem<Item, LootGeneratorItem> LORE_TABLET_DIRTY = registerSimpleBlockItemSupplier("lore_tablet_dirty", () -> new LootGeneratorItem(LootTablesPM.LIBRARY_ARCHAEOLOGY, new Item.Properties()));

    // Register debug items
    public static final IRegistryItem<Item, TickStickItem> TICK_STICK = registerSimpleBlockItemWithoutTab("tick_stick", () -> new TickStickItem(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));
}
