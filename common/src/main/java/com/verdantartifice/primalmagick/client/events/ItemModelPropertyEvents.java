package com.verdantartifice.primalmagick.client.events;

import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.client.item.properties.AntennaAngle;
import com.verdantartifice.primalmagick.client.item.properties.StackDyeColor;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperty;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiConsumer;

/**
 * Respond to client-only item model property definition events.
 *
 * @author Daedalus4096
 */
public class ItemModelPropertyEvents {
    public static void onSelectItemModelPropertyInit(BiConsumer<ResourceLocation, SelectItemModelProperty.Type<?, ?>> propertyMapper) {
        propertyMapper.accept(ResourceUtils.loc("dye_color"), StackDyeColor.TYPE);
    }

    public static void onRangeSelectItemModelPropertyInit(BiConsumer<ResourceLocation, MapCodec<? extends RangeSelectItemModelProperty>> propertyMapper) {
        propertyMapper.accept(ResourceUtils.loc("antenna_angle"), AntennaAngle.MAP_CODEC);
    }
}
