package com.verdantartifice.primalmagick.common.items;

import java.util.function.Supplier;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.armortrim.TrimPatternsPM;
import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import com.verdantartifice.primalmagick.common.books.BookType;
import com.verdantartifice.primalmagick.common.books.CodexType;
import com.verdantartifice.primalmagick.common.creative.CreativeModeTabsPM;
import com.verdantartifice.primalmagick.common.entities.EntityTypesPM;
import com.verdantartifice.primalmagick.common.items.armor.ArmorMaterialPM;
import com.verdantartifice.primalmagick.common.items.armor.RobeArmorItem;
import com.verdantartifice.primalmagick.common.items.armor.WardingModuleItem;
import com.verdantartifice.primalmagick.common.items.books.LinguisticsGainItem;
import com.verdantartifice.primalmagick.common.items.books.StaticBookGeneratorItem;
import com.verdantartifice.primalmagick.common.items.books.StaticBookItem;
import com.verdantartifice.primalmagick.common.items.concoctions.AlchemicalBombItem;
import com.verdantartifice.primalmagick.common.items.concoctions.BombCasingItem;
import com.verdantartifice.primalmagick.common.items.concoctions.ConcoctionItem;
import com.verdantartifice.primalmagick.common.items.concoctions.SkyglassFlaskItem;
import com.verdantartifice.primalmagick.common.items.entities.FlyingCarpetItem;
import com.verdantartifice.primalmagick.common.items.entities.ManaArrowItem;
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
import com.verdantartifice.primalmagick.common.items.misc.DreamVisionTalismanItem;
import com.verdantartifice.primalmagick.common.items.misc.EarthshatterHammerItem;
import com.verdantartifice.primalmagick.common.items.misc.EnchantedInkAndQuillItem;
import com.verdantartifice.primalmagick.common.items.misc.ForbiddenSourceGainItem;
import com.verdantartifice.primalmagick.common.items.misc.GrimoireItem;
import com.verdantartifice.primalmagick.common.items.misc.HallowedOrbItem;
import com.verdantartifice.primalmagick.common.items.misc.HummingArtifactItem;
import com.verdantartifice.primalmagick.common.items.misc.IgnyxItem;
import com.verdantartifice.primalmagick.common.items.misc.KnowledgeGainItem;
import com.verdantartifice.primalmagick.common.items.misc.ManaFontBlockItem;
import com.verdantartifice.primalmagick.common.items.misc.PixieItem;
import com.verdantartifice.primalmagick.common.items.misc.RecallStoneItem;
import com.verdantartifice.primalmagick.common.items.misc.ResearchGainItem;
import com.verdantartifice.primalmagick.common.items.misc.RuneItem;
import com.verdantartifice.primalmagick.common.items.misc.SanguineCoreItem;
import com.verdantartifice.primalmagick.common.items.misc.SeascribePenItem;
import com.verdantartifice.primalmagick.common.items.misc.SpellcraftingAltarBlockItem;
import com.verdantartifice.primalmagick.common.items.misc.TickStickItem;
import com.verdantartifice.primalmagick.common.items.tools.ForbiddenBowItem;
import com.verdantartifice.primalmagick.common.items.tools.ForbiddenSwordItem;
import com.verdantartifice.primalmagick.common.items.tools.ForbiddenTridentItem;
import com.verdantartifice.primalmagick.common.items.tools.HallowsteelShieldItem;
import com.verdantartifice.primalmagick.common.items.tools.HallowsteelTridentItem;
import com.verdantartifice.primalmagick.common.items.tools.HexiumShieldItem;
import com.verdantartifice.primalmagick.common.items.tools.HexiumTridentItem;
import com.verdantartifice.primalmagick.common.items.tools.ItemTierPM;
import com.verdantartifice.primalmagick.common.items.tools.PrimalAxeItem;
import com.verdantartifice.primalmagick.common.items.tools.PrimalFishingRodItem;
import com.verdantartifice.primalmagick.common.items.tools.PrimalHoeItem;
import com.verdantartifice.primalmagick.common.items.tools.PrimalPickaxeItem;
import com.verdantartifice.primalmagick.common.items.tools.PrimalShovelItem;
import com.verdantartifice.primalmagick.common.items.tools.PrimaliteShieldItem;
import com.verdantartifice.primalmagick.common.items.tools.PrimaliteTridentItem;
import com.verdantartifice.primalmagick.common.items.tools.SacredShieldItem;
import com.verdantartifice.primalmagick.common.items.tools.TieredBowItem;
import com.verdantartifice.primalmagick.common.items.tools.TieredFishingRodItem;
import com.verdantartifice.primalmagick.common.items.wands.ModularStaffItem;
import com.verdantartifice.primalmagick.common.items.wands.ModularWandItem;
import com.verdantartifice.primalmagick.common.items.wands.MundaneWandItem;
import com.verdantartifice.primalmagick.common.items.wands.SpellScrollItem;
import com.verdantartifice.primalmagick.common.items.wands.StaffCoreItem;
import com.verdantartifice.primalmagick.common.items.wands.WandCapItem;
import com.verdantartifice.primalmagick.common.items.wands.WandCoreItem;
import com.verdantartifice.primalmagick.common.items.wands.WandGemItem;
import com.verdantartifice.primalmagick.common.misc.DeviceTier;
import com.verdantartifice.primalmagick.common.research.KnowledgeType;
import com.verdantartifice.primalmagick.common.research.ResearchNames;
import com.verdantartifice.primalmagick.common.runes.Rune;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.wands.WandCap;
import com.verdantartifice.primalmagick.common.wands.WandCore;
import com.verdantartifice.primalmagick.common.wands.WandGem;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BowlFoodItem;
import net.minecraft.world.item.DoubleHighBlockItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SmithingTemplateItem;
import net.minecraft.world.item.SwordItem;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Deferred registry for mod items.
 * 
 * @author Daedalus4096
 */
public class ItemsPM {
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, PrimalMagick.MODID);
    
    public static void init() {
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
    
    protected static <I extends Item> RegistryObject<I> registerSupplier(String name, Supplier<? extends I> itemSupplier) {
        RegistryObject<I> obj = registerWithoutTab(name, itemSupplier);
        CreativeModeTabsPM.registerSupplier(obj);
        return obj;
    }
    
    protected static <I extends Item> RegistryObject<I> registerDefaultInstance(String name, Supplier<? extends I> itemSupplier) {
        RegistryObject<I> obj = registerWithoutTab(name, itemSupplier);
        CreativeModeTabsPM.registerDefaultInstance(obj);
        return obj;
    }
    
    protected static <I extends Item> RegistryObject<I> registerCustom(String name, CreativeModeTabsPM.CustomTabRegistrar registrar, Supplier<? extends I> itemSupplier) {
        RegistryObject<I> obj = registerWithoutTab(name, itemSupplier);
        CreativeModeTabsPM.registerCustom(obj, registrar);
        return obj;
    }
    
    protected static <I extends Item> RegistryObject<I> registerWithoutTab(String name, Supplier<? extends I> itemSupplier) {
        return ITEMS.register(name, itemSupplier);
    }
    
    // Register grimoire items
    public static final RegistryObject<GrimoireItem> GRIMOIRE = registerSupplier("grimoire", () -> new GrimoireItem(false, new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<GrimoireItem> CREATIVE_GRIMOIRE = registerSupplier("grimoire_creative", () -> new GrimoireItem(true, new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));
    
    // Register raw marble block items
    public static final RegistryObject<BlockItem> MARBLE_RAW = registerSupplier("marble_raw", () -> new BlockItem(BlocksPM.MARBLE_RAW.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MARBLE_SLAB = registerSupplier("marble_slab", () -> new BlockItem(BlocksPM.MARBLE_SLAB.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MARBLE_STAIRS = registerSupplier("marble_stairs", () -> new BlockItem(BlocksPM.MARBLE_STAIRS.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MARBLE_WALL = registerSupplier("marble_wall", () -> new BlockItem(BlocksPM.MARBLE_WALL.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MARBLE_BRICKS = registerSupplier("marble_bricks", () -> new BlockItem(BlocksPM.MARBLE_BRICKS.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MARBLE_BRICK_SLAB = registerSupplier("marble_brick_slab", () -> new BlockItem(BlocksPM.MARBLE_BRICK_SLAB.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MARBLE_BRICK_STAIRS = registerSupplier("marble_brick_stairs", () -> new BlockItem(BlocksPM.MARBLE_BRICK_STAIRS.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MARBLE_BRICK_WALL = registerSupplier("marble_brick_wall", () -> new BlockItem(BlocksPM.MARBLE_BRICK_WALL.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MARBLE_PILLAR = registerSupplier("marble_pillar", () -> new BlockItem(BlocksPM.MARBLE_PILLAR.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MARBLE_CHISELED = registerSupplier("marble_chiseled", () -> new BlockItem(BlocksPM.MARBLE_CHISELED.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MARBLE_RUNED = registerSupplier("marble_runed", () -> new BlockItem(BlocksPM.MARBLE_RUNED.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MARBLE_TILES = registerSupplier("marble_tiles", () -> new BlockItem(BlocksPM.MARBLE_TILES.get(), new Item.Properties()));

    // Register enchanted marble block items
    public static final RegistryObject<BlockItem> MARBLE_ENCHANTED = registerSupplier("marble_enchanted", () -> new BlockItem(BlocksPM.MARBLE_ENCHANTED.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MARBLE_ENCHANTED_SLAB = registerSupplier("marble_enchanted_slab", () -> new BlockItem(BlocksPM.MARBLE_ENCHANTED_SLAB.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MARBLE_ENCHANTED_STAIRS = registerSupplier("marble_enchanted_stairs", () -> new BlockItem(BlocksPM.MARBLE_ENCHANTED_STAIRS.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MARBLE_ENCHANTED_WALL = registerSupplier("marble_enchanted_wall", () -> new BlockItem(BlocksPM.MARBLE_ENCHANTED_WALL.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MARBLE_ENCHANTED_BRICKS = registerSupplier("marble_enchanted_bricks", () -> new BlockItem(BlocksPM.MARBLE_ENCHANTED_BRICKS.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MARBLE_ENCHANTED_BRICK_SLAB = registerSupplier("marble_enchanted_brick_slab", () -> new BlockItem(BlocksPM.MARBLE_ENCHANTED_BRICK_SLAB.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MARBLE_ENCHANTED_BRICK_STAIRS = registerSupplier("marble_enchanted_brick_stairs", () -> new BlockItem(BlocksPM.MARBLE_ENCHANTED_BRICK_STAIRS.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MARBLE_ENCHANTED_BRICK_WALL = registerSupplier("marble_enchanted_brick_wall", () -> new BlockItem(BlocksPM.MARBLE_ENCHANTED_BRICK_WALL.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MARBLE_ENCHANTED_PILLAR = registerSupplier("marble_enchanted_pillar", () -> new BlockItem(BlocksPM.MARBLE_ENCHANTED_PILLAR.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MARBLE_ENCHANTED_CHISELED = registerSupplier("marble_enchanted_chiseled", () -> new BlockItem(BlocksPM.MARBLE_ENCHANTED_CHISELED.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MARBLE_ENCHANTED_RUNED = registerSupplier("marble_enchanted_runed", () -> new BlockItem(BlocksPM.MARBLE_ENCHANTED_RUNED.get(), new Item.Properties()));

    // Register smoked marble block items
    public static final RegistryObject<BlockItem> MARBLE_SMOKED = registerSupplier("marble_smoked", () -> new BlockItem(BlocksPM.MARBLE_SMOKED.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MARBLE_SMOKED_SLAB = registerSupplier("marble_smoked_slab", () -> new BlockItem(BlocksPM.MARBLE_SMOKED_SLAB.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MARBLE_SMOKED_STAIRS = registerSupplier("marble_smoked_stairs", () -> new BlockItem(BlocksPM.MARBLE_SMOKED_STAIRS.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MARBLE_SMOKED_WALL = registerSupplier("marble_smoked_wall", () -> new BlockItem(BlocksPM.MARBLE_SMOKED_WALL.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MARBLE_SMOKED_BRICKS = registerSupplier("marble_smoked_bricks", () -> new BlockItem(BlocksPM.MARBLE_SMOKED_BRICKS.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MARBLE_SMOKED_BRICK_SLAB = registerSupplier("marble_smoked_brick_slab", () -> new BlockItem(BlocksPM.MARBLE_SMOKED_BRICK_SLAB.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MARBLE_SMOKED_BRICK_STAIRS = registerSupplier("marble_smoked_brick_stairs", () -> new BlockItem(BlocksPM.MARBLE_SMOKED_BRICK_STAIRS.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MARBLE_SMOKED_BRICK_WALL = registerSupplier("marble_smoked_brick_wall", () -> new BlockItem(BlocksPM.MARBLE_SMOKED_BRICK_WALL.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MARBLE_SMOKED_PILLAR = registerSupplier("marble_smoked_pillar", () -> new BlockItem(BlocksPM.MARBLE_SMOKED_PILLAR.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MARBLE_SMOKED_CHISELED = registerSupplier("marble_smoked_chiseled", () -> new BlockItem(BlocksPM.MARBLE_SMOKED_CHISELED.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MARBLE_SMOKED_RUNED = registerSupplier("marble_smoked_runed", () -> new BlockItem(BlocksPM.MARBLE_SMOKED_RUNED.get(), new Item.Properties()));
    
    // Register hallowed marble block items
    public static final RegistryObject<BlockItem> MARBLE_HALLOWED = registerSupplier("marble_hallowed", () -> new BlockItem(BlocksPM.MARBLE_HALLOWED.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MARBLE_HALLOWED_SLAB = registerSupplier("marble_hallowed_slab", () -> new BlockItem(BlocksPM.MARBLE_HALLOWED_SLAB.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MARBLE_HALLOWED_STAIRS = registerSupplier("marble_hallowed_stairs", () -> new BlockItem(BlocksPM.MARBLE_HALLOWED_STAIRS.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MARBLE_HALLOWED_WALL = registerSupplier("marble_hallowed_wall", () -> new BlockItem(BlocksPM.MARBLE_HALLOWED_WALL.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MARBLE_HALLOWED_BRICKS = registerSupplier("marble_hallowed_bricks", () -> new BlockItem(BlocksPM.MARBLE_HALLOWED_BRICKS.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MARBLE_HALLOWED_BRICK_SLAB = registerSupplier("marble_hallowed_brick_slab", () -> new BlockItem(BlocksPM.MARBLE_HALLOWED_BRICK_SLAB.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MARBLE_HALLOWED_BRICK_STAIRS = registerSupplier("marble_hallowed_brick_stairs", () -> new BlockItem(BlocksPM.MARBLE_HALLOWED_BRICK_STAIRS.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MARBLE_HALLOWED_BRICK_WALL = registerSupplier("marble_hallowed_brick_wall", () -> new BlockItem(BlocksPM.MARBLE_HALLOWED_BRICK_WALL.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MARBLE_HALLOWED_PILLAR = registerSupplier("marble_hallowed_pillar", () -> new BlockItem(BlocksPM.MARBLE_HALLOWED_PILLAR.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MARBLE_HALLOWED_CHISELED = registerSupplier("marble_hallowed_chiseled", () -> new BlockItem(BlocksPM.MARBLE_HALLOWED_CHISELED.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MARBLE_HALLOWED_RUNED = registerSupplier("marble_hallowed_runed", () -> new BlockItem(BlocksPM.MARBLE_HALLOWED_RUNED.get(), new Item.Properties()));

    // Register sunwood block items
    public static final RegistryObject<BlockItem> SUNWOOD_LOG = registerSupplier("sunwood_log", () -> new BlockItem(BlocksPM.SUNWOOD_LOG.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> STRIPPED_SUNWOOD_LOG = registerSupplier("stripped_sunwood_log", () -> new BlockItem(BlocksPM.STRIPPED_SUNWOOD_LOG.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> SUNWOOD_WOOD = registerSupplier("sunwood_wood", () -> new BlockItem(BlocksPM.SUNWOOD_WOOD.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> STRIPPED_SUNWOOD_WOOD = registerSupplier("stripped_sunwood_wood", () -> new BlockItem(BlocksPM.STRIPPED_SUNWOOD_WOOD.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> SUNWOOD_LEAVES = registerSupplier("sunwood_leaves", () -> new BlockItem(BlocksPM.SUNWOOD_LEAVES.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> SUNWOOD_SAPLING = registerSupplier("sunwood_sapling", () -> new BlockItem(BlocksPM.SUNWOOD_SAPLING.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> SUNWOOD_PLANKS = registerSupplier("sunwood_planks", () -> new BlockItem(BlocksPM.SUNWOOD_PLANKS.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> SUNWOOD_SLAB = registerSupplier("sunwood_slab", () -> new BlockItem(BlocksPM.SUNWOOD_SLAB.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> SUNWOOD_STAIRS = registerSupplier("sunwood_stairs", () -> new BlockItem(BlocksPM.SUNWOOD_STAIRS.get(), new Item.Properties()));
    public static final RegistryObject<BurnableBlockItem> SUNWOOD_PILLAR = registerSupplier("sunwood_pillar", () -> new BurnableBlockItem(BlocksPM.SUNWOOD_PILLAR.get(), 300, new Item.Properties()));

    // Register moonwood block items
    public static final RegistryObject<BlockItem> MOONWOOD_LOG = registerSupplier("moonwood_log", () -> new BlockItem(BlocksPM.MOONWOOD_LOG.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> STRIPPED_MOONWOOD_LOG = registerSupplier("stripped_moonwood_log", () -> new BlockItem(BlocksPM.STRIPPED_MOONWOOD_LOG.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MOONWOOD_WOOD = registerSupplier("moonwood_wood", () -> new BlockItem(BlocksPM.MOONWOOD_WOOD.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> STRIPPED_MOONWOOD_WOOD = registerSupplier("stripped_moonwood_wood", () -> new BlockItem(BlocksPM.STRIPPED_MOONWOOD_WOOD.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MOONWOOD_LEAVES = registerSupplier("moonwood_leaves", () -> new BlockItem(BlocksPM.MOONWOOD_LEAVES.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MOONWOOD_SAPLING = registerSupplier("moonwood_sapling", () -> new BlockItem(BlocksPM.MOONWOOD_SAPLING.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MOONWOOD_PLANKS = registerSupplier("moonwood_planks", () -> new BlockItem(BlocksPM.MOONWOOD_PLANKS.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MOONWOOD_SLAB = registerSupplier("moonwood_slab", () -> new BlockItem(BlocksPM.MOONWOOD_SLAB.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MOONWOOD_STAIRS = registerSupplier("moonwood_stairs", () -> new BlockItem(BlocksPM.MOONWOOD_STAIRS.get(), new Item.Properties()));
    public static final RegistryObject<BurnableBlockItem> MOONWOOD_PILLAR = registerSupplier("moonwood_pillar", () -> new BurnableBlockItem(BlocksPM.MOONWOOD_PILLAR.get(), 300, new Item.Properties()));
    
    // Register hallowood block items
    public static final RegistryObject<BlockItem> HALLOWOOD_LOG = registerSupplier("hallowood_log", () -> new BlockItem(BlocksPM.HALLOWOOD_LOG.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> STRIPPED_HALLOWOOD_LOG = registerSupplier("stripped_hallowood_log", () -> new BlockItem(BlocksPM.STRIPPED_HALLOWOOD_LOG.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> HALLOWOOD_WOOD = registerSupplier("hallowood_wood", () -> new BlockItem(BlocksPM.HALLOWOOD_WOOD.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> STRIPPED_HALLOWOOD_WOOD = registerSupplier("stripped_hallowood_wood", () -> new BlockItem(BlocksPM.STRIPPED_HALLOWOOD_WOOD.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> HALLOWOOD_LEAVES = registerSupplier("hallowood_leaves", () -> new BlockItem(BlocksPM.HALLOWOOD_LEAVES.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> HALLOWOOD_SAPLING = registerSupplier("hallowood_sapling", () -> new BlockItem(BlocksPM.HALLOWOOD_SAPLING.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> HALLOWOOD_PLANKS = registerSupplier("hallowood_planks", () -> new BlockItem(BlocksPM.HALLOWOOD_PLANKS.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> HALLOWOOD_SLAB = registerSupplier("hallowood_slab", () -> new BlockItem(BlocksPM.HALLOWOOD_SLAB.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> HALLOWOOD_STAIRS = registerSupplier("hallowood_stairs", () -> new BlockItem(BlocksPM.HALLOWOOD_STAIRS.get(), new Item.Properties()));
    public static final RegistryObject<BurnableBlockItem> HALLOWOOD_PILLAR = registerSupplier("hallowood_pillar", () -> new BurnableBlockItem(BlocksPM.HALLOWOOD_PILLAR.get(), 300, new Item.Properties()));
    
    // Register crop items
    public static final RegistryObject<Item> HYDROMELON_SEEDS = registerSupplier("hydromelon_seeds", () -> new ItemNameBlockItem(BlocksPM.HYRDOMELON_STEM.get(), new Item.Properties()));
    public static final RegistryObject<Item> HYDROMELON = registerSupplier("hydromelon", () -> new BlockItem(BlocksPM.HYDROMELON.get(), new Item.Properties()));
    public static final RegistryObject<Item> HYDROMELON_SLICE = registerSupplier("hydromelon_slice", () -> new Item(new Item.Properties().food(FoodsPM.HYDROMELON_SLICE)));
    public static final RegistryObject<DoubleHighBlockItem> BLOOD_ROSE = registerSupplier("blood_rose", () -> new DoubleHighBlockItem(BlocksPM.BLOOD_ROSE.get(), new Item.Properties()));
    public static final RegistryObject<DoubleHighBlockItem> EMBERFLOWER = registerSupplier("emberflower", () -> new DoubleHighBlockItem(BlocksPM.EMBERFLOWER.get(), new Item.Properties()));

    // Register infused stone block items
    public static final RegistryObject<BlockItem> INFUSED_STONE_EARTH = registerSupplier("infused_stone_earth", () -> new BlockItem(BlocksPM.INFUSED_STONE_EARTH.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> INFUSED_STONE_SEA = registerSupplier("infused_stone_sea", () -> new BlockItem(BlocksPM.INFUSED_STONE_SEA.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> INFUSED_STONE_SKY = registerSupplier("infused_stone_sky", () -> new BlockItem(BlocksPM.INFUSED_STONE_SKY.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> INFUSED_STONE_SUN = registerSupplier("infused_stone_sun", () -> new BlockItem(BlocksPM.INFUSED_STONE_SUN.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> INFUSED_STONE_MOON = registerSupplier("infused_stone_moon", () -> new BlockItem(BlocksPM.INFUSED_STONE_MOON.get(), new Item.Properties()));
    
    // Register budding gem block items
    public static final RegistryObject<BlockItem> SYNTHETIC_AMETHYST_CLUSTER = registerSupplier("synthetic_amethyst_cluster", () -> new BlockItem(BlocksPM.SYNTHETIC_AMETHYST_CLUSTER.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> LARGE_SYNTHETIC_AMETHYST_BUD = registerSupplier("large_synthetic_amethyst_bud", () -> new BlockItem(BlocksPM.LARGE_SYNTHETIC_AMETHYST_BUD.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MEDIUM_SYNTHETIC_AMETHYST_BUD = registerSupplier("medium_synthetic_amethyst_bud", () -> new BlockItem(BlocksPM.MEDIUM_SYNTHETIC_AMETHYST_BUD.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> SMALL_SYNTHETIC_AMETHYST_BUD = registerSupplier("small_synthetic_amethyst_bud", () -> new BlockItem(BlocksPM.SMALL_SYNTHETIC_AMETHYST_BUD.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> DAMAGED_BUDDING_AMETHYST_BLOCK = registerSupplier("damaged_budding_amethyst_block", () -> new BlockItem(BlocksPM.DAMAGED_BUDDING_AMETHYST_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> CHIPPED_BUDDING_AMETHYST_BLOCK = registerSupplier("chipped_budding_amethyst_block", () -> new BlockItem(BlocksPM.CHIPPED_BUDDING_AMETHYST_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> FLAWED_BUDDING_AMETHYST_BLOCK = registerSupplier("flawed_budding_amethyst_block", () -> new BlockItem(BlocksPM.FLAWED_BUDDING_AMETHYST_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> SYNTHETIC_DIAMOND_CLUSTER = registerSupplier("synthetic_diamond_cluster", () -> new BlockItem(BlocksPM.SYNTHETIC_DIAMOND_CLUSTER.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> LARGE_SYNTHETIC_DIAMOND_BUD = registerSupplier("large_synthetic_diamond_bud", () -> new BlockItem(BlocksPM.LARGE_SYNTHETIC_DIAMOND_BUD.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MEDIUM_SYNTHETIC_DIAMOND_BUD = registerSupplier("medium_synthetic_diamond_bud", () -> new BlockItem(BlocksPM.MEDIUM_SYNTHETIC_DIAMOND_BUD.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> SMALL_SYNTHETIC_DIAMOND_BUD = registerSupplier("small_synthetic_diamond_bud", () -> new BlockItem(BlocksPM.SMALL_SYNTHETIC_DIAMOND_BUD.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> DAMAGED_BUDDING_DIAMOND_BLOCK = registerSupplier("damaged_budding_diamond_block", () -> new BlockItem(BlocksPM.DAMAGED_BUDDING_DIAMOND_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> CHIPPED_BUDDING_DIAMOND_BLOCK = registerSupplier("chipped_budding_diamond_block", () -> new BlockItem(BlocksPM.CHIPPED_BUDDING_DIAMOND_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> FLAWED_BUDDING_DIAMOND_BLOCK = registerSupplier("flawed_budding_diamond_block", () -> new BlockItem(BlocksPM.FLAWED_BUDDING_DIAMOND_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> SYNTHETIC_EMERALD_CLUSTER = registerSupplier("synthetic_emerald_cluster", () -> new BlockItem(BlocksPM.SYNTHETIC_EMERALD_CLUSTER.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> LARGE_SYNTHETIC_EMERALD_BUD = registerSupplier("large_synthetic_emerald_bud", () -> new BlockItem(BlocksPM.LARGE_SYNTHETIC_EMERALD_BUD.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MEDIUM_SYNTHETIC_EMERALD_BUD = registerSupplier("medium_synthetic_emerald_bud", () -> new BlockItem(BlocksPM.MEDIUM_SYNTHETIC_EMERALD_BUD.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> SMALL_SYNTHETIC_EMERALD_BUD = registerSupplier("small_synthetic_emerald_bud", () -> new BlockItem(BlocksPM.SMALL_SYNTHETIC_EMERALD_BUD.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> DAMAGED_BUDDING_EMERALD_BLOCK = registerSupplier("damaged_budding_emerald_block", () -> new BlockItem(BlocksPM.DAMAGED_BUDDING_EMERALD_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> CHIPPED_BUDDING_EMERALD_BLOCK = registerSupplier("chipped_budding_emerald_block", () -> new BlockItem(BlocksPM.CHIPPED_BUDDING_EMERALD_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> FLAWED_BUDDING_EMERALD_BLOCK = registerSupplier("flawed_budding_emerald_block", () -> new BlockItem(BlocksPM.FLAWED_BUDDING_EMERALD_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> SYNTHETIC_QUARTZ_CLUSTER = registerSupplier("synthetic_quartz_cluster", () -> new BlockItem(BlocksPM.SYNTHETIC_QUARTZ_CLUSTER.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> LARGE_SYNTHETIC_QUARTZ_BUD = registerSupplier("large_synthetic_quartz_bud", () -> new BlockItem(BlocksPM.LARGE_SYNTHETIC_QUARTZ_BUD.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MEDIUM_SYNTHETIC_QUARTZ_BUD = registerSupplier("medium_synthetic_quartz_bud", () -> new BlockItem(BlocksPM.MEDIUM_SYNTHETIC_QUARTZ_BUD.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> SMALL_SYNTHETIC_QUARTZ_BUD = registerSupplier("small_synthetic_quartz_bud", () -> new BlockItem(BlocksPM.SMALL_SYNTHETIC_QUARTZ_BUD.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> DAMAGED_BUDDING_QUARTZ_BLOCK = registerSupplier("damaged_budding_quartz_block", () -> new BlockItem(BlocksPM.DAMAGED_BUDDING_QUARTZ_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> CHIPPED_BUDDING_QUARTZ_BLOCK = registerSupplier("chipped_budding_quartz_block", () -> new BlockItem(BlocksPM.CHIPPED_BUDDING_QUARTZ_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> FLAWED_BUDDING_QUARTZ_BLOCK = registerSupplier("flawed_budding_quartz_block", () -> new BlockItem(BlocksPM.FLAWED_BUDDING_QUARTZ_BLOCK.get(), new Item.Properties()));

    // Register skyglass block items
    public static final RegistryObject<BlockItem> SKYGLASS = registerSupplier("skyglass", () -> new BlockItem(BlocksPM.SKYGLASS.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> STAINED_SKYGLASS_BLACK = registerSupplier("stained_skyglass_black", () -> new BlockItem(BlocksPM.STAINED_SKYGLASS_BLACK.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> STAINED_SKYGLASS_BLUE = registerSupplier("stained_skyglass_blue", () -> new BlockItem(BlocksPM.STAINED_SKYGLASS_BLUE.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> STAINED_SKYGLASS_BROWN = registerSupplier("stained_skyglass_brown", () -> new BlockItem(BlocksPM.STAINED_SKYGLASS_BROWN.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> STAINED_SKYGLASS_CYAN = registerSupplier("stained_skyglass_cyan", () -> new BlockItem(BlocksPM.STAINED_SKYGLASS_CYAN.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> STAINED_SKYGLASS_GRAY = registerSupplier("stained_skyglass_gray", () -> new BlockItem(BlocksPM.STAINED_SKYGLASS_GRAY.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> STAINED_SKYGLASS_GREEN = registerSupplier("stained_skyglass_green", () -> new BlockItem(BlocksPM.STAINED_SKYGLASS_GREEN.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> STAINED_SKYGLASS_LIGHT_BLUE = registerSupplier("stained_skyglass_light_blue", () -> new BlockItem(BlocksPM.STAINED_SKYGLASS_LIGHT_BLUE.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> STAINED_SKYGLASS_LIGHT_GRAY = registerSupplier("stained_skyglass_light_gray", () -> new BlockItem(BlocksPM.STAINED_SKYGLASS_LIGHT_GRAY.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> STAINED_SKYGLASS_LIME = registerSupplier("stained_skyglass_lime", () -> new BlockItem(BlocksPM.STAINED_SKYGLASS_LIME.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> STAINED_SKYGLASS_MAGENTA = registerSupplier("stained_skyglass_magenta", () -> new BlockItem(BlocksPM.STAINED_SKYGLASS_MAGENTA.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> STAINED_SKYGLASS_ORANGE = registerSupplier("stained_skyglass_orange", () -> new BlockItem(BlocksPM.STAINED_SKYGLASS_ORANGE.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> STAINED_SKYGLASS_PINK = registerSupplier("stained_skyglass_pink", () -> new BlockItem(BlocksPM.STAINED_SKYGLASS_PINK.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> STAINED_SKYGLASS_PURPLE = registerSupplier("stained_skyglass_purple", () -> new BlockItem(BlocksPM.STAINED_SKYGLASS_PURPLE.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> STAINED_SKYGLASS_RED = registerSupplier("stained_skyglass_red", () -> new BlockItem(BlocksPM.STAINED_SKYGLASS_RED.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> STAINED_SKYGLASS_WHITE = registerSupplier("stained_skyglass_white", () -> new BlockItem(BlocksPM.STAINED_SKYGLASS_WHITE.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> STAINED_SKYGLASS_YELLOW = registerSupplier("stained_skyglass_yellow", () -> new BlockItem(BlocksPM.STAINED_SKYGLASS_YELLOW.get(), new Item.Properties()));

    // Register skyglass pane block items
    public static final RegistryObject<BlockItem> SKYGLASS_PANE = registerSupplier("skyglass_pane", () -> new BlockItem(BlocksPM.SKYGLASS_PANE.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> STAINED_SKYGLASS_PANE_BLACK = registerSupplier("stained_skyglass_pane_black", () -> new BlockItem(BlocksPM.STAINED_SKYGLASS_PANE_BLACK.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> STAINED_SKYGLASS_PANE_BLUE = registerSupplier("stained_skyglass_pane_blue", () -> new BlockItem(BlocksPM.STAINED_SKYGLASS_PANE_BLUE.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> STAINED_SKYGLASS_PANE_BROWN = registerSupplier("stained_skyglass_pane_brown", () -> new BlockItem(BlocksPM.STAINED_SKYGLASS_PANE_BROWN.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> STAINED_SKYGLASS_PANE_CYAN = registerSupplier("stained_skyglass_pane_cyan", () -> new BlockItem(BlocksPM.STAINED_SKYGLASS_PANE_CYAN.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> STAINED_SKYGLASS_PANE_GRAY = registerSupplier("stained_skyglass_pane_gray", () -> new BlockItem(BlocksPM.STAINED_SKYGLASS_PANE_GRAY.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> STAINED_SKYGLASS_PANE_GREEN = registerSupplier("stained_skyglass_pane_green", () -> new BlockItem(BlocksPM.STAINED_SKYGLASS_PANE_GREEN.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> STAINED_SKYGLASS_PANE_LIGHT_BLUE = registerSupplier("stained_skyglass_pane_light_blue", () -> new BlockItem(BlocksPM.STAINED_SKYGLASS_PANE_LIGHT_BLUE.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> STAINED_SKYGLASS_PANE_LIGHT_GRAY = registerSupplier("stained_skyglass_pane_light_gray", () -> new BlockItem(BlocksPM.STAINED_SKYGLASS_PANE_LIGHT_GRAY.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> STAINED_SKYGLASS_PANE_LIME = registerSupplier("stained_skyglass_pane_lime", () -> new BlockItem(BlocksPM.STAINED_SKYGLASS_PANE_LIME.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> STAINED_SKYGLASS_PANE_MAGENTA = registerSupplier("stained_skyglass_pane_magenta", () -> new BlockItem(BlocksPM.STAINED_SKYGLASS_PANE_MAGENTA.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> STAINED_SKYGLASS_PANE_ORANGE = registerSupplier("stained_skyglass_pane_orange", () -> new BlockItem(BlocksPM.STAINED_SKYGLASS_PANE_ORANGE.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> STAINED_SKYGLASS_PANE_PINK = registerSupplier("stained_skyglass_pane_pink", () -> new BlockItem(BlocksPM.STAINED_SKYGLASS_PANE_PINK.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> STAINED_SKYGLASS_PANE_PURPLE = registerSupplier("stained_skyglass_pane_purple", () -> new BlockItem(BlocksPM.STAINED_SKYGLASS_PANE_PURPLE.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> STAINED_SKYGLASS_PANE_RED = registerSupplier("stained_skyglass_pane_red", () -> new BlockItem(BlocksPM.STAINED_SKYGLASS_PANE_RED.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> STAINED_SKYGLASS_PANE_WHITE = registerSupplier("stained_skyglass_pane_white", () -> new BlockItem(BlocksPM.STAINED_SKYGLASS_PANE_WHITE.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> STAINED_SKYGLASS_PANE_YELLOW = registerSupplier("stained_skyglass_pane_yellow", () -> new BlockItem(BlocksPM.STAINED_SKYGLASS_PANE_YELLOW.get(), new Item.Properties()));

    // Register ritual candle block items
    public static final RegistryObject<BlockItem> RITUAL_CANDLE_BLACK = registerSupplier("ritual_candle_black", () -> new BlockItem(BlocksPM.RITUAL_CANDLE_BLACK.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> RITUAL_CANDLE_BLUE = registerSupplier("ritual_candle_blue", () -> new BlockItem(BlocksPM.RITUAL_CANDLE_BLUE.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> RITUAL_CANDLE_BROWN = registerSupplier("ritual_candle_brown", () -> new BlockItem(BlocksPM.RITUAL_CANDLE_BROWN.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> RITUAL_CANDLE_CYAN = registerSupplier("ritual_candle_cyan", () -> new BlockItem(BlocksPM.RITUAL_CANDLE_CYAN.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> RITUAL_CANDLE_GRAY = registerSupplier("ritual_candle_gray", () -> new BlockItem(BlocksPM.RITUAL_CANDLE_GRAY.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> RITUAL_CANDLE_GREEN = registerSupplier("ritual_candle_green", () -> new BlockItem(BlocksPM.RITUAL_CANDLE_GREEN.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> RITUAL_CANDLE_LIGHT_BLUE = registerSupplier("ritual_candle_light_blue", () -> new BlockItem(BlocksPM.RITUAL_CANDLE_LIGHT_BLUE.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> RITUAL_CANDLE_LIGHT_GRAY = registerSupplier("ritual_candle_light_gray", () -> new BlockItem(BlocksPM.RITUAL_CANDLE_LIGHT_GRAY.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> RITUAL_CANDLE_LIME = registerSupplier("ritual_candle_lime", () -> new BlockItem(BlocksPM.RITUAL_CANDLE_LIME.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> RITUAL_CANDLE_MAGENTA = registerSupplier("ritual_candle_magenta", () -> new BlockItem(BlocksPM.RITUAL_CANDLE_MAGENTA.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> RITUAL_CANDLE_ORANGE = registerSupplier("ritual_candle_orange", () -> new BlockItem(BlocksPM.RITUAL_CANDLE_ORANGE.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> RITUAL_CANDLE_PINK = registerSupplier("ritual_candle_pink", () -> new BlockItem(BlocksPM.RITUAL_CANDLE_PINK.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> RITUAL_CANDLE_PURPLE = registerSupplier("ritual_candle_purple", () -> new BlockItem(BlocksPM.RITUAL_CANDLE_PURPLE.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> RITUAL_CANDLE_RED = registerSupplier("ritual_candle_red", () -> new BlockItem(BlocksPM.RITUAL_CANDLE_RED.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> RITUAL_CANDLE_WHITE = registerSupplier("ritual_candle_white", () -> new BlockItem(BlocksPM.RITUAL_CANDLE_WHITE.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> RITUAL_CANDLE_YELLOW = registerSupplier("ritual_candle_yellow", () -> new BlockItem(BlocksPM.RITUAL_CANDLE_YELLOW.get(), new Item.Properties()));

    // Register mana font block items
    public static final RegistryObject<ManaFontBlockItem> ANCIENT_FONT_EARTH = registerSupplier("ancient_font_earth", () -> new ManaFontBlockItem(BlocksPM.ANCIENT_FONT_EARTH.get(), new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));
    public static final RegistryObject<ManaFontBlockItem> ANCIENT_FONT_SEA = registerSupplier("ancient_font_sea", () -> new ManaFontBlockItem(BlocksPM.ANCIENT_FONT_SEA.get(), new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));
    public static final RegistryObject<ManaFontBlockItem> ANCIENT_FONT_SKY = registerSupplier("ancient_font_sky", () -> new ManaFontBlockItem(BlocksPM.ANCIENT_FONT_SKY.get(), new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));
    public static final RegistryObject<ManaFontBlockItem> ANCIENT_FONT_SUN = registerSupplier("ancient_font_sun", () -> new ManaFontBlockItem(BlocksPM.ANCIENT_FONT_SUN.get(), new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));
    public static final RegistryObject<ManaFontBlockItem> ANCIENT_FONT_MOON = registerSupplier("ancient_font_moon", () -> new ManaFontBlockItem(BlocksPM.ANCIENT_FONT_MOON.get(), new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));
    public static final RegistryObject<ManaFontBlockItem> ARTIFICIAL_FONT_EARTH = registerSupplier("artificial_font_earth", () -> new ManaFontBlockItem(BlocksPM.ARTIFICIAL_FONT_EARTH.get(), new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<ManaFontBlockItem> ARTIFICIAL_FONT_SEA = registerSupplier("artificial_font_sea", () -> new ManaFontBlockItem(BlocksPM.ARTIFICIAL_FONT_SEA.get(), new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<ManaFontBlockItem> ARTIFICIAL_FONT_SKY = registerSupplier("artificial_font_sky", () -> new ManaFontBlockItem(BlocksPM.ARTIFICIAL_FONT_SKY.get(), new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<ManaFontBlockItem> ARTIFICIAL_FONT_SUN = registerSupplier("artificial_font_sun", () -> new ManaFontBlockItem(BlocksPM.ARTIFICIAL_FONT_SUN.get(), new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<ManaFontBlockItem> ARTIFICIAL_FONT_MOON = registerSupplier("artificial_font_moon", () -> new ManaFontBlockItem(BlocksPM.ARTIFICIAL_FONT_MOON.get(), new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<ManaFontBlockItem> ARTIFICIAL_FONT_BLOOD = registerSupplier("artificial_font_blood", () -> new ManaFontBlockItem(BlocksPM.ARTIFICIAL_FONT_BLOOD.get(), new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<ManaFontBlockItem> ARTIFICIAL_FONT_INFERNAL = registerSupplier("artificial_font_infernal", () -> new ManaFontBlockItem(BlocksPM.ARTIFICIAL_FONT_INFERNAL.get(), new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<ManaFontBlockItem> ARTIFICIAL_FONT_VOID = registerSupplier("artificial_font_void", () -> new ManaFontBlockItem(BlocksPM.ARTIFICIAL_FONT_VOID.get(), new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<ManaFontBlockItem> ARTIFICIAL_FONT_HALLOWED = registerSupplier("artificial_font_hallowed", () -> new ManaFontBlockItem(BlocksPM.ARTIFICIAL_FONT_HALLOWED.get(), new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<ManaFontBlockItem> FORBIDDEN_FONT_EARTH = registerSupplier("forbidden_font_earth", () -> new ManaFontBlockItem(BlocksPM.FORBIDDEN_FONT_EARTH.get(), new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));
    public static final RegistryObject<ManaFontBlockItem> FORBIDDEN_FONT_SEA = registerSupplier("forbidden_font_sea", () -> new ManaFontBlockItem(BlocksPM.FORBIDDEN_FONT_SEA.get(), new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));
    public static final RegistryObject<ManaFontBlockItem> FORBIDDEN_FONT_SKY = registerSupplier("forbidden_font_sky", () -> new ManaFontBlockItem(BlocksPM.FORBIDDEN_FONT_SKY.get(), new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));
    public static final RegistryObject<ManaFontBlockItem> FORBIDDEN_FONT_SUN = registerSupplier("forbidden_font_sun", () -> new ManaFontBlockItem(BlocksPM.FORBIDDEN_FONT_SUN.get(), new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));
    public static final RegistryObject<ManaFontBlockItem> FORBIDDEN_FONT_MOON = registerSupplier("forbidden_font_moon", () -> new ManaFontBlockItem(BlocksPM.FORBIDDEN_FONT_MOON.get(), new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));
    public static final RegistryObject<ManaFontBlockItem> FORBIDDEN_FONT_BLOOD = registerSupplier("forbidden_font_blood", () -> new ManaFontBlockItem(BlocksPM.FORBIDDEN_FONT_BLOOD.get(), new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));
    public static final RegistryObject<ManaFontBlockItem> FORBIDDEN_FONT_INFERNAL = registerSupplier("forbidden_font_infernal", () -> new ManaFontBlockItem(BlocksPM.FORBIDDEN_FONT_INFERNAL.get(), new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));
    public static final RegistryObject<ManaFontBlockItem> FORBIDDEN_FONT_VOID = registerSupplier("forbidden_font_void", () -> new ManaFontBlockItem(BlocksPM.FORBIDDEN_FONT_VOID.get(), new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));
    public static final RegistryObject<ManaFontBlockItem> FORBIDDEN_FONT_HALLOWED = registerSupplier("forbidden_font_hallowed", () -> new ManaFontBlockItem(BlocksPM.FORBIDDEN_FONT_HALLOWED.get(), new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));
    public static final RegistryObject<ManaFontBlockItem> HEAVENLY_FONT_EARTH = registerSupplier("heavenly_font_earth", () -> new ManaFontBlockItem(BlocksPM.HEAVENLY_FONT_EARTH.get(), new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));
    public static final RegistryObject<ManaFontBlockItem> HEAVENLY_FONT_SEA = registerSupplier("heavenly_font_sea", () -> new ManaFontBlockItem(BlocksPM.HEAVENLY_FONT_SEA.get(), new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));
    public static final RegistryObject<ManaFontBlockItem> HEAVENLY_FONT_SKY = registerSupplier("heavenly_font_sky", () -> new ManaFontBlockItem(BlocksPM.HEAVENLY_FONT_SKY.get(), new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));
    public static final RegistryObject<ManaFontBlockItem> HEAVENLY_FONT_SUN = registerSupplier("heavenly_font_sun", () -> new ManaFontBlockItem(BlocksPM.HEAVENLY_FONT_SUN.get(), new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));
    public static final RegistryObject<ManaFontBlockItem> HEAVENLY_FONT_MOON = registerSupplier("heavenly_font_moon", () -> new ManaFontBlockItem(BlocksPM.HEAVENLY_FONT_MOON.get(), new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));
    public static final RegistryObject<ManaFontBlockItem> HEAVENLY_FONT_BLOOD = registerSupplier("heavenly_font_blood", () -> new ManaFontBlockItem(BlocksPM.HEAVENLY_FONT_BLOOD.get(), new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));
    public static final RegistryObject<ManaFontBlockItem> HEAVENLY_FONT_INFERNAL = registerSupplier("heavenly_font_infernal", () -> new ManaFontBlockItem(BlocksPM.HEAVENLY_FONT_INFERNAL.get(), new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));
    public static final RegistryObject<ManaFontBlockItem> HEAVENLY_FONT_VOID = registerSupplier("heavenly_font_void", () -> new ManaFontBlockItem(BlocksPM.HEAVENLY_FONT_VOID.get(), new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));
    public static final RegistryObject<ManaFontBlockItem> HEAVENLY_FONT_HALLOWED = registerSupplier("heavenly_font_hallowed", () -> new ManaFontBlockItem(BlocksPM.HEAVENLY_FONT_HALLOWED.get(), new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));
    
    // Register device block items
    public static final RegistryObject<BurnableBlockItem> ARCANE_WORKBENCH = registerSupplier("arcane_workbench", () -> new BurnableBlockItem(BlocksPM.ARCANE_WORKBENCH.get(), 300, new Item.Properties()));
    public static final RegistryObject<BlockItem> WAND_ASSEMBLY_TABLE = registerSupplier("wand_assembly_table", () -> new BlockItem(BlocksPM.WAND_ASSEMBLY_TABLE.get(), new Item.Properties()));
    public static final RegistryObject<BurnableBlockItem> WOOD_TABLE = registerSupplier("wood_table", () -> new BurnableBlockItem(BlocksPM.WOOD_TABLE.get(), 300, new Item.Properties()));
    public static final RegistryObject<BurnableBlockItem> ANALYSIS_TABLE = registerSupplier("analysis_table", () -> new BurnableBlockItem(BlocksPM.ANALYSIS_TABLE.get(), 300, new Item.Properties()));
    public static final RegistryObject<BlockItem> ESSENCE_FURNACE = registerSupplier("essence_furnace", () -> new BlockItem(BlocksPM.ESSENCE_FURNACE.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> CALCINATOR_BASIC = registerSupplier("calcinator_basic", () -> new BlockItem(BlocksPM.CALCINATOR_BASIC.get(), new Item.Properties().rarity(Rarity.COMMON)));
    public static final RegistryObject<BlockItem> CALCINATOR_ENCHANTED = registerSupplier("calcinator_enchanted", () -> new BlockItem(BlocksPM.CALCINATOR_ENCHANTED.get(), new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<BlockItem> CALCINATOR_FORBIDDEN = registerSupplier("calcinator_forbidden", () -> new BlockItem(BlocksPM.CALCINATOR_FORBIDDEN.get(), new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<BlockItem> CALCINATOR_HEAVENLY = registerSupplier("calcinator_heavenly", () -> new BlockItem(BlocksPM.CALCINATOR_HEAVENLY.get(), new Item.Properties().rarity(Rarity.EPIC)));
    public static final RegistryObject<BurnableBlockItem> WAND_INSCRIPTION_TABLE = registerSupplier("wand_inscription_table", () -> new BurnableBlockItem(BlocksPM.WAND_INSCRIPTION_TABLE.get(), 300, new Item.Properties()));
    public static final RegistryObject<SpellcraftingAltarBlockItem> SPELLCRAFTING_ALTAR = registerSupplier("spellcrafting_altar", () -> new SpellcraftingAltarBlockItem(BlocksPM.SPELLCRAFTING_ALTAR.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> WAND_CHARGER = registerSupplier("wand_charger", () -> new BlockItem(BlocksPM.WAND_CHARGER.get(), new Item.Properties()));
    public static final RegistryObject<BurnableBlockItem> RESEARCH_TABLE = registerSupplier("research_table", () -> new BurnableBlockItem(BlocksPM.RESEARCH_TABLE.get(), 300, new Item.Properties()));
    public static final RegistryObject<BlockItem> SUNLAMP = registerSupplier("sunlamp", () -> new BlockItem(BlocksPM.SUNLAMP.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> SPIRIT_LANTERN = registerSupplier("spirit_lantern", () -> new BlockItem(BlocksPM.SPIRIT_LANTERN.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> RITUAL_ALTAR = registerSupplier("ritual_altar", () -> new BlockItem(BlocksPM.RITUAL_ALTAR.get(), new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<BlockItem> OFFERING_PEDESTAL = registerSupplier("offering_pedestal", () -> new BlockItem(BlocksPM.OFFERING_PEDESTAL.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> INCENSE_BRAZIER = registerSupplier("incense_brazier", () -> new BlockItem(BlocksPM.INCENSE_BRAZIER.get(), new Item.Properties()));
    public static final RegistryObject<BurnableBlockItem> RITUAL_LECTERN = registerSupplier("ritual_lectern", () -> new BurnableBlockItem(BlocksPM.RITUAL_LECTERN.get(), 300, new Item.Properties()));
    public static final RegistryObject<BlockItem> RITUAL_BELL = registerSupplier("ritual_bell", () -> new BlockItem(BlocksPM.RITUAL_BELL.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> BLOODLETTER = registerSupplier("bloodletter", () -> new BlockItem(BlocksPM.BLOODLETTER.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> SOUL_ANVIL = registerSupplier("soul_anvil", () -> new BlockItem(BlocksPM.SOUL_ANVIL.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> RUNESCRIBING_ALTAR_BASIC = registerSupplier("runescribing_altar_basic", () -> new BlockItem(BlocksPM.RUNESCRIBING_ALTAR_BASIC.get(), new Item.Properties().rarity(Rarity.COMMON)));
    public static final RegistryObject<BlockItem> RUNESCRIBING_ALTAR_ENCHANTED = registerSupplier("runescribing_altar_enchanted", () -> new BlockItem(BlocksPM.RUNESCRIBING_ALTAR_ENCHANTED.get(), new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<BlockItem> RUNESCRIBING_ALTAR_FORBIDDEN = registerSupplier("runescribing_altar_forbidden", () -> new BlockItem(BlocksPM.RUNESCRIBING_ALTAR_FORBIDDEN.get(), new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<BlockItem> RUNESCRIBING_ALTAR_HEAVENLY = registerSupplier("runescribing_altar_heavenly", () -> new BlockItem(BlocksPM.RUNESCRIBING_ALTAR_HEAVENLY.get(), new Item.Properties().rarity(Rarity.EPIC)));
    public static final RegistryObject<BurnableBlockItem> RUNECARVING_TABLE = registerSupplier("runecarving_table", () -> new BurnableBlockItem(BlocksPM.RUNECARVING_TABLE.get(), 300, new Item.Properties()));
    public static final RegistryObject<BlockItem> RUNIC_GRINDSTONE = registerSupplier("runic_grindstone", () -> new BlockItem(BlocksPM.RUNIC_GRINDSTONE.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> HONEY_EXTRACTOR = registerSupplier("honey_extractor", () -> new BlockItem(BlocksPM.HONEY_EXTRACTOR.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> PRIMALITE_GOLEM_CONTROLLER = registerSupplier("primalite_golem_controller", () -> new BlockItem(BlocksPM.PRIMALITE_GOLEM_CONTROLLER.get(), new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<BlockItem> HEXIUM_GOLEM_CONTROLLER = registerSupplier("hexium_golem_controller", () -> new BlockItem(BlocksPM.HEXIUM_GOLEM_CONTROLLER.get(), new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<BlockItem> HALLOWSTEEL_GOLEM_CONTROLLER = registerSupplier("hallowsteel_golem_controller", () -> new BlockItem(BlocksPM.HALLOWSTEEL_GOLEM_CONTROLLER.get(), new Item.Properties().rarity(Rarity.EPIC)));
    public static final RegistryObject<BlockItem> SANGUINE_CRUCIBLE = registerSupplier("sanguine_crucible", () -> new BlockItem(BlocksPM.SANGUINE_CRUCIBLE.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> CONCOCTER = registerSupplier("concocter", () -> new BlockItem(BlocksPM.CONCOCTER.get(), new Item.Properties()));
    public static final RegistryObject<BurnableBlockItem> CELESTIAL_HARP = registerSupplier("celestial_harp", () -> new BurnableBlockItem(BlocksPM.CELESTIAL_HARP.get(), 300, new Item.Properties()));
    public static final RegistryObject<BlockItem> ENTROPY_SINK = registerSupplier("entropy_sink", () -> new BlockItem(BlocksPM.ENTROPY_SINK.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> AUTO_CHARGER = registerSupplier("auto_charger", () -> new BlockItem(BlocksPM.AUTO_CHARGER.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> ESSENCE_TRANSMUTER = registerSupplier("essence_transmuter", () -> new BlockItem(BlocksPM.ESSENCE_TRANSMUTER.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> DISSOLUTION_CHAMBER = registerSupplier("dissolution_chamber", () -> new BlockItem(BlocksPM.DISSOLUTION_CHAMBER.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> ZEPHYR_ENGINE = registerSupplier("zephyr_engine", () -> new BlockItem(BlocksPM.ZEPHYR_ENGINE.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> VOID_TURBINE = registerSupplier("void_turbine", () -> new BlockItem(BlocksPM.VOID_TURBINE.get(), new Item.Properties()));
    public static final RegistryObject<BurnableBlockItem> ESSENCE_CASK_ENCHANTED = registerSupplier("essence_cask_enchanted", () -> new BurnableBlockItem(BlocksPM.ESSENCE_CASK_ENCHANTED.get(), 300, new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<BurnableBlockItem> ESSENCE_CASK_FORBIDDEN = registerSupplier("essence_cask_forbidden", () -> new BurnableBlockItem(BlocksPM.ESSENCE_CASK_FORBIDDEN.get(), 300, new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<BurnableBlockItem> ESSENCE_CASK_HEAVENLY = registerSupplier("essence_cask_heavenly", () -> new BurnableBlockItem(BlocksPM.ESSENCE_CASK_HEAVENLY.get(), 300, new Item.Properties().rarity(Rarity.EPIC)));
    public static final RegistryObject<BurnableBlockItem> WAND_GLAMOUR_TABLE = registerSupplier("wand_glamour_table", () -> new BurnableBlockItem(BlocksPM.WAND_GLAMOUR_TABLE.get(), 300, new Item.Properties()));
    public static final RegistryObject<BlockItem> INFERNAL_FURNACE = registerSupplier("infernal_furnace", () -> new BlockItem(BlocksPM.INFERNAL_FURNACE.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MANA_NEXUS = registerSupplier("mana_nexus", () -> new BlockItem(BlocksPM.MANA_NEXUS.get(), new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<BlockItem> MANA_SINGULARITY = registerSupplier("mana_singularity", () -> new BlockItem(BlocksPM.MANA_SINGULARITY.get(), new Item.Properties().rarity(Rarity.EPIC)));
    public static final RegistryObject<BlockItem> MANA_SINGULARITY_CREATIVE = registerSupplier("mana_singularity_creative", () -> new BlockItem(BlocksPM.MANA_SINGULARITY_CREATIVE.get(), new Item.Properties().rarity(Rarity.EPIC)));

    // Register miscellaneous block items
    public static final RegistryObject<ItemNameBlockItem> REFINED_SALT = registerSupplier("refined_salt", () -> new ItemNameBlockItem(BlocksPM.SALT_TRAIL.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> ROCK_SALT_ORE = registerSupplier("rock_salt_ore", () -> new BlockItem(BlocksPM.ROCK_SALT_ORE.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> QUARTZ_ORE = registerSupplier("quartz_ore", () -> new BlockItem(BlocksPM.QUARTZ_ORE.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> PRIMALITE_BLOCK = registerSupplier("primalite_block", () -> new BlockItem(BlocksPM.PRIMALITE_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> HEXIUM_BLOCK = registerSupplier("hexium_block", () -> new BlockItem(BlocksPM.HEXIUM_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> HALLOWSTEEL_BLOCK = registerSupplier("hallowsteel_block", () -> new BlockItem(BlocksPM.HALLOWSTEEL_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<BurnableBlockItem> IGNYX_BLOCK = registerSupplier("ignyx_block", () -> new BurnableBlockItem(BlocksPM.IGNYX_BLOCK.get(), 128000, new Item.Properties()));
    public static final RegistryObject<BlockItem> SALT_BLOCK = registerSupplier("salt_block", () -> new BlockItem(BlocksPM.SALT_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<ItemNameBlockItem> TREEFOLK_SEED = registerSupplier("treefolk_seed", () -> new ItemNameBlockItem(BlocksPM.TREEFOLK_SPROUT.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> ENDERWARD = registerSupplier("enderward", () -> new BlockItem(BlocksPM.ENDERWARD.get(), new Item.Properties().stacksTo(16)));
    
    // Register salted food items
    public static final RegistryObject<Item> SALTED_BAKED_POTATO = registerSupplier("salted_baked_potato", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationMod(0.72F).build())));
    public static final RegistryObject<Item> SALTED_COOKED_BEEF = registerSupplier("salted_cooked_beef", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(10).saturationMod(0.96F).meat().build())));
    public static final RegistryObject<Item> SALTED_COOKED_CHICKEN = registerSupplier("salted_cooked_chicken", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationMod(0.72F).meat().build())));
    public static final RegistryObject<Item> SALTED_COOKED_COD = registerSupplier("salted_cooked_cod", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationMod(0.72F).build())));
    public static final RegistryObject<Item> SALTED_COOKED_MUTTON = registerSupplier("salted_cooked_mutton", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationMod(0.96F).meat().build())));
    public static final RegistryObject<Item> SALTED_COOKED_PORKCHOP = registerSupplier("salted_cooked_porkchop", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(10).saturationMod(0.96F).meat().build())));
    public static final RegistryObject<Item> SALTED_COOKED_RABBIT = registerSupplier("salted_cooked_rabbit", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationMod(0.72F).meat().build())));
    public static final RegistryObject<Item> SALTED_COOKED_SALMON = registerSupplier("salted_cooked_salmon", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationMod(0.96F).build())));
    public static final RegistryObject<BowlFoodItem> SALTED_BEETROOT_SOUP = registerSupplier("salted_beetroot_soup", () -> new BowlFoodItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(7).saturationMod(0.72F).build())));
    public static final RegistryObject<BowlFoodItem> SALTED_MUSHROOM_STEW = registerSupplier("salted_mushroom_stew", () -> new BowlFoodItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(7).saturationMod(0.72F).build())));
    public static final RegistryObject<BowlFoodItem> SALTED_RABBIT_STEW = registerSupplier("salted_rabbit_stew", () -> new BowlFoodItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(12).saturationMod(0.72F).build())));
    
    // Register mineral items
    public static final RegistryObject<Item> IRON_GRIT = registerSupplier("iron_grit", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GOLD_GRIT = registerSupplier("gold_grit", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> COPPER_GRIT = registerSupplier("copper_grit", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PRIMALITE_INGOT = registerSupplier("primalite_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> HEXIUM_INGOT = registerSupplier("hexium_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> HALLOWSTEEL_INGOT = registerSupplier("hallowsteel_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PRIMALITE_NUGGET = registerSupplier("primalite_nugget", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> HEXIUM_NUGGET = registerSupplier("hexium_nugget", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> HALLOWSTEEL_NUGGET = registerSupplier("hallowsteel_nugget", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> QUARTZ_NUGGET = registerSupplier("quartz_nugget", () -> new Item(new Item.Properties()));
    public static final RegistryObject<EnergizedGemItem> ENERGIZED_AMETHYST = registerSupplier("energized_amethyst", () -> new EnergizedGemItem(new Item.Properties()));
    public static final RegistryObject<EnergizedGemItem> ENERGIZED_DIAMOND = registerSupplier("energized_diamond", () -> new EnergizedGemItem(new Item.Properties()));
    public static final RegistryObject<EnergizedGemItem> ENERGIZED_EMERALD = registerSupplier("energized_emerald", () -> new EnergizedGemItem(new Item.Properties()));
    public static final RegistryObject<EnergizedGemItem> ENERGIZED_QUARTZ = registerSupplier("energized_quartz", () -> new EnergizedGemItem(new Item.Properties()));
    
    // Register tool items
    public static final RegistryObject<SwordItem> PRIMALITE_SWORD = registerSupplier("primalite_sword", () -> new SwordItem(ItemTierPM.PRIMALITE, 3, -2.4F, new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<PrimaliteTridentItem> PRIMALITE_TRIDENT = registerSupplier("primalite_trident", () -> new PrimaliteTridentItem(new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<TieredBowItem> PRIMALITE_BOW = registerSupplier("primalite_bow", () -> new TieredBowItem(ItemTierPM.PRIMALITE, new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<ShovelItem> PRIMALITE_SHOVEL = registerSupplier("primalite_shovel", () -> new ShovelItem(ItemTierPM.PRIMALITE, 1.5F, -3.0F, new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<PickaxeItem> PRIMALITE_PICKAXE = registerSupplier("primalite_pickaxe", () -> new PickaxeItem(ItemTierPM.PRIMALITE, 1, -2.8F, new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<AxeItem> PRIMALITE_AXE = registerSupplier("primalite_axe", () -> new AxeItem(ItemTierPM.PRIMALITE, 5.5F, -3.0F, new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<HoeItem> PRIMALITE_HOE = registerSupplier("primalite_hoe", () -> new HoeItem(ItemTierPM.PRIMALITE, -2, 0.0F, new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<TieredFishingRodItem> PRIMALITE_FISHING_ROD = registerSupplier("primalite_fishing_rod", () -> new TieredFishingRodItem(ItemTierPM.PRIMALITE, new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<PrimaliteShieldItem> PRIMALITE_SHIELD = registerSupplier("primalite_shield", () -> new PrimaliteShieldItem(new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<SwordItem> HEXIUM_SWORD = registerSupplier("hexium_sword", () -> new SwordItem(ItemTierPM.HEXIUM, 3, -2.4F, new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<HexiumTridentItem> HEXIUM_TRIDENT = registerSupplier("hexium_trident", () -> new HexiumTridentItem(new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<TieredBowItem> HEXIUM_BOW = registerSupplier("hexium_bow", () -> new TieredBowItem(ItemTierPM.HEXIUM, new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<ShovelItem> HEXIUM_SHOVEL = registerSupplier("hexium_shovel", () -> new ShovelItem(ItemTierPM.HEXIUM, 1.5F, -3.0F, new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<PickaxeItem> HEXIUM_PICKAXE = registerSupplier("hexium_pickaxe", () -> new PickaxeItem(ItemTierPM.HEXIUM, 1, -2.8F, new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<AxeItem> HEXIUM_AXE = registerSupplier("hexium_axe", () -> new AxeItem(ItemTierPM.HEXIUM, 4.0F, -3.0F, new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<HoeItem> HEXIUM_HOE = registerSupplier("hexium_hoe", () -> new HoeItem(ItemTierPM.HEXIUM, -4, 0.0F, new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<TieredFishingRodItem> HEXIUM_FISHING_ROD = registerSupplier("hexium_fishing_rod", () -> new TieredFishingRodItem(ItemTierPM.HEXIUM, new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<HexiumShieldItem> HEXIUM_SHIELD = registerSupplier("hexium_shield", () -> new HexiumShieldItem(new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<SwordItem> HALLOWSTEEL_SWORD = registerSupplier("hallowsteel_sword", () -> new SwordItem(ItemTierPM.HALLOWSTEEL, 3, -2.4F, new Item.Properties().rarity(Rarity.EPIC)));
    public static final RegistryObject<HallowsteelTridentItem> HALLOWSTEEL_TRIDENT = registerSupplier("hallowsteel_trident", () -> new HallowsteelTridentItem(new Item.Properties().rarity(Rarity.EPIC)));
    public static final RegistryObject<TieredBowItem> HALLOWSTEEL_BOW = registerSupplier("hallowsteel_bow", () -> new TieredBowItem(ItemTierPM.HALLOWSTEEL, new Item.Properties().rarity(Rarity.EPIC)));
    public static final RegistryObject<ShovelItem> HALLOWSTEEL_SHOVEL = registerSupplier("hallowsteel_shovel", () -> new ShovelItem(ItemTierPM.HALLOWSTEEL, 1.5F, -3.0F, new Item.Properties().rarity(Rarity.EPIC)));
    public static final RegistryObject<PickaxeItem> HALLOWSTEEL_PICKAXE = registerSupplier("hallowsteel_pickaxe", () -> new PickaxeItem(ItemTierPM.HALLOWSTEEL, 1, -2.8F, new Item.Properties().rarity(Rarity.EPIC)));
    public static final RegistryObject<AxeItem> HALLOWSTEEL_AXE = registerSupplier("hallowsteel_axe", () -> new AxeItem(ItemTierPM.HALLOWSTEEL, 3.5F, -3.0F, new Item.Properties().rarity(Rarity.EPIC)));
    public static final RegistryObject<HoeItem> HALLOWSTEEL_HOE = registerSupplier("hallowsteel_hoe", () -> new HoeItem(ItemTierPM.HALLOWSTEEL, -5, 0.0F, new Item.Properties().rarity(Rarity.EPIC)));
    public static final RegistryObject<TieredFishingRodItem> HALLOWSTEEL_FISHING_ROD = registerSupplier("hallowsteel_fishing_rod", () -> new TieredFishingRodItem(ItemTierPM.HALLOWSTEEL, new Item.Properties().rarity(Rarity.EPIC)));
    public static final RegistryObject<HallowsteelShieldItem> HALLOWSTEEL_SHIELD = registerSupplier("hallowsteel_shield", () -> new HallowsteelShieldItem(new Item.Properties().rarity(Rarity.EPIC)));
    public static final RegistryObject<PrimalShovelItem> PRIMAL_SHOVEL = registerDefaultInstance("primal_shovel", () -> new PrimalShovelItem(ItemTierPM.PRIMALITE, 1.5F, -3.0F, new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<PrimalFishingRodItem> PRIMAL_FISHING_ROD = registerDefaultInstance("primal_fishing_rod", () -> new PrimalFishingRodItem(ItemTierPM.PRIMALITE, new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<PrimalAxeItem> PRIMAL_AXE = registerDefaultInstance("primal_axe", () -> new PrimalAxeItem(ItemTierPM.PRIMALITE, 5.5F, -3.0F, new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<PrimalHoeItem> PRIMAL_HOE = registerDefaultInstance("primal_hoe", () -> new PrimalHoeItem(ItemTierPM.PRIMALITE, -2, 0.0F, new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<PrimalPickaxeItem> PRIMAL_PICKAXE = registerDefaultInstance("primal_pickaxe", () -> new PrimalPickaxeItem(ItemTierPM.PRIMALITE, 1, -2.8F, new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<ForbiddenTridentItem> FORBIDDEN_TRIDENT = registerDefaultInstance("forbidden_trident", () -> new ForbiddenTridentItem(new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<ForbiddenBowItem> FORBIDDEN_BOW = registerDefaultInstance("forbidden_bow", () -> new ForbiddenBowItem(new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<ForbiddenSwordItem> FORBIDDEN_SWORD = registerDefaultInstance("forbidden_sword", () -> new ForbiddenSwordItem(ItemTierPM.HEXIUM, 3, -2.4F, new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<SacredShieldItem> SACRED_SHIELD = registerDefaultInstance("sacred_shield", () -> new SacredShieldItem(new Item.Properties().rarity(Rarity.EPIC)));
    
    // Register mana arrow items
    public static final RegistryObject<ManaArrowItem> MANA_ARROW_EARTH = registerSupplier("mana_arrow_earth", () -> new ManaArrowItem(Source.EARTH, new Item.Properties()));
    public static final RegistryObject<ManaArrowItem> MANA_ARROW_SEA = registerSupplier("mana_arrow_sea", () -> new ManaArrowItem(Source.SEA, new Item.Properties()));
    public static final RegistryObject<ManaArrowItem> MANA_ARROW_SKY = registerSupplier("mana_arrow_sky", () -> new ManaArrowItem(Source.SKY, new Item.Properties()));
    public static final RegistryObject<ManaArrowItem> MANA_ARROW_SUN = registerSupplier("mana_arrow_sun", () -> new ManaArrowItem(Source.SUN, new Item.Properties()));
    public static final RegistryObject<ManaArrowItem> MANA_ARROW_MOON = registerSupplier("mana_arrow_moon", () -> new ManaArrowItem(Source.MOON, new Item.Properties()));
    public static final RegistryObject<ManaArrowItem> MANA_ARROW_BLOOD = registerSupplier("mana_arrow_blood", () -> new ManaArrowItem(Source.BLOOD, new Item.Properties()));
    public static final RegistryObject<ManaArrowItem> MANA_ARROW_INFERNAL = registerSupplier("mana_arrow_infernal", () -> new ManaArrowItem(Source.INFERNAL, new Item.Properties()));
    public static final RegistryObject<ManaArrowItem> MANA_ARROW_VOID = registerSupplier("mana_arrow_void", () -> new ManaArrowItem(Source.VOID, new Item.Properties()));
    public static final RegistryObject<ManaArrowItem> MANA_ARROW_HALLOWED = registerSupplier("mana_arrow_hallowed", () -> new ManaArrowItem(Source.HALLOWED, new Item.Properties()));
    
    // Register armor items
    public static final RegistryObject<RobeArmorItem> IMBUED_WOOL_HEAD = registerSupplier("imbued_wool_head", () -> new RobeArmorItem(ArmorMaterialPM.IMBUED_WOOL, ArmorItem.Type.HELMET, 1, new Item.Properties().rarity(Rarity.COMMON)));
    public static final RegistryObject<RobeArmorItem> IMBUED_WOOL_CHEST = registerSupplier("imbued_wool_chest", () -> new RobeArmorItem(ArmorMaterialPM.IMBUED_WOOL, ArmorItem.Type.CHESTPLATE, 2, new Item.Properties().rarity(Rarity.COMMON)));
    public static final RegistryObject<RobeArmorItem> IMBUED_WOOL_LEGS = registerSupplier("imbued_wool_legs", () -> new RobeArmorItem(ArmorMaterialPM.IMBUED_WOOL, ArmorItem.Type.LEGGINGS, 1, new Item.Properties().rarity(Rarity.COMMON)));
    public static final RegistryObject<RobeArmorItem> IMBUED_WOOL_FEET = registerSupplier("imbued_wool_feet", () -> new RobeArmorItem(ArmorMaterialPM.IMBUED_WOOL, ArmorItem.Type.BOOTS, 1, new Item.Properties().rarity(Rarity.COMMON)));
    public static final RegistryObject<RobeArmorItem> SPELLCLOTH_HEAD = registerSupplier("spellcloth_head", () -> new RobeArmorItem(ArmorMaterialPM.SPELLCLOTH, ArmorItem.Type.HELMET, 2, new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<RobeArmorItem> SPELLCLOTH_CHEST = registerSupplier("spellcloth_chest", () -> new RobeArmorItem(ArmorMaterialPM.SPELLCLOTH, ArmorItem.Type.CHESTPLATE, 3, new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<RobeArmorItem> SPELLCLOTH_LEGS = registerSupplier("spellcloth_legs", () -> new RobeArmorItem(ArmorMaterialPM.SPELLCLOTH, ArmorItem.Type.LEGGINGS, 3, new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<RobeArmorItem> SPELLCLOTH_FEET = registerSupplier("spellcloth_feet", () -> new RobeArmorItem(ArmorMaterialPM.SPELLCLOTH, ArmorItem.Type.BOOTS, 2, new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<RobeArmorItem> HEXWEAVE_HEAD = registerSupplier("hexweave_head", () -> new RobeArmorItem(ArmorMaterialPM.HEXWEAVE, ArmorItem.Type.HELMET, 3, new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<RobeArmorItem> HEXWEAVE_CHEST = registerSupplier("hexweave_chest", () -> new RobeArmorItem(ArmorMaterialPM.HEXWEAVE, ArmorItem.Type.CHESTPLATE, 5, new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<RobeArmorItem> HEXWEAVE_LEGS = registerSupplier("hexweave_legs", () -> new RobeArmorItem(ArmorMaterialPM.HEXWEAVE, ArmorItem.Type.LEGGINGS, 4, new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<RobeArmorItem> HEXWEAVE_FEET = registerSupplier("hexweave_feet", () -> new RobeArmorItem(ArmorMaterialPM.HEXWEAVE, ArmorItem.Type.BOOTS, 3, new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<RobeArmorItem> SAINTSWOOL_HEAD = registerSupplier("saintswool_head", () -> new RobeArmorItem(ArmorMaterialPM.SAINTSWOOL, ArmorItem.Type.HELMET, 4, new Item.Properties().rarity(Rarity.EPIC)));
    public static final RegistryObject<RobeArmorItem> SAINTSWOOL_CHEST = registerSupplier("saintswool_chest", () -> new RobeArmorItem(ArmorMaterialPM.SAINTSWOOL, ArmorItem.Type.CHESTPLATE, 6, new Item.Properties().rarity(Rarity.EPIC)));
    public static final RegistryObject<RobeArmorItem> SAINTSWOOL_LEGS = registerSupplier("saintswool_legs", () -> new RobeArmorItem(ArmorMaterialPM.SAINTSWOOL, ArmorItem.Type.LEGGINGS, 6, new Item.Properties().rarity(Rarity.EPIC)));
    public static final RegistryObject<RobeArmorItem> SAINTSWOOL_FEET = registerSupplier("saintswool_feet", () -> new RobeArmorItem(ArmorMaterialPM.SAINTSWOOL, ArmorItem.Type.BOOTS, 4, new Item.Properties().rarity(Rarity.EPIC)));
    public static final RegistryObject<ArmorItem> PRIMALITE_HEAD = registerSupplier("primalite_head", () -> new ArmorItem(ArmorMaterialPM.PRIMALITE, ArmorItem.Type.HELMET, new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<ArmorItem> PRIMALITE_CHEST = registerSupplier("primalite_chest", () -> new ArmorItem(ArmorMaterialPM.PRIMALITE, ArmorItem.Type.CHESTPLATE, new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<ArmorItem> PRIMALITE_LEGS = registerSupplier("primalite_legs", () -> new ArmorItem(ArmorMaterialPM.PRIMALITE, ArmorItem.Type.LEGGINGS, new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<ArmorItem> PRIMALITE_FEET = registerSupplier("primalite_feet", () -> new ArmorItem(ArmorMaterialPM.PRIMALITE, ArmorItem.Type.BOOTS, new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<ArmorItem> HEXIUM_HEAD = registerSupplier("hexium_head", () -> new ArmorItem(ArmorMaterialPM.HEXIUM, ArmorItem.Type.HELMET, new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<ArmorItem> HEXIUM_CHEST = registerSupplier("hexium_chest", () -> new ArmorItem(ArmorMaterialPM.HEXIUM, ArmorItem.Type.CHESTPLATE, new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<ArmorItem> HEXIUM_LEGS = registerSupplier("hexium_legs", () -> new ArmorItem(ArmorMaterialPM.HEXIUM, ArmorItem.Type.LEGGINGS, new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<ArmorItem> HEXIUM_FEET = registerSupplier("hexium_feet", () -> new ArmorItem(ArmorMaterialPM.HEXIUM, ArmorItem.Type.BOOTS, new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<ArmorItem> HALLOWSTEEL_HEAD = registerSupplier("hallowsteel_head", () -> new ArmorItem(ArmorMaterialPM.HALLOWSTEEL, ArmorItem.Type.HELMET, new Item.Properties().rarity(Rarity.EPIC)));
    public static final RegistryObject<ArmorItem> HALLOWSTEEL_CHEST = registerSupplier("hallowsteel_chest", () -> new ArmorItem(ArmorMaterialPM.HALLOWSTEEL, ArmorItem.Type.CHESTPLATE, new Item.Properties().rarity(Rarity.EPIC)));
    public static final RegistryObject<ArmorItem> HALLOWSTEEL_LEGS = registerSupplier("hallowsteel_legs", () -> new ArmorItem(ArmorMaterialPM.HALLOWSTEEL, ArmorItem.Type.LEGGINGS, new Item.Properties().rarity(Rarity.EPIC)));
    public static final RegistryObject<ArmorItem> HALLOWSTEEL_FEET = registerSupplier("hallowsteel_feet", () -> new ArmorItem(ArmorMaterialPM.HALLOWSTEEL, ArmorItem.Type.BOOTS, new Item.Properties().rarity(Rarity.EPIC)));
    
    // Register miscellaneous items
    public static final RegistryObject<ArcanometerItem> ARCANOMETER = registerSupplier("arcanometer", ArcanometerItem::new);
    public static final RegistryObject<Item> MAGNIFYING_GLASS = registerSupplier("magnifying_glass", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ALCHEMICAL_WASTE = registerSupplier("alchemical_waste", () -> new Item(new Item.Properties()));
    public static final RegistryObject<BloodyFleshItem> BLOODY_FLESH = registerSupplier("bloody_flesh", BloodyFleshItem::new);
    public static final RegistryObject<HallowedOrbItem> HALLOWED_ORB = registerSupplier("hallowed_orb", HallowedOrbItem::new);
    public static final RegistryObject<Item> HEARTWOOD = registerSupplier("heartwood", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ENCHANTED_INK = registerSupplier("enchanted_ink", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ENCHANTED_INK_AND_QUILL = registerSupplier("enchanted_ink_and_quill", EnchantedInkAndQuillItem::new);
    public static final RegistryObject<SeascribePenItem> SEASCRIBE_PEN = registerSupplier("seascribe_pen", () -> new SeascribePenItem(new Item.Properties()));
    public static final RegistryObject<Item> ROCK_SALT = registerSupplier("rock_salt", () -> new Item(new Item.Properties()));
    public static final RegistryObject<EarthshatterHammerItem> EARTHSHATTER_HAMMER = registerSupplier("earthshatter_hammer", EarthshatterHammerItem::new);
    public static final RegistryObject<Item> MANA_PRISM = registerSupplier("mana_prism", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> TALLOW = registerSupplier("tallow", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> BEESWAX = registerSupplier("beeswax", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> MANA_SALTS = registerSupplier("mana_salts", () -> new Item(new Item.Properties()));
    public static final RegistryObject<ManafruitItem> MANAFRUIT = registerSupplier("manafruit", () -> new ManafruitItem(0, new Item.Properties().food(new FoodProperties.Builder().nutrition(8).saturationMod(0.8F).build())));
    public static final RegistryObject<Item> INCENSE_STICK = registerSupplier("incense_stick", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SOUL_GEM = registerSupplier("soul_gem", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SOUL_GEM_SLIVER = registerSupplier("soul_gem_sliver", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SPELLCLOTH = registerSupplier("spellcloth", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> HEXWEAVE = registerSupplier("hexweave", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SAINTSWOOL = registerSupplier("saintswool", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> MAGITECH_PARTS_BASIC = registerSupplier("magitech_parts_basic", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> MAGITECH_PARTS_ENCHANTED = registerSupplier("magitech_parts_enchanted", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> MAGITECH_PARTS_FORBIDDEN = registerSupplier("magitech_parts_forbidden", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> MAGITECH_PARTS_HEAVENLY = registerSupplier("magitech_parts_heavenly", () -> new Item(new Item.Properties()));
    public static final RegistryObject<FlyingCarpetItem> FLYING_CARPET = registerSupplier("flying_carpet", () -> new FlyingCarpetItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<DreamVisionTalismanItem> DREAM_VISION_TALISMAN = registerSupplier("dream_vision_talisman", DreamVisionTalismanItem::new);
    public static final RegistryObject<IgnyxItem> IGNYX = registerSupplier("ignyx", () -> new IgnyxItem(new Item.Properties()));
    public static final RegistryObject<DowsingRodItem> DOWSING_ROD = registerSupplier("dowsing_rod", () -> new DowsingRodItem(new Item.Properties().stacksTo(1).durability(63)));
    public static final RegistryObject<Item> FOUR_LEAF_CLOVER = registerSupplier("four_leaf_clover", () -> new Item(new Item.Properties()));
    public static final RegistryObject<RecallStoneItem> RECALL_STONE = registerSupplier("recall_stone", () -> new RecallStoneItem(new Item.Properties()));
    public static final RegistryObject<SmithingTemplateItem> RUNIC_ARMOR_TRIM_SMITHING_TEMPLATE = registerSupplier("runic_armor_trim_smithing_template", () -> TrimPatternsPM.createRunicArmorTrimTemplate(TrimPatternsPM.RUNIC));
    public static final RegistryObject<WardingModuleItem> BASIC_WARDING_MODULE = registerSupplier("warding_module_basic", () -> new WardingModuleItem(DeviceTier.ENCHANTED, new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<WardingModuleItem> GREATER_WARDING_MODULE = registerSupplier("warding_module_greater", () -> new WardingModuleItem(DeviceTier.FORBIDDEN, new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<WardingModuleItem> SUPREME_WARDING_MODULE = registerSupplier("warding_module_supreme", () -> new WardingModuleItem(DeviceTier.HEAVENLY, new Item.Properties().rarity(Rarity.EPIC)));
    
    // Register knowledge items
    public static final RegistryObject<KnowledgeGainItem> OBSERVATION_NOTES = registerSupplier("observation_notes", () -> new KnowledgeGainItem(KnowledgeType.OBSERVATION, KnowledgeType.OBSERVATION.getProgression(), new Item.Properties()));
    public static final RegistryObject<KnowledgeGainItem> THEORY_NOTES = registerSupplier("theory_notes", () -> new KnowledgeGainItem(KnowledgeType.THEORY, KnowledgeType.THEORY.getProgression(), new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<KnowledgeGainItem> MYSTICAL_RELIC = registerSupplier("mystical_relic", () -> new KnowledgeGainItem(KnowledgeType.THEORY, KnowledgeType.THEORY.getProgression(), new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> MYSTICAL_RELIC_FRAGMENT = registerSupplier("mystical_relic_fragment", () -> new Item(new Item.Properties()));
    public static final RegistryObject<ForbiddenSourceGainItem> BLOOD_NOTES = registerSupplier("blood_notes", () -> new ForbiddenSourceGainItem(Source.BLOOD, new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<ResearchGainItem> SHEEP_TOME = registerSupplier("sheep_tome", () -> new ResearchGainItem(ResearchNames.simpleKey(ResearchNames.SPELL_PAYLOAD_POLYMORPH_SHEEP), new Item.Properties().rarity(Rarity.UNCOMMON)));
    
    // Register dust essence items
    public static final RegistryObject<EssenceItem> ESSENCE_DUST_EARTH = registerSupplier("essence_dust_earth", () -> new EssenceItem(EssenceType.DUST, Source.EARTH));
    public static final RegistryObject<EssenceItem> ESSENCE_DUST_SEA = registerSupplier("essence_dust_sea", () -> new EssenceItem(EssenceType.DUST, Source.SEA));
    public static final RegistryObject<EssenceItem> ESSENCE_DUST_SKY = registerSupplier("essence_dust_sky", () -> new EssenceItem(EssenceType.DUST, Source.SKY));
    public static final RegistryObject<EssenceItem> ESSENCE_DUST_SUN = registerSupplier("essence_dust_sun", () -> new EssenceItem(EssenceType.DUST, Source.SUN));
    public static final RegistryObject<EssenceItem> ESSENCE_DUST_MOON = registerSupplier("essence_dust_moon", () -> new EssenceItem(EssenceType.DUST, Source.MOON));
    public static final RegistryObject<EssenceItem> ESSENCE_DUST_BLOOD = registerSupplier("essence_dust_blood", () -> new EssenceItem(EssenceType.DUST, Source.BLOOD));
    public static final RegistryObject<EssenceItem> ESSENCE_DUST_INFERNAL = registerSupplier("essence_dust_infernal", () -> new EssenceItem(EssenceType.DUST, Source.INFERNAL));
    public static final RegistryObject<EssenceItem> ESSENCE_DUST_VOID = registerSupplier("essence_dust_void", () -> new EssenceItem(EssenceType.DUST, Source.VOID));
    public static final RegistryObject<EssenceItem> ESSENCE_DUST_HALLOWED = registerSupplier("essence_dust_hallowed", () -> new EssenceItem(EssenceType.DUST, Source.HALLOWED));

    // Register shard essence items
    public static final RegistryObject<EssenceItem> ESSENCE_SHARD_EARTH = registerSupplier("essence_shard_earth", () -> new EssenceItem(EssenceType.SHARD, Source.EARTH));
    public static final RegistryObject<EssenceItem> ESSENCE_SHARD_SEA = registerSupplier("essence_shard_sea", () -> new EssenceItem(EssenceType.SHARD, Source.SEA));
    public static final RegistryObject<EssenceItem> ESSENCE_SHARD_SKY = registerSupplier("essence_shard_sky", () -> new EssenceItem(EssenceType.SHARD, Source.SKY));
    public static final RegistryObject<EssenceItem> ESSENCE_SHARD_SUN = registerSupplier("essence_shard_sun", () -> new EssenceItem(EssenceType.SHARD, Source.SUN));
    public static final RegistryObject<EssenceItem> ESSENCE_SHARD_MOON = registerSupplier("essence_shard_moon", () -> new EssenceItem(EssenceType.SHARD, Source.MOON));
    public static final RegistryObject<EssenceItem> ESSENCE_SHARD_BLOOD = registerSupplier("essence_shard_blood", () -> new EssenceItem(EssenceType.SHARD, Source.BLOOD));
    public static final RegistryObject<EssenceItem> ESSENCE_SHARD_INFERNAL = registerSupplier("essence_shard_infernal", () -> new EssenceItem(EssenceType.SHARD, Source.INFERNAL));
    public static final RegistryObject<EssenceItem> ESSENCE_SHARD_VOID = registerSupplier("essence_shard_void", () -> new EssenceItem(EssenceType.SHARD, Source.VOID));
    public static final RegistryObject<EssenceItem> ESSENCE_SHARD_HALLOWED = registerSupplier("essence_shard_hallowed", () -> new EssenceItem(EssenceType.SHARD, Source.HALLOWED));

    // Register crystal essence items
    public static final RegistryObject<EssenceItem> ESSENCE_CRYSTAL_EARTH = registerSupplier("essence_crystal_earth", () -> new EssenceItem(EssenceType.CRYSTAL, Source.EARTH));
    public static final RegistryObject<EssenceItem> ESSENCE_CRYSTAL_SEA = registerSupplier("essence_crystal_sea", () -> new EssenceItem(EssenceType.CRYSTAL, Source.SEA));
    public static final RegistryObject<EssenceItem> ESSENCE_CRYSTAL_SKY = registerSupplier("essence_crystal_sky", () -> new EssenceItem(EssenceType.CRYSTAL, Source.SKY));
    public static final RegistryObject<EssenceItem> ESSENCE_CRYSTAL_SUN = registerSupplier("essence_crystal_sun", () -> new EssenceItem(EssenceType.CRYSTAL, Source.SUN));
    public static final RegistryObject<EssenceItem> ESSENCE_CRYSTAL_MOON = registerSupplier("essence_crystal_moon", () -> new EssenceItem(EssenceType.CRYSTAL, Source.MOON));
    public static final RegistryObject<EssenceItem> ESSENCE_CRYSTAL_BLOOD = registerSupplier("essence_crystal_blood", () -> new EssenceItem(EssenceType.CRYSTAL, Source.BLOOD));
    public static final RegistryObject<EssenceItem> ESSENCE_CRYSTAL_INFERNAL = registerSupplier("essence_crystal_infernal", () -> new EssenceItem(EssenceType.CRYSTAL, Source.INFERNAL));
    public static final RegistryObject<EssenceItem> ESSENCE_CRYSTAL_VOID = registerSupplier("essence_crystal_void", () -> new EssenceItem(EssenceType.CRYSTAL, Source.VOID));
    public static final RegistryObject<EssenceItem> ESSENCE_CRYSTAL_HALLOWED = registerSupplier("essence_crystal_hallowed", () -> new EssenceItem(EssenceType.CRYSTAL, Source.HALLOWED));

    // Register cluster essence items
    public static final RegistryObject<EssenceItem> ESSENCE_CLUSTER_EARTH = registerSupplier("essence_cluster_earth", () -> new EssenceItem(EssenceType.CLUSTER, Source.EARTH));
    public static final RegistryObject<EssenceItem> ESSENCE_CLUSTER_SEA = registerSupplier("essence_cluster_sea", () -> new EssenceItem(EssenceType.CLUSTER, Source.SEA));
    public static final RegistryObject<EssenceItem> ESSENCE_CLUSTER_SKY = registerSupplier("essence_cluster_sky", () -> new EssenceItem(EssenceType.CLUSTER, Source.SKY));
    public static final RegistryObject<EssenceItem> ESSENCE_CLUSTER_SUN = registerSupplier("essence_cluster_sun", () -> new EssenceItem(EssenceType.CLUSTER, Source.SUN));
    public static final RegistryObject<EssenceItem> ESSENCE_CLUSTER_MOON = registerSupplier("essence_cluster_moon", () -> new EssenceItem(EssenceType.CLUSTER, Source.MOON));
    public static final RegistryObject<EssenceItem> ESSENCE_CLUSTER_BLOOD = registerSupplier("essence_cluster_blood", () -> new EssenceItem(EssenceType.CLUSTER, Source.BLOOD));
    public static final RegistryObject<EssenceItem> ESSENCE_CLUSTER_INFERNAL = registerSupplier("essence_cluster_infernal", () -> new EssenceItem(EssenceType.CLUSTER, Source.INFERNAL));
    public static final RegistryObject<EssenceItem> ESSENCE_CLUSTER_VOID = registerSupplier("essence_cluster_void", () -> new EssenceItem(EssenceType.CLUSTER, Source.VOID));
    public static final RegistryObject<EssenceItem> ESSENCE_CLUSTER_HALLOWED = registerSupplier("essence_cluster_hallowed", () -> new EssenceItem(EssenceType.CLUSTER, Source.HALLOWED));
    
    // Register rune items
    public static final RegistryObject<Item> RUNE_UNATTUNED = registerSupplier("rune_unattuned", () -> new Item(new Item.Properties()));
    public static final RegistryObject<RuneItem> RUNE_EARTH = registerSupplier("rune_earth", () -> new RuneItem(Rune.EARTH));
    public static final RegistryObject<RuneItem> RUNE_SEA = registerSupplier("rune_sea", () -> new RuneItem(Rune.SEA));
    public static final RegistryObject<RuneItem> RUNE_SKY = registerSupplier("rune_sky", () -> new RuneItem(Rune.SKY));
    public static final RegistryObject<RuneItem> RUNE_SUN = registerSupplier("rune_sun", () -> new RuneItem(Rune.SUN));
    public static final RegistryObject<RuneItem> RUNE_MOON = registerSupplier("rune_moon", () -> new RuneItem(Rune.MOON));
    public static final RegistryObject<RuneItem> RUNE_BLOOD = registerSupplier("rune_blood", () -> new RuneItem(Rune.BLOOD));
    public static final RegistryObject<RuneItem> RUNE_INFERNAL = registerSupplier("rune_infernal", () -> new RuneItem(Rune.INFERNAL));
    public static final RegistryObject<RuneItem> RUNE_VOID = registerSupplier("rune_void", () -> new RuneItem(Rune.VOID));
    public static final RegistryObject<RuneItem> RUNE_HALLOWED = registerSupplier("rune_hallowed", () -> new RuneItem(Rune.HALLOWED));
    public static final RegistryObject<RuneItem> RUNE_ABSORB = registerSupplier("rune_absorb", () -> new RuneItem(Rune.ABSORB));
    public static final RegistryObject<RuneItem> RUNE_DISPEL = registerSupplier("rune_dispel", () -> new RuneItem(Rune.DISPEL));
    public static final RegistryObject<RuneItem> RUNE_PROJECT = registerSupplier("rune_project", () -> new RuneItem(Rune.PROJECT));
    public static final RegistryObject<RuneItem> RUNE_PROTECT = registerSupplier("rune_protect", () -> new RuneItem(Rune.PROTECT));
    public static final RegistryObject<RuneItem> RUNE_SUMMON = registerSupplier("rune_summon", () -> new RuneItem(Rune.SUMMON));
    public static final RegistryObject<RuneItem> RUNE_AREA = registerSupplier("rune_area", () -> new RuneItem(Rune.AREA));
    public static final RegistryObject<RuneItem> RUNE_CREATURE = registerSupplier("rune_creature", () -> new RuneItem(Rune.CREATURE));
    public static final RegistryObject<RuneItem> RUNE_ITEM = registerSupplier("rune_item", () -> new RuneItem(Rune.ITEM));
    public static final RegistryObject<RuneItem> RUNE_SELF = registerSupplier("rune_self", () -> new RuneItem(Rune.SELF));
    public static final RegistryObject<RuneItem> RUNE_INSIGHT = registerSupplier("rune_insight", () -> new RuneItem(Rune.INSIGHT));
    public static final RegistryObject<RuneItem> RUNE_POWER = registerSupplier("rune_power", () -> new RuneItem(Rune.POWER));
    public static final RegistryObject<RuneItem> RUNE_GRACE = registerSupplier("rune_grace", () -> new RuneItem(Rune.GRACE));
    
    // Register ambrosia items
    public static final RegistryObject<AmbrosiaItem> BASIC_EARTH_AMBROSIA = registerSupplier("ambrosia_basic_earth", () -> new AmbrosiaItem(Source.EARTH, AmbrosiaItem.Type.BASIC, new Item.Properties().rarity(Rarity.UNCOMMON).food(FoodsPM.AMBROSIA)));
    public static final RegistryObject<AmbrosiaItem> BASIC_SEA_AMBROSIA = registerSupplier("ambrosia_basic_sea", () -> new AmbrosiaItem(Source.SEA, AmbrosiaItem.Type.BASIC, new Item.Properties().rarity(Rarity.UNCOMMON).food(FoodsPM.AMBROSIA)));
    public static final RegistryObject<AmbrosiaItem> BASIC_SKY_AMBROSIA = registerSupplier("ambrosia_basic_sky", () -> new AmbrosiaItem(Source.SKY, AmbrosiaItem.Type.BASIC, new Item.Properties().rarity(Rarity.UNCOMMON).food(FoodsPM.AMBROSIA)));
    public static final RegistryObject<AmbrosiaItem> BASIC_SUN_AMBROSIA = registerSupplier("ambrosia_basic_sun", () -> new AmbrosiaItem(Source.SUN, AmbrosiaItem.Type.BASIC, new Item.Properties().rarity(Rarity.UNCOMMON).food(FoodsPM.AMBROSIA)));
    public static final RegistryObject<AmbrosiaItem> BASIC_MOON_AMBROSIA = registerSupplier("ambrosia_basic_moon", () -> new AmbrosiaItem(Source.MOON, AmbrosiaItem.Type.BASIC, new Item.Properties().rarity(Rarity.UNCOMMON).food(FoodsPM.AMBROSIA)));
    public static final RegistryObject<AmbrosiaItem> BASIC_BLOOD_AMBROSIA = registerSupplier("ambrosia_basic_blood", () -> new AmbrosiaItem(Source.BLOOD, AmbrosiaItem.Type.BASIC, new Item.Properties().rarity(Rarity.UNCOMMON).food(FoodsPM.AMBROSIA)));
    public static final RegistryObject<AmbrosiaItem> BASIC_INFERNAL_AMBROSIA = registerSupplier("ambrosia_basic_infernal", () -> new AmbrosiaItem(Source.INFERNAL, AmbrosiaItem.Type.BASIC, new Item.Properties().rarity(Rarity.UNCOMMON).food(FoodsPM.AMBROSIA)));
    public static final RegistryObject<AmbrosiaItem> BASIC_VOID_AMBROSIA = registerSupplier("ambrosia_basic_void", () -> new AmbrosiaItem(Source.VOID, AmbrosiaItem.Type.BASIC, new Item.Properties().rarity(Rarity.UNCOMMON).food(FoodsPM.AMBROSIA)));
    public static final RegistryObject<AmbrosiaItem> BASIC_HALLOWED_AMBROSIA = registerSupplier("ambrosia_basic_hallowed", () -> new AmbrosiaItem(Source.HALLOWED, AmbrosiaItem.Type.BASIC, new Item.Properties().rarity(Rarity.UNCOMMON).food(FoodsPM.AMBROSIA)));
    public static final RegistryObject<AmbrosiaItem> GREATER_EARTH_AMBROSIA = registerSupplier("ambrosia_greater_earth", () -> new AmbrosiaItem(Source.EARTH, AmbrosiaItem.Type.GREATER, new Item.Properties().rarity(Rarity.RARE).food(FoodsPM.AMBROSIA)));
    public static final RegistryObject<AmbrosiaItem> GREATER_SEA_AMBROSIA = registerSupplier("ambrosia_greater_sea", () -> new AmbrosiaItem(Source.SEA, AmbrosiaItem.Type.GREATER, new Item.Properties().rarity(Rarity.RARE).food(FoodsPM.AMBROSIA)));
    public static final RegistryObject<AmbrosiaItem> GREATER_SKY_AMBROSIA = registerSupplier("ambrosia_greater_sky", () -> new AmbrosiaItem(Source.SKY, AmbrosiaItem.Type.GREATER, new Item.Properties().rarity(Rarity.RARE).food(FoodsPM.AMBROSIA)));
    public static final RegistryObject<AmbrosiaItem> GREATER_SUN_AMBROSIA = registerSupplier("ambrosia_greater_sun", () -> new AmbrosiaItem(Source.SUN, AmbrosiaItem.Type.GREATER, new Item.Properties().rarity(Rarity.RARE).food(FoodsPM.AMBROSIA)));
    public static final RegistryObject<AmbrosiaItem> GREATER_MOON_AMBROSIA = registerSupplier("ambrosia_greater_moon", () -> new AmbrosiaItem(Source.MOON, AmbrosiaItem.Type.GREATER, new Item.Properties().rarity(Rarity.RARE).food(FoodsPM.AMBROSIA)));
    public static final RegistryObject<AmbrosiaItem> GREATER_BLOOD_AMBROSIA = registerSupplier("ambrosia_greater_blood", () -> new AmbrosiaItem(Source.BLOOD, AmbrosiaItem.Type.GREATER, new Item.Properties().rarity(Rarity.RARE).food(FoodsPM.AMBROSIA)));
    public static final RegistryObject<AmbrosiaItem> GREATER_INFERNAL_AMBROSIA = registerSupplier("ambrosia_greater_infernal", () -> new AmbrosiaItem(Source.INFERNAL, AmbrosiaItem.Type.GREATER, new Item.Properties().rarity(Rarity.RARE).food(FoodsPM.AMBROSIA)));
    public static final RegistryObject<AmbrosiaItem> GREATER_VOID_AMBROSIA = registerSupplier("ambrosia_greater_void", () -> new AmbrosiaItem(Source.VOID, AmbrosiaItem.Type.GREATER, new Item.Properties().rarity(Rarity.RARE).food(FoodsPM.AMBROSIA)));
    public static final RegistryObject<AmbrosiaItem> GREATER_HALLOWED_AMBROSIA = registerSupplier("ambrosia_greater_hallowed", () -> new AmbrosiaItem(Source.HALLOWED, AmbrosiaItem.Type.GREATER, new Item.Properties().rarity(Rarity.RARE).food(FoodsPM.AMBROSIA)));
    public static final RegistryObject<AmbrosiaItem> SUPREME_EARTH_AMBROSIA = registerSupplier("ambrosia_supreme_earth", () -> new AmbrosiaItem(Source.EARTH, AmbrosiaItem.Type.SUPREME, new Item.Properties().rarity(Rarity.EPIC).food(FoodsPM.AMBROSIA)));
    public static final RegistryObject<AmbrosiaItem> SUPREME_SEA_AMBROSIA = registerSupplier("ambrosia_supreme_sea", () -> new AmbrosiaItem(Source.SEA, AmbrosiaItem.Type.SUPREME, new Item.Properties().rarity(Rarity.EPIC).food(FoodsPM.AMBROSIA)));
    public static final RegistryObject<AmbrosiaItem> SUPREME_SKY_AMBROSIA = registerSupplier("ambrosia_supreme_sky", () -> new AmbrosiaItem(Source.SKY, AmbrosiaItem.Type.SUPREME, new Item.Properties().rarity(Rarity.EPIC).food(FoodsPM.AMBROSIA)));
    public static final RegistryObject<AmbrosiaItem> SUPREME_SUN_AMBROSIA = registerSupplier("ambrosia_supreme_sun", () -> new AmbrosiaItem(Source.SUN, AmbrosiaItem.Type.SUPREME, new Item.Properties().rarity(Rarity.EPIC).food(FoodsPM.AMBROSIA)));
    public static final RegistryObject<AmbrosiaItem> SUPREME_MOON_AMBROSIA = registerSupplier("ambrosia_supreme_moon", () -> new AmbrosiaItem(Source.MOON, AmbrosiaItem.Type.SUPREME, new Item.Properties().rarity(Rarity.EPIC).food(FoodsPM.AMBROSIA)));
    public static final RegistryObject<AmbrosiaItem> SUPREME_BLOOD_AMBROSIA = registerSupplier("ambrosia_supreme_blood", () -> new AmbrosiaItem(Source.BLOOD, AmbrosiaItem.Type.SUPREME, new Item.Properties().rarity(Rarity.EPIC).food(FoodsPM.AMBROSIA)));
    public static final RegistryObject<AmbrosiaItem> SUPREME_INFERNAL_AMBROSIA = registerSupplier("ambrosia_supreme_infernal", () -> new AmbrosiaItem(Source.INFERNAL, AmbrosiaItem.Type.SUPREME, new Item.Properties().rarity(Rarity.EPIC).food(FoodsPM.AMBROSIA)));
    public static final RegistryObject<AmbrosiaItem> SUPREME_VOID_AMBROSIA = registerSupplier("ambrosia_supreme_void", () -> new AmbrosiaItem(Source.VOID, AmbrosiaItem.Type.SUPREME, new Item.Properties().rarity(Rarity.EPIC).food(FoodsPM.AMBROSIA)));
    public static final RegistryObject<AmbrosiaItem> SUPREME_HALLOWED_AMBROSIA = registerSupplier("ambrosia_supreme_hallowed", () -> new AmbrosiaItem(Source.HALLOWED, AmbrosiaItem.Type.SUPREME, new Item.Properties().rarity(Rarity.EPIC).food(FoodsPM.AMBROSIA)));
    
    // Register attunement shackle items
    public static final RegistryObject<AttunementShacklesItem> ATTUNEMENT_SHACKLES_EARTH = registerSupplier("attunement_shackles_earth", () -> new AttunementShacklesItem(Source.EARTH, new Item.Properties()));
    public static final RegistryObject<AttunementShacklesItem> ATTUNEMENT_SHACKLES_SEA = registerSupplier("attunement_shackles_sea", () -> new AttunementShacklesItem(Source.SEA, new Item.Properties()));
    public static final RegistryObject<AttunementShacklesItem> ATTUNEMENT_SHACKLES_SKY = registerSupplier("attunement_shackles_sky", () -> new AttunementShacklesItem(Source.SKY, new Item.Properties()));
    public static final RegistryObject<AttunementShacklesItem> ATTUNEMENT_SHACKLES_SUN = registerSupplier("attunement_shackles_sun", () -> new AttunementShacklesItem(Source.SUN, new Item.Properties()));
    public static final RegistryObject<AttunementShacklesItem> ATTUNEMENT_SHACKLES_MOON = registerSupplier("attunement_shackles_moon", () -> new AttunementShacklesItem(Source.MOON, new Item.Properties()));
    public static final RegistryObject<AttunementShacklesItem> ATTUNEMENT_SHACKLES_BLOOD = registerSupplier("attunement_shackles_blood", () -> new AttunementShacklesItem(Source.BLOOD, new Item.Properties()));
    public static final RegistryObject<AttunementShacklesItem> ATTUNEMENT_SHACKLES_INFERNAL = registerSupplier("attunement_shackles_infernal", () -> new AttunementShacklesItem(Source.INFERNAL, new Item.Properties()));
    public static final RegistryObject<AttunementShacklesItem> ATTUNEMENT_SHACKLES_VOID = registerSupplier("attunement_shackles_void", () -> new AttunementShacklesItem(Source.VOID, new Item.Properties()));
    public static final RegistryObject<AttunementShacklesItem> ATTUNEMENT_SHACKLES_HALLOWED = registerSupplier("attunement_shackles_hallowed", () -> new AttunementShacklesItem(Source.HALLOWED, new Item.Properties()));
    
    // Register humming artifact items
    public static final RegistryObject<Item> HUMMING_ARTIFACT_UNATTUNED = registerSupplier("humming_artifact_unattuned", () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<HummingArtifactItem> HUMMING_ARTIFACT_EARTH = registerSupplier("humming_artifact_earth", () -> new HummingArtifactItem(Source.EARTH, new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<HummingArtifactItem> HUMMING_ARTIFACT_SEA = registerSupplier("humming_artifact_sea", () -> new HummingArtifactItem(Source.SEA, new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<HummingArtifactItem> HUMMING_ARTIFACT_SKY = registerSupplier("humming_artifact_sky", () -> new HummingArtifactItem(Source.SKY, new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<HummingArtifactItem> HUMMING_ARTIFACT_SUN = registerSupplier("humming_artifact_sun", () -> new HummingArtifactItem(Source.SUN, new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<HummingArtifactItem> HUMMING_ARTIFACT_MOON = registerSupplier("humming_artifact_moon", () -> new HummingArtifactItem(Source.MOON, new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<HummingArtifactItem> HUMMING_ARTIFACT_BLOOD = registerSupplier("humming_artifact_blood", () -> new HummingArtifactItem(Source.BLOOD, new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<HummingArtifactItem> HUMMING_ARTIFACT_INFERNAL = registerSupplier("humming_artifact_infernal", () -> new HummingArtifactItem(Source.INFERNAL, new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<HummingArtifactItem> HUMMING_ARTIFACT_VOID = registerSupplier("humming_artifact_void", () -> new HummingArtifactItem(Source.VOID, new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<HummingArtifactItem> HUMMING_ARTIFACT_HALLOWED = registerSupplier("humming_artifact_hallowed", () -> new HummingArtifactItem(Source.HALLOWED, new Item.Properties().rarity(Rarity.RARE)));
    
    // Register sanguine core items
    public static final RegistryObject<Item> SANGUINE_CORE_BLANK = registerSupplier("sanguine_core_blank", () -> new Item(new Item.Properties()));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_ALLAY = registerSupplier("sanguine_core_allay", () -> new SanguineCoreItem(() -> EntityType.ALLAY, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_AXOLOTL = registerSupplier("sanguine_core_axolotl", () -> new SanguineCoreItem(() -> EntityType.AXOLOTL, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_BAT = registerSupplier("sanguine_core_bat", () -> new SanguineCoreItem(() -> EntityType.BAT, 1, new Item.Properties().rarity(Rarity.UNCOMMON).durability(63)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_BEE = registerSupplier("sanguine_core_bee", () -> new SanguineCoreItem(() -> EntityType.BEE, 2, new Item.Properties().rarity(Rarity.UNCOMMON).durability(31)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_BLAZE = registerSupplier("sanguine_core_blaze", () -> new SanguineCoreItem(() -> EntityType.BLAZE, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_CAMEL = registerSupplier("sanguine_core_camel", () -> new SanguineCoreItem(() -> EntityType.CAMEL, 2, new Item.Properties().rarity(Rarity.UNCOMMON).durability(31)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_CAT = registerSupplier("sanguine_core_cat", () -> new SanguineCoreItem(() -> EntityType.CAT, 1, new Item.Properties().rarity(Rarity.UNCOMMON).durability(63)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_CAVE_SPIDER = registerSupplier("sanguine_core_cave_spider", () -> new SanguineCoreItem(() -> EntityType.CAVE_SPIDER, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_CHICKEN = registerSupplier("sanguine_core_chicken", () -> new SanguineCoreItem(() -> EntityType.CHICKEN, 2, new Item.Properties().rarity(Rarity.UNCOMMON).durability(31)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_COD = registerSupplier("sanguine_core_cod", () -> new SanguineCoreItem(() -> EntityType.COD, 2, new Item.Properties().rarity(Rarity.UNCOMMON).durability(31)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_COW = registerSupplier("sanguine_core_cow", () -> new SanguineCoreItem(() -> EntityType.COW, 2, new Item.Properties().rarity(Rarity.UNCOMMON).durability(31)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_CREEPER = registerSupplier("sanguine_core_creeper", () -> new SanguineCoreItem(() -> EntityType.CREEPER, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_DOLPHIN = registerSupplier("sanguine_core_dolphin", () -> new SanguineCoreItem(() -> EntityType.DOLPHIN, 2, new Item.Properties().rarity(Rarity.UNCOMMON).durability(31)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_DONKEY = registerSupplier("sanguine_core_donkey", () -> new SanguineCoreItem(() -> EntityType.DONKEY, 2, new Item.Properties().rarity(Rarity.UNCOMMON).durability(31)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_DROWNED = registerSupplier("sanguine_core_drowned", () -> new SanguineCoreItem(() -> EntityType.DROWNED, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_ELDER_GUARDIAN = registerSupplier("sanguine_core_elder_guardian", () -> new SanguineCoreItem(() -> EntityType.ELDER_GUARDIAN, 16, new Item.Properties().rarity(Rarity.UNCOMMON).durability(3)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_ENDERMAN = registerSupplier("sanguine_core_enderman", () -> new SanguineCoreItem(() -> EntityType.ENDERMAN, 8, new Item.Properties().rarity(Rarity.UNCOMMON).durability(7)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_ENDERMITE = registerSupplier("sanguine_core_endermite", () -> new SanguineCoreItem(() -> EntityType.ENDERMITE, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_EVOKER = registerSupplier("sanguine_core_evoker", () -> new SanguineCoreItem(() -> EntityType.EVOKER, 16, new Item.Properties().rarity(Rarity.UNCOMMON).durability(3)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_FOX = registerSupplier("sanguine_core_fox", () -> new SanguineCoreItem(() -> EntityType.FOX, 2, new Item.Properties().rarity(Rarity.UNCOMMON).durability(31)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_FROG = registerSupplier("sanguine_core_frog", () -> new SanguineCoreItem(() -> EntityType.FROG, 2, new Item.Properties().rarity(Rarity.UNCOMMON).durability(31)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_GHAST = registerSupplier("sanguine_core_ghast", () -> new SanguineCoreItem(() -> EntityType.GHAST, 8, new Item.Properties().rarity(Rarity.UNCOMMON).durability(7)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_GLOW_SQUID = registerSupplier("sanguine_core_glow_squid", () -> new SanguineCoreItem(() -> EntityType.GLOW_SQUID, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_GOAT = registerSupplier("sanguine_core_goat", () -> new SanguineCoreItem(() -> EntityType.GOAT, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_GUARDIAN = registerSupplier("sanguine_core_guardian", () -> new SanguineCoreItem(() -> EntityType.GUARDIAN, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_HOGLIN = registerSupplier("sanguine_core_hoglin", () -> new SanguineCoreItem(() -> EntityType.HOGLIN, 2, new Item.Properties().rarity(Rarity.UNCOMMON).durability(31)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_HORSE = registerSupplier("sanguine_core_horse", () -> new SanguineCoreItem(() -> EntityType.HORSE, 2, new Item.Properties().rarity(Rarity.UNCOMMON).durability(31)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_HUSK = registerSupplier("sanguine_core_husk", () -> new SanguineCoreItem(() -> EntityType.HUSK, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_LLAMA = registerSupplier("sanguine_core_llama", () -> new SanguineCoreItem(() -> EntityType.LLAMA, 2, new Item.Properties().rarity(Rarity.UNCOMMON).durability(31)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_MAGMA_CUBE = registerSupplier("sanguine_core_magma_cube", () -> new SanguineCoreItem(() -> EntityType.MAGMA_CUBE, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_MOOSHROOM = registerSupplier("sanguine_core_mooshroom", () -> new SanguineCoreItem(() -> EntityType.MOOSHROOM, 2, new Item.Properties().rarity(Rarity.UNCOMMON).durability(31)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_OCELOT = registerSupplier("sanguine_core_ocelot", () -> new SanguineCoreItem(() -> EntityType.OCELOT, 1, new Item.Properties().rarity(Rarity.UNCOMMON).durability(63)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_PANDA = registerSupplier("sanguine_core_panda", () -> new SanguineCoreItem(() -> EntityType.PANDA, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_PARROT = registerSupplier("sanguine_core_parrot", () -> new SanguineCoreItem(() -> EntityType.PARROT, 1, new Item.Properties().rarity(Rarity.UNCOMMON).durability(63)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_PHANTOM = registerSupplier("sanguine_core_phantom", () -> new SanguineCoreItem(() -> EntityType.PHANTOM, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_PIG = registerSupplier("sanguine_core_pig", () -> new SanguineCoreItem(() -> EntityType.PIG, 2, new Item.Properties().rarity(Rarity.UNCOMMON).durability(31)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_PIGLIN = registerSupplier("sanguine_core_piglin", () -> new SanguineCoreItem(() -> EntityType.PIGLIN, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_PIGLIN_BRUTE = registerSupplier("sanguine_core_piglin_brute", () -> new SanguineCoreItem(() -> EntityType.PIGLIN_BRUTE, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_PILLAGER = registerSupplier("sanguine_core_pillager", () -> new SanguineCoreItem(() -> EntityType.PILLAGER, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_POLAR_BEAR = registerSupplier("sanguine_core_polar_bear", () -> new SanguineCoreItem(() -> EntityType.POLAR_BEAR, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_PUFFERFISH = registerSupplier("sanguine_core_pufferfish", () -> new SanguineCoreItem(() -> EntityType.PUFFERFISH, 2, new Item.Properties().rarity(Rarity.UNCOMMON).durability(31)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_RABBIT = registerSupplier("sanguine_core_rabbit", () -> new SanguineCoreItem(() -> EntityType.RABBIT, 2, new Item.Properties().rarity(Rarity.UNCOMMON).durability(31)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_RAVAGER = registerSupplier("sanguine_core_ravager", () -> new SanguineCoreItem(() -> EntityType.RAVAGER, 8, new Item.Properties().rarity(Rarity.UNCOMMON).durability(7)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_SALMON = registerSupplier("sanguine_core_salmon", () -> new SanguineCoreItem(() -> EntityType.SALMON, 2, new Item.Properties().rarity(Rarity.UNCOMMON).durability(31)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_SHEEP = registerSupplier("sanguine_core_sheep", () -> new SanguineCoreItem(() -> EntityType.SHEEP, 2, new Item.Properties().rarity(Rarity.UNCOMMON).durability(31)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_SHULKER = registerSupplier("sanguine_core_shulker", () -> new SanguineCoreItem(() -> EntityType.SHULKER, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_SILVERFISH = registerSupplier("sanguine_core_silverfish", () -> new SanguineCoreItem(() -> EntityType.SILVERFISH, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_SKELETON = registerSupplier("sanguine_core_skeleton", () -> new SanguineCoreItem(() -> EntityType.SKELETON, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_SKELETON_HORSE = registerSupplier("sanguine_core_skeleton_horse", () -> new SanguineCoreItem(() -> EntityType.SKELETON_HORSE, 2, new Item.Properties().rarity(Rarity.UNCOMMON).durability(31)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_SLIME = registerSupplier("sanguine_core_slime", () -> new SanguineCoreItem(() -> EntityType.SLIME, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_SNIFFER = registerSupplier("sanguine_core_sniffer", () -> new SanguineCoreItem(() -> EntityType.SNIFFER, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_SPIDER = registerSupplier("sanguine_core_spider", () -> new SanguineCoreItem(() -> EntityType.SPIDER, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_SQUID = registerSupplier("sanguine_core_squid", () -> new SanguineCoreItem(() -> EntityType.SQUID, 2, new Item.Properties().rarity(Rarity.UNCOMMON).durability(31)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_STRAY = registerSupplier("sanguine_core_stray", () -> new SanguineCoreItem(() -> EntityType.STRAY, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_STRIDER = registerSupplier("sanguine_core_strider", () -> new SanguineCoreItem(() -> EntityType.STRIDER, 2, new Item.Properties().rarity(Rarity.UNCOMMON).durability(31)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_TROPICAL_FISH = registerSupplier("sanguine_core_tropical_fish", () -> new SanguineCoreItem(() -> EntityType.TROPICAL_FISH, 2, new Item.Properties().rarity(Rarity.UNCOMMON).durability(31)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_TURTLE = registerSupplier("sanguine_core_turtle", () -> new SanguineCoreItem(() -> EntityType.TURTLE, 2, new Item.Properties().rarity(Rarity.UNCOMMON).durability(31)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_VEX = registerSupplier("sanguine_core_vex", () -> new SanguineCoreItem(() -> EntityType.VEX, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_VILLAGER = registerSupplier("sanguine_core_villager", () -> new SanguineCoreItem(() -> EntityType.VILLAGER, 8, new Item.Properties().rarity(Rarity.UNCOMMON).durability(7)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_VINDICATOR = registerSupplier("sanguine_core_vindicator", () -> new SanguineCoreItem(() -> EntityType.VINDICATOR, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_WITCH = registerSupplier("sanguine_core_witch", () -> new SanguineCoreItem(() -> EntityType.WITCH, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_WITHER_SKELETON = registerSupplier("sanguine_core_wither_skeleton", () -> new SanguineCoreItem(() -> EntityType.WITHER_SKELETON, 8, new Item.Properties().rarity(Rarity.UNCOMMON).durability(7)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_WOLF = registerSupplier("sanguine_core_wolf", () -> new SanguineCoreItem(() -> EntityType.WOLF, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_ZOGLIN = registerSupplier("sanguine_core_zoglin", () -> new SanguineCoreItem(() -> EntityType.ZOGLIN, 2, new Item.Properties().rarity(Rarity.UNCOMMON).durability(31)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_ZOMBIE = registerSupplier("sanguine_core_zombie", () -> new SanguineCoreItem(() -> EntityType.ZOMBIE, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_ZOMBIE_HORSE = registerSupplier("sanguine_core_zombie_horse", () -> new SanguineCoreItem(() -> EntityType.ZOMBIE_HORSE, 2, new Item.Properties().rarity(Rarity.UNCOMMON).durability(31)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_ZOMBIE_VILLAGER = registerSupplier("sanguine_core_zombie_villager", () -> new SanguineCoreItem(() -> EntityType.ZOMBIE_VILLAGER, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_ZOMBIFIED_PIGLIN = registerSupplier("sanguine_core_zombified_piglin", () -> new SanguineCoreItem(() -> EntityType.ZOMBIFIED_PIGLIN, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_TREEFOLK = registerSupplier("sanguine_core_treefolk", () -> new SanguineCoreItem(() -> EntityTypesPM.TREEFOLK.get(), 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_INNER_DEMON = registerSupplier("sanguine_core_inner_demon", () -> new SanguineCoreItem(() -> EntityTypesPM.INNER_DEMON.get(), 64, new Item.Properties().rarity(Rarity.UNCOMMON).durability(0).stacksTo(1)));

    // Register concoction items
    public static final RegistryObject<SkyglassFlaskItem> SKYGLASS_FLASK = registerSupplier("skyglass_flask", () -> new SkyglassFlaskItem(new Item.Properties()));
    public static final RegistryObject<ConcoctionItem> CONCOCTION = registerCustom("concoction", ConcoctionItem::registerCreativeTabItems, () -> new ConcoctionItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<BombCasingItem> BOMB_CASING = registerSupplier("bomb_casing", () -> new BombCasingItem(new Item.Properties()));
    public static final RegistryObject<AlchemicalBombItem> ALCHEMICAL_BOMB = registerCustom("alchemical_bomb", AlchemicalBombItem::registerCreativeTabItems, () -> new AlchemicalBombItem(new Item.Properties().stacksTo(1)));
    
    // Register caster/wand items
    public static final RegistryObject<Item> SPELL_SCROLL_BLANK = registerSupplier("spell_scroll_blank", () -> new Item(new Item.Properties()));
    public static final RegistryObject<SpellScrollItem> SPELL_SCROLL_FILLED = registerWithoutTab("spell_scroll_filled", SpellScrollItem::new);
    public static final RegistryObject<MundaneWandItem> MUNDANE_WAND = registerSupplier("mundane_wand", MundaneWandItem::new);
    public static final RegistryObject<ModularWandItem> MODULAR_WAND = registerCustom("modular_wand", ModularWandItem::registerCreativeTabItems, () -> new ModularWandItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<ModularStaffItem> MODULAR_STAFF = registerCustom("modular_staff", ModularWandItem::registerCreativeTabItems, () -> new ModularStaffItem(5, -2.4F, new Item.Properties().stacksTo(1)));
    public static final RegistryObject<WandCoreItem> HEARTWOOD_WAND_CORE_ITEM = registerSupplier("heartwood_wand_core_item", () -> new WandCoreItem(WandCore.HEARTWOOD, new Item.Properties()));
    public static final RegistryObject<WandCoreItem> OBSIDIAN_WAND_CORE_ITEM = registerSupplier("obsidian_wand_core_item", () -> new WandCoreItem(WandCore.OBSIDIAN, new Item.Properties()));
    public static final RegistryObject<WandCoreItem> CORAL_WAND_CORE_ITEM = registerSupplier("coral_wand_core_item", () -> new WandCoreItem(WandCore.CORAL, new Item.Properties()));
    public static final RegistryObject<WandCoreItem> BAMBOO_WAND_CORE_ITEM = registerSupplier("bamboo_wand_core_item", () -> new WandCoreItem(WandCore.BAMBOO, new Item.Properties()));
    public static final RegistryObject<WandCoreItem> SUNWOOD_WAND_CORE_ITEM = registerSupplier("sunwood_wand_core_item", () -> new WandCoreItem(WandCore.SUNWOOD, new Item.Properties()));
    public static final RegistryObject<WandCoreItem> MOONWOOD_WAND_CORE_ITEM = registerSupplier("moonwood_wand_core_item", () -> new WandCoreItem(WandCore.MOONWOOD, new Item.Properties()));
    public static final RegistryObject<WandCoreItem> BONE_WAND_CORE_ITEM = registerSupplier("bone_wand_core_item", () -> new WandCoreItem(WandCore.BONE, new Item.Properties()));
    public static final RegistryObject<WandCoreItem> BLAZE_ROD_WAND_CORE_ITEM = registerSupplier("blaze_rod_wand_core_item", () -> new WandCoreItem(WandCore.BLAZE_ROD, new Item.Properties()));
    public static final RegistryObject<WandCoreItem> PURPUR_WAND_CORE_ITEM = registerSupplier("purpur_wand_core_item", () -> new WandCoreItem(WandCore.PURPUR, new Item.Properties()));
    public static final RegistryObject<WandCoreItem> PRIMAL_WAND_CORE_ITEM = registerSupplier("primal_wand_core_item", () -> new WandCoreItem(WandCore.PRIMAL, new Item.Properties()));
    public static final RegistryObject<WandCoreItem> DARK_PRIMAL_WAND_CORE_ITEM = registerSupplier("dark_primal_wand_core_item", () -> new WandCoreItem(WandCore.DARK_PRIMAL, new Item.Properties()));
    public static final RegistryObject<WandCoreItem> PURE_PRIMAL_WAND_CORE_ITEM = registerSupplier("pure_primal_wand_core_item", () -> new WandCoreItem(WandCore.PURE_PRIMAL, new Item.Properties()));
    public static final RegistryObject<WandCapItem> IRON_WAND_CAP_ITEM = registerSupplier("iron_wand_cap_item", () -> new WandCapItem(WandCap.IRON, new Item.Properties()));
    public static final RegistryObject<WandCapItem> GOLD_WAND_CAP_ITEM = registerSupplier("gold_wand_cap_item", () -> new WandCapItem(WandCap.GOLD, new Item.Properties()));
    public static final RegistryObject<WandCapItem> PRIMALITE_WAND_CAP_ITEM = registerSupplier("primalite_wand_cap_item", () -> new WandCapItem(WandCap.PRIMALITE, new Item.Properties()));
    public static final RegistryObject<WandCapItem> HEXIUM_WAND_CAP_ITEM = registerSupplier("hexium_wand_cap_item", () -> new WandCapItem(WandCap.HEXIUM, new Item.Properties()));
    public static final RegistryObject<WandCapItem> HALLOWSTEEL_WAND_CAP_ITEM = registerSupplier("hallowsteel_wand_cap_item", () -> new WandCapItem(WandCap.HALLOWSTEEL, new Item.Properties()));
    public static final RegistryObject<WandGemItem> APPRENTICE_WAND_GEM_ITEM = registerSupplier("apprentice_wand_gem_item", () -> new WandGemItem(WandGem.APPRENTICE, new Item.Properties()));
    public static final RegistryObject<WandGemItem> ADEPT_WAND_GEM_ITEM = registerSupplier("adept_wand_gem_item", () -> new WandGemItem(WandGem.ADEPT, new Item.Properties()));
    public static final RegistryObject<WandGemItem> WIZARD_WAND_GEM_ITEM = registerSupplier("wizard_wand_gem_item", () -> new WandGemItem(WandGem.WIZARD, new Item.Properties()));
    public static final RegistryObject<WandGemItem> ARCHMAGE_WAND_GEM_ITEM = registerSupplier("archmage_wand_gem_item", () -> new WandGemItem(WandGem.ARCHMAGE, new Item.Properties()));
    public static final RegistryObject<WandGemItem> CREATIVE_WAND_GEM_ITEM = registerSupplier("creative_wand_gem_item", () -> new WandGemItem(WandGem.CREATIVE, new Item.Properties()));
    public static final RegistryObject<StaffCoreItem> HEARTWOOD_STAFF_CORE_ITEM = registerSupplier("heartwood_staff_core_item", () -> new StaffCoreItem(WandCore.HEARTWOOD, new Item.Properties()));
    public static final RegistryObject<StaffCoreItem> OBSIDIAN_STAFF_CORE_ITEM = registerSupplier("obsidian_staff_core_item", () -> new StaffCoreItem(WandCore.OBSIDIAN, new Item.Properties()));
    public static final RegistryObject<StaffCoreItem> CORAL_STAFF_CORE_ITEM = registerSupplier("coral_staff_core_item", () -> new StaffCoreItem(WandCore.CORAL, new Item.Properties()));
    public static final RegistryObject<StaffCoreItem> BAMBOO_STAFF_CORE_ITEM = registerSupplier("bamboo_staff_core_item", () -> new StaffCoreItem(WandCore.BAMBOO, new Item.Properties()));
    public static final RegistryObject<StaffCoreItem> SUNWOOD_STAFF_CORE_ITEM = registerSupplier("sunwood_staff_core_item", () -> new StaffCoreItem(WandCore.SUNWOOD, new Item.Properties()));
    public static final RegistryObject<StaffCoreItem> MOONWOOD_STAFF_CORE_ITEM = registerSupplier("moonwood_staff_core_item", () -> new StaffCoreItem(WandCore.MOONWOOD, new Item.Properties()));
    public static final RegistryObject<StaffCoreItem> BONE_STAFF_CORE_ITEM = registerSupplier("bone_staff_core_item", () -> new StaffCoreItem(WandCore.BONE, new Item.Properties()));
    public static final RegistryObject<StaffCoreItem> BLAZE_ROD_STAFF_CORE_ITEM = registerSupplier("blaze_rod_staff_core_item", () -> new StaffCoreItem(WandCore.BLAZE_ROD, new Item.Properties()));
    public static final RegistryObject<StaffCoreItem> PURPUR_STAFF_CORE_ITEM = registerSupplier("purpur_staff_core_item", () -> new StaffCoreItem(WandCore.PURPUR, new Item.Properties()));
    public static final RegistryObject<StaffCoreItem> PRIMAL_STAFF_CORE_ITEM = registerSupplier("primal_staff_core_item", () -> new StaffCoreItem(WandCore.PRIMAL, new Item.Properties()));
    public static final RegistryObject<StaffCoreItem> DARK_PRIMAL_STAFF_CORE_ITEM = registerSupplier("dark_primal_staff_core_item", () -> new StaffCoreItem(WandCore.DARK_PRIMAL, new Item.Properties()));
    public static final RegistryObject<StaffCoreItem> PURE_PRIMAL_STAFF_CORE_ITEM = registerSupplier("pure_primal_staff_core_item", () -> new StaffCoreItem(WandCore.PURE_PRIMAL, new Item.Properties()));
    
    // Register spawn egg items
    public static final RegistryObject<ForgeSpawnEggItem> TREEFOLK_SPAWN_EGG = registerSupplier("treefolk_spawn_egg", () -> new ForgeSpawnEggItem(() -> EntityTypesPM.TREEFOLK.get(), 0x76440F, 0x007302, new Item.Properties()));
    public static final RegistryObject<ForgeSpawnEggItem> PRIMALITE_GOLEM_SPAWN_EGG = registerSupplier("primalite_golem_spawn_egg", () -> new ForgeSpawnEggItem(() -> EntityTypesPM.PRIMALITE_GOLEM.get(), 0x27E1C7, 0x026278, new Item.Properties()));
    public static final RegistryObject<ForgeSpawnEggItem> HEXIUM_GOLEM_SPAWN_EGG = registerSupplier("hexium_golem_spawn_egg", () -> new ForgeSpawnEggItem(() -> EntityTypesPM.HEXIUM_GOLEM.get(), 0x791E29, 0x100736, new Item.Properties()));
    public static final RegistryObject<ForgeSpawnEggItem> HALLOWSTEEL_GOLEM_SPAWN_EGG = registerSupplier("hallowsteel_golem_spawn_egg", () -> new ForgeSpawnEggItem(() -> EntityTypesPM.HALLOWSTEEL_GOLEM.get(), 0xFDFFE0, 0xEDE1A2, new Item.Properties()));
    public static final RegistryObject<PixieItem> BASIC_EARTH_PIXIE = registerSupplier("pixie_basic_earth", () -> new PixieItem(() -> EntityTypesPM.BASIC_EARTH_PIXIE.get(), Source.EARTH, new Item.Properties().stacksTo(16).rarity(Rarity.COMMON)));
    public static final RegistryObject<PixieItem> GRAND_EARTH_PIXIE = registerSupplier("pixie_grand_earth", () -> new PixieItem(() -> EntityTypesPM.GRAND_EARTH_PIXIE.get(), Source.EARTH, new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<PixieItem> MAJESTIC_EARTH_PIXIE = registerSupplier("pixie_majestic_earth", () -> new PixieItem(() -> EntityTypesPM.MAJESTIC_EARTH_PIXIE.get(), Source.EARTH, new Item.Properties().stacksTo(16).rarity(Rarity.RARE)));
    public static final RegistryObject<PixieItem> BASIC_SEA_PIXIE = registerSupplier("pixie_basic_sea", () -> new PixieItem(() -> EntityTypesPM.BASIC_SEA_PIXIE.get(), Source.SEA, new Item.Properties().stacksTo(16).rarity(Rarity.COMMON)));
    public static final RegistryObject<PixieItem> GRAND_SEA_PIXIE = registerSupplier("pixie_grand_sea", () -> new PixieItem(() -> EntityTypesPM.GRAND_SEA_PIXIE.get(), Source.SEA, new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<PixieItem> MAJESTIC_SEA_PIXIE = registerSupplier("pixie_majestic_sea", () -> new PixieItem(() -> EntityTypesPM.MAJESTIC_SEA_PIXIE.get(), Source.SEA, new Item.Properties().stacksTo(16).rarity(Rarity.RARE)));
    public static final RegistryObject<PixieItem> BASIC_SKY_PIXIE = registerSupplier("pixie_basic_sky", () -> new PixieItem(() -> EntityTypesPM.BASIC_SKY_PIXIE.get(), Source.SKY, new Item.Properties().stacksTo(16).rarity(Rarity.COMMON)));
    public static final RegistryObject<PixieItem> GRAND_SKY_PIXIE = registerSupplier("pixie_grand_sky", () -> new PixieItem(() -> EntityTypesPM.GRAND_SKY_PIXIE.get(), Source.SKY, new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<PixieItem> MAJESTIC_SKY_PIXIE = registerSupplier("pixie_majestic_sky", () -> new PixieItem(() -> EntityTypesPM.MAJESTIC_SKY_PIXIE.get(), Source.SKY, new Item.Properties().stacksTo(16).rarity(Rarity.RARE)));
    public static final RegistryObject<PixieItem> BASIC_SUN_PIXIE = registerSupplier("pixie_basic_sun", () -> new PixieItem(() -> EntityTypesPM.BASIC_SUN_PIXIE.get(), Source.SUN, new Item.Properties().stacksTo(16).rarity(Rarity.COMMON)));
    public static final RegistryObject<PixieItem> GRAND_SUN_PIXIE = registerSupplier("pixie_grand_sun", () -> new PixieItem(() -> EntityTypesPM.GRAND_SUN_PIXIE.get(), Source.SUN, new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<PixieItem> MAJESTIC_SUN_PIXIE = registerSupplier("pixie_majestic_sun", () -> new PixieItem(() -> EntityTypesPM.MAJESTIC_SUN_PIXIE.get(), Source.SUN, new Item.Properties().stacksTo(16).rarity(Rarity.RARE)));
    public static final RegistryObject<PixieItem> BASIC_MOON_PIXIE = registerSupplier("pixie_basic_moon", () -> new PixieItem(() -> EntityTypesPM.BASIC_MOON_PIXIE.get(), Source.MOON, new Item.Properties().stacksTo(16).rarity(Rarity.COMMON)));
    public static final RegistryObject<PixieItem> GRAND_MOON_PIXIE = registerSupplier("pixie_grand_moon", () -> new PixieItem(() -> EntityTypesPM.GRAND_MOON_PIXIE.get(), Source.MOON, new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<PixieItem> MAJESTIC_MOON_PIXIE = registerSupplier("pixie_majestic_moon", () -> new PixieItem(() -> EntityTypesPM.MAJESTIC_MOON_PIXIE.get(), Source.MOON, new Item.Properties().stacksTo(16).rarity(Rarity.RARE)));
    public static final RegistryObject<PixieItem> BASIC_BLOOD_PIXIE = registerSupplier("pixie_basic_blood", () -> new PixieItem(() -> EntityTypesPM.BASIC_BLOOD_PIXIE.get(), Source.BLOOD, new Item.Properties().stacksTo(16).rarity(Rarity.COMMON)));
    public static final RegistryObject<PixieItem> GRAND_BLOOD_PIXIE = registerSupplier("pixie_grand_blood", () -> new PixieItem(() -> EntityTypesPM.GRAND_BLOOD_PIXIE.get(), Source.BLOOD, new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<PixieItem> MAJESTIC_BLOOD_PIXIE = registerSupplier("pixie_majestic_blood", () -> new PixieItem(() -> EntityTypesPM.MAJESTIC_BLOOD_PIXIE.get(), Source.BLOOD, new Item.Properties().stacksTo(16).rarity(Rarity.RARE)));
    public static final RegistryObject<PixieItem> BASIC_INFERNAL_PIXIE = registerSupplier("pixie_basic_infernal", () -> new PixieItem(() -> EntityTypesPM.BASIC_INFERNAL_PIXIE.get(), Source.INFERNAL, new Item.Properties().stacksTo(16).rarity(Rarity.COMMON)));
    public static final RegistryObject<PixieItem> GRAND_INFERNAL_PIXIE = registerSupplier("pixie_grand_infernal", () -> new PixieItem(() -> EntityTypesPM.GRAND_INFERNAL_PIXIE.get(), Source.INFERNAL, new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<PixieItem> MAJESTIC_INFERNAL_PIXIE = registerSupplier("pixie_majestic_infernal", () -> new PixieItem(() -> EntityTypesPM.MAJESTIC_INFERNAL_PIXIE.get(), Source.INFERNAL, new Item.Properties().stacksTo(16).rarity(Rarity.RARE)));
    public static final RegistryObject<PixieItem> BASIC_VOID_PIXIE = registerSupplier("pixie_basic_void", () -> new PixieItem(() -> EntityTypesPM.BASIC_VOID_PIXIE.get(), Source.VOID, new Item.Properties().stacksTo(16).rarity(Rarity.COMMON)));
    public static final RegistryObject<PixieItem> GRAND_VOID_PIXIE = registerSupplier("pixie_grand_void", () -> new PixieItem(() -> EntityTypesPM.GRAND_VOID_PIXIE.get(), Source.VOID, new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<PixieItem> MAJESTIC_VOID_PIXIE = registerSupplier("pixie_majestic_void", () -> new PixieItem(() -> EntityTypesPM.MAJESTIC_VOID_PIXIE.get(), Source.VOID, new Item.Properties().stacksTo(16).rarity(Rarity.RARE)));
    public static final RegistryObject<PixieItem> BASIC_HALLOWED_PIXIE = registerSupplier("pixie_basic_hallowed", () -> new PixieItem(() -> EntityTypesPM.BASIC_HALLOWED_PIXIE.get(), Source.HALLOWED, new Item.Properties().stacksTo(16).rarity(Rarity.COMMON)));
    public static final RegistryObject<PixieItem> GRAND_HALLOWED_PIXIE = registerSupplier("pixie_grand_hallowed", () -> new PixieItem(() -> EntityTypesPM.GRAND_HALLOWED_PIXIE.get(), Source.HALLOWED, new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<PixieItem> MAJESTIC_HALLOWED_PIXIE = registerSupplier("pixie_majestic_hallowed", () -> new PixieItem(() -> EntityTypesPM.MAJESTIC_HALLOWED_PIXIE.get(), Source.HALLOWED, new Item.Properties().stacksTo(16).rarity(Rarity.RARE)));
    
    // Register drained pixie items
    public static final RegistryObject<Item> DRAINED_BASIC_EARTH_PIXIE = registerWithoutTab("drained_pixie_basic_earth", () -> new Item(new Item.Properties().stacksTo(16).rarity(Rarity.COMMON)));
    public static final RegistryObject<Item> DRAINED_GRAND_EARTH_PIXIE = registerWithoutTab("drained_pixie_grand_earth", () -> new Item(new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> DRAINED_MAJESTIC_EARTH_PIXIE = registerWithoutTab("drained_pixie_majestic_earth", () -> new Item(new Item.Properties().stacksTo(16).rarity(Rarity.RARE)));
    public static final RegistryObject<Item> DRAINED_BASIC_SEA_PIXIE = registerWithoutTab("drained_pixie_basic_sea", () -> new Item(new Item.Properties().stacksTo(16).rarity(Rarity.COMMON)));
    public static final RegistryObject<Item> DRAINED_GRAND_SEA_PIXIE = registerWithoutTab("drained_pixie_grand_sea", () -> new Item(new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> DRAINED_MAJESTIC_SEA_PIXIE = registerWithoutTab("drained_pixie_majestic_sea", () -> new Item(new Item.Properties().stacksTo(16).rarity(Rarity.RARE)));
    public static final RegistryObject<Item> DRAINED_BASIC_SKY_PIXIE = registerWithoutTab("drained_pixie_basic_sky", () -> new Item(new Item.Properties().stacksTo(16).rarity(Rarity.COMMON)));
    public static final RegistryObject<Item> DRAINED_GRAND_SKY_PIXIE = registerWithoutTab("drained_pixie_grand_sky", () -> new Item(new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> DRAINED_MAJESTIC_SKY_PIXIE = registerWithoutTab("drained_pixie_majestic_sky", () -> new Item(new Item.Properties().stacksTo(16).rarity(Rarity.RARE)));
    public static final RegistryObject<Item> DRAINED_BASIC_SUN_PIXIE = registerWithoutTab("drained_pixie_basic_sun", () -> new Item(new Item.Properties().stacksTo(16).rarity(Rarity.COMMON)));
    public static final RegistryObject<Item> DRAINED_GRAND_SUN_PIXIE = registerWithoutTab("drained_pixie_grand_sun", () -> new Item(new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> DRAINED_MAJESTIC_SUN_PIXIE = registerWithoutTab("drained_pixie_majestic_sun", () -> new Item(new Item.Properties().stacksTo(16).rarity(Rarity.RARE)));
    public static final RegistryObject<Item> DRAINED_BASIC_MOON_PIXIE = registerWithoutTab("drained_pixie_basic_moon", () -> new Item(new Item.Properties().stacksTo(16).rarity(Rarity.COMMON)));
    public static final RegistryObject<Item> DRAINED_GRAND_MOON_PIXIE = registerWithoutTab("drained_pixie_grand_moon", () -> new Item(new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> DRAINED_MAJESTIC_MOON_PIXIE = registerWithoutTab("drained_pixie_majestic_moon", () -> new Item(new Item.Properties().stacksTo(16).rarity(Rarity.RARE)));
    public static final RegistryObject<Item> DRAINED_BASIC_BLOOD_PIXIE = registerWithoutTab("drained_pixie_basic_blood", () -> new Item(new Item.Properties().stacksTo(16).rarity(Rarity.COMMON)));
    public static final RegistryObject<Item> DRAINED_GRAND_BLOOD_PIXIE = registerWithoutTab("drained_pixie_grand_blood", () -> new Item(new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> DRAINED_MAJESTIC_BLOOD_PIXIE = registerWithoutTab("drained_pixie_majestic_blood", () -> new Item(new Item.Properties().stacksTo(16).rarity(Rarity.RARE)));
    public static final RegistryObject<Item> DRAINED_BASIC_INFERNAL_PIXIE = registerWithoutTab("drained_pixie_basic_infernal", () -> new Item(new Item.Properties().stacksTo(16).fireResistant().rarity(Rarity.COMMON)));
    public static final RegistryObject<Item> DRAINED_GRAND_INFERNAL_PIXIE = registerWithoutTab("drained_pixie_grand_infernal", () -> new Item(new Item.Properties().stacksTo(16).fireResistant().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> DRAINED_MAJESTIC_INFERNAL_PIXIE = registerWithoutTab("drained_pixie_majestic_infernal", () -> new Item(new Item.Properties().stacksTo(16).fireResistant().rarity(Rarity.RARE)));
    public static final RegistryObject<Item> DRAINED_BASIC_VOID_PIXIE = registerWithoutTab("drained_pixie_basic_void", () -> new Item(new Item.Properties().stacksTo(16).rarity(Rarity.COMMON)));
    public static final RegistryObject<Item> DRAINED_GRAND_VOID_PIXIE = registerWithoutTab("drained_pixie_grand_void", () -> new Item(new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> DRAINED_MAJESTIC_VOID_PIXIE = registerWithoutTab("drained_pixie_majestic_void", () -> new Item(new Item.Properties().stacksTo(16).rarity(Rarity.RARE)));
    public static final RegistryObject<Item> DRAINED_BASIC_HALLOWED_PIXIE = registerWithoutTab("drained_pixie_basic_hallowed", () -> new Item(new Item.Properties().stacksTo(16).rarity(Rarity.COMMON)));
    public static final RegistryObject<Item> DRAINED_GRAND_HALLOWED_PIXIE = registerWithoutTab("drained_pixie_grand_hallowed", () -> new Item(new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> DRAINED_MAJESTIC_HALLOWED_PIXIE = registerWithoutTab("drained_pixie_majestic_hallowed", () -> new Item(new Item.Properties().stacksTo(16).rarity(Rarity.RARE)));
    
    // Register book items
    public static final RegistryObject<StaticBookItem> STATIC_BOOK = registerWithoutTab("static_book", () -> new StaticBookItem(BookType.BOOK, new Item.Properties().stacksTo(16)));
    public static final RegistryObject<StaticBookItem> STATIC_TABLET = registerWithoutTab("static_tablet", () -> new StaticBookItem(BookType.TABLET, new Item.Properties().stacksTo(16)));
    // TODO Re-add book items to creative tab once the book project is ready to deploy
    public static final RegistryObject<LinguisticsGainItem> CODEX = registerWithoutTab("codex", () -> new LinguisticsGainItem(CodexType.DEFAULT, new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<LinguisticsGainItem> CODEX_CREATIVE = registerWithoutTab("codex_creative", () -> new LinguisticsGainItem(CodexType.CREATIVE, new Item.Properties().stacksTo(16).rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> LORE_TABLET_FRAGMENT = registerWithoutTab("lore_tablet_fragment", () -> new Item(new Item.Properties()));
    public static final RegistryObject<StaticBookGeneratorItem> LORE_TABLET_DIRTY = registerWithoutTab("lore_tablet_dirty", () -> new StaticBookGeneratorItem(() -> ItemsPM.STATIC_TABLET.get(), new Item.Properties()));
    
    // Register debug items
    public static final RegistryObject<TickStickItem> TICK_STICK = registerWithoutTab("tick_stick", () -> new TickStickItem(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));
}
