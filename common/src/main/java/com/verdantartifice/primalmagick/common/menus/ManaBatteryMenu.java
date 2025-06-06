package com.verdantartifice.primalmagick.common.menus;

import com.verdantartifice.primalmagick.common.components.DataComponentsPM;
import com.verdantartifice.primalmagick.common.items.essence.EssenceItem;
import com.verdantartifice.primalmagick.common.menus.base.AbstractTileSidedInventoryMenu;
import com.verdantartifice.primalmagick.common.menus.data.ContainerSynchronizerLarge;
import com.verdantartifice.primalmagick.common.menus.slots.FilteredSlotProperties;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.keys.ResearchEntryKey;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.ResearchRequirement;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.Sources;
import com.verdantartifice.primalmagick.common.tiles.mana.ManaBatteryTileEntity;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import com.verdantartifice.primalmagick.common.wands.IWand;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerSynchronizer;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/**
 * Server data container for mana battery GUIs.
 * 
 * @author Daedalus4096
 */
public class ManaBatteryMenu extends AbstractTileSidedInventoryMenu<ManaBatteryTileEntity> {
    public static final ResourceLocation DUST_SLOT_TEXTURE = ResourceUtils.loc("item/empty_essence_dust_slot");
    public static final ResourceLocation SHARD_SLOT_TEXTURE = ResourceUtils.loc("item/empty_essence_shard_slot");
    public static final ResourceLocation CRYSTAL_SLOT_TEXTURE = ResourceUtils.loc("item/empty_essence_crystal_slot");
    public static final ResourceLocation CLUSTER_SLOT_TEXTURE = ResourceUtils.loc("item/empty_essence_cluster_slot");
    public static final ResourceLocation WAND_SLOT_TEXTURE = ResourceUtils.loc("item/empty_wand_slot");
    protected static final Component INPUT_SLOT_TOOLTIP = Component.translatable("tooltip.primalmagick.mana_battery.slot.input");
    protected static final Component CHARGE_SLOT_TOOLTIP = Component.translatable("tooltip.primalmagick.mana_battery.slot.charge");
    protected static final AbstractRequirement<?> SHARD_REQUIREMENT = new ResearchRequirement(new ResearchEntryKey(ResearchEntries.SHARD_SYNTHESIS));
    protected static final AbstractRequirement<?> CRYSTAL_REQUIREMENT = new ResearchRequirement(new ResearchEntryKey(ResearchEntries.CRYSTAL_SYNTHESIS));
    protected static final AbstractRequirement<?> CLUSTER_REQUIREMENT = new ResearchRequirement(new ResearchEntryKey(ResearchEntries.CLUSTER_SYNTHESIS));

    protected final ContainerData data;
    protected final Slot inputSlot;
    protected final Slot chargeSlot;
    protected final Player player;
    
    public ManaBatteryMenu(int id, Inventory playerInv, BlockPos tilePos) {
        this(id, playerInv, tilePos, null, new SimpleContainerData(20));
    }
    
    public ManaBatteryMenu(int id, Inventory playerInv, BlockPos tilePos, ManaBatteryTileEntity tile, ContainerData data) {
        super(MenuTypesPM.MANA_BATTERY.get(), id, ManaBatteryTileEntity.class, playerInv.player.level(), tilePos, tile);
        checkContainerDataCount(data, 20);
        this.player = playerInv.player;
        this.data = data;
        
        // Slot 0: input slot
        this.inputSlot = this.addSlot(Services.MENU.makeFilteredSlot(this.getTileInventory(Direction.UP), 0, 8, 34, new FilteredSlotProperties().typeOf(EssenceItem.class, IWand.class).tooltip(INPUT_SLOT_TOOLTIP)
                .background(DUST_SLOT_TEXTURE)
                .background(SHARD_SLOT_TEXTURE, $ -> SHARD_REQUIREMENT.isMetBy(playerInv.player))
                .background(CRYSTAL_SLOT_TEXTURE, $ -> CRYSTAL_REQUIREMENT.isMetBy(playerInv.player))
                .background(CLUSTER_SLOT_TEXTURE, $ -> CLUSTER_REQUIREMENT.isMetBy(playerInv.player))
                .background(WAND_SLOT_TEXTURE)));
        
        // Slot 1: charge slot
        this.chargeSlot = this.addSlot(Services.MENU.makeFilteredSlot(this.getTileInventory(Direction.NORTH), 0, 206, 34, 
                new FilteredSlotProperties().filter(stack -> stack.has(DataComponentsPM.CAPABILITY_MANA_STORAGE.get())).tooltip(CHARGE_SLOT_TOOLTIP)));

        // Slots 2-28: player backpack
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInv, j + i * 9 + 9, 35 + j * 18, 82 + i * 18));
            }
        }

        // Slots 29-37: player hotbar
        for (int k = 0; k < 9; k++) {
            this.addSlot(new Slot(playerInv, k, 35 + k * 18, 140));
        }

        this.addDataSlots(this.data);
    }
    
    @Override
    public void setSynchronizer(ContainerSynchronizer pSynchronizer) {
        // The data slot values anticipated by this menu are too large to be transfered by the vanilla synchronizer,
        // which transmits data slot values as shorts.  Assuming that this menu is on the server side, ignore the
        // given synchronizer and substitute in one which can handle larger values.
        if (this.player instanceof ServerPlayer serverPlayer) {
            super.setSynchronizer(new ContainerSynchronizerLarge(serverPlayer));
        } else {
            super.setSynchronizer(pSynchronizer);
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            stack = slotStack.copy();
            if (index >= 2 && index < 29) {
                // If transferring from the backpack, move wands and essences to the appropriate slots, and everything else to the hotbar
                if (this.chargeSlot.mayPlace(slotStack)) {
                    if (!this.moveItemStackTo(slotStack, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.inputSlot.mayPlace(slotStack)) {
                    if (!this.moveItemStackTo(slotStack, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    if (!this.moveItemStackTo(slotStack, 29, 38, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (index >= 29 && index < 38) {
                // If transferring from the hotbar, move wands and essences to the appropriate slots, and everything else to the backpack
                if (this.chargeSlot.mayPlace(slotStack)) {
                    if (!this.moveItemStackTo(slotStack, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.inputSlot.mayPlace(slotStack)) {
                    if (!this.moveItemStackTo(slotStack, 0, 1, false)) {
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
            
            slot.onTake(player, slotStack);
        }
        return stack;
    }

    public int getChargeProgressionScaled() {
        // Determine how much of the charge arrow to show
        int i = this.data.get(0);
        int j = this.data.get(1);
        return j != 0 && i != 0 ? i * 24 / j : 0;
    }
    
    public int getCurrentMana(Source source) {
        int sourceIndex = Sources.getAllSorted().indexOf(source);
        return this.data.get(2 + (2 * sourceIndex));
    }
    
    public int getMaxMana(Source source) {
        int sourceIndex = Sources.getAllSorted().indexOf(source);
        return this.data.get(3 + (2 * sourceIndex));
    }
}
