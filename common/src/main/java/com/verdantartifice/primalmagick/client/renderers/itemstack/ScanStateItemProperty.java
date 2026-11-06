package com.verdantartifice.primalmagick.client.renderers.itemstack;

import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.common.items.misc.ArcanometerItem;
import com.verdantartifice.primalmagick.common.util.RayTraceUtils;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Range-select item model property for the arcanometer's scan state. Ramps the antennae up when the
 * currently moused-over block or entity has not yet been scanned, and back down otherwise.
 *
 * @see com.verdantartifice.primalmagick.common.items.misc.ArcanometerItem
 */
public final class ScanStateItemProperty implements RangeSelectItemModelProperty {
    public static final MapCodec<ScanStateItemProperty> MAP_CODEC = MapCodec.unit(new ScanStateItemProperty());
    public static final Identifier ID = ResourceUtils.loc("scan_state");

    private float scanState = 0;

    @Override
    public float get(@NotNull ItemStack stack, ClientLevel world, ItemOwner owner, int seed) {
        LivingEntity entity = owner == null ? null : owner.asLivingEntity();
        if (entity instanceof Player player) {
            // If the currently moused-over block/item has not yet been scanned, raise the antennae
            if (ArcanometerItem.isMouseOverScannable(RayTraceUtils.getMouseOver(world, player), world, player)) {
                this.incrementScanState();
            } else {
                this.decrementScanState();
            }
            return this.scanState;
        } else {
            return 0F;
        }
    }

    private void incrementScanState() {
        this.scanState = Math.min(1.0F, this.scanState + 0.0625F);
    }

    private void decrementScanState() {
        this.scanState = Math.max(0.0F, this.scanState - 0.0625F);
    }

    @Override
    @NotNull
    public MapCodec<ScanStateItemProperty> type() {
        return MAP_CODEC;
    }
}
