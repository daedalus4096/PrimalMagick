package com.verdantartifice.primalmagick.common.items.misc;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import org.jetbrains.annotations.Nullable;

/**
 * Item definition for an earthshatter hammer.  Can be crafted with ore to break it into grit for
 * ore doubling.  The hammer is returned to the crafting grid unchanged, keeping all of its components.
 */
public class EarthshatterHammerItemNeoforge extends Item {
    public EarthshatterHammerItemNeoforge(Item.Properties properties) {
        super(properties);
    }

    @Override
    public @Nullable ItemStackTemplate getCraftingRemainder(ItemInstance instance) {
        return switch (instance) {
            case ItemStack stack -> ItemStackTemplate.fromNonEmptyStack(stack.copyWithCount(1));
            case ItemStackTemplate template -> template.withCount(1);
            default -> new ItemStackTemplate(this);
        };
    }
}
