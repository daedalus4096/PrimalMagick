package com.verdantartifice.primalmagick.common.items;

import java.util.function.Supplier;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.attunements.AttunementType;
import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerKnowledge.KnowledgeType;
import com.verdantartifice.primalmagick.common.creative.CreativeModeTabsPM;
import com.verdantartifice.primalmagick.common.entities.EntityTypesPM;
import com.verdantartifice.primalmagick.common.items.armor.ArmorMaterialPM;
import com.verdantartifice.primalmagick.common.items.armor.RobeArmorItem;
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
import com.verdantartifice.primalmagick.common.items.food.ManafruitItem;
import com.verdantartifice.primalmagick.common.items.misc.ArcanometerItem;
import com.verdantartifice.primalmagick.common.items.misc.AttunementGainItem;
import com.verdantartifice.primalmagick.common.items.misc.BurnableBlockItem;
import com.verdantartifice.primalmagick.common.items.misc.DowsingRodItem;
import com.verdantartifice.primalmagick.common.items.misc.DreamVisionTalismanItem;
import com.verdantartifice.primalmagick.common.items.misc.EarthshatterHammerItem;
import com.verdantartifice.primalmagick.common.items.misc.EnchantedInkAndQuillItem;
import com.verdantartifice.primalmagick.common.items.misc.ForbiddenSourceGainItem;
import com.verdantartifice.primalmagick.common.items.misc.GrimoireItem;
import com.verdantartifice.primalmagick.common.items.misc.HallowedOrbItem;
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
import com.verdantartifice.primalmagick.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagick.common.runes.Rune;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.wands.WandCap;
import com.verdantartifice.primalmagick.common.wands.WandCore;
import com.verdantartifice.primalmagick.common.wands.WandGem;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BowlFoodItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ShovelItem;
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
    
    protected static <I extends Item> RegistryObject<I> register(String name, Supplier<? extends I> itemSupplier) {
        CreativeModeTabsPM.registerTabItem(itemSupplier);
        return ITEMS.register(name, itemSupplier);
    }
    
    // Register raw marble block items
    public static final RegistryObject<BlockItem> MARBLE_RAW = register("marble_raw", () -> new BlockItem(BlocksPM.MARBLE_RAW.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MARBLE_SLAB = register("marble_slab", () -> new BlockItem(BlocksPM.MARBLE_SLAB.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MARBLE_STAIRS = register("marble_stairs", () -> new BlockItem(BlocksPM.MARBLE_STAIRS.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MARBLE_WALL = register("marble_wall", () -> new BlockItem(BlocksPM.MARBLE_WALL.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MARBLE_BRICKS = register("marble_bricks", () -> new BlockItem(BlocksPM.MARBLE_BRICKS.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MARBLE_BRICK_SLAB = register("marble_brick_slab", () -> new BlockItem(BlocksPM.MARBLE_BRICK_SLAB.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MARBLE_BRICK_STAIRS = register("marble_brick_stairs", () -> new BlockItem(BlocksPM.MARBLE_BRICK_STAIRS.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MARBLE_BRICK_WALL = register("marble_brick_wall", () -> new BlockItem(BlocksPM.MARBLE_BRICK_WALL.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MARBLE_PILLAR = register("marble_pillar", () -> new BlockItem(BlocksPM.MARBLE_PILLAR.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MARBLE_CHISELED = register("marble_chiseled", () -> new BlockItem(BlocksPM.MARBLE_CHISELED.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MARBLE_RUNED = register("marble_runed", () -> new BlockItem(BlocksPM.MARBLE_RUNED.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MARBLE_TILES = register("marble_tiles", () -> new BlockItem(BlocksPM.MARBLE_TILES.get(), new Item.Properties()));

    // Register enchanted marble block items
    public static final RegistryObject<BlockItem> MARBLE_ENCHANTED = register("marble_enchanted", () -> new BlockItem(BlocksPM.MARBLE_ENCHANTED.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MARBLE_ENCHANTED_SLAB = register("marble_enchanted_slab", () -> new BlockItem(BlocksPM.MARBLE_ENCHANTED_SLAB.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MARBLE_ENCHANTED_STAIRS = register("marble_enchanted_stairs", () -> new BlockItem(BlocksPM.MARBLE_ENCHANTED_STAIRS.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MARBLE_ENCHANTED_WALL = register("marble_enchanted_wall", () -> new BlockItem(BlocksPM.MARBLE_ENCHANTED_WALL.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MARBLE_ENCHANTED_BRICKS = register("marble_enchanted_bricks", () -> new BlockItem(BlocksPM.MARBLE_ENCHANTED_BRICKS.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MARBLE_ENCHANTED_BRICK_SLAB = register("marble_enchanted_brick_slab", () -> new BlockItem(BlocksPM.MARBLE_ENCHANTED_BRICK_SLAB.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MARBLE_ENCHANTED_BRICK_STAIRS = register("marble_enchanted_brick_stairs", () -> new BlockItem(BlocksPM.MARBLE_ENCHANTED_BRICK_STAIRS.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MARBLE_ENCHANTED_BRICK_WALL = register("marble_enchanted_brick_wall", () -> new BlockItem(BlocksPM.MARBLE_ENCHANTED_BRICK_WALL.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MARBLE_ENCHANTED_PILLAR = register("marble_enchanted_pillar", () -> new BlockItem(BlocksPM.MARBLE_ENCHANTED_PILLAR.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MARBLE_ENCHANTED_CHISELED = register("marble_enchanted_chiseled", () -> new BlockItem(BlocksPM.MARBLE_ENCHANTED_CHISELED.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MARBLE_ENCHANTED_RUNED = register("marble_enchanted_runed", () -> new BlockItem(BlocksPM.MARBLE_ENCHANTED_RUNED.get(), new Item.Properties()));

    // Register smoked marble block items
    public static final RegistryObject<BlockItem> MARBLE_SMOKED = register("marble_smoked", () -> new BlockItem(BlocksPM.MARBLE_SMOKED.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MARBLE_SMOKED_SLAB = register("marble_smoked_slab", () -> new BlockItem(BlocksPM.MARBLE_SMOKED_SLAB.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MARBLE_SMOKED_STAIRS = register("marble_smoked_stairs", () -> new BlockItem(BlocksPM.MARBLE_SMOKED_STAIRS.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MARBLE_SMOKED_WALL = register("marble_smoked_wall", () -> new BlockItem(BlocksPM.MARBLE_SMOKED_WALL.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MARBLE_SMOKED_BRICKS = register("marble_smoked_bricks", () -> new BlockItem(BlocksPM.MARBLE_SMOKED_BRICKS.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MARBLE_SMOKED_BRICK_SLAB = register("marble_smoked_brick_slab", () -> new BlockItem(BlocksPM.MARBLE_SMOKED_BRICK_SLAB.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MARBLE_SMOKED_BRICK_STAIRS = register("marble_smoked_brick_stairs", () -> new BlockItem(BlocksPM.MARBLE_SMOKED_BRICK_STAIRS.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MARBLE_SMOKED_BRICK_WALL = register("marble_smoked_brick_wall", () -> new BlockItem(BlocksPM.MARBLE_SMOKED_BRICK_WALL.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MARBLE_SMOKED_PILLAR = register("marble_smoked_pillar", () -> new BlockItem(BlocksPM.MARBLE_SMOKED_PILLAR.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MARBLE_SMOKED_CHISELED = register("marble_smoked_chiseled", () -> new BlockItem(BlocksPM.MARBLE_SMOKED_CHISELED.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MARBLE_SMOKED_RUNED = register("marble_smoked_runed", () -> new BlockItem(BlocksPM.MARBLE_SMOKED_RUNED.get(), new Item.Properties()));
    
    // Register hallowed marble block items
    public static final RegistryObject<BlockItem> MARBLE_HALLOWED = register("marble_hallowed", () -> new BlockItem(BlocksPM.MARBLE_HALLOWED.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MARBLE_HALLOWED_SLAB = register("marble_hallowed_slab", () -> new BlockItem(BlocksPM.MARBLE_HALLOWED_SLAB.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MARBLE_HALLOWED_STAIRS = register("marble_hallowed_stairs", () -> new BlockItem(BlocksPM.MARBLE_HALLOWED_STAIRS.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MARBLE_HALLOWED_WALL = register("marble_hallowed_wall", () -> new BlockItem(BlocksPM.MARBLE_HALLOWED_WALL.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MARBLE_HALLOWED_BRICKS = register("marble_hallowed_bricks", () -> new BlockItem(BlocksPM.MARBLE_HALLOWED_BRICKS.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MARBLE_HALLOWED_BRICK_SLAB = register("marble_hallowed_brick_slab", () -> new BlockItem(BlocksPM.MARBLE_HALLOWED_BRICK_SLAB.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MARBLE_HALLOWED_BRICK_STAIRS = register("marble_hallowed_brick_stairs", () -> new BlockItem(BlocksPM.MARBLE_HALLOWED_BRICK_STAIRS.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MARBLE_HALLOWED_BRICK_WALL = register("marble_hallowed_brick_wall", () -> new BlockItem(BlocksPM.MARBLE_HALLOWED_BRICK_WALL.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MARBLE_HALLOWED_PILLAR = register("marble_hallowed_pillar", () -> new BlockItem(BlocksPM.MARBLE_HALLOWED_PILLAR.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MARBLE_HALLOWED_CHISELED = register("marble_hallowed_chiseled", () -> new BlockItem(BlocksPM.MARBLE_HALLOWED_CHISELED.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MARBLE_HALLOWED_RUNED = register("marble_hallowed_runed", () -> new BlockItem(BlocksPM.MARBLE_HALLOWED_RUNED.get(), new Item.Properties()));

    // Register sunwood block items
    public static final RegistryObject<BlockItem> SUNWOOD_LOG = register("sunwood_log", () -> new BlockItem(BlocksPM.SUNWOOD_LOG.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> STRIPPED_SUNWOOD_LOG = register("stripped_sunwood_log", () -> new BlockItem(BlocksPM.STRIPPED_SUNWOOD_LOG.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> SUNWOOD_WOOD = register("sunwood_wood", () -> new BlockItem(BlocksPM.SUNWOOD_WOOD.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> STRIPPED_SUNWOOD_WOOD = register("stripped_sunwood_wood", () -> new BlockItem(BlocksPM.STRIPPED_SUNWOOD_WOOD.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> SUNWOOD_LEAVES = register("sunwood_leaves", () -> new BlockItem(BlocksPM.SUNWOOD_LEAVES.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> SUNWOOD_SAPLING = register("sunwood_sapling", () -> new BlockItem(BlocksPM.SUNWOOD_SAPLING.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> SUNWOOD_PLANKS = register("sunwood_planks", () -> new BlockItem(BlocksPM.SUNWOOD_PLANKS.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> SUNWOOD_SLAB = register("sunwood_slab", () -> new BlockItem(BlocksPM.SUNWOOD_SLAB.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> SUNWOOD_STAIRS = register("sunwood_stairs", () -> new BlockItem(BlocksPM.SUNWOOD_STAIRS.get(), new Item.Properties()));
    public static final RegistryObject<BurnableBlockItem> SUNWOOD_PILLAR = register("sunwood_pillar", () -> new BurnableBlockItem(BlocksPM.SUNWOOD_PILLAR.get(), 300, new Item.Properties()));

    // Register moonwood block items
    public static final RegistryObject<BlockItem> MOONWOOD_LOG = register("moonwood_log", () -> new BlockItem(BlocksPM.MOONWOOD_LOG.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> STRIPPED_MOONWOOD_LOG = register("stripped_moonwood_log", () -> new BlockItem(BlocksPM.STRIPPED_MOONWOOD_LOG.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MOONWOOD_WOOD = register("moonwood_wood", () -> new BlockItem(BlocksPM.MOONWOOD_WOOD.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> STRIPPED_MOONWOOD_WOOD = register("stripped_moonwood_wood", () -> new BlockItem(BlocksPM.STRIPPED_MOONWOOD_WOOD.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MOONWOOD_LEAVES = register("moonwood_leaves", () -> new BlockItem(BlocksPM.MOONWOOD_LEAVES.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MOONWOOD_SAPLING = register("moonwood_sapling", () -> new BlockItem(BlocksPM.MOONWOOD_SAPLING.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MOONWOOD_PLANKS = register("moonwood_planks", () -> new BlockItem(BlocksPM.MOONWOOD_PLANKS.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MOONWOOD_SLAB = register("moonwood_slab", () -> new BlockItem(BlocksPM.MOONWOOD_SLAB.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> MOONWOOD_STAIRS = register("moonwood_stairs", () -> new BlockItem(BlocksPM.MOONWOOD_STAIRS.get(), new Item.Properties()));
    public static final RegistryObject<BurnableBlockItem> MOONWOOD_PILLAR = register("moonwood_pillar", () -> new BurnableBlockItem(BlocksPM.MOONWOOD_PILLAR.get(), 300, new Item.Properties()));
    
    // Register hallowood block items
    public static final RegistryObject<BlockItem> HALLOWOOD_LOG = register("hallowood_log", () -> new BlockItem(BlocksPM.HALLOWOOD_LOG.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> STRIPPED_HALLOWOOD_LOG = register("stripped_hallowood_log", () -> new BlockItem(BlocksPM.STRIPPED_HALLOWOOD_LOG.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> HALLOWOOD_WOOD = register("hallowood_wood", () -> new BlockItem(BlocksPM.HALLOWOOD_WOOD.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> STRIPPED_HALLOWOOD_WOOD = register("stripped_hallowood_wood", () -> new BlockItem(BlocksPM.STRIPPED_HALLOWOOD_WOOD.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> HALLOWOOD_LEAVES = register("hallowood_leaves", () -> new BlockItem(BlocksPM.HALLOWOOD_LEAVES.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> HALLOWOOD_SAPLING = register("hallowood_sapling", () -> new BlockItem(BlocksPM.HALLOWOOD_SAPLING.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> HALLOWOOD_PLANKS = register("hallowood_planks", () -> new BlockItem(BlocksPM.HALLOWOOD_PLANKS.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> HALLOWOOD_SLAB = register("hallowood_slab", () -> new BlockItem(BlocksPM.HALLOWOOD_SLAB.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> HALLOWOOD_STAIRS = register("hallowood_stairs", () -> new BlockItem(BlocksPM.HALLOWOOD_STAIRS.get(), new Item.Properties()));
    public static final RegistryObject<BurnableBlockItem> HALLOWOOD_PILLAR = register("hallowood_pillar", () -> new BurnableBlockItem(BlocksPM.HALLOWOOD_PILLAR.get(), 300, new Item.Properties()));

    // Register infused stone block items
    public static final RegistryObject<BlockItem> INFUSED_STONE_EARTH = register("infused_stone_earth", () -> new BlockItem(BlocksPM.INFUSED_STONE_EARTH.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> INFUSED_STONE_SEA = register("infused_stone_sea", () -> new BlockItem(BlocksPM.INFUSED_STONE_SEA.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> INFUSED_STONE_SKY = register("infused_stone_sky", () -> new BlockItem(BlocksPM.INFUSED_STONE_SKY.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> INFUSED_STONE_SUN = register("infused_stone_sun", () -> new BlockItem(BlocksPM.INFUSED_STONE_SUN.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> INFUSED_STONE_MOON = register("infused_stone_moon", () -> new BlockItem(BlocksPM.INFUSED_STONE_MOON.get(), new Item.Properties()));

    // Register skyglass block items
    public static final RegistryObject<BlockItem> SKYGLASS = register("skyglass", () -> new BlockItem(BlocksPM.SKYGLASS.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> STAINED_SKYGLASS_BLACK = register("stained_skyglass_black", () -> new BlockItem(BlocksPM.STAINED_SKYGLASS_BLACK.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> STAINED_SKYGLASS_BLUE = register("stained_skyglass_blue", () -> new BlockItem(BlocksPM.STAINED_SKYGLASS_BLUE.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> STAINED_SKYGLASS_BROWN = register("stained_skyglass_brown", () -> new BlockItem(BlocksPM.STAINED_SKYGLASS_BROWN.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> STAINED_SKYGLASS_CYAN = register("stained_skyglass_cyan", () -> new BlockItem(BlocksPM.STAINED_SKYGLASS_CYAN.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> STAINED_SKYGLASS_GRAY = register("stained_skyglass_gray", () -> new BlockItem(BlocksPM.STAINED_SKYGLASS_GRAY.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> STAINED_SKYGLASS_GREEN = register("stained_skyglass_green", () -> new BlockItem(BlocksPM.STAINED_SKYGLASS_GREEN.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> STAINED_SKYGLASS_LIGHT_BLUE = register("stained_skyglass_light_blue", () -> new BlockItem(BlocksPM.STAINED_SKYGLASS_LIGHT_BLUE.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> STAINED_SKYGLASS_LIGHT_GRAY = register("stained_skyglass_light_gray", () -> new BlockItem(BlocksPM.STAINED_SKYGLASS_LIGHT_GRAY.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> STAINED_SKYGLASS_LIME = register("stained_skyglass_lime", () -> new BlockItem(BlocksPM.STAINED_SKYGLASS_LIME.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> STAINED_SKYGLASS_MAGENTA = register("stained_skyglass_magenta", () -> new BlockItem(BlocksPM.STAINED_SKYGLASS_MAGENTA.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> STAINED_SKYGLASS_ORANGE = register("stained_skyglass_orange", () -> new BlockItem(BlocksPM.STAINED_SKYGLASS_ORANGE.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> STAINED_SKYGLASS_PINK = register("stained_skyglass_pink", () -> new BlockItem(BlocksPM.STAINED_SKYGLASS_PINK.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> STAINED_SKYGLASS_PURPLE = register("stained_skyglass_purple", () -> new BlockItem(BlocksPM.STAINED_SKYGLASS_PURPLE.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> STAINED_SKYGLASS_RED = register("stained_skyglass_red", () -> new BlockItem(BlocksPM.STAINED_SKYGLASS_RED.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> STAINED_SKYGLASS_WHITE = register("stained_skyglass_white", () -> new BlockItem(BlocksPM.STAINED_SKYGLASS_WHITE.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> STAINED_SKYGLASS_YELLOW = register("stained_skyglass_yellow", () -> new BlockItem(BlocksPM.STAINED_SKYGLASS_YELLOW.get(), new Item.Properties()));

    // Register skyglass pane block items
    public static final RegistryObject<BlockItem> SKYGLASS_PANE = register("skyglass_pane", () -> new BlockItem(BlocksPM.SKYGLASS_PANE.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> STAINED_SKYGLASS_PANE_BLACK = register("stained_skyglass_pane_black", () -> new BlockItem(BlocksPM.STAINED_SKYGLASS_PANE_BLACK.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> STAINED_SKYGLASS_PANE_BLUE = register("stained_skyglass_pane_blue", () -> new BlockItem(BlocksPM.STAINED_SKYGLASS_PANE_BLUE.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> STAINED_SKYGLASS_PANE_BROWN = register("stained_skyglass_pane_brown", () -> new BlockItem(BlocksPM.STAINED_SKYGLASS_PANE_BROWN.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> STAINED_SKYGLASS_PANE_CYAN = register("stained_skyglass_pane_cyan", () -> new BlockItem(BlocksPM.STAINED_SKYGLASS_PANE_CYAN.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> STAINED_SKYGLASS_PANE_GRAY = register("stained_skyglass_pane_gray", () -> new BlockItem(BlocksPM.STAINED_SKYGLASS_PANE_GRAY.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> STAINED_SKYGLASS_PANE_GREEN = register("stained_skyglass_pane_green", () -> new BlockItem(BlocksPM.STAINED_SKYGLASS_PANE_GREEN.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> STAINED_SKYGLASS_PANE_LIGHT_BLUE = register("stained_skyglass_pane_light_blue", () -> new BlockItem(BlocksPM.STAINED_SKYGLASS_PANE_LIGHT_BLUE.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> STAINED_SKYGLASS_PANE_LIGHT_GRAY = register("stained_skyglass_pane_light_gray", () -> new BlockItem(BlocksPM.STAINED_SKYGLASS_PANE_LIGHT_GRAY.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> STAINED_SKYGLASS_PANE_LIME = register("stained_skyglass_pane_lime", () -> new BlockItem(BlocksPM.STAINED_SKYGLASS_PANE_LIME.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> STAINED_SKYGLASS_PANE_MAGENTA = register("stained_skyglass_pane_magenta", () -> new BlockItem(BlocksPM.STAINED_SKYGLASS_PANE_MAGENTA.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> STAINED_SKYGLASS_PANE_ORANGE = register("stained_skyglass_pane_orange", () -> new BlockItem(BlocksPM.STAINED_SKYGLASS_PANE_ORANGE.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> STAINED_SKYGLASS_PANE_PINK = register("stained_skyglass_pane_pink", () -> new BlockItem(BlocksPM.STAINED_SKYGLASS_PANE_PINK.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> STAINED_SKYGLASS_PANE_PURPLE = register("stained_skyglass_pane_purple", () -> new BlockItem(BlocksPM.STAINED_SKYGLASS_PANE_PURPLE.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> STAINED_SKYGLASS_PANE_RED = register("stained_skyglass_pane_red", () -> new BlockItem(BlocksPM.STAINED_SKYGLASS_PANE_RED.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> STAINED_SKYGLASS_PANE_WHITE = register("stained_skyglass_pane_white", () -> new BlockItem(BlocksPM.STAINED_SKYGLASS_PANE_WHITE.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> STAINED_SKYGLASS_PANE_YELLOW = register("stained_skyglass_pane_yellow", () -> new BlockItem(BlocksPM.STAINED_SKYGLASS_PANE_YELLOW.get(), new Item.Properties()));

    // Register ritual candle block items
    public static final RegistryObject<BlockItem> RITUAL_CANDLE_BLACK = register("ritual_candle_black", () -> new BlockItem(BlocksPM.RITUAL_CANDLE_BLACK.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> RITUAL_CANDLE_BLUE = register("ritual_candle_blue", () -> new BlockItem(BlocksPM.RITUAL_CANDLE_BLUE.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> RITUAL_CANDLE_BROWN = register("ritual_candle_brown", () -> new BlockItem(BlocksPM.RITUAL_CANDLE_BROWN.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> RITUAL_CANDLE_CYAN = register("ritual_candle_cyan", () -> new BlockItem(BlocksPM.RITUAL_CANDLE_CYAN.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> RITUAL_CANDLE_GRAY = register("ritual_candle_gray", () -> new BlockItem(BlocksPM.RITUAL_CANDLE_GRAY.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> RITUAL_CANDLE_GREEN = register("ritual_candle_green", () -> new BlockItem(BlocksPM.RITUAL_CANDLE_GREEN.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> RITUAL_CANDLE_LIGHT_BLUE = register("ritual_candle_light_blue", () -> new BlockItem(BlocksPM.RITUAL_CANDLE_LIGHT_BLUE.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> RITUAL_CANDLE_LIGHT_GRAY = register("ritual_candle_light_gray", () -> new BlockItem(BlocksPM.RITUAL_CANDLE_LIGHT_GRAY.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> RITUAL_CANDLE_LIME = register("ritual_candle_lime", () -> new BlockItem(BlocksPM.RITUAL_CANDLE_LIME.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> RITUAL_CANDLE_MAGENTA = register("ritual_candle_magenta", () -> new BlockItem(BlocksPM.RITUAL_CANDLE_MAGENTA.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> RITUAL_CANDLE_ORANGE = register("ritual_candle_orange", () -> new BlockItem(BlocksPM.RITUAL_CANDLE_ORANGE.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> RITUAL_CANDLE_PINK = register("ritual_candle_pink", () -> new BlockItem(BlocksPM.RITUAL_CANDLE_PINK.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> RITUAL_CANDLE_PURPLE = register("ritual_candle_purple", () -> new BlockItem(BlocksPM.RITUAL_CANDLE_PURPLE.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> RITUAL_CANDLE_RED = register("ritual_candle_red", () -> new BlockItem(BlocksPM.RITUAL_CANDLE_RED.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> RITUAL_CANDLE_WHITE = register("ritual_candle_white", () -> new BlockItem(BlocksPM.RITUAL_CANDLE_WHITE.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> RITUAL_CANDLE_YELLOW = register("ritual_candle_yellow", () -> new BlockItem(BlocksPM.RITUAL_CANDLE_YELLOW.get(), new Item.Properties()));

    // Register mana font block items
    public static final RegistryObject<ManaFontBlockItem> ANCIENT_FONT_EARTH = register("ancient_font_earth", () -> new ManaFontBlockItem(BlocksPM.ANCIENT_FONT_EARTH.get(), new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));
    public static final RegistryObject<ManaFontBlockItem> ANCIENT_FONT_SEA = register("ancient_font_sea", () -> new ManaFontBlockItem(BlocksPM.ANCIENT_FONT_SEA.get(), new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));
    public static final RegistryObject<ManaFontBlockItem> ANCIENT_FONT_SKY = register("ancient_font_sky", () -> new ManaFontBlockItem(BlocksPM.ANCIENT_FONT_SKY.get(), new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));
    public static final RegistryObject<ManaFontBlockItem> ANCIENT_FONT_SUN = register("ancient_font_sun", () -> new ManaFontBlockItem(BlocksPM.ANCIENT_FONT_SUN.get(), new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));
    public static final RegistryObject<ManaFontBlockItem> ANCIENT_FONT_MOON = register("ancient_font_moon", () -> new ManaFontBlockItem(BlocksPM.ANCIENT_FONT_MOON.get(), new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));
    public static final RegistryObject<ManaFontBlockItem> ARTIFICIAL_FONT_EARTH = register("artificial_font_earth", () -> new ManaFontBlockItem(BlocksPM.ARTIFICIAL_FONT_EARTH.get(), new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<ManaFontBlockItem> ARTIFICIAL_FONT_SEA = register("artificial_font_sea", () -> new ManaFontBlockItem(BlocksPM.ARTIFICIAL_FONT_SEA.get(), new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<ManaFontBlockItem> ARTIFICIAL_FONT_SKY = register("artificial_font_sky", () -> new ManaFontBlockItem(BlocksPM.ARTIFICIAL_FONT_SKY.get(), new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<ManaFontBlockItem> ARTIFICIAL_FONT_SUN = register("artificial_font_sun", () -> new ManaFontBlockItem(BlocksPM.ARTIFICIAL_FONT_SUN.get(), new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<ManaFontBlockItem> ARTIFICIAL_FONT_MOON = register("artificial_font_moon", () -> new ManaFontBlockItem(BlocksPM.ARTIFICIAL_FONT_MOON.get(), new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<ManaFontBlockItem> ARTIFICIAL_FONT_BLOOD = register("artificial_font_blood", () -> new ManaFontBlockItem(BlocksPM.ARTIFICIAL_FONT_BLOOD.get(), new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<ManaFontBlockItem> ARTIFICIAL_FONT_INFERNAL = register("artificial_font_infernal", () -> new ManaFontBlockItem(BlocksPM.ARTIFICIAL_FONT_INFERNAL.get(), new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<ManaFontBlockItem> ARTIFICIAL_FONT_VOID = register("artificial_font_void", () -> new ManaFontBlockItem(BlocksPM.ARTIFICIAL_FONT_VOID.get(), new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<ManaFontBlockItem> ARTIFICIAL_FONT_HALLOWED = register("artificial_font_hallowed", () -> new ManaFontBlockItem(BlocksPM.ARTIFICIAL_FONT_HALLOWED.get(), new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<ManaFontBlockItem> FORBIDDEN_FONT_EARTH = register("forbidden_font_earth", () -> new ManaFontBlockItem(BlocksPM.FORBIDDEN_FONT_EARTH.get(), new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));
    public static final RegistryObject<ManaFontBlockItem> FORBIDDEN_FONT_SEA = register("forbidden_font_sea", () -> new ManaFontBlockItem(BlocksPM.FORBIDDEN_FONT_SEA.get(), new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));
    public static final RegistryObject<ManaFontBlockItem> FORBIDDEN_FONT_SKY = register("forbidden_font_sky", () -> new ManaFontBlockItem(BlocksPM.FORBIDDEN_FONT_SKY.get(), new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));
    public static final RegistryObject<ManaFontBlockItem> FORBIDDEN_FONT_SUN = register("forbidden_font_sun", () -> new ManaFontBlockItem(BlocksPM.FORBIDDEN_FONT_SUN.get(), new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));
    public static final RegistryObject<ManaFontBlockItem> FORBIDDEN_FONT_MOON = register("forbidden_font_moon", () -> new ManaFontBlockItem(BlocksPM.FORBIDDEN_FONT_MOON.get(), new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));
    public static final RegistryObject<ManaFontBlockItem> FORBIDDEN_FONT_BLOOD = register("forbidden_font_blood", () -> new ManaFontBlockItem(BlocksPM.FORBIDDEN_FONT_BLOOD.get(), new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));
    public static final RegistryObject<ManaFontBlockItem> FORBIDDEN_FONT_INFERNAL = register("forbidden_font_infernal", () -> new ManaFontBlockItem(BlocksPM.FORBIDDEN_FONT_INFERNAL.get(), new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));
    public static final RegistryObject<ManaFontBlockItem> FORBIDDEN_FONT_VOID = register("forbidden_font_void", () -> new ManaFontBlockItem(BlocksPM.FORBIDDEN_FONT_VOID.get(), new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));
    public static final RegistryObject<ManaFontBlockItem> FORBIDDEN_FONT_HALLOWED = register("forbidden_font_hallowed", () -> new ManaFontBlockItem(BlocksPM.FORBIDDEN_FONT_HALLOWED.get(), new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));
    public static final RegistryObject<ManaFontBlockItem> HEAVENLY_FONT_EARTH = register("heavenly_font_earth", () -> new ManaFontBlockItem(BlocksPM.HEAVENLY_FONT_EARTH.get(), new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));
    public static final RegistryObject<ManaFontBlockItem> HEAVENLY_FONT_SEA = register("heavenly_font_sea", () -> new ManaFontBlockItem(BlocksPM.HEAVENLY_FONT_SEA.get(), new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));
    public static final RegistryObject<ManaFontBlockItem> HEAVENLY_FONT_SKY = register("heavenly_font_sky", () -> new ManaFontBlockItem(BlocksPM.HEAVENLY_FONT_SKY.get(), new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));
    public static final RegistryObject<ManaFontBlockItem> HEAVENLY_FONT_SUN = register("heavenly_font_sun", () -> new ManaFontBlockItem(BlocksPM.HEAVENLY_FONT_SUN.get(), new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));
    public static final RegistryObject<ManaFontBlockItem> HEAVENLY_FONT_MOON = register("heavenly_font_moon", () -> new ManaFontBlockItem(BlocksPM.HEAVENLY_FONT_MOON.get(), new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));
    public static final RegistryObject<ManaFontBlockItem> HEAVENLY_FONT_BLOOD = register("heavenly_font_blood", () -> new ManaFontBlockItem(BlocksPM.HEAVENLY_FONT_BLOOD.get(), new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));
    public static final RegistryObject<ManaFontBlockItem> HEAVENLY_FONT_INFERNAL = register("heavenly_font_infernal", () -> new ManaFontBlockItem(BlocksPM.HEAVENLY_FONT_INFERNAL.get(), new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));
    public static final RegistryObject<ManaFontBlockItem> HEAVENLY_FONT_VOID = register("heavenly_font_void", () -> new ManaFontBlockItem(BlocksPM.HEAVENLY_FONT_VOID.get(), new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));
    public static final RegistryObject<ManaFontBlockItem> HEAVENLY_FONT_HALLOWED = register("heavenly_font_hallowed", () -> new ManaFontBlockItem(BlocksPM.HEAVENLY_FONT_HALLOWED.get(), new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));
    
    // Register device block items
    public static final RegistryObject<BurnableBlockItem> ARCANE_WORKBENCH = register("arcane_workbench", () -> new BurnableBlockItem(BlocksPM.ARCANE_WORKBENCH.get(), 300, new Item.Properties()));
    public static final RegistryObject<BlockItem> WAND_ASSEMBLY_TABLE = register("wand_assembly_table", () -> new BlockItem(BlocksPM.WAND_ASSEMBLY_TABLE.get(), new Item.Properties()));
    public static final RegistryObject<BurnableBlockItem> WOOD_TABLE = register("wood_table", () -> new BurnableBlockItem(BlocksPM.WOOD_TABLE.get(), 300, new Item.Properties()));
    public static final RegistryObject<BurnableBlockItem> ANALYSIS_TABLE = register("analysis_table", () -> new BurnableBlockItem(BlocksPM.ANALYSIS_TABLE.get(), 300, new Item.Properties()));
    public static final RegistryObject<BlockItem> ESSENCE_FURNACE = register("essence_furnace", () -> new BlockItem(BlocksPM.ESSENCE_FURNACE.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> CALCINATOR_BASIC = register("calcinator_basic", () -> new BlockItem(BlocksPM.CALCINATOR_BASIC.get(), new Item.Properties().rarity(Rarity.COMMON)));
    public static final RegistryObject<BlockItem> CALCINATOR_ENCHANTED = register("calcinator_enchanted", () -> new BlockItem(BlocksPM.CALCINATOR_ENCHANTED.get(), new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<BlockItem> CALCINATOR_FORBIDDEN = register("calcinator_forbidden", () -> new BlockItem(BlocksPM.CALCINATOR_FORBIDDEN.get(), new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<BlockItem> CALCINATOR_HEAVENLY = register("calcinator_heavenly", () -> new BlockItem(BlocksPM.CALCINATOR_HEAVENLY.get(), new Item.Properties().rarity(Rarity.EPIC)));
    public static final RegistryObject<BurnableBlockItem> WAND_INSCRIPTION_TABLE = register("wand_inscription_table", () -> new BurnableBlockItem(BlocksPM.WAND_INSCRIPTION_TABLE.get(), 300, new Item.Properties()));
    public static final RegistryObject<SpellcraftingAltarBlockItem> SPELLCRAFTING_ALTAR = register("spellcrafting_altar", () -> new SpellcraftingAltarBlockItem(BlocksPM.SPELLCRAFTING_ALTAR.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> WAND_CHARGER = register("wand_charger", () -> new BlockItem(BlocksPM.WAND_CHARGER.get(), new Item.Properties()));
    public static final RegistryObject<BurnableBlockItem> RESEARCH_TABLE = register("research_table", () -> new BurnableBlockItem(BlocksPM.RESEARCH_TABLE.get(), 300, new Item.Properties()));
    public static final RegistryObject<BlockItem> SUNLAMP = register("sunlamp", () -> new BlockItem(BlocksPM.SUNLAMP.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> SPIRIT_LANTERN = register("spirit_lantern", () -> new BlockItem(BlocksPM.SPIRIT_LANTERN.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> RITUAL_ALTAR = register("ritual_altar", () -> new BlockItem(BlocksPM.RITUAL_ALTAR.get(), new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<BlockItem> OFFERING_PEDESTAL = register("offering_pedestal", () -> new BlockItem(BlocksPM.OFFERING_PEDESTAL.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> INCENSE_BRAZIER = register("incense_brazier", () -> new BlockItem(BlocksPM.INCENSE_BRAZIER.get(), new Item.Properties()));
    public static final RegistryObject<BurnableBlockItem> RITUAL_LECTERN = register("ritual_lectern", () -> new BurnableBlockItem(BlocksPM.RITUAL_LECTERN.get(), 300, new Item.Properties()));
    public static final RegistryObject<BlockItem> RITUAL_BELL = register("ritual_bell", () -> new BlockItem(BlocksPM.RITUAL_BELL.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> BLOODLETTER = register("bloodletter", () -> new BlockItem(BlocksPM.BLOODLETTER.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> SOUL_ANVIL = register("soul_anvil", () -> new BlockItem(BlocksPM.SOUL_ANVIL.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> RUNESCRIBING_ALTAR_BASIC = register("runescribing_altar_basic", () -> new BlockItem(BlocksPM.RUNESCRIBING_ALTAR_BASIC.get(), new Item.Properties().rarity(Rarity.COMMON)));
    public static final RegistryObject<BlockItem> RUNESCRIBING_ALTAR_ENCHANTED = register("runescribing_altar_enchanted", () -> new BlockItem(BlocksPM.RUNESCRIBING_ALTAR_ENCHANTED.get(), new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<BlockItem> RUNESCRIBING_ALTAR_FORBIDDEN = register("runescribing_altar_forbidden", () -> new BlockItem(BlocksPM.RUNESCRIBING_ALTAR_FORBIDDEN.get(), new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<BlockItem> RUNESCRIBING_ALTAR_HEAVENLY = register("runescribing_altar_heavenly", () -> new BlockItem(BlocksPM.RUNESCRIBING_ALTAR_HEAVENLY.get(), new Item.Properties().rarity(Rarity.EPIC)));
    public static final RegistryObject<BurnableBlockItem> RUNECARVING_TABLE = register("runecarving_table", () -> new BurnableBlockItem(BlocksPM.RUNECARVING_TABLE.get(), 300, new Item.Properties()));
    public static final RegistryObject<BlockItem> RUNIC_GRINDSTONE = register("runic_grindstone", () -> new BlockItem(BlocksPM.RUNIC_GRINDSTONE.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> HONEY_EXTRACTOR = register("honey_extractor", () -> new BlockItem(BlocksPM.HONEY_EXTRACTOR.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> PRIMALITE_GOLEM_CONTROLLER = register("primalite_golem_controller", () -> new BlockItem(BlocksPM.PRIMALITE_GOLEM_CONTROLLER.get(), new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<BlockItem> HEXIUM_GOLEM_CONTROLLER = register("hexium_golem_controller", () -> new BlockItem(BlocksPM.HEXIUM_GOLEM_CONTROLLER.get(), new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<BlockItem> HALLOWSTEEL_GOLEM_CONTROLLER = register("hallowsteel_golem_controller", () -> new BlockItem(BlocksPM.HALLOWSTEEL_GOLEM_CONTROLLER.get(), new Item.Properties().rarity(Rarity.EPIC)));
    public static final RegistryObject<BlockItem> SANGUINE_CRUCIBLE = register("sanguine_crucible", () -> new BlockItem(BlocksPM.SANGUINE_CRUCIBLE.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> CONCOCTER = register("concocter", () -> new BlockItem(BlocksPM.CONCOCTER.get(), new Item.Properties()));
    public static final RegistryObject<BurnableBlockItem> CELESTIAL_HARP = register("celestial_harp", () -> new BurnableBlockItem(BlocksPM.CELESTIAL_HARP.get(), 300, new Item.Properties()));
    public static final RegistryObject<BlockItem> ENTROPY_SINK = register("entropy_sink", () -> new BlockItem(BlocksPM.ENTROPY_SINK.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> AUTO_CHARGER = register("auto_charger", () -> new BlockItem(BlocksPM.AUTO_CHARGER.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> ESSENCE_TRANSMUTER = register("essence_transmuter", () -> new BlockItem(BlocksPM.ESSENCE_TRANSMUTER.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> DISSOLUTION_CHAMBER = register("dissolution_chamber", () -> new BlockItem(BlocksPM.DISSOLUTION_CHAMBER.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> ZEPHYR_ENGINE = register("zephyr_engine", () -> new BlockItem(BlocksPM.ZEPHYR_ENGINE.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> VOID_TURBINE = register("void_turbine", () -> new BlockItem(BlocksPM.VOID_TURBINE.get(), new Item.Properties()));
    public static final RegistryObject<BurnableBlockItem> ESSENCE_CASK_ENCHANTED = register("essence_cask_enchanted", () -> new BurnableBlockItem(BlocksPM.ESSENCE_CASK_ENCHANTED.get(), 300, new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<BurnableBlockItem> ESSENCE_CASK_FORBIDDEN = register("essence_cask_forbidden", () -> new BurnableBlockItem(BlocksPM.ESSENCE_CASK_FORBIDDEN.get(), 300, new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<BurnableBlockItem> ESSENCE_CASK_HEAVENLY = register("essence_cask_heavenly", () -> new BurnableBlockItem(BlocksPM.ESSENCE_CASK_HEAVENLY.get(), 300, new Item.Properties().rarity(Rarity.EPIC)));
    public static final RegistryObject<BurnableBlockItem> WAND_GLAMOUR_TABLE = register("wand_glamour_table", () -> new BurnableBlockItem(BlocksPM.WAND_GLAMOUR_TABLE.get(), 300, new Item.Properties()));

    // Register miscellaneous block items
    public static final RegistryObject<ItemNameBlockItem> REFINED_SALT = register("refined_salt", () -> new ItemNameBlockItem(BlocksPM.SALT_TRAIL.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> ROCK_SALT_ORE = register("rock_salt_ore", () -> new BlockItem(BlocksPM.ROCK_SALT_ORE.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> QUARTZ_ORE = register("quartz_ore", () -> new BlockItem(BlocksPM.QUARTZ_ORE.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> PRIMALITE_BLOCK = register("primalite_block", () -> new BlockItem(BlocksPM.PRIMALITE_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> HEXIUM_BLOCK = register("hexium_block", () -> new BlockItem(BlocksPM.HEXIUM_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> HALLOWSTEEL_BLOCK = register("hallowsteel_block", () -> new BlockItem(BlocksPM.HALLOWSTEEL_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<BurnableBlockItem> IGNYX_BLOCK = register("ignyx_block", () -> new BurnableBlockItem(BlocksPM.IGNYX_BLOCK.get(), 128000, new Item.Properties()));
    public static final RegistryObject<BlockItem> SALT_BLOCK = register("salt_block", () -> new BlockItem(BlocksPM.SALT_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<ItemNameBlockItem> TREEFOLK_SEED = register("treefolk_seed", () -> new ItemNameBlockItem(BlocksPM.TREEFOLK_SPROUT.get(), new Item.Properties()));
    
    // Register salted food items
    public static final RegistryObject<Item> SALTED_BAKED_POTATO = register("salted_baked_potato", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationMod(0.72F).build())));
    public static final RegistryObject<Item> SALTED_COOKED_BEEF = register("salted_cooked_beef", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(10).saturationMod(0.96F).meat().build())));
    public static final RegistryObject<Item> SALTED_COOKED_CHICKEN = register("salted_cooked_chicken", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationMod(0.72F).meat().build())));
    public static final RegistryObject<Item> SALTED_COOKED_COD = register("salted_cooked_cod", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationMod(0.72F).build())));
    public static final RegistryObject<Item> SALTED_COOKED_MUTTON = register("salted_cooked_mutton", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationMod(0.96F).meat().build())));
    public static final RegistryObject<Item> SALTED_COOKED_PORKCHOP = register("salted_cooked_porkchop", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(10).saturationMod(0.96F).meat().build())));
    public static final RegistryObject<Item> SALTED_COOKED_RABBIT = register("salted_cooked_rabbit", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationMod(0.72F).meat().build())));
    public static final RegistryObject<Item> SALTED_COOKED_SALMON = register("salted_cooked_salmon", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(7).saturationMod(0.96F).build())));
    public static final RegistryObject<BowlFoodItem> SALTED_BEETROOT_SOUP = register("salted_beetroot_soup", () -> new BowlFoodItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(7).saturationMod(0.72F).build())));
    public static final RegistryObject<BowlFoodItem> SALTED_MUSHROOM_STEW = register("salted_mushroom_stew", () -> new BowlFoodItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(7).saturationMod(0.72F).build())));
    public static final RegistryObject<BowlFoodItem> SALTED_RABBIT_STEW = register("salted_rabbit_stew", () -> new BowlFoodItem(new Item.Properties().stacksTo(1).food(new FoodProperties.Builder().nutrition(12).saturationMod(0.72F).build())));
    
    // Register mineral items
    public static final RegistryObject<Item> IRON_GRIT = register("iron_grit", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GOLD_GRIT = register("gold_grit", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> COPPER_GRIT = register("copper_grit", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PRIMALITE_INGOT = register("primalite_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> HEXIUM_INGOT = register("hexium_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> HALLOWSTEEL_INGOT = register("hallowsteel_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> PRIMALITE_NUGGET = register("primalite_nugget", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> HEXIUM_NUGGET = register("hexium_nugget", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> HALLOWSTEEL_NUGGET = register("hallowsteel_nugget", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> QUARTZ_NUGGET = register("quartz_nugget", () -> new Item(new Item.Properties()));
    
    // Register tool items
    public static final RegistryObject<SwordItem> PRIMALITE_SWORD = register("primalite_sword", () -> new SwordItem(ItemTierPM.PRIMALITE, 3, -2.4F, new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<PrimaliteTridentItem> PRIMALITE_TRIDENT = register("primalite_trident", () -> new PrimaliteTridentItem(new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<TieredBowItem> PRIMALITE_BOW = register("primalite_bow", () -> new TieredBowItem(ItemTierPM.PRIMALITE, new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<ShovelItem> PRIMALITE_SHOVEL = register("primalite_shovel", () -> new ShovelItem(ItemTierPM.PRIMALITE, 1.5F, -3.0F, new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<PickaxeItem> PRIMALITE_PICKAXE = register("primalite_pickaxe", () -> new PickaxeItem(ItemTierPM.PRIMALITE, 1, -2.8F, new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<AxeItem> PRIMALITE_AXE = register("primalite_axe", () -> new AxeItem(ItemTierPM.PRIMALITE, 5.5F, -3.0F, new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<HoeItem> PRIMALITE_HOE = register("primalite_hoe", () -> new HoeItem(ItemTierPM.PRIMALITE, -2, 0.0F, new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<TieredFishingRodItem> PRIMALITE_FISHING_ROD = register("primalite_fishing_rod", () -> new TieredFishingRodItem(ItemTierPM.PRIMALITE, new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<PrimaliteShieldItem> PRIMALITE_SHIELD = register("primalite_shield", () -> new PrimaliteShieldItem(new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<SwordItem> HEXIUM_SWORD = register("hexium_sword", () -> new SwordItem(ItemTierPM.HEXIUM, 3, -2.4F, new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<HexiumTridentItem> HEXIUM_TRIDENT = register("hexium_trident", () -> new HexiumTridentItem(new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<TieredBowItem> HEXIUM_BOW = register("hexium_bow", () -> new TieredBowItem(ItemTierPM.HEXIUM, new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<ShovelItem> HEXIUM_SHOVEL = register("hexium_shovel", () -> new ShovelItem(ItemTierPM.HEXIUM, 1.5F, -3.0F, new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<PickaxeItem> HEXIUM_PICKAXE = register("hexium_pickaxe", () -> new PickaxeItem(ItemTierPM.HEXIUM, 1, -2.8F, new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<AxeItem> HEXIUM_AXE = register("hexium_axe", () -> new AxeItem(ItemTierPM.HEXIUM, 4.0F, -3.0F, new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<HoeItem> HEXIUM_HOE = register("hexium_hoe", () -> new HoeItem(ItemTierPM.HEXIUM, -4, 0.0F, new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<TieredFishingRodItem> HEXIUM_FISHING_ROD = register("hexium_fishing_rod", () -> new TieredFishingRodItem(ItemTierPM.HEXIUM, new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<HexiumShieldItem> HEXIUM_SHIELD = register("hexium_shield", () -> new HexiumShieldItem(new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<SwordItem> HALLOWSTEEL_SWORD = register("hallowsteel_sword", () -> new SwordItem(ItemTierPM.HALLOWSTEEL, 3, -2.4F, new Item.Properties().rarity(Rarity.EPIC)));
    public static final RegistryObject<HallowsteelTridentItem> HALLOWSTEEL_TRIDENT = register("hallowsteel_trident", () -> new HallowsteelTridentItem(new Item.Properties().rarity(Rarity.EPIC)));
    public static final RegistryObject<TieredBowItem> HALLOWSTEEL_BOW = register("hallowsteel_bow", () -> new TieredBowItem(ItemTierPM.HALLOWSTEEL, new Item.Properties().rarity(Rarity.EPIC)));
    public static final RegistryObject<ShovelItem> HALLOWSTEEL_SHOVEL = register("hallowsteel_shovel", () -> new ShovelItem(ItemTierPM.HALLOWSTEEL, 1.5F, -3.0F, new Item.Properties().rarity(Rarity.EPIC)));
    public static final RegistryObject<PickaxeItem> HALLOWSTEEL_PICKAXE = register("hallowsteel_pickaxe", () -> new PickaxeItem(ItemTierPM.HALLOWSTEEL, 1, -2.8F, new Item.Properties().rarity(Rarity.EPIC)));
    public static final RegistryObject<AxeItem> HALLOWSTEEL_AXE = register("hallowsteel_axe", () -> new AxeItem(ItemTierPM.HALLOWSTEEL, 3.5F, -3.0F, new Item.Properties().rarity(Rarity.EPIC)));
    public static final RegistryObject<HoeItem> HALLOWSTEEL_HOE = register("hallowsteel_hoe", () -> new HoeItem(ItemTierPM.HALLOWSTEEL, -5, 0.0F, new Item.Properties().rarity(Rarity.EPIC)));
    public static final RegistryObject<TieredFishingRodItem> HALLOWSTEEL_FISHING_ROD = register("hallowsteel_fishing_rod", () -> new TieredFishingRodItem(ItemTierPM.HALLOWSTEEL, new Item.Properties().rarity(Rarity.EPIC)));
    public static final RegistryObject<HallowsteelShieldItem> HALLOWSTEEL_SHIELD = register("hallowsteel_shield", () -> new HallowsteelShieldItem(new Item.Properties().rarity(Rarity.EPIC)));
    public static final RegistryObject<PrimalShovelItem> PRIMAL_SHOVEL = register("primal_shovel", () -> new PrimalShovelItem(ItemTierPM.PRIMALITE, 1.5F, -3.0F, new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<PrimalFishingRodItem> PRIMAL_FISHING_ROD = register("primal_fishing_rod", () -> new PrimalFishingRodItem(ItemTierPM.PRIMALITE, new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<PrimalAxeItem> PRIMAL_AXE = register("primal_axe", () -> new PrimalAxeItem(ItemTierPM.PRIMALITE, 5.5F, -3.0F, new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<PrimalHoeItem> PRIMAL_HOE = register("primal_hoe", () -> new PrimalHoeItem(ItemTierPM.PRIMALITE, -2, 0.0F, new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<PrimalPickaxeItem> PRIMAL_PICKAXE = register("primal_pickaxe", () -> new PrimalPickaxeItem(ItemTierPM.PRIMALITE, 1, -2.8F, new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<ForbiddenTridentItem> FORBIDDEN_TRIDENT = register("forbidden_trident", () -> new ForbiddenTridentItem(new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<ForbiddenBowItem> FORBIDDEN_BOW = register("forbidden_bow", () -> new ForbiddenBowItem(new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<ForbiddenSwordItem> FORBIDDEN_SWORD = register("forbidden_sword", () -> new ForbiddenSwordItem(ItemTierPM.HEXIUM, 3, -2.4F, new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<SacredShieldItem> SACRED_SHIELD = register("sacred_shield", () -> new SacredShieldItem(new Item.Properties().rarity(Rarity.EPIC)));
    
    // Register mana arrow items
    public static final RegistryObject<ManaArrowItem> MANA_ARROW_EARTH = register("mana_arrow_earth", () -> new ManaArrowItem(Source.EARTH, new Item.Properties()));
    public static final RegistryObject<ManaArrowItem> MANA_ARROW_SEA = register("mana_arrow_sea", () -> new ManaArrowItem(Source.SEA, new Item.Properties()));
    public static final RegistryObject<ManaArrowItem> MANA_ARROW_SKY = register("mana_arrow_sky", () -> new ManaArrowItem(Source.SKY, new Item.Properties()));
    public static final RegistryObject<ManaArrowItem> MANA_ARROW_SUN = register("mana_arrow_sun", () -> new ManaArrowItem(Source.SUN, new Item.Properties()));
    public static final RegistryObject<ManaArrowItem> MANA_ARROW_MOON = register("mana_arrow_moon", () -> new ManaArrowItem(Source.MOON, new Item.Properties()));
    public static final RegistryObject<ManaArrowItem> MANA_ARROW_BLOOD = register("mana_arrow_blood", () -> new ManaArrowItem(Source.BLOOD, new Item.Properties()));
    public static final RegistryObject<ManaArrowItem> MANA_ARROW_INFERNAL = register("mana_arrow_infernal", () -> new ManaArrowItem(Source.INFERNAL, new Item.Properties()));
    public static final RegistryObject<ManaArrowItem> MANA_ARROW_VOID = register("mana_arrow_void", () -> new ManaArrowItem(Source.VOID, new Item.Properties()));
    public static final RegistryObject<ManaArrowItem> MANA_ARROW_HALLOWED = register("mana_arrow_hallowed", () -> new ManaArrowItem(Source.HALLOWED, new Item.Properties()));
    
    // Register armor items
    public static final RegistryObject<RobeArmorItem> IMBUED_WOOL_HEAD = register("imbued_wool_head", () -> new RobeArmorItem(ArmorMaterialPM.IMBUED_WOOL, EquipmentSlot.HEAD, 1, new Item.Properties().rarity(Rarity.COMMON)));
    public static final RegistryObject<RobeArmorItem> IMBUED_WOOL_CHEST = register("imbued_wool_chest", () -> new RobeArmorItem(ArmorMaterialPM.IMBUED_WOOL, EquipmentSlot.CHEST, 2, new Item.Properties().rarity(Rarity.COMMON)));
    public static final RegistryObject<RobeArmorItem> IMBUED_WOOL_LEGS = register("imbued_wool_legs", () -> new RobeArmorItem(ArmorMaterialPM.IMBUED_WOOL, EquipmentSlot.LEGS, 1, new Item.Properties().rarity(Rarity.COMMON)));
    public static final RegistryObject<RobeArmorItem> IMBUED_WOOL_FEET = register("imbued_wool_feet", () -> new RobeArmorItem(ArmorMaterialPM.IMBUED_WOOL, EquipmentSlot.FEET, 1, new Item.Properties().rarity(Rarity.COMMON)));
    public static final RegistryObject<RobeArmorItem> SPELLCLOTH_HEAD = register("spellcloth_head", () -> new RobeArmorItem(ArmorMaterialPM.SPELLCLOTH, EquipmentSlot.HEAD, 2, new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<RobeArmorItem> SPELLCLOTH_CHEST = register("spellcloth_chest", () -> new RobeArmorItem(ArmorMaterialPM.SPELLCLOTH, EquipmentSlot.CHEST, 3, new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<RobeArmorItem> SPELLCLOTH_LEGS = register("spellcloth_legs", () -> new RobeArmorItem(ArmorMaterialPM.SPELLCLOTH, EquipmentSlot.LEGS, 3, new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<RobeArmorItem> SPELLCLOTH_FEET = register("spellcloth_feet", () -> new RobeArmorItem(ArmorMaterialPM.SPELLCLOTH, EquipmentSlot.FEET, 2, new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<RobeArmorItem> HEXWEAVE_HEAD = register("hexweave_head", () -> new RobeArmorItem(ArmorMaterialPM.HEXWEAVE, EquipmentSlot.HEAD, 3, new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<RobeArmorItem> HEXWEAVE_CHEST = register("hexweave_chest", () -> new RobeArmorItem(ArmorMaterialPM.HEXWEAVE, EquipmentSlot.CHEST, 5, new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<RobeArmorItem> HEXWEAVE_LEGS = register("hexweave_legs", () -> new RobeArmorItem(ArmorMaterialPM.HEXWEAVE, EquipmentSlot.LEGS, 4, new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<RobeArmorItem> HEXWEAVE_FEET = register("hexweave_feet", () -> new RobeArmorItem(ArmorMaterialPM.HEXWEAVE, EquipmentSlot.FEET, 3, new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<RobeArmorItem> SAINTSWOOL_HEAD = register("saintswool_head", () -> new RobeArmorItem(ArmorMaterialPM.SAINTSWOOL, EquipmentSlot.HEAD, 4, new Item.Properties().rarity(Rarity.EPIC)));
    public static final RegistryObject<RobeArmorItem> SAINTSWOOL_CHEST = register("saintswool_chest", () -> new RobeArmorItem(ArmorMaterialPM.SAINTSWOOL, EquipmentSlot.CHEST, 6, new Item.Properties().rarity(Rarity.EPIC)));
    public static final RegistryObject<RobeArmorItem> SAINTSWOOL_LEGS = register("saintswool_legs", () -> new RobeArmorItem(ArmorMaterialPM.SAINTSWOOL, EquipmentSlot.LEGS, 6, new Item.Properties().rarity(Rarity.EPIC)));
    public static final RegistryObject<RobeArmorItem> SAINTSWOOL_FEET = register("saintswool_feet", () -> new RobeArmorItem(ArmorMaterialPM.SAINTSWOOL, EquipmentSlot.FEET, 4, new Item.Properties().rarity(Rarity.EPIC)));
    public static final RegistryObject<ArmorItem> PRIMALITE_HEAD = register("primalite_head", () -> new ArmorItem(ArmorMaterialPM.PRIMALITE, EquipmentSlot.HEAD, new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<ArmorItem> PRIMALITE_CHEST = register("primalite_chest", () -> new ArmorItem(ArmorMaterialPM.PRIMALITE, EquipmentSlot.CHEST, new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<ArmorItem> PRIMALITE_LEGS = register("primalite_legs", () -> new ArmorItem(ArmorMaterialPM.PRIMALITE, EquipmentSlot.LEGS, new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<ArmorItem> PRIMALITE_FEET = register("primalite_feet", () -> new ArmorItem(ArmorMaterialPM.PRIMALITE, EquipmentSlot.FEET, new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<ArmorItem> HEXIUM_HEAD = register("hexium_head", () -> new ArmorItem(ArmorMaterialPM.HEXIUM, EquipmentSlot.HEAD, new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<ArmorItem> HEXIUM_CHEST = register("hexium_chest", () -> new ArmorItem(ArmorMaterialPM.HEXIUM, EquipmentSlot.CHEST, new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<ArmorItem> HEXIUM_LEGS = register("hexium_legs", () -> new ArmorItem(ArmorMaterialPM.HEXIUM, EquipmentSlot.LEGS, new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<ArmorItem> HEXIUM_FEET = register("hexium_feet", () -> new ArmorItem(ArmorMaterialPM.HEXIUM, EquipmentSlot.FEET, new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<ArmorItem> HALLOWSTEEL_HEAD = register("hallowsteel_head", () -> new ArmorItem(ArmorMaterialPM.HALLOWSTEEL, EquipmentSlot.HEAD, new Item.Properties().rarity(Rarity.EPIC)));
    public static final RegistryObject<ArmorItem> HALLOWSTEEL_CHEST = register("hallowsteel_chest", () -> new ArmorItem(ArmorMaterialPM.HALLOWSTEEL, EquipmentSlot.CHEST, new Item.Properties().rarity(Rarity.EPIC)));
    public static final RegistryObject<ArmorItem> HALLOWSTEEL_LEGS = register("hallowsteel_legs", () -> new ArmorItem(ArmorMaterialPM.HALLOWSTEEL, EquipmentSlot.LEGS, new Item.Properties().rarity(Rarity.EPIC)));
    public static final RegistryObject<ArmorItem> HALLOWSTEEL_FEET = register("hallowsteel_feet", () -> new ArmorItem(ArmorMaterialPM.HALLOWSTEEL, EquipmentSlot.FEET, new Item.Properties().rarity(Rarity.EPIC)));
    
    // Register miscellaneous items
    public static final RegistryObject<GrimoireItem> GRIMOIRE = register("grimoire", GrimoireItem::new);
    public static final RegistryObject<ArcanometerItem> ARCANOMETER = register("arcanometer", ArcanometerItem::new);
    public static final RegistryObject<Item> MAGNIFYING_GLASS = register("magnifying_glass", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ALCHEMICAL_WASTE = register("alchemical_waste", () -> new Item(new Item.Properties()));
    public static final RegistryObject<BloodyFleshItem> BLOODY_FLESH = register("bloody_flesh", BloodyFleshItem::new);
    public static final RegistryObject<HallowedOrbItem> HALLOWED_ORB = register("hallowed_orb", HallowedOrbItem::new);
    public static final RegistryObject<Item> HEARTWOOD = register("heartwood", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ENCHANTED_INK = register("enchanted_ink", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ENCHANTED_INK_AND_QUILL = register("enchanted_ink_and_quill", EnchantedInkAndQuillItem::new);
    public static final RegistryObject<SeascribePenItem> SEASCRIBE_PEN = register("seascribe_pen", () -> new SeascribePenItem(new Item.Properties()));
    public static final RegistryObject<Item> ROCK_SALT = register("rock_salt", () -> new Item(new Item.Properties()));
    public static final RegistryObject<EarthshatterHammerItem> EARTHSHATTER_HAMMER = register("earthshatter_hammer", EarthshatterHammerItem::new);
    public static final RegistryObject<Item> MANA_PRISM = register("mana_prism", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> TALLOW = register("tallow", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> BEESWAX = register("beeswax", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> MANA_SALTS = register("mana_salts", () -> new Item(new Item.Properties()));
    public static final RegistryObject<ManafruitItem> MANAFRUIT = register("manafruit", () -> new ManafruitItem(0, new Item.Properties().food(new FoodProperties.Builder().nutrition(8).saturationMod(0.8F).build())));
    public static final RegistryObject<Item> INCENSE_STICK = register("incense_stick", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SOUL_GEM = register("soul_gem", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SOUL_GEM_SLIVER = register("soul_gem_sliver", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SPELLCLOTH = register("spellcloth", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> HEXWEAVE = register("hexweave", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SAINTSWOOL = register("saintswool", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> MAGITECH_PARTS_BASIC = register("magitech_parts_basic", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> MAGITECH_PARTS_ENCHANTED = register("magitech_parts_enchanted", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> MAGITECH_PARTS_FORBIDDEN = register("magitech_parts_forbidden", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> MAGITECH_PARTS_HEAVENLY = register("magitech_parts_heavenly", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> FLYING_CARPET = register("flying_carpet", () -> new FlyingCarpetItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<DreamVisionTalismanItem> DREAM_VISION_TALISMAN = register("dream_vision_talisman", DreamVisionTalismanItem::new);
    public static final RegistryObject<IgnyxItem> IGNYX = register("ignyx", () -> new IgnyxItem(new Item.Properties()));
    public static final RegistryObject<DowsingRodItem> DOWSING_ROD = register("dowsing_rod", () -> new DowsingRodItem(new Item.Properties().stacksTo(1).durability(63)));
    public static final RegistryObject<Item> FOUR_LEAF_CLOVER = register("four_leaf_clover", () -> new Item(new Item.Properties()));
    public static final RegistryObject<RecallStoneItem> RECALL_STONE = register("recall_stone", () -> new RecallStoneItem(new Item.Properties()));
    
    // Register knowledge items
    public static final RegistryObject<KnowledgeGainItem> OBSERVATION_NOTES = register("observation_notes", () -> new KnowledgeGainItem(KnowledgeType.OBSERVATION, KnowledgeType.OBSERVATION.getProgression(), new Item.Properties()));
    public static final RegistryObject<KnowledgeGainItem> THEORY_NOTES = register("theory_notes", () -> new KnowledgeGainItem(KnowledgeType.THEORY, KnowledgeType.THEORY.getProgression(), new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<KnowledgeGainItem> MYSTICAL_RELIC = register("mystical_relic", () -> new KnowledgeGainItem(KnowledgeType.THEORY, KnowledgeType.THEORY.getProgression(), new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> MYSTICAL_RELIC_FRAGMENT = register("mystical_relic_fragment", () -> new Item(new Item.Properties()));
    public static final RegistryObject<ForbiddenSourceGainItem> BLOOD_NOTES = register("blood_notes", () -> new ForbiddenSourceGainItem(Source.BLOOD, new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<ResearchGainItem> SHEEP_TOME = register("sheep_tome", () -> new ResearchGainItem(SimpleResearchKey.parse("SPELL_PAYLOAD_POLYMORPH_SHEEP"), new Item.Properties().rarity(Rarity.UNCOMMON)));
    
    // Register dust essence items
    public static final RegistryObject<EssenceItem> ESSENCE_DUST_EARTH = register("essence_dust_earth", () -> new EssenceItem(EssenceType.DUST, Source.EARTH));
    public static final RegistryObject<EssenceItem> ESSENCE_DUST_SEA = register("essence_dust_sea", () -> new EssenceItem(EssenceType.DUST, Source.SEA));
    public static final RegistryObject<EssenceItem> ESSENCE_DUST_SKY = register("essence_dust_sky", () -> new EssenceItem(EssenceType.DUST, Source.SKY));
    public static final RegistryObject<EssenceItem> ESSENCE_DUST_SUN = register("essence_dust_sun", () -> new EssenceItem(EssenceType.DUST, Source.SUN));
    public static final RegistryObject<EssenceItem> ESSENCE_DUST_MOON = register("essence_dust_moon", () -> new EssenceItem(EssenceType.DUST, Source.MOON));
    public static final RegistryObject<EssenceItem> ESSENCE_DUST_BLOOD = register("essence_dust_blood", () -> new EssenceItem(EssenceType.DUST, Source.BLOOD));
    public static final RegistryObject<EssenceItem> ESSENCE_DUST_INFERNAL = register("essence_dust_infernal", () -> new EssenceItem(EssenceType.DUST, Source.INFERNAL));
    public static final RegistryObject<EssenceItem> ESSENCE_DUST_VOID = register("essence_dust_void", () -> new EssenceItem(EssenceType.DUST, Source.VOID));
    public static final RegistryObject<EssenceItem> ESSENCE_DUST_HALLOWED = register("essence_dust_hallowed", () -> new EssenceItem(EssenceType.DUST, Source.HALLOWED));

    // Register shard essence items
    public static final RegistryObject<EssenceItem> ESSENCE_SHARD_EARTH = register("essence_shard_earth", () -> new EssenceItem(EssenceType.SHARD, Source.EARTH));
    public static final RegistryObject<EssenceItem> ESSENCE_SHARD_SEA = register("essence_shard_sea", () -> new EssenceItem(EssenceType.SHARD, Source.SEA));
    public static final RegistryObject<EssenceItem> ESSENCE_SHARD_SKY = register("essence_shard_sky", () -> new EssenceItem(EssenceType.SHARD, Source.SKY));
    public static final RegistryObject<EssenceItem> ESSENCE_SHARD_SUN = register("essence_shard_sun", () -> new EssenceItem(EssenceType.SHARD, Source.SUN));
    public static final RegistryObject<EssenceItem> ESSENCE_SHARD_MOON = register("essence_shard_moon", () -> new EssenceItem(EssenceType.SHARD, Source.MOON));
    public static final RegistryObject<EssenceItem> ESSENCE_SHARD_BLOOD = register("essence_shard_blood", () -> new EssenceItem(EssenceType.SHARD, Source.BLOOD));
    public static final RegistryObject<EssenceItem> ESSENCE_SHARD_INFERNAL = register("essence_shard_infernal", () -> new EssenceItem(EssenceType.SHARD, Source.INFERNAL));
    public static final RegistryObject<EssenceItem> ESSENCE_SHARD_VOID = register("essence_shard_void", () -> new EssenceItem(EssenceType.SHARD, Source.VOID));
    public static final RegistryObject<EssenceItem> ESSENCE_SHARD_HALLOWED = register("essence_shard_hallowed", () -> new EssenceItem(EssenceType.SHARD, Source.HALLOWED));

    // Register crystal essence items
    public static final RegistryObject<EssenceItem> ESSENCE_CRYSTAL_EARTH = register("essence_crystal_earth", () -> new EssenceItem(EssenceType.CRYSTAL, Source.EARTH));
    public static final RegistryObject<EssenceItem> ESSENCE_CRYSTAL_SEA = register("essence_crystal_sea", () -> new EssenceItem(EssenceType.CRYSTAL, Source.SEA));
    public static final RegistryObject<EssenceItem> ESSENCE_CRYSTAL_SKY = register("essence_crystal_sky", () -> new EssenceItem(EssenceType.CRYSTAL, Source.SKY));
    public static final RegistryObject<EssenceItem> ESSENCE_CRYSTAL_SUN = register("essence_crystal_sun", () -> new EssenceItem(EssenceType.CRYSTAL, Source.SUN));
    public static final RegistryObject<EssenceItem> ESSENCE_CRYSTAL_MOON = register("essence_crystal_moon", () -> new EssenceItem(EssenceType.CRYSTAL, Source.MOON));
    public static final RegistryObject<EssenceItem> ESSENCE_CRYSTAL_BLOOD = register("essence_crystal_blood", () -> new EssenceItem(EssenceType.CRYSTAL, Source.BLOOD));
    public static final RegistryObject<EssenceItem> ESSENCE_CRYSTAL_INFERNAL = register("essence_crystal_infernal", () -> new EssenceItem(EssenceType.CRYSTAL, Source.INFERNAL));
    public static final RegistryObject<EssenceItem> ESSENCE_CRYSTAL_VOID = register("essence_crystal_void", () -> new EssenceItem(EssenceType.CRYSTAL, Source.VOID));
    public static final RegistryObject<EssenceItem> ESSENCE_CRYSTAL_HALLOWED = register("essence_crystal_hallowed", () -> new EssenceItem(EssenceType.CRYSTAL, Source.HALLOWED));

    // Register cluster essence items
    public static final RegistryObject<EssenceItem> ESSENCE_CLUSTER_EARTH = register("essence_cluster_earth", () -> new EssenceItem(EssenceType.CLUSTER, Source.EARTH));
    public static final RegistryObject<EssenceItem> ESSENCE_CLUSTER_SEA = register("essence_cluster_sea", () -> new EssenceItem(EssenceType.CLUSTER, Source.SEA));
    public static final RegistryObject<EssenceItem> ESSENCE_CLUSTER_SKY = register("essence_cluster_sky", () -> new EssenceItem(EssenceType.CLUSTER, Source.SKY));
    public static final RegistryObject<EssenceItem> ESSENCE_CLUSTER_SUN = register("essence_cluster_sun", () -> new EssenceItem(EssenceType.CLUSTER, Source.SUN));
    public static final RegistryObject<EssenceItem> ESSENCE_CLUSTER_MOON = register("essence_cluster_moon", () -> new EssenceItem(EssenceType.CLUSTER, Source.MOON));
    public static final RegistryObject<EssenceItem> ESSENCE_CLUSTER_BLOOD = register("essence_cluster_blood", () -> new EssenceItem(EssenceType.CLUSTER, Source.BLOOD));
    public static final RegistryObject<EssenceItem> ESSENCE_CLUSTER_INFERNAL = register("essence_cluster_infernal", () -> new EssenceItem(EssenceType.CLUSTER, Source.INFERNAL));
    public static final RegistryObject<EssenceItem> ESSENCE_CLUSTER_VOID = register("essence_cluster_void", () -> new EssenceItem(EssenceType.CLUSTER, Source.VOID));
    public static final RegistryObject<EssenceItem> ESSENCE_CLUSTER_HALLOWED = register("essence_cluster_hallowed", () -> new EssenceItem(EssenceType.CLUSTER, Source.HALLOWED));
    
    // Register rune items
    public static final RegistryObject<Item> RUNE_UNATTUNED = register("rune_unattuned", () -> new Item(new Item.Properties()));
    public static final RegistryObject<RuneItem> RUNE_EARTH = register("rune_earth", () -> new RuneItem(Rune.EARTH));
    public static final RegistryObject<RuneItem> RUNE_SEA = register("rune_sea", () -> new RuneItem(Rune.SEA));
    public static final RegistryObject<RuneItem> RUNE_SKY = register("rune_sky", () -> new RuneItem(Rune.SKY));
    public static final RegistryObject<RuneItem> RUNE_SUN = register("rune_sun", () -> new RuneItem(Rune.SUN));
    public static final RegistryObject<RuneItem> RUNE_MOON = register("rune_moon", () -> new RuneItem(Rune.MOON));
    public static final RegistryObject<RuneItem> RUNE_BLOOD = register("rune_blood", () -> new RuneItem(Rune.BLOOD));
    public static final RegistryObject<RuneItem> RUNE_INFERNAL = register("rune_infernal", () -> new RuneItem(Rune.INFERNAL));
    public static final RegistryObject<RuneItem> RUNE_VOID = register("rune_void", () -> new RuneItem(Rune.VOID));
    public static final RegistryObject<RuneItem> RUNE_HALLOWED = register("rune_hallowed", () -> new RuneItem(Rune.HALLOWED));
    public static final RegistryObject<RuneItem> RUNE_ABSORB = register("rune_absorb", () -> new RuneItem(Rune.ABSORB));
    public static final RegistryObject<RuneItem> RUNE_DISPEL = register("rune_dispel", () -> new RuneItem(Rune.DISPEL));
    public static final RegistryObject<RuneItem> RUNE_PROJECT = register("rune_project", () -> new RuneItem(Rune.PROJECT));
    public static final RegistryObject<RuneItem> RUNE_PROTECT = register("rune_protect", () -> new RuneItem(Rune.PROTECT));
    public static final RegistryObject<RuneItem> RUNE_SUMMON = register("rune_summon", () -> new RuneItem(Rune.SUMMON));
    public static final RegistryObject<RuneItem> RUNE_AREA = register("rune_area", () -> new RuneItem(Rune.AREA));
    public static final RegistryObject<RuneItem> RUNE_CREATURE = register("rune_creature", () -> new RuneItem(Rune.CREATURE));
    public static final RegistryObject<RuneItem> RUNE_ITEM = register("rune_item", () -> new RuneItem(Rune.ITEM));
    public static final RegistryObject<RuneItem> RUNE_SELF = register("rune_self", () -> new RuneItem(Rune.SELF));
    public static final RegistryObject<RuneItem> RUNE_POWER = register("rune_power", () -> new RuneItem(Rune.POWER));
    
    // Register ambrosia items
    public static final RegistryObject<AmbrosiaItem> BASIC_EARTH_AMBROSIA = register("ambrosia_basic_earth", () -> new AmbrosiaItem(Source.EARTH, 10, new Item.Properties().rarity(Rarity.UNCOMMON).food(new FoodProperties.Builder().nutrition(4).saturationMod(1.2F).alwaysEat().build())));
    public static final RegistryObject<AmbrosiaItem> BASIC_SEA_AMBROSIA = register("ambrosia_basic_sea", () -> new AmbrosiaItem(Source.SEA, 10, new Item.Properties().rarity(Rarity.UNCOMMON).food(new FoodProperties.Builder().nutrition(4).saturationMod(1.2F).alwaysEat().build())));
    public static final RegistryObject<AmbrosiaItem> BASIC_SKY_AMBROSIA = register("ambrosia_basic_sky", () -> new AmbrosiaItem(Source.SKY, 10, new Item.Properties().rarity(Rarity.UNCOMMON).food(new FoodProperties.Builder().nutrition(4).saturationMod(1.2F).alwaysEat().build())));
    public static final RegistryObject<AmbrosiaItem> BASIC_SUN_AMBROSIA = register("ambrosia_basic_sun", () -> new AmbrosiaItem(Source.SUN, 10, new Item.Properties().rarity(Rarity.UNCOMMON).food(new FoodProperties.Builder().nutrition(4).saturationMod(1.2F).alwaysEat().build())));
    public static final RegistryObject<AmbrosiaItem> BASIC_MOON_AMBROSIA = register("ambrosia_basic_moon", () -> new AmbrosiaItem(Source.MOON, 10, new Item.Properties().rarity(Rarity.UNCOMMON).food(new FoodProperties.Builder().nutrition(4).saturationMod(1.2F).alwaysEat().build())));
    public static final RegistryObject<AmbrosiaItem> BASIC_BLOOD_AMBROSIA = register("ambrosia_basic_blood", () -> new AmbrosiaItem(Source.BLOOD, 10, new Item.Properties().rarity(Rarity.UNCOMMON).food(new FoodProperties.Builder().nutrition(4).saturationMod(1.2F).alwaysEat().build())));
    public static final RegistryObject<AmbrosiaItem> BASIC_INFERNAL_AMBROSIA = register("ambrosia_basic_infernal", () -> new AmbrosiaItem(Source.INFERNAL, 10, new Item.Properties().rarity(Rarity.UNCOMMON).food(new FoodProperties.Builder().nutrition(4).saturationMod(1.2F).alwaysEat().build())));
    public static final RegistryObject<AmbrosiaItem> BASIC_VOID_AMBROSIA = register("ambrosia_basic_void", () -> new AmbrosiaItem(Source.VOID, 10, new Item.Properties().rarity(Rarity.UNCOMMON).food(new FoodProperties.Builder().nutrition(4).saturationMod(1.2F).alwaysEat().build())));
    public static final RegistryObject<AmbrosiaItem> BASIC_HALLOWED_AMBROSIA = register("ambrosia_basic_hallowed", () -> new AmbrosiaItem(Source.HALLOWED, 10, new Item.Properties().rarity(Rarity.UNCOMMON).food(new FoodProperties.Builder().nutrition(4).saturationMod(1.2F).alwaysEat().build())));
    public static final RegistryObject<AmbrosiaItem> GREATER_EARTH_AMBROSIA = register("ambrosia_greater_earth", () -> new AmbrosiaItem(Source.EARTH, 30, new Item.Properties().rarity(Rarity.RARE).food(new FoodProperties.Builder().nutrition(4).saturationMod(1.2F).alwaysEat().build())));
    public static final RegistryObject<AmbrosiaItem> GREATER_SEA_AMBROSIA = register("ambrosia_greater_sea", () -> new AmbrosiaItem(Source.SEA, 30, new Item.Properties().rarity(Rarity.RARE).food(new FoodProperties.Builder().nutrition(4).saturationMod(1.2F).alwaysEat().build())));
    public static final RegistryObject<AmbrosiaItem> GREATER_SKY_AMBROSIA = register("ambrosia_greater_sky", () -> new AmbrosiaItem(Source.SKY, 30, new Item.Properties().rarity(Rarity.RARE).food(new FoodProperties.Builder().nutrition(4).saturationMod(1.2F).alwaysEat().build())));
    public static final RegistryObject<AmbrosiaItem> GREATER_SUN_AMBROSIA = register("ambrosia_greater_sun", () -> new AmbrosiaItem(Source.SUN, 30, new Item.Properties().rarity(Rarity.RARE).food(new FoodProperties.Builder().nutrition(4).saturationMod(1.2F).alwaysEat().build())));
    public static final RegistryObject<AmbrosiaItem> GREATER_MOON_AMBROSIA = register("ambrosia_greater_moon", () -> new AmbrosiaItem(Source.MOON, 30, new Item.Properties().rarity(Rarity.RARE).food(new FoodProperties.Builder().nutrition(4).saturationMod(1.2F).alwaysEat().build())));
    public static final RegistryObject<AmbrosiaItem> GREATER_BLOOD_AMBROSIA = register("ambrosia_greater_blood", () -> new AmbrosiaItem(Source.BLOOD, 30, new Item.Properties().rarity(Rarity.RARE).food(new FoodProperties.Builder().nutrition(4).saturationMod(1.2F).alwaysEat().build())));
    public static final RegistryObject<AmbrosiaItem> GREATER_INFERNAL_AMBROSIA = register("ambrosia_greater_infernal", () -> new AmbrosiaItem(Source.INFERNAL, 30, new Item.Properties().rarity(Rarity.RARE).food(new FoodProperties.Builder().nutrition(4).saturationMod(1.2F).alwaysEat().build())));
    public static final RegistryObject<AmbrosiaItem> GREATER_VOID_AMBROSIA = register("ambrosia_greater_void", () -> new AmbrosiaItem(Source.VOID, 30, new Item.Properties().rarity(Rarity.RARE).food(new FoodProperties.Builder().nutrition(4).saturationMod(1.2F).alwaysEat().build())));
    public static final RegistryObject<AmbrosiaItem> GREATER_HALLOWED_AMBROSIA = register("ambrosia_greater_hallowed", () -> new AmbrosiaItem(Source.HALLOWED, 30, new Item.Properties().rarity(Rarity.RARE).food(new FoodProperties.Builder().nutrition(4).saturationMod(1.2F).alwaysEat().build())));
    public static final RegistryObject<AmbrosiaItem> SUPREME_EARTH_AMBROSIA = register("ambrosia_supreme_earth", () -> new AmbrosiaItem(Source.EARTH, 50, new Item.Properties().rarity(Rarity.EPIC).food(new FoodProperties.Builder().nutrition(4).saturationMod(1.2F).alwaysEat().build())));
    public static final RegistryObject<AmbrosiaItem> SUPREME_SEA_AMBROSIA = register("ambrosia_supreme_sea", () -> new AmbrosiaItem(Source.SEA, 50, new Item.Properties().rarity(Rarity.EPIC).food(new FoodProperties.Builder().nutrition(4).saturationMod(1.2F).alwaysEat().build())));
    public static final RegistryObject<AmbrosiaItem> SUPREME_SKY_AMBROSIA = register("ambrosia_supreme_sky", () -> new AmbrosiaItem(Source.SKY, 50, new Item.Properties().rarity(Rarity.EPIC).food(new FoodProperties.Builder().nutrition(4).saturationMod(1.2F).alwaysEat().build())));
    public static final RegistryObject<AmbrosiaItem> SUPREME_SUN_AMBROSIA = register("ambrosia_supreme_sun", () -> new AmbrosiaItem(Source.SUN, 50, new Item.Properties().rarity(Rarity.EPIC).food(new FoodProperties.Builder().nutrition(4).saturationMod(1.2F).alwaysEat().build())));
    public static final RegistryObject<AmbrosiaItem> SUPREME_MOON_AMBROSIA = register("ambrosia_supreme_moon", () -> new AmbrosiaItem(Source.MOON, 50, new Item.Properties().rarity(Rarity.EPIC).food(new FoodProperties.Builder().nutrition(4).saturationMod(1.2F).alwaysEat().build())));
    public static final RegistryObject<AmbrosiaItem> SUPREME_BLOOD_AMBROSIA = register("ambrosia_supreme_blood", () -> new AmbrosiaItem(Source.BLOOD, 50, new Item.Properties().rarity(Rarity.EPIC).food(new FoodProperties.Builder().nutrition(4).saturationMod(1.2F).alwaysEat().build())));
    public static final RegistryObject<AmbrosiaItem> SUPREME_INFERNAL_AMBROSIA = register("ambrosia_supreme_infernal", () -> new AmbrosiaItem(Source.INFERNAL, 50, new Item.Properties().rarity(Rarity.EPIC).food(new FoodProperties.Builder().nutrition(4).saturationMod(1.2F).alwaysEat().build())));
    public static final RegistryObject<AmbrosiaItem> SUPREME_VOID_AMBROSIA = register("ambrosia_supreme_void", () -> new AmbrosiaItem(Source.VOID, 50, new Item.Properties().rarity(Rarity.EPIC).food(new FoodProperties.Builder().nutrition(4).saturationMod(1.2F).alwaysEat().build())));
    public static final RegistryObject<AmbrosiaItem> SUPREME_HALLOWED_AMBROSIA = register("ambrosia_supreme_hallowed", () -> new AmbrosiaItem(Source.HALLOWED, 50, new Item.Properties().rarity(Rarity.EPIC).food(new FoodProperties.Builder().nutrition(4).saturationMod(1.2F).alwaysEat().build())));
    
    // Register humming artifact items
    public static final RegistryObject<Item> HUMMING_ARTIFACT_UNATTUNED = register("humming_artifact_unattuned", () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<AttunementGainItem> HUMMING_ARTIFACT_EARTH = register("humming_artifact_earth", () -> new AttunementGainItem(Source.EARTH, AttunementType.PERMANENT, 1, new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<AttunementGainItem> HUMMING_ARTIFACT_SEA = register("humming_artifact_sea", () -> new AttunementGainItem(Source.SEA, AttunementType.PERMANENT, 1, new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<AttunementGainItem> HUMMING_ARTIFACT_SKY = register("humming_artifact_sky", () -> new AttunementGainItem(Source.SKY, AttunementType.PERMANENT, 1, new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<AttunementGainItem> HUMMING_ARTIFACT_SUN = register("humming_artifact_sun", () -> new AttunementGainItem(Source.SUN, AttunementType.PERMANENT, 1, new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<AttunementGainItem> HUMMING_ARTIFACT_MOON = register("humming_artifact_moon", () -> new AttunementGainItem(Source.MOON, AttunementType.PERMANENT, 1, new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<AttunementGainItem> HUMMING_ARTIFACT_BLOOD = register("humming_artifact_blood", () -> new AttunementGainItem(Source.BLOOD, AttunementType.PERMANENT, 1, new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<AttunementGainItem> HUMMING_ARTIFACT_INFERNAL = register("humming_artifact_infernal", () -> new AttunementGainItem(Source.INFERNAL, AttunementType.PERMANENT, 1, new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<AttunementGainItem> HUMMING_ARTIFACT_VOID = register("humming_artifact_void", () -> new AttunementGainItem(Source.VOID, AttunementType.PERMANENT, 1, new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<AttunementGainItem> HUMMING_ARTIFACT_HALLOWED = register("humming_artifact_hallowed", () -> new AttunementGainItem(Source.HALLOWED, AttunementType.PERMANENT, 1, new Item.Properties().rarity(Rarity.RARE)));
    
    // Register sanguine core items
    public static final RegistryObject<Item> SANGUINE_CORE_BLANK = register("sanguine_core_blank", () -> new Item(new Item.Properties()));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_ALLAY = register("sanguine_core_allay", () -> new SanguineCoreItem(() -> EntityType.ALLAY, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_AXOLOTL = register("sanguine_core_axolotl", () -> new SanguineCoreItem(() -> EntityType.AXOLOTL, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_BAT = register("sanguine_core_bat", () -> new SanguineCoreItem(() -> EntityType.BAT, 1, new Item.Properties().rarity(Rarity.UNCOMMON).durability(63)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_BEE = register("sanguine_core_bee", () -> new SanguineCoreItem(() -> EntityType.BEE, 2, new Item.Properties().rarity(Rarity.UNCOMMON).durability(31)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_BLAZE = register("sanguine_core_blaze", () -> new SanguineCoreItem(() -> EntityType.BLAZE, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_CAT = register("sanguine_core_cat", () -> new SanguineCoreItem(() -> EntityType.CAT, 1, new Item.Properties().rarity(Rarity.UNCOMMON).durability(63)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_CAVE_SPIDER = register("sanguine_core_cave_spider", () -> new SanguineCoreItem(() -> EntityType.CAVE_SPIDER, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_CHICKEN = register("sanguine_core_chicken", () -> new SanguineCoreItem(() -> EntityType.CHICKEN, 2, new Item.Properties().rarity(Rarity.UNCOMMON).durability(31)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_COD = register("sanguine_core_cod", () -> new SanguineCoreItem(() -> EntityType.COD, 2, new Item.Properties().rarity(Rarity.UNCOMMON).durability(31)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_COW = register("sanguine_core_cow", () -> new SanguineCoreItem(() -> EntityType.COW, 2, new Item.Properties().rarity(Rarity.UNCOMMON).durability(31)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_CREEPER = register("sanguine_core_creeper", () -> new SanguineCoreItem(() -> EntityType.CREEPER, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_DOLPHIN = register("sanguine_core_dolphin", () -> new SanguineCoreItem(() -> EntityType.DOLPHIN, 2, new Item.Properties().rarity(Rarity.UNCOMMON).durability(31)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_DONKEY = register("sanguine_core_donkey", () -> new SanguineCoreItem(() -> EntityType.DONKEY, 2, new Item.Properties().rarity(Rarity.UNCOMMON).durability(31)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_DROWNED = register("sanguine_core_drowned", () -> new SanguineCoreItem(() -> EntityType.DROWNED, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_ELDER_GUARDIAN = register("sanguine_core_elder_guardian", () -> new SanguineCoreItem(() -> EntityType.ELDER_GUARDIAN, 16, new Item.Properties().rarity(Rarity.UNCOMMON).durability(3)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_ENDERMAN = register("sanguine_core_enderman", () -> new SanguineCoreItem(() -> EntityType.ENDERMAN, 8, new Item.Properties().rarity(Rarity.UNCOMMON).durability(7)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_ENDERMITE = register("sanguine_core_endermite", () -> new SanguineCoreItem(() -> EntityType.ENDERMITE, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_EVOKER = register("sanguine_core_evoker", () -> new SanguineCoreItem(() -> EntityType.EVOKER, 16, new Item.Properties().rarity(Rarity.UNCOMMON).durability(3)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_FOX = register("sanguine_core_fox", () -> new SanguineCoreItem(() -> EntityType.FOX, 2, new Item.Properties().rarity(Rarity.UNCOMMON).durability(31)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_FROG = register("sanguine_core_frog", () -> new SanguineCoreItem(() -> EntityType.FROG, 2, new Item.Properties().rarity(Rarity.UNCOMMON).durability(31)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_GHAST = register("sanguine_core_ghast", () -> new SanguineCoreItem(() -> EntityType.GHAST, 8, new Item.Properties().rarity(Rarity.UNCOMMON).durability(7)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_GLOW_SQUID = register("sanguine_core_glow_squid", () -> new SanguineCoreItem(() -> EntityType.GLOW_SQUID, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_GOAT = register("sanguine_core_goat", () -> new SanguineCoreItem(() -> EntityType.GOAT, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_GUARDIAN = register("sanguine_core_guardian", () -> new SanguineCoreItem(() -> EntityType.GUARDIAN, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_HOGLIN = register("sanguine_core_hoglin", () -> new SanguineCoreItem(() -> EntityType.HOGLIN, 2, new Item.Properties().rarity(Rarity.UNCOMMON).durability(31)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_HORSE = register("sanguine_core_horse", () -> new SanguineCoreItem(() -> EntityType.HORSE, 2, new Item.Properties().rarity(Rarity.UNCOMMON).durability(31)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_HUSK = register("sanguine_core_husk", () -> new SanguineCoreItem(() -> EntityType.HUSK, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_LLAMA = register("sanguine_core_llama", () -> new SanguineCoreItem(() -> EntityType.LLAMA, 2, new Item.Properties().rarity(Rarity.UNCOMMON).durability(31)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_MAGMA_CUBE = register("sanguine_core_magma_cube", () -> new SanguineCoreItem(() -> EntityType.MAGMA_CUBE, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_MOOSHROOM = register("sanguine_core_mooshroom", () -> new SanguineCoreItem(() -> EntityType.MOOSHROOM, 2, new Item.Properties().rarity(Rarity.UNCOMMON).durability(31)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_OCELOT = register("sanguine_core_ocelot", () -> new SanguineCoreItem(() -> EntityType.OCELOT, 1, new Item.Properties().rarity(Rarity.UNCOMMON).durability(63)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_PANDA = register("sanguine_core_panda", () -> new SanguineCoreItem(() -> EntityType.PANDA, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_PARROT = register("sanguine_core_parrot", () -> new SanguineCoreItem(() -> EntityType.PARROT, 1, new Item.Properties().rarity(Rarity.UNCOMMON).durability(63)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_PHANTOM = register("sanguine_core_phantom", () -> new SanguineCoreItem(() -> EntityType.PHANTOM, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_PIG = register("sanguine_core_pig", () -> new SanguineCoreItem(() -> EntityType.PIG, 2, new Item.Properties().rarity(Rarity.UNCOMMON).durability(31)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_PIGLIN = register("sanguine_core_piglin", () -> new SanguineCoreItem(() -> EntityType.PIGLIN, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_PIGLIN_BRUTE = register("sanguine_core_piglin_brute", () -> new SanguineCoreItem(() -> EntityType.PIGLIN_BRUTE, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_PILLAGER = register("sanguine_core_pillager", () -> new SanguineCoreItem(() -> EntityType.PILLAGER, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_POLAR_BEAR = register("sanguine_core_polar_bear", () -> new SanguineCoreItem(() -> EntityType.POLAR_BEAR, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_PUFFERFISH = register("sanguine_core_pufferfish", () -> new SanguineCoreItem(() -> EntityType.PUFFERFISH, 2, new Item.Properties().rarity(Rarity.UNCOMMON).durability(31)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_RABBIT = register("sanguine_core_rabbit", () -> new SanguineCoreItem(() -> EntityType.RABBIT, 2, new Item.Properties().rarity(Rarity.UNCOMMON).durability(31)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_RAVAGER = register("sanguine_core_ravager", () -> new SanguineCoreItem(() -> EntityType.RAVAGER, 8, new Item.Properties().rarity(Rarity.UNCOMMON).durability(7)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_SALMON = register("sanguine_core_salmon", () -> new SanguineCoreItem(() -> EntityType.SALMON, 2, new Item.Properties().rarity(Rarity.UNCOMMON).durability(31)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_SHEEP = register("sanguine_core_sheep", () -> new SanguineCoreItem(() -> EntityType.SHEEP, 2, new Item.Properties().rarity(Rarity.UNCOMMON).durability(31)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_SHULKER = register("sanguine_core_shulker", () -> new SanguineCoreItem(() -> EntityType.SHULKER, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_SILVERFISH = register("sanguine_core_silverfish", () -> new SanguineCoreItem(() -> EntityType.SILVERFISH, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_SKELETON = register("sanguine_core_skeleton", () -> new SanguineCoreItem(() -> EntityType.SKELETON, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_SKELETON_HORSE = register("sanguine_core_skeleton_horse", () -> new SanguineCoreItem(() -> EntityType.SKELETON_HORSE, 2, new Item.Properties().rarity(Rarity.UNCOMMON).durability(31)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_SLIME = register("sanguine_core_slime", () -> new SanguineCoreItem(() -> EntityType.SLIME, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_SPIDER = register("sanguine_core_spider", () -> new SanguineCoreItem(() -> EntityType.SPIDER, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_SQUID = register("sanguine_core_squid", () -> new SanguineCoreItem(() -> EntityType.SQUID, 2, new Item.Properties().rarity(Rarity.UNCOMMON).durability(31)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_STRAY = register("sanguine_core_stray", () -> new SanguineCoreItem(() -> EntityType.STRAY, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_STRIDER = register("sanguine_core_strider", () -> new SanguineCoreItem(() -> EntityType.STRIDER, 2, new Item.Properties().rarity(Rarity.UNCOMMON).durability(31)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_TROPICAL_FISH = register("sanguine_core_tropical_fish", () -> new SanguineCoreItem(() -> EntityType.TROPICAL_FISH, 2, new Item.Properties().rarity(Rarity.UNCOMMON).durability(31)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_TURTLE = register("sanguine_core_turtle", () -> new SanguineCoreItem(() -> EntityType.TURTLE, 2, new Item.Properties().rarity(Rarity.UNCOMMON).durability(31)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_VEX = register("sanguine_core_vex", () -> new SanguineCoreItem(() -> EntityType.VEX, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_VILLAGER = register("sanguine_core_villager", () -> new SanguineCoreItem(() -> EntityType.VILLAGER, 8, new Item.Properties().rarity(Rarity.UNCOMMON).durability(7)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_VINDICATOR = register("sanguine_core_vindicator", () -> new SanguineCoreItem(() -> EntityType.VINDICATOR, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_WITCH = register("sanguine_core_witch", () -> new SanguineCoreItem(() -> EntityType.WITCH, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_WITHER_SKELETON = register("sanguine_core_wither_skeleton", () -> new SanguineCoreItem(() -> EntityType.WITHER_SKELETON, 8, new Item.Properties().rarity(Rarity.UNCOMMON).durability(7)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_WOLF = register("sanguine_core_wolf", () -> new SanguineCoreItem(() -> EntityType.WOLF, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_ZOGLIN = register("sanguine_core_zoglin", () -> new SanguineCoreItem(() -> EntityType.ZOGLIN, 2, new Item.Properties().rarity(Rarity.UNCOMMON).durability(31)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_ZOMBIE = register("sanguine_core_zombie", () -> new SanguineCoreItem(() -> EntityType.ZOMBIE, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_ZOMBIE_HORSE = register("sanguine_core_zombie_horse", () -> new SanguineCoreItem(() -> EntityType.ZOMBIE_HORSE, 2, new Item.Properties().rarity(Rarity.UNCOMMON).durability(31)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_ZOMBIE_VILLAGER = register("sanguine_core_zombie_villager", () -> new SanguineCoreItem(() -> EntityType.ZOMBIE_VILLAGER, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_ZOMBIFIED_PIGLIN = register("sanguine_core_zombified_piglin", () -> new SanguineCoreItem(() -> EntityType.ZOMBIFIED_PIGLIN, 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_TREEFOLK = register("sanguine_core_treefolk", () -> new SanguineCoreItem(() -> EntityTypesPM.TREEFOLK.get(), 4, new Item.Properties().rarity(Rarity.UNCOMMON).durability(15)));
    public static final RegistryObject<SanguineCoreItem> SANGUINE_CORE_INNER_DEMON = register("sanguine_core_inner_demon", () -> new SanguineCoreItem(() -> EntityTypesPM.INNER_DEMON.get(), 64, new Item.Properties().rarity(Rarity.UNCOMMON).durability(0).stacksTo(1)));

    // Register concoction items
    public static final RegistryObject<SkyglassFlaskItem> SKYGLASS_FLASK = register("skyglass_flask", () -> new SkyglassFlaskItem(new Item.Properties()));
    public static final RegistryObject<ConcoctionItem> CONCOCTION = register("concoction", () -> new ConcoctionItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<BombCasingItem> BOMB_CASING = register("bomb_casing", () -> new BombCasingItem(new Item.Properties()));
    public static final RegistryObject<AlchemicalBombItem> ALCHEMICAL_BOMB = register("alchemical_bomb", () -> new AlchemicalBombItem(new Item.Properties().stacksTo(1)));
    
    // Register caster/wand items
    public static final RegistryObject<Item> SPELL_SCROLL_BLANK = register("spell_scroll_blank", () -> new Item(new Item.Properties()));
    public static final RegistryObject<SpellScrollItem> SPELL_SCROLL_FILLED = register("spell_scroll_filled", SpellScrollItem::new);
    public static final RegistryObject<MundaneWandItem> MUNDANE_WAND = register("mundane_wand", MundaneWandItem::new);
    public static final RegistryObject<ModularWandItem> MODULAR_WAND = register("modular_wand", () -> new ModularWandItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<ModularStaffItem> MODULAR_STAFF = register("modular_staff", () -> new ModularStaffItem(5, -2.4F, new Item.Properties().stacksTo(1)));
    public static final RegistryObject<WandCoreItem> HEARTWOOD_WAND_CORE_ITEM = register("heartwood_wand_core_item", () -> new WandCoreItem(WandCore.HEARTWOOD, new Item.Properties()));
    public static final RegistryObject<WandCoreItem> OBSIDIAN_WAND_CORE_ITEM = register("obsidian_wand_core_item", () -> new WandCoreItem(WandCore.OBSIDIAN, new Item.Properties()));
    public static final RegistryObject<WandCoreItem> CORAL_WAND_CORE_ITEM = register("coral_wand_core_item", () -> new WandCoreItem(WandCore.CORAL, new Item.Properties()));
    public static final RegistryObject<WandCoreItem> BAMBOO_WAND_CORE_ITEM = register("bamboo_wand_core_item", () -> new WandCoreItem(WandCore.BAMBOO, new Item.Properties()));
    public static final RegistryObject<WandCoreItem> SUNWOOD_WAND_CORE_ITEM = register("sunwood_wand_core_item", () -> new WandCoreItem(WandCore.SUNWOOD, new Item.Properties()));
    public static final RegistryObject<WandCoreItem> MOONWOOD_WAND_CORE_ITEM = register("moonwood_wand_core_item", () -> new WandCoreItem(WandCore.MOONWOOD, new Item.Properties()));
    public static final RegistryObject<WandCoreItem> BONE_WAND_CORE_ITEM = register("bone_wand_core_item", () -> new WandCoreItem(WandCore.BONE, new Item.Properties()));
    public static final RegistryObject<WandCoreItem> BLAZE_ROD_WAND_CORE_ITEM = register("blaze_rod_wand_core_item", () -> new WandCoreItem(WandCore.BLAZE_ROD, new Item.Properties()));
    public static final RegistryObject<WandCoreItem> PURPUR_WAND_CORE_ITEM = register("purpur_wand_core_item", () -> new WandCoreItem(WandCore.PURPUR, new Item.Properties()));
    public static final RegistryObject<WandCoreItem> PRIMAL_WAND_CORE_ITEM = register("primal_wand_core_item", () -> new WandCoreItem(WandCore.PRIMAL, new Item.Properties()));
    public static final RegistryObject<WandCoreItem> DARK_PRIMAL_WAND_CORE_ITEM = register("dark_primal_wand_core_item", () -> new WandCoreItem(WandCore.DARK_PRIMAL, new Item.Properties()));
    public static final RegistryObject<WandCoreItem> PURE_PRIMAL_WAND_CORE_ITEM = register("pure_primal_wand_core_item", () -> new WandCoreItem(WandCore.PURE_PRIMAL, new Item.Properties()));
    public static final RegistryObject<WandCapItem> IRON_WAND_CAP_ITEM = register("iron_wand_cap_item", () -> new WandCapItem(WandCap.IRON, new Item.Properties()));
    public static final RegistryObject<WandCapItem> GOLD_WAND_CAP_ITEM = register("gold_wand_cap_item", () -> new WandCapItem(WandCap.GOLD, new Item.Properties()));
    public static final RegistryObject<WandCapItem> PRIMALITE_WAND_CAP_ITEM = register("primalite_wand_cap_item", () -> new WandCapItem(WandCap.PRIMALITE, new Item.Properties()));
    public static final RegistryObject<WandCapItem> HEXIUM_WAND_CAP_ITEM = register("hexium_wand_cap_item", () -> new WandCapItem(WandCap.HEXIUM, new Item.Properties()));
    public static final RegistryObject<WandCapItem> HALLOWSTEEL_WAND_CAP_ITEM = register("hallowsteel_wand_cap_item", () -> new WandCapItem(WandCap.HALLOWSTEEL, new Item.Properties()));
    public static final RegistryObject<WandGemItem> APPRENTICE_WAND_GEM_ITEM = register("apprentice_wand_gem_item", () -> new WandGemItem(WandGem.APPRENTICE, new Item.Properties()));
    public static final RegistryObject<WandGemItem> ADEPT_WAND_GEM_ITEM = register("adept_wand_gem_item", () -> new WandGemItem(WandGem.ADEPT, new Item.Properties()));
    public static final RegistryObject<WandGemItem> WIZARD_WAND_GEM_ITEM = register("wizard_wand_gem_item", () -> new WandGemItem(WandGem.WIZARD, new Item.Properties()));
    public static final RegistryObject<WandGemItem> ARCHMAGE_WAND_GEM_ITEM = register("archmage_wand_gem_item", () -> new WandGemItem(WandGem.ARCHMAGE, new Item.Properties()));
    public static final RegistryObject<WandGemItem> CREATIVE_WAND_GEM_ITEM = register("creative_wand_gem_item", () -> new WandGemItem(WandGem.CREATIVE, new Item.Properties()));
    public static final RegistryObject<StaffCoreItem> HEARTWOOD_STAFF_CORE_ITEM = register("heartwood_staff_core_item", () -> new StaffCoreItem(WandCore.HEARTWOOD, new Item.Properties()));
    public static final RegistryObject<StaffCoreItem> OBSIDIAN_STAFF_CORE_ITEM = register("obsidian_staff_core_item", () -> new StaffCoreItem(WandCore.OBSIDIAN, new Item.Properties()));
    public static final RegistryObject<StaffCoreItem> CORAL_STAFF_CORE_ITEM = register("coral_staff_core_item", () -> new StaffCoreItem(WandCore.CORAL, new Item.Properties()));
    public static final RegistryObject<StaffCoreItem> BAMBOO_STAFF_CORE_ITEM = register("bamboo_staff_core_item", () -> new StaffCoreItem(WandCore.BAMBOO, new Item.Properties()));
    public static final RegistryObject<StaffCoreItem> SUNWOOD_STAFF_CORE_ITEM = register("sunwood_staff_core_item", () -> new StaffCoreItem(WandCore.SUNWOOD, new Item.Properties()));
    public static final RegistryObject<StaffCoreItem> MOONWOOD_STAFF_CORE_ITEM = register("moonwood_staff_core_item", () -> new StaffCoreItem(WandCore.MOONWOOD, new Item.Properties()));
    public static final RegistryObject<StaffCoreItem> BONE_STAFF_CORE_ITEM = register("bone_staff_core_item", () -> new StaffCoreItem(WandCore.BONE, new Item.Properties()));
    public static final RegistryObject<StaffCoreItem> BLAZE_ROD_STAFF_CORE_ITEM = register("blaze_rod_staff_core_item", () -> new StaffCoreItem(WandCore.BLAZE_ROD, new Item.Properties()));
    public static final RegistryObject<StaffCoreItem> PURPUR_STAFF_CORE_ITEM = register("purpur_staff_core_item", () -> new StaffCoreItem(WandCore.PURPUR, new Item.Properties()));
    public static final RegistryObject<StaffCoreItem> PRIMAL_STAFF_CORE_ITEM = register("primal_staff_core_item", () -> new StaffCoreItem(WandCore.PRIMAL, new Item.Properties()));
    public static final RegistryObject<StaffCoreItem> DARK_PRIMAL_STAFF_CORE_ITEM = register("dark_primal_staff_core_item", () -> new StaffCoreItem(WandCore.DARK_PRIMAL, new Item.Properties()));
    public static final RegistryObject<StaffCoreItem> PURE_PRIMAL_STAFF_CORE_ITEM = register("pure_primal_staff_core_item", () -> new StaffCoreItem(WandCore.PURE_PRIMAL, new Item.Properties()));
    
    // Register spawn egg items
    public static final RegistryObject<ForgeSpawnEggItem> TREEFOLK_SPAWN_EGG = register("treefolk_spawn_egg", () -> new ForgeSpawnEggItem(() -> EntityTypesPM.TREEFOLK.get(), 0x76440F, 0x007302, new Item.Properties()));
    public static final RegistryObject<ForgeSpawnEggItem> PRIMALITE_GOLEM_SPAWN_EGG = register("primalite_golem_spawn_egg", () -> new ForgeSpawnEggItem(() -> EntityTypesPM.PRIMALITE_GOLEM.get(), 0x27E1C7, 0x026278, new Item.Properties()));
    public static final RegistryObject<ForgeSpawnEggItem> HEXIUM_GOLEM_SPAWN_EGG = register("hexium_golem_spawn_egg", () -> new ForgeSpawnEggItem(() -> EntityTypesPM.HEXIUM_GOLEM.get(), 0x791E29, 0x100736, new Item.Properties()));
    public static final RegistryObject<ForgeSpawnEggItem> HALLOWSTEEL_GOLEM_SPAWN_EGG = register("hallowsteel_golem_spawn_egg", () -> new ForgeSpawnEggItem(() -> EntityTypesPM.HALLOWSTEEL_GOLEM.get(), 0xFDFFE0, 0xEDE1A2, new Item.Properties()));
    public static final RegistryObject<PixieItem> BASIC_EARTH_PIXIE = register("pixie_basic_earth", () -> new PixieItem(() -> EntityTypesPM.BASIC_EARTH_PIXIE.get(), Source.EARTH, new Item.Properties().stacksTo(16).rarity(Rarity.COMMON)));
    public static final RegistryObject<PixieItem> GRAND_EARTH_PIXIE = register("pixie_grand_earth", () -> new PixieItem(() -> EntityTypesPM.GRAND_EARTH_PIXIE.get(), Source.EARTH, new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<PixieItem> MAJESTIC_EARTH_PIXIE = register("pixie_majestic_earth", () -> new PixieItem(() -> EntityTypesPM.MAJESTIC_EARTH_PIXIE.get(), Source.EARTH, new Item.Properties().stacksTo(16).rarity(Rarity.RARE)));
    public static final RegistryObject<PixieItem> BASIC_SEA_PIXIE = register("pixie_basic_sea", () -> new PixieItem(() -> EntityTypesPM.BASIC_SEA_PIXIE.get(), Source.SEA, new Item.Properties().stacksTo(16).rarity(Rarity.COMMON)));
    public static final RegistryObject<PixieItem> GRAND_SEA_PIXIE = register("pixie_grand_sea", () -> new PixieItem(() -> EntityTypesPM.GRAND_SEA_PIXIE.get(), Source.SEA, new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<PixieItem> MAJESTIC_SEA_PIXIE = register("pixie_majestic_sea", () -> new PixieItem(() -> EntityTypesPM.MAJESTIC_SEA_PIXIE.get(), Source.SEA, new Item.Properties().stacksTo(16).rarity(Rarity.RARE)));
    public static final RegistryObject<PixieItem> BASIC_SKY_PIXIE = register("pixie_basic_sky", () -> new PixieItem(() -> EntityTypesPM.BASIC_SKY_PIXIE.get(), Source.SKY, new Item.Properties().stacksTo(16).rarity(Rarity.COMMON)));
    public static final RegistryObject<PixieItem> GRAND_SKY_PIXIE = register("pixie_grand_sky", () -> new PixieItem(() -> EntityTypesPM.GRAND_SKY_PIXIE.get(), Source.SKY, new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<PixieItem> MAJESTIC_SKY_PIXIE = register("pixie_majestic_sky", () -> new PixieItem(() -> EntityTypesPM.MAJESTIC_SKY_PIXIE.get(), Source.SKY, new Item.Properties().stacksTo(16).rarity(Rarity.RARE)));
    public static final RegistryObject<PixieItem> BASIC_SUN_PIXIE = register("pixie_basic_sun", () -> new PixieItem(() -> EntityTypesPM.BASIC_SUN_PIXIE.get(), Source.SUN, new Item.Properties().stacksTo(16).rarity(Rarity.COMMON)));
    public static final RegistryObject<PixieItem> GRAND_SUN_PIXIE = register("pixie_grand_sun", () -> new PixieItem(() -> EntityTypesPM.GRAND_SUN_PIXIE.get(), Source.SUN, new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<PixieItem> MAJESTIC_SUN_PIXIE = register("pixie_majestic_sun", () -> new PixieItem(() -> EntityTypesPM.MAJESTIC_SUN_PIXIE.get(), Source.SUN, new Item.Properties().stacksTo(16).rarity(Rarity.RARE)));
    public static final RegistryObject<PixieItem> BASIC_MOON_PIXIE = register("pixie_basic_moon", () -> new PixieItem(() -> EntityTypesPM.BASIC_MOON_PIXIE.get(), Source.MOON, new Item.Properties().stacksTo(16).rarity(Rarity.COMMON)));
    public static final RegistryObject<PixieItem> GRAND_MOON_PIXIE = register("pixie_grand_moon", () -> new PixieItem(() -> EntityTypesPM.GRAND_MOON_PIXIE.get(), Source.MOON, new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<PixieItem> MAJESTIC_MOON_PIXIE = register("pixie_majestic_moon", () -> new PixieItem(() -> EntityTypesPM.MAJESTIC_MOON_PIXIE.get(), Source.MOON, new Item.Properties().stacksTo(16).rarity(Rarity.RARE)));
    public static final RegistryObject<PixieItem> BASIC_BLOOD_PIXIE = register("pixie_basic_blood", () -> new PixieItem(() -> EntityTypesPM.BASIC_BLOOD_PIXIE.get(), Source.BLOOD, new Item.Properties().stacksTo(16).rarity(Rarity.COMMON)));
    public static final RegistryObject<PixieItem> GRAND_BLOOD_PIXIE = register("pixie_grand_blood", () -> new PixieItem(() -> EntityTypesPM.GRAND_BLOOD_PIXIE.get(), Source.BLOOD, new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<PixieItem> MAJESTIC_BLOOD_PIXIE = register("pixie_majestic_blood", () -> new PixieItem(() -> EntityTypesPM.MAJESTIC_BLOOD_PIXIE.get(), Source.BLOOD, new Item.Properties().stacksTo(16).rarity(Rarity.RARE)));
    public static final RegistryObject<PixieItem> BASIC_INFERNAL_PIXIE = register("pixie_basic_infernal", () -> new PixieItem(() -> EntityTypesPM.BASIC_INFERNAL_PIXIE.get(), Source.INFERNAL, new Item.Properties().stacksTo(16).rarity(Rarity.COMMON)));
    public static final RegistryObject<PixieItem> GRAND_INFERNAL_PIXIE = register("pixie_grand_infernal", () -> new PixieItem(() -> EntityTypesPM.GRAND_INFERNAL_PIXIE.get(), Source.INFERNAL, new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<PixieItem> MAJESTIC_INFERNAL_PIXIE = register("pixie_majestic_infernal", () -> new PixieItem(() -> EntityTypesPM.MAJESTIC_INFERNAL_PIXIE.get(), Source.INFERNAL, new Item.Properties().stacksTo(16).rarity(Rarity.RARE)));
    public static final RegistryObject<PixieItem> BASIC_VOID_PIXIE = register("pixie_basic_void", () -> new PixieItem(() -> EntityTypesPM.BASIC_VOID_PIXIE.get(), Source.VOID, new Item.Properties().stacksTo(16).rarity(Rarity.COMMON)));
    public static final RegistryObject<PixieItem> GRAND_VOID_PIXIE = register("pixie_grand_void", () -> new PixieItem(() -> EntityTypesPM.GRAND_VOID_PIXIE.get(), Source.VOID, new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<PixieItem> MAJESTIC_VOID_PIXIE = register("pixie_majestic_void", () -> new PixieItem(() -> EntityTypesPM.MAJESTIC_VOID_PIXIE.get(), Source.VOID, new Item.Properties().stacksTo(16).rarity(Rarity.RARE)));
    public static final RegistryObject<PixieItem> BASIC_HALLOWED_PIXIE = register("pixie_basic_hallowed", () -> new PixieItem(() -> EntityTypesPM.BASIC_HALLOWED_PIXIE.get(), Source.HALLOWED, new Item.Properties().stacksTo(16).rarity(Rarity.COMMON)));
    public static final RegistryObject<PixieItem> GRAND_HALLOWED_PIXIE = register("pixie_grand_hallowed", () -> new PixieItem(() -> EntityTypesPM.GRAND_HALLOWED_PIXIE.get(), Source.HALLOWED, new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<PixieItem> MAJESTIC_HALLOWED_PIXIE = register("pixie_majestic_hallowed", () -> new PixieItem(() -> EntityTypesPM.MAJESTIC_HALLOWED_PIXIE.get(), Source.HALLOWED, new Item.Properties().stacksTo(16).rarity(Rarity.RARE)));
    
    // Register drained pixie items
    public static final RegistryObject<Item> DRAINED_BASIC_EARTH_PIXIE = register("drained_pixie_basic_earth", () -> new Item(new Item.Properties().stacksTo(16).rarity(Rarity.COMMON)));
    public static final RegistryObject<Item> DRAINED_GRAND_EARTH_PIXIE = register("drained_pixie_grand_earth", () -> new Item(new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> DRAINED_MAJESTIC_EARTH_PIXIE = register("drained_pixie_majestic_earth", () -> new Item(new Item.Properties().stacksTo(16).rarity(Rarity.RARE)));
    public static final RegistryObject<Item> DRAINED_BASIC_SEA_PIXIE = register("drained_pixie_basic_sea", () -> new Item(new Item.Properties().stacksTo(16).rarity(Rarity.COMMON)));
    public static final RegistryObject<Item> DRAINED_GRAND_SEA_PIXIE = register("drained_pixie_grand_sea", () -> new Item(new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> DRAINED_MAJESTIC_SEA_PIXIE = register("drained_pixie_majestic_sea", () -> new Item(new Item.Properties().stacksTo(16).rarity(Rarity.RARE)));
    public static final RegistryObject<Item> DRAINED_BASIC_SKY_PIXIE = register("drained_pixie_basic_sky", () -> new Item(new Item.Properties().stacksTo(16).rarity(Rarity.COMMON)));
    public static final RegistryObject<Item> DRAINED_GRAND_SKY_PIXIE = register("drained_pixie_grand_sky", () -> new Item(new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> DRAINED_MAJESTIC_SKY_PIXIE = register("drained_pixie_majestic_sky", () -> new Item(new Item.Properties().stacksTo(16).rarity(Rarity.RARE)));
    public static final RegistryObject<Item> DRAINED_BASIC_SUN_PIXIE = register("drained_pixie_basic_sun", () -> new Item(new Item.Properties().stacksTo(16).rarity(Rarity.COMMON)));
    public static final RegistryObject<Item> DRAINED_GRAND_SUN_PIXIE = register("drained_pixie_grand_sun", () -> new Item(new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> DRAINED_MAJESTIC_SUN_PIXIE = register("drained_pixie_majestic_sun", () -> new Item(new Item.Properties().stacksTo(16).rarity(Rarity.RARE)));
    public static final RegistryObject<Item> DRAINED_BASIC_MOON_PIXIE = register("drained_pixie_basic_moon", () -> new Item(new Item.Properties().stacksTo(16).rarity(Rarity.COMMON)));
    public static final RegistryObject<Item> DRAINED_GRAND_MOON_PIXIE = register("drained_pixie_grand_moon", () -> new Item(new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> DRAINED_MAJESTIC_MOON_PIXIE = register("drained_pixie_majestic_moon", () -> new Item(new Item.Properties().stacksTo(16).rarity(Rarity.RARE)));
    public static final RegistryObject<Item> DRAINED_BASIC_BLOOD_PIXIE = register("drained_pixie_basic_blood", () -> new Item(new Item.Properties().stacksTo(16).rarity(Rarity.COMMON)));
    public static final RegistryObject<Item> DRAINED_GRAND_BLOOD_PIXIE = register("drained_pixie_grand_blood", () -> new Item(new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> DRAINED_MAJESTIC_BLOOD_PIXIE = register("drained_pixie_majestic_blood", () -> new Item(new Item.Properties().stacksTo(16).rarity(Rarity.RARE)));
    public static final RegistryObject<Item> DRAINED_BASIC_INFERNAL_PIXIE = register("drained_pixie_basic_infernal", () -> new Item(new Item.Properties().stacksTo(16).fireResistant().rarity(Rarity.COMMON)));
    public static final RegistryObject<Item> DRAINED_GRAND_INFERNAL_PIXIE = register("drained_pixie_grand_infernal", () -> new Item(new Item.Properties().stacksTo(16).fireResistant().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> DRAINED_MAJESTIC_INFERNAL_PIXIE = register("drained_pixie_majestic_infernal", () -> new Item(new Item.Properties().stacksTo(16).fireResistant().rarity(Rarity.RARE)));
    public static final RegistryObject<Item> DRAINED_BASIC_VOID_PIXIE = register("drained_pixie_basic_void", () -> new Item(new Item.Properties().stacksTo(16).rarity(Rarity.COMMON)));
    public static final RegistryObject<Item> DRAINED_GRAND_VOID_PIXIE = register("drained_pixie_grand_void", () -> new Item(new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> DRAINED_MAJESTIC_VOID_PIXIE = register("drained_pixie_majestic_void", () -> new Item(new Item.Properties().stacksTo(16).rarity(Rarity.RARE)));
    public static final RegistryObject<Item> DRAINED_BASIC_HALLOWED_PIXIE = register("drained_pixie_basic_hallowed", () -> new Item(new Item.Properties().stacksTo(16).rarity(Rarity.COMMON)));
    public static final RegistryObject<Item> DRAINED_GRAND_HALLOWED_PIXIE = register("drained_pixie_grand_hallowed", () -> new Item(new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> DRAINED_MAJESTIC_HALLOWED_PIXIE = register("drained_pixie_majestic_hallowed", () -> new Item(new Item.Properties().stacksTo(16).rarity(Rarity.RARE)));
}
