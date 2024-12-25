package com.verdantartifice.primalmagick.platform;

import com.mojang.blaze3d.platform.InputConstants;
import com.verdantartifice.primalmagick.platform.services.IInputService;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;

/**
 * Forge implementation of the input service.
 *
 * @author Daedalus4096
 */
public class InputServiceForge implements IInputService {
    @Override
    public boolean isKeyDown(KeyMapping keybind) {
        if (keybind.isUnbound()) {
            return false;
        } else {
            Minecraft mc = Minecraft.getInstance();
            boolean isDown = switch (keybind.getKey().getType()) {
                case KEYSYM -> InputConstants.isKeyDown(mc.getWindow().getWindow(), keybind.getKey().getValue());
                case MOUSE -> GLFW.glfwGetMouseButton(mc.getWindow().getWindow(), keybind.getKey().getValue()) == GLFW.GLFW_PRESS;
                default -> false;
            };
            return isDown && keybind.getKeyConflictContext().isActive() && keybind.getKeyModifier().isActive(keybind.getKeyConflictContext());
        }
    }
}
