package com.verdantartifice.primalmagick.client.config;

import com.verdantartifice.primalmagick.Constants;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

import java.util.function.Consumer;

/**
 * Define and register custom client key-bindings.
 * 
 * @author Daedalus4096
 */
public class KeyBindings {
    public static final String KEY_CATEGORY = "key.categories." + Constants.MOD_ID;
    
    public static final KeyMapping CHANGE_SPELL_KEY = new KeyMapping("key.primalmagick.change_spell", GLFW.GLFW_KEY_R, KEY_CATEGORY);                   // Key for changing the active spell of a wand
    public static final KeyMapping CARPET_FORWARD_KEY = new KeyMapping("key.primalmagick.carpet_forward", GLFW.GLFW_KEY_W, KEY_CATEGORY);               // Key for commanding a flying carpet forward
    public static final KeyMapping CARPET_BACKWARD_KEY = new KeyMapping("key.primalmagick.carpet_backward", GLFW.GLFW_KEY_S, KEY_CATEGORY);             // Key for commanding a flying carpet backward
    public static final KeyMapping GRIMOIRE_NEXT_PAGE = new KeyMapping("key.primalmagick.grimoire_next_page", GLFW.GLFW_KEY_RIGHT, KEY_CATEGORY);       // Key for going to the next page in a grimoire entry
    public static final KeyMapping GRIMOIRE_PREV_PAGE = new KeyMapping("key.primalmagick.grimoire_prev_page", GLFW.GLFW_KEY_LEFT, KEY_CATEGORY);        // Key for going to the previous page in a grimoire entry
    public static final KeyMapping GRIMOIRE_PREV_TOPIC = new KeyMapping("key.primalmagick.grimoire_prev_topic", GLFW.GLFW_KEY_BACKSPACE, KEY_CATEGORY); // Key for going back to the previous grimoire topic
    public static final KeyMapping VIEW_AFFINITY_KEY = new KeyMapping("key.primalmagick.view_affinity", GLFW.GLFW_KEY_LEFT_SHIFT, KEY_CATEGORY);        // Key for viewing affinities in an item stack tooltip
    
    public static void onRegisterKeyMappings(Consumer<KeyMapping> consumer) {
        consumer.accept(CHANGE_SPELL_KEY);
        consumer.accept(CARPET_FORWARD_KEY);
        consumer.accept(CARPET_BACKWARD_KEY);
        consumer.accept(GRIMOIRE_NEXT_PAGE);
        consumer.accept(GRIMOIRE_PREV_PAGE);
        consumer.accept(GRIMOIRE_PREV_TOPIC);
        consumer.accept(VIEW_AFFINITY_KEY);
    }
}
