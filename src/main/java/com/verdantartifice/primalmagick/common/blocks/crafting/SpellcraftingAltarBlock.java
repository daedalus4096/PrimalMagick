package com.verdantartifice.primalmagick.common.blocks.crafting;

import com.verdantartifice.primalmagick.common.containers.SpellcraftingAltarContainer;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;

/**
 * Block definition for the spellcrafting altar.  A spellcrafting altar allows a player to define a spell
 * from known vehicles, payloads, and mods, then scribe that spell onto a blank scroll for casting or
 * inscription onto a wand.
 * 
 * @author Daedalus4096
 */
public class SpellcraftingAltarBlock extends Block {
    public SpellcraftingAltarBlock() {
        super(Block.Properties.of(Material.STONE, MaterialColor.QUARTZ).strength(1.5F, 6.0F).sound(SoundType.STONE));
    }
    
    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (!worldIn.isClientSide && player instanceof ServerPlayer) {
            // Open the GUI for the spellcrafting altar
            NetworkHooks.openGui((ServerPlayer)player, new MenuProvider() {
                @Override
                public AbstractContainerMenu createMenu(int windowId, Inventory inv, Player player) {
                    return new SpellcraftingAltarContainer(windowId, inv, ContainerLevelAccess.create(worldIn, pos));
                }

                @Override
                public Component getDisplayName() {
                    return new TranslatableComponent(SpellcraftingAltarBlock.this.getDescriptionId());
                }
            });
        }
        return InteractionResult.SUCCESS;
    }
}
