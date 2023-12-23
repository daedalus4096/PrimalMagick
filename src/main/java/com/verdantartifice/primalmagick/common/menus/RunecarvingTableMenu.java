package com.verdantartifice.primalmagick.common.menus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.crafting.IRunecarvingRecipe;
import com.verdantartifice.primalmagick.common.crafting.RecipeTypesPM;
import com.verdantartifice.primalmagick.common.menus.base.AbstractTileSidedInventoryMenu;
import com.verdantartifice.primalmagick.common.menus.slots.FilteredSlot;
import com.verdantartifice.primalmagick.common.menus.slots.GenericResultSlot;
import com.verdantartifice.primalmagick.common.stats.StatsManager;
import com.verdantartifice.primalmagick.common.stats.StatsPM;
import com.verdantartifice.primalmagick.common.tags.ItemTagsPM;
import com.verdantartifice.primalmagick.common.tiles.crafting.RunecarvingTableTileEntity;
import com.verdantartifice.primalmagick.common.util.InventoryUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerListener;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.RecipeWrapper;

/**
 * Server data container for the runecarving table GUI.
 * 
 * @author Daedalus4096
 */
public class RunecarvingTableMenu extends AbstractTileSidedInventoryMenu<RunecarvingTableTileEntity> implements ContainerListener {
    public static final ResourceLocation BASE_SLOT_TEXTURE = PrimalMagick.resource("item/empty_slab_slot");
    public static final ResourceLocation ETCHING_SLOT_TEXTURE = PrimalMagick.resource("item/empty_lapis_slot");

    protected final DataSlot selectedRecipe = DataSlot.standalone();
    protected final Player player;
    
    protected final Slot inputSlabSlot;
    protected final Slot inputLapisSlot;
    protected final Slot outputSlot;
    protected final ResultContainer outputInventory = new ResultContainer();
    protected final Optional<RecipeWrapper> tileInvWrapper;

    protected List<RecipeHolder<IRunecarvingRecipe>> recipes = new ArrayList<>();
    
    protected ItemStack slabInput = ItemStack.EMPTY;
    protected ItemStack lapisInput = ItemStack.EMPTY;

    /**
     * Stores the game time of the last time the player took items from the the crafting result slot. This is used to
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
        this.tileInvWrapper = this.getTileInventory(Direction.UP) instanceof IItemHandlerModifiable modifiable ? Optional.of(new RecipeWrapper(modifiable)) : Optional.empty();
        this.tile.addListener(Direction.UP, this);
        
        // Slot 0: input slabs
        this.inputSlabSlot = this.addSlot(new FilteredSlot(this.getTileInventory(Direction.UP), 0, 20, 21,
                new FilteredSlot.Properties().background(BASE_SLOT_TEXTURE).tag(ItemTagsPM.RUNE_BASES)));
        
        // Slot 1: input lapis
        this.inputLapisSlot = this.addSlot(new FilteredSlot(this.getTileInventory(Direction.UP), 1, 20, 46,
                new FilteredSlot.Properties().background(ETCHING_SLOT_TEXTURE).tag(ItemTagsPM.RUNE_ETCHINGS)));
        
        // Slot 2: runecarving output
        this.outputSlot = this.addSlot(new GenericResultSlot(this.player, InventoryUtils.wrapInventory(this.outputInventory, null), 0, 143, 33) {
            @Override
            public void onTake(Player thePlayer, ItemStack stack) {
                RunecarvingTableMenu.this.getTileInventory(Direction.UP).extractItem(0, 1, false);
                RunecarvingTableMenu.this.getTileInventory(Direction.UP).extractItem(1, 1, false);
                RunecarvingTableMenu.this.updateRecipeResultSlot(thePlayer.level().registryAccess());
                
                stack.getItem().onCraftedBy(stack, thePlayer.level(), thePlayer);
                RunecarvingTableMenu.this.containerLevelAccess.execute((world, pos) -> {
                    long time = world.getGameTime();
                    if (RunecarvingTableMenu.this.lastOnTake != time) {
                        world.playSound(null, pos, SoundEvents.UI_STONECUTTER_TAKE_RESULT, SoundSource.BLOCKS, 1.0F, 1.0F);
                        RunecarvingTableMenu.this.lastOnTake = time;
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
        if (this.tileInvWrapper.isPresent()) {
            this.containerChanged(this.tileInvWrapper.get());
        }
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
            this.updateRecipeResultSlot(playerIn.level().registryAccess());
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
        this.recipes = this.level.getRecipeManager().getRecipesFor(RecipeTypesPM.RUNECARVING.get(), inventoryIn, this.level).stream()
                .filter(r -> r != null && (r.value().getRequiredResearch() == null || r.value().getRequiredResearch().isKnownByStrict(this.player)))
                .collect(Collectors.toList());
    }
    
    protected void updateRecipeResultSlot(RegistryAccess registryAccess) {
        if (!this.recipes.isEmpty() && this.tileInvWrapper.isPresent()) {
            IRunecarvingRecipe recipe = this.recipes.get(this.selectedRecipe.get()).value();
            this.outputSlot.set(recipe.assemble(this.tileInvWrapper.get(), registryAccess));
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
