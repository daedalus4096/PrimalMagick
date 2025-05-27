package com.verdantartifice.primalmagick.common.items.tools;

import com.verdantartifice.primalmagick.client.renderers.itemstack.ManaOrbISTER;
import com.verdantartifice.primalmagick.common.capabilities.ManaStorage;
import com.verdantartifice.primalmagick.common.components.DataComponentsPM;
import com.verdantartifice.primalmagick.common.items.IHasCustomRenderer;
import com.verdantartifice.primalmagick.common.misc.DeviceTier;
import com.verdantartifice.primalmagick.common.misc.ITieredDevice;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import com.verdantartifice.primalmagick.common.wands.IManaContainer;
import com.verdantartifice.primalmagick.common.wands.WandGem;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public abstract class ManaOrbItem extends Item implements Equipable, IHasCustomRenderer, ITieredDevice, IManaContainer {
    private final DeviceTier tier;
    private BlockEntityWithoutLevelRenderer customRenderer;

    public ManaOrbItem(DeviceTier tier, Item.Properties pProperties) {
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
        return ManaOrbISTER::new;
    }

    @Override
    public @NotNull EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.OFFHAND;
    }

    @Override
    public DeviceTier getDeviceTier() {
        return this.tier;
    }

    @Override
    public boolean isEnchantable(ItemStack pStack) {
        return true;
    }

    @Override
    public int getEnchantmentValue() {
        return switch (this.getDeviceTier()) {
            case BASIC -> 13;
            case ENCHANTED -> 18;
            case FORBIDDEN -> 23;
            case HEAVENLY, CREATIVE -> 28;
        };
    }

    private @NotNull WandGem getWandGemEquivalent() {
        return switch (this.getDeviceTier()) {
            case BASIC -> WandGem.APPRENTICE;
            case ENCHANTED -> WandGem.ADEPT;
            case FORBIDDEN -> WandGem.WIZARD;
            case HEAVENLY -> WandGem.ARCHMAGE;
            case CREATIVE -> WandGem.CREATIVE;
        };
    }

    public ManaStorage getManaStorage(ItemStack stack) {
        return stack.getOrDefault(DataComponentsPM.CAPABILITY_MANA_STORAGE.get(), ManaStorage.EMPTY);
    }

    @Override
    public int getMaxMana(@Nullable ItemStack stack, @Nullable Source source) {
        // FIXME Get the per-source amount
        return this.getWandGemEquivalent().getCapacity();
    }

    @Override
    public int addMana(@Nullable ItemStack stack, @Nullable Source source, int amount) {
        // TODO Stub
        return 0;
    }

    @Override
    public int addMana(@Nullable ItemStack stack, @Nullable Source source, int amount, int max) {
        // TODO Stub
        return 0;
    }

    @Override
    public int deductMana(@Nullable ItemStack stack, @Nullable Source source, int amount) {
        // TODO Stub
        return 0;
    }

    @Override
    public boolean consumeMana(@Nullable ItemStack stack, @Nullable Player player, @Nullable Source source, int amount, HolderLookup.Provider registries) {
        // TODO Stub
        return false;
    }

    @Override
    public boolean consumeMana(@Nullable ItemStack stack, @Nullable Player player, @Nullable SourceList sources, HolderLookup.Provider registries) {
        // TODO Stub
        return false;
    }

    @Override
    public boolean removeManaRaw(@Nullable ItemStack stack, @Nullable Source source, int amount) {
        // TODO Stub
        return false;
    }

    @Override
    public boolean containsMana(@Nullable ItemStack stack, @Nullable Player player, @Nullable Source source, int amount, HolderLookup.Provider registries) {
        // TODO Stub
        return false;
    }
}
