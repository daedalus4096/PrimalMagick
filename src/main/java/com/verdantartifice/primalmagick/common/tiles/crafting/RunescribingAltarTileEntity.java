package com.verdantartifice.primalmagick.common.tiles.crafting;

import com.verdantartifice.primalmagick.common.blocks.crafting.RunescribingAltarBlock;
import com.verdantartifice.primalmagick.common.menus.RunescribingAltarBasicContainer;
import com.verdantartifice.primalmagick.common.menus.RunescribingAltarEnchantedContainer;
import com.verdantartifice.primalmagick.common.menus.RunescribingAltarForbiddenContainer;
import com.verdantartifice.primalmagick.common.menus.RunescribingAltarHeavenlyContainer;
import com.verdantartifice.primalmagick.common.misc.DeviceTier;
import com.verdantartifice.primalmagick.common.tiles.TileEntityTypesPM;
import com.verdantartifice.primalmagick.common.tiles.base.TilePM;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Definition of a runescribing altar tile entity.
 * 
 * @author Daedalus4096
 */
public class RunescribingAltarTileEntity extends TilePM implements MenuProvider {
    public RunescribingAltarTileEntity(BlockPos pos, BlockState state) {
        super(TileEntityTypesPM.RUNESCRIBING_ALTAR.get(), pos, state);
    }

    @Override
    public AbstractContainerMenu createMenu(int windowId, Inventory playerInv, Player player) {
        if (this.getBlockState().getBlock() instanceof RunescribingAltarBlock) {
            DeviceTier tier = ((RunescribingAltarBlock)this.getBlockState().getBlock()).getDeviceTier();
            switch (tier) {
            case BASIC:
                return new RunescribingAltarBasicContainer(windowId, playerInv);
            case ENCHANTED:
                return new RunescribingAltarEnchantedContainer(windowId, playerInv);
            case FORBIDDEN:
                return new RunescribingAltarForbiddenContainer(windowId, playerInv);
            case HEAVENLY:
                return new RunescribingAltarHeavenlyContainer(windowId, playerInv);
            default:
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable(this.getBlockState().getBlock().getDescriptionId());
    }
}
