package com.verdantartifice.primalmagick.common.sounds;

import com.verdantartifice.primalmagick.PrimalMagick;

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
    
    public static final RegistryObject<SoundEvent> PAGE = SOUNDS.register("page", () -> SoundEvent.createVariableRangeEvent(PrimalMagick.resource("page")));
    public static final RegistryObject<SoundEvent> POOF = SOUNDS.register("poof", () -> SoundEvent.createVariableRangeEvent(PrimalMagick.resource("poof")));
    public static final RegistryObject<SoundEvent> SCAN = SOUNDS.register("scan", () -> SoundEvent.createVariableRangeEvent(PrimalMagick.resource("scan")));
    public static final RegistryObject<SoundEvent> ROCKSLIDE = SOUNDS.register("rockslide", () -> SoundEvent.createVariableRangeEvent(PrimalMagick.resource("rockslide")));
    public static final RegistryObject<SoundEvent> ICE = SOUNDS.register("ice", () -> SoundEvent.createVariableRangeEvent(PrimalMagick.resource("ice")));
    public static final RegistryObject<SoundEvent> ELECTRIC = SOUNDS.register("electric", () -> SoundEvent.createVariableRangeEvent(PrimalMagick.resource("electric")));
    public static final RegistryObject<SoundEvent> SUNBEAM = SOUNDS.register("sunbeam", () -> SoundEvent.createVariableRangeEvent(PrimalMagick.resource("sunbeam")));
    public static final RegistryObject<SoundEvent> MOONBEAM = SOUNDS.register("moonbeam", () -> SoundEvent.createVariableRangeEvent(PrimalMagick.resource("moonbeam")));
    public static final RegistryObject<SoundEvent> BLOOD = SOUNDS.register("blood", () -> SoundEvent.createVariableRangeEvent(PrimalMagick.resource("blood")));
    public static final RegistryObject<SoundEvent> WHISPERS = SOUNDS.register("whispers", () -> SoundEvent.createVariableRangeEvent(PrimalMagick.resource("whispers")));
    public static final RegistryObject<SoundEvent> ANGELS = SOUNDS.register("angels", () -> SoundEvent.createVariableRangeEvent(PrimalMagick.resource("angels")));
    public static final RegistryObject<SoundEvent> HEAL = SOUNDS.register("heal", () -> SoundEvent.createVariableRangeEvent(PrimalMagick.resource("heal")));
    public static final RegistryObject<SoundEvent> WINGFLAP = SOUNDS.register("wingflap", () -> SoundEvent.createVariableRangeEvent(PrimalMagick.resource("wingflap")));
    public static final RegistryObject<SoundEvent> COINS = SOUNDS.register("coins", () -> SoundEvent.createVariableRangeEvent(PrimalMagick.resource("coins")));
    public static final RegistryObject<SoundEvent> EGG_CRACK = SOUNDS.register("egg_crack", () -> SoundEvent.createVariableRangeEvent(PrimalMagick.resource("egg_crack")));
    public static final RegistryObject<SoundEvent> SHIMMER = SOUNDS.register("shimmer", () -> SoundEvent.createVariableRangeEvent(PrimalMagick.resource("shimmer")));
    public static final RegistryObject<SoundEvent> WRITING = SOUNDS.register("writing", () -> SoundEvent.createVariableRangeEvent(PrimalMagick.resource("writing")));
    public static final RegistryObject<SoundEvent> TREEFOLK_HURT = SOUNDS.register("treefolk_hurt", () -> SoundEvent.createVariableRangeEvent(PrimalMagick.resource("treefolk_hurt")));
    public static final RegistryObject<SoundEvent> TREEFOLK_DEATH = SOUNDS.register("treefolk_death", () -> SoundEvent.createVariableRangeEvent(PrimalMagick.resource("treefolk_death")));
    public static final RegistryObject<SoundEvent> CLANK = SOUNDS.register("clank", () -> SoundEvent.createVariableRangeEvent(PrimalMagick.resource("clank")));
    public static final RegistryObject<SoundEvent> HARP = SOUNDS.register("harp", () -> SoundEvent.createVariableRangeEvent(PrimalMagick.resource("harp")));
    public static final RegistryObject<SoundEvent> RITUAL = SOUNDS.register("ritual", () -> SoundEvent.createVariableRangeEvent(PrimalMagick.resource("ritual")));
}
