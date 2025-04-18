package com.verdantartifice.primalmagick.common.menus;

import com.verdantartifice.primalmagick.common.items.essence.EssenceType;
import com.verdantartifice.primalmagick.common.menus.base.AbstractTileSidedInventoryMenu;
import com.verdantartifice.primalmagick.common.menus.slots.FilteredSlotProperties;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.keys.ResearchEntryKey;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.ResearchRequirement;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.Sources;
import com.verdantartifice.primalmagick.common.tags.ItemTagsPM;
import com.verdantartifice.primalmagick.common.tiles.devices.EssenceCaskTileEntity;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * Server data container for the essence cask GUI.
 * 
 * @author Daedalus4096
 */
public class EssenceCaskMenu extends AbstractTileSidedInventoryMenu<EssenceCaskTileEntity> {
    public static final ResourceLocation DUST_SLOT_TEXTURE = ResourceUtils.loc("item/empty_essence_dust_slot");
    public static final ResourceLocation SHARD_SLOT_TEXTURE = ResourceUtils.loc("item/empty_essence_shard_slot");
    public static final ResourceLocation CRYSTAL_SLOT_TEXTURE = ResourceUtils.loc("item/empty_essence_crystal_slot");
    public static final ResourceLocation CLUSTER_SLOT_TEXTURE = ResourceUtils.loc("item/empty_essence_cluster_slot");
    protected static final Component INPUT_SLOT_TOOLTIP = Component.translatable("tooltip.primalmagick.essence_cask.slot.essence");
    protected static final AbstractRequirement<?> SHARD_REQUIREMENT = new ResearchRequirement(new ResearchEntryKey(ResearchEntries.SHARD_SYNTHESIS));
    protected static final AbstractRequirement<?> CRYSTAL_REQUIREMENT = new ResearchRequirement(new ResearchEntryKey(ResearchEntries.CRYSTAL_SYNTHESIS));
    protected static final AbstractRequirement<?> CLUSTER_REQUIREMENT = new ResearchRequirement(new ResearchEntryKey(ResearchEntries.CLUSTER_SYNTHESIS));
    
    protected final ContainerData caskData;
    protected final Slot inputSlot;

    public EssenceCaskMenu(int id, Inventory playerInv, BlockPos pos) {
        this(id, playerInv, pos, null, new SimpleContainerData(EssenceCaskTileEntity.NUM_SLOTS));
    }
    
    public EssenceCaskMenu(int id, Inventory playerInv, BlockPos pos, EssenceCaskTileEntity cask, ContainerData caskData) {
        super(MenuTypesPM.ESSENCE_CASK.get(), id, EssenceCaskTileEntity.class, playerInv.player.level(), pos, cask);
        checkContainerDataCount(caskData, EssenceCaskTileEntity.NUM_SLOTS);
        this.caskData = caskData;
        
        this.tile.startOpen(playerInv.player);
        
        // Slot 0: Cask input
        this.inputSlot = this.addSlot(Services.MENU.makeFilteredSlot(this.getTileInventory(Direction.UP), 0, 80, 108, new FilteredSlotProperties().tag(ItemTagsPM.ESSENCES).tooltip(INPUT_SLOT_TOOLTIP)
                .background(DUST_SLOT_TEXTURE)
                .background(SHARD_SLOT_TEXTURE, $ -> SHARD_REQUIREMENT.isMetBy(playerInv.player))
                .background(CRYSTAL_SLOT_TEXTURE, $ -> CRYSTAL_REQUIREMENT.isMetBy(playerInv.player))
                .background(CLUSTER_SLOT_TEXTURE, $ -> CLUSTER_REQUIREMENT.isMetBy(playerInv.player))));
        
        // Slots 1-27: Player backpack
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 140 + i * 18));
            }
        }

        // Slots 28-36: Player hotbar
        for (int k = 0; k < 9; k++) {
            this.addSlot(new Slot(playerInv, k, 8 + k * 18, 198));
        }
        
        this.addDataSlots(this.caskData);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            stack = slotStack.copy();
            if (index == 0) {
                // If transferring an item in the input slot, move it into the player's backpack or hotbar
                if (!this.moveItemStackTo(slotStack, 1, 37, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.inputSlot.mayPlace(slotStack)) {
                // If transferring a valid essence, move it into the appropriate slot
                if (!this.moveItemStackTo(slotStack, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 1 && index < 28) {
                // If transferring from the backpack and not a valid fit, move to the hotbar
                if (!this.moveItemStackTo(slotStack, 28, 37, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 28 && index < 37) {
                // If transferring from the hotbar and not a valid fit, move to the backpack
                if (!this.moveItemStackTo(slotStack, 1, 28, false)) {
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
            
            slot.onTake(player, slotStack);
            this.broadcastChanges();
        }
        
        return stack;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.tile.stopOpen(player);
    }

    public int getEssenceCount(int index) {
        return this.caskData.get(index);
    }
    
    public int getTotalEssenceCount() {
        int retVal = 0;
        for (int index = 0; index < this.caskData.getCount(); index++) {
            retVal += this.caskData.get(index);
        }
        return retVal;
    }
    
    public int getTotalEssenceCapacity() {
        BlockEntity tile = this.level.getBlockEntity(this.tilePos);
        if (tile instanceof EssenceCaskTileEntity caskTile) {
            return caskTile.getTotalEssenceCapacity();
        } else {
            return 0;
        }
    }
    
    public boolean isEssenceTypeVisible(EssenceType type, Player player) {
        if (type == null) {
            return false;
        }
        
        // If the essence type's synthesis research has been discovered, then it's visible
        if (type.isDiscovered(player)) {
            return true;
        }
        
        // If the research has not been discovered, but there's an essence of that type in the cask, then it's visible
        int typeIndex = type.ordinal();
        int rowSize = Sources.getAllSorted().size();
        for (int index = 0; index < this.caskData.getCount(); index++) {
            if (index / rowSize == typeIndex && this.caskData.get(index) > 0) {
                return true;
            }
        }
        
        // Otherwise, it's not visible
        return false;
    }
    
    public boolean isEssenceSourceVisible(Source source, Player player) {
        if (source == null) {
            return false;
        }
        
        // If the essence source has been discovered, then it's visible
        if (source.isDiscovered(player)) {
            return true;
        }
        
        // If the source has not been discovered, but there's an essence of that source in the cask, then it's visible
        int sourceIndex = Sources.getAllSorted().indexOf(source);
        int rowSize = Sources.getAllSorted().size();
        for (int index = 0; index < this.caskData.getCount(); index++) {
            if (index % rowSize == sourceIndex && this.caskData.get(index) > 0) {
                return true;
            }
        }
        
        // Otherwise, it's not visible
        return false;
    }
}
