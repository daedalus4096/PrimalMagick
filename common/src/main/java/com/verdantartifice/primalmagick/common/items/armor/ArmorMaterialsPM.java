package com.verdantartifice.primalmagick.common.items.armor;

import com.verdantartifice.primalmagick.common.tags.ItemTagsPM;
import net.minecraft.Util;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAssets;

import java.util.EnumMap;

/**
 * Deferred registry for mod armor material types.
 * 
 * @author Daedalus4096
 */
public class ArmorMaterialsPM {
    public static final ArmorMaterial IMBUED_WOOL = new ArmorMaterial(
            8,
            Util.make(new EnumMap<>(ArmorType.class), defMap -> {
                defMap.put(ArmorType.BOOTS, 1);
                defMap.put(ArmorType.LEGGINGS, 3);
                defMap.put(ArmorType.CHESTPLATE, 4);
                defMap.put(ArmorType.HELMET, 1);
                defMap.put(ArmorType.BODY, 3);
            }),
            17,
            SoundEvents.ARMOR_EQUIP_LEATHER,
            0.0F,
            0.0F,
            ItemTags.WOOL,
            EquipmentAssets.LEATHER    // FIXME Need equipment asset for armor material
    );
    public static final ArmorMaterial SPELLCLOTH = new ArmorMaterial(
            20,
            Util.make(new EnumMap<>(ArmorType.class), defMap -> {
                defMap.put(ArmorType.BOOTS, 2);
                defMap.put(ArmorType.LEGGINGS, 5);
                defMap.put(ArmorType.CHESTPLATE, 7);
                defMap.put(ArmorType.HELMET, 2);
                defMap.put(ArmorType.BODY, 6);
            }),
            22,
            SoundEvents.ARMOR_EQUIP_LEATHER,
            1.0F,
            0.0F,
            ItemTagsPM.SPELLCLOTH,
            EquipmentAssets.LEATHER    // FIXME Need equipment asset for armor material
    );
    public static final ArmorMaterial HEXWEAVE = new ArmorMaterial(
            35,
            Util.make(new EnumMap<>(ArmorType.class), defMap -> {
                defMap.put(ArmorType.BOOTS, 3);
                defMap.put(ArmorType.LEGGINGS, 7);
                defMap.put(ArmorType.CHESTPLATE, 9);
                defMap.put(ArmorType.HELMET, 3);
                defMap.put(ArmorType.BODY, 8);
            }),
            27,
            SoundEvents.ARMOR_EQUIP_LEATHER,
            2.0F,
            0.0F,
            ItemTagsPM.HEXWEAVE,
            EquipmentAssets.LEATHER    // FIXME Need equipment asset for armor material
    );
    public static final ArmorMaterial SAINTSWOOL = new ArmorMaterial(
            50,
            Util.make(new EnumMap<>(ArmorType.class), defMap -> {
                defMap.put(ArmorType.BOOTS, 4);
                defMap.put(ArmorType.LEGGINGS, 8);
                defMap.put(ArmorType.CHESTPLATE, 10);
                defMap.put(ArmorType.HELMET, 4);
                defMap.put(ArmorType.BODY, 10);
            }),
            32,
            SoundEvents.ARMOR_EQUIP_LEATHER,
            3.0F,
            0.0F,
            ItemTagsPM.SAINTSWOOL,
            EquipmentAssets.LEATHER    // FIXME Need equipment asset for armor material
    );
    public static final ArmorMaterial PRIMALITE = new ArmorMaterial(
            30,
            Util.make(new EnumMap<>(ArmorType.class), defMap -> {
                defMap.put(ArmorType.BOOTS, 3);
                defMap.put(ArmorType.LEGGINGS, 5);
                defMap.put(ArmorType.CHESTPLATE, 7);
                defMap.put(ArmorType.HELMET, 3);
                defMap.put(ArmorType.BODY, 7);
            }),
            18,
            SoundEvents.ARMOR_EQUIP_IRON,
            1.0F,
            0.0F,
            ItemTagsPM.INGOTS_PRIMALITE,
            EquipmentAssets.IRON    // FIXME Need equipment asset for armor material
    );
    public static final ArmorMaterial HEXIUM = new ArmorMaterial(
            45,
            Util.make(new EnumMap<>(ArmorType.class), defMap -> {
                defMap.put(ArmorType.BOOTS, 4);
                defMap.put(ArmorType.LEGGINGS, 7);
                defMap.put(ArmorType.CHESTPLATE, 9);
                defMap.put(ArmorType.HELMET, 4);
                defMap.put(ArmorType.BODY, 11);
            }),
            23,
            SoundEvents.ARMOR_EQUIP_DIAMOND,
            2.0F,
            0.1F,
            ItemTagsPM.INGOTS_HEXIUM,
            EquipmentAssets.DIAMOND     // FIXME Need equipment asset for armor material
    );
    public static final ArmorMaterial HALLOWSTEEL = new ArmorMaterial(
            60,
            Util.make(new EnumMap<>(ArmorType.class), defMap -> {
                defMap.put(ArmorType.BOOTS, 5);
                defMap.put(ArmorType.LEGGINGS, 8);
                defMap.put(ArmorType.CHESTPLATE, 10);
                defMap.put(ArmorType.HELMET, 5);
                defMap.put(ArmorType.BODY, 14);
            }),
            28,
            SoundEvents.ARMOR_EQUIP_NETHERITE,
            3.0F,
            0.2F,
            ItemTagsPM.INGOTS_HALLOWSTEEL,
            EquipmentAssets.NETHERITE   // FIXME Need equipment asset for armor material
    );
}
