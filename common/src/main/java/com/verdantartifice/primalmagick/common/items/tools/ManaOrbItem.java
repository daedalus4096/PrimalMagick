package com.verdantartifice.primalmagick.common.items.tools;

import com.verdantartifice.primalmagick.client.renderers.itemstack.ManaOrbISTER;
import com.verdantartifice.primalmagick.common.capabilities.ManaStorage;
import com.verdantartifice.primalmagick.common.components.DataComponentsPM;
import com.verdantartifice.primalmagick.common.items.IHasCustomRenderer;
import com.verdantartifice.primalmagick.common.misc.DeviceTier;
import com.verdantartifice.primalmagick.common.misc.ITieredDevice;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.Sources;
import com.verdantartifice.primalmagick.common.wands.IManaContainer;
import com.verdantartifice.primalmagick.common.wands.WandGem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
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
    public boolean isEnchantable(@NotNull ItemStack pStack) {
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

    protected boolean isAttuned(ItemStack stack) {
        return !this.getManaStorage(stack).equals(ManaStorage.EMPTY);
    }

    protected boolean isAttuned(ItemStack stack, Source source) {
        return this.getManaStorage(stack).canStore(source);
    }

    public void attuneStorage(@NotNull ItemStack stack, @NotNull Source source) {
        if (!this.getManaStorage(stack).canStore(source)) {
            // If attuning to a source other than the one currently used, replace the stack's mana storage component
            // with one for the given source; any stored mana of other sources is lost.
            stack.set(DataComponentsPM.CAPABILITY_MANA_STORAGE.get(), ManaStorage.emptyManaOrb(source, this.getWandGemEquivalent().getCapacity()));
        }
    }

    @Override
    public int getMaxMana(@Nullable ItemStack stack, @Nullable Source source) {
        if (stack == null || source == null) {
            return 0;
        }
        return this.getManaStorage(stack).getMaxManaStored(source);
    }

    @Override
    public void setMana(@NotNull ItemStack stack, @NotNull Source source, int amount) {
        stack.update(DataComponentsPM.CAPABILITY_MANA_STORAGE.get(), ManaStorage.EMPTY, mana -> mana.copyWith(source, amount));
        stack.set(DataComponentsPM.LAST_UPDATED.get(), System.currentTimeMillis());   // FIXME Is there a better way of marking this stack as dirty?
    }

    @Override
    public int getBaseCostModifier(@Nullable ItemStack stack) {
        // As mana orbs don't have wand cores, they don't provide a wand core discount to anything
        return 0;
    }

    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);

        if (this.isAttuned(pStack)) {
            // Add detailed mana information
            for (Source source : Sources.getAllSorted()) {
                // Only include a mana source in the listing if this orb is attuned to it
                if (this.isAttuned(pStack, source)) {
                    Component nameComp = source.getNameText();
                    Component line = Component.translatable("tooltip.primalmagick.source.mana_gauge", nameComp, this.getManaText(pStack, source), this.getMaxManaText(pStack, source));
                    pTooltipComponents.add(line);
                }
            }
        } else {
            pTooltipComponents.add(Component.translatable("tooltip.primalmagick.mana_orb.unattuned").withStyle(ChatFormatting.GRAY));
        }
    }
}
