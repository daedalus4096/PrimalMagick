package com.verdantartifice.primalmagic.common.config;

import java.nio.file.Path;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLPaths;

@Mod.EventBusSubscriber
public class Config {
    protected static final String CATEGORY_MISC = "misc";
    
    protected static ForgeConfigSpec COMMON_CONFIG_SPEC;
    protected static boolean IS_REGISTERED = false;
    
    public static ForgeConfigSpec.BooleanValue SHOW_UNSCANNED_AFFINITIES;
    
    static {
        buildConfigSpecs();
    }
    
    protected static void buildConfigSpecs() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        
        builder.comment("Misc settings").push(CATEGORY_MISC);
        SHOW_UNSCANNED_AFFINITIES = builder.comment("Show affinities of blocks and items even without scanning them").define("showUnscannedAffinities", false);
        builder.pop();
        
        COMMON_CONFIG_SPEC = builder.build();
    }
    
    protected static void loadConfig(ForgeConfigSpec spec, Path path) {
        final CommentedFileConfig configData = CommentedFileConfig.builder(path)
                .sync()
                .autosave()
                .writingMode(WritingMode.REPLACE)
                .build();
        configData.load();
        spec.setConfig(configData);
    }
    
    public static void register() {
        if (IS_REGISTERED) {
            throw new IllegalStateException("Primal Magic config spec is already registered!");
        }
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_CONFIG_SPEC);
        Config.loadConfig(Config.COMMON_CONFIG_SPEC, FMLPaths.CONFIGDIR.get().resolve("primalmagic-common.toml"));
        IS_REGISTERED = true;
    }
    
    @SubscribeEvent
    public static void onLoad(final ModConfig.Loading event) {
        // Perform load-time operations here
    }
    
    @SubscribeEvent
    public static void onReload(final ModConfig.ConfigReloading event) {
        // Perform reload-time operations here
    }
}
