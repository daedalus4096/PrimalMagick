package com.verdantartifice.primalmagic.client.config;

import org.lwjgl.glfw.GLFW;

import com.verdantartifice.primalmagic.PrimalMagic;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class KeyBindings {
    public static KeyBinding changeSpellKey;
    
    private static final String KEY_CATEGORY = "key.categories." + PrimalMagic.MODID;
    
    public static void init() {
        changeSpellKey = new KeyBinding("key.primalmagic.change_spell", GLFW.GLFW_KEY_R, KEY_CATEGORY);
        ClientRegistry.registerKeyBinding(changeSpellKey);
    }
}
