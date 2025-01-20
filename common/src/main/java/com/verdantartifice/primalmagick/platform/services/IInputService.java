package com.verdantartifice.primalmagick.platform.services;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;

public interface IInputService {
    /**
     * Determine whether the given keybind is current being pressed.
     *
     * @param keybind the keybind to query
     * @return true if the keybind is currently down, false otherwise
     */
    boolean isKeyDown(KeyMapping keybind);

    /**
     * Get the currently assigned key for the given key mapping.
     *
     * @param keybind the keybind to query
     * @return the currently assigned key for the mapping
     */
    InputConstants.Key getKey(KeyMapping keybind);
}
