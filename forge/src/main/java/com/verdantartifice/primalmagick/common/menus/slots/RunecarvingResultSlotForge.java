package com.verdantartifice.primalmagick.common.menus.slots;

import com.verdantartifice.primalmagick.common.menus.RunecarvingTableMenu;
import com.verdantartifice.primalmagick.common.stats.ExpertiseManager;
import com.verdantartifice.primalmagick.common.stats.StatsManager;
import com.verdantartifice.primalmagick.common.stats.StatsPM;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.IItemHandler;

public class RunecarvingResultSlotForge extends GenericResultSlotForge {
    private final RunecarvingTableMenu menu;

    public RunecarvingResultSlotForge(RunecarvingTableMenu menu, Player player, IItemHandler inventoryIn, int index, int xPosition, int yPosition) {
        super(player, inventoryIn, index, xPosition, yPosition);
        this.menu = menu;
    }

    @Override
    protected void checkTakeAchievements(ItemStack stack) {
        if (this.removeCount > 0) {
            RecipeHolder<?> recipeUsed = this.menu.getOutputInventory().getRecipeUsed();
            if (recipeUsed != null) {
                ExpertiseManager.awardExpertise(this.player, recipeUsed);
                StatsManager.incrementValue(this.player, StatsPM.CRAFTED_RUNEWORKING, this.removeCount);
            }
        }
        super.checkTakeAchievements(stack);
    }

    @Override
    public void onTake(Player thePlayer, ItemStack stack) {
        Level level = thePlayer.level();
        this.menu.getTileInventory(Direction.UP).extractItem(0, 1, false);
        this.menu.getTileInventory(Direction.UP).extractItem(1, 1, false);
        this.menu.updateRecipeResultSlot(level.registryAccess());

        stack.getItem().onCraftedBy(stack, level, thePlayer);
        this.menu.getContainerLevelAccess().execute((world, pos) -> {
            long time = world.getGameTime();
            if (this.menu.getLastOnTake() != time) {
                world.playSound(null, pos, SoundEvents.UI_STONECUTTER_TAKE_RESULT, SoundSource.BLOCKS, 1.0F, 1.0F);
                this.menu.setLastOnTake(time);
            }
        });

        super.onTake(thePlayer, stack);
    }
}
