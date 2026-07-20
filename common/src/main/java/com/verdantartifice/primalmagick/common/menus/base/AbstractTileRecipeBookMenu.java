package com.verdantartifice.primalmagick.common.menus.base;

import com.verdantartifice.primalmagick.common.capabilities.IItemHandlerPM;
import com.verdantartifice.primalmagick.common.tiles.base.AbstractTileSidedInventoryPM;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.recipebook.ServerPlaceRecipe;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Base class for a menu that serves a mod block entity with an attached sided item handler and that has a screen with a recipe book.
 *
 * @author Daedalus4096
 */
public abstract class AbstractTileRecipeBookMenu<T extends AbstractTileSidedInventoryPM & StackedContentsCompatible, I extends RecipeInput, R extends Recipe<I>> extends RecipeBookMenu implements ITileSidedInventoryMenu<T> {
    protected static final Logger LOGGER = LogManager.getLogger();

    protected final T tile;
    protected final Level level;
    protected final BlockPos tilePos;
    protected final ContainerLevelAccess containerLevelAccess;
    protected final int width;
    protected final int height;

    @SuppressWarnings("unchecked")
    public AbstractTileRecipeBookMenu(MenuType<?> menuType, int containerId, Class<T> tileClass, Level level, BlockPos tilePos, T tile, int width, int height) {
        super(menuType, containerId);
        this.level = level;
        this.tilePos = tilePos;
        this.containerLevelAccess = ContainerLevelAccess.create(level, tilePos);
        this.width = width;
        this.height = height;
        this.tile = tile != null ? tile : (tileClass.isInstance(level.getBlockEntity(tilePos)) ? (T)level.getBlockEntity(tilePos) : null);
        if (this.tile == null) {
            LOGGER.error("Block entity at {} is not of the expected type for menu", tilePos.toString());
            throw new IllegalStateException("Block entity at " + tilePos + " is not of the expected type for menu");
        }
    }

    @Override
    public IItemHandlerPM getTileInventory(Direction face) {
        return Services.CAPABILITIES.itemHandler(this.tile, face).orElseThrow(IllegalStateException::new);
    }

    @Override
    public IItemHandlerPM getTileInventory(int index) {
        IItemHandlerPM retVal = this.tile.getRawItemHandler(index);
        if (retVal == null) {
            throw new IllegalStateException("No tile inventory found for index " + index);
        } else {
            return retVal;
        }
    }

    @Override
    public T getTile() {
        return this.tile;
    }

    @Override
    public Level getLevel() {
        return this.level;
    }

    @Override
    public BlockPos getTilePos() {
        return this.tilePos;
    }

    @Override
    public ContainerLevelAccess getContainerLevelAccess() {
        return this.containerLevelAccess;
    }

    @Override
    @NotNull
    public PostPlaceAction handlePlacement(boolean useMaxItems, boolean allowDroppingItemsToClear, @NotNull RecipeHolder<?> recipe, @NotNull final ServerLevel level, @NotNull Inventory inventory) {
        @SuppressWarnings("unchecked")
        RecipeHolder<R> typedRecipe = (RecipeHolder<R>)recipe;
        this.beginPlacingRecipe();

        final List<Slot> slotsToClear = this.getSlotsToClear();

        RecipeBookMenu.PostPlaceAction action;
        try {
            action = ServerPlaceRecipe.placeRecipe(new ServerPlaceRecipe.CraftingMenuAccess<>() {
                @Override
                public void fillCraftSlotsStackedContents(@NotNull StackedItemContents stackedItemContents) {
                    AbstractTileRecipeBookMenu.this.fillCraftSlotsStackedContents(stackedItemContents);
                }

                @Override
                public void clearCraftingContent() {
                    slotsToClear.forEach(s -> s.set(ItemStack.EMPTY));
                }

                @Override
                public boolean recipeMatches(@NotNull RecipeHolder<R> recipeHolder) {
                    return recipeHolder.value().matches(AbstractTileRecipeBookMenu.this.getRecipeInputForMatch(), level);
                }
            }, this.width, this.height, this.getInputGridSlots(), slotsToClear, inventory, typedRecipe, useMaxItems, allowDroppingItemsToClear);
        } finally {
            this.finishPlacingRecipe(level, typedRecipe);
        }

        return action;
    }

    @Override
    public void fillCraftSlotsStackedContents(@NotNull StackedItemContents stackedItemContents) {
        this.tile.fillStackedContents(stackedItemContents);
    }

    @Override
    @NotNull
    public RecipeBookType getRecipeBookType() {
        return null;
    }

    public boolean stillValid(@NotNull Player pPlayer) {
        return this.tile != null && this.tile.stillValid(pPlayer);
    }

    @NotNull
    protected abstract List<Slot> getInputGridSlots();

    @NotNull
    protected abstract List<Slot> getSlotsToClear();

    @NotNull
    protected abstract I getRecipeInputForMatch();

    protected void beginPlacingRecipe() {
    }

    protected void finishPlacingRecipe(ServerLevel level, RecipeHolder<R> recipe) {
    }
}
