package com.verdantartifice.primalmagick.datagen.models;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.common.items.EquipmentAssetsPM;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.equipment.EquipmentAsset;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class EquipmentAssetProviderPM implements DataProvider {
    private final PackOutput.PathProvider pathProvider;

    public EquipmentAssetProviderPM(PackOutput pOutput) {
        this.pathProvider = pOutput.createPathProvider(PackOutput.Target.RESOURCE_PACK, "equipment");
    }

    private static void bootstrap(BiConsumer<ResourceKey<EquipmentAsset>, EquipmentClientInfo> pOutput) {
        pOutput.accept(EquipmentAssetsPM.PRIMALITE, onlyHumanoid("primalite"));
        pOutput.accept(EquipmentAssetsPM.HEXIUM, onlyHumanoid("hexium"));
        pOutput.accept(EquipmentAssetsPM.HALLOWSTEEL, onlyHumanoid("hallowsteel"));
    }

    private static EquipmentClientInfo onlyHumanoid(String pName) {
        return EquipmentClientInfo.builder().addHumanoidLayers(ResourceUtils.loc(pName)).build();
    }

    @Override
    public @NotNull CompletableFuture<?> run(@NotNull CachedOutput cachedOutput) {
        Map<ResourceKey<EquipmentAsset>, EquipmentClientInfo> map = new HashMap<>();
        bootstrap((resourceKey, eci) -> {
            if (map.putIfAbsent(resourceKey, eci) != null) {
                throw new IllegalStateException("Tried to register equipment asset twice for id: " + resourceKey);
            }
        });
        return DataProvider.saveAll(cachedOutput, EquipmentClientInfo.CODEC, this.pathProvider::json, map);
    }

    @Override
    public @NotNull String getName() {
        return "Equipment Asset Definitions - " + Constants.MOD_NAME;
    }
}
