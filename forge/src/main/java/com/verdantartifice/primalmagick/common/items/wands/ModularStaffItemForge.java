package com.verdantartifice.primalmagick.common.items.wands;

import com.verdantartifice.primalmagick.common.items.IHasCustomRendererForge;
import com.verdantartifice.primalmagick.common.wands.IWandComponent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;

public class ModularStaffItemForge extends ModularStaffItem implements IHasCustomRendererForge {
    private IClientItemExtensions renderProps;

    public ModularStaffItemForge(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        return this.onWandUseFirst(stack, context);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

    @Override
    public boolean canContinueUsing(ItemStack oldStack, ItemStack newStack) {
        // Don't break wand interaction just because the stack NBT changes
        return true;
    }

    @Override
    public int getEnchantmentValue(ItemStack stack) {
        // The enchantability of a staff is determined by its components
        return this.getComponents(stack).stream().mapToInt(IWandComponent::getEnchantability).sum();
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(this.getRenderProperties());
    }

    public IClientItemExtensions getRenderProperties() {
        if (this.renderProps == null) {
            this.renderProps = this.getRenderPropertiesUncached();
        }
        return this.renderProps;
    }
}
