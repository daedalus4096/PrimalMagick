package com.verdantartifice.primalmagick.common.menus.slots;

import com.mojang.datafixers.util.Pair;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class FilteredSlotProperties {
    private final List<Pair<Predicate<Slot>, ResourceLocation>> backgrounds = new ArrayList<>();
    private Optional<Predicate<ItemStack>> filter = Optional.empty();
    private Optional<Component> tooltip = Optional.empty();
    private Optional<Integer> maxStackSize = Optional.empty();

    List<Pair<Predicate<Slot>, ResourceLocation>> getBackgrounds() {
        return this.backgrounds;
    }

    Optional<Predicate<ItemStack>> getFilter() {
        return this.filter;
    }

    Optional<Component> getTooltip() {
        return this.tooltip;
    }

    Optional<Integer> getMaxStackSize() {
        return this.maxStackSize;
    }

    /**
     * Specify an arbitrary predicate filter for the slot; item stacks will be allowed if they pass the given predicate
     *
     * @param filter the predicate to filter by
     * @return the modified properties object
     */
    public FilteredSlotProperties filter(Predicate<ItemStack> filter) {
        this.filter = Optional.ofNullable(filter);
        return this;
    }

    /**
     * Specify an item filter for the slot; item stacks will be allowed if they match the given item
     *
     * @param item the item to filter by
     * @return the modified properties object
     */
    public FilteredSlotProperties item(Item item) {
        Objects.requireNonNull(item);
        return this.filter(stack -> stack.is(item));
    }

    /**
     * Specify an item filter for the slot; item stacks will be allowed if they match any of the given items
     *
     * @param items the items to filter by
     * @return the modified properties object
     */
    public FilteredSlotProperties item(Item... items) {
        Objects.requireNonNull(items);
        return this.filter(stack -> Stream.of(items).anyMatch(stack::is));
    }

    /**
     * Specify a tag filter for the slot; item stacks will be allowed if they are members of the tag
     *
     * @param tagKey the tag key to filter by
     * @return the modified properties object
     */
    public FilteredSlotProperties tag(TagKey<Item> tagKey) {
        Objects.requireNonNull(tagKey);
        return this.filter(stack -> stack.is(tagKey));
    }

    /**
     * Specify a type filter for the slot; item stacks will be allowed if they are an instance of the given class
     *
     * @param clazz the class to filter by
     * @return the modified properties object
     */
    public FilteredSlotProperties typeOf(Class<?> clazz) {
        Objects.requireNonNull(clazz);
        return this.filter(stack -> clazz.isInstance(stack.getItem()));
    }

    /**
     * Specify a type filter for the slot; item stacks will be allowed if they are an instance of any of the given classes
     *
     * @param clazzes the classes to filter by
     * @return the modified properties object
     */
    public FilteredSlotProperties typeOf(Class<?>... clazzes) {
        Objects.requireNonNull(clazzes);
        return this.filter(stack -> Stream.of(clazzes).anyMatch(clazz -> clazz.isInstance(stack.getItem())));
    }

    /**
     * Specify a background image for the slot from the blocks atlas
     *
     * @param loc the location of the sprite to use
     * @return the modified properties object
     */
    public FilteredSlotProperties background(ResourceLocation loc) {
        return this.background(loc, $ -> true);
    }

    public FilteredSlotProperties background(ResourceLocation loc, Predicate<Slot> predicate) {
        this.backgrounds.add(Pair.of(predicate, loc));
        return this;
    }

    /**
     * Specify a tooltip to show when the slot is hovered over while empty
     *
     * @param tooltip the tooltip text to be displayed
     * @return the modified properties object
     */
    public FilteredSlotProperties tooltip(Component tooltip) {
        this.tooltip = Optional.ofNullable(tooltip);
        return this;
    }

    /**
     * Specify a max stack size for the slot; defaults to its container's max stack size
     *
     * @param size the max stack size for the slot
     * @return the modified properties object
     */
    public FilteredSlotProperties stacksTo(int size) {
        this.maxStackSize = Optional.of(size);
        return this;
    }
}
