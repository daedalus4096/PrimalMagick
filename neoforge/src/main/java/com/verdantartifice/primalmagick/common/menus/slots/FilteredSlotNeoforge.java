package com.verdantartifice.primalmagick.common.menus.slots;

import com.mojang.datafixers.util.Pair;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Definition of a GUI slot whose accepted contents are filtered in some way.  Can be customized
 * via constructor properties.
 * 
 * @author Daedalus4096
 */
public class FilteredSlotNeoforge extends SlotItemHandler implements IHasTooltip, IHasCyclingBackgrounds {
    private static final int BACKGROUND_CHANGE_TICK_RATE = 30;

    private final List<Pair<Predicate<Slot>, ResourceLocation>> backgrounds;
    private final Optional<Predicate<ItemStack>> filter;
    private final Optional<Component> tooltip;
    private final Optional<Integer> maxStackSize;
    private int ticks = 0;

    public FilteredSlotNeoforge(IItemHandler pItemHandler, int pSlot, int pX, int pY, FilteredSlotProperties properties) {
        super(pItemHandler, pSlot, pX, pY);
        this.filter = properties.getFilter();
        this.tooltip = properties.getTooltip();
        this.maxStackSize = properties.getMaxStackSize();
        this.backgrounds = properties.getBackgrounds();
        
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
}
