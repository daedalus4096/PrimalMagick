package com.verdantartifice.primalmagick.common.menus;

import com.verdantartifice.primalmagick.common.concoctions.ConcoctionUtils;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.menus.base.AbstractTileSidedInventoryMenu;
import com.verdantartifice.primalmagick.common.menus.slots.FilteredSlotProperties;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.keys.ResearchEntryKey;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.ResearchRequirement;
import com.verdantartifice.primalmagick.common.tiles.devices.DesalinatorTileEntity;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;

/**
 * Server data container for the desalinator GUI.
 *
 * @author Daedalus4096
 */
public class DesalinatorMenu extends AbstractTileSidedInventoryMenu<DesalinatorTileEntity> {
    public static final ResourceLocation BUCKET_SLOT_TEXTURE = ResourceUtils.loc("item/empty_bucket_slot");
    public static final ResourceLocation BOTTLE_SLOT_TEXTURE = ResourceUtils.loc("item/empty_bottle_slot");
    public static final ResourceLocation FLASK_SLOT_TEXTURE = ResourceUtils.loc("item/empty_flask_slot");
    protected static final Component WATER_BUCKET_TOOLTIP = Component.translatable("tooltip.primalmagick.desalinator.slot.water_bucket");
    protected static final AbstractRequirement<?> FLASK_REQUIREMENT = new ResearchRequirement(new ResearchEntryKey(ResearchEntries.CONCOCTING_TINCTURES));

    protected final ContainerData containerData;
    protected final Slot waterBucketSlot;
    protected final Slot wandSlot;

    public DesalinatorMenu(int id, Inventory playerInv, BlockPos tilePos) {
        this(id, playerInv, tilePos, null, new SimpleContainerData(4));
    }

    public DesalinatorMenu(int id, Inventory playerInv, BlockPos tilePos, DesalinatorTileEntity desalinator, ContainerData containerData) {
        super(MenuTypesPM.DESALINATOR.get(), id, DesalinatorTileEntity.class, playerInv.player.level(), tilePos, desalinator);
        checkContainerDataCount(containerData, 6);
        this.containerData = containerData;

        // Slot 0: water bucket input
        this.waterBucketSlot = this.addSlot(Services.MENU.makeFilteredSlot(this.getTileInventory(DesalinatorTileEntity.INPUT_INV_INDEX), 0, 30, 17,
                new FilteredSlotProperties().filter(DesalinatorMenu::isFullWaterContainer).tooltip(WATER_BUCKET_TOOLTIP)
                        .background(BUCKET_SLOT_TEXTURE)
                        .background(BOTTLE_SLOT_TEXTURE)
                        .background(FLASK_SLOT_TEXTURE, $ -> FLASK_REQUIREMENT.isMetBy(playerInv.player))));

        // Slot 1: salt output
        this.addSlot(Services.MENU.makeGenericResultSlot(playerInv.player, this.getTileInventory(DesalinatorTileEntity.OUTPUT_INV_INDEX), 0, 108, 45));

        // Slot 2: essence output
        this.addSlot(Services.MENU.makeGenericResultSlot(playerInv.player, this.getTileInventory(DesalinatorTileEntity.OUTPUT_INV_INDEX), 1, 130, 45));

        // Slot 3: empty bucket output
        this.addSlot(Services.MENU.makeGenericResultSlot(playerInv.player, this.getTileInventory(DesalinatorTileEntity.OUTPUT_INV_INDEX), 2, 30, 72));

        // Slot 4: wand input
        this.wandSlot = this.addSlot(Services.MENU.makeWandSlot(this.getTileInventory(DesalinatorTileEntity.WAND_INV_INDEX), 0, 8, 72, false));

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

        this.addDataSlots(this.containerData);
    }

    private static boolean isFullWaterContainer(ItemStack stack) {
        // Water buckets, water bottles, and water flasks are allowed; nothing else
        return stack.is(Items.WATER_BUCKET) ||
                (stack.is(Items.POTION) && stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).is(Potions.WATER)) ||
                (stack.is(ItemsPM.CONCOCTION.get()) && stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).is(Potions.WATER));
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            stack = slotStack.copy();
            if (index == 1 || index == 2 || index == 3) {
                // If transferring an output item, move it into the player's backpack or hotbar
                if (!this.moveItemStackTo(slotStack, 5, 41, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(slotStack, stack);
            } else if (index == 0 || index == 4) {
                // If transferring one of the input items, move it into the player's backpack or hotbar
                if (!this.moveItemStackTo(slotStack, 5, 41, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.waterBucketSlot.mayPlace(slotStack)) {
                // If transferring a valid ingredient, move it into the appropriate slot
                if (!this.moveItemStackTo(slotStack, 0, 1, false)) {
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

            slot.onTake(player, slotStack);
            this.broadcastChanges();
        }

        return stack;
    }

    public int getBoilProgressionScaled() {
        // Determine how much of the progress arrow to show
        int i = this.containerData.get(0);
        int j = this.containerData.get(1);
        return j != 0 && i != 0 ? i * 24 / j : 0;
    }

    public int getCurrentMana() {
        return this.containerData.get(2);
    }

    public int getMaxMana() {
        return this.containerData.get(3);
    }

    public int getCurrentWaterAmount() {
        return this.containerData.get(4);
    }

    public int getWaterCapacity() {
        return this.containerData.get(5);
    }
}
