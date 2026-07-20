package com.verdantartifice.primalmagick.common.menus;

import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import com.verdantartifice.primalmagick.common.crafting.IArcaneRecipe;
import com.verdantartifice.primalmagick.common.crafting.RecipeTypesPM;
import com.verdantartifice.primalmagick.common.crafting.WandInventory;
import com.verdantartifice.primalmagick.common.menus.slots.ArcaneCraftingResultSlot;
import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.misc.SetActiveRecipeDisplayPacket;
import com.verdantartifice.primalmagick.common.wands.IWand;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractCraftingMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.VisibleForTesting;

import java.util.List;
import java.util.Optional;

/**
 * Server data container for the arcane workbench GUI.
 * 
 * @author Daedalus4096
 */
public class ArcaneWorkbenchMenu extends AbstractCraftingMenu implements IRecipeDisplayListener {
    protected final WandInventory wandInv = new WandInventory(this);
    protected final ResultContainer resultInv = new ResultContainer();
    protected final ContainerLevelAccess access;
    protected final Player player;
    protected final Slot wandSlot;

    protected RecipeDisplay activeRecipeDisplay = null;
    private boolean placingRecipe;

    public ArcaneWorkbenchMenu(int windowId, Inventory inv) {
        this(windowId, inv, ContainerLevelAccess.NULL);
    }
    
    public ArcaneWorkbenchMenu(int windowId, Inventory inv, ContainerLevelAccess callable) {
        super(MenuTypesPM.ARCANE_WORKBENCH.get(), windowId, 3, 3);
        this.access = callable;
        this.player = inv.player;
        
        // Slot 0: Workbench output
        this.addResultSlot(this.player, 138, 52);

        // Slots 1-9: Crafting inputs
        this.addCraftingGridSlots(44, 34);

        // Slot 10: Crafting wand
        this.wandSlot = this.addSlot(Services.MENU.makeWandSlot(Services.ITEM_HANDLERS.wrap(this.wandInv, null), 0, 19, 52, false));
        
        // Slots 11-37: Player backpack
        // Slots 38-46: Player hotbar
        this.addStandardInventorySlots(inv, 8, 101);
    }

    @Override
    @NotNull
    protected Slot addResultSlot(@NotNull Player player, int x, int y) {
        return this.addSlot(new ArcaneCraftingResultSlot(player, this.craftSlots, this.wandInv, this.resultInv, 0, x, y));
    }

    @Nullable
    public RecipeDisplay getActiveRecipeDisplay() {
        return this.activeRecipeDisplay;
    }

    @Override
    public void setRecipeDisplay(@Nullable RecipeDisplay display) {
        this.activeRecipeDisplay = display;
    }

    @Override
    public boolean stillValid(@NotNull Player playerIn) {
        return stillValid(this.access, playerIn, BlocksPM.ARCANE_WORKBENCH.get());
    }
    
    @Override
    public void removed(@NotNull Player playerIn) {
        // Return crafting inputs and wand to the player's inventory when GUI is closed
        super.removed(playerIn);
        this.clearContainer(playerIn, this.wandInv);
        this.clearContainer(playerIn, this.craftSlots);
    }
    
    @Override
    @NotNull
    public ItemStack quickMoveStack(@NotNull Player playerIn, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
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
    public boolean canTakeItemForPickAll(@NotNull ItemStack stack, Slot slotIn) {
        return slotIn.container != this.resultInv && super.canTakeItemForPickAll(stack, slotIn);
    }
    
    @Override
    public void slotsChanged(@NotNull Container inventoryIn) {
        if (!this.placingRecipe) {
            this.access.execute((level, pos) -> {
                if (level instanceof ServerLevel serverLevel && this.player instanceof ServerPlayer spe) {
                    slotChangedCraftingGrid(this, serverLevel, spe, this.craftSlots, this.resultSlots);
                }
            });
        }
    }

    @Override
    public void beginPlacingRecipe() {
        this.placingRecipe = true;
    }

    @Override
    public void finishPlacingRecipe(@NotNull ServerLevel level, @NotNull RecipeHolder<CraftingRecipe> recipe) {
        this.placingRecipe = false;
        if (this.player instanceof ServerPlayer spe) {
            slotChangedCraftingGrid(this, level, spe, this.craftSlots, this.resultSlots);
        }
    }

    protected static void slotChangedCraftingGrid(ArcaneWorkbenchMenu menu, ServerLevel level, ServerPlayer player, CraftingContainer container, ResultContainer resultSlots) {
        CraftingInput input = container.asCraftInput();
        ItemStack result = ItemStack.EMPTY;
        RecipeDisplay display = null;

        Optional<RecipeHolder<IArcaneRecipe>> arcaneOptional = level.recipeAccess().getRecipeFor(RecipeTypesPM.ARCANE_CRAFTING.get(), input, level);
        if (arcaneOptional.isPresent()) {
            // If the inputs match a defined arcane recipe, show the output if the player can use it
            RecipeHolder<IArcaneRecipe> recipe = arcaneOptional.get();
            display = recipe.value().display().getFirst();
            if (menu.canUseArcaneRecipe(resultSlots, player, recipe)) {
                ItemStack recipeResult = recipe.value().assemble(input);
                if (recipeResult.isItemEnabled(level.enabledFeatures())) {
                    result = recipeResult;
                }
            }
        } else {
            Optional<RecipeHolder<CraftingRecipe>> vanillaOptional = level.recipeAccess().getRecipeFor(RecipeType.CRAFTING, input, level);
            if (vanillaOptional.isPresent()) {
                // If the inputs match a defined vanilla recipe, show the output if the player can use it
                RecipeHolder<CraftingRecipe> recipe = vanillaOptional.get();
                display = recipe.value().display().getFirst();
                if (resultSlots.setRecipeUsed(player, recipe)) {
                    ItemStack recipeResult = recipe.value().assemble(input);
                    if (recipeResult.isItemEnabled(level.enabledFeatures())) {
                        result = recipeResult;
                    }
                }
            }
        }

        resultSlots.setItem(0, result);
        menu.setRemoteSlot(0, result);
        player.connection.send(new ClientboundContainerSetSlotPacket(menu.containerId, menu.incrementStateId(), 0, result));
        PacketHandler.sendToPlayer(new SetActiveRecipeDisplayPacket(menu.containerId, display), player);
    }

    protected boolean canUseArcaneRecipe(ResultContainer resultSlots, ServerPlayer player, RecipeHolder<IArcaneRecipe> recipeHolder) {
        // Players must know the correct research and the wand must have enough mana in order to use the recipe
        IArcaneRecipe recipe = recipeHolder.value();
        return resultSlots.setRecipeUsed(player, recipeHolder) &&
                (recipe.getRequirement().isEmpty() || recipe.getRequirement().get().isMetBy(player)) &&
                (recipe.getManaCosts().isEmpty() || this.wandContainsEnoughMana(player, recipe));
    }
    
    protected boolean wandContainsEnoughMana(Player player, IArcaneRecipe recipe) {
        ItemStack stack = this.getWand();
        if (stack == null || stack.isEmpty() || !(stack.getItem() instanceof IWand wand)) {
            return false;
        }
        return wand.containsMana(stack, player, recipe.getManaCosts(), player.registryAccess());
    }

    @Override
    @NotNull
    public Slot getResultSlot() {
        return this.slots.getFirst();
    }

    @Override
    @NotNull
    public List<Slot> getInputGridSlots() {
        return this.slots.subList(1, 10);
    }

    @Override
    @NotNull
    public Player owner() {
        return this.player;
    }

    @Override
    @NotNull
    public RecipeBookType getRecipeBookType() {
        return RecipeBookType.CRAFTING;
    }

    public ItemStack getWand() {
        return this.wandInv.getItem(0);
    }

    @VisibleForTesting
    public List<Slot> getSlots() {
        return this.slots;
    }
}
