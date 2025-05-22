package com.verdantartifice.primalmagick.common.items.tools;

import com.google.common.collect.ImmutableList;
import com.verdantartifice.primalmagick.client.renderers.itemstack.SpelltomeISTER;
import com.verdantartifice.primalmagick.client.util.ClientUtils;
import com.verdantartifice.primalmagick.common.components.DataComponentsPM;
import com.verdantartifice.primalmagick.common.items.IHasCustomRenderer;
import com.verdantartifice.primalmagick.common.misc.DeviceTier;
import com.verdantartifice.primalmagick.common.misc.ITieredDevice;
import com.verdantartifice.primalmagick.common.sounds.SoundsPM;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.spells.SpellManager;
import com.verdantartifice.primalmagick.common.spells.SpellPackage;
import com.verdantartifice.primalmagick.common.wands.ISpellContainer;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public abstract class SpelltomeItem extends Item implements Equipable, IHasCustomRenderer, ITieredDevice, ISpellContainer {
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

    protected int getSpellCapacity() {
        return switch (this.getDeviceTier()) {
            case BASIC -> 1;
            case ENCHANTED -> 2;
            case FORBIDDEN -> 3;
            case HEAVENLY, CREATIVE -> 4;
        };
    }

    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);

        Player player = Services.PLATFORM.isClientDist() ? ClientUtils.getCurrentPlayer() : null;
        boolean showDetails = Services.PLATFORM.isClientDist() && ClientUtils.hasShiftDown();
        if (showDetails) {
            // Add inscribed spell listing
            SpellManager.appendSpellListingText(player, pStack, pContext, pTooltipComponents);
        } else {
            // Add active spell
            SpellManager.appendActiveSpellText(pStack, pTooltipComponents);

            // Add more info tooltip
            pTooltipComponents.add(Component.translatable("tooltip.primalmagick.more_info").withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));
        }
    }

    @Override
    public Component getSpellCapacityText(@Nullable ItemStack stack) {
        if (stack == null) {
            return Component.translatable("tooltip.primalmagick.spells.capacity", 0);
        } else {
            return Component.translatable("tooltip.primalmagick.spells.capacity", this.getSpellCapacity());
        }
    }

    @Override
    public boolean canAddSpell(@Nullable ItemStack stack, @Nullable SpellPackage spell) {
        if (stack == null || spell == null || spell.payload() == null) {
            return false;
        }

        // Determine the payload sources of all spells to be included in the given tome
        List<Source> spellSources = this.getSpells(stack).stream()
                .filter(p -> (p != null && p.payload() != null))
                .map(p -> p.payload().getComponent().getSource())
                .collect(Collectors.toCollection(ArrayList::new));
        spellSources.add(spell.payload().getComponent().getSource());

        int slots = this.getSpellCapacity();
        return spellSources.size() < slots + 1;
    }
}
