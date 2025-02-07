package com.verdantartifice.primalmagick.platform;

import com.verdantartifice.primalmagick.platform.services.IModelResourceLocationService;
import net.minecraft.client.resources.model.ModelResourceLocation;

public class ModelResourceLocationServiceNeoforge implements IModelResourceLocationService {
    @Override
    public String getStandaloneVariant() {
        return ModelResourceLocation.STANDALONE_VARIANT;
    }

    @Override
    public String getInventoryVariant() {
        return ModelResourceLocation.INVENTORY_VARIANT;
    }
}
