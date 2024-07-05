package com.verdantartifice.primalmagick.common.enchantments;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.effects.EffectsPM;
import com.verdantartifice.primalmagick.common.enchantments.effects.ApplyConstantMobEffect;
import com.verdantartifice.primalmagick.common.enchantments.effects.ApplyStackingMobEffect;
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
    public static final ResourceKey<Enchantment> DISINTEGRATION = key("disintegration");
    public static final ResourceKey<Enchantment> VERDANT = key("verdant");
    public static final ResourceKey<Enchantment> LUCKY_STRIKE = key("lucky_strike");
    public static final ResourceKey<Enchantment> RENDING = key("rending");
    public static final ResourceKey<Enchantment> SOULPIERCING = key("soulpiercing");
    public static final ResourceKey<Enchantment> ESSENCE_THIEF = key("essence_thief");
    public static final ResourceKey<Enchantment> BULWARK = key("bulwark");
    
    public static final RegistryObject<Enchantment> MAGICK_PROTECTION = ENCHANTMENTS.register("magick_protection", () -> new MagickProtectionEnchantment(Enchantment.Rarity.UNCOMMON, new EquipmentSlot[] {EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET}));
    public static final RegistryObject<Enchantment> GUILLOTINE = ENCHANTMENTS.register("guillotine", () -> new GuillotineEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND));

    public static void bootstrap(BootstrapContext<Enchantment> pContext) {
        HolderGetter<Item> itemHolderGetter = pContext.lookup(Registries.ITEM);
        HolderGetter<Enchantment> enchantmentHolderGetter = pContext.lookup(Registries.ENCHANTMENT);

        /*
         * Definition of an enchantment that steals life from entities wounded by this weapon.  Gives a
         * twenty percent chance per level to heal one point.
         */
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
        
        /*
         * Definition of an enchantment that prevents entities from teleporting for a brief time.
         */
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
        
        /*
         * Definition of a rune enchantment that combines the effects of Sharpness and Smite.
         */
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
        
        /*
         * Definition of an enchantment that teleports the shooter of an arrow to the impact location.
         */
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
        
        /*
         * Definition of an enchantment that slowly mends equipment over time.
         */
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
        
        /*
         * Definition of a rune enchantment that combines the damage reduction of the base five protection
         * enchantments plus Magick Protection.
         */
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
        
        /*
         * Definition of a mana efficiency enchantment that can be applied to wands or staves.  Decreases
         * mana consumed when crafting or casting spells.
         */
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
        
        /*
         * Definition of a wand enchantment that mimics and stacks with the effect of the Amplify spell mod.
         */
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
        
        /*
         * Definition of a loot bonus enchantment that can be applied to wands and staves.
         */
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
        
        /*
         * Definition of a melee damage boosting enchantment for magickal staves.
         */
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
        
        /*
         * Definition of an enchantment that increases the planar digging area of a digging tool.
         */
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
        
        /*
         * Definition of an enchantment that grants bonus rolls when fishing or harvesting crops.  Note
         * that unlike Luck of the Sea, this increases the quantity of items gained, not quality.
         */
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
        
        /*
         * Definition of an enchantment that causes adjacent, identical blocks to be broken in a chain
         * when digging with the enchanted tool.
         */
        register(
                pContext,
                DISINTEGRATION,
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
        
        /*
         * Definition of an enchantment that simulates bone meal when using on crops, at the cost of
         * durability.  Higher enchantment levels reduce durability cost.
         */
        register(
                pContext,
                VERDANT,
                Enchantment.enchantment(
                        Enchantment.definition(
                                itemHolderGetter.getOrThrow(ItemTagsPM.HOE_ENCHANTABLE),
                                2,
                                4,
                                Enchantment.dynamicCost(5, 10),
                                Enchantment.dynamicCost(20, 10),
                                4,
                                EquipmentSlotGroup.MAINHAND
                        )
                )
        );
        
        /*
         * Definition of an enchantment that grants a chance of bonus nuggets when mining tag-defined ores.
         */
        register(
                pContext,
                LUCKY_STRIKE,
                Enchantment.enchantment(
                        Enchantment.definition(
                                itemHolderGetter.getOrThrow(ItemTags.MINING_LOOT_ENCHANTABLE),
                                2,
                                4,
                                Enchantment.dynamicCost(5, 10),
                                Enchantment.dynamicCost(20, 10),
                                4,
                                EquipmentSlotGroup.MAINHAND
                        )
                )
        );
        
        /*
         * Definition of an enchantment that applies a stacking bleed damage-over-time effect to the target.
         */
        register(
                pContext,
                RENDING,
                Enchantment.enchantment(
                        Enchantment.definition(
                                itemHolderGetter.getOrThrow(ItemTagsPM.RENDING_ENCHANTABLE),
                                2,
                                4,
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
                        new ApplyStackingMobEffect(
                                EffectsPM.BLEEDING.getHolder().get(),
                                LevelBasedValue.constant(120F),
                                LevelBasedValue.constant(1F),
                                LevelBasedValue.perLevel(1F)
                        )
                )
        );
        
        /*
         * Definition of an enchantment that causes the target to drop soul slivers when struck.
         */
        register(
                pContext,
                SOULPIERCING,
                Enchantment.enchantment(
                        Enchantment.definition(
                                itemHolderGetter.getOrThrow(ItemTagsPM.ARCHERY_ENCHANTABLE),
                                2,
                                4,
                                Enchantment.dynamicCost(5, 10),
                                Enchantment.dynamicCost(20, 10),
                                4,
                                EquipmentSlotGroup.MAINHAND
                        )
                )
        );
        
        /*
         * Definition of an enchantment that causes mobs to drop a sample of their essence when killed.
         */
        register(
                pContext,
                ESSENCE_THIEF,
                Enchantment.enchantment(
                        Enchantment.definition(
                                itemHolderGetter.getOrThrow(ItemTagsPM.ESSENCE_THIEF_ENCHANTABLE),
                                2,
                                4,
                                Enchantment.dynamicCost(5, 10),
                                Enchantment.dynamicCost(20, 10),
                                4,
                                EquipmentSlotGroup.HAND
                        )
                )
        );
        
        /*
         * Definition of an enchantment that grants reduction to all incoming damage while blocking.
         */
        register(
                pContext,
                BULWARK,
                Enchantment.enchantment(
                        Enchantment.definition(
                                itemHolderGetter.getOrThrow(ItemTagsPM.SHIELD_ENCHANTABLE),
                                2,
                                4,
                                Enchantment.dynamicCost(5, 10),
                                Enchantment.dynamicCost(20, 10),
                                4,
                                EquipmentSlotGroup.OFFHAND
                        )
                )
                // TODO Move Bulwark effect here from EntityEvents if possible
        );
    }
    
    private static void register(BootstrapContext<Enchantment> pContext, ResourceKey<Enchantment> pKey, Enchantment.Builder pBuilder) {
        pContext.register(pKey, pBuilder.build(pKey.location()));
    }

    private static ResourceKey<Enchantment> key(String pName) {
        return ResourceKey.create(Registries.ENCHANTMENT, PrimalMagick.resource(pName));
    }
}
