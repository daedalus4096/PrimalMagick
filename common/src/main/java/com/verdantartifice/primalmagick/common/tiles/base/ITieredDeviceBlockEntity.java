package com.verdantartifice.primalmagick.common.tiles.base;

import com.verdantartifice.primalmagick.common.misc.DeviceTier;
import com.verdantartifice.primalmagick.common.misc.ITieredDevice;
import net.minecraft.world.level.block.state.BlockState;

public interface ITieredDeviceBlockEntity {
    BlockState getBlockState();

    default DeviceTier getDeviceTier() {
        return this.getBlockState().getBlock() instanceof ITieredDevice device ? device.getDeviceTier() : DeviceTier.BASIC;
    }
}
