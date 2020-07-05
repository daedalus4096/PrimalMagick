package com.verdantartifice.primalmagic.common.items.armor;

import java.util.function.Supplier;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.items.ItemsPM;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.LazyValue;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public enum ArmorMaterialPM implements IArmorMaterial {
    IMBUED_WOOL("imbued_wool", 8, new int[] {1, 3, 4, 1}, 17, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0.0F, () -> {
        return Ingredient.fromTag(ItemTags.WOOL);
    }),
    SPELLCLOTH("spellcloth", 20, new int[] {2, 5, 7, 2}, 22, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 1.0F, () -> {
        return Ingredient.fromItems(ItemsPM.SPELLCLOTH.get());
    }),
    HEXWEAVE("hexweave", 35, new int[] {3, 7, 9, 3}, 27, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 2.0F, () -> {
        return Ingredient.fromItems(ItemsPM.HEXWEAVE.get());
    });

    private static final int[] MAX_DAMAGE_ARRAY = new int[] {13, 15, 16, 11};
    
    private final String name;
    private final int maxDamageFactor;
    private final int[] damageReductionAmountArray;
    private final int enchantability;
    private final SoundEvent soundEvent;
    private final float toughness;
    private final LazyValue<Ingredient> repairMaterial;

    private ArmorMaterialPM(String name, int maxDamageFactor, int[] damageReductionAmounts, int enchantability, SoundEvent equipSound, float toughness, Supplier<Ingredient> repairMaterialSupplier) {
        this.name = PrimalMagic.MODID + ":" + name;
        this.maxDamageFactor = maxDamageFactor;
        this.damageReductionAmountArray = damageReductionAmounts;
        this.enchantability = enchantability;
        this.soundEvent = equipSound;
        this.toughness = toughness;
        this.repairMaterial = new LazyValue<>(repairMaterialSupplier);
    }

    @Override
    public int getDurability(EquipmentSlotType slot) {
        return MAX_DAMAGE_ARRAY[slot.getIndex()] * this.maxDamageFactor;
    }

    @Override
    public int getDamageReductionAmount(EquipmentSlotType slot) {
        return this.damageReductionAmountArray[slot.getIndex()];
    }

    @Override
    public int getEnchantability() {
        return this.enchantability;
    }

    @Override
    public SoundEvent getSoundEvent() {
        return this.soundEvent;
    }

    @Override
    public Ingredient getRepairMaterial() {
        return this.repairMaterial.getValue();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public String getName() {
        return this.name;
    }

    @Override
    public float getToughness() {
        return this.toughness;
    }
}
