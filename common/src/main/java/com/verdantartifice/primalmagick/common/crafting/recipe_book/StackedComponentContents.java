package com.verdantartifice.primalmagick.common.crafting.recipe_book;

import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * An extension of StackedContents that also tracks the component data of each of the stacks
 * for each item type.
 * 
 * @author Daedalus4096
 */
public class StackedComponentContents extends StackedItemContents {
    protected final Reference2ObjectOpenHashMap<Holder<Item>, List<DataComponentMap>> componentData = new Reference2ObjectOpenHashMap<>();

    @Override
    public void accountStack(@NotNull ItemStack stack, int maxCount) {
        super.accountStack(stack, maxCount);
        if (!stack.isEmpty() && !stack.getComponents().isEmpty()) {
            this.componentData.computeIfAbsent(stack.getItemHolder(), key -> new ArrayList<>()).add(stack.getComponents());
        }
    }

    @Override
    public void clear() {
        super.clear();
        this.componentData.clear();
    }
    
    @Nullable
    public List<DataComponentMap> getComponentData(Holder<Item> itemHolder) {
        return this.componentData.get(itemHolder);
    }
    
    public boolean hasComponentData(Holder<Item> itemHolder) {
        return !this.componentData.getOrDefault(itemHolder, List.of()).isEmpty();
    }
}
