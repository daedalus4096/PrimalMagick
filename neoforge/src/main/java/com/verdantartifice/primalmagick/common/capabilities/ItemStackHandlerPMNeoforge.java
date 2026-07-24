package com.verdantartifice.primalmagick.common.capabilities;

import com.verdantartifice.primalmagick.common.tiles.base.AbstractTilePM;
import com.verdantartifice.primalmagick.common.util.RecipeContainerWrapper;
import net.minecraft.core.NonNullList;
import net.minecraft.util.Util;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemUtil;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jetbrains.annotations.NotNull;

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
public class ItemStackHandlerPMNeoforge extends ItemStacksResourceHandler implements IItemHandlerPM {
    protected final AbstractTilePM tile;
    protected final Optional<Function<Integer, Integer>> limitFuncOverride;
    protected final Optional<BiPredicate<Integer, ItemStack>> validityFuncOverride;
    protected final Optional<Consumer<Integer>> contentsChangedFuncOverride;

    public ItemStackHandlerPMNeoforge(int size, AbstractTilePM tile) {
        super(size);
        this.tile = tile;
        this.limitFuncOverride = Optional.empty();
        this.validityFuncOverride = Optional.empty();
        this.contentsChangedFuncOverride = Optional.empty();
    }

    public ItemStackHandlerPMNeoforge(NonNullList<ItemStack> stacks, AbstractTilePM tile) {
        super(stacks);
        this.tile = tile;
        this.limitFuncOverride = Optional.empty();
        this.validityFuncOverride = Optional.empty();
        this.contentsChangedFuncOverride = Optional.empty();
    }

    public ItemStackHandlerPMNeoforge(ResourceHandler<ItemResource> original, @Nullable AbstractTilePM tile) {
        super(Util.make(NonNullList.createWithCapacity(original.size()), newList -> {
            for (int i = 0; i < original.size(); i++) {
                newList.add(ItemUtil.getStack(original, i));
            }
        }));
        this.tile = tile;
        this.limitFuncOverride = Optional.empty();
        this.validityFuncOverride = Optional.empty();
        this.contentsChangedFuncOverride = Optional.empty();
    }

    protected ItemStackHandlerPMNeoforge(NonNullList<ItemStack> stacks, AbstractTilePM tile, Optional<Function<Integer, Integer>> limit,
                                         Optional<BiPredicate<Integer, ItemStack>> validity, Optional<Consumer<Integer>> contentsChanged) {
        super(stacks);
        this.tile = tile;
        this.limitFuncOverride = limit;
        this.validityFuncOverride = validity;
        this.contentsChangedFuncOverride = contentsChanged;
    }

    @Override
    public int getSlots() {
        return this.size();
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return ItemUtil.getStack(this, slot);
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        return ItemUtil.insertItemReturnRemaining(this, slot, stack, simulate, null);
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        ItemStack retVal;
        try (Transaction tx = Transaction.openRoot()) {
            ItemResource resource = this.getResource(slot);
            int extracted = this.extract(slot, resource, amount, tx);
            retVal = resource.toStack(extracted);
            if (!simulate) {
                tx.commit();
            }
        }
        return retVal;
    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack) {
        // FIXME Remove from interface, and refactor all callers
    }

    @Override
    public int getSlotLimit(int slot) {
        return this.limitFuncOverride.map(f -> f.apply(slot)).orElseGet(() -> super.getCapacity(slot, this.getResource(slot)));
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return this.validityFuncOverride.map(f -> f.test(slot, stack)).orElseGet(() -> super.isValid(slot, ItemResource.of(stack)));
    }

    @Override
    public Container asContainer() {
        return new RecipeContainerWrapper(this);
    }

    @Override
    protected void onContentsChanged(int slot, @NotNull ItemStack previousContents) {
        super.onContentsChanged(slot, previousContents);
        if (this.tile != null) {
            this.tile.syncTile(true);
            this.tile.setChanged();
        }
        // TODO Refactor consumers to accept previous contents
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
            return new ItemStackHandlerPMNeoforge(this.stacks, this.tile, this.limitFuncOverride,
                    this.validityFuncOverride, this.contentsChangedFuncOverride);
        }
    }
}
