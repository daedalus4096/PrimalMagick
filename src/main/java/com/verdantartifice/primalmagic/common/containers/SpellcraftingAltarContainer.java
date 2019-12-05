package com.verdantartifice.primalmagic.common.containers;

import com.verdantartifice.primalmagic.common.blocks.BlocksPM;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.IWorldPosCallable;

public class SpellcraftingAltarContainer extends Container {
    protected final IWorldPosCallable worldPosCallable;
    protected final PlayerEntity player;

    public SpellcraftingAltarContainer(int windowId, PlayerInventory inv) {
        this(windowId, inv, IWorldPosCallable.DUMMY);
    }

    public SpellcraftingAltarContainer(int windowId, PlayerInventory inv, IWorldPosCallable callable) {
        super(ContainersPM.SPELLCRAFTING_ALTAR, windowId);
        this.worldPosCallable = callable;
        this.player = inv.player;
        
        // TODO Add result slot
        // TODO Add input wand slot
        // TODO Add input scroll slot
        
        // Slots 3-29: Player backpack
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(inv, j + (i * 9) + 9, 26 + (j * 18), 140 + (i * 18)));
            }
        }
        
        // Slots 30-38: Player hotbar
        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(inv, i, 26 + (i * 18), 198));
        }
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return isWithinUsableDistance(this.worldPosCallable, playerIn, BlocksPM.SPELLCRAFTING_ALTAR);
    }

}
