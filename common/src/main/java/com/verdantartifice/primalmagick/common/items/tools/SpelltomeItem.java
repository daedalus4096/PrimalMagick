package com.verdantartifice.primalmagick.common.items.tools;

import com.verdantartifice.primalmagick.client.renderers.itemstack.SpelltomeISTER;
import com.verdantartifice.primalmagick.common.items.IHasCustomRenderer;
import com.verdantartifice.primalmagick.common.misc.DeviceTier;
import com.verdantartifice.primalmagick.common.misc.ITieredDevice;
import com.verdantartifice.primalmagick.common.sounds.SoundsPM;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public abstract class SpelltomeItem extends Item implements Equipable, IHasCustomRenderer, ITieredDevice {
    private final DeviceTier tier;
    private BlockEntityWithoutLevelRenderer customRenderer;

    public SpelltomeItem(DeviceTier tier, Item.Properties pProperties) {
        super(pProperties);
        this.tier = tier;
    }

    @Override
    public Supplier<BlockEntityWithoutLevelRenderer> getCustomRendererSupplier() {
        if (this.customRenderer == null) {
            this.customRenderer = this.getCustomRendererSupplierUncached().get();
        }
        return () -> this.customRenderer;
    }

    @Override
    public Supplier<BlockEntityWithoutLevelRenderer> getCustomRendererSupplierUncached() {
        return SpelltomeISTER::new;
    }

    @Override
    public @NotNull EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.OFFHAND;
    }

    @Override
    public @NotNull Holder<SoundEvent> getEquipSound() {
        return SoundsPM.PAGE.getHolder();
    }

    @Override
    public DeviceTier getDeviceTier() {
        return this.tier;
    }
}
