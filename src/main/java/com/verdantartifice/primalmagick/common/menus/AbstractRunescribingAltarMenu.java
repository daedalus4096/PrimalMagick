package com.verdantartifice.primalmagick.common.menus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.items.misc.RuneItem;
import com.verdantartifice.primalmagick.common.menus.base.AbstractTileMenu;
import com.verdantartifice.primalmagick.common.menus.slots.FilteredSlot;
import com.verdantartifice.primalmagick.common.menus.slots.RunescribingResultSlot;
import com.verdantartifice.primalmagick.common.runes.Rune;
import com.verdantartifice.primalmagick.common.runes.RuneManager;
import com.verdantartifice.primalmagick.common.tiles.crafting.RunescribingAltarTileEntity;
import com.verdantartifice.primalmagick.common.util.InventoryUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.TransientCraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;

/**
 * Base server data container for the runescribing altar GUIs.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractRunescribingAltarMenu extends AbstractTileMenu<RunescribingAltarTileEntity> {
    public static final ResourceLocation RUNE_SLOT_TEXTURE = PrimalMagick.resource("item/empty_rune_slot");

    protected final CraftingContainer altarInv = new TransientCraftingContainer(this, 4, 3) {
        @Override
        public int getMaxStackSize() {
            return 1;
        }
    };
    protected final Container resultInv = new SimpleContainer(1);
    protected final Player player;
    protected final Level world;
    protected final Slot runeSlot;

    public AbstractRunescribingAltarMenu(@Nonnull MenuType<?> type, int id, @Nonnull Inventory playerInv, BlockPos tilePos, RunescribingAltarTileEntity altar) {
        super(type, id, RunescribingAltarTileEntity.class, playerInv.player.level(), tilePos, altar);
        this.player = playerInv.player;
        this.world = this.player.level();
        
        // Slot 0: runescribing output
        this.addSlot(new RunescribingResultSlot(this.player, this.altarInv, this.resultInv, 0, 138, 35));
        
        // Slot 1: runescribing input
        this.addSlot(new Slot(this.altarInv, 0, 19, 35));
        
        // Slots 2-(R+1), where R = rune capacity: runes
        this.runeSlot = this.addRuneSlots();
        
        // Slots (R+2)-(R+28), where R = rune capacity: Player backpack
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        
        // Slots (R+29)-(R+37), where R = rune capacity: Player hotbar
        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(playerInv, i, 8 + i * 18, 142));
        }
    }
    
    /**
     * Get the number of runes holdable by this container.
     * 
     * @return the number of runes holdable by this container
     */
    protected abstract int getRuneCapacity();
    
    /**
     * Add slots for runes to this container.
     */
    @Nonnull
    protected abstract Slot addRuneSlots();
    
    protected static Slot makeRuneSlot(Container inventoryIn, int index, int xPosition, int yPosition) {
        return new FilteredSlot(InventoryUtils.wrapInventory(inventoryIn, null), index, xPosition, yPosition,
                new FilteredSlot.Properties().background(RUNE_SLOT_TEXTURE).typeOf(RuneItem.class));
    }
    
    @Override
    public void removed(Player playerIn) {
        super.removed(playerIn);
        this.clearContainer(playerIn, this.altarInv);
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            stack = slotStack.copy();
            if (index == 0) {
                // If transferring the output item, move it into the player's backpack or hotbar
                if (!this.moveItemStackTo(slotStack, this.getRuneCapacity() + 2, this.getRuneCapacity() + 38, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(slotStack, stack);
            } else if (index >= (this.getRuneCapacity() + 2) && index < (this.getRuneCapacity() + 38)) {
                // If transferring from the player's backpack or hotbar, put runes in the rune section and everything else into the input slot
                if (this.runeSlot.mayPlace(slotStack)) {
                    if (!this.moveItemStackTo(slotStack, 2, this.getRuneCapacity() + 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    if (!this.moveItemStackTo(slotStack, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (!this.moveItemStackTo(slotStack, this.getRuneCapacity() + 2, this.getRuneCapacity() + 38, false)) {
                // Move all other transfers into the backpack or hotbar
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
    
    @Override
    public boolean canTakeItemForPickAll(ItemStack stack, Slot slotIn) {
        return slotIn.container != this.resultInv && super.canTakeItemForPickAll(stack, slotIn);
    }
    
    @Override
    public void slotsChanged(Container inventoryIn) {
        super.slotsChanged(inventoryIn);
        this.slotChangedCraftingGrid();
    }
    
    protected void slotChangedCraftingGrid() {
        if (!this.world.isClientSide && this.player instanceof ServerPlayer) {
            ServerPlayer spe = (ServerPlayer)this.player;
            ItemStack stack = ItemStack.EMPTY;
            ItemStack baseStack = this.altarInv.getItem(0);
            
            // Don't allow application to items that already have runes
            if (!RuneManager.hasRunes(baseStack)) {
                // Get the list of runes in the input
                List<Rune> runes = new ArrayList<>();
                for (int index = 1; index < this.altarInv.getContainerSize(); index++) {
                    ItemStack inputStack = this.altarInv.getItem(index);
                    if (inputStack != null && inputStack.getItem() instanceof RuneItem) {
                        runes.add(((RuneItem)inputStack.getItem()).getRune());
                    }
                }

                // Determine what enchantments can be applied with the slotted rune combination
                Map<Enchantment, Integer> inputEnch = RuneManager.getRuneEnchantments(runes, baseStack, this.player, true);
                if (!inputEnch.isEmpty()) {
                    Map<Enchantment, Integer> finalEnch = RuneManager.mergeEnchantments(EnchantmentHelper.getEnchantments(baseStack), inputEnch);
                    stack = baseStack.copy();
                    EnchantmentHelper.setEnchantments(finalEnch, stack);
                    RuneManager.setRunes(stack, runes);
                }
            }
            
            // Send a packet to the client to update its GUI with the shown output
            this.resultInv.setItem(0, stack);
            spe.connection.send(new ClientboundContainerSetSlotPacket(this.containerId, this.incrementStateId(), 0, stack));
        }
    }
}
