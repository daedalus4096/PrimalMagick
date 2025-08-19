package com.verdantartifice.primalmagick.client.color.item;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.components.DataComponentsPM;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Item tint source that colors its item based on an attached source tint data component, or the given default if said
 * component is not present.
 *
 * @author daedalus4096
 */
public record SourceTint(int defaultColor) implements ItemTintSource {
    public static final MapCodec<SourceTint> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ExtraCodecs.RGB_COLOR_CODEC.fieldOf("default").forGetter(SourceTint::defaultColor)
        ).apply(instance, SourceTint::new));

    public SourceTint() {
        this(-1);
    }

    @Override
    public int calculate(@NotNull ItemStack itemStack, @Nullable ClientLevel clientLevel, @Nullable LivingEntity livingEntity) {
        return itemStack.has(DataComponentsPM.SOURCE_TINT.get()) ?
            itemStack.get(DataComponentsPM.SOURCE_TINT.get()).getColor() :
            this.defaultColor;
    }

    @Override
    public @NotNull MapCodec<SourceTint> type() {
        return MAP_CODEC;
    }
}
