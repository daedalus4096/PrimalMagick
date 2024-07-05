package com.verdantartifice.primalmagick.common.enchantments;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.effects.EffectsPM;
import com.verdantartifice.primalmagick.common.enchantments.effects.ApplyConstantMobEffect;
import com.verdantartifice.primalmagick.common.enchantments.effects.Lifesteal;
import com.verdantartifice.primalmagick.common.tags.DamageTypeTagsPM;
import com.verdantartifice.primalmagick.common.tags.EnchantmentTagsPM;
import com.verdantartifice.primalmagick.common.tags.ItemTagsPM;

import net.minecraft.advancements.critereon.DamageSourcePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.EntityTypePredicate;
import net.minecraft.advancements.critereon.TagPredicate;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentTarget;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.AddValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentAttributeEffect;
import net.minecraft.world.item.enchantment.effects.Ignite;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.AllOfCondition;
import net.minecraft.world.level.storage.loot.predicates.DamageSourceCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
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
    public static final ResourceKey<Enchantment> JUDGMENT = key("judgment");
    public static final ResourceKey<Enchantment> ENDERPORT = key("enderport");
    public static final ResourceKey<Enchantment> REGROWTH = key("regrowth");
    public static final ResourceKey<Enchantment> AEGIS = key("aegis");
    public static final ResourceKey<Enchantment> MANA_EFFICIENCY = key("mana_efficiency");
    public static final ResourceKey<Enchantment> SPELL_POWER = key("spell_power");
    public static final ResourceKey<Enchantment> TREASURE = key("treasure");
    public static final ResourceKey<Enchantment> BLUDGEONING = key("bludgeoning");
    public static final ResourceKey<Enchantment> REVERBERATION = key("reverberation");
    public static final ResourceKey<Enchantment> BOUNTY = key("bounty");
    
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
        HolderGetter<Enchantment> enchantmentHolderGetter = pContext.lookup(Registries.ENCHANTMENT);

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
                )
                .withEffect(
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
                )
                .withEffect(
                        EnchantmentEffectComponents.POST_ATTACK,
                        EnchantmentTarget.ATTACKER,
                        EnchantmentTarget.VICTIM,
                        new ApplyConstantMobEffect(
                                EffectsPM.ENDERLOCK.getHolder().get(),
                                LevelBasedValue.perLevel(40F),
                                LevelBasedValue.constant(0F)
                        )
                )
        );
        register(
                pContext,
                JUDGMENT,
                Enchantment.enchantment(
                        Enchantment.definition(
                                itemHolderGetter.getOrThrow(ItemTagsPM.MELEE_ENCHANTABLE),
                                1,
                                5,
                                Enchantment.dynamicCost(10, 10),
                                Enchantment.dynamicCost(35, 10),
                                8,
                                EquipmentSlotGroup.MAINHAND
                        )
                )
                .exclusiveWith(enchantmentHolderGetter.getOrThrow(EnchantmentTags.DAMAGE_EXCLUSIVE))
                .withEffect(EnchantmentEffectComponents.DAMAGE, new AddValue(LevelBasedValue.perLevel(1.0F, 0.5F)))
                .withEffect(
                        EnchantmentEffectComponents.DAMAGE,
                        new AddValue(LevelBasedValue.perLevel(2.5F)),
                        LootItemEntityPropertyCondition.hasProperties(
                                LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().entityType(EntityTypePredicate.of(EntityTypeTags.SENSITIVE_TO_SMITE))
                        )
                )
        );
        register(
                pContext,
                ENDERPORT,
                Enchantment.enchantment(
                        Enchantment.definition(
                                itemHolderGetter.getOrThrow(ItemTagsPM.ARCHERY_ENCHANTABLE),
                                2,
                                1,
                                Enchantment.dynamicCost(5, 10),
                                Enchantment.dynamicCost(20, 10),
                                4,
                                EquipmentSlotGroup.MAINHAND
                        )
                )
                // TODO Move Enderport effect here from CombatEvents if possible
        );
        register(
                pContext,
                REGROWTH,
                Enchantment.enchantment(
                        Enchantment.definition(
                                itemHolderGetter.getOrThrow(ItemTags.DURABILITY_ENCHANTABLE),
                                1,
                                1,
                                Enchantment.constantCost(30),
                                Enchantment.constantCost(90),
                                8,
                                EquipmentSlotGroup.ANY
                        )
                )
                // TODO Move Regrowth effect here from PlayerEvents if possible
        );
        register(
                pContext,
                AEGIS,
                Enchantment.enchantment(
                        Enchantment.definition(
                                itemHolderGetter.getOrThrow(ItemTags.ARMOR_ENCHANTABLE),
                                1,
                                4,
                                Enchantment.dynamicCost(10, 11),
                                Enchantment.dynamicCost(18, 11),
                                8,
                                EquipmentSlotGroup.ARMOR
                        )
                )
                .exclusiveWith(enchantmentHolderGetter.getOrThrow(EnchantmentTags.ARMOR_EXCLUSIVE))
                .withEffect(
                        // Fire damage reduction
                        EnchantmentEffectComponents.DAMAGE_PROTECTION,
                        new AddValue(LevelBasedValue.perLevel(2.0F)),
                        AllOfCondition.allOf(
                            DamageSourceCondition.hasDamageSource(
                                DamageSourcePredicate.Builder.damageType()
                                    .tag(TagPredicate.is(DamageTypeTags.IS_FIRE))
                                    .tag(TagPredicate.isNot(DamageTypeTags.BYPASSES_INVULNERABILITY))
                            )
                        )
                )
                .withEffect(
                        // Fire burn time reduction
                        EnchantmentEffectComponents.ATTRIBUTES,
                        new EnchantmentAttributeEffect(
                            ResourceLocation.withDefaultNamespace("enchantment.fire_protection"),
                            Attributes.BURNING_TIME,
                            LevelBasedValue.perLevel(-0.15F),
                            AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                        )
                )
                .withEffect(
                        // Fall damage reduction
                        EnchantmentEffectComponents.DAMAGE_PROTECTION,
                        new AddValue(LevelBasedValue.perLevel(3.0F)),
                        DamageSourceCondition.hasDamageSource(
                            DamageSourcePredicate.Builder.damageType()
                                .tag(TagPredicate.is(DamageTypeTags.IS_FALL))
                                .tag(TagPredicate.isNot(DamageTypeTags.BYPASSES_INVULNERABILITY))
                        )
                )
                .withEffect(
                        // Blast damage reduction
                        EnchantmentEffectComponents.DAMAGE_PROTECTION,
                        new AddValue(LevelBasedValue.perLevel(2.0F)),
                        DamageSourceCondition.hasDamageSource(
                            DamageSourcePredicate.Builder.damageType()
                                .tag(TagPredicate.is(DamageTypeTags.IS_EXPLOSION))
                                .tag(TagPredicate.isNot(DamageTypeTags.BYPASSES_INVULNERABILITY))
                        )
                )
                .withEffect(
                        // Blast knockback reduction
                        EnchantmentEffectComponents.ATTRIBUTES,
                        new EnchantmentAttributeEffect(
                            ResourceLocation.withDefaultNamespace("enchantment.blast_protection"),
                            Attributes.EXPLOSION_KNOCKBACK_RESISTANCE,
                            LevelBasedValue.perLevel(0.15F),
                            AttributeModifier.Operation.ADD_VALUE
                        )
                )
                .withEffect(
                        // Projectile damage reduction
                        EnchantmentEffectComponents.DAMAGE_PROTECTION,
                        new AddValue(LevelBasedValue.perLevel(2.0F)),
                        DamageSourceCondition.hasDamageSource(
                            DamageSourcePredicate.Builder.damageType()
                                .tag(TagPredicate.is(DamageTypeTags.IS_PROJECTILE))
                                .tag(TagPredicate.isNot(DamageTypeTags.BYPASSES_INVULNERABILITY))
                        )
                )
                .withEffect(
                        // Magick damage reduction
                        EnchantmentEffectComponents.DAMAGE_PROTECTION,
                        new AddValue(LevelBasedValue.perLevel(2.0F)),
                        DamageSourceCondition.hasDamageSource(
                            DamageSourcePredicate.Builder.damageType()
                                .tag(TagPredicate.is(DamageTypeTagsPM.IS_MAGIC))
                                .tag(TagPredicate.isNot(DamageTypeTags.BYPASSES_INVULNERABILITY))
                        )
                )
                .withEffect(
                        // Default damage reduction for anything not covered by the other effects
                        EnchantmentEffectComponents.DAMAGE_PROTECTION,
                        new AddValue(LevelBasedValue.perLevel(1.0F)),
                        DamageSourceCondition.hasDamageSource(
                            DamageSourcePredicate.Builder.damageType()
                                .tag(TagPredicate.isNot(DamageTypeTags.IS_FIRE))
                                .tag(TagPredicate.isNot(DamageTypeTags.IS_FALL))
                                .tag(TagPredicate.isNot(DamageTypeTags.IS_EXPLOSION))
                                .tag(TagPredicate.isNot(DamageTypeTags.IS_PROJECTILE))
                                .tag(TagPredicate.isNot(DamageTypeTagsPM.IS_MAGIC))
                                .tag(TagPredicate.isNot(DamageTypeTags.BYPASSES_INVULNERABILITY))
                        )
                )
        );
        register(
                pContext,
                MANA_EFFICIENCY,
                Enchantment.enchantment(
                        Enchantment.definition(
                                itemHolderGetter.getOrThrow(ItemTagsPM.WAND_ENCHANTABLE),
                                itemHolderGetter.getOrThrow(ItemTagsPM.WAND_ENCHANTABLE),
                                10,
                                5,
                                Enchantment.dynamicCost(1, 10),
                                Enchantment.dynamicCost(51, 10),
                                1,
                                EquipmentSlotGroup.MAINHAND
                        )
                )
        );
        register(
                pContext,
                SPELL_POWER,
                Enchantment.enchantment(
                        Enchantment.definition(
                                itemHolderGetter.getOrThrow(ItemTagsPM.WAND_ENCHANTABLE),
                                itemHolderGetter.getOrThrow(ItemTagsPM.WAND_ENCHANTABLE),
                                2,
                                5,
                                Enchantment.dynamicCost(10, 10),
                                Enchantment.dynamicCost(35, 10),
                                4,
                                EquipmentSlotGroup.MAINHAND
                        )
                )
        );
        register(
                pContext,
                TREASURE,
                Enchantment.enchantment(
                        Enchantment.definition(
                                itemHolderGetter.getOrThrow(ItemTagsPM.WAND_ENCHANTABLE),
                                itemHolderGetter.getOrThrow(ItemTagsPM.WAND_ENCHANTABLE),
                                2,
                                3,
                                Enchantment.dynamicCost(15, 9),
                                Enchantment.dynamicCost(65, 9),
                                4,
                                EquipmentSlotGroup.MAINHAND
                        )
                )
                .withEffect(
                        EnchantmentEffectComponents.EQUIPMENT_DROPS,
                        EnchantmentTarget.ATTACKER,
                        EnchantmentTarget.VICTIM,
                        new AddValue(LevelBasedValue.perLevel(0.01F)),
                        LootItemEntityPropertyCondition.hasProperties(
                            LootContext.EntityTarget.ATTACKER, EntityPredicate.Builder.entity().entityType(EntityTypePredicate.of(EntityType.PLAYER))
                        )
                )
        );
        register(
                pContext,
                BLUDGEONING,
                Enchantment.enchantment(
                        Enchantment.definition(
                                itemHolderGetter.getOrThrow(ItemTagsPM.STAFF_ENCHANTABLE),
                                itemHolderGetter.getOrThrow(ItemTagsPM.STAFF_ENCHANTABLE),
                                10,
                                5,
                                Enchantment.dynamicCost(1, 11),
                                Enchantment.dynamicCost(21, 11),
                                1,
                                EquipmentSlotGroup.MAINHAND
                        )
                )
                .exclusiveWith(enchantmentHolderGetter.getOrThrow(EnchantmentTags.DAMAGE_EXCLUSIVE))
                .withEffect(EnchantmentEffectComponents.DAMAGE, new AddValue(LevelBasedValue.perLevel(1.0F, 0.5F)))
        );
        register(
                pContext,
                REVERBERATION,
                Enchantment.enchantment(
                        Enchantment.definition(
                                itemHolderGetter.getOrThrow(ItemTags.MINING_ENCHANTABLE),
                                1,
                                4,
                                Enchantment.dynamicCost(5, 10),
                                Enchantment.dynamicCost(20, 10),
                                8,
                                EquipmentSlotGroup.MAINHAND
                        )
                )
                .exclusiveWith(enchantmentHolderGetter.getOrThrow(EnchantmentTagsPM.DIGGING_AREA_EXCLUSIVE))
        );
        register(
                pContext,
                BOUNTY,
                Enchantment.enchantment(
                        Enchantment.definition(
                                itemHolderGetter.getOrThrow(ItemTagsPM.BOUNTY_ENCHANTABLE),
                                2,
                                4,
                                Enchantment.dynamicCost(5, 10),
                                Enchantment.dynamicCost(20, 10),
                                4,
                                EquipmentSlotGroup.MAINHAND
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
