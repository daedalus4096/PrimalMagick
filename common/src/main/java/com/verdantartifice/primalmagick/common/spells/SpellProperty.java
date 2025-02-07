package com.verdantartifice.primalmagick.common.spells;

import com.mojang.serialization.Codec;
import com.verdantartifice.primalmagick.platform.Services;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.StringRepresentable;

/**
 * Definition of a spell property.  Spell components have zero to two properties that determine their
 * functionality (e.g. power, duration).  Each property is bounded to a min and max value.
 * 
 * @author Daedalus4096
 */
public record SpellProperty(ResourceLocation id, String translationKey, int min, int max) implements StringRepresentable {
    public static final Codec<SpellProperty> CODEC = ResourceLocation.CODEC.xmap(SpellPropertiesPM::get, SpellProperty::id);
    public static final StreamCodec<ByteBuf, SpellProperty> STREAM_CODEC = ResourceLocation.STREAM_CODEC.map(SpellPropertiesPM::get, SpellProperty::id);
    
    public Component getDescription() {
        return Component.translatable(this.translationKey);
    }

    @Override
    public String getSerializedName() {
        return this.id.toString();
    }

    public boolean is(TagKey<SpellProperty> tag) {
        return Services.SPELL_PROPERTIES_REGISTRY.getTag(tag).stream().anyMatch(this::equals);
    }
}
