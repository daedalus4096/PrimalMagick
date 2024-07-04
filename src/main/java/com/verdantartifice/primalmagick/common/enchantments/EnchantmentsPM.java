package com.verdantartifice.primalmagick.common.enchantments;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.effects.EffectsPM;
import com.verdantartifice.primalmagick.common.enchantments.effects.ApplyConstantMobEffect;
import com.verdantartifice.primalmagick.common.enchantments.effects.Lifesteal;
import com.verdantartifice.primalmagick.common.tags.ItemTagsPM;

import net.minecraft.advancements.critereon.DamageSourcePredicate;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentTarget;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.Ignite;
import net.minecraft.world.level.storage.loot.predicates.DamageSourceCondition;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Deferred registry for mod enchantments.
 * 
 * @author Daedalus4096
 */
public class EnchantmentsPM {
    public static final ResourceKey<Enchantment> LIFESTEAL = key("lifesteal");
    public static final ResourceKey<Enchantment> ENDERLOCK = key("enderlock");
    
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
    public static final RegistryObject<Enchantment> ESSENCE_THIEF = ENCHANTMENTS.register("essence_thief", () -> new EssenceThiefEnchantment(Enchantment.Rarity.RARE, new EquipmentSlot[] {EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND}));
    public static final RegistryObject<Enchantment> BULWARK = ENCHANTMENTS.register("bulwark", () -> new BulwarkEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.OFFHAND));
    public static final RegistryObject<Enchantment> MAGICK_PROTECTION = ENCHANTMENTS.register("magick_protection", () -> new MagickProtectionEnchantment(Enchantment.Rarity.UNCOMMON, new EquipmentSlot[] {EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET}));
    public static final RegistryObject<Enchantment> GUILLOTINE = ENCHANTMENTS.register("guillotine", () -> new GuillotineEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND));

    public static void bootstrap(BootstrapContext<Enchantment> pContext) {
        HolderGetter<Item> itemHolderGetter = pContext.lookup(Registries.ITEM);

        register(
                pContext,
                LIFESTEAL,
                Enchantment.enchantment(
                        Enchantment.definition(
                                itemHolderGetter.getOrThrow(ItemTagsPM.MELEE_ENCHANTABLE),
                                2,
                                5,
                                Enchantment.dynamicCost(5, 10),
                                Enchantment.dynamicCost(20, 10),
                                4,
                                EquipmentSlotGroup.MAINHAND
                        )
                ).withEffect(
                        EnchantmentEffectComponents.POST_ATTACK,
                        EnchantmentTarget.ATTACKER,
                        EnchantmentTarget.VICTIM,
                        new Lifesteal(LevelBasedValue.perLevel(0.2F)),
                        DamageSourceCondition.hasDamageSource(DamageSourcePredicate.Builder.damageType().isDirect(true))
                )
        );
        register(
                pContext,
                ENDERLOCK,
                Enchantment.enchantment(
                        Enchantment.definition(
                                itemHolderGetter.getOrThrow(ItemTagsPM.MELEE_ENCHANTABLE),
                                2,
                                5,
                                Enchantment.dynamicCost(5, 10),
                                Enchantment.dynamicCost(20, 10),
                                4,
                                EquipmentSlotGroup.MAINHAND
                        )
                ).withEffect(
                        EnchantmentEffectComponents.POST_ATTACK,
                        EnchantmentTarget.ATTACKER,
                        EnchantmentTarget.VICTIM,
                        new ApplyConstantMobEffect(
                                EffectsPM.ENDERLOCK.getHolder().get(),
                                LevelBasedValue.perLevel(0F, 40F),
                                LevelBasedValue.constant(0F)
                        )
                )
        );
    }
    
    private static void register(BootstrapContext<Enchantment> pContext, ResourceKey<Enchantment> pKey, Enchantment.Builder pBuilder) {
        pContext.register(pKey, pBuilder.build(pKey.location()));
    }

    private static ResourceKey<Enchantment> key(String pName) {
        return ResourceKey.create(Registries.ENCHANTMENT, PrimalMagick.resource(pName));
    }
}
