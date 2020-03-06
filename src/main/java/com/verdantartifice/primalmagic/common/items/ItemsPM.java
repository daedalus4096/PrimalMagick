package com.verdantartifice.primalmagic.common.items;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.client.renderers.itemstack.AncientManaFontISTER;
import com.verdantartifice.primalmagic.common.blocks.BlocksPM;
import com.verdantartifice.primalmagic.common.items.essence.EssenceItem;
import com.verdantartifice.primalmagic.common.items.essence.EssenceType;
import com.verdantartifice.primalmagic.common.items.misc.ArcanometerItem;
import com.verdantartifice.primalmagic.common.items.misc.BloodyFleshItem;
import com.verdantartifice.primalmagic.common.items.misc.BurnableBlockItem;
import com.verdantartifice.primalmagic.common.items.misc.EnchantedInkAndQuill;
import com.verdantartifice.primalmagic.common.items.misc.GrimoireItem;
import com.verdantartifice.primalmagic.common.items.misc.HallowedOrbItem;
import com.verdantartifice.primalmagic.common.items.wands.ModularWandItem;
import com.verdantartifice.primalmagic.common.items.wands.MundaneWandItem;
import com.verdantartifice.primalmagic.common.items.wands.SpellScrollItem;
import com.verdantartifice.primalmagic.common.items.wands.WandCapItem;
import com.verdantartifice.primalmagic.common.items.wands.WandCoreItem;
import com.verdantartifice.primalmagic.common.items.wands.WandGemItem;
import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.wands.WandCap;
import com.verdantartifice.primalmagic.common.wands.WandCore;
import com.verdantartifice.primalmagic.common.wands.WandGem;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Rarity;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Deferred registry for mod items.
 * 
 * @author Daedalus4096
 */
public class ItemsPM {
    private static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, PrimalMagic.MODID);
    
    public static void init() {
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
    
    // Register raw marble block items
    public static final RegistryObject<BlockItem> MARBLE_RAW = ITEMS.register("marble_raw", () -> new BlockItem(BlocksPM.MARBLE_RAW.get(), new Item.Properties().group(PrimalMagic.ITEM_GROUP)));
    public static final RegistryObject<BlockItem> MARBLE_SLAB = ITEMS.register("marble_slab", () -> new BlockItem(BlocksPM.MARBLE_SLAB.get(), new Item.Properties().group(PrimalMagic.ITEM_GROUP)));
    public static final RegistryObject<BlockItem> MARBLE_STAIRS = ITEMS.register("marble_stairs", () -> new BlockItem(BlocksPM.MARBLE_STAIRS.get(), new Item.Properties().group(PrimalMagic.ITEM_GROUP)));
    public static final RegistryObject<BlockItem> MARBLE_WALL = ITEMS.register("marble_wall", () -> new BlockItem(BlocksPM.MARBLE_WALL.get(), new Item.Properties().group(PrimalMagic.ITEM_GROUP)));
    public static final RegistryObject<BlockItem> MARBLE_BRICKS = ITEMS.register("marble_bricks", () -> new BlockItem(BlocksPM.MARBLE_BRICKS.get(), new Item.Properties().group(PrimalMagic.ITEM_GROUP)));
    public static final RegistryObject<BlockItem> MARBLE_BRICK_SLAB = ITEMS.register("marble_brick_slab", () -> new BlockItem(BlocksPM.MARBLE_BRICK_SLAB.get(), new Item.Properties().group(PrimalMagic.ITEM_GROUP)));
    public static final RegistryObject<BlockItem> MARBLE_BRICK_STAIRS = ITEMS.register("marble_brick_stairs", () -> new BlockItem(BlocksPM.MARBLE_BRICK_STAIRS.get(), new Item.Properties().group(PrimalMagic.ITEM_GROUP)));
    public static final RegistryObject<BlockItem> MARBLE_BRICK_WALL = ITEMS.register("marble_brick_wall", () -> new BlockItem(BlocksPM.MARBLE_BRICK_WALL.get(), new Item.Properties().group(PrimalMagic.ITEM_GROUP)));
    public static final RegistryObject<BlockItem> MARBLE_PILLAR = ITEMS.register("marble_pillar", () -> new BlockItem(BlocksPM.MARBLE_PILLAR.get(), new Item.Properties().group(PrimalMagic.ITEM_GROUP)));
    public static final RegistryObject<BlockItem> MARBLE_CHISELED = ITEMS.register("marble_chiseled", () -> new BlockItem(BlocksPM.MARBLE_CHISELED.get(), new Item.Properties().group(PrimalMagic.ITEM_GROUP)));
    public static final RegistryObject<BlockItem> MARBLE_RUNED = ITEMS.register("marble_runed", () -> new BlockItem(BlocksPM.MARBLE_RUNED.get(), new Item.Properties().group(PrimalMagic.ITEM_GROUP)));

    // Register enchanted marble block items
    public static final RegistryObject<BlockItem> MARBLE_ENCHANTED = ITEMS.register("marble_enchanted", () -> new BlockItem(BlocksPM.MARBLE_ENCHANTED.get(), new Item.Properties().group(PrimalMagic.ITEM_GROUP)));
    public static final RegistryObject<BlockItem> MARBLE_ENCHANTED_SLAB = ITEMS.register("marble_enchanted_slab", () -> new BlockItem(BlocksPM.MARBLE_ENCHANTED_SLAB.get(), new Item.Properties().group(PrimalMagic.ITEM_GROUP)));
    public static final RegistryObject<BlockItem> MARBLE_ENCHANTED_STAIRS = ITEMS.register("marble_enchanted_stairs", () -> new BlockItem(BlocksPM.MARBLE_ENCHANTED_STAIRS.get(), new Item.Properties().group(PrimalMagic.ITEM_GROUP)));
    public static final RegistryObject<BlockItem> MARBLE_ENCHANTED_WALL = ITEMS.register("marble_enchanted_wall", () -> new BlockItem(BlocksPM.MARBLE_ENCHANTED_WALL.get(), new Item.Properties().group(PrimalMagic.ITEM_GROUP)));
    public static final RegistryObject<BlockItem> MARBLE_ENCHANTED_BRICKS = ITEMS.register("marble_enchanted_bricks", () -> new BlockItem(BlocksPM.MARBLE_ENCHANTED_BRICKS.get(), new Item.Properties().group(PrimalMagic.ITEM_GROUP)));
    public static final RegistryObject<BlockItem> MARBLE_ENCHANTED_BRICK_SLAB = ITEMS.register("marble_enchanted_brick_slab", () -> new BlockItem(BlocksPM.MARBLE_ENCHANTED_BRICK_SLAB.get(), new Item.Properties().group(PrimalMagic.ITEM_GROUP)));
    public static final RegistryObject<BlockItem> MARBLE_ENCHANTED_BRICK_STAIRS = ITEMS.register("marble_enchanted_brick_stairs", () -> new BlockItem(BlocksPM.MARBLE_ENCHANTED_BRICK_STAIRS.get(), new Item.Properties().group(PrimalMagic.ITEM_GROUP)));
    public static final RegistryObject<BlockItem> MARBLE_ENCHANTED_BRICK_WALL = ITEMS.register("marble_enchanted_brick_wall", () -> new BlockItem(BlocksPM.MARBLE_ENCHANTED_BRICK_WALL.get(), new Item.Properties().group(PrimalMagic.ITEM_GROUP)));
    public static final RegistryObject<BlockItem> MARBLE_ENCHANTED_PILLAR = ITEMS.register("marble_enchanted_pillar", () -> new BlockItem(BlocksPM.MARBLE_ENCHANTED_PILLAR.get(), new Item.Properties().group(PrimalMagic.ITEM_GROUP)));
    public static final RegistryObject<BlockItem> MARBLE_ENCHANTED_CHISELED = ITEMS.register("marble_enchanted_chiseled", () -> new BlockItem(BlocksPM.MARBLE_ENCHANTED_CHISELED.get(), new Item.Properties().group(PrimalMagic.ITEM_GROUP)));
    public static final RegistryObject<BlockItem> MARBLE_ENCHANTED_RUNED = ITEMS.register("marble_enchanted_runed", () -> new BlockItem(BlocksPM.MARBLE_ENCHANTED_RUNED.get(), new Item.Properties().group(PrimalMagic.ITEM_GROUP)));

    // Register smoked marble block items
    public static final RegistryObject<BlockItem> MARBLE_SMOKED = ITEMS.register("marble_smoked", () -> new BlockItem(BlocksPM.MARBLE_SMOKED.get(), new Item.Properties().group(PrimalMagic.ITEM_GROUP)));
    public static final RegistryObject<BlockItem> MARBLE_SMOKED_SLAB = ITEMS.register("marble_smoked_slab", () -> new BlockItem(BlocksPM.MARBLE_SMOKED_SLAB.get(), new Item.Properties().group(PrimalMagic.ITEM_GROUP)));
    public static final RegistryObject<BlockItem> MARBLE_SMOKED_STAIRS = ITEMS.register("marble_smoked_stairs", () -> new BlockItem(BlocksPM.MARBLE_SMOKED_STAIRS.get(), new Item.Properties().group(PrimalMagic.ITEM_GROUP)));
    public static final RegistryObject<BlockItem> MARBLE_SMOKED_WALL = ITEMS.register("marble_smoked_wall", () -> new BlockItem(BlocksPM.MARBLE_SMOKED_WALL.get(), new Item.Properties().group(PrimalMagic.ITEM_GROUP)));
    public static final RegistryObject<BlockItem> MARBLE_SMOKED_BRICKS = ITEMS.register("marble_smoked_bricks", () -> new BlockItem(BlocksPM.MARBLE_SMOKED_BRICKS.get(), new Item.Properties().group(PrimalMagic.ITEM_GROUP)));
    public static final RegistryObject<BlockItem> MARBLE_SMOKED_BRICK_SLAB = ITEMS.register("marble_smoked_brick_slab", () -> new BlockItem(BlocksPM.MARBLE_SMOKED_BRICK_SLAB.get(), new Item.Properties().group(PrimalMagic.ITEM_GROUP)));
    public static final RegistryObject<BlockItem> MARBLE_SMOKED_BRICK_STAIRS = ITEMS.register("marble_smoked_brick_stairs", () -> new BlockItem(BlocksPM.MARBLE_SMOKED_BRICK_STAIRS.get(), new Item.Properties().group(PrimalMagic.ITEM_GROUP)));
    public static final RegistryObject<BlockItem> MARBLE_SMOKED_BRICK_WALL = ITEMS.register("marble_smoked_brick_wall", () -> new BlockItem(BlocksPM.MARBLE_SMOKED_BRICK_WALL.get(), new Item.Properties().group(PrimalMagic.ITEM_GROUP)));
    public static final RegistryObject<BlockItem> MARBLE_SMOKED_PILLAR = ITEMS.register("marble_smoked_pillar", () -> new BlockItem(BlocksPM.MARBLE_SMOKED_PILLAR.get(), new Item.Properties().group(PrimalMagic.ITEM_GROUP)));
    public static final RegistryObject<BlockItem> MARBLE_SMOKED_CHISELED = ITEMS.register("marble_smoked_chiseled", () -> new BlockItem(BlocksPM.MARBLE_SMOKED_CHISELED.get(), new Item.Properties().group(PrimalMagic.ITEM_GROUP)));
    public static final RegistryObject<BlockItem> MARBLE_SMOKED_RUNED = ITEMS.register("marble_smoked_runed", () -> new BlockItem(BlocksPM.MARBLE_SMOKED_RUNED.get(), new Item.Properties().group(PrimalMagic.ITEM_GROUP)));

    // Register sunwood block items
    public static final RegistryObject<BlockItem> SUNWOOD_LOG = ITEMS.register("sunwood_log", () -> new BlockItem(BlocksPM.SUNWOOD_LOG.get(), new Item.Properties().group(PrimalMagic.ITEM_GROUP)));
    public static final RegistryObject<BlockItem> STRIPPED_SUNWOOD_LOG = ITEMS.register("stripped_sunwood_log", () -> new BlockItem(BlocksPM.STRIPPED_SUNWOOD_LOG.get(), new Item.Properties().group(PrimalMagic.ITEM_GROUP)));
    public static final RegistryObject<BlockItem> SUNWOOD_WOOD = ITEMS.register("sunwood_wood", () -> new BlockItem(BlocksPM.SUNWOOD_WOOD.get(), new Item.Properties().group(PrimalMagic.ITEM_GROUP)));
    public static final RegistryObject<BlockItem> STRIPPED_SUNWOOD_WOOD = ITEMS.register("stripped_sunwood_wood", () -> new BlockItem(BlocksPM.STRIPPED_SUNWOOD_WOOD.get(), new Item.Properties().group(PrimalMagic.ITEM_GROUP)));
    public static final RegistryObject<BlockItem> SUNWOOD_LEAVES = ITEMS.register("sunwood_leaves", () -> new BlockItem(BlocksPM.SUNWOOD_LEAVES.get(), new Item.Properties().group(PrimalMagic.ITEM_GROUP)));
    public static final RegistryObject<BlockItem> SUNWOOD_SAPLING = ITEMS.register("sunwood_sapling", () -> new BlockItem(BlocksPM.SUNWOOD_SAPLING.get(), new Item.Properties().group(PrimalMagic.ITEM_GROUP)));
    public static final RegistryObject<BlockItem> SUNWOOD_PLANKS = ITEMS.register("sunwood_planks", () -> new BlockItem(BlocksPM.SUNWOOD_PLANKS.get(), new Item.Properties().group(PrimalMagic.ITEM_GROUP)));
    public static final RegistryObject<BlockItem> SUNWOOD_SLAB = ITEMS.register("sunwood_slab", () -> new BlockItem(BlocksPM.SUNWOOD_SLAB.get(), new Item.Properties().group(PrimalMagic.ITEM_GROUP)));
    public static final RegistryObject<BlockItem> SUNWOOD_STAIRS = ITEMS.register("sunwood_stairs", () -> new BlockItem(BlocksPM.SUNWOOD_STAIRS.get(), new Item.Properties().group(PrimalMagic.ITEM_GROUP)));
    public static final RegistryObject<BurnableBlockItem> SUNWOOD_PILLAR = ITEMS.register("sunwood_pillar", () -> new BurnableBlockItem(BlocksPM.SUNWOOD_PILLAR.get(), 300, new Item.Properties().group(PrimalMagic.ITEM_GROUP)));

    // Register moonwood block items
    public static final RegistryObject<BlockItem> MOONWOOD_LOG = ITEMS.register("moonwood_log", () -> new BlockItem(BlocksPM.MOONWOOD_LOG.get(), new Item.Properties().group(PrimalMagic.ITEM_GROUP)));
    public static final RegistryObject<BlockItem> STRIPPED_MOONWOOD_LOG = ITEMS.register("stripped_moonwood_log", () -> new BlockItem(BlocksPM.STRIPPED_MOONWOOD_LOG.get(), new Item.Properties().group(PrimalMagic.ITEM_GROUP)));
    public static final RegistryObject<BlockItem> MOONWOOD_WOOD = ITEMS.register("moonwood_wood", () -> new BlockItem(BlocksPM.MOONWOOD_WOOD.get(), new Item.Properties().group(PrimalMagic.ITEM_GROUP)));
    public static final RegistryObject<BlockItem> STRIPPED_MOONWOOD_WOOD = ITEMS.register("stripped_moonwood_wood", () -> new BlockItem(BlocksPM.STRIPPED_MOONWOOD_WOOD.get(), new Item.Properties().group(PrimalMagic.ITEM_GROUP)));
    public static final RegistryObject<BlockItem> MOONWOOD_LEAVES = ITEMS.register("moonwood_leaves", () -> new BlockItem(BlocksPM.MOONWOOD_LEAVES.get(), new Item.Properties().group(PrimalMagic.ITEM_GROUP)));
    public static final RegistryObject<BlockItem> MOONWOOD_SAPLING = ITEMS.register("moonwood_sapling", () -> new BlockItem(BlocksPM.MOONWOOD_SAPLING.get(), new Item.Properties().group(PrimalMagic.ITEM_GROUP)));
    public static final RegistryObject<BlockItem> MOONWOOD_PLANKS = ITEMS.register("moonwood_planks", () -> new BlockItem(BlocksPM.MOONWOOD_PLANKS.get(), new Item.Properties().group(PrimalMagic.ITEM_GROUP)));
    public static final RegistryObject<BlockItem> MOONWOOD_SLAB = ITEMS.register("moonwood_slab", () -> new BlockItem(BlocksPM.MOONWOOD_SLAB.get(), new Item.Properties().group(PrimalMagic.ITEM_GROUP)));
    public static final RegistryObject<BlockItem> MOONWOOD_STAIRS = ITEMS.register("moonwood_stairs", () -> new BlockItem(BlocksPM.MOONWOOD_STAIRS.get(), new Item.Properties().group(PrimalMagic.ITEM_GROUP)));
    public static final RegistryObject<BurnableBlockItem> MOONWOOD_PILLAR = ITEMS.register("moonwood_pillar", () -> new BurnableBlockItem(BlocksPM.MOONWOOD_PILLAR.get(), 300, new Item.Properties().group(PrimalMagic.ITEM_GROUP)));

    // Register infused stone block items
    public static final RegistryObject<BlockItem> INFUSED_STONE_EARTH = ITEMS.register("infused_stone_earth", () -> new BlockItem(BlocksPM.INFUSED_STONE_EARTH.get(), new Item.Properties().group(PrimalMagic.ITEM_GROUP)));
    public static final RegistryObject<BlockItem> INFUSED_STONE_SEA = ITEMS.register("infused_stone_sea", () -> new BlockItem(BlocksPM.INFUSED_STONE_SEA.get(), new Item.Properties().group(PrimalMagic.ITEM_GROUP)));
    public static final RegistryObject<BlockItem> INFUSED_STONE_SKY = ITEMS.register("infused_stone_sky", () -> new BlockItem(BlocksPM.INFUSED_STONE_SKY.get(), new Item.Properties().group(PrimalMagic.ITEM_GROUP)));
    public static final RegistryObject<BlockItem> INFUSED_STONE_SUN = ITEMS.register("infused_stone_sun", () -> new BlockItem(BlocksPM.INFUSED_STONE_SUN.get(), new Item.Properties().group(PrimalMagic.ITEM_GROUP)));
    public static final RegistryObject<BlockItem> INFUSED_STONE_MOON = ITEMS.register("infused_stone_moon", () -> new BlockItem(BlocksPM.INFUSED_STONE_MOON.get(), new Item.Properties().group(PrimalMagic.ITEM_GROUP)));

    // Register mana font block items
    public static final RegistryObject<BlockItem> ANCIENT_FONT_EARTH = ITEMS.register("ancient_font_earth", () -> new BlockItem(BlocksPM.ANCIENT_FONT_EARTH.get(), new Item.Properties().group(PrimalMagic.ITEM_GROUP).maxStackSize(1).rarity(Rarity.RARE).setISTER(() -> AncientManaFontISTER::new)));
    public static final RegistryObject<BlockItem> ANCIENT_FONT_SEA = ITEMS.register("ancient_font_sea", () -> new BlockItem(BlocksPM.ANCIENT_FONT_SEA.get(), new Item.Properties().group(PrimalMagic.ITEM_GROUP).maxStackSize(1).rarity(Rarity.RARE).setISTER(() -> AncientManaFontISTER::new)));
    public static final RegistryObject<BlockItem> ANCIENT_FONT_SKY = ITEMS.register("ancient_font_sky", () -> new BlockItem(BlocksPM.ANCIENT_FONT_SKY.get(), new Item.Properties().group(PrimalMagic.ITEM_GROUP).maxStackSize(1).rarity(Rarity.RARE).setISTER(() -> AncientManaFontISTER::new)));
    public static final RegistryObject<BlockItem> ANCIENT_FONT_SUN = ITEMS.register("ancient_font_sun", () -> new BlockItem(BlocksPM.ANCIENT_FONT_SUN.get(), new Item.Properties().group(PrimalMagic.ITEM_GROUP).maxStackSize(1).rarity(Rarity.RARE).setISTER(() -> AncientManaFontISTER::new)));
    public static final RegistryObject<BlockItem> ANCIENT_FONT_MOON = ITEMS.register("ancient_font_moon", () -> new BlockItem(BlocksPM.ANCIENT_FONT_MOON.get(), new Item.Properties().group(PrimalMagic.ITEM_GROUP).maxStackSize(1).rarity(Rarity.RARE).setISTER(() -> AncientManaFontISTER::new)));
    
    // Register device block items
    public static final RegistryObject<BurnableBlockItem> ARCANE_WORKBENCH = ITEMS.register("arcane_workbench", () -> new BurnableBlockItem(BlocksPM.ARCANE_WORKBENCH.get(), 300, new Item.Properties().group(PrimalMagic.ITEM_GROUP)));
    public static final RegistryObject<BlockItem> WAND_ASSEMBLY_TABLE = ITEMS.register("wand_assembly_table", () -> new BlockItem(BlocksPM.WAND_ASSEMBLY_TABLE.get(), new Item.Properties().group(PrimalMagic.ITEM_GROUP)));
    public static final RegistryObject<BurnableBlockItem> WOOD_TABLE = ITEMS.register("wood_table", () -> new BurnableBlockItem(BlocksPM.WOOD_TABLE.get(), 300, new Item.Properties().group(PrimalMagic.ITEM_GROUP)));
    public static final RegistryObject<BurnableBlockItem> ANALYSIS_TABLE = ITEMS.register("analysis_table", () -> new BurnableBlockItem(BlocksPM.ANALYSIS_TABLE.get(), 300, new Item.Properties().group(PrimalMagic.ITEM_GROUP)));
    public static final RegistryObject<BlockItem> CALCINATOR = ITEMS.register("calcinator", () -> new BlockItem(BlocksPM.CALCINATOR.get(), new Item.Properties().group(PrimalMagic.ITEM_GROUP)));
    public static final RegistryObject<BurnableBlockItem> WAND_INSCRIPTION_TABLE = ITEMS.register("wand_inscription_table", () -> new BurnableBlockItem(BlocksPM.WAND_INSCRIPTION_TABLE.get(), 300, new Item.Properties().group(PrimalMagic.ITEM_GROUP)));
    public static final RegistryObject<BlockItem> SPELLCRAFTING_ALTAR = ITEMS.register("spellcrafting_altar", () -> new BlockItem(BlocksPM.SPELLCRAFTING_ALTAR.get(), new Item.Properties().group(PrimalMagic.ITEM_GROUP)));
    public static final RegistryObject<BlockItem> WAND_CHARGER = ITEMS.register("wand_charger", () -> new BlockItem(BlocksPM.WAND_CHARGER.get(), new Item.Properties().group(PrimalMagic.ITEM_GROUP)));
    public static final RegistryObject<BurnableBlockItem> RESEARCH_TABLE = ITEMS.register("research_table", () -> new BurnableBlockItem(BlocksPM.RESEARCH_TABLE.get(), 300, new Item.Properties().group(PrimalMagic.ITEM_GROUP)));
    
    // Register miscellaneous items
    public static final RegistryObject<GrimoireItem> GRIMOIRE = ITEMS.register("grimoire", GrimoireItem::new);
    public static final RegistryObject<ArcanometerItem> ARCANOMETER = ITEMS.register("arcanometer", ArcanometerItem::new);
    public static final RegistryObject<Item> MAGNIFYING_GLASS = ITEMS.register("magnifying_glass", () -> new Item(new Item.Properties().group(PrimalMagic.ITEM_GROUP)));
    public static final RegistryObject<Item> ALCHEMICAL_WASTE = ITEMS.register("alchemical_waste", () -> new Item(new Item.Properties().group(PrimalMagic.ITEM_GROUP)));
    public static final RegistryObject<BloodyFleshItem> BLOODY_FLESH = ITEMS.register("bloody_flesh", BloodyFleshItem::new);
    public static final RegistryObject<HallowedOrbItem> HALLOWED_ORB = ITEMS.register("hallowed_orb", HallowedOrbItem::new);
    public static final RegistryObject<Item> HEARTWOOD = ITEMS.register("heartwood", () -> new Item(new Item.Properties().group(PrimalMagic.ITEM_GROUP)));
    public static final RegistryObject<Item> ENCHANTED_INK = ITEMS.register("enchanted_ink", () -> new Item(new Item.Properties().group(PrimalMagic.ITEM_GROUP)));
    public static final RegistryObject<Item> ENCHANTED_INK_AND_QUILL = ITEMS.register("enchanted_ink_and_quill", EnchantedInkAndQuill::new);
    
    // Register dust essence items
    public static final RegistryObject<EssenceItem> ESSENCE_DUST_EARTH = ITEMS.register("essence_dust_earth", () -> new EssenceItem(EssenceType.DUST, Source.EARTH));
    public static final RegistryObject<EssenceItem> ESSENCE_DUST_SEA = ITEMS.register("essence_dust_sea", () -> new EssenceItem(EssenceType.DUST, Source.SEA));
    public static final RegistryObject<EssenceItem> ESSENCE_DUST_SKY = ITEMS.register("essence_dust_sky", () -> new EssenceItem(EssenceType.DUST, Source.SKY));
    public static final RegistryObject<EssenceItem> ESSENCE_DUST_SUN = ITEMS.register("essence_dust_sun", () -> new EssenceItem(EssenceType.DUST, Source.SUN));
    public static final RegistryObject<EssenceItem> ESSENCE_DUST_MOON = ITEMS.register("essence_dust_moon", () -> new EssenceItem(EssenceType.DUST, Source.MOON));
    public static final RegistryObject<EssenceItem> ESSENCE_DUST_BLOOD = ITEMS.register("essence_dust_blood", () -> new EssenceItem(EssenceType.DUST, Source.BLOOD));
    public static final RegistryObject<EssenceItem> ESSENCE_DUST_INFERNAL = ITEMS.register("essence_dust_infernal", () -> new EssenceItem(EssenceType.DUST, Source.INFERNAL));
    public static final RegistryObject<EssenceItem> ESSENCE_DUST_VOID = ITEMS.register("essence_dust_void", () -> new EssenceItem(EssenceType.DUST, Source.VOID));
    public static final RegistryObject<EssenceItem> ESSENCE_DUST_HALLOWED = ITEMS.register("essence_dust_hallowed", () -> new EssenceItem(EssenceType.DUST, Source.HALLOWED));

    // Register shard essence items
    public static final RegistryObject<EssenceItem> ESSENCE_SHARD_EARTH = ITEMS.register("essence_shard_earth", () -> new EssenceItem(EssenceType.SHARD, Source.EARTH));
    public static final RegistryObject<EssenceItem> ESSENCE_SHARD_SEA = ITEMS.register("essence_shard_sea", () -> new EssenceItem(EssenceType.SHARD, Source.SEA));
    public static final RegistryObject<EssenceItem> ESSENCE_SHARD_SKY = ITEMS.register("essence_shard_sky", () -> new EssenceItem(EssenceType.SHARD, Source.SKY));
    public static final RegistryObject<EssenceItem> ESSENCE_SHARD_SUN = ITEMS.register("essence_shard_sun", () -> new EssenceItem(EssenceType.SHARD, Source.SUN));
    public static final RegistryObject<EssenceItem> ESSENCE_SHARD_MOON = ITEMS.register("essence_shard_moon", () -> new EssenceItem(EssenceType.SHARD, Source.MOON));
    public static final RegistryObject<EssenceItem> ESSENCE_SHARD_BLOOD = ITEMS.register("essence_shard_blood", () -> new EssenceItem(EssenceType.SHARD, Source.BLOOD));
    public static final RegistryObject<EssenceItem> ESSENCE_SHARD_INFERNAL = ITEMS.register("essence_shard_infernal", () -> new EssenceItem(EssenceType.SHARD, Source.INFERNAL));
    public static final RegistryObject<EssenceItem> ESSENCE_SHARD_VOID = ITEMS.register("essence_shard_void", () -> new EssenceItem(EssenceType.SHARD, Source.VOID));
    public static final RegistryObject<EssenceItem> ESSENCE_SHARD_HALLOWED = ITEMS.register("essence_shard_hallowed", () -> new EssenceItem(EssenceType.SHARD, Source.HALLOWED));

    // Register crystal essence items
    public static final RegistryObject<EssenceItem> ESSENCE_CRYSTAL_EARTH = ITEMS.register("essence_crystal_earth", () -> new EssenceItem(EssenceType.CRYSTAL, Source.EARTH));
    public static final RegistryObject<EssenceItem> ESSENCE_CRYSTAL_SEA = ITEMS.register("essence_crystal_sea", () -> new EssenceItem(EssenceType.CRYSTAL, Source.SEA));
    public static final RegistryObject<EssenceItem> ESSENCE_CRYSTAL_SKY = ITEMS.register("essence_crystal_sky", () -> new EssenceItem(EssenceType.CRYSTAL, Source.SKY));
    public static final RegistryObject<EssenceItem> ESSENCE_CRYSTAL_SUN = ITEMS.register("essence_crystal_sun", () -> new EssenceItem(EssenceType.CRYSTAL, Source.SUN));
    public static final RegistryObject<EssenceItem> ESSENCE_CRYSTAL_MOON = ITEMS.register("essence_crystal_moon", () -> new EssenceItem(EssenceType.CRYSTAL, Source.MOON));
    public static final RegistryObject<EssenceItem> ESSENCE_CRYSTAL_BLOOD = ITEMS.register("essence_crystal_blood", () -> new EssenceItem(EssenceType.CRYSTAL, Source.BLOOD));
    public static final RegistryObject<EssenceItem> ESSENCE_CRYSTAL_INFERNAL = ITEMS.register("essence_crystal_infernal", () -> new EssenceItem(EssenceType.CRYSTAL, Source.INFERNAL));
    public static final RegistryObject<EssenceItem> ESSENCE_CRYSTAL_VOID = ITEMS.register("essence_crystal_void", () -> new EssenceItem(EssenceType.CRYSTAL, Source.VOID));
    public static final RegistryObject<EssenceItem> ESSENCE_CRYSTAL_HALLOWED = ITEMS.register("essence_crystal_hallowed", () -> new EssenceItem(EssenceType.CRYSTAL, Source.HALLOWED));

    // Register cluster essence items
    public static final RegistryObject<EssenceItem> ESSENCE_CLUSTER_EARTH = ITEMS.register("essence_cluster_earth", () -> new EssenceItem(EssenceType.CLUSTER, Source.EARTH));
    public static final RegistryObject<EssenceItem> ESSENCE_CLUSTER_SEA = ITEMS.register("essence_cluster_sea", () -> new EssenceItem(EssenceType.CLUSTER, Source.SEA));
    public static final RegistryObject<EssenceItem> ESSENCE_CLUSTER_SKY = ITEMS.register("essence_cluster_sky", () -> new EssenceItem(EssenceType.CLUSTER, Source.SKY));
    public static final RegistryObject<EssenceItem> ESSENCE_CLUSTER_SUN = ITEMS.register("essence_cluster_sun", () -> new EssenceItem(EssenceType.CLUSTER, Source.SUN));
    public static final RegistryObject<EssenceItem> ESSENCE_CLUSTER_MOON = ITEMS.register("essence_cluster_moon", () -> new EssenceItem(EssenceType.CLUSTER, Source.MOON));
    public static final RegistryObject<EssenceItem> ESSENCE_CLUSTER_BLOOD = ITEMS.register("essence_cluster_blood", () -> new EssenceItem(EssenceType.CLUSTER, Source.BLOOD));
    public static final RegistryObject<EssenceItem> ESSENCE_CLUSTER_INFERNAL = ITEMS.register("essence_cluster_infernal", () -> new EssenceItem(EssenceType.CLUSTER, Source.INFERNAL));
    public static final RegistryObject<EssenceItem> ESSENCE_CLUSTER_VOID = ITEMS.register("essence_cluster_void", () -> new EssenceItem(EssenceType.CLUSTER, Source.VOID));
    public static final RegistryObject<EssenceItem> ESSENCE_CLUSTER_HALLOWED = ITEMS.register("essence_cluster_hallowed", () -> new EssenceItem(EssenceType.CLUSTER, Source.HALLOWED));

    // Register caster/wand items
    public static final RegistryObject<Item> SPELL_SCROLL_BLANK = ITEMS.register("spell_scroll_blank", () -> new Item(new Item.Properties().group(PrimalMagic.ITEM_GROUP)));
    public static final RegistryObject<SpellScrollItem> SPELL_SCROLL_FILLED = ITEMS.register("spell_scroll_filled", SpellScrollItem::new);
    public static final RegistryObject<MundaneWandItem> MUNDANE_WAND = ITEMS.register("mundane_wand", MundaneWandItem::new);
    public static final RegistryObject<ModularWandItem> MODULAR_WAND = ITEMS.register("modular_wand", ModularWandItem::new);
    public static final RegistryObject<WandCoreItem> HEARTWOOD_WAND_CORE_ITEM = ITEMS.register("heartwood_wand_core_item", () -> new WandCoreItem(WandCore.HEARTWOOD, new Item.Properties().group(PrimalMagic.ITEM_GROUP)));
    public static final RegistryObject<WandCapItem> IRON_WAND_CAP_ITEM = ITEMS.register("iron_wand_cap_item", () -> new WandCapItem(WandCap.IRON, new Item.Properties().group(PrimalMagic.ITEM_GROUP)));
    public static final RegistryObject<WandGemItem> APPRENTICE_WAND_GEM_ITEM = ITEMS.register("apprentice_wand_gem_item", () -> new WandGemItem(WandGem.APPRENTICE, new Item.Properties().group(PrimalMagic.ITEM_GROUP)));
    public static final RegistryObject<WandGemItem> CREATIVE_WAND_GEM_ITEM = ITEMS.register("creative_wand_gem_item", () -> new WandGemItem(WandGem.CREATIVE, new Item.Properties().group(PrimalMagic.ITEM_GROUP)));
}
