package com.verdantartifice.primalmagick.common.menus;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagick.common.menus.slots.PaperSlot;
import com.verdantartifice.primalmagick.common.menus.slots.WritingImplementSlot;
import com.verdantartifice.primalmagick.common.theorycrafting.IWritingImplement;
import com.verdantartifice.primalmagick.common.theorycrafting.TheorycraftManager;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerListener;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

/**
 * Server data container for the research table GUI.
 * 
 * @author Daedalus4096
 */
public class ResearchTableContainer extends AbstractContainerMenu implements ContainerListener {
    protected final ContainerLevelAccess worldPosCallable;
    protected final Player player;
    protected final Container writingInv;
    protected final Slot paperSlot;
    protected final Slot pencilSlot;
    protected final DataSlot writingReady = DataSlot.standalone();

    public ResearchTableContainer(int windowId, Inventory inv, BlockPos pos) {
        this(windowId, inv, new SimpleContainer(2), ContainerLevelAccess.create(inv.player.level(), pos));
    }

    public ResearchTableContainer(int windowId, Inventory inv, Container tableInv, ContainerLevelAccess callable) {
        super(ContainersPM.RESEARCH_TABLE.get(), windowId);
        this.worldPosCallable = callable;
        this.player = inv.player;
        checkContainerSize(tableInv, 2);
        this.writingInv = tableInv;
        
        // Slot 0: Pencil
        this.pencilSlot = this.addSlot(new WritingImplementSlot(this.writingInv, 0, 8, 8));
        
        // Slot 1: Paper
        this.paperSlot = this.addSlot(new PaperSlot(this.writingInv, 1, 206, 8));
        
        // Slots 2-28: Player backpack
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(inv, j + (i * 9) + 9, 35 + (j * 18), 140 + (i * 18)));
            }
        }
        
        // Slots 29-37: Player hotbar
        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(inv, i, 35 + (i * 18), 198));
        }
        
        this.addDataSlot(this.writingReady).set(0);
        this.checkWritingImplements();
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return this.writingInv.stillValid(playerIn);
    }
    
    protected void checkWritingImplements() {
        // Set whether the container has writing tools ready; 1 for yes, 0 for no
        boolean ready = (!this.getWritingImplementStack().isEmpty() && !this.getPaperStack().isEmpty());
        this.writingReady.set(ready ? 1 : 0);
    }

    @Override
    public void containerChanged(Container invBasic) {
        this.checkWritingImplements();
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            stack = slotStack.copy();
            if (index >= 2 && index < 29) {
                // If transferring from the backpack, move paper and writing implements to the appropriate slots, and everything else to the hotbar
                if (this.pencilSlot.mayPlace(slotStack)) {
                    if (!this.moveItemStackTo(slotStack, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.paperSlot.mayPlace(slotStack)) {
                    if (!this.moveItemStackTo(slotStack, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    if (!this.moveItemStackTo(slotStack, 29, 38, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (index >= 29 && index < 38) {
                // If transferring from the hotbar, move paper and writing implements to the appropriate slots, and everything else to the backpack
                if (this.pencilSlot.mayPlace(slotStack)) {
                    if (!this.moveItemStackTo(slotStack, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.paperSlot.mayPlace(slotStack)) {
                    if (!this.moveItemStackTo(slotStack, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    if (!this.moveItemStackTo(slotStack, 2, 29, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (!this.moveItemStackTo(slotStack, 2, 38, false)) {
                // Move all other transfers to the backpack or hotbar
                return ItemStack.EMPTY;
            }
            
            if (slotStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
            
            if (slotStack.getCount() == stack.getCount()) {
                return ItemStack.EMPTY;
            }
            
            slot.onTake(playerIn, slotStack);
        }
        return stack;
    }
    
    @Nonnull
    protected ItemStack getWritingImplementStack() {
        return this.writingInv.getItem(0);
    }
    
    @Nonnull
    protected ItemStack getPaperStack() {
        return this.writingInv.getItem(1);
    }
    
    public boolean isWritingReady() {
        return this.writingReady.get() != 0;
    }
    
    public void consumeWritingImplements() {
        // Don't consume if in creative mode
        if (!this.player.getAbilities().instabuild) {
            // Consume ink, if applicable
            ItemStack inkStack = this.getWritingImplementStack();
            if (!inkStack.isEmpty() && inkStack.getItem() instanceof IWritingImplement && ((IWritingImplement)inkStack.getItem()).isDamagedOnUse()) {
                inkStack.hurtAndBreak(1, this.player, (player) -> {});
            }

            // Consume paper
            this.writingInv.removeItem(1, 1);
        }
    }
    
    @Nonnull
    public ContainerLevelAccess getWorldPosCallable() {
        return this.worldPosCallable;
    }
    
    @Nonnull
    public List<Component> getNearbyAidBlockNames() {
        Set<Block> nearby = this.worldPosCallable.evaluate((level, pos) -> {
            return TheorycraftManager.getNearbyAidBlocks(this.player.level(), pos);
        }, Collections.emptySet());
        return nearby.stream().map(b -> b.getName()).distinct().sorted(Comparator.comparing(c -> c.getString())).collect(Collectors.toList());
    }
}
