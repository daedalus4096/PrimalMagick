package com.verdantartifice.primalmagick.common.menus.slots;

import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.Predicate;
import java.util.stream.Stream;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * Definition of a GUI slot whose accepted contents are filtered in some way.  Can be customized
 * via constructor properties.
 * 
 * @author Daedalus4096
 */
public class FilteredSlot extends Slot {
    private final Optional<Predicate<ItemStack>> filter;
    private final OptionalInt maxStackSize;

    public FilteredSlot(Container pContainer, int pSlot, int pX, int pY, FilteredSlot.Properties properties) {
        super(pContainer, pSlot, pX, pY);
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
        
        public FilteredSlot.Properties filter(Predicate<ItemStack> filter) {
            this.filter = Optional.ofNullable(filter);
            return this;
        }
        
        public FilteredSlot.Properties item(Item item) {
            Objects.requireNonNull(item);
            return this.filter(stack -> stack.is(item));
        }
        
        public FilteredSlot.Properties item(Item... items) {
            Objects.requireNonNull(items);
            return this.filter(stack -> Stream.of(items).anyMatch(item -> stack.is(item)));
        }
        
        public FilteredSlot.Properties tag(TagKey<Item> tagKey) {
            Objects.requireNonNull(tagKey);
            return this.filter(stack -> stack.is(tagKey));
        }
        
        public FilteredSlot.Properties typeOf(Class<?> clazz) {
            Objects.requireNonNull(clazz);
            return this.filter(stack -> clazz.isInstance(stack.getItem()));
        }
        
        public FilteredSlot.Properties typeOf(Class<?>... clazzes) {
            Objects.requireNonNull(clazzes);
            return this.filter(stack -> Stream.of(clazzes).anyMatch(clazz -> clazz.isInstance(stack.getItem())));
        }
        
        public FilteredSlot.Properties background(ResourceLocation loc) {
            this.background = Optional.ofNullable(loc);
            return this;
        }
        
        public FilteredSlot.Properties stacksTo(int size) {
            this.maxStackSize = OptionalInt.of(size);
            return this;
        }
    }
}
