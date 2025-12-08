package com.verdantartifice.primalmagick.client.item.properties;

import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.common.components.DataComponentsPM;
import com.verdantartifice.primalmagick.common.items.misc.ArcanometerItem;
import com.verdantartifice.primalmagick.common.util.RayTraceUtils;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Model selection property based on the angle at which the Arcanometer's antenna should be rendered.
 *
 * @author Daedalus4096
 */
public record AntennaAngle() implements RangeSelectItemModelProperty {
    public static final MapCodec<AntennaAngle> MAP_CODEC = MapCodec.unit(new AntennaAngle());

    @Override
    public float get(@NotNull ItemStack itemStack, @Nullable ClientLevel clientLevel, @Nullable ItemOwner itemOwner, int seed) {
        float scanState = itemStack.getOrDefault(DataComponentsPM.ANTENNA_ANGLE.get(), 0F);
        if (clientLevel != null && itemOwner != null && itemOwner.asLivingEntity() instanceof Player player) {
            // If the currently moused-over block/item has not yet been scanned, raise the antennae
            if (ArcanometerItem.isMouseOverScannable(RayTraceUtils.getMouseOver(clientLevel, player), clientLevel, player)) {
                scanState = this.incrementScanState(itemStack, scanState);
            } else {
                scanState = this.decrementScanState(itemStack, scanState);
            }
        }
        return scanState;
    }

    private float incrementScanState(@NotNull ItemStack itemStack, float currentState) {
        float newState = Math.min(1.0F, currentState + 0.0625F);
        if (newState != currentState) {
            itemStack.set(DataComponentsPM.ANTENNA_ANGLE.get(), newState);
        }
        return newState;
    }

    private float decrementScanState(@NotNull ItemStack itemStack, float currentState) {
        float newState = Math.max(0.0F, currentState - 0.0625F);
        if (newState != currentState) {
            itemStack.set(DataComponentsPM.ANTENNA_ANGLE.get(), newState);
        }
        return newState;
    }

    @Override
    @NotNull
    public MapCodec<? extends RangeSelectItemModelProperty> type() {
        return MAP_CODEC;
    }
}
