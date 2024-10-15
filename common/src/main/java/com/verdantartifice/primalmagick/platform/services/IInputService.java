package com.verdantartifice.primalmagick.platform.services;

import net.minecraft.client.KeyMapping;

public interface IInputService {
    /**
     * Determine whether the given keybind is current being pressed.
     *
     * @param keybind the keybind to query
     * @return true if the keybind is currently down, false otherwise
     */
    boolean isKeyDown(KeyMapping keybind);
}
