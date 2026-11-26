package com.verdantartifice.primalmagick.datagen.models;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.common.items.EquipmentAssetsPM;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
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
        pOutput.accept(EquipmentAssetsPM.IMBUED_WOOL, onlyAdultHumanoid("imbued_wool"));
        pOutput.accept(EquipmentAssetsPM.SPELLCLOTH, onlyAdultHumanoid("spellcloth"));
        pOutput.accept(EquipmentAssetsPM.HEXWEAVE, onlyAdultHumanoid("hexweave"));
        pOutput.accept(EquipmentAssetsPM.SAINTSWOOL, onlyAdultHumanoid("saintswool"));
        pOutput.accept(EquipmentAssetsPM.PRIMALITE, onlyAdultHumanoid("primalite"));
        pOutput.accept(EquipmentAssetsPM.HEXIUM, onlyAdultHumanoid("hexium"));
        pOutput.accept(EquipmentAssetsPM.HALLOWSTEEL, onlyAdultHumanoid("hallowsteel"));
    }

    private static EquipmentClientInfo onlyAdultHumanoid(String pName) {
        // TODO Add baby textures and switch back to calling addHumanoidLayers
        Identifier textureId = ResourceUtils.loc(pName);
        return EquipmentClientInfo.builder()
                .addLayers(EquipmentClientInfo.LayerType.HUMANOID, EquipmentClientInfo.Layer.leatherDyeable(textureId, false))
                .addLayers(EquipmentClientInfo.LayerType.HUMANOID_LEGGINGS, EquipmentClientInfo.Layer.leatherDyeable(textureId, false))
                .build();
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
