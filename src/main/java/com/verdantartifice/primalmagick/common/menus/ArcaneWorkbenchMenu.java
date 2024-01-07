package com.verdantartifice.primalmagick.common.menus;

import java.util.Optional;

import javax.annotation.Nullable;

import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import com.verdantartifice.primalmagick.common.crafting.IArcaneRecipe;
import com.verdantartifice.primalmagick.common.crafting.RecipeTypesPM;
import com.verdantartifice.primalmagick.common.crafting.WandInventory;
import com.verdantartifice.primalmagick.common.crafting.recipe_book.ArcaneRecipeBookType;
import com.verdantartifice.primalmagick.common.menus.base.IArcaneRecipeBookMenu;
import com.verdantartifice.primalmagick.common.menus.slots.ArcaneCraftingResultSlot;
import com.verdantartifice.primalmagick.common.menus.slots.WandSlot;
import com.verdantartifice.primalmagick.common.util.InventoryUtils;
import com.verdantartifice.primalmagick.common.wands.IWand;

import net.minecraft.core.NonNullList;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.TransientCraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

/**
 * Server data container for the arcane workbench GUI.
 * 
 * @author Daedalus4096
 */
public class ArcaneWorkbenchMenu extends AbstractContainerMenu implements IArcaneRecipeBookMenu<CraftingContainer> {
    protected final CraftingContainer craftingInv = new TransientCraftingContainer(this, 3, 3);
    protected final WandInventory wandInv = new WandInventory(this);
    protected final ResultContainer resultInv = new ResultContainer();
    protected final ContainerLevelAccess worldPosCallable;
    protected final Player player;
    protected final Slot wandSlot;
    protected RecipeHolder<IArcaneRecipe> activeArcaneRecipe = null;
    
    public ArcaneWorkbenchMenu(int windowId, Inventory inv) {
        this(windowId, inv, ContainerLevelAccess.NULL);
    }
    
    public ArcaneWorkbenchMenu(int windowId, Inventory inv, ContainerLevelAccess callable) {
        super(MenuTypesPM.ARCANE_WORKBENCH.get(), windowId);
        this.worldPosCallable = callable;
        this.player = inv.player;
        
        // Slot 0: Workbench output
        this.addSlot(new ArcaneCraftingResultSlot(this.player, this.craftingInv, this.wandInv, this.resultInv, 0, 138, 52));
        
        // Slots 1-9: Crafting inputs
        int i, j;
        for (i = 0; i < 3; i++) {
            for (j = 0; j < 3; j++) {
                this.addSlot(new Slot(this.craftingInv, j + i * 3, 44 + j * 18, 34 + i * 18));
            }
        }
        
        // Slot 10: Crafting wand
        this.wandSlot = this.addSlot(new WandSlot(InventoryUtils.wrapInventory(this.wandInv, null), 0, 19, 52, false));
        
        // Slots 11-37: Player backpack
        for (i = 0; i < 3; i++) {
            for (j = 0; j < 9; j++) {
                this.addSlot(new Slot(inv, j + i * 9 + 9, 8 + j * 18, 101 + i * 18));
            }
        }
        
        // Slots 38-46: Player hotbar
        for (i = 0; i < 9; i++) {
            this.addSlot(new Slot(inv, i, 8 + i * 18, 159));
        }
    }
    
    @Nullable
    public RecipeHolder<IArcaneRecipe> getActiveArcaneRecipe() {
        return this.activeArcaneRecipe;
    }
    
    @Override
    public boolean stillValid(Player playerIn) {
        return stillValid(this.worldPosCallable, playerIn, BlocksPM.ARCANE_WORKBENCH.get());
    }
    
    @Override
    public void removed(Player playerIn) {
        // Return crafting inputs and wand to the player's inventory when GUI is closed
        super.removed(playerIn);
        this.clearContainer(playerIn, this.wandInv);
        this.clearContainer(playerIn, this.craftingInv);
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
                if (!this.moveItemStackTo(slotStack, 11, 47, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(slotStack, stack);
            } else if (index >= 11 && index < 38) {
                // If transferring from the player's backpack, put wands in the wand slot and everything else into the inputs or hotbar, in that order
                if (this.wandSlot.mayPlace(slotStack)) {
                    if (!this.moveItemStackTo(slotStack, 10, 11, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.moveItemStackTo(slotStack, 1, 10, false) && !this.moveItemStackTo(slotStack, 38, 47, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 38 && index < 47) {
                // If transferring from the player's hotbar, put wands in the wand slot and everything else into the inputs or backpack, in that order
                if (this.wandSlot.mayPlace(slotStack)) {
                    if (!this.moveItemStackTo(slotStack, 10, 11, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.moveItemStackTo(slotStack, 1, 10, false) && !this.moveItemStackTo(slotStack, 11, 38, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(slotStack, 11, 47, false)) {
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
        this.slotChangedCraftingGrid(this.player.level());
    }
    
    protected void slotChangedCraftingGrid(Level world) {
        if (world.isClientSide) {
            // Get the active recipe, if any, for client display of mana costs
            this.activeArcaneRecipe = null;
            Optional<RecipeHolder<IArcaneRecipe>> arcaneOptional = world.getRecipeManager().getRecipeFor(RecipeTypesPM.ARCANE_CRAFTING.get(), this.craftingInv, world);
            if (arcaneOptional.isPresent()) {
                RecipeHolder<IArcaneRecipe> recipe = arcaneOptional.get();
                if (recipe.value().getRequiredResearch() == null || recipe.value().getRequiredResearch().isKnownByStrict(player)) {
                    this.activeArcaneRecipe = recipe;
                }
            }
        }
        if (!world.isClientSide && this.player instanceof ServerPlayer) {
            ServerPlayer spe = (ServerPlayer)this.player;
            ItemStack stack = ItemStack.EMPTY;
            Optional<RecipeHolder<IArcaneRecipe>> arcaneOptional = world.getServer().getRecipeManager().getRecipeFor(RecipeTypesPM.ARCANE_CRAFTING.get(), this.craftingInv, world);
            if (arcaneOptional.isPresent()) {
                // If the inputs match a defined arcane recipe, show the output if the player can use it
                RecipeHolder<IArcaneRecipe> recipe = arcaneOptional.get();
                if (this.canUseArcaneRecipe(world, spe, recipe)) {
                    stack = recipe.value().assemble(this.craftingInv, world.registryAccess());
                }
            } else {
                Optional<RecipeHolder<CraftingRecipe>> vanillaOptional = world.getServer().getRecipeManager().getRecipeFor(RecipeType.CRAFTING, this.craftingInv, world);
                if (vanillaOptional.isPresent()) {
                    // If the inputs match a defined vanilla recipe, show the output if the player can use it
                    RecipeHolder<CraftingRecipe> recipe = vanillaOptional.get();
                    if (this.resultInv.setRecipeUsed(world, spe, recipe)) {
                        stack = recipe.value().assemble(this.craftingInv, world.registryAccess());
                    }
                }
            }
            
            // Send a packet to the client to update its GUI with the shown output
            this.resultInv.setItem(0, stack);
            spe.connection.send(new ClientboundContainerSetSlotPacket(this.containerId, this.incrementStateId(), 0, stack));
        }
    }
    
    protected boolean canUseArcaneRecipe(Level world, ServerPlayer player, RecipeHolder<IArcaneRecipe> recipeHolder) {
        // Players must know the correct research and the wand must have enough mana in order to use the recipe
        IArcaneRecipe recipe = recipeHolder.value();
        return this.resultInv.setRecipeUsed(world, player, recipeHolder) &&
                (recipe.getRequiredResearch() == null || recipe.getRequiredResearch().isKnownByStrict(player)) &&
                (recipe.getManaCosts().isEmpty() || this.wandContainsEnoughMana(player, recipe));
    }
    
    protected boolean wandContainsEnoughMana(Player player, IArcaneRecipe recipe) {
        ItemStack stack = this.getWand();
        if (stack == null || stack.isEmpty() || !(stack.getItem() instanceof IWand)) {
            return false;
        }
        IWand wand = (IWand)stack.getItem();
        return wand.containsRealMana(stack, player, recipe.getManaCosts());
    }

    @Override
    public void fillCraftSlotsStackedContents(StackedContents contents) {
        this.craftingInv.fillStackedContents(contents);
    }

    @Override
    public void clearCraftingContent() {
        this.craftingInv.clearContent();
        this.resultInv.clearContent();
    }

    @Override
    public boolean recipeMatches(RecipeHolder<? extends Recipe<? super CraftingContainer>> recipe) {
        return recipe.value().matches(this.craftingInv, this.player.level());
    }

    @Override
    public int getResultSlotIndex() {
        return 0;
    }

    @Override
    public int getGridWidth() {
        return this.craftingInv.getWidth();
    }

    @Override
    public int getGridHeight() {
        return this.craftingInv.getHeight();
    }

    @Override
    public int getSize() {
        return 10;
    }

    @Override
    public ArcaneRecipeBookType getRecipeBookType() {
        return ArcaneRecipeBookType.CRAFTING;
    }

    @Override
    public boolean shouldMoveToInventory(int index) {
        return index != this.getResultSlotIndex();
    }

    @Override
    public NonNullList<Slot> getSlots() {
        return this.slots;
    }
    
    public ItemStack getWand() {
        return this.wandInv.getItem(0);
    }
    
    public Player getPlayer() {
        return this.player;
    }
}
