package com.verdantartifice.primalmagick.common.containers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import com.verdantartifice.primalmagick.common.containers.slots.GenericResultSlot;
import com.verdantartifice.primalmagick.common.containers.slots.RuneEtchingSlot;
import com.verdantartifice.primalmagick.common.containers.slots.RuneBaseSlot;
import com.verdantartifice.primalmagick.common.crafting.IRunecarvingRecipe;
import com.verdantartifice.primalmagick.common.crafting.RecipeTypesPM;
import com.verdantartifice.primalmagick.common.stats.StatsManager;
import com.verdantartifice.primalmagick.common.stats.StatsPM;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerListener;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Server data container for the runecarving table GUI.
 * 
 * @author Daedalus4096
 */
public class RunecarvingTableContainer extends AbstractContainerMenu implements ContainerListener {
    protected final ContainerLevelAccess worldPosCallable;
    protected final DataSlot selectedRecipe = DataSlot.standalone();
    protected final Player player;
    protected final Level world;
    
    protected final Slot inputSlabSlot;
    protected final Slot inputLapisSlot;
    protected final Slot outputSlot;
    protected final Container tableInv;
    protected final ResultContainer outputInventory = new ResultContainer();

    protected List<IRunecarvingRecipe> recipes = new ArrayList<>();
    
    protected ItemStack slabInput = ItemStack.EMPTY;
    protected ItemStack lapisInput = ItemStack.EMPTY;

    /**
     * Stores the game time of the last time the player took items from the the crafting result slot. This is used to
     * prevent the sound from being played multiple times on the same tick.
     */
    protected long lastOnTake;
    protected Runnable inventoryUpdateListener = () -> {};

    public RunecarvingTableContainer(int windowId, Inventory inv, BlockPos pos) {
        this(windowId, inv, new SimpleContainer(2), ContainerLevelAccess.create(inv.player.level, pos));
        ((SimpleContainer)this.tableInv).addListener(this);
    }
    
    public RunecarvingTableContainer(int windowId, Inventory inv, Container tableInv, ContainerLevelAccess worldPosCallable) {
        super(ContainersPM.RUNECARVING_TABLE.get(), windowId);
        this.worldPosCallable = worldPosCallable;
        this.player = inv.player;
        this.world = inv.player.level;
        checkContainerSize(tableInv, 2);
        this.tableInv = tableInv;
        
        // Slot 0: input slabs
        this.inputSlabSlot = this.addSlot(new RuneBaseSlot(this.tableInv, 0, 20, 21));
        
        // Slot 1: input lapis
        this.inputLapisSlot = this.addSlot(new RuneEtchingSlot(this.tableInv, 1, 20, 46));
        
        // Slot 2: runecarving output
        this.outputSlot = this.addSlot(new GenericResultSlot(this.player, this.outputInventory, 0, 143, 33) {
            @Override
            public void onTake(Player thePlayer, ItemStack stack) {
                RunecarvingTableContainer.this.inputSlabSlot.remove(1);
                RunecarvingTableContainer.this.inputLapisSlot.remove(1);
                RunecarvingTableContainer.this.updateRecipeResultSlot();
                
                stack.getItem().onCraftedBy(stack, thePlayer.level, thePlayer);
                RunecarvingTableContainer.this.worldPosCallable.execute((world, pos) -> {
                    long time = world.getGameTime();
                    if (RunecarvingTableContainer.this.lastOnTake != time) {
                        world.playSound(null, pos, SoundEvents.UI_STONECUTTER_TAKE_RESULT, SoundSource.BLOCKS, 1.0F, 1.0F);
                        RunecarvingTableContainer.this.lastOnTake = time;
                    }
                });
                
                super.onTake(thePlayer, stack);
            }

            @Override
            protected void checkTakeAchievements(ItemStack stack) {
                super.checkTakeAchievements(stack);
                StatsManager.incrementValue(this.player, StatsPM.CRAFTED_RUNEWORKING, stack.getCount());
            }
        });
        
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
        this.containerChanged(this.tableInv);
    }

    public int getSelectedRecipe() {
        return this.selectedRecipe.get();
    }

    public List<IRunecarvingRecipe> getRecipeList() {
        return this.recipes;
    }

    public int getRecipeListSize() {
        return this.recipes.size();
    }

    public boolean hasItemsInInputSlot() {
        return this.inputSlabSlot.hasItem() && this.inputLapisSlot.hasItem() && !this.recipes.isEmpty();
    }
    
    @Override
    public boolean stillValid(Player playerIn) {
        return stillValid(this.worldPosCallable, playerIn, BlocksPM.RUNECARVING_TABLE.get());
    }

    @Override
    public boolean clickMenuButton(Player playerIn, int id) {
        if (id >= 0 && id < this.recipes.size()) {
            this.selectedRecipe.set(id);
            this.updateRecipeResultSlot();
        }
        return true;
    }
    
    @Override
    public void containerChanged(Container inventoryIn) {
        ItemStack slabStack = this.inputSlabSlot.getItem();
        ItemStack lapisStack = this.inputLapisSlot.getItem();
        if (slabStack.getItem() != this.slabInput.getItem() || lapisStack.getItem() != this.lapisInput.getItem()) {
            this.slabInput = slabStack.copy();
            this.lapisInput = lapisStack.copy();
            this.updateAvailableRecipes(inventoryIn, slabStack, lapisStack);
        }
        this.inventoryUpdateListener.run();
    };
    
    protected void updateAvailableRecipes(Container inventoryIn, ItemStack slabStack, ItemStack lapisStack) {
        this.recipes.clear();
        this.selectedRecipe.set(-1);
        this.outputSlot.set(ItemStack.EMPTY);
        if (!slabStack.isEmpty() && !lapisStack.isEmpty()) {
            this.recipes = this.world.getRecipeManager().getRecipesFor(RecipeTypesPM.RUNECARVING.get(), inventoryIn, this.world).stream()
                    .filter(r -> r != null && (r.getRequiredResearch() == null || r.getRequiredResearch().isKnownByStrict(this.player)))
                    .collect(Collectors.toList());
        }
    }
    
    protected void updateRecipeResultSlot() {
        if (!this.recipes.isEmpty()) {
            IRunecarvingRecipe recipe = this.recipes.get(this.selectedRecipe.get());
            this.outputSlot.set(recipe.assemble(this.tableInv));
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
                slotStack.getItem().onCraftedBy(slotStack, playerIn.level, playerIn);
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
