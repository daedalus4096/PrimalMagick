package com.verdantartifice.primalmagic.common.sounds;

import com.verdantartifice.primalmagic.PrimalMagic;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Deferred registry for mod sound events.
 * 
 * @author Daedalus4096
 */
public class SoundsPM {
    private static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, PrimalMagic.MODID);
    
    public static void init() {
        SOUNDS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
    
    public static final RegistryObject<SoundEvent> PAGE = SOUNDS.register("page", () -> new SoundEvent(new ResourceLocation(PrimalMagic.MODID, "page")));
    public static final RegistryObject<SoundEvent> POOF = SOUNDS.register("poof", () -> new SoundEvent(new ResourceLocation(PrimalMagic.MODID, "poof")));
    public static final RegistryObject<SoundEvent> SCAN = SOUNDS.register("scan", () -> new SoundEvent(new ResourceLocation(PrimalMagic.MODID, "scan")));
    public static final RegistryObject<SoundEvent> ROCKSLIDE = SOUNDS.register("rockslide", () -> new SoundEvent(new ResourceLocation(PrimalMagic.MODID, "rockslide")));
    public static final RegistryObject<SoundEvent> ICE = SOUNDS.register("ice", () -> new SoundEvent(new ResourceLocation(PrimalMagic.MODID, "ice")));
    public static final RegistryObject<SoundEvent> ELECTRIC = SOUNDS.register("electric", () -> new SoundEvent(new ResourceLocation(PrimalMagic.MODID, "electric")));
    public static final RegistryObject<SoundEvent> SUNBEAM = SOUNDS.register("sunbeam", () -> new SoundEvent(new ResourceLocation(PrimalMagic.MODID, "sunbeam")));
    public static final RegistryObject<SoundEvent> MOONBEAM = SOUNDS.register("moonbeam", () -> new SoundEvent(new ResourceLocation(PrimalMagic.MODID, "moonbeam")));
    public static final RegistryObject<SoundEvent> BLOOD = SOUNDS.register("blood", () -> new SoundEvent(new ResourceLocation(PrimalMagic.MODID, "blood")));
    public static final RegistryObject<SoundEvent> WHISPERS = SOUNDS.register("whispers", () -> new SoundEvent(new ResourceLocation(PrimalMagic.MODID, "whispers")));
    public static final RegistryObject<SoundEvent> ANGELS = SOUNDS.register("angels", () -> new SoundEvent(new ResourceLocation(PrimalMagic.MODID, "angels")));
    public static final RegistryObject<SoundEvent> HEAL = SOUNDS.register("heal", () -> new SoundEvent(new ResourceLocation(PrimalMagic.MODID, "heal")));
    public static final RegistryObject<SoundEvent> WINGFLAP = SOUNDS.register("wingflap", () -> new SoundEvent(new ResourceLocation(PrimalMagic.MODID, "wingflap")));
    public static final RegistryObject<SoundEvent> COINS = SOUNDS.register("coins", () -> new SoundEvent(new ResourceLocation(PrimalMagic.MODID, "coins")));
    public static final RegistryObject<SoundEvent> EGG_CRACK = SOUNDS.register("egg_crack", () -> new SoundEvent(new ResourceLocation(PrimalMagic.MODID, "egg_crack")));
    public static final RegistryObject<SoundEvent> SHIMMER = SOUNDS.register("shimmer", () -> new SoundEvent(new ResourceLocation(PrimalMagic.MODID, "shimmer")));
    public static final RegistryObject<SoundEvent> WRITING = SOUNDS.register("writing", () -> new SoundEvent(new ResourceLocation(PrimalMagic.MODID, "writing")));
    public static final RegistryObject<SoundEvent> TREEFOLK_HURT = SOUNDS.register("treefolk_hurt", () -> new SoundEvent(new ResourceLocation(PrimalMagic.MODID, "treefolk_hurt")));
    public static final RegistryObject<SoundEvent> TREEFOLK_DEATH = SOUNDS.register("treefolk_death", () -> new SoundEvent(new ResourceLocation(PrimalMagic.MODID, "treefolk_death")));
    public static final RegistryObject<SoundEvent> CLANK = SOUNDS.register("clank", () -> new SoundEvent(new ResourceLocation(PrimalMagic.MODID, "clank")));
    public static final RegistryObject<SoundEvent> HARP = SOUNDS.register("harp", () -> new SoundEvent(new ResourceLocation(PrimalMagic.MODID, "harp")));
}
