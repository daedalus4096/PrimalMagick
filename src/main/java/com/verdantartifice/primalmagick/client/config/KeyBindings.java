package com.verdantartifice.primalmagick.client.config;

import org.lwjgl.glfw.GLFW;

import com.verdantartifice.primalmagick.PrimalMagick;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.fmlclient.registry.ClientRegistry;

/**
 * Define and register custom client key-bindings.
 * 
 * @author Daedalus4096
 */
public class KeyBindings {
    public static KeyMapping changeSpellKey;    // Key for changing the active spell of a wand
    public static KeyMapping carpetForwardKey;  // Key for commanding a flying carpet forward
    public static KeyMapping carpetBackwardKey; // Key for commanding a flying carpet backward
    
    private static final String KEY_CATEGORY = "key.categories." + PrimalMagick.MODID;
    
    public static void init() {
        changeSpellKey = new KeyMapping("key.primalmagick.change_spell", GLFW.GLFW_KEY_R, KEY_CATEGORY);
        ClientRegistry.registerKeyBinding(changeSpellKey);
        
        carpetForwardKey = new KeyMapping("key.primalmagick.carpet_forward", GLFW.GLFW_KEY_W, KEY_CATEGORY);
        ClientRegistry.registerKeyBinding(carpetForwardKey);
        
        carpetBackwardKey = new KeyMapping("key.primalmagick.carpet_backward", GLFW.GLFW_KEY_S, KEY_CATEGORY);
        ClientRegistry.registerKeyBinding(carpetBackwardKey);
    }
}
