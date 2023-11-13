package com.verdantartifice.primalmagick.common.menus.slots;

import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.Predicate;
import java.util.stream.Stream;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

/**
 * Definition of a GUI slot whose accepted contents are filtered in some way.  Can be customized
 * via constructor properties.
 * 
 * @author Daedalus4096
 */
public class FilteredSlot extends SlotItemHandler {
    private final Optional<Predicate<ItemStack>> filter;
    private final OptionalInt maxStackSize;

    public FilteredSlot(IItemHandler pItemHandler, int pSlot, int pX, int pY, FilteredSlot.Properties properties) {
        super(pItemHandler, pSlot, pX, pY);
        this.filter = properties.filter;
        this.maxStackSize = properties.maxStackSize;
        properties.background.ifPresent(bg -> this.setBackground(InventoryMenu.BLOCK_ATLAS, bg));
    }

    @Override
    public boolean mayPlace(ItemStack pStack) {
        return this.filter.orElse(super::mayPlace).test(pStack);
    }

    @Override
    public int getMaxStackSize() {
        return this.maxStackSize.orElseGet(super::getMaxStackSize);
    }

    public static class Properties {
        private Optional<Predicate<ItemStack>> filter = Optional.empty();
        private Optional<ResourceLocation> background = Optional.empty();
        private OptionalInt maxStackSize = OptionalInt.empty();
        
        /**
         * Specify an arbitrary predicate filter for the slot; item stacks will be allowed if they pass the given predicate
         * 
         * @param filter the predicate to filter by
         * @return the modified properties object
         */
        public FilteredSlot.Properties filter(Predicate<ItemStack> filter) {
            this.filter = Optional.ofNullable(filter);
            return this;
        }
        
        /**
         * Specify an item filter for the slot; item stacks will be allowed if they match the given item
         * 
         * @param item the item to filter by
         * @return the modified properties object
         */
        public FilteredSlot.Properties item(Item item) {
            Objects.requireNonNull(item);
            return this.filter(stack -> stack.is(item));
        }
        
        /**
         * Specify an item filter for the slot; item stacks will be allowed if they match any of the given items
         * 
         * @param items the items to filter by
         * @return the modified properties object
         */
        public FilteredSlot.Properties item(Item... items) {
            Objects.requireNonNull(items);
            return this.filter(stack -> Stream.of(items).anyMatch(item -> stack.is(item)));
        }
        
        /**
         * Specify a tag filter for the slot; item stacks will be allowed if they are members of the tag
         * 
         * @param tagKey the tag key to filter by
         * @return the modified properties object
         */
        public FilteredSlot.Properties tag(TagKey<Item> tagKey) {
            Objects.requireNonNull(tagKey);
            return this.filter(stack -> stack.is(tagKey));
        }
        
        /**
         * Specify a type filter for the slot; item stacks will be allowed if they are an instance of the given class
         * 
         * @param clazz the class to filter by
         * @return the modified properties object
         */
        public FilteredSlot.Properties typeOf(Class<?> clazz) {
            Objects.requireNonNull(clazz);
            return this.filter(stack -> clazz.isInstance(stack.getItem()));
        }
        
        /**
         * Specify a type filter for the slot; item stacks will be allowed if they are an instance of any of the given classes
         * 
         * @param clazzes the classes to filter by
         * @return the modified properties object
         */
        public FilteredSlot.Properties typeOf(Class<?>... clazzes) {
            Objects.requireNonNull(clazzes);
            return this.filter(stack -> Stream.of(clazzes).anyMatch(clazz -> clazz.isInstance(stack.getItem())));
        }
        
        /**
         * Specify a background image for the slot from the blocks atlas
         * 
         * @param loc the location of the sprite to use
         * @return the modified properties object
         */
        public FilteredSlot.Properties background(ResourceLocation loc) {
            this.background = Optional.ofNullable(loc);
            return this;
        }
        
        /**
         * Specify a max stack size for the slot; defaults to its container's max stack size
         * 
         * @param size the max stack size for the slot
         * @return the modified properties object
         */
        public FilteredSlot.Properties stacksTo(int size) {
            this.maxStackSize = OptionalInt.of(size);
            return this;
        }
    }
}
