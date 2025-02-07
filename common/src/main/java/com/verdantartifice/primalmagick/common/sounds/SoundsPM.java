package com.verdantartifice.primalmagick.common.sounds;

import com.verdantartifice.primalmagick.common.registries.IRegistryItem;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.sounds.SoundEvent;

import java.util.function.Supplier;

/**
 * Deferred registry for mod sound events.
 * 
 * @author Daedalus4096
 */
public class SoundsPM {
    public static void init() {
        // Pass the service initialization through this class so it gets class loaded and fields registered
        Services.SOUND_EVENTS_REGISTRY.init();
    }

    public static final IRegistryItem<SoundEvent, SoundEvent> PAGE = register("page");
    public static final IRegistryItem<SoundEvent, SoundEvent> POOF = register("poof");
    public static final IRegistryItem<SoundEvent, SoundEvent> SCAN = register("scan");
    public static final IRegistryItem<SoundEvent, SoundEvent> ROCKSLIDE = register("rockslide");
    public static final IRegistryItem<SoundEvent, SoundEvent> ICE = register("ice");
    public static final IRegistryItem<SoundEvent, SoundEvent> ELECTRIC = register("electric");
    public static final IRegistryItem<SoundEvent, SoundEvent> SUNBEAM = register("sunbeam");
    public static final IRegistryItem<SoundEvent, SoundEvent> MOONBEAM = register("moonbeam");
    public static final IRegistryItem<SoundEvent, SoundEvent> BLOOD = register("blood");
    public static final IRegistryItem<SoundEvent, SoundEvent> WHISPERS = register("whispers");
    public static final IRegistryItem<SoundEvent, SoundEvent> ANGELS = register("angels");
    public static final IRegistryItem<SoundEvent, SoundEvent> HEAL = register("heal");
    public static final IRegistryItem<SoundEvent, SoundEvent> WING_FLAP = register("wingflap");
    public static final IRegistryItem<SoundEvent, SoundEvent> COINS = register("coins");
    public static final IRegistryItem<SoundEvent, SoundEvent> EGG_CRACK = register("egg_crack");
    public static final IRegistryItem<SoundEvent, SoundEvent> SHIMMER = register("shimmer");
    public static final IRegistryItem<SoundEvent, SoundEvent> WRITING = register("writing");
    public static final IRegistryItem<SoundEvent, SoundEvent> TREEFOLK_HURT = register("treefolk_hurt");
    public static final IRegistryItem<SoundEvent, SoundEvent> TREEFOLK_DEATH = register("treefolk_death");
    public static final IRegistryItem<SoundEvent, SoundEvent> CLANK = register("clank");
    public static final IRegistryItem<SoundEvent, SoundEvent> HARP = register("harp");
    public static final IRegistryItem<SoundEvent, SoundEvent> RITUAL = register("ritual");

    private static IRegistryItem<SoundEvent, SoundEvent> register(String name, Supplier<SoundEvent> supplier) {
        return Services.SOUND_EVENTS_REGISTRY.register(name, supplier);
    }
    
    private static IRegistryItem<SoundEvent, SoundEvent> register(String name) {
        return register(name, () -> SoundEvent.createVariableRangeEvent(ResourceUtils.loc(name)));
    }
}
