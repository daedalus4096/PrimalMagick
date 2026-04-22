package com.verdantartifice.primalmagick.platform.services;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.Identifier;

public interface IModelResourceLocationService {
    default ModelResourceLocation create(Identifier baseLocation, String variant) {
        return new ModelResourceLocation(baseLocation, variant);
    }

    default ModelResourceLocation createStandalone(Identifier baseLocation) {
        return this.create(baseLocation, this.getStandaloneVariant());
    }

    default ModelResourceLocation createInventory(Identifier baseLocation) {
        return this.create(baseLocation, this.getInventoryVariant());
    }

    String getStandaloneVariant();
    String getInventoryVariant();
}
