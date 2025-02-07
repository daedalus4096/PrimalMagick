package com.verdantartifice.primalmagick.common.capabilities;

import com.verdantartifice.primalmagick.common.tiles.base.AbstractTilePM;
import net.minecraft.Util;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Extension of the default IItemHandler implementation which updates its owning tile's client
 * side when the contents of the capability change.
 * 
 * @author Daedalus4096
 */
public class ItemStackHandlerPMForge extends ItemStackHandler implements IItemHandlerPM {
    protected final AbstractTilePM tile;
    protected final Optional<Function<Integer, Integer>> limitFuncOverride;
    protected final Optional<BiPredicate<Integer, ItemStack>> validityFuncOverride;
    protected final Optional<Consumer<Integer>> contentsChangedFuncOverride;

    public ItemStackHandlerPMForge(AbstractTilePM tile) {
        super();
        this.tile = tile;
        this.limitFuncOverride = Optional.empty();
        this.validityFuncOverride = Optional.empty();
        this.contentsChangedFuncOverride = Optional.empty();
    }
    
    public ItemStackHandlerPMForge(int size, AbstractTilePM tile) {
        super(size);
        this.tile = tile;
        this.limitFuncOverride = Optional.empty();
        this.validityFuncOverride = Optional.empty();
        this.contentsChangedFuncOverride = Optional.empty();
    }
    
    public ItemStackHandlerPMForge(NonNullList<ItemStack> stacks, AbstractTilePM tile) {
        super(stacks);
        this.tile = tile;
        this.limitFuncOverride = Optional.empty();
        this.validityFuncOverride = Optional.empty();
        this.contentsChangedFuncOverride = Optional.empty();
    }

    public ItemStackHandlerPMForge(IItemHandler original, @Nullable AbstractTilePM tile) {
        super(Util.make(NonNullList.createWithCapacity(original.getSlots()), newList -> {
            for (int i = 0; i < original.getSlots(); i++) {
                newList.add(original.getStackInSlot(i));
            }
        }));
        this.tile = tile;
        this.limitFuncOverride = Optional.empty();
        this.validityFuncOverride = Optional.empty();
        this.contentsChangedFuncOverride = Optional.empty();
    }

    protected ItemStackHandlerPMForge(NonNullList<ItemStack> stacks, AbstractTilePM tile, Optional<Function<Integer, Integer>> limit,
                                      Optional<BiPredicate<Integer, ItemStack>> validity, Optional<Consumer<Integer>> contentsChanged) {
        super(stacks);
        this.tile = tile;
        this.limitFuncOverride = limit;
        this.validityFuncOverride = validity;
        this.contentsChangedFuncOverride = contentsChanged;
    }

    @Override
    public int getSlotLimit(int slot) {
        return this.limitFuncOverride.map(f -> f.apply(slot)).orElseGet(() -> super.getSlotLimit(slot));
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return this.validityFuncOverride.map(f -> f.test(slot, stack)).orElseGet(() -> super.isItemValid(slot, stack));
    }

    @Override
    public Container asContainer() {
        return new RecipeWrapper(this);
    }

    @Override
    protected void onContentsChanged(int slot) {
        super.onContentsChanged(slot);
        if (this.tile != null) {
            this.tile.syncTile(true);
            this.tile.setChanged();
        }
        this.contentsChangedFuncOverride.ifPresent(c -> c.accept(slot));
    }

    public static Builder builder(NonNullList<ItemStack> stacks, AbstractTilePM tile) {
        return new Builder(stacks, tile);
    }

    public static class Builder implements IItemHandlerPM.Builder {
        private final NonNullList<ItemStack> stacks;
        private final AbstractTilePM tile;
        private Optional<Function<Integer, Integer>> limitFuncOverride = Optional.empty();
        private Optional<BiPredicate<Integer, ItemStack>> validityFuncOverride = Optional.empty();
        private Optional<Consumer<Integer>> contentsChangedFuncOverride = Optional.empty();

        public Builder(NonNullList<ItemStack> stacks, AbstractTilePM tile) {
            this.stacks = stacks;
            this.tile = tile;
        }

        @Override
        public IItemHandlerPM.Builder slotLimitFunction(Function<Integer, Integer> limitFunction) {
            this.limitFuncOverride = Optional.of(limitFunction);
            return this;
        }

        @Override
        public IItemHandlerPM.Builder itemValidFunction(BiPredicate<Integer, ItemStack> itemValidFunction) {
            this.validityFuncOverride = Optional.of(itemValidFunction);
            return this;
        }

        @Override
        public IItemHandlerPM.Builder contentsChangedFunction(Consumer<Integer> contentsChangedFunction) {
            this.contentsChangedFuncOverride = Optional.of(contentsChangedFunction);
            return this;
        }

        @Override
        public IItemHandlerPM build() {
            return new ItemStackHandlerPMForge(this.stacks, this.tile, this.limitFuncOverride,
                    this.validityFuncOverride, this.contentsChangedFuncOverride);
        }
    }
}
