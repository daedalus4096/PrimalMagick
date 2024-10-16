package com.verdantartifice.primalmagick.common.config;

import com.verdantartifice.primalmagick.common.theorycrafting.TheorycraftSpeed;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * Definition of common and client configuration files for the mod.
 * 
 * @author Daedalus4096
 */
@EventBusSubscriber
public class ConfigNeoforge {
    protected static final String CATEGORY_MISC = "misc";
    protected static final String CATEGORY_RADIAL = "radial";
    
    protected static ModConfigSpec COMMON_CONFIG_SPEC;
    protected static ModConfigSpec CLIENT_CONFIG_SPEC;
    protected static boolean IS_REGISTERED = false;
    
    public static ModConfigSpec.BooleanValue SHOW_AFFINITIES;
    public static ModConfigSpec.BooleanValue SHOW_WAND_HUD;
    public static ModConfigSpec.BooleanValue RADIAL_RELEASE_TO_SWITCH;
    public static ModConfigSpec.BooleanValue RADIAL_CLIP_MOUSE;
    public static ModConfigSpec.BooleanValue RADIAL_ALLOW_CLICK_OUTSIDE_BOUNDS;
    
    public static ModConfigSpec.BooleanValue SHOW_UNSCANNED_AFFINITIES;
    public static ModConfigSpec.EnumValue<TheorycraftSpeed> THEORYCRAFT_SPEED;
    
    static {
        buildCommonConfigSpec();
        buildClientConfigSpec();
    }
    
    protected static void buildCommonConfigSpec() {
        // Define the common config file spec
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        
        builder.comment("Misc settings").push(CATEGORY_MISC);
        SHOW_UNSCANNED_AFFINITIES = builder.comment("Show affinities of blocks and items even without scanning them").define("showUnscannedAffinities", false);
        THEORYCRAFT_SPEED = builder.comment("Progress rate modifier for Research Table theory yields").defineEnum("theorycraftSpeed", TheorycraftSpeed.NORMAL);
        builder.pop();
        
        COMMON_CONFIG_SPEC = builder.build();
    }
    
    protected static void buildClientConfigSpec() {
        // Define the client-only config file spec
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        
        builder.comment("Misc settings").push(CATEGORY_MISC);
        SHOW_AFFINITIES = builder.comment("Item affinities are hidden by default and pressing shift reveals them.", "Setting this to 'true' will reverse this behavior.").define("showAffinities", false);
        SHOW_WAND_HUD = builder.comment("Whether to show the wand HUD while a wand or staff is held in the main hand.").define("showWandHud", true);
        builder.pop();
        
        builder.comment("Radial menu settings").push(CATEGORY_RADIAL);
        RADIAL_RELEASE_TO_SWITCH = builder.comment("If true, releasing the menu key will activate the highlighted item; otherwise requires a click").define("releaseToSwitch", true);
        RADIAL_CLIP_MOUSE = builder.comment("If true, the radial menu will try to prevent the mouse from leaving the outer circle").define("clipMouse", false);
        RADIAL_ALLOW_CLICK_OUTSIDE_BOUNDS = builder.comment("If true, the radial menu will allow clicking outside the outer circle to activate the highlighted item").define("allowClickOutsideBounds", false);
        builder.pop();
        
        CLIENT_CONFIG_SPEC = builder.build();
    }
    
    public static void register(ModContainer container) {
        if (IS_REGISTERED) {
            // Only allow registration once
            throw new IllegalStateException("Primal Magick config spec is already registered!");
        }
        container.registerConfig(ModConfig.Type.COMMON, ConfigNeoforge.COMMON_CONFIG_SPEC);
        container.registerConfig(ModConfig.Type.CLIENT, ConfigNeoforge.CLIENT_CONFIG_SPEC);
        IS_REGISTERED = true;
    }
}
