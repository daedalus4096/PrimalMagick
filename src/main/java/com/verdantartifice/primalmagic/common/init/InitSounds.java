package com.verdantartifice.primalmagic.common.init;

import com.verdantartifice.primalmagic.PrimalMagic;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class InitSounds {
    public static void initSoundEvents(IForgeRegistry<SoundEvent> registry) {
        registry.register(createEvent("page"));
        registry.register(createEvent("poof"));
        registry.register(createEvent("scan"));
        registry.register(createEvent("rockslide"));
        registry.register(createEvent("ice"));
        registry.register(createEvent("electric"));
        registry.register(createEvent("sunbeam"));
        registry.register(createEvent("moonbeam"));
        registry.register(createEvent("blood"));
        registry.register(createEvent("whispers"));
        registry.register(createEvent("angels"));
    }
    
    private static SoundEvent createEvent(String name) {
        ResourceLocation location = new ResourceLocation(PrimalMagic.MODID, name);
        SoundEvent event = new SoundEvent(location);
        event.setRegistryName(location);
        return event;
    }
}
