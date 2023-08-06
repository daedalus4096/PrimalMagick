package com.verdantartifice.primalmagick.client.config;

import org.lwjgl.glfw.GLFW;

import com.verdantartifice.primalmagick.PrimalMagick;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Define and register custom client key-bindings.
 * 
 * @author Daedalus4096
 */
@Mod.EventBusSubscriber(modid=PrimalMagick.MODID, value=Dist.CLIENT, bus=Mod.EventBusSubscriber.Bus.MOD)
public class KeyBindings {
    public static KeyMapping changeSpellKey;    // Key for changing the active spell of a wand
    public static KeyMapping carpetForwardKey;  // Key for commanding a flying carpet forward
    public static KeyMapping carpetBackwardKey; // Key for commanding a flying carpet backward
    public static KeyMapping grimoireNextPage;  // Key for going to the next page in a grimoire entry
    public static KeyMapping grimoirePrevPage;  // Key for going to the previous page in a grimoire entry
    public static KeyMapping grimoirePrevTopic; // Key for going back to the previous grimoire topic
    public static KeyMapping viewAffinityKey;   // Key for viewing affinities in an item stack tooltip
    
    private static final String KEY_CATEGORY = "key.categories." + PrimalMagick.MODID;
    
    @SubscribeEvent
    public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        changeSpellKey = new KeyMapping("key.primalmagick.change_spell", GLFW.GLFW_KEY_R, KEY_CATEGORY);
        event.register(changeSpellKey);
        
        carpetForwardKey = new KeyMapping("key.primalmagick.carpet_forward", GLFW.GLFW_KEY_W, KEY_CATEGORY);
        event.register(carpetForwardKey);
        
        carpetBackwardKey = new KeyMapping("key.primalmagick.carpet_backward", GLFW.GLFW_KEY_S, KEY_CATEGORY);
        event.register(carpetBackwardKey);
        
        grimoireNextPage = new KeyMapping("key.primalmagick.grimoire_next_page", GLFW.GLFW_KEY_RIGHT, KEY_CATEGORY);
        event.register(grimoireNextPage);
        
        grimoirePrevPage = new KeyMapping("key.primalmagick.grimoire_prev_page", GLFW.GLFW_KEY_LEFT, KEY_CATEGORY);
        event.register(grimoirePrevPage);
        
        grimoirePrevTopic = new KeyMapping("key.primalmagick.grimoire_prev_topic", GLFW.GLFW_KEY_BACKSPACE, KEY_CATEGORY);
        event.register(grimoirePrevTopic);
        
        viewAffinityKey = new KeyMapping("key.primalmagick.view_affinity", GLFW.GLFW_KEY_LEFT_SHIFT, KEY_CATEGORY);
        event.register(viewAffinityKey);
    }
}
