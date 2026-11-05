package com.verdantartifice.primalmagick.common.util;

import com.verdantartifice.primalmagick.common.capabilities.IItemHandlerPM;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemUtil;
import net.neoforged.neoforge.transfer.item.VanillaContainerWrapper;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

import java.util.List;

public class InvWrapperPMNeoforge implements IItemHandlerPM {
    private final ResourceHandler<ItemResource> inner;
    private final Container inv;

    public InvWrapperPMNeoforge(Container container) {
        this.inner = VanillaContainerWrapper.of(container);
        this.inv = container;
    }

    @Override
    public int getSlots() {
        return this.inner.size();
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return ItemUtil.getStack(this.inner, slot);
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        return ItemUtil.insertItemReturnRemaining(this.inner, slot, stack, simulate, null);
    }

    @Override
    public ItemStack insertItem(ItemStack stack, boolean simulate) {
        return ItemUtil.insertItemReturnRemaining(this.inner, stack, simulate, null);
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        ItemStack retVal;
        try (Transaction tx = Transaction.openRoot()) {
            ItemResource resource = this.inner.getResource(slot);
            retVal = resource.toStack(this.inner.extract(slot, resource, amount, tx));
            if (!simulate) {
                tx.commit();
            }
        }
        return retVal;
    }

    @Override
    public ItemStack extractItem(ItemStack stack, boolean simulate) {
        ItemStack retVal;
        try (Transaction tx = Transaction.openRoot()) {
            ItemResource resource = ItemResource.of(stack);
            retVal = resource.toStack(this.inner.extract(resource, stack.count(), tx));
            if (!simulate) {
                tx.commit();
            }
        }
        return retVal;
    }

    @Override
    public boolean transactSlots(boolean simulate, List<SlotOperation> slotOperations) {
        boolean success;
        try (Transaction tx = Transaction.openRoot()) {
            success = slotOperations.stream().allMatch(op -> ItemStack.matches(op.stack(), this.performSlotTransactionOperation(simulate, op, tx)));
            if (success && !simulate) {
                tx.commit();
            }
        }
        return success;
    }

    protected ItemStack performSlotTransactionOperation(boolean simulate, SlotOperation slotOperation, TransactionContext parent) {
        try (Transaction childTx = Transaction.open(parent)) {
            ItemResource opResource = ItemResource.of(slotOperation.stack());
            ItemStack retVal = switch (slotOperation.type()) {
                case EXTRACT -> this.inner.getResource(slotOperation.slot()).toStack(this.inner.extract(slotOperation.slot(), opResource, slotOperation.stack().count(), childTx));
                case INSERT -> opResource.toStack(this.inner.insert(slotOperation.slot(), opResource, slotOperation.stack().count(), childTx));
            };
            if (!simulate) {
                childTx.commit();
            }
            return retVal;
        }
    }

    @Override
    public boolean transact(boolean simulate, List<HandlerOperation> handlerOperations) {
        boolean success;
        try (Transaction tx = Transaction.openRoot()) {
            success = handlerOperations.stream().allMatch(op -> ItemStack.matches(op.stack(), this.performHandlerTransactionOperations(simulate, op, tx)));
            if (success && !simulate) {
                tx.commit();
            }
        }
        return success;
    }

    protected ItemStack performHandlerTransactionOperations(boolean simulate, HandlerOperation handlerOperation, TransactionContext parent) {
        try (Transaction childTx = Transaction.open(parent)) {
            ItemResource opResource = ItemResource.of(handlerOperation.stack());
            ItemStack retVal = switch (handlerOperation.type()) {
                case EXTRACT -> opResource.toStack(this.inner.extract(opResource, handlerOperation.stack().count(), childTx));
                case INSERT -> opResource.toStack(this.inner.insert(opResource, handlerOperation.stack().count(), childTx));
            };
            if (!simulate) {
                childTx.commit();
            }
            return retVal;
        }
    }

    @Override
    public int getSlotLimit(int slot) {
        return this.inner.getCapacityAsInt(slot, this.inner.getResource(slot));
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return this.inner.isValid(slot, ItemResource.of(stack));
    }

    @Override
    public Container asContainer() {
        return this.inv;
    }
}
