package com.verdantartifice.primalmagick.common.menus;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.menus.base.AbstractTileSidedInventoryMenu;
import com.verdantartifice.primalmagick.common.menus.slots.FilteredSlot;
import com.verdantartifice.primalmagick.common.menus.slots.GenericResultSlot;
import com.verdantartifice.primalmagick.common.menus.slots.WandSlot;
import com.verdantartifice.primalmagick.common.tiles.devices.HoneyExtractorTileEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/**
 * Server data container for the honey extractor GUI.
 * 
 * @author Daedalus4096
 */
public class HoneyExtractorMenu extends AbstractTileSidedInventoryMenu<HoneyExtractorTileEntity> {
    public static final ResourceLocation BOTTLE_SLOT_TEXTURE = PrimalMagick.resource("item/empty_bottle_slot");
    public static final ResourceLocation HONEYCOMB_SLOT_TEXTURE = PrimalMagick.resource("item/empty_honeycomb_slot");

    protected final ContainerData extractorData;
    protected final Slot honeycombSlot;
    protected final Slot bottleSlot;
    protected final Slot wandSlot;

    public HoneyExtractorMenu(int id, Inventory playerInv, BlockPos tilePos) {
        this(id, playerInv, tilePos, null, new SimpleContainerData(4));
    }
    
    public HoneyExtractorMenu(int id, Inventory playerInv, BlockPos tilePos, HoneyExtractorTileEntity extractor, ContainerData extractorData) {
        super(MenuTypesPM.HONEY_EXTRACTOR.get(), id, HoneyExtractorTileEntity.class, playerInv.player.level(), tilePos, extractor);
        checkContainerDataCount(extractorData, 4);
        this.extractorData = extractorData;
        
        // Slot 0: honeycomb input
        this.honeycombSlot = this.addSlot(new FilteredSlot(this.getTileInventory(Direction.UP), 0, 30, 35,
                new FilteredSlot.Properties().background(HONEYCOMB_SLOT_TEXTURE).item(Items.HONEYCOMB)));
        
        // Slot 1: bottle input
        this.bottleSlot = this.addSlot(new FilteredSlot(this.getTileInventory(Direction.UP), 1, 52, 35, 
                new FilteredSlot.Properties().background(BOTTLE_SLOT_TEXTURE).item(Items.GLASS_BOTTLE)));
        
        // Slot 2: honey output
        this.addSlot(new GenericResultSlot(playerInv.player, this.getTileInventory(Direction.DOWN), 0, 108, 35));
        
        // Slot 3: beeswax output
        this.addSlot(new GenericResultSlot(playerInv.player, this.getTileInventory(Direction.DOWN), 1, 130, 35));
        
        // Slot 4: wand input
        this.wandSlot = this.addSlot(new WandSlot(this.getTileInventory(Direction.NORTH), 0, 8, 62, false));
        
        // Slots 5-31: player backpack
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        // Slots 32-40: player hotbar
        for (int k = 0; k < 9; k++) {
            this.addSlot(new Slot(playerInv, k, 8 + k * 18, 142));
        }

        this.addDataSlots(this.extractorData);
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            stack = slotStack.copy();
            if (index == 2 || index == 3) {
                // If transferring an output item, move it into the player's backpack or hotbar
                if (!this.moveItemStackTo(slotStack, 5, 41, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(slotStack, stack);
            } else if (index == 0 || index == 1 || index == 4) {
                // If transferring one of the input items, move it into the player's backpack or hotbar
                if (!this.moveItemStackTo(slotStack, 5, 41, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.honeycombSlot.mayPlace(slotStack)) {
                // If transferring a valid ingredient, move it into the appropriate slot
                if (!this.moveItemStackTo(slotStack, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.bottleSlot.mayPlace(slotStack)) {
                // If transferring a valid ingredient, move it into the appropriate slot
                if (!this.moveItemStackTo(slotStack, 1, 2, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.wandSlot.mayPlace(slotStack)) {
                // If transferring a valid wand, move it into the appropriate slot
                if (!this.moveItemStackTo(slotStack, 4, 5, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 5 && index < 32) {
                // If transferring from the backpack and not a valid fit, move to the hotbar
                if (!this.moveItemStackTo(slotStack, 32, 41, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 32 && index < 41) {
                // If transferring from the hotbar and not a valid fit, move to the backpack
                if (!this.moveItemStackTo(slotStack, 5, 32, false)) {
                    return ItemStack.EMPTY;
                }
            }
            
            if (slotStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            }
            
            slot.setChanged();
            if (slotStack.getCount() == stack.getCount()) {
                return ItemStack.EMPTY;
            }
            
            slot.onTake(playerIn, slotStack);
            this.broadcastChanges();
        }
        
        return stack;
    }

    public int getSpinProgressionScaled() {
        // Determine how much of the progress arrow to show
        int i = this.extractorData.get(0);
        int j = this.extractorData.get(1);
        return j != 0 && i != 0 ? i * 24 / j : 0;
    }
    
    public int getCurrentMana() {
        return this.extractorData.get(2);
    }
    
    public int getMaxMana() {
        return this.extractorData.get(3);
    }
}
