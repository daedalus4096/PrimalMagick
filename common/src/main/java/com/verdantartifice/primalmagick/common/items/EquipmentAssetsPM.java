package com.verdantartifice.primalmagick.common.items;

import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;

public class EquipmentAssetsPM {
    public static final ResourceKey<EquipmentAsset> PRIMALITE = createId("primalite");
    public static final ResourceKey<EquipmentAsset> HEXIUM = createId("hexium");
    public static final ResourceKey<EquipmentAsset> HALLOWSTEEL = createId("hallowsteel");

    private static ResourceKey<EquipmentAsset> createId(String pName) {
        return ResourceKey.create(EquipmentAssets.ROOT_ID, ResourceUtils.loc(pName));
    }
}
