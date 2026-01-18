package com.verdantartifice.primalmagick.datagen.sounds;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.common.registries.IRegistryItem;
import com.verdantartifice.primalmagick.common.sounds.SoundsPM;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.common.data.SoundDefinition;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Data provider for mod sound definitions.
 * 
 * @author Daedalus4096
 */
public class SoundDefinitionsProviderPMNeoforge extends SoundDefinitionsProvider {
    private static final Logger LOGGER = LogManager.getLogger();

    private final List<Identifier> generatedSounds = new ArrayList<>();

    public SoundDefinitionsProviderPMNeoforge(PackOutput output) {
        super(output, Constants.MOD_ID);
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
        this.addSingle(SoundsPM.WING_FLAP);
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

    private void addSingle(IRegistryItem<SoundEvent, SoundEvent> eventSupplier) {
        this.add(eventSupplier.get(), definition().with(sound(eventSupplier.getId())));
    }
    
    private void addMultiple(IRegistryItem<SoundEvent, SoundEvent> eventSupplier, int count) {
        SoundDefinition def = definition();
        IntStream.rangeClosed(1, count).forEach(val -> def.with(sound(eventSupplier.getId().withSuffix(Integer.toString(val)))));
        this.add(eventSupplier.get(), def);
    }

    @Override
    protected void add(@NotNull Identifier soundEvent, @NotNull SoundDefinition definition) {
        super.add(soundEvent, definition);
        this.generatedSounds.add(soundEvent);
    }
    
    protected void verifyComplete() {
        List<Identifier> registeredSounds = new ArrayList<>(Services.SOUND_EVENTS_REGISTRY.getAllKeys().stream().filter(loc -> loc.getNamespace().equals(Constants.MOD_ID)).toList());
        registeredSounds.removeAll(this.generatedSounds);
        if (!registeredSounds.isEmpty()) {
            registeredSounds.forEach(loc -> LOGGER.warn("No sound definition generated for sound {}", loc.toString()));
            throw new IllegalStateException("Missing sound definitions for " + Constants.MOD_ID);
        }
    }
}
