package com.verdantartifice.primalmagic.common.tiles.crafting;

import com.verdantartifice.primalmagic.common.blocks.crafting.RunescribingAltarBlock;
import com.verdantartifice.primalmagic.common.containers.RunescribingAltarBasicContainer;
import com.verdantartifice.primalmagic.common.containers.RunescribingAltarEnchantedContainer;
import com.verdantartifice.primalmagic.common.containers.RunescribingAltarForbiddenContainer;
import com.verdantartifice.primalmagic.common.containers.RunescribingAltarHeavenlyContainer;
import com.verdantartifice.primalmagic.common.misc.DeviceTier;
import com.verdantartifice.primalmagic.common.tiles.TileEntityTypesPM;
import com.verdantartifice.primalmagic.common.tiles.base.TilePM;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.MenuProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

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
        return new TranslatableComponent(this.getBlockState().getBlock().getDescriptionId());
    }
}
