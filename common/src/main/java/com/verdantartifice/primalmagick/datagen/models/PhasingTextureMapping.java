package com.verdantartifice.primalmagick.datagen.models;

import com.verdantartifice.primalmagick.common.blockstates.properties.TimePhase;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

public class PhasingTextureMapping {
    private final Map<TextureSlot, Function<TimePhase, Identifier>> slots = new HashMap<>();
    private final Set<TextureSlot> forcedSlots = new HashSet<>();

    public PhasingTextureMapping put(TextureSlot slot, Function<TimePhase, Identifier> textureFunc) {
        this.slots.put(slot, textureFunc);
        return this;
    }

    public PhasingTextureMapping putForced(TextureSlot slot, Function<TimePhase, Identifier> textureFunc) {
        this.forcedSlots.add(slot);
        return this.put(slot, textureFunc);
    }

    public Stream<TextureSlot> getForced() {
        return this.forcedSlots.stream();
    }

    public PhasingTextureMapping copySlot(TextureSlot source, TextureSlot destination) {
        this.slots.put(destination, this.slots.get(source));
        return this;
    }

    public PhasingTextureMapping copyForced(TextureSlot source, TextureSlot destination) {
        this.forcedSlots.add(destination);
        return this.copySlot(source, destination);
    }

    public PhasingTextureMapping copyAndUpdate(TextureSlot slot, Function<TimePhase, Identifier> textureFunc) {
        PhasingTextureMapping newMapping = new PhasingTextureMapping();
        newMapping.slots.putAll(this.slots);
        newMapping.forcedSlots.addAll(this.forcedSlots);
        newMapping.put(slot, textureFunc);
        return newMapping;
    }

    public PhasingTextureMapping copyAndUpdate(TextureSlot source, TextureSlot destination) {
        return this.copyAndUpdate(destination, this.slots.get(source));
    }

    public TextureMapping resolve(TimePhase phase) {
        TextureMapping newMapping = new TextureMapping();
        this.slots.forEach((slot, textureFunc) -> {
            if (this.forcedSlots.contains(slot)) {
                newMapping.putForced(slot, textureFunc.apply(phase));
            } else {
                newMapping.put(slot, textureFunc.apply(phase));
            }
        });
        return newMapping;
    }

    public static PhasingTextureMapping logColumn(Block block) {
        return new PhasingTextureMapping()
                .put(TextureSlot.SIDE, phase -> TextureMapping.getBlockTexture(block, "_" + phase))
                .put(TextureSlot.END, phase -> TextureMapping.getBlockTexture(block, "_top_" + phase))
                .put(TextureSlot.PARTICLE, phase -> TextureMapping.getBlockTexture(block, "_" + phase));
    }

    public static PhasingTextureMapping leaves(Block block) {
        return new PhasingTextureMapping()
                .put(TextureSlot.ALL, phase -> TextureMapping.getBlockTexture(block, "_" + phase));
    }
}
