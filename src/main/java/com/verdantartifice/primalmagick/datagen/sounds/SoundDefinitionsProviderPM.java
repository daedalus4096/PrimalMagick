package com.verdantartifice.primalmagick.datagen.sounds;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.sounds.SoundsPM;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SoundDefinition;
import net.minecraftforge.common.data.SoundDefinitionsProvider;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Data provider for mod sound definitions.
 * 
 * @author Daedalus4096
 */
public class SoundDefinitionsProviderPM extends SoundDefinitionsProvider {
    private static final Logger LOGGER = LogManager.getLogger();

    private final List<ResourceLocation> generatedSounds = new ArrayList<>();
    
    public SoundDefinitionsProviderPM(PackOutput output, ExistingFileHelper helper) {
        super(output, PrimalMagick.MODID, helper);
    }
    
    @Override
    public void registerSounds() {
        // Register sound event data
        this.addSingle(SoundsPM.PAGE);
        this.addSingle(SoundsPM.POOF);
        this.addSingle(SoundsPM.SCAN);
        this.addMultiple(SoundsPM.ROCKSLIDE, 3);
        this.addMultiple(SoundsPM.ICE, 3);
        this.addSingle(SoundsPM.ELECTRIC);
        this.addMultiple(SoundsPM.SUNBEAM, 3);
        this.addSingle(SoundsPM.MOONBEAM);
        this.addSingle(SoundsPM.BLOOD);
        this.addSingle(SoundsPM.WHISPERS);
        this.addSingle(SoundsPM.ANGELS);
        this.addSingle(SoundsPM.HEAL);
        this.addSingle(SoundsPM.WINGFLAP);
        this.addMultiple(SoundsPM.COINS, 3);
        this.addSingle(SoundsPM.EGG_CRACK);
        this.addSingle(SoundsPM.SHIMMER);
        this.addSingle(SoundsPM.WRITING);
        this.addMultiple(SoundsPM.TREEFOLK_HURT, 3);
        this.addMultiple(SoundsPM.TREEFOLK_DEATH, 2);
        this.addMultiple(SoundsPM.CLANK, 3);
        this.addSingle(SoundsPM.HARP);
        this.addSingle(SoundsPM.RITUAL);
        
        // Verify that all mod sound events have been registered
        this.verifyComplete();
    }

    private void addSingle(RegistryObject<SoundEvent> eventSupplier) {
        this.add(eventSupplier, definition().with(sound(eventSupplier.getId())));
    }
    
    private void addMultiple(RegistryObject<SoundEvent> eventSupplier, int count) {
        SoundDefinition def = definition();
        IntStream.rangeClosed(1, count).forEach(val -> def.with(sound(eventSupplier.getId().withSuffix(Integer.toString(val)))));
        this.add(eventSupplier, def);
    }

    @Override
    protected void add(ResourceLocation soundEvent, SoundDefinition definition) {
        super.add(soundEvent, definition);
        this.generatedSounds.add(soundEvent);
    }
    
    protected void verifyComplete() {
        List<ResourceLocation> registeredSounds = new ArrayList<>(ForgeRegistries.SOUND_EVENTS.getKeys().stream().filter(loc -> loc.getNamespace().equals(PrimalMagick.MODID)).toList());
        registeredSounds.removeAll(this.generatedSounds);
        if (!registeredSounds.isEmpty()) {
            registeredSounds.forEach(loc -> LOGGER.warn("No sound definition generated for sound {}", loc.toString()));
            throw new IllegalStateException("Missing sound definitions for " + PrimalMagick.MODID);
        }
    }
}
