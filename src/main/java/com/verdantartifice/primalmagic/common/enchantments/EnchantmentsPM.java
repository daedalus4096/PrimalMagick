package com.verdantartifice.primalmagic.common.enchantments;

import com.verdantartifice.primalmagic.PrimalMagic;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Deferred registry for mod enchantments.
 * 
 * @author Daedalus4096
 */
public class EnchantmentsPM {
    private static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, PrimalMagic.MODID);
    
    public static void init() {
        ENCHANTMENTS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
    
    public static final RegistryObject<Enchantment> LIFESTEAL = ENCHANTMENTS.register("lifesteal", () -> new LifestealEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> ENDERLOCK = ENCHANTMENTS.register("enderlock", () -> new EnderlockEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> JUDGMENT = ENCHANTMENTS.register("judgment", () -> new JudgmentEnchantment(Enchantment.Rarity.VERY_RARE, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> ENDERPORT = ENCHANTMENTS.register("enderport", () -> new EnderportEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> REGROWTH = ENCHANTMENTS.register("regrowth", () -> new RegrowthEnchantment(Enchantment.Rarity.VERY_RARE, EquipmentSlot.values()));
    public static final RegistryObject<Enchantment> AEGIS = ENCHANTMENTS.register("aegis", () -> new AegisEnchantment(Enchantment.Rarity.VERY_RARE, new EquipmentSlot[] {EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET}));
    public static final RegistryObject<Enchantment> MANA_EFFICIENCY = ENCHANTMENTS.register("mana_efficiency", () -> new ManaEfficiencyEnchantment(Enchantment.Rarity.COMMON, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> SPELL_POWER = ENCHANTMENTS.register("spell_power", () -> new SpellPowerEnchantment(Enchantment.Rarity.VERY_RARE, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> TREASURE = ENCHANTMENTS.register("treasure", () -> new TreasureEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> BLUDGEONING = ENCHANTMENTS.register("bludgeoning", () -> new BludgeoningEnchantment(Enchantment.Rarity.COMMON, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> REVERBERATION = ENCHANTMENTS.register("reverberation", () -> new DiggingAreaEnchantment(Enchantment.Rarity.VERY_RARE, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> BOUNTY = ENCHANTMENTS.register("bounty", () -> new BountyEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> DISINTEGRATION = ENCHANTMENTS.register("disintegration", () -> new DiggingAreaEnchantment(Enchantment.Rarity.VERY_RARE, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> VERDANT = ENCHANTMENTS.register("verdant", () -> new VerdantEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> LUCKY_STRIKE = ENCHANTMENTS.register("lucky_strike", () -> new LuckyStrikeEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> RENDING = ENCHANTMENTS.register("rending", () -> new RendingEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> SOULPIERCING = ENCHANTMENTS.register("soulpiercing", () -> new SoulpiercingEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> ESSENCE_THIEF = ENCHANTMENTS.register("essence_thief", () -> new EssenceThiefEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> BULWARK = ENCHANTMENTS.register("bulwark", () -> new BulwarkEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.OFFHAND));
    public static final RegistryObject<Enchantment> MAGIC_PROTECTION = ENCHANTMENTS.register("magic_protection", () -> new MagicProtectionEnchantment(Enchantment.Rarity.UNCOMMON, new EquipmentSlot[] {EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET}));
}
