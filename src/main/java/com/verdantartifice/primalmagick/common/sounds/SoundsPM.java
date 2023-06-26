package com.verdantartifice.primalmagick.common.sounds;

import com.verdantartifice.primalmagick.PrimalMagick;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Deferred registry for mod sound events.
 * 
 * @author Daedalus4096
 */
public class SoundsPM {
    private static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, PrimalMagick.MODID);
    
    public static void init() {
        SOUNDS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
    
    public static final RegistryObject<SoundEvent> PAGE = SOUNDS.register("page", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(PrimalMagick.MODID, "page")));
    public static final RegistryObject<SoundEvent> POOF = SOUNDS.register("poof", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(PrimalMagick.MODID, "poof")));
    public static final RegistryObject<SoundEvent> SCAN = SOUNDS.register("scan", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(PrimalMagick.MODID, "scan")));
    public static final RegistryObject<SoundEvent> ROCKSLIDE = SOUNDS.register("rockslide", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(PrimalMagick.MODID, "rockslide")));
    public static final RegistryObject<SoundEvent> ICE = SOUNDS.register("ice", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(PrimalMagick.MODID, "ice")));
    public static final RegistryObject<SoundEvent> ELECTRIC = SOUNDS.register("electric", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(PrimalMagick.MODID, "electric")));
    public static final RegistryObject<SoundEvent> SUNBEAM = SOUNDS.register("sunbeam", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(PrimalMagick.MODID, "sunbeam")));
    public static final RegistryObject<SoundEvent> MOONBEAM = SOUNDS.register("moonbeam", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(PrimalMagick.MODID, "moonbeam")));
    public static final RegistryObject<SoundEvent> BLOOD = SOUNDS.register("blood", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(PrimalMagick.MODID, "blood")));
    public static final RegistryObject<SoundEvent> WHISPERS = SOUNDS.register("whispers", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(PrimalMagick.MODID, "whispers")));
    public static final RegistryObject<SoundEvent> ANGELS = SOUNDS.register("angels", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(PrimalMagick.MODID, "angels")));
    public static final RegistryObject<SoundEvent> HEAL = SOUNDS.register("heal", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(PrimalMagick.MODID, "heal")));
    public static final RegistryObject<SoundEvent> WINGFLAP = SOUNDS.register("wingflap", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(PrimalMagick.MODID, "wingflap")));
    public static final RegistryObject<SoundEvent> COINS = SOUNDS.register("coins", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(PrimalMagick.MODID, "coins")));
    public static final RegistryObject<SoundEvent> EGG_CRACK = SOUNDS.register("egg_crack", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(PrimalMagick.MODID, "egg_crack")));
    public static final RegistryObject<SoundEvent> SHIMMER = SOUNDS.register("shimmer", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(PrimalMagick.MODID, "shimmer")));
    public static final RegistryObject<SoundEvent> WRITING = SOUNDS.register("writing", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(PrimalMagick.MODID, "writing")));
    public static final RegistryObject<SoundEvent> TREEFOLK_HURT = SOUNDS.register("treefolk_hurt", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(PrimalMagick.MODID, "treefolk_hurt")));
    public static final RegistryObject<SoundEvent> TREEFOLK_DEATH = SOUNDS.register("treefolk_death", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(PrimalMagick.MODID, "treefolk_death")));
    public static final RegistryObject<SoundEvent> CLANK = SOUNDS.register("clank", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(PrimalMagick.MODID, "clank")));
    public static final RegistryObject<SoundEvent> HARP = SOUNDS.register("harp", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(PrimalMagick.MODID, "harp")));
    public static final RegistryObject<SoundEvent> RITUAL = SOUNDS.register("ritual", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(PrimalMagick.MODID, "ritual")));
}
