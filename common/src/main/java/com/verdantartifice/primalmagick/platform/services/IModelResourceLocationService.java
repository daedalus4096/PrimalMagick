package com.verdantartifice.primalmagick.platform.services;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;

public interface IModelResourceLocationService {
    default ModelResourceLocation create(ResourceLocation baseLocation, String variant) {
        return new ModelResourceLocation(baseLocation, variant);
    }

    default ModelResourceLocation createStandalone(ResourceLocation baseLocation) {
        return this.create(baseLocation, this.getStandaloneVariant());
    }

    default ModelResourceLocation createInventory(ResourceLocation baseLocation) {
        return this.create(baseLocation, this.getInventoryVariant());
    }

    String getStandaloneVariant();
    String getInventoryVariant();
}
