package com.verdantartifice.primalmagick.common.menus;

import com.verdantartifice.primalmagick.common.capabilities.IItemHandlerPM;
import com.verdantartifice.primalmagick.common.crafting.IRunecarvingRecipe;
import com.verdantartifice.primalmagick.common.crafting.RecipeTypesPM;
import com.verdantartifice.primalmagick.common.crafting.inputs.RunecarvingRecipeInput;
import com.verdantartifice.primalmagick.common.items.IItemHandlerChangeListener;
import com.verdantartifice.primalmagick.common.menus.base.AbstractTileSidedInventoryMenu;
import com.verdantartifice.primalmagick.common.menus.slots.FilteredSlotProperties;
import com.verdantartifice.primalmagick.common.tags.ItemTagsPM;
import com.verdantartifice.primalmagick.common.tiles.crafting.RunecarvingTableTileEntity;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.RecipeCraftingHolder;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Server data container for the runecarving table GUI.
 * 
 * @author Daedalus4096
 */
public class RunecarvingTableMenu extends AbstractTileSidedInventoryMenu<RunecarvingTableTileEntity> implements IItemHandlerChangeListener {
    public static final ResourceLocation BASE_SLOT_TEXTURE = ResourceUtils.loc("item/empty_slab_slot");
    public static final ResourceLocation ETCHING_SLOT_TEXTURE = ResourceUtils.loc("item/empty_lapis_slot");
    protected static final Component BASE_SLOT_TOOLTIP = Component.translatable("tooltip.primalmagick.runecarving_table.slot.base");
    protected static final Component ETCHING_SLOT_TOOLTIP = Component.translatable("tooltip.primalmagick.runecarving_table.slot.etching");

    protected final DataSlot selectedRecipe = DataSlot.standalone();
    protected final Player player;
    
    protected final Slot inputSlabSlot;
    protected final Slot inputLapisSlot;
    protected final Slot outputSlot;
    protected final ResultContainer outputInventory = new ResultContainer();

    protected List<RecipeHolder<IRunecarvingRecipe>> recipes = new ArrayList<>();
    
    protected ItemStack slabInput = ItemStack.EMPTY;
    protected ItemStack lapisInput = ItemStack.EMPTY;

    /**
     * Stores the game time of the last time the player took items from the crafting result slot. This is used to
     * prevent the sound from being played multiple times on the same tick.
     */
    protected long lastOnTake;
    protected Runnable inventoryUpdateListener = () -> {};

    public RunecarvingTableMenu(int windowId, Inventory inv, BlockPos pos) {
        this(windowId, inv, pos, null);
    }
    
    public RunecarvingTableMenu(int windowId, Inventory inv, BlockPos pos, RunecarvingTableTileEntity table) {
        super(MenuTypesPM.RUNECARVING_TABLE.get(), windowId, RunecarvingTableTileEntity.class, inv.player.level(), pos, table);
        this.player = inv.player;
        this.tile.addInventoryChangeListener(Direction.UP, this);
        
        // Slot 0: input slabs
        this.inputSlabSlot = this.addSlot(Services.MENU.makeFilteredSlot(this.getTileInventory(RunecarvingTableTileEntity.INPUT_INV_INDEX), 0, 20, 21,
                new FilteredSlotProperties().background(BASE_SLOT_TEXTURE).tooltip(BASE_SLOT_TOOLTIP).tag(ItemTagsPM.RUNE_BASES)));
        
        // Slot 1: input lapis
        this.inputLapisSlot = this.addSlot(Services.MENU.makeFilteredSlot(this.getTileInventory(RunecarvingTableTileEntity.INPUT_INV_INDEX), 1, 20, 46,
                new FilteredSlotProperties().background(ETCHING_SLOT_TEXTURE).tooltip(ETCHING_SLOT_TOOLTIP).tag(ItemTagsPM.RUNE_ETCHINGS)));
        
        // Slot 2: runecarving output
        this.outputSlot = this.addSlot(Services.MENU.makeRunecarvingResultSlot(this, this.player,
                Services.ITEM_HANDLERS.wrap(this.outputInventory, null), 0, 143, 33));
        
        // Slots 3-29: Player backpack
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(inv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        
        // Slots 30-38: Player hotbar
        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(inv, i, 8 + i * 18, 142));
        }

        this.addDataSlot(this.selectedRecipe);
        
        // Do an immediate update of the table GUI
        this.itemsChanged(RunecarvingTableTileEntity.INPUT_INV_INDEX, this.getTileInventory(RunecarvingTableTileEntity.INPUT_INV_INDEX));
    }

    public RecipeCraftingHolder getOutputInventory() {
        return this.outputInventory;
    }

    public long getLastOnTake() {
        return this.lastOnTake;
    }

    public void setLastOnTake(long lastOnTake) {
        this.lastOnTake = lastOnTake;
    }

    public int getSelectedRecipe() {
        return this.selectedRecipe.get();
    }

    public List<RecipeHolder<IRunecarvingRecipe>> getRecipeList() {
        return this.recipes;
    }

    public int getRecipeListSize() {
        return this.recipes.size();
    }

    public boolean hasItemsInInputSlot() {
        return this.inputSlabSlot.hasItem() && this.inputLapisSlot.hasItem() && !this.recipes.isEmpty();
    }
    
    @Override
    public boolean clickMenuButton(Player playerIn, int id) {
        if (id >= 0 && id < this.recipes.size()) {
            this.selectedRecipe.set(id);
            this.outputInventory.setRecipeUsed(this.recipes.get(id));
            this.updateRecipeResultSlot(playerIn.level().registryAccess());
        }
        return true;
    }

    @Override
    public void itemsChanged(int itemHandlerIndex, IItemHandlerPM itemHandler) {
        ItemStack slabStack = this.inputSlabSlot.getItem();
        ItemStack lapisStack = this.inputLapisSlot.getItem();
        if (slabStack.getItem() != this.slabInput.getItem() || lapisStack.getItem() != this.lapisInput.getItem()) {
            this.slabInput = slabStack.copy();
            this.lapisInput = lapisStack.copy();
            this.updateAvailableRecipes(itemHandler, slabStack, lapisStack);
        }
        this.inventoryUpdateListener.run();
    }
    
    private static RunecarvingRecipeInput createRecipeInput(IItemHandlerPM inventory) {
        return new RunecarvingRecipeInput(inventory.getStackInSlot(0), inventory.getStackInSlot(1));
    }
    
    protected void updateAvailableRecipes(IItemHandlerPM inventoryIn, ItemStack slabStack, ItemStack lapisStack) {
        this.recipes.clear();
        this.selectedRecipe.set(-1);
        this.outputSlot.set(ItemStack.EMPTY);
        this.recipes = this.level.getRecipeManager().getRecipesFor(RecipeTypesPM.RUNECARVING.get(), createRecipeInput(inventoryIn), this.level).stream()
                .filter(r -> r != null && (r.value().getRequirement().isEmpty() || r.value().getRequirement().get().isMetBy(this.player)))
                .collect(Collectors.toList());
    }
    
    public void updateRecipeResultSlot(RegistryAccess registryAccess) {
        if (!this.recipes.isEmpty() && this.getTileInventory(Direction.UP) != null) {
            IRunecarvingRecipe recipe = this.recipes.get(this.selectedRecipe.get()).value();
            this.outputSlot.set(recipe.assemble(createRecipeInput(this.getTileInventory(Direction.UP)), registryAccess));
        } else {
            this.outputSlot.set(ItemStack.EMPTY);
        }
        this.broadcastChanges();
    }

    public void setInventoryUpdateListener(Runnable listenerIn) {
        this.inventoryUpdateListener = listenerIn;
    }
    
    @Override
    public boolean canTakeItemForPickAll(ItemStack stack, Slot slotIn) {
        return slotIn != this.outputSlot && super.canTakeItemForPickAll(stack, slotIn);
    }
    
    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            stack = slotStack.copy();
            if (index == 2) {
                // If transferring the output item, move it into the player's backpack or hotbar
                slotStack.getItem().onCraftedBy(slotStack, playerIn.level(), playerIn);
                if (!this.moveItemStackTo(slotStack, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(slotStack, stack);
            } else if (index == 0 || index == 1) {
                // If transferring one of the input items, move it into the player's backpack or hotbar
                if (!this.moveItemStackTo(slotStack, 3, 39, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.inputSlabSlot.mayPlace(slotStack)) {
                // If transferring a valid ingredient, move it into the appropriate slot
                if (!this.moveItemStackTo(slotStack, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.inputLapisSlot.mayPlace(slotStack)) {
                // If transferring a valid ingredient, move it into the appropriate slot
                if (!this.moveItemStackTo(slotStack, 1, 2, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 3 && index < 30) {
                // If transferring from the backpack and not a valid fit, move to the hotbar
                if (!this.moveItemStackTo(slotStack, 30, 39, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 30 && index < 39) {
                // If transferring from the hotbar and not a valid fit, move to the backpack
                if (!this.moveItemStackTo(slotStack, 3, 30, false)) {
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
    
    @Override
    public void removed(Player playerIn) {
        super.removed(playerIn);
        this.outputInventory.removeItemNoUpdate(0);
    }
}
