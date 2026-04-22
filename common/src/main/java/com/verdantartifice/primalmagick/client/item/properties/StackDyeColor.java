package com.verdantartifice.primalmagick.client.item.properties;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.common.items.IHasDyeColor;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Model selection property based on the dye color of the item stack. This differs from an item tinter in that a fully
 * discrete item model is used for each dye color, rather than applying a tint to a single model.
 *
 * @author daedalus4096
 */
public record StackDyeColor() implements SelectItemModelProperty<DyeColor> {
    public static final SelectItemModelProperty.Type<StackDyeColor, DyeColor> TYPE = SelectItemModelProperty.Type.create(
            MapCodec.unit(new StackDyeColor()),
            DyeColor.CODEC
    );

    @Override
    public @Nullable DyeColor get(@NotNull ItemStack itemStack, @Nullable ClientLevel clientLevel, @Nullable LivingEntity livingEntity, int seed, @NotNull ItemDisplayContext itemDisplayContext) {
        if (itemStack.getItem() instanceof IHasDyeColor hasDyeColor) {
            return hasDyeColor.getDyeColor(itemStack);
        }
        return null;
    }

    @Override
    public @NotNull Codec<DyeColor> valueCodec() {
        return DyeColor.CODEC;
    }

    @Override
    public @NotNull Type<? extends SelectItemModelProperty<DyeColor>, DyeColor> type() {
        return TYPE;
    }
}
