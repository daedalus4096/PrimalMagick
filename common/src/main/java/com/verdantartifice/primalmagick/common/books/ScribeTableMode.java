package com.verdantartifice.primalmagick.common.books;

import com.mojang.serialization.Codec;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.IntFunction;

/**
 * Defines what mode of operation a Scribe's Table is currently operating in, and thus what menu
 * it's currently showing, or was last showing.
 * 
 * @author Daedalus4096
 */
public enum ScribeTableMode implements StringRepresentable {
    STUDY_VOCABULARY(0, "study_vocabulary"),
    GAIN_COMPREHENSION(1, "gain_comprehension"),
    TRANSCRIBE_WORKS(2, "transcribe_works");

    private static final IntFunction<ScribeTableMode> BY_ID = ByIdMap.continuous(ScribeTableMode::getId, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
    public static final Codec<ScribeTableMode> CODEC = StringRepresentable.fromValues(ScribeTableMode::values);
    public static final StreamCodec<ByteBuf, ScribeTableMode> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, ScribeTableMode::getId);

    private final int id;
    private final String tag;
    private final Component tooltip;
    private final Identifier iconSprite;
    
    ScribeTableMode(int id, String tag) {
        this.id = id;
        this.tag = tag;
        this.tooltip = Component.translatable("tooltip.primalmagick.scribe_table.mode." + tag);
        this.iconSprite = ResourceUtils.loc("scribe_table/" + tag);
    }

    public int getId() {
        return this.id;
    }

    public String getTag() {
        return this.tag;
    }
    
    public Component getTooltip() {
        return this.tooltip;
    }
    
    public Identifier getIconSprite() {
        return this.iconSprite;
    }
    
    @Override
    @NotNull
    public String getSerializedName() {
        return this.tag;
    }
    
    @Nullable
    public static ScribeTableMode fromName(@Nullable String name) {
        for (ScribeTableMode mode : values()) {
            if (mode.getSerializedName().equals(name)) {
                return mode;
            }
        }
        return null;
    }
}
