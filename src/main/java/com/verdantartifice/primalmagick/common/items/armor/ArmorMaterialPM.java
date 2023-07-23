package com.verdantartifice.primalmagick.common.items.armor;

import java.util.function.Supplier;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.tags.ItemTagsPM;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

/**
 * Definition of tiered armor materials for the mod.
 * 
 * @author Daedalus4096
 */
@SuppressWarnings("deprecation")
public enum ArmorMaterialPM implements ArmorMaterial {
    IMBUED_WOOL("imbued_wool", 8, new int[] {1, 3, 4, 1}, 17, SoundEvents.ARMOR_EQUIP_LEATHER, 0.0F, 0.0F, () -> {
        return Ingredient.of(ItemTags.WOOL);
    }),
    SPELLCLOTH("spellcloth", 20, new int[] {2, 5, 7, 2}, 22, SoundEvents.ARMOR_EQUIP_LEATHER, 1.0F, 0.0F, () -> {
        return Ingredient.of(ItemsPM.SPELLCLOTH.get());
    }),
    HEXWEAVE("hexweave", 35, new int[] {3, 7, 9, 3}, 27, SoundEvents.ARMOR_EQUIP_LEATHER, 2.0F, 0.0F, () -> {
        return Ingredient.of(ItemsPM.HEXWEAVE.get());
    }),
    SAINTSWOOL("saintswool", 50, new int[] {4, 8, 10, 4}, 32, SoundEvents.ARMOR_EQUIP_LEATHER, 3.0F, 0.0F, () -> {
        return Ingredient.of(ItemsPM.SAINTSWOOL.get());
    }),
    PRIMALITE("primalite", 30, new int[] {3, 5, 7, 3}, 18, SoundEvents.ARMOR_EQUIP_IRON, 1.0F, 0.0F, () -> {
        return Ingredient.of(ItemTagsPM.INGOTS_PRIMALITE);
    }),
    HEXIUM("hexium", 45, new int[] {4, 7, 9, 4}, 23, SoundEvents.ARMOR_EQUIP_IRON, 2.0F, 0.1F, () -> {
        return Ingredient.of(ItemTagsPM.INGOTS_HEXIUM);
    }),
    HALLOWSTEEL("hallowsteel", 60, new int[] {5, 8, 10, 5}, 28, SoundEvents.ARMOR_EQUIP_IRON, 3.0F, 0.2F, () -> {
        return Ingredient.of(ItemTagsPM.INGOTS_HALLOWSTEEL);
    });

    private static final int[] MAX_DAMAGE_ARRAY = new int[] {13, 15, 16, 11};
    
    private final String name;
    private final int maxDamageFactor;
    private final int[] damageReductionAmountArray;
    private final int enchantability;
    private final SoundEvent soundEvent;
    private final float toughness;
    private final float knockbackResistance;
    private final LazyLoadedValue<Ingredient> repairMaterial;

    private ArmorMaterialPM(String name, int maxDamageFactor, int[] damageReductionAmounts, int enchantability, SoundEvent equipSound, float toughness, float knockbackResistance, Supplier<Ingredient> repairMaterialSupplier) {
        this.name = PrimalMagick.MODID + ":" + name;
        this.maxDamageFactor = maxDamageFactor;
        this.damageReductionAmountArray = damageReductionAmounts;
        this.enchantability = enchantability;
        this.soundEvent = equipSound;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.repairMaterial = new LazyLoadedValue<>(repairMaterialSupplier);
    }

    @Override
    public int getDurabilityForType(ArmorItem.Type type) {
        return MAX_DAMAGE_ARRAY[type.getSlot().getIndex()] * this.maxDamageFactor;
    }

    @Override
    public int getDefenseForType(ArmorItem.Type type) {
        return this.damageReductionAmountArray[type.getSlot().getIndex()];
    }

    @Override
    public int getEnchantmentValue() {
        return this.enchantability;
    }

    @Override
    public SoundEvent getEquipSound() {
        return this.soundEvent;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairMaterial.get();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public float getToughness() {
        return this.toughness;
    }

    @Override
    public float getKnockbackResistance() {
        return this.knockbackResistance;
    }
}
