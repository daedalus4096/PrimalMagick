package com.verdantartifice.primalmagick.common.menus.slots;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.Predicate;
import java.util.stream.Stream;

import com.mojang.datafixers.util.Pair;

import net.minecraft.network.chat.Component;
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
public class FilteredSlot extends SlotItemHandler implements IHasTooltip, IHasCyclingBackgrounds {
    private static final int BACKGROUND_CHANGE_TICK_RATE = 30;

    private final List<Pair<Predicate<FilteredSlot>, ResourceLocation>> backgrounds;
    private final Optional<Predicate<ItemStack>> filter;
    private final Optional<Component> tooltip;
    private final OptionalInt maxStackSize;
    private int ticks = 0;

    public FilteredSlot(IItemHandler pItemHandler, int pSlot, int pX, int pY, FilteredSlot.Properties properties) {
        super(pItemHandler, pSlot, pX, pY);
        this.filter = properties.filter;
        this.tooltip = properties.tooltip;
        this.maxStackSize = properties.maxStackSize;
        this.backgrounds = properties.backgrounds;
        
        // Set the default background to the first active one, if any
        this.getActiveBackgrounds().stream().findFirst().ifPresent(loc -> this.setBackground(InventoryMenu.BLOCK_ATLAS, loc));
    }

    @Override
    public boolean mayPlace(ItemStack pStack) {
        return this.filter.orElse(super::mayPlace).test(pStack);
    }

    @Override
    public int getMaxStackSize() {
        return this.maxStackSize.orElseGet(super::getMaxStackSize);
    }

    @Override
    public boolean shouldShowTooltip() {
        return this.tooltip.isPresent() && !this.hasItem();
    }

    @Override
    public Component getTooltip() {
        return this.tooltip.orElse(null);
    }

    @Override
    public void tickBackgrounds() {
        List<ResourceLocation> active = this.getActiveBackgrounds();
        if (!active.isEmpty()) {
            int backgroundIndex = (this.ticks++ / BACKGROUND_CHANGE_TICK_RATE) % active.size();
            this.setBackground(InventoryMenu.BLOCK_ATLAS, active.get(backgroundIndex));
        }
    }
    
    protected List<ResourceLocation> getActiveBackgrounds() {
        return this.backgrounds.stream().filter(p -> p.getFirst().test(this)).map(Pair::getSecond).toList();
    }

    public static class Properties {
        private final List<Pair<Predicate<FilteredSlot>, ResourceLocation>> backgrounds = new ArrayList<>();
        private Optional<Predicate<ItemStack>> filter = Optional.empty();
        private Optional<Component> tooltip = Optional.empty();
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
            return this.background(loc, $ -> true);
        }
        
        public FilteredSlot.Properties background(ResourceLocation loc, Predicate<FilteredSlot> predicate) {
            this.backgrounds.add(Pair.of(predicate, loc));
            return this;
        }
        
        /**
         * Specify a tooltip to show when the slot is hovered over while empty
         * 
         * @param tooltip the tooltip text to be displayed
         * @return the modified properties object
         */
        public FilteredSlot.Properties tooltip(Component tooltip) {
            this.tooltip = Optional.ofNullable(tooltip);
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
