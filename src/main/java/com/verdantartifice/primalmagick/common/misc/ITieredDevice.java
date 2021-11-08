package com.verdantartifice.primalmagick.common.misc;

/**
 * Interface describing a device block which can come in multiple tiers.  Higher tiered devices
 * are typically more powerful and more expensive than lower tiered devices.
 * 
 * @author Daedalus4096
 */
public interface ITieredDevice {
    public DeviceTier getDeviceTier();
}
