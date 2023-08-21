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
    public static final String KEY_CATEGORY = "key.categories." + PrimalMagick.MODID;
    
    public static final KeyMapping CHANGE_SPELL_KEY = new KeyMapping("key.primalmagick.change_spell", GLFW.GLFW_KEY_R, KEY_CATEGORY);                   // Key for changing the active spell of a wand
    public static final KeyMapping CARPET_FORWARD_KEY = new KeyMapping("key.primalmagick.carpet_forward", GLFW.GLFW_KEY_W, KEY_CATEGORY);               // Key for commanding a flying carpet forward
    public static final KeyMapping CARPET_BACKWARD_KEY = new KeyMapping("key.primalmagick.carpet_backward", GLFW.GLFW_KEY_S, KEY_CATEGORY);             // Key for commanding a flying carpet backward
    public static final KeyMapping GRIMOIRE_NEXT_PAGE = new KeyMapping("key.primalmagick.grimoire_next_page", GLFW.GLFW_KEY_RIGHT, KEY_CATEGORY);       // Key for going to the next page in a grimoire entry
    public static final KeyMapping GRIMOIRE_PREV_PAGE = new KeyMapping("key.primalmagick.grimoire_prev_page", GLFW.GLFW_KEY_LEFT, KEY_CATEGORY);        // Key for going to the previous page in a grimoire entry
    public static final KeyMapping GRIMOIRE_PREV_TOPIC = new KeyMapping("key.primalmagick.grimoire_prev_topic", GLFW.GLFW_KEY_BACKSPACE, KEY_CATEGORY); // Key for going back to the previous grimoire topic
    public static final KeyMapping VIEW_AFFINITY_KEY = new KeyMapping("key.primalmagick.view_affinity", GLFW.GLFW_KEY_LEFT_SHIFT, KEY_CATEGORY);        // Key for viewing affinities in an item stack tooltip
    
    @SubscribeEvent
    public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(CHANGE_SPELL_KEY);
        event.register(CARPET_FORWARD_KEY);
        event.register(CARPET_BACKWARD_KEY);
        event.register(GRIMOIRE_NEXT_PAGE);
        event.register(GRIMOIRE_PREV_PAGE);
        event.register(GRIMOIRE_PREV_TOPIC);
        event.register(VIEW_AFFINITY_KEY);
    }
}
