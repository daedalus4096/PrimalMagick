package com.verdantartifice.primalmagick.common.sounds;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Deferred registry for mod sound events.
 * 
 * @author Daedalus4096
 */
public class SoundsPM {
    private static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Constants.MOD_ID);
    
    public static void init() {
        SOUNDS.register(PrimalMagick.getModLoadingContext().getModEventBus());
    }
    
    public static final RegistryObject<SoundEvent> PAGE = SOUNDS.register("page", () -> SoundEvent.createVariableRangeEvent(ResourceUtils.loc("page")));
    public static final RegistryObject<SoundEvent> POOF = SOUNDS.register("poof", () -> SoundEvent.createVariableRangeEvent(ResourceUtils.loc("poof")));
    public static final RegistryObject<SoundEvent> SCAN = SOUNDS.register("scan", () -> SoundEvent.createVariableRangeEvent(ResourceUtils.loc("scan")));
    public static final RegistryObject<SoundEvent> ROCKSLIDE = SOUNDS.register("rockslide", () -> SoundEvent.createVariableRangeEvent(ResourceUtils.loc("rockslide")));
    public static final RegistryObject<SoundEvent> ICE = SOUNDS.register("ice", () -> SoundEvent.createVariableRangeEvent(ResourceUtils.loc("ice")));
    public static final RegistryObject<SoundEvent> ELECTRIC = SOUNDS.register("electric", () -> SoundEvent.createVariableRangeEvent(ResourceUtils.loc("electric")));
    public static final RegistryObject<SoundEvent> SUNBEAM = SOUNDS.register("sunbeam", () -> SoundEvent.createVariableRangeEvent(ResourceUtils.loc("sunbeam")));
    public static final RegistryObject<SoundEvent> MOONBEAM = SOUNDS.register("moonbeam", () -> SoundEvent.createVariableRangeEvent(ResourceUtils.loc("moonbeam")));
    public static final RegistryObject<SoundEvent> BLOOD = SOUNDS.register("blood", () -> SoundEvent.createVariableRangeEvent(ResourceUtils.loc("blood")));
    public static final RegistryObject<SoundEvent> WHISPERS = SOUNDS.register("whispers", () -> SoundEvent.createVariableRangeEvent(ResourceUtils.loc("whispers")));
    public static final RegistryObject<SoundEvent> ANGELS = SOUNDS.register("angels", () -> SoundEvent.createVariableRangeEvent(ResourceUtils.loc("angels")));
    public static final RegistryObject<SoundEvent> HEAL = SOUNDS.register("heal", () -> SoundEvent.createVariableRangeEvent(ResourceUtils.loc("heal")));
    public static final RegistryObject<SoundEvent> WINGFLAP = SOUNDS.register("wingflap", () -> SoundEvent.createVariableRangeEvent(ResourceUtils.loc("wingflap")));
    public static final RegistryObject<SoundEvent> COINS = SOUNDS.register("coins", () -> SoundEvent.createVariableRangeEvent(ResourceUtils.loc("coins")));
    public static final RegistryObject<SoundEvent> EGG_CRACK = SOUNDS.register("egg_crack", () -> SoundEvent.createVariableRangeEvent(ResourceUtils.loc("egg_crack")));
    public static final RegistryObject<SoundEvent> SHIMMER = SOUNDS.register("shimmer", () -> SoundEvent.createVariableRangeEvent(ResourceUtils.loc("shimmer")));
    public static final RegistryObject<SoundEvent> WRITING = SOUNDS.register("writing", () -> SoundEvent.createVariableRangeEvent(ResourceUtils.loc("writing")));
    public static final RegistryObject<SoundEvent> TREEFOLK_HURT = SOUNDS.register("treefolk_hurt", () -> SoundEvent.createVariableRangeEvent(ResourceUtils.loc("treefolk_hurt")));
    public static final RegistryObject<SoundEvent> TREEFOLK_DEATH = SOUNDS.register("treefolk_death", () -> SoundEvent.createVariableRangeEvent(ResourceUtils.loc("treefolk_death")));
    public static final RegistryObject<SoundEvent> CLANK = SOUNDS.register("clank", () -> SoundEvent.createVariableRangeEvent(ResourceUtils.loc("clank")));
    public static final RegistryObject<SoundEvent> HARP = SOUNDS.register("harp", () -> SoundEvent.createVariableRangeEvent(ResourceUtils.loc("harp")));
    public static final RegistryObject<SoundEvent> RITUAL = SOUNDS.register("ritual", () -> SoundEvent.createVariableRangeEvent(ResourceUtils.loc("ritual")));
}
