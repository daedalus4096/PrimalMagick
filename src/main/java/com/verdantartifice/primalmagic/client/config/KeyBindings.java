package com.verdantartifice.primalmagic.client.config;

import org.lwjgl.glfw.GLFW;

import com.verdantartifice.primalmagic.PrimalMagic;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

/**
 * Define and register custom client key-bindings.
 * 
 * @author Michael Bunting
 */
public class KeyBindings {
    public static KeyBinding changeSpellKey;    // Key for changing the active spell of a wand
    
    private static final String KEY_CATEGORY = "key.categories." + PrimalMagic.MODID;
    
    public static void init() {
        changeSpellKey = new KeyBinding("key.primalmagic.change_spell", GLFW.GLFW_KEY_R, KEY_CATEGORY);
        ClientRegistry.registerKeyBinding(changeSpellKey);
    }
}
