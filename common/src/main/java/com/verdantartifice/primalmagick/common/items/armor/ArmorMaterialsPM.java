package com.verdantartifice.primalmagick.common.items.armor;

import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.registries.IRegistryItem;
import com.verdantartifice.primalmagick.common.tags.ItemTagsPM;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.EnumMap;
import java.util.List;
import java.util.function.Supplier;

/**
 * Deferred registry for mod armor material types.
 * 
 * @author Daedalus4096
 */
public class ArmorMaterialsPM {
    public static void init() {
        // Pass the service initialization through this class so it gets class loaded and fields registered
        Services.ARMOR_MATERIALS_REGISTRY.init();
    }

    public static final IRegistryItem<ArmorMaterial, ArmorMaterial> IMBUED_WOOL = register("imbued_wool", Util.make(new EnumMap<>(ArmorItem.Type.class), defMap -> {
        defMap.put(ArmorItem.Type.BOOTS, 1);
        defMap.put(ArmorItem.Type.LEGGINGS, 3);
        defMap.put(ArmorItem.Type.CHESTPLATE, 4);
        defMap.put(ArmorItem.Type.HELMET, 1);
        defMap.put(ArmorItem.Type.BODY, 3);
    }), 17, SoundEvents.ARMOR_EQUIP_LEATHER, 0.0F, 0.0F, () -> Ingredient.of(ItemTags.WOOL));
    public static final IRegistryItem<ArmorMaterial, ArmorMaterial> SPELLCLOTH = register("spellcloth", Util.make(new EnumMap<>(ArmorItem.Type.class), defMap -> {
        defMap.put(ArmorItem.Type.BOOTS, 2);
        defMap.put(ArmorItem.Type.LEGGINGS, 5);
        defMap.put(ArmorItem.Type.CHESTPLATE, 7);
        defMap.put(ArmorItem.Type.HELMET, 2);
        defMap.put(ArmorItem.Type.BODY, 6);
    }), 22, SoundEvents.ARMOR_EQUIP_LEATHER, 1.0F, 0.0F, () -> Ingredient.of(ItemsPM.SPELLCLOTH.get()));
    public static final IRegistryItem<ArmorMaterial, ArmorMaterial> HEXWEAVE = register("hexweave", Util.make(new EnumMap<>(ArmorItem.Type.class), defMap -> {
        defMap.put(ArmorItem.Type.BOOTS, 3);
        defMap.put(ArmorItem.Type.LEGGINGS, 7);
        defMap.put(ArmorItem.Type.CHESTPLATE, 9);
        defMap.put(ArmorItem.Type.HELMET, 3);
        defMap.put(ArmorItem.Type.BODY, 8);
    }), 27, SoundEvents.ARMOR_EQUIP_LEATHER, 2.0F, 0.0F, () -> Ingredient.of(ItemsPM.HEXWEAVE.get()));
    public static final IRegistryItem<ArmorMaterial, ArmorMaterial> SAINTSWOOL = register("saintswool", Util.make(new EnumMap<>(ArmorItem.Type.class), defMap -> {
        defMap.put(ArmorItem.Type.BOOTS, 4);
        defMap.put(ArmorItem.Type.LEGGINGS, 8);
        defMap.put(ArmorItem.Type.CHESTPLATE, 10);
        defMap.put(ArmorItem.Type.HELMET, 4);
        defMap.put(ArmorItem.Type.BODY, 10);
    }), 32, SoundEvents.ARMOR_EQUIP_LEATHER, 3.0F, 0.0F, () -> Ingredient.of(ItemsPM.SAINTSWOOL.get()));
    public static final IRegistryItem<ArmorMaterial, ArmorMaterial> PRIMALITE = register("primalite", Util.make(new EnumMap<>(ArmorItem.Type.class), defMap -> {
        defMap.put(ArmorItem.Type.BOOTS, 3);
        defMap.put(ArmorItem.Type.LEGGINGS, 5);
        defMap.put(ArmorItem.Type.CHESTPLATE, 7);
        defMap.put(ArmorItem.Type.HELMET, 3);
        defMap.put(ArmorItem.Type.BODY, 7);
    }), 18, SoundEvents.ARMOR_EQUIP_IRON, 1.0F, 0.0F, () -> Ingredient.of(ItemTagsPM.INGOTS_PRIMALITE));
    public static final IRegistryItem<ArmorMaterial, ArmorMaterial> HEXIUM = register("hexium", Util.make(new EnumMap<>(ArmorItem.Type.class), defMap -> {
        defMap.put(ArmorItem.Type.BOOTS, 4);
        defMap.put(ArmorItem.Type.LEGGINGS, 7);
        defMap.put(ArmorItem.Type.CHESTPLATE, 9);
        defMap.put(ArmorItem.Type.HELMET, 4);
        defMap.put(ArmorItem.Type.BODY, 11);
    }), 23, SoundEvents.ARMOR_EQUIP_DIAMOND, 2.0F, 0.1F, () -> Ingredient.of(ItemTagsPM.INGOTS_HEXIUM));
    public static final IRegistryItem<ArmorMaterial, ArmorMaterial> HALLOWSTEEL = register("hallowsteel", Util.make(new EnumMap<>(ArmorItem.Type.class), defMap -> {
        defMap.put(ArmorItem.Type.BOOTS, 5);
        defMap.put(ArmorItem.Type.LEGGINGS, 8);
        defMap.put(ArmorItem.Type.CHESTPLATE, 10);
        defMap.put(ArmorItem.Type.HELMET, 5);
        defMap.put(ArmorItem.Type.BODY, 14);
    }), 28, SoundEvents.ARMOR_EQUIP_NETHERITE, 3.0F, 0.2F, () -> Ingredient.of(ItemTagsPM.INGOTS_HALLOWSTEEL));
    
    private static IRegistryItem<ArmorMaterial, ArmorMaterial> register(
        String pName,
        EnumMap<ArmorItem.Type, Integer> pDefense,
        int pEnchantmentValue,
        Holder<SoundEvent> pEquipSound,
        float pToughness,
        float pKnockbackResistance,
        Supplier<Ingredient> pRepairIngredient
    ) {
        List<ArmorMaterial.Layer> list = List.of(new ArmorMaterial.Layer(ResourceUtils.loc(pName)));
        return register(pName, pDefense, pEnchantmentValue, pEquipSound, pToughness, pKnockbackResistance, pRepairIngredient, list);
    }

    private static IRegistryItem<ArmorMaterial, ArmorMaterial> register(
        String pName,
        EnumMap<ArmorItem.Type, Integer> pDefense,
        int pEnchantmentValue,
        Holder<SoundEvent> pEquipSound,
        float pToughness,
        float pKnockbackResistance,
        Supplier<Ingredient> pRepairIngredient,
        List<ArmorMaterial.Layer> pLayers
    ) {
        EnumMap<ArmorItem.Type, Integer> defenseMap = new EnumMap<>(ArmorItem.Type.class);
        for (ArmorItem.Type armorType : ArmorItem.Type.values()) {
            defenseMap.put(armorType, pDefense.get(armorType));
        }
        return Services.ARMOR_MATERIALS_REGISTRY.register(pName, () -> new ArmorMaterial(defenseMap, pEnchantmentValue, pEquipSound, pRepairIngredient, pLayers, pToughness, pKnockbackResistance));
    }
}
